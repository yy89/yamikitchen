package com.xiaobudian.yamikitchen.domain.account;

import com.xiaobudian.yamikitchen.domain.merchant.Merchant;
import com.xiaobudian.yamikitchen.domain.operation.PlatformAccount;
import com.xiaobudian.yamikitchen.domain.operation.PlatformTransactionFlow;
import com.xiaobudian.yamikitchen.domain.order.Order;
import com.xiaobudian.yamikitchen.domain.order.OrderStatus;
import com.xiaobudian.yamikitchen.repository.PlatformAccountRepository;
import com.xiaobudian.yamikitchen.repository.PlatformTransactionFlowRepository;
import com.xiaobudian.yamikitchen.repository.account.AccountRepository;
import com.xiaobudian.yamikitchen.repository.account.TransactionFlowRepository;
import com.xiaobudian.yamikitchen.repository.merchant.MerchantRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by johnson1 on 5/19/15.
 */
@Component(value = "settlementHandler")
public class SettlementHandler {
    @Value(value = "${service.charge.percentage}")
    private double serviceCharge;
    @Inject
    private AccountRepository accountRepository;
    @Inject
    private MerchantRepository merchantRepository;
    @Inject
    private PlatformAccountRepository platformAccountRepository;
    @Inject
    private TransactionFlowRepository transactionFlowRepository;
    @Inject
    private PlatformTransactionFlowRepository platformTransactionFlowRepository;

    private Map<AccountType, Account> getAccounts(Long uid) {
        Map<AccountType, Account> result = new HashMap<>();
        for (Account account : accountRepository.findByUid(uid)) {
            result.put(account.getType(), account);
        }
        return result;
    }

    private PlatformAccount getPlatformAccount() {
        return platformAccountRepository.findOne(1l);
    }


    public void settlement(Order order) {
        if (OrderStatus.COMPLETED.equals(OrderStatus.from(order.getStatus()))) return;
        List<TransactionFlow> transactionFlows = new ArrayList<>();
        Merchant merchant = merchantRepository.findOne(order.getMerchantId());
        Map<AccountType, Account> accountMap = getAccounts(merchant.getCreator());
        final double sharingScale = merchant.getSharing() + serviceCharge;
        Account waitConfirmAccount = accountMap.get(AccountType.WAIT_CONFIRM);
        waitConfirmAccount.setBalance(waitConfirmAccount.getBalance() - order.settlementAmountOfMerchant(sharingScale));
        transactionFlows.add(createTransactionFlow(order, waitConfirmAccount, order.settlementAmountOfMerchant(sharingScale)));
        Account balanceAccount = accountMap.get(AccountType.BALANCE);
        balanceAccount.setBalance(balanceAccount.getBalance() + order.settlementAmountOfMerchant(sharingScale));
        transactionFlows.add(createTransactionFlow(order, waitConfirmAccount, order.settlementAmountOfMerchant(sharingScale)));
        transactionFlowRepository.save(transactionFlows);
        PlatformAccount platformAccount = getPlatformAccount();
        platformAccount.setBalance(platformAccount.getBalance() + order.settlementAmountOfCompany(sharingScale));
        platformTransactionFlowRepository.save(createPlatformTransactionFlow(order, platformAccount, order.settlementAmountOfCompany(sharingScale)));

    }

    public TransactionFlow createTransactionFlow(Order order, Account account, double amount) {
        return new TransactionFlow(account.getId(), order.getOrderNo(), order.getMerchantId(), account.getUid(), amount, 0);
    }

    public PlatformTransactionFlow createPlatformTransactionFlow(Order order, PlatformAccount account, double amount) {
        return new PlatformTransactionFlow(order.getOrderNo(), order.getMerchantId(), account.getId(), amount);
    }
}
