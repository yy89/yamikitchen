package com.xiaobudian.yamikitchen.service;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.xiaobudian.yamikitchen.common.Day;
import com.xiaobudian.yamikitchen.common.Keys;
import com.xiaobudian.yamikitchen.domain.cart.Cart;
import com.xiaobudian.yamikitchen.domain.cart.Settlement;
import com.xiaobudian.yamikitchen.domain.merchant.Merchant;
import com.xiaobudian.yamikitchen.domain.message.NoticeEvent;
import com.xiaobudian.yamikitchen.domain.order.AsyncPostHandler;
import com.xiaobudian.yamikitchen.domain.order.Order;
import com.xiaobudian.yamikitchen.domain.order.OrderBuilder;
import com.xiaobudian.yamikitchen.domain.order.OrderDetail;
import com.xiaobudian.yamikitchen.domain.order.OrderItem;
import com.xiaobudian.yamikitchen.domain.order.OrderNoGenerator;
import com.xiaobudian.yamikitchen.domain.order.OrderStatus;
import com.xiaobudian.yamikitchen.repository.RedisRepository;
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
    AsyncPostHandler asyncPostHandler;
    @Inject
    DadaService dadaService;
    private ApplicationEventPublisher applicationEventPublisher;


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
    public List<OrderItem> getItemsInOrder(String orderNo) {
        return orderItemRepository.findByOrderNo(orderNo);
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
        Order newOrder = new OrderBuilder(order).cart(cart).merchant(merchant)
                .address(userAddressRepository.findOne(order.getAddressId()))
                .user(userRepository.findOne(order.getUid())).distance(merchant)
                .orderNo(oderNoGenerator.getOrderNo(order.getMerchantNo())).build();
        newOrder = orderRepository.save(newOrder);
        List<OrderItem> items = saveOrderItems(cart, newOrder.getOrderNo());
        if (newOrder.getPaymentMethod() == 1)
            asyncPostHandler.handle(newOrder, items);
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
        settlement.setCart(getCart(uid));
        settlement.setCoupon(couponRepository.findFirstByUid(uid));
        settlement.setDeliverDate(merchantRepository.findOne(settlement.getCart().getMerchantId()).getBusinessHours());
        return settlement;
    }

    @Override
    public List<OrderDetail> getUnconfirmedOrders(Long uid, Date createDate) {
        return createDate == null ? orderRepository.findUnconfirmedOrders(uid) : orderRepository.findUnconfirmedOrders(uid, createDate);
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
        cart.setDeliverMethod(paymentMethod);
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
        if (deliverGroup == 2) {
        	dadaService.addOrderToDada(order);
        }
        order.setStatus(4);
        order.setOutDate(new Date());
        return orderRepository.save(order);
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
