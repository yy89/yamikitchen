package com.xiaobudian.yamikitchen.domain.account;

import com.xiaobudian.yamikitchen.domain.order.Order;
import com.xiaobudian.yamikitchen.repository.account.AccountRepository;
import com.xiaobudian.yamikitchen.repository.account.TransactionFlowRepository;
import org.springframework.stereotype.Component;
import sun.beans.editors.DoubleEditor;

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

    private Account getAccount(Order order, TransactionType type) {
        List<Account> accounts = accountRepository.findByMerchantId(order.getMerchantId());
        return accounts.get(type.getAccountType());
    }

    public void handle(Order order, TransactionType type) {
        handle(order, order.priceAsDouble(), type);
    }

    public void handle(Order order, Double amount, TransactionType type) {
        Account account = getAccount(order, type);
        TransactionFlow flow = new TransactionFlow(account.getAccountNo(), order.getOrderNo(), order.getMerchantId(),
                account.getUid(), order.priceAsDouble(), type.getCode());
        account.setAvailableBalance(account.getAvailableBalance() + amount);
        account.setBalance(account.getBalance() + amount);
        transactionFlowRepository.save(flow);
    }

    public void handle(Account account, Double amount, TransactionType type) {
        TransactionFlow flow = new TransactionFlow(account.getAccountNo(), null, account.getMerchantId(),
                account.getUid(), amount, type.getCode());
        transactionFlowRepository.save(flow);
    }
}
