package com.xiaobudian.yamikitchen.web.dto;

import java.util.List;

import com.xiaobudian.yamikitchen.domain.order.OrderDetail;

/**
 * Created by Liuminglu on 2015/5/20.
 */
public class OrderDetailResponse {
	
    private List<OrderDetail> orderDetails;
    
    // 是否有新订单
    private boolean haveNewOrder = false;
    
	public List<OrderDetail> getOrderDetails() {
		return orderDetails;
	}
	public void setOrderDetails(List<OrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}
	public boolean isHaveNewOrder() {
		return haveNewOrder;
	}
	public void setHaveNewOrder(boolean haveNewOrder) {
		this.haveNewOrder = haveNewOrder;
	}
	
}
