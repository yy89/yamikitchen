package com.xiaobudian.yamikitchen.domain.order;

import com.xiaobudian.yamikitchen.domain.coupon.Coupon;
import com.xiaobudian.yamikitchen.domain.merchant.Product;
import com.xiaobudian.yamikitchen.repository.coupon.CouponRepository;
import com.xiaobudian.yamikitchen.repository.merchant.ProductRepository;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * Created by Johnson on 2015/5/15.
 */
@Component(value = "orderPostHandler")
public class OrderPostHandler {
    @Inject
    private ProductRepository productRepository;
    @Inject
    private CouponRepository couponRepository;

    private void handleCoupon(Coupon coupon) {
        if (coupon == null) return;
        coupon.setLocked(true);
        couponRepository.save(coupon);
    }

    public void handle(OrderDetail orderDetail, Coupon coupon) {
        handleCoupon(coupon);
        for (OrderItem item : orderDetail.getItems()) {
            Product p = productRepository.findOne(item.getProductId());
            p.setSoldCount(p.getSoldCount() + item.getQuantity());
            if (orderDetail.getOrder().isToday()) p.setRestCount(Math.max(0, p.getRestCount() - item.getQuantity()));
            if (orderDetail.getOrder().isTomorrow())
                p.setTwRestCount(Math.max(0, p.getTwRestCount() - item.getQuantity()));
            productRepository.save(p);
        }
    }
}
