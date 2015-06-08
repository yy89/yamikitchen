package com.xiaobudian.yamikitchen.domain.order;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnson1 on 5/11/15.
 */
public class OrderDetail {
    private Order order;
    private List<OrderItem> items = new ArrayList<>();
    private String shareUrl;
    private Integer couponCount;

    public OrderDetail() {
    }

    public OrderDetail(Order order, List<OrderItem> items) {
        this.order = order;
        this.items = items;
    }

    public OrderDetail(Order order, List<OrderItem> items, String shareUrl, Integer couponCount) {
        this(order, items);
        this.shareUrl = shareUrl;
        this.couponCount = couponCount;
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

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public Integer getCouponCount() {
        return couponCount;
    }

    public void setCouponCount(Integer couponCount) {
        this.couponCount = couponCount;
    }
}
