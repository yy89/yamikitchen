package com.xiaobudian.yamikitchen.repository.message;

import com.xiaobudian.yamikitchen.domain.message.PushNotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Johnson on 2015/5/27.
 */
public interface PushNotificationTypeRepository extends JpaRepository<PushNotificationType, Long> {
    public PushNotificationType findByKey(String key);
}