package com.xiaobudian.yamikitchen.repository;

import com.xiaobudian.yamikitchen.domain.coupon.CouponSummary;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Johnson on 2015/5/3.
 */
public interface CouponHistoryRepository extends JpaRepository<CouponSummary, Long> {
}
