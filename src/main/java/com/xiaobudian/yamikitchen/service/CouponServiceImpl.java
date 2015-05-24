package com.xiaobudian.yamikitchen.service;

import com.xiaobudian.yamikitchen.common.Day;
import com.xiaobudian.yamikitchen.domain.coupon.Coupon;
import com.xiaobudian.yamikitchen.domain.coupon.CouponSummary;
import com.xiaobudian.yamikitchen.repository.coupon.CouponHistoryRepository;
import com.xiaobudian.yamikitchen.repository.coupon.CouponRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Johnson on 2015/5/3.
 */
@Service(value = "couponService")
@Transactional
public class CouponServiceImpl implements CouponService {
    @Inject
    private CouponRepository couponRepository;
    @Inject
    private CouponHistoryRepository couponHistoryRepository;

    @Override
    public List<Coupon> getCoupons(Long uid) {

        return couponRepository.findAll();
    }

    @Override
    public List<Coupon> getAvailableCoupons(Long uid) {
        return couponRepository.findAvailableCoupons(uid, Day.TODAY.getDate());
    }

    @Override
    public List<Coupon> getExpiredCoupons(Long uid) {
        return couponRepository.findExpiredCoupons(uid, Day.TODAY.getDate());
    }

    @Override
    public List<CouponSummary> getCouponHistories() {
        return couponHistoryRepository.findAll();
    }
}
