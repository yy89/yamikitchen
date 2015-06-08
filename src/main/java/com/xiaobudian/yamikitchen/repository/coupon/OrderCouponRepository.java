package com.xiaobudian.yamikitchen.repository.coupon;

import com.xiaobudian.yamikitchen.domain.coupon.OrderCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Johnson on 2015/6/1.
 */
public interface OrderCouponRepository extends JpaRepository<OrderCoupon, Long> {
    @Query("from OrderCoupon oc where oc.orderNo = ?1 order by oc.receivedDate desc")
    public List<OrderCoupon> findByOrderNo(String orderNo);
}
