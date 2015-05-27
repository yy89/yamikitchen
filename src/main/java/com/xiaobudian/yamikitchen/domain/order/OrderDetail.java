package com.xiaobudian.yamikitchen.domain.order;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnson1 on 5/11/15.
 */
public class OrderDetail {
    private Order order;
    private List<OrderItem> items = new ArrayList<>();

    public OrderDetail() {
    }

    public OrderDetail(Order order, List<OrderItem> items) {
        this.order = order;
        this.items = items;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
}
