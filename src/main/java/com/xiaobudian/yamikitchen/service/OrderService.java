package com.xiaobudian.yamikitchen.service;

import java.util.Date;
import java.util.List;

import com.xiaobudian.yamikitchen.domain.cart.Cart;
import com.xiaobudian.yamikitchen.domain.cart.Settlement;
import com.xiaobudian.yamikitchen.domain.order.Order;
import com.xiaobudian.yamikitchen.domain.order.OrderDetail;
import com.xiaobudian.yamikitchen.domain.order.OrderItem;
import com.xiaobudian.yamikitchen.web.dto.OrderRequest;

/**
 * Created by johnson1 on 4/27/15.
 */
public interface OrderService {
    public Cart addProductInCart(Long uid, Long rid, Long productId);

    public Cart removeProductInCart(Long id, Long rid, Long productId);

    public Cart getCart(Long id);

    public List<OrderItem> getItemsInOrder(String orderNo);

    public boolean removeCart(Long id);

    public List<Order> getTodayPandingOrdersBy(int page, int pageSize, long rid);

    public List<Order> getTodayCompletedOrdersBy(int page, int pageSize, long rid);

    public Order initOrder(OrderRequest orderRequest);

    public Order createOrder(Order order);

    public List<Order> getOrders(Long id);

    public Settlement getSettlement(Long uid);
    
    /**
     * 查询商户待确认的订单列表
     * @param uid
     * @param createDate 根据创建时间增量查询
     * @return
     * @author Liuminglu
     * @Date 2015年5月14日 上午11:55:43
     */
    public List<OrderDetail> getUnconfirmedOrders(Long uid, Date createDate);

    /**
     * 商户确认订单
     * @param uid
     * @param orderId
     * @return
     * @author Liuminglu
     * @Date 2015年5月13日 下午1:28:59
     */
	public Order confirmOrder(Long uid, Long orderId);

	public Object chooseDeliverGroup(Long id, Long orderId, Integer deliverGroup);
}
