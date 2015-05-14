package com.xiaobudian.yamikitchen.service;

import com.xiaobudian.yamikitchen.common.Keys;
import com.xiaobudian.yamikitchen.domain.message.Notice;
import com.xiaobudian.yamikitchen.domain.message.OrderStatusChangeEvent;
import com.xiaobudian.yamikitchen.domain.order.Order;
import com.xiaobudian.yamikitchen.domain.order.OrderStatus;
import com.xiaobudian.yamikitchen.repository.NoticeRepository;
import com.xiaobudian.yamikitchen.repository.RedisRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by Johnson on 2015/5/12.
 */
@Service(value = "messageService")
public class MessageServiceImpl implements MessageService, ApplicationListener<OrderStatusChangeEvent> {
    @Inject
    private NoticeRepository noticeRepository;
    @Inject
    private RedisRepository redisRepository;

    @Override
    public List<Notice> getNotices(Long uid, Integer pageFrom, Integer pageSize) {
        redisRepository.removeKey(Keys.uidUnreadNoticeQueue(String.valueOf(uid)));
        Set<String> noticeIdList = redisRepository.members(Keys.uidNoticeQueue(String.valueOf(uid)), pageFrom, Math.max(0, pageFrom + pageSize - 1));
        return CollectionUtils.isEmpty(noticeIdList) ? new ArrayList<Notice>() : noticeRepository.findByIdIn(noticeIdList);
    }

    @Override
    public boolean removeNotice(Long uid, Long noticeId) {
        redisRepository.addForZSet(Keys.uidNoticeQueue(String.valueOf(uid)), String.valueOf(noticeId));
        redisRepository.addForZSet(Keys.uidUnreadNoticeQueue(String.valueOf(uid)), String.valueOf(noticeId));
        return true;
    }

    @Override
    public boolean addNotice(Notice notice) {
        Notice newNotice = noticeRepository.save(notice);
        Set<String> uidList = notice.toSomebody() ? Collections.singleton(String.valueOf(notice.getUid())) : redisRepository.members(Keys.allUids());
        for (String uid : uidList) {
            redisRepository.addForZSet(Keys.uidNoticeQueue(uid), String.valueOf(newNotice.getId()));
            redisRepository.addForZSet(Keys.uidUnreadNoticeQueue(uid), String.valueOf(newNotice.getId()));
        }
        return true;
    }


    @Override
    public void onApplicationEvent(OrderStatusChangeEvent orderStatusChangeEvent) {
        Order order = orderStatusChangeEvent.getOrder();
        OrderStatus status = OrderStatus.from(order.getStatus());
//        switch (status) {
//            case WAIT_CONFIRMATION:
//                new Notice(3, status.getNoticeMessage(), "您有一笔在address的新订单", order.getOrderNo(), order.getMerchantId())
//
//                Integer type, String title, String content, String oderNo, Long uid
//                break;
//            case WAIT_DELIVER:
//                break;
//            case WAIT_SELF_TAKE:
//                break;
//            case DELIVERING:
//                break;
//            case COMPLETED:
//                break;
//            case CANCELLED:
//                break;
//        }
/*
1.订单状态变更为等待确认时，向订单所绑定的商户发送订单
type=3
title：您有新订单啦
content：您有一笔在address的新订单
ordno：订单号

2.订单状态变更为等待配送时，向订单创建用户发送消息和通知
type=1
title：厨房已经接单啦
content：“厨房名称”已经确认接收您的订单：ordno
ordno：订单号

3.订单状态变更为订单取消时，向订单创建用户和订单所属商户分别发送系统通知
type：1
title：订单已取消
content：订单：ordno已经取消。
ordno
 */
    }
}
