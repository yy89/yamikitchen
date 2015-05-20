package com.xiaobudian.yamikitchen.repository.account;

import com.xiaobudian.yamikitchen.domain.account.Account;
import com.xiaobudian.yamikitchen.domain.account.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Johnson on 2015/5/17.
 */
public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query("from Account a where a.uid =?1 order by a.type")
    public List<Account> findByUid(Long uid);

    public Account findByUidAndType(Long uid, AccountType type);
    @Query("from Account a where a.merchantId =?1 order by a.type")
    public List<Account> findByMerchantId(Long merchantId);
}
