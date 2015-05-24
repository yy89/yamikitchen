package com.xiaobudian.yamikitchen.domain.account;

import com.xiaobudian.yamikitchen.domain.operation.PlatformAccount;
import com.xiaobudian.yamikitchen.domain.operation.PlatformTransactionFlow;
import com.xiaobudian.yamikitchen.domain.order.Order;
import com.xiaobudian.yamikitchen.repository.PlatformAccountRepository;
import com.xiaobudian.yamikitchen.repository.PlatformTransactionFlowRepository;
import com.xiaobudian.yamikitchen.repository.account.AccountRepository;
import com.xiaobudian.yamikitchen.repository.account.TransactionFlowRepository;
import com.xiaobudian.yamikitchen.repository.account.TransactionTypeRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Johnson on 2015/5/20.
 */
@Component(value = "transactionHandler")
public class TransactionHandler {
    @Inject
    private AccountRepository accountRepository;
    @Inject
    private TransactionFlowRepository transactionFlowRepository;
    @Inject
    private PlatformAccountRepository platformAccountRepository;
    @Inject
    private PlatformTransactionFlowRepository platformTransactionFlowRepository;
    @Inject
    private TransactionTypeRepository transactionTypeRepository;
    private PlatformAccount platformAccount;
    private Map<Integer, Integer> TRANS_ACT_TYPE_MAP = new HashMap<>();

    @PostConstruct
    public void init() {
        platformAccount = platformAccountRepository.findOne(1l);
        for (TransactionType t : transactionTypeRepository.findAll()) {
            TRANS_ACT_TYPE_MAP.put(t.getCode(), t.getAccountType());
        }
    }

    private Account getAccount(Order order, int transactionCode) {
        List<Account> accounts = accountRepository.findByMerchantId(order.getMerchantId());
        return accounts.get(TRANS_ACT_TYPE_MAP.get(transactionCode));
    }

    public void handle(Order order, int transactionCode) {
        handle(order, order.priceAsDouble(), transactionCode);
    }

    public void handle(Order order, Double amount, int transactionCode) {
        Account account = getAccount(order, transactionCode);
        account.setAvailableBalance(account.getAvailableBalance() + amount);
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
        transactionFlowRepository.save(new TransactionFlow(account, order, amount, transactionCode));
    }

    public void handle(Account account, Double amount, int transactionCode) {
        transactionFlowRepository.save(new TransactionFlow(account, amount, transactionCode));
    }

    public void handleWithinPlatform(Order order, double amt, int transactionCode) {
        platformAccount.setBalance(platformAccount.getBalance() + amt);
        platformAccount.setAvailableBalance(platformAccount.getAvailableBalance() + amt);
        platformAccountRepository.save(platformAccount);
        platformTransactionFlowRepository.save(new PlatformTransactionFlow(platformAccount, order, amt, transactionCode));
    }
}
