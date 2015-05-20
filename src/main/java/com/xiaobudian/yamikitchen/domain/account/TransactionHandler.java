package com.xiaobudian.yamikitchen.domain.account;

import com.xiaobudian.yamikitchen.domain.merchant.Merchant;
import com.xiaobudian.yamikitchen.domain.order.Order;
import com.xiaobudian.yamikitchen.repository.account.AccountRepository;
import com.xiaobudian.yamikitchen.repository.merchant.MerchantRepository;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by Johnson on 2015/5/20.
 */
public class TransactionHandler {
    @Inject
    private AccountRepository accountRepository;

    private Account getAccount(Order order, TransactionType type) {
        List<Account> accounts = accountRepository.findByMerchantId(order.getMerchantId());
        return accounts.get(type.getAccountType());
    }

    public void handle(Order order, TransactionType type) {
        Account account = getAccount(order, type);
        /*String accountNo, String orderNo, Long merchantId, Long uid, Double amount, Integer transactionType

         */
        new TransactionFlow(account.getAccountNo(), order.getOrderNo(), order.getMerchantId(),account.getUid(), order.priceAsDouble(),type.getCode());

        createFlow();
    }


    public TransactionFlow createFlow() {
        return new TransactionFlow();
    }
}
