package com.xiaobudian.yamikitchen.domain.member;

import com.xiaobudian.yamikitchen.domain.coupon.CouponAccount;
import com.xiaobudian.yamikitchen.domain.coupon.Dispatcher;
import com.xiaobudian.yamikitchen.repository.coupon.CouponAccountRepository;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * Created by Johnson on 2015/5/23.
 */
@Component(value = "registrationPostHandler")
public class RegistrationPostHandler {
    @Inject
    private CouponAccountRepository couponAccountRepository;
    @Inject
    private Dispatcher dispatcher;

    public void handle(User user) {
        CouponAccount account = couponAccountRepository.findByMobile(user.getBindingPhone());
        if (account == null) account = new CouponAccount(user.getId(), user.getBindingPhone());
        account.setUid(user.getId());
        couponAccountRepository.save(account);
        dispatcher.dispatch(account);
    }

    private void createCouponAccount(User user) {
    }
}
