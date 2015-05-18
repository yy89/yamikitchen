package com.xiaobudian.yamikitchen.service;

import java.util.Date;
import java.util.List;

import com.xiaobudian.yamikitchen.domain.cart.Cart;
import com.xiaobudian.yamikitchen.domain.cart.Settlement;
import com.xiaobudian.yamikitchen.domain.order.Order;
import com.xiaobudian.yamikitchen.domain.order.OrderDetail;
import com.xiaobudian.yamikitchen.domain.order.OrderItem;
import com.xiaobudian.yamikitchen.web.dto.DadaResultDto;
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
     * @param orderNo
     * @return
     * @author Liuminglu
     * @Date 2015年5月13日 下午1:28:59
     */
	public Order confirmOrder(Long uid, String orderNo);

	/**
	 * 选择配送机构
	 * @param uid
	 * @param orderNo  订单编号
	 * @param deliverGroup 机构：1自己配送  2达达配送
	 * @return
	 * @author Liuminglu
	 * @Date 2015年5月18日 上午11:38:27
	 */
	public Object chooseDeliverGroup(Long uid, String orderNo, Integer deliverGroup);

	/**
	 * 达达接口回调
	 * @param dadaResultDto
	 * @return
	 * @author Liuminglu
	 * @Date 2015年5月18日 下午2:20:58
	 */
	public Order dadaCallBack(DadaResultDto dadaResultDto);

	/**
	 * 订单完成
	 * @param uid
	 * @param orderNo
	 * @return
	 * @author Liuminglu
	 * @Date 2015年5月18日 下午3:22:38
	 */
	public Order finishOrder(Long uid, String orderNo);
}
