package com.xiaobudian.yamikitchen.service;

import com.xiaobudian.yamikitchen.domain.Coupon;
import com.xiaobudian.yamikitchen.domain.CouponSummary;
import com.xiaobudian.yamikitchen.repository.CouponHistoryRepository;
import com.xiaobudian.yamikitchen.repository.CouponRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by Johnson on 2015/5/3.
 */
@Service(value = "couponService")
public class CouponServiceImpl implements CouponService {
    @Inject
    private CouponRepository couponRepository;
    @Inject
    private CouponHistoryRepository couponHistoryRepository;

    @Override
    public List<Coupon> getCoupons() {
        return couponRepository.findAll();
    }

    @Override
    public List<CouponSummary> getCouponHistories() {
        return couponHistoryRepository.findAll();
    }
}
