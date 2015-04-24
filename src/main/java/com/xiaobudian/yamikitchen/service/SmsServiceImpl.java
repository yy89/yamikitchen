package com.xiaobudian.yamikitchen.service;

import com.jianzhou.sdk.BusinessService;
import com.xiaobudian.yamikitchen.common.Keys;
import com.xiaobudian.yamikitchen.repository.RedisRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.text.MessageFormat;

/**
 * Created by Administrator on 2014/12/1.
 */
@Service(value = "smsService")
public class SmsServiceImpl implements SmsService {
    @Inject
    private RedisRepository redisRepository;
    @Value(value = "${sms.service}")
    private String smsServiceUrl;
    @Value(value = "${sms.account}")
    private String account;
    @Value(value = "${sms.password}")
    private String password;
    @Value(value = "${sms.message.template}")
    private String messageTemplate;
    @Value(value = "${sms.expire.minutes}")
    private Long expireMinutes;

    @Override
    public void sendSms(String mobilePhone) {
        BusinessService smsService = new BusinessService();
        smsService.setWebService(smsServiceUrl);
        final String code = RandomStringUtils.randomNumeric(4);
        final String message = MessageFormat.format(messageTemplate, code);
        smsService.sendBatchMessage(account, password, mobilePhone, message);
        redisRepository.set(Keys.mobileSmsKey(mobilePhone), code, expireMinutes);
    }

    @Override
    public String getVerificationCode(String mobilePhone) {
        return redisRepository.get(Keys.mobileSmsKey(mobilePhone));
    }

    @Override
    public boolean isValidVerificationCode(String mobilePhone, String verificationCode) {
        final String v = redisRepository.get(Keys.mobileSmsKey(mobilePhone));
        return v != null && verificationCode.equalsIgnoreCase(v);
    }
}
