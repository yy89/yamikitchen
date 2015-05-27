package com.xiaobudian.yamikitchen.web;

import com.xiaobudian.yamikitchen.common.Result;
import com.xiaobudian.yamikitchen.domain.member.BankCard;
import com.xiaobudian.yamikitchen.domain.member.User;
import com.xiaobudian.yamikitchen.domain.merchant.UserAddress;
import com.xiaobudian.yamikitchen.service.MemberService;
import com.xiaobudian.yamikitchen.service.SmsService;
import com.xiaobudian.yamikitchen.web.dto.UserRequest;
import com.xiaobudian.yamikitchen.web.dto.UserResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;


/**
 * Created by Johnson on 2015/4/22.
 */
@RestController
public class MemberController {
    @Inject
    private MemberService memberService;
    @Inject
    private SmsService smsService;
    @Resource(name = "authenticationManager")
    private AuthenticationManager authenticationManager;

    private String autoLoginAfterRegistration(UserRequest userRequest, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userRequest.getMobile(), userRequest.getPassword());
        token.setDetails(new WebAuthenticationDetails(request));
        Authentication authenticatedUser = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
        return request.getSession().getId();
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public Result register(@RequestBody UserRequest userRequest, HttpServletRequest request) {
        User u = new User(userRequest.getMobile(), userRequest.getPassword());
        if (memberService.getUserBy(u.getUsername()) != null)
            throw new RuntimeException("registration.username.exists.error");
        if (!smsService.isValidVerificationCode(userRequest.getMobile(), userRequest.getCertCode())) {
            throw new RuntimeException("registration.mobile.verification.error");
        }
        User newUser = memberService.register(u);
        return Result.successResult(new UserResponse(autoLoginAfterRegistration(userRequest, request), newUser));
    }

    @RequestMapping(value = "/changePwd", method = RequestMethod.POST)
    @ResponseBody
    public Result changePassword(@RequestBody UserRequest userRequest, HttpServletRequest request) {
        if (!smsService.isValidVerificationCode(userRequest.getMobile(), userRequest.getCertCode()))
            throw new RuntimeException("registration.mobile.verification.error");
        User newUser = memberService.changePassword(new User(userRequest.getMobile(), userRequest.getPassword()));
        return Result.successResult(new UserResponse(autoLoginAfterRegistration(userRequest, request), newUser));
    }

    @RequestMapping(value = "/addresses", method = RequestMethod.GET)
    @ResponseBody
    public Result getAddresses(@AuthenticationPrincipal User user) {
        return Result.successResult(memberService.getAddresses(user.getId()));
    }

    @RequestMapping(value = "/addresses", method = RequestMethod.POST)
    @ResponseBody
    public Result addAddress(@RequestBody UserAddress address, @AuthenticationPrincipal User user) {
        address.setUid(user.getId());
        return Result.successResult(memberService.addAddress(address));
    }

    @RequestMapping(value = "/addresses", method = RequestMethod.PUT)
    @ResponseBody
    public Result editAddress(@RequestBody UserAddress address, @AuthenticationPrincipal User user) {
        address.setUid(user.getId());
        return Result.successResult(memberService.addAddress(address));
    }

    @RequestMapping(value = "/addresses/{addressId}", method = RequestMethod.DELETE)
    @ResponseBody
    public Result removeAddress(@PathVariable Long addressId) {
        return Result.successResult(memberService.removeAddress(addressId));
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public Result updateUserIntroduction(@RequestBody User user, @AuthenticationPrincipal User authenticationUser) {
        user.setId(authenticationUser.getId());
        return Result.successResult(memberService.updateUser(user));
    }

    @RequestMapping(value = "/users/bankCard", method = RequestMethod.POST)
    public Result updateUserIntroduction(@RequestBody BankCard card, @AuthenticationPrincipal User authenticationUser) {
        card.setUid(authenticationUser.getId());
        return Result.successResult(memberService.bindingBankCard(card));
    }
}
