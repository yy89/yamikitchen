package com.xiaobudian.yamikitchen.repository.account;

import com.xiaobudian.yamikitchen.domain.account.Account;
import com.xiaobudian.yamikitchen.domain.account.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Johnson on 2015/5/17.
 */
public interface AccountRepository extends JpaRepository<Account, Long> {
    public List<Account> findByUid(Long uid);

    public Account findByUidAndType(Long uid, AccountType type);
}
