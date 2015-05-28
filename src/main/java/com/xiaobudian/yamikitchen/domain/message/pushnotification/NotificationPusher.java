package com.xiaobudian.yamikitchen.domain.message.pushnotification;

import com.xiaobudian.yamikitchen.domain.member.Device;
import com.xiaobudian.yamikitchen.domain.message.Message;
import com.xiaobudian.yamikitchen.domain.message.MessageType;
import com.xiaobudian.yamikitchen.domain.message.PushMessage;
import com.xiaobudian.yamikitchen.domain.message.PushNotificationType;
import com.xiaobudian.yamikitchen.repository.member.DeviceRepository;
import com.xiaobudian.yamikitchen.repository.message.PushNotificationTypeRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johnson on 2015/1/25.
 */
@Component(value = "notificationPusher")
public class NotificationPusher {
    @Inject
    private DeviceRepository deviceRepository;
    @Inject
    private PushNotificationTypeRepository pushNotificationTypeRepository;
    private List<PushNotificationType> pushNotificationTypes = new ArrayList<>();
    @Inject
    private ApnsWrapper apnsWrapper;
    @Inject
    private AndroidNotificationWrapper androidNotificationWrapper;

    @PostConstruct
    private void init() throws IOException {
        pushNotificationTypes = pushNotificationTypeRepository.findAll();
    }

    private Device getDevice(Long uid) {
        List<Device> devices = deviceRepository.findByUid(uid);
        return CollectionUtils.isEmpty(devices) ? null : devices.get(0);
    }

    private PushNotificationType getTypeBy(final String key) {
        for (PushNotificationType type : pushNotificationTypes) {
            if (key.equals(type.getKey())) return type;
        }
        return null;
    }

    private PushMessage getPushMessage(Message message) {
        if (message.getType().equals(MessageType.COMMENT))
            return new PushMessage(getTypeBy("order"), message.getContent(), message.getOrderId().toString());
        return null;
    }

    public void push(Message message) {
        Device device = getDevice(message.getReceiver());
        if (device == null || StringUtils.isEmpty(device.getDeviceToken())) return;
        PushMessage pm = getPushMessage(message);
        if (device.isAndroidPlatform()) androidNotificationWrapper.pushByDevice(pm, device);
        else apnsWrapper.pushByDevice(pm, device);
    }

    public void pushToApp(PushMessage pushMessage) {
        androidNotificationWrapper.pushByApp(pushMessage);
        apnsWrapper.pushByApp(pushMessage, deviceRepository.findByType(1));
    }

    public void pushToUserList(List<Long> uidList, String message, Long feedId) {
        PushMessage pm = new PushMessage(getTypeBy("homepage"), message, feedId.toString());
        List<Device> devices = deviceRepository.findByUidIn(uidList);
        for (Device device : devices) {
            if (StringUtils.isEmpty(device.getDeviceToken())) continue;
            if (device.isAndroidPlatform()) androidNotificationWrapper.pushByDevice(pm, device);
            else apnsWrapper.pushByDevice(pm, device);
        }
    }
}
