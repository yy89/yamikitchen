package com.xiaobudian.yamikitchen.repository.account;

import com.xiaobudian.yamikitchen.domain.account.TransactionFlow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Johnson on 2015/4/22.
 */
public interface TransactionFlowRepository extends JpaRepository<TransactionFlow, Long> {
    @Query("select tf from TransactionFlow tf where tf.uid=?1 order by operateDate desc")
    List<TransactionFlow> findByUid(Long uid);
}
