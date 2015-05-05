package com.xiaobudian.yamikitchen.web.dto;

import com.xiaobudian.yamikitchen.domain.order.Order;
import com.xiaobudian.yamikitchen.domain.order.OrderItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hackcoder on 2015/4/29.
 */
public class OrderResponse {
    private Order order;
    private List<OrderItem> orderItems = new ArrayList<>();

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }


    public static class Builder {
        private OrderResponse response;

        public Builder() {
            this.response = new OrderResponse();
        }
        public  Builder order(Order order){
            this.response.setOrder(order);
            return this;
        }
        public Builder orderItems(List<OrderItem> orderItems){
            this.response.setOrderItems(orderItems);
            return this;
        }

        public OrderResponse build(){
            return this.response;
        }
    }
}
