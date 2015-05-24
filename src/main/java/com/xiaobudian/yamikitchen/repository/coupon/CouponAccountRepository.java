package com.xiaobudian.yamikitchen.repository.coupon;

import com.xiaobudian.yamikitchen.domain.coupon.CouponAccount;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Johnson on 2015/5/23.
 */
public interface CouponAccountRepository extends JpaRepository<CouponAccount, Long> {
    public CouponAccount findByMobile(String mobile);
}
