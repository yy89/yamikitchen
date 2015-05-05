package com.xiaobudian.yamikitchen.repository;

import com.xiaobudian.yamikitchen.domain.coupon.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Johnson on 2015/5/3.
 */
public interface CouponRepository extends JpaRepository<Coupon, Long> {
  //  public List<Coupon> findByUid(Long uid);
}
