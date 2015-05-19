package com.xiaobudian.yamikitchen.service;

import com.xiaobudian.yamikitchen.common.Keys;
import com.xiaobudian.yamikitchen.domain.message.Notice;
import com.xiaobudian.yamikitchen.domain.message.NoticeEvent;
import com.xiaobudian.yamikitchen.repository.member.NoticeRepository;
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
public class MessageServiceImpl implements MessageService, ApplicationListener<NoticeEvent> {
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
    public void onApplicationEvent(NoticeEvent noticeEvent) {
        List<Notice> notices = noticeEvent.getNotices();
        if (CollectionUtils.isEmpty(notices)) return;
        for (Notice notice : noticeRepository.save(notices)) {
            addNotice(notice);
        }
    }
}
