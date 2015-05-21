package com.xiaobudian.yamikitchen.service;

import java.util.Date;
import java.util.List;

import com.xiaobudian.yamikitchen.domain.cart.Cart;
import com.xiaobudian.yamikitchen.domain.cart.Settlement;
import com.xiaobudian.yamikitchen.domain.order.Order;
import com.xiaobudian.yamikitchen.domain.order.OrderDetail;
import com.xiaobudian.yamikitchen.domain.order.OrderItem;
import com.xiaobudian.yamikitchen.web.dto.OrderDetailResponse;

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

    public OrderDetailResponse getOrdersByCondition(Long uid, Integer status, boolean isToday, Date lastOrderCreateDate);

    public Order confirmOrder(Order order);

    public void settlement(Order order);

    public OrderDetail getOrdersBy(String orderNo);

    public List<Order> getOrdersForLastMonth(Long uid);

    public List<Order> getInProgressOrders(Long uid);

    public List<Order> getWaitForCommentOrders(Long uid);

    public Cart changeDeliverMethodOfCart(Long uid, Integer deliverMethod);

    public Cart changePaymentMethodOfCart(Long uid, Integer paymentMethod);

    public Order getOrder(Long orderId);

    public Order chooseDeliverGroup(Order order, Integer deliverGroup);

	public Order finishOrder(Order order);

}

