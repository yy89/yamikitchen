package com.xiaobudian.yamikitchen.web;

import com.xiaobudian.yamikitchen.common.Result;
import com.xiaobudian.yamikitchen.domain.coupon.WeChatCouponSharing;
import com.xiaobudian.yamikitchen.domain.member.User;
import com.xiaobudian.yamikitchen.domain.member.WeChatUser;
import com.xiaobudian.yamikitchen.repository.member.WeChatUserRepository;
import com.xiaobudian.yamikitchen.service.CouponService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Johnson on 2015/5/3.
 */
@RestController(value = "couponController")
public class CouponController {
    @Inject
    private CouponService couponService;
    @Inject
    private WeChatUserRepository weChatUserRepository;
    @Inject
    private RestTemplate restTemplate;
    @Inject
    private WeChatCouponSharing weChatCouponSharing;

    @RequestMapping(value = "/user/coupons/{available}", method = RequestMethod.GET)
    public Result getCoupons(@PathVariable boolean available, @AuthenticationPrincipal User user) {
        final Long uid = user.getId();
        return Result.successResult(available ? couponService.getAvailableCoupons(uid) : couponService.getExpiredCoupons(uid));
    }

    @RequestMapping(value = "/coupons/mobile/{mobile}/types/{type}", method = RequestMethod.POST)
    public Result addCoupon(@PathVariable String mobile, @PathVariable Long type, @AuthenticationPrincipal User user) {
        return Result.successResult(couponService.addCoupon(mobile, type));
    }

    @RequestMapping(value = "/couponHistories", method = RequestMethod.GET)
    public Result getCouponHistories(@AuthenticationPrincipal User user) {
        return Result.successResult(couponService.getCouponHistories());
    }

    @RequestMapping(value = "/order/{orderNo}/coupons", method = RequestMethod.GET)
    public Result getOrderCoupons(@PathVariable String orderNo) {
        return Result.successResult(couponService.getOrderCoupons(orderNo));
    }

    @RequestMapping(value = "/coupon/openId/{openId}/mobile/{mobile}/orderNo/{orderNo}", method = RequestMethod.POST)
    public Result bindingMobileWithWeChatUser(@PathVariable String openId, @PathVariable String mobile, @PathVariable String orderNo) {
        WeChatUser weChatUser = weChatUserRepository.findByOpenId(openId);
        weChatUser.setMobile(mobile);
        weChatUserRepository.save(weChatUser);
        couponService.createCouponAccount(mobile);
        couponService.dispatchCoupon(mobile, orderNo);
        return Result.successResult(couponService.getOrderCoupons(orderNo));
    }

    @RequestMapping(value = "/coupon/share", method = RequestMethod.GET)
    public void shareCouponByWeChat(@RequestParam(value = "code", required = false) String code,
                                    @RequestParam(value = "orderNo", required = false) String orderNo, HttpServletResponse response) throws IOException {
        WeChatCouponSharing.Principal principal = weChatCouponSharing.getAccessToken(code);
        if (principal == null) response.sendRedirect(weChatCouponSharing.getAuthorizeUrl(orderNo));
        assert principal != null;
        WeChatUser user = weChatUserRepository.findByOpenId(principal.getOpenid());
        if (user == null) user = weChatUserRepository.save(weChatCouponSharing.getUserInfo(principal));
        if (user.hasMobile()) couponService.createCouponAccount(user.getMobile());
        couponService.dispatchCoupon(user.getMobile(), orderNo);
        response.sendRedirect(weChatCouponSharing.getCouponRedirectUrl(user, orderNo));
    }

    @RequestMapping(value = "/wechat/js/signature", method = RequestMethod.GET)
    public WeChatCouponSharing.Signature getSignature(@RequestParam(value = "orderNo") String orderNo) {
        return weChatCouponSharing.getSignature(orderNo);
    }
}
