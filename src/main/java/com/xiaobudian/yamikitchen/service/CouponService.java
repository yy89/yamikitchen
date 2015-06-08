package com.xiaobudian.yamikitchen.service;

import com.xiaobudian.yamikitchen.domain.coupon.*;

import java.util.List;

/**
 * Created by Johnson on 2015/5/3.
 */
public interface CouponService {
    public List<Coupon> getCoupons(Long uid);

    public List<Coupon> getAvailableCoupons(Long uid);

    public List<Coupon> getExpiredCoupons(Long uid);

    public List<CouponSummary> getCouponHistories();

    public Coupon getCouponBy(Long uid, Double price);

    public Coupon getCoupon(Long couponId);

    public Coupon addCoupon(String mobile, Long type);

    public List<OrderCoupon> getOrderCoupons(String orderNo);

    public CouponAccount createCouponAccount(String mobile);

    public OrderCouponDetail dispatchCoupon(String mobile, String orderNo);

}
