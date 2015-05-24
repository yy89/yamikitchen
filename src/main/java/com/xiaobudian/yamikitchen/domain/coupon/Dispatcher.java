package com.xiaobudian.yamikitchen.domain.coupon;

import com.xiaobudian.yamikitchen.repository.coupon.CouponRepository;
import com.xiaobudian.yamikitchen.repository.coupon.CouponTypeRepository;
import com.xiaobudian.yamikitchen.repository.coupon.DispatchRuleRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

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
    private List<DispatchRule> rules = new ArrayList<>();

    @PostConstruct
    private void init() {
        rules = dispatchRuleRepository.findByAvailableIsTrue();
    }

    public List<DispatchRule> getRegistrationRules() {
        List<DispatchRule> result = new ArrayList<>();
        for (DispatchRule r : rules) {
            if (r.isForShare()) continue;
            result.add(r);
        }
        return result;
    }

    public void dispatch(CouponAccount account) {
        List<Coupon> coupons = new ArrayList<>();
        for (DispatchRule r : getRegistrationRules()) {
            CouponType couponType = couponTypeRepository.findOne(r.getType());
            for (int i = 0; i < r.getCount(); i++) {
                coupons.add(new Coupon(couponType, account));
            }
        }
        couponRepository.save(coupons);
    }
}
