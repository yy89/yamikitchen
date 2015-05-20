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
import com.xiaobudian.yamikitchen.repository.account.TransactionTypeRepository;
import com.xiaobudian.yamikitchen.repository.merchant.MerchantRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

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
    private PlatformTransactionFlowRepository platformTransactionFlowRepository;
    @Inject
    private TransactionHandler transactionHandler;
    @Inject
    private TransactionTypeRepository transactionTypeRepository;

    private PlatformAccount getPlatformAccount() {
        return platformAccountRepository.findOne(1l);
    }

    private double settlementAmount(Order order) {
        Merchant merchant = merchantRepository.findOne(order.getMerchantId());
        double share = merchant.getSharing() + serviceCharge;
        return order.settlementAmountOfMerchant(share);
    }

    public void settlement(Order order) {
        final double amt = settlementAmount(order);
        transactionHandler.handle(order, amt, transactionTypeRepository.findByCode(1002));
        transactionHandler.handle(order, amt, transactionTypeRepository.findByCode(2002));

        PlatformAccount platformAccount = getPlatformAccount();
        platformAccount.setBalance(platformAccount.getBalance() + order.priceAsDouble() - amt);
        platformTransactionFlowRepository.save(createPlatformTransactionFlow(order, platformAccount, order.priceAsDouble() - amt));

    }

    public PlatformTransactionFlow createPlatformTransactionFlow(Order order, PlatformAccount account, double amount) {
        return new PlatformTransactionFlow(order.getOrderNo(), order.getMerchantId(), account.getId(), amount);
    }
}
