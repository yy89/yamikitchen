package com.xiaobudian.yamikitchen.web;

import com.qiniu.api.auth.AuthException;
import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.rs.PutPolicy;
import com.xiaobudian.yamikitchen.common.Result;
import com.xiaobudian.yamikitchen.service.SmsService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

/**
 * Created by Johnson on 2015/4/23.
 */
@RestController
public class ExternalIntegrationController {
    @Inject
    private SmsService smsService;
    @Value(value = "${qiniu.access.key}")
    private String accessKey;
    @Value(value = "${qiniu.secret.key}")
    private String secretKey;

    @RequestMapping(value = "/sms/{mobilePhone}", method = RequestMethod.GET)
    @ResponseBody
    public Result sendSmsVerificationCode(@PathVariable String mobilePhone) {
        smsService.sendSms(mobilePhone);
        return Result.successResultWithoutData();
    }

    @RequestMapping(value = "/voice/{mobilePhone}", method = RequestMethod.GET)
    @ResponseBody
    public Result sendVoiceVerificationCode(@PathVariable String mobilePhone) {
        return Result.successResult(smsService.sendVoiceVerifyCode(mobilePhone));
    }

    @RequestMapping(value = "/qiniu/token/{bucketName}", method = RequestMethod.GET)
    @ResponseBody
    public Result getPostToken(@PathVariable String bucketName) throws AuthException, JSONException {
        Mac mac = new Mac(accessKey, secretKey);
        PutPolicy putPolicy = new PutPolicy(bucketName);
        return Result.successResult(putPolicy.token(mac));
    }
}
