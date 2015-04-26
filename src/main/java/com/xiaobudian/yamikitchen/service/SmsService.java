package com.xiaobudian.yamikitchen.service;

/**
 * Created by Administrator on 2014/12/1.
 */
public interface SmsService {
    public void sendSms(String mobilePhone);

    public  boolean  sendVoiceVerifyCode(String mobilePhone);

    public boolean isValidVerificationCode(String mobilePhone, String verificationCode);


}
