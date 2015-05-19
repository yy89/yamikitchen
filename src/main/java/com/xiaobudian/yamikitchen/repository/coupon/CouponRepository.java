package com.xiaobudian.yamikitchen.repository.coupon;

import com.xiaobudian.yamikitchen.domain.coupon.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Johnson on 2015/5/3.
 */
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    public Coupon findFirstByUid(Long uid);
}
