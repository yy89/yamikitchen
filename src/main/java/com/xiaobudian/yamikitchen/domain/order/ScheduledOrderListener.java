package com.xiaobudian.yamikitchen.domain.order;

import com.xiaobudian.yamikitchen.repository.order.OrderRepository;
import com.xiaobudian.yamikitchen.service.OrderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import javax.inject.Inject;

/**
 * Created by Johnson on 2015/5/30.
 */
public class ScheduledOrderListener implements MessageListener {
    @Inject
    private OrderRepository orderRepository;
    @Inject
    private OrderService orderService;

    private boolean isDeliveringQueue(String[] parts) {
        return StringUtils.equals(parts[0], QueueScheduler.DELIVERING_QUEUE_PATTERN);
    }

    private boolean isUnPaidQueue(String[] parts) {
        return StringUtils.equals(parts[0], QueueScheduler.UNPAID_QUEUE_PATTERN);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        if (message.getBody().length < 1 || message.getChannel().length < 1) return;
        String[] parts = StringUtils.split(message.toString(), QueueScheduler.QUEUE_NAME_DELIMITER);
        Order order = orderRepository.findOne(Long.parseLong(parts[2]));
        if (isDeliveringQueue(parts)) orderService.finishOrder(order);
        if (isUnPaidQueue(parts)) orderService.cancelOrder(order, order.getUid());
    }
}