package com.xiaobudian.yamikitchen.repository.account;

import com.xiaobudian.yamikitchen.domain.account.AlipayHistory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Johnson on 2015/5/15.
 */
public interface AlipayHistoryRepository extends JpaRepository<AlipayHistory, Long> {
}
