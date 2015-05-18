package com.xiaobudian.yamikitchen.web;

import com.xiaobudian.yamikitchen.common.Result;
import com.xiaobudian.yamikitchen.domain.member.User;
import com.xiaobudian.yamikitchen.service.CouponService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

/**
 * Created by Johnson on 2015/5/3.
 */
@RestController
public class CouponController {
    @Inject
    private CouponService couponService;

    @RequestMapping(value = "/coupons", method = RequestMethod.GET)
    public Result getCoupons(@AuthenticationPrincipal User user) {
        return Result.successResult(couponService.getCoupons());
    }

    @RequestMapping(value = "/couponHistories", method = RequestMethod.GET)
    public Result getCouponHistories(@AuthenticationPrincipal User user) {
        return Result.successResult(couponService.getCouponHistories());
    }

}
