package com.xiaobudian.yamikitchen.domain.account;

import com.xiaobudian.yamikitchen.domain.operation.PlatformAccount;
import com.xiaobudian.yamikitchen.domain.operation.PlatformTransactionFlow;
import com.xiaobudian.yamikitchen.domain.order.Order;
import com.xiaobudian.yamikitchen.repository.PlatformAccountRepository;
import com.xiaobudian.yamikitchen.repository.PlatformTransactionFlowRepository;
import com.xiaobudian.yamikitchen.repository.account.AccountRepository;
import com.xiaobudian.yamikitchen.repository.account.TransactionFlowRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

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

    private PlatformAccount platformAccount;

    @PostConstruct
    public void init() {
        platformAccount = platformAccountRepository.findOne(1l);
    }

    private Account getAccount(Order order, TransactionType type) {
        List<Account> accounts = accountRepository.findByMerchantId(order.getMerchantId());
        return accounts.get(type.getAccountType());
    }

    public void handle(Order order, TransactionType type) {
        handle(order, order.priceAsDouble(), type);
    }

    public void handle(Order order, Double amount, TransactionType type) {
        Account account = getAccount(order, type);
        account.setAvailableBalance(account.getAvailableBalance() + amount);
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
        TransactionFlow flow = new TransactionFlow(account.getAccountNo(), order.getOrderNo(), order.getMerchantId(),
                account.getUid(), order.priceAsDouble(), type.getCode());
        transactionFlowRepository.save(flow);
    }

    public void handle(Account account, Double amount, TransactionType type) {
        TransactionFlow flow = new TransactionFlow(account.getAccountNo(), null, account.getMerchantId(),
                account.getUid(), amount, type.getCode());
        transactionFlowRepository.save(flow);
    }

    public void handleWithinPlatform(Order order, double amt, TransactionType type) {
        platformAccount.setBalance(platformAccount.getBalance() + amt);
        platformAccount.setAvailableBalance(platformAccount.getAvailableBalance() + amt);
        platformAccountRepository.save(platformAccount);
        platformTransactionFlowRepository.save(new PlatformTransactionFlow(platformAccount.getAccountNo(), order.getOrderNo(), order.getMerchantId(), null, amt, type.getCode()));
    }
}
