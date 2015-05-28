package com.xiaobudian.yamikitchen.service;

import com.xiaobudian.yamikitchen.common.Day;
import com.xiaobudian.yamikitchen.common.Keys;
import com.xiaobudian.yamikitchen.domain.account.SettlementCenter;
import com.xiaobudian.yamikitchen.domain.cart.Cart;
import com.xiaobudian.yamikitchen.domain.cart.Settlement;
import com.xiaobudian.yamikitchen.domain.coupon.Coupon;
import com.xiaobudian.yamikitchen.domain.merchant.Merchant;
import com.xiaobudian.yamikitchen.domain.message.NoticeEvent;
import com.xiaobudian.yamikitchen.domain.order.*;
import com.xiaobudian.yamikitchen.repository.RedisRepository;
import com.xiaobudian.yamikitchen.repository.coupon.CouponRepository;
import com.xiaobudian.yamikitchen.repository.member.UserAddressRepository;
import com.xiaobudian.yamikitchen.repository.member.UserRepository;
import com.xiaobudian.yamikitchen.repository.merchant.MerchantRepository;
import com.xiaobudian.yamikitchen.repository.merchant.ProductRepository;
import com.xiaobudian.yamikitchen.repository.order.OrderItemRepository;
import com.xiaobudian.yamikitchen.repository.order.OrderRepository;
import com.xiaobudian.yamikitchen.service.thirdparty.dada.DadaService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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
        return orderRepository.findOrdersWithDetail(rid, OrderStatus.PROCESSING, Day.TODAY.startOfDay(), Day.TODAY.endOfDay(), pr);
    }

    @Override
    public List<OrderDetail> getTodayCompletedOrders(Long rid, int page, int pageSize) {
        PageRequest pr = new PageRequest(page, pageSize);
        return orderRepository.findOrdersWithDetail(rid, OrderStatus.SOLVED, Day.TODAY.startOfDay(), Day.TODAY.endOfDay(), pr);
    }

    @Override
    public Order createOrder(Order order) {
        Cart cart = getCart(order.getUid());
        if (cart == null) return null;
        Merchant merchant = merchantRepository.findOne(cart.getMerchantId());
        Coupon coupon = order.getCouponId() == null ? null : couponRepository.findOne(order.getCouponId());
        Order newOrder = new OrderBuilder(order).cart(cart).merchant(merchant)
                .address(userAddressRepository.findOne(order.getAddressId()))
                .user(userRepository.findOne(order.getUid())).distance(merchant)
                .coupon(coupon)
                .orderNo(oderNoGenerator.getOrderNo(order.getMerchantNo())).build();
        newOrder = orderRepository.save(newOrder);
        List<OrderItem> items = saveOrderItems(cart, newOrder.getOrderNo());
        if (newOrder.getPaymentMethod() == 1) orderPostHandler.handle(new OrderDetail(newOrder, items), coupon);
        removeCart(newOrder.getUid());
        applicationEventPublisher.publishEvent(new NoticeEvent(this, OrderStatus.from(order.getStatus()).getNotices(merchant, newOrder)));
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

    @Override
    public Settlement getSettlement(Long uid) {
        Settlement settlement = new Settlement();
        settlement.setAddress(userAddressRepository.findByUidAndIsDefaultTrue(uid));
        settlement.setPaymentMethod(1);
        Cart cart = getCart(uid);
        settlement.setCart(getCart(uid));
        List<Coupon> coupons = couponRepository.findFirstByAmountAndExpireDate(uid, cart.getTotalAmount(), new Date(), new PageRequest(0, 1));
        settlement.setCoupon(CollectionUtils.isEmpty(coupons) ? null : coupons.get(0));
        settlement.setTotalAmount(cart.getTotalAmount() - (settlement.getCoupon() == null ? 0 : coupons.get(0).getAmount() * 100));
        settlement.setDeliverDate(merchantRepository.findOne(settlement.getCart().getMerchantId()).getBusinessHours());
        return settlement;
    }

    @SuppressWarnings("deprecation")
    @Override
    public List<OrderDetail> getOrders(Long merchantId, Integer status, boolean isToday, Date lastPaymentDate) {
        Day day = isToday ? Day.TODAY : Day.TOMORROW;
        Collection<Integer> statuses = status == 0 ? OrderStatus.PENDING : Arrays.asList(status);
        if (lastPaymentDate != null)
            return orderRepository.findLatestOrders(merchantId, statuses, day.startOfDay(), day.endOfDay(), lastPaymentDate);
        return orderRepository.findOrders(merchantId, statuses, day.startOfDay(), day.endOfDay());
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
    public OrderDetail getOrdersBy(String orderNo) {
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
    public Cart changePaymentMethodOfCart(Long uid, Integer paymentMethod) {
        Cart cart = getCart(uid);
        cart.setPaymentMethod(paymentMethod);
        redisRepository.setCart(Keys.uidCartKey(uid), cart);
        return getCart(uid);
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
        settlement(newOrder);
        return newOrder;
    }

    @Override
    public Order deliverOrder(Order order) {
        order.deliver();
        return orderRepository.save(order);
    }

    @Override
    public Order cancelOrder(Order order, Long uid) {
        order.cancel();
        if (order.isRefundable()) accountService.refundOrder(order);
        if (order.payIncludeCoupon()) recoveryCoupon(order.getCouponId());
        return orderRepository.save(order);
    }

    public void recoveryCoupon(Long couponId) {
        Coupon coupon = couponRepository.findOne(couponId);
        coupon.setHasUsed(false);
        couponRepository.save(coupon);
    }

    @Override
    public Settlement changeCouponForSettlement(Long uid, Long couponId) {
        Cart cart = getCart(uid);
        Coupon coupon = couponRepository.findOne(couponId);
        Long amt = cart.getTotalAmount() - (coupon == null ? 0 : coupon.getAmount() * 100);
        return new Settlement(coupon, 1, amt);
    }

    public static void main(String[] args) {
        System.out.println(new Date().getTime());
    }
}
