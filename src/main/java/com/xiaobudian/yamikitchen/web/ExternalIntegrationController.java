package com.xiaobudian.yamikitchen.web;

import com.xiaobudian.yamikitchen.common.Result;
import com.xiaobudian.yamikitchen.service.SmsService;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

/**
 * Created by Johnson on 2015/4/23.
 */
@RestController
public class ExternalIntegrationController {
    @Inject
    private SmsService smsService;

    @RequestMapping(value = "/sms/{mobilePhone}", method = RequestMethod.GET)
    @ResponseBody
    public Result sendSmsVerificationCode(@PathVariable String mobilePhone) {
        smsService.sendSms(mobilePhone);
        return Result.successResultWithoutData();
    }
}
