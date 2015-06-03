package com.xiaobudian.yamikitchen.domain.order;

import com.xiaobudian.yamikitchen.repository.RedisRepository;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * Created by Johnson on 2015/5/30.
 */
@Component(value = "queueScheduler")
public class QueueScheduler {
    public static final Long UNPAID_QUEUE_ESCAPE_TIME = 15l;
    public static final Long DELIVER_QUEUE_ESCAPE_TIME = 180l;
    public static final String DELIVERING_QUEUE_PATTERN = "delivering";
    public static final String UNPAID_QUEUE_PATTERN = "unPaid";
    public static final String QUEUE_NAME_DELIMITER = ":";
    @Inject
    private RedisRepository redisRepository;

    public void put(String queueName, Long value, Long triggerInMinutes) {
        redisRepository.setLong(queueName, value, triggerInMinutes);
    }

    public void remove(String queueName) {
        redisRepository.removeKey(queueName);
    }
}
