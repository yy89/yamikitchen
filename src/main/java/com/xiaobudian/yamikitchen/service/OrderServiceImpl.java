package com.xiaobudian.yamikitchen.service;

import static com.xiaobudian.yamikitchen.domain.order.QueueScheduler.DELIVER_QUEUE_ESCAPE_TIME;
import static com.xiaobudian.yamikitchen.domain.order.QueueScheduler.UNPAID_QUEUE_ESCAPE_TIME;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.xiaobudian.yamikitchen.common.Day;
import com.xiaobudian.yamikitchen.common.Keys;
import com.xiaobudian.yamikitchen.domain.account.AlipayHistory;
import com.xiaobudian.yamikitchen.domain.account.RefundForAlipay;
import com.xiaobudian.yamikitchen.domain.account.SettlementCenter;
import com.xiaobudian.yamikitchen.domain.cart.Cart;
import com.xiaobudian.yamikitchen.domain.cart.Settlement;
import com.xiaobudian.yamikitchen.domain.coupon.Coupon;
import com.xiaobudian.yamikitchen.domain.merchant.Merchant;
import com.xiaobudian.yamikitchen.domain.message.NoticeEvent;
import com.xiaobudian.yamikitchen.domain.order.Order;
import com.xiaobudian.yamikitchen.domain.order.OrderBuilder;
import com.xiaobudian.yamikitchen.domain.order.OrderDetail;
import com.xiaobudian.yamikitchen.domain.order.OrderItem;
import com.xiaobudian.yamikitchen.domain.order.OrderNoGenerator;
import com.xiaobudian.yamikitchen.domain.order.OrderPostHandler;
import com.xiaobudian.yamikitchen.domain.order.OrderStatus;
import com.xiaobudian.yamikitchen.domain.order.QueueScheduler;
import com.xiaobudian.yamikitchen.repository.RedisRepository;
import com.xiaobudian.yamikitchen.repository.account.AlipayHistoryRepository;
import com.xiaobudian.yamikitchen.repository.account.RefundForAlipayRepository;
import com.xiaobudian.yamikitchen.repository.coupon.CouponRepository;
import com.xiaobudian.yamikitchen.repository.member.UserAddressRepository;
import com.xiaobudian.yamikitchen.repository.member.UserRepository;
import com.xiaobudian.yamikitchen.repository.merchant.MerchantRepository;
import com.xiaobudian.yamikitchen.repository.merchant.ProductRepository;
import com.xiaobudian.yamikitchen.repository.order.OrderItemRepository;
import com.xiaobudian.yamikitchen.repository.order.OrderRepository;
import com.xiaobudian.yamikitchen.service.thirdparty.dada.DadaService;

/**
 * Created by johnson1 on 4/28/15.
 */
@Service(value = "orderService")
@Transactional
public class OrderServiceImpl implements OrderService, ApplicationEventPublisherAware {
    @Value(value = "${extra.field.name}")
    private String extraFieldName;
    @Value(value = "${extra.deliver.price}")
    private String deliverPrice;
    @Inject
    private RedisRepository redisRepository;
    @Inject
    private ProductRepository productRepository;
    @Inject
    private MerchantRepository merchantRepository;
    @Inject
    private OrderRepository orderRepository;
    @Inject
    private OrderItemRepository orderItemRepository;
    @Inject
    private UserAddressRepository userAddressRepository;
    @Inject
    private CouponRepository couponRepository;
    @Inject
    private OrderNoGenerator oderNoGenerator;
    @Inject
    private UserRepository userRepository;
    @Inject
    private OrderPostHandler orderPostHandler;
    private ApplicationEventPublisher applicationEventPublisher;
    @Inject
    private DadaService dadaService;
    @Inject
    private SettlementCenter settlementCenter;
    @Inject
    private AccountService accountService;
    @Inject
    private QueueScheduler queueScheduler;
    @Inject
    private AlipayHistoryRepository alipayHistoryRepository;
    @Inject
    private RefundForAlipayRepository refundForAlipayRepository;

    @Override
    public Cart addProductInCart(Long uid, Long rid, Long productId, boolean isToday) {
        Cart cart = getCart(uid);
        if (cart == null) cart = new Cart(uid, merchantRepository.findOne(rid), extraFieldName, deliverPrice, isToday);
        cart.addItem(new OrderItem(productRepository.findOne(productId), 1));
        redisRepository.setCart(Keys.uidCartKey(uid), cart);
        return getCart(uid);
    }

    @Override
    public Cart removeProductInCart(Long uid, Long rid, Long productId) {
        Cart cart = getCart(uid);
        cart.removeItem(productId);
        redisRepository.setCart(Keys.uidCartKey(uid), cart);
        return getCart(uid);
    }

    @Override
    public Cart getCart(Long uid) {
        return redisRepository.getCart(Keys.uidCartKey(uid));
    }

    @Override
    public boolean removeCart(Long uid) {
        redisRepository.removeKey(Keys.uidCartKey(uid));
        return true;
    }


    @Override
    public List<OrderDetail> getTodayPendingOrders(Long rid, int page, int pageSize) {
        PageRequest pr = new PageRequest(page, pageSize);
        return getOrderDetails(orderRepository.findOrders(rid, OrderStatus.PROCESSING, Day.TODAY.startOfDay(), Day.TODAY.endOfDay(), pr));
    }

    @Override
    public List<OrderDetail> getTodayCompletedOrders(Long rid, int page, int pageSize) {
        PageRequest pr = new PageRequest(page, pageSize);
        return getOrderDetails(orderRepository.findOrders(rid, OrderStatus.SOLVED, Day.TODAY.startOfDay(), Day.TODAY.endOfDay(), pr));
    }

    @Override
    public Order createOrder(Order order) {
        Cart cart = getCart(order.getUid());
        if (cart == null) return null;
        Merchant merchant = merchantRepository.findOne(cart.getMerchantId());
        Coupon coupon = cart.getCouponId() == null ? null : couponRepository.findOne(cart.getCouponId());
        Order newOrder = new OrderBuilder(order).cart(cart).merchant(merchant)
                .address(userAddressRepository.findOne(order.getAddressId()))
                .user(userRepository.findOne(order.getUid())).distance(merchant)
                .coupon(coupon)
                .orderNo(oderNoGenerator.getOrderNo(order.getMerchantNo())).build();
        newOrder = orderRepository.save(newOrder);
        List<OrderItem> items = saveOrderItems(cart, newOrder.getOrderNo());
        if (newOrder.getPaymentMethod() == 1) {
            orderPostHandler.handle(new OrderDetail(newOrder, items), coupon);
            newOrder.setPaymentDate(DateTime.now().toDate());
        }
        removeCart(newOrder.getUid());
        applicationEventPublisher.publishEvent(new NoticeEvent(this, OrderStatus.from(order.getStatus()).getNotices(merchant, newOrder)));
        queueScheduler.put(Keys.unPaidQueue(order.getId()), order.getId(), UNPAID_QUEUE_ESCAPE_TIME);
        return newOrder;
    }

    private List<OrderItem> saveOrderItems(Cart cart, String orderNo) {
        for (OrderItem item : cart.getItems()) {
            item.setOrderNo(orderNo);
        }
        return orderItemRepository.save(cart.getItems());
    }

    @Override
    public List<Order> getOrders(Long uid) {
        return orderRepository.findByUid(uid);
    }

    private Coupon getCoupon(Cart cart) {
        if (cart.getPaymentMethod() == 1) return null;
        if (cart.getCouponId() != null) return couponRepository.findOne(cart.getCouponId());
        List<Coupon> coupons = couponRepository.findFirstByAmountAndExpireDate(cart.getUid(), cart.getTotalAmount(), new Date(), new PageRequest(0, 1));
        return CollectionUtils.isEmpty(coupons) ? null : coupons.get(0);
    }

    @Override
    public Settlement getSettlement(Long uid) {
        Settlement settlement = new Settlement(getCart(uid));
        settlement.setAddress(userAddressRepository.findByUidAndIsDefaultTrue(uid));
        settlement.setDeliverDate(merchantRepository.findOne(settlement.getCart().getMerchantId()).getBusinessHours());
        settlement.setCoupon(getCoupon(settlement.getCart()));
        redisRepository.setCart(Keys.uidCartKey(uid), settlement.getCart());
        return settlement;
    }

    @Override
    public List<OrderDetail> getOrders(Long merchantId, Integer status, Date paymentDate) {
        Collection<Integer> statuses = status == 0 ? OrderStatus.PENDING : Arrays.asList(status);
        if (paymentDate != null)
            return getOrderDetails(orderRepository.findByPaymentDate(merchantId, statuses, paymentDate));
        return getOrderDetails(orderRepository.findOrders(merchantId, statuses));
    }

    private List<OrderDetail> getOrderDetails(List<Order> orders) {
        List<OrderDetail> result = new ArrayList<>();
        for (Order order : orders) {
            result.add(new OrderDetail(order, orderItemRepository.findByOrderNo(order.getOrderNo())));
        }
        return result;
    }

