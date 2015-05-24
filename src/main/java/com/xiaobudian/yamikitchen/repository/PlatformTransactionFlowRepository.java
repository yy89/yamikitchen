package com.xiaobudian.yamikitchen.repository;

import com.xiaobudian.yamikitchen.domain.operation.PlatformTransactionFlow;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by johnson1 on 5/19/15.
 */
public interface PlatformTransactionFlowRepository extends JpaRepository<PlatformTransactionFlow, Long> {
}
