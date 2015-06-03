package com.xiaobudian.yamikitchen.domain.cart;

import com.xiaobudian.yamikitchen.domain.coupon.Coupon;
import com.xiaobudian.yamikitchen.domain.merchant.UserAddress;

import java.math.BigDecimal;

/**
 * Created by johnson1 on 5/4/15.
 */
public class Settlement {
    private UserAddress address;
    private String deliverDate;
    private Cart cart;
    private Coupon coupon;
    private Integer paymentMethod;
    private Double totalAmount = 0.00d;

    public Settlement() {
    }

    public Settlement(Coupon coupon, Integer paymentMethod, Double totalAmount) {
        this();
        this.coupon = coupon;
        this.paymentMethod = paymentMethod;
        this.totalAmount = totalAmount;
    }

    public Settlement(Cart cart) {
        this.cart = cart;
        this.totalAmount = cart.getTotalAmount();
        this.paymentMethod = cart.getPaymentMethod();
    }

    public UserAddress getAddress() {
        return address;
    }

    public void setAddress(UserAddress address) {
        this.address = address;
    }

    public String getDeliverDate() {
        return deliverDate;
    }

    public void setDeliverDate(String deliverDate) {
        this.deliverDate = deliverDate;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
        if (coupon == null) return;
        this.totalAmount -= coupon.getAmount();
        cart.setCouponId(coupon.getId());
    }

    public Double getTotalAmount() {
        return new BigDecimal(totalAmount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(Integer paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
