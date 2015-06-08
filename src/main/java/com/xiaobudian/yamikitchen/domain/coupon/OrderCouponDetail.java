package com.xiaobudian.yamikitchen.domain.coupon;

import java.util.List;

/**
 * Created by Johnson on 2015/6/3.
 */
public class OrderCouponDetail {
    private double amount = 0.00d;
    private boolean received = false;
    private List<OrderCoupon> coupons;

    public OrderCouponDetail() {
    }

    public OrderCouponDetail(List<OrderCoupon> coupons) {
        this.coupons = coupons;
    }

    public OrderCouponDetail(double amount, boolean received, List<OrderCoupon> coupons) {
        this();
        this.amount = amount;
        this.received = received;
        this.coupons = coupons;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isReceived() {
        return received;
    }

    public void setReceived(boolean received) {
        this.received = received;
    }

    public List<OrderCoupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<OrderCoupon> coupons) {
        this.coupons = coupons;
    }
}
