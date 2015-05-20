package com.xiaobudian.yamikitchen.repository.account;

import com.xiaobudian.yamikitchen.domain.account.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Johnson on 2015/5/20.
 */
public interface TransactionTypeRepository extends JpaRepository<TransactionType, Long> {
    public TransactionType findByCode(String code);
}
