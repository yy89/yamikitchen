package com.xiaobudian.yamikitchen.service;

import com.xiaobudian.yamikitchen.domain.cart.Cart;
import com.xiaobudian.yamikitchen.domain.cart.Settlement;
import com.xiaobudian.yamikitchen.domain.order.Order;
import com.xiaobudian.yamikitchen.domain.order.OrderDetail;

import java.util.Date;
import java.util.List;

/**
 * Created by johnson1 on 4/27/15.
 */
public interface OrderService {
    public Cart addProductInCart(Long uid, Long rid, Long productId, boolean isToday);

    public Cart removeProductInCart(Long id, Long rid, Long productId);

    public Cart getCart(Long id);

    public boolean removeCart(Long id);

    public List<OrderDetail> getTodayPendingOrders(Long rid, int page, int pageSize);

    public List<OrderDetail> getTodayCompletedOrders(Long rid, int page, int pageSize);

    public Order createOrder(Order order);

    public List<Order> getOrders(Long id);

    public Settlement getSettlement(Long uid);

    public List<OrderDetail> getOrders(Long merchantId, Integer status, Date paymentDate);

    public Order confirmOrder(Order order);

    public void settlement(Order order);

    public OrderDetail getOrderBy(String orderNo);

    public List<Order> getOrdersForLastMonth(Long uid);

    public List<Order> getInProgressOrders(Long uid);

    public List<Order> getWaitForCommentOrders(Long uid);

    public Cart changeDeliverMethodOfCart(Long uid, Integer deliverMethod);

    public Settlement changePaymentMethodOfCart(Long uid, Integer paymentMethod);

    public Order getOrder(Long orderId);

    public Order chooseDeliverGroup(Order order, Integer deliverGroup);

    public Order finishOrder(Order order);

    public Order deliverOrder(Order order);

    public Order cancelOrder(Order order, Long uid);

    public void recoveryCoupon(Long couponId);

    public Settlement changeCouponForSettlement(Long uid, Long couponId);

}

