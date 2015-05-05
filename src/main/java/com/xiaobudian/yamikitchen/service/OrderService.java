package com.xiaobudian.yamikitchen.service;

import com.xiaobudian.yamikitchen.domain.order.Order;
import com.xiaobudian.yamikitchen.domain.order.OrderItem;
import com.xiaobudian.yamikitchen.domain.cart.Cart;
import com.xiaobudian.yamikitchen.domain.cart.Settlement;
import com.xiaobudian.yamikitchen.web.dto.OrderRequest;

import java.util.Date;
import java.util.List;

/**
 * Created by johnson1 on 4/27/15.
 */
public interface OrderService {
    public Cart addProductInCart(Long uid, Long rid, Long productId);

    public Cart removeProductInCart(Long id, Long rid, Long productId);

    public Cart getCart(Long id);

    public List<Order> getOrdersByMerchantIdAndStatusAndCreateDateBetween(int page, int pageSize, long rid, int status, Date dateFrom, Date dateTo);

    public List<OrderItem> getItemsInOrder(String orderNo);

    public boolean removeCart(Long id);

    public List<Order> getTodayHandingOrdersBy(int page, int pageSize, long rid);

    public List<Order> getTodaySolvedOrdersBy(int page, int pageSize, long rid);

    public Order initOrder(OrderRequest orderRequest);

    public Order createOrder(Order order);

    public List<Order> getOrders(Long id);

    public Settlement getSettlement(Long uid);
}
