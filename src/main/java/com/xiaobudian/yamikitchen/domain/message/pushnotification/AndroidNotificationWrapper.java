package com.xiaobudian.yamikitchen.domain.message.pushnotification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gexin.rp.sdk.base.ITemplate;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.xiaobudian.yamikitchen.domain.member.Device;
import com.xiaobudian.yamikitchen.domain.message.PushMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johnson on 2015/3/21.
 */
@Component(value = "androidNotificationWrapper")
public class AndroidNotificationWrapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationPusher.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Long EXPIRE_TIME = 24 * 3600 * 1000l;
    @Value(value = "${getui.appId}")
    private String appId;
    @Value(value = "${getui.appKey}")
    private String appKey;
    @Value(value = "${getui.masterSecret}")
    private String masterSecret;
    @Value(value = "${getui.host}")
    private String host;

    private ITemplate getTemplate(PushMessage message) {
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(appId);
        template.setAppkey(appKey);
        template.setTransmissionType(2);
        try {
            template.setTransmissionContent(MAPPER.writeValueAsString(message.mapOfPushedPayLoad()));
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage());
        }
        return template;
    }

    private IGtPush createPushConnection() {
        IGtPush result = new IGtPush(host, appKey, masterSecret);
        try {
            if (result.connect()) return result;
        } catch (IOException e) {
            LOGGER.error("Can not connect to host");
        }
        return null;
    }

    public void pushByDevice(PushMessage pm, Device device) {
        IGtPush push = createPushConnection();
        SingleMessage pushMessage = new SingleMessage();
        pushMessage.setData(getTemplate(pm));
        pushMessage.setOffline(true);
        pushMessage.setOfflineExpireTime(EXPIRE_TIME);
        Target target = new Target();
        target.setAppId(appId);
        target.setClientId(device.getDeviceToken());
        push.pushMessageToSingle(pushMessage, target);
    }

    public void pushByApp(PushMessage pm) {
        IGtPush push = createPushConnection();
        AppMessage message = new AppMessage();
        message.setData(getTemplate(pm));
        message.setOffline(true);
        message.setOfflineExpireTime(EXPIRE_TIME);
        List<String> appIdList = new ArrayList<>();
        appIdList.add(appId);
        message.setAppIdList(appIdList);
        push.pushMessageToApp(message);
    }

}
