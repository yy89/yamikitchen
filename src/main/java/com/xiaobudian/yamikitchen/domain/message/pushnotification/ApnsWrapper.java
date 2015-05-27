package com.xiaobudian.yamikitchen.domain.message.pushnotification;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsNotification;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.ApnsServiceBuilder;
import com.xiaobudian.yamikitchen.domain.member.Device;
import com.xiaobudian.yamikitchen.domain.message.PushMessage;
import com.xiaobudian.yamikitchen.repository.message.PushHistoryRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Johnson on 2015/3/21.
 */
@Component(value = "apnsWrapper")
public class ApnsWrapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationPusher.class);
    @Value(value = "${apns.push.cert.password}")
    private String certPassword;
    @Value(value = "${apns.push.cert.dev}")
    private String devCertFileName;
    @Value(value = "${apns.push.cert.prd}")
    private String prdCertFileName;
    @Inject
    private PushHistoryRepository pushHistoryRepository;

    private ApnsService getApnsService(boolean isDev) {
        try {
            final String certFileName = isDev ? devCertFileName : prdCertFileName;
            String certFilePath = new ClassPathResource(certFileName).getURL().getPath();
            ApnsServiceBuilder builder = APNS.newService().withCert(certFilePath, certPassword);
            return isDev ? builder.withSandboxDestination().build() : builder.withProductionDestination().build();
        } catch (IOException e) {
            return null;
        }
    }

    private String getPayload(PushMessage pushMessage) {
        return APNS.newPayload().alertBody(pushMessage.getContent()).sound("default")
                .customField("messageType", pushMessage.getType().getId())
                .customField(pushMessage.getType().getKey(), pushMessage.getPayLoad()).build();
    }

    public void pushByDevice(PushMessage pm, Device device) {
        getApnsService(device.isDev()).push(device.getDeviceToken(), getPayload(pm));
    }

    public void pushByApp(PushMessage pushMessage, List<Device> devices) {
        Set<String> deviceTokens = new HashSet<>();
        for (Device device : devices) {
            if (StringUtils.isEmpty(device.getDeviceToken()) || device.isDev()) continue;
            deviceTokens.add(device.getDeviceToken());
        }
        final String payload = getPayload(pushMessage);
        Collection<? extends ApnsNotification> notifications = getApnsService(true).push(deviceTokens, payload);
        PushHistory pushHistory = new PushHistory(deviceTokens.size(), notifications.size(), pushMessage.getContent());
        pushHistoryRepository.save(pushHistory);
    }
}
