package com.xiaobudian.yamikitchen.service;

import com.cloopen.rest.sdk.CCPRestSDK;
import com.jianzhou.sdk.BusinessService;
import com.xiaobudian.yamikitchen.common.Keys;
import com.xiaobudian.yamikitchen.repository.RedisRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.text.MessageFormat;
import java.util.Map;

/**
 * Created by Administrator on 2014/12/1.
 */
@Service(value = "smsService")
public class SmsServiceImpl implements SmsService {
    private static final String VOICE_SUCCESS_CODE = "000000";
    private static final String STATUS_CODE_KEY = "statusCode";
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
    @Value(value = "${sms.voice.host}")
    private String voiceHost;
    @Value(value = "${sms.voice.port}")
    private String voicePort;
    @Value(value = "${sms.voice.accountSid}")
    private String voiceAccountSid;
    @Value(value = "${sms.voice.token}")
    private String voiceAccountAuthToken;
    @Value(value = "${sms.voice.appid}")
    private String voiceAppid;
    @Value(value = "${sms.voice.displayPhone}")
    private String displayPhone;


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
    public boolean sendVoiceVerifyCode(String mobilePhone) {
        CCPRestSDK restAPI = new CCPRestSDK();
        restAPI.init(voiceHost, voicePort);
        restAPI.setAccount(voiceAccountSid, voiceAccountAuthToken);
        restAPI.setAppId(voiceAppid);
        final String code = RandomStringUtils.randomNumeric(4);
        Map<String, Object> response = restAPI.voiceVerify(code, mobilePhone, displayPhone, "2", "");
        if (!VOICE_SUCCESS_CODE.equals(response.get(STATUS_CODE_KEY))) return false;
        redisRepository.set(Keys.mobileSmsKey(mobilePhone), code, expireMinutes);
        return true;
    }

    @Override
    public boolean isValidVerificationCode(String mobilePhone, String verificationCode) {
        final String v = redisRepository.get(Keys.mobileSmsKey(mobilePhone));
        return v != null && verificationCode.equalsIgnoreCase(v);
    }
}
