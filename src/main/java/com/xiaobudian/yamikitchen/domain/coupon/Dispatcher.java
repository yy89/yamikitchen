package com.xiaobudian.yamikitchen.domain.coupon;

import com.xiaobudian.yamikitchen.domain.member.User;
import com.xiaobudian.yamikitchen.repository.coupon.CouponRepository;
import com.xiaobudian.yamikitchen.repository.coupon.CouponTypeRepository;
import com.xiaobudian.yamikitchen.repository.coupon.DispatchRuleRepository;
import com.xiaobudian.yamikitchen.repository.coupon.OrderCouponRepository;
import com.xiaobudian.yamikitchen.repository.member.UserRepository;
import com.xiaobudian.yamikitchen.repository.member.WeChatUserRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.*;

/**
 * Created by Johnson on 2015/5/23.
 */
@Component(value = "dispatcher")
public class Dispatcher {
    @Inject
    private DispatchRuleRepository dispatchRuleRepository;
    @Inject
    private CouponTypeRepository couponTypeRepository;
    @Inject
    private CouponRepository couponRepository;
    @Inject
    private OrderCouponRepository orderCouponRepository;
    @Inject
    private UserRepository userRepository;
    @Inject
    private WeChatUserRepository weChatUserRepository;
    private Map<Boolean, List<DispatchRule>> RULE_MAP = new HashMap<>();

    @PostConstruct
    private void init() {
        List<DispatchRule> rules = dispatchRuleRepository.findByAvailableIsTrue();
        for (DispatchRule r : rules) {
            if (RULE_MAP.get(r.isForShare()) == null) RULE_MAP.put(r.isForShare(), new ArrayList<DispatchRule>());
            RULE_MAP.get(r.isForShare()).add(r);
        }
    }

    private boolean dispatchIfPossible(String mobile, DispatchRule rule, List<OrderCoupon> receivedCoupons) {
        for (OrderCoupon coupon : receivedCoupons) {
            if (coupon.getMobile().equals(mobile)) return false;
        }
        return rule.getCount() > receivedCoupons.size();
    }

    public OrderCoupon dispatch(String orderNo, CouponAccount account, List<OrderCoupon> receivedCoupons) {
        List<DispatchRule> rules = RULE_MAP.get(true);
        if (!dispatchIfPossible(account.getMobile(), rules.get(0), receivedCoupons)) return null;
        User user = userRepository.findByUsername(account.getMobile());
        Long type = user != null ? rules.get(Math.abs(new Random().nextInt() % 2) + 1).getType() : rules.get(0).getType();
        Coupon coupon = couponRepository.save(new Coupon(couponTypeRepository.findOne(type), account, orderNo));
        return orderCouponRepository.save(new OrderCoupon(orderNo, weChatUserRepository.findByMobile(account.getMobile()), coupon));
    }

    public void dispatch(CouponAccount account) {
        List<Coupon> coupons = new ArrayList<>();
        for (DispatchRule r : RULE_MAP.get(false)) {
            CouponType couponType = couponTypeRepository.findOne(r.getType());
            for (int i = 0; i < r.getCount(); i++) {
                coupons.add(new Coupon(couponType, account));
            }
        }
        couponRepository.save(coupons);
    }
}
