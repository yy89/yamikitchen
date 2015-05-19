package com.xiaobudian.yamikitchen.service;

import com.xiaobudian.yamikitchen.domain.coupon.Coupon;
import com.xiaobudian.yamikitchen.domain.coupon.CouponSummary;

import java.util.List;

/**
 * Created by Johnson on 2015/5/3.
 */
public interface CouponService {
    public List<Coupon> getCoupons();

    public List<CouponSummary> getCouponHistories();

}
