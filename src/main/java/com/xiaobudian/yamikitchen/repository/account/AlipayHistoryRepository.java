package com.xiaobudian.yamikitchen.repository.account;

import com.xiaobudian.yamikitchen.domain.account.AlipayHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Johnson on 2015/5/15.
 */
public interface AlipayHistoryRepository extends JpaRepository<AlipayHistory, Long> {
    @Query("from AlipayHistory where out_trade_no = ?1")
    public List<AlipayHistory> findByOrderNo(String orderNo);
}
