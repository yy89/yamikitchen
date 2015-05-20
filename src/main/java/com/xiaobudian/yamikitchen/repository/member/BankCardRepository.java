package com.xiaobudian.yamikitchen.repository.member;

import com.xiaobudian.yamikitchen.domain.member.BankCard;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Johnson on 2015/5/20.
 */
public interface BankCardRepository extends JpaRepository<BankCard, Long> {
    public BankCard findByUid(Long uid);
}
