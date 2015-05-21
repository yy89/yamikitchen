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
    private TransactionHandler transactionHandler;
    @Inject
    private TransactionTypeRepository transactionTypeRepository;

    private double getShare(Order order) {
        Merchant merchant = merchantRepository.findOne(order.getMerchantId());
        return merchant.getSharing() + serviceCharge;
    }

    public void settlement(Order order) {
        double share = getShare(order);
        double amtMerchant = order.settlementAmountOfMerchant(share);
        double amtCompany = order.settlementAmountOfCompany(share);
        double deliverPrice = order.deliverPriceAsDouble();
        transactionHandler.handle(order, amtMerchant, transactionTypeRepository.findByCode(1002));
        transactionHandler.handle(order, amtMerchant * (-1), transactionTypeRepository.findByCode(2002));
        transactionHandler.handle(order, amtCompany * (-1), transactionTypeRepository.findByCode(2007));
        transactionHandler.handleWithinPlatform(order, amtCompany, transactionTypeRepository.findByCode(1007));
        if (order.deliverByDaDa()) {
            transactionHandler.handleWithinPlatform(order, order.deliverPriceAsDouble(), transactionTypeRepository.findByCode(1008));
            transactionHandler.handle(order, deliverPrice * (-1), transactionTypeRepository.findByCode(2008));
        } else {
            transactionHandler.handle(order, deliverPrice, transactionTypeRepository.findByCode(2009));
            transactionHandler.handle(order, deliverPrice * (-1), transactionTypeRepository.findByCode(2009));
        }
    }
}
