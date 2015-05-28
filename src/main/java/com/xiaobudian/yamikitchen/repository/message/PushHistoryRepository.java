package com.xiaobudian.yamikitchen.repository.message;

import com.xiaobudian.yamikitchen.domain.message.pushnotification.PushHistory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Johnson on 2015/5/27.
 */
public interface PushHistoryRepository extends JpaRepository<PushHistory, Long> {
}