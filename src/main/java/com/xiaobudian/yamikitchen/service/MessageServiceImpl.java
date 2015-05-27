package com.xiaobudian.yamikitchen.service;

import com.xiaobudian.yamikitchen.common.Keys;
import com.xiaobudian.yamikitchen.domain.message.*;
import com.xiaobudian.yamikitchen.domain.message.pushnotification.NotificationPusher;
import com.xiaobudian.yamikitchen.repository.RedisRepository;
import com.xiaobudian.yamikitchen.repository.member.UserRepository;
import com.xiaobudian.yamikitchen.repository.message.MessageRepository;
import com.xiaobudian.yamikitchen.repository.message.NoticeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by Johnson on 2015/5/12.
 */
@Service(value = "messageService")
@Transactional
public class MessageServiceImpl implements MessageService, ApplicationListener<ApplicationEvent> {
    @Inject
    private NoticeRepository noticeRepository;
    @Inject
    private RedisRepository redisRepository;
    @Inject
    private MessageRepository messageRepository;
    @Inject
    private UserRepository userRepository;
    @Value(value = "${comment.message.template}")
    private String commentMessageTemplate;
    @Value(value = "${comment.reply.message.template}")
    private String replyCommentMessageTemplate;
    @Inject
    private NotificationPusher pusher;

    private String getContent(Message message) {
        if (MessageType.REPLY_COMMENT.equals(message.getType()))
            return MessageFormat.format(replyCommentMessageTemplate, message.getNickName());
        return MessageFormat.format(commentMessageTemplate, message.getNickName());
    }

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
    public List<Message> getMessages(Integer type, Long uid, Integer pageFrom, Integer pageSize) {
        Set<Long> messageList = redisRepository.membersAsLong(Keys.uidMessageQueue(uid, MessageType.fromCode(type)), pageFrom, pageFrom + pageSize - 1);
        cleanUnreadQueue(uid, type);
        if (CollectionUtils.isEmpty(messageList)) return new ArrayList<>();
        return messageRepository.findByIdIn(messageList);
    }

    @Override
    public void cleanUnreadQueue(Long uid, Integer type) {
        String key = Keys.uidMessageUnreadQueue(uid, MessageType.fromCode(type));
        redisRepository.removeKey(key);
    }

    private void handleMessageVent(MessageEvent event) {
        Message message = messageRepository.save(event.getMessage());
        redisRepository.addForZSetWithScore(message.queueName(), message.getId(), message.getCreateDate().getTime());
        redisRepository.addForZSetWithScore(message.unreadQueueName(), message.getId(), message.getCreateDate().getTime());
        if (event.getMessage().getType().isComment())
            redisRepository.setLong(Keys.commentMessageIdQueue(message.getCommentId()), message.getId());
//        if (!userRepository.findOne(message.getReceiver()).pushNotificationIfNeeded(message.getType())) return;
        message.setContent(getContent(message));
        pusher.push(message);
    }

    private void handleNoticeEvent(NoticeEvent event) {
        List<Notice> notices = event.getNotices();
        if (CollectionUtils.isEmpty(notices)) return;
        for (Notice notice : noticeRepository.save(notices)) {
            addNotice(notice);
        }
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof MessageEvent) handleMessageVent((MessageEvent) event);
        if (event instanceof NoticeEvent) handleNoticeEvent((NoticeEvent) event);
    }
}
