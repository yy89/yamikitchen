package com.xiaobudian.yamikitchen.domain.order;

import com.xiaobudian.yamikitchen.common.Keys;
import com.xiaobudian.yamikitchen.domain.coupon.Coupon;
import com.xiaobudian.yamikitchen.domain.merchant.Merchant;
import com.xiaobudian.yamikitchen.domain.merchant.Product;
import com.xiaobudian.yamikitchen.repository.coupon.CouponRepository;
import com.xiaobudian.yamikitchen.repository.merchant.MerchantRepository;
import com.xiaobudian.yamikitchen.repository.merchant.ProductRepository;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

import static com.xiaobudian.yamikitchen.domain.order.QueueScheduler.UNPAID_QUEUE_ESCAPE_TIME;

/**
 * Created by Johnson on 2015/5/15.
 */
@Component(value = "orderPostHandler")
public class OrderPostHandler {
    @Inject
    private ProductRepository productRepository;
    @Inject
    private CouponRepository couponRepository;
    @Inject
    private QueueScheduler queueScheduler;
    @Inject
    private MerchantRepository merchantRepository;

    private void handleCoupon(Coupon coupon) {
        if (coupon == null) return;
        coupon.setLocked(true);
        couponRepository.save(coupon);
    }

    private void handleProduct(OrderDetail orderDetail) {
        for (OrderItem item : orderDetail.getItems()) {
            Product p = productRepository.findOne(item.getProductId());
            p.setSoldCount(p.getSoldCount() + item.getQuantity());
            if (orderDetail.getOrder().isToday()) p.setRestCount(Math.max(0, p.getRestCount() - item.getQuantity()));
            if (orderDetail.getOrder().isTomorrow())
                p.setTwRestCount(Math.max(0, p.getTwRestCount() - item.getQuantity()));
            productRepository.save(p);
        }
    }

    private void handleBusinessData(Merchant merchant, OrderDetail orderDetail) {
        merchant.updateTurnOver(orderDetail.getOrder());
        merchant.setSoldCount(merchant.getSoldCount() + 1);
        merchant.setMonthlySoldCount(merchant.getMonthlySoldCount() + 1);
        merchantRepository.save(merchant);
    }

    public void handle(OrderDetail orderDetail, Coupon coupon, Merchant merchant) {
        Order order = orderDetail.getOrder();
        if (order.isPayOnline()) {
            queueScheduler.put(Keys.unPaidQueue(order.getId()), order.getId(), UNPAID_QUEUE_ESCAPE_TIME);
            if (order.isHasPaid()) {
                handleBusinessData(merchant, orderDetail);
                handleProduct(orderDetail);
            }
        }
        if (order.isPayOnDeliver()) {
            handleCoupon(coupon);
            handleProduct(orderDetail);
            handleBusinessData(merchant, orderDetail);
        }
    }
}