    @Override
    public Order confirmOrder(Order order) {
        order.confirm();
        Order newOrder = orderRepository.save(order);
        Merchant merchant = merchantRepository.findOne(order.getMerchantId());
        applicationEventPublisher.publishEvent(new NoticeEvent(this, OrderStatus.from(order.getStatus()).getNotices(merchant, newOrder)));
        return newOrder;
    }

    @Override
    public void settlement(Order order) {
        settlementCenter.settle(order);
    }

    @Override
    public OrderDetail getOrderBy(String orderNo) {
        Order order = orderRepository.findByOrderNo(orderNo);
        List<OrderItem> items = orderItemRepository.findByOrderNo(orderNo);
        return new OrderDetail(order, items);
    }

    @Override
    public List<Order> getOrdersForLastMonth(Long uid) {
        Date previousMonth = DateTime.now().plusMonths(-1).toDate();
        return orderRepository.findByUidAndCreateDateAfter(uid, previousMonth);
    }

    @Override
    public List<Order> getInProgressOrders(Long uid) {
        return orderRepository.findByUidAndStatusIn(uid, OrderStatus.IN_PROGRESS);
    }

    @Override
    public List<Order> getWaitForCommentOrders(Long uid) {
        return orderRepository.findByUidAndStatusAndCommentableTrue(uid, OrderStatus.COMPLETED.getIndex());
    }

    @Override
    public Cart changeDeliverMethodOfCart(Long uid, Integer deliverMethod) {
        Cart cart = getCart(uid);
        cart.setDeliverMethod(deliverMethod);
        redisRepository.setCart(Keys.uidCartKey(uid), cart);
        return getCart(uid);
    }

    @Override
    public Settlement changePaymentMethodOfCart(Long uid, Integer paymentMethod) {
        Cart cart = getCart(uid);
        cart.setPaymentMethod(paymentMethod);
        cart.setCouponId(null);
        redisRepository.setCart(Keys.uidCartKey(uid), cart);
        return getSettlement(uid);
    }

    @Override
    public Order getOrder(Long orderId) {
        return orderRepository.findOne(orderId);
    }

    @Override
    public Order chooseDeliverGroup(Order order, Integer deliverGroup) {
        order.setDeliverGroup(deliverGroup);
        if (order.deliverByDaDa()) dadaService.addOrderToDada(order);
        order.setStatus(3);
        return orderRepository.save(order);
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public Order finishOrder(Order order) {
        order.finish();
        Order newOrder = orderRepository.save(order);
        if (order.getPaymentMethod() == 1) return newOrder;
        settlement(newOrder);
        return newOrder;
    }

    @Override
    public Order deliverOrder(Order order) {
        order.deliver();
        Order newOrder = orderRepository.save(order);
        queueScheduler.put(Keys.deliveringQueue(order.getId()), order.getId(), DELIVER_QUEUE_ESCAPE_TIME);
        return newOrder;
    }

    @Override
    public Order cancelOrder(Order order, Long uid) {
        order.cancel();
        if (order.isRefundable() && !order.isPayOnDeliver()) {
        	accountService.refundOrder(order);
        	saveRefundApplication(order);
        }
        if (order.payIncludeCoupon()) recoveryCoupon(order.getCouponId());
        return orderRepository.save(order);
    }
    
    private void saveRefundApplication(Order order) {
        RefundForAlipay refundForAlipay = new RefundForAlipay();
        refundForAlipay.setOrderNo(order.getOrderNo());
        refundForAlipay.setUid(order.getUid());
        List<AlipayHistory> alipayHistoryList = alipayHistoryRepository.findByOrderNo(order.getOrderNo());
        if (CollectionUtils.isEmpty(alipayHistoryList)) {
            return;
        }
        AlipayHistory alipayHistory = alipayHistoryList.get(0);
        refundForAlipay.setPrice(Double.parseDouble(alipayHistory.getPrice()));
        refundForAlipay.setTradeNo(alipayHistory.getTrade_no());
        refundForAlipayRepository.save(refundForAlipay);
    }

    public void recoveryCoupon(Long couponId) {
        Coupon coupon = couponRepository.findOne(couponId);
        coupon.setHasUsed(false);
        couponRepository.save(coupon);
    }

    @Override
    public Settlement changeCouponForSettlement(Long uid, Long couponId) {
        Cart cart = getCart(uid);
        cart.setCouponId(couponId);
        redisRepository.setCart(Keys.uidCartKey(uid), cart);
        Coupon coupon = couponRepository.findOne(couponId);
        Double amt = cart.getTotalAmount() - (coupon == null ? 0 : coupon.getAmount());
        return new Settlement(coupon, 1, amt);
    }
}
