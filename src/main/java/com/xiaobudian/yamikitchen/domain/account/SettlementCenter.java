package com.xiaobudian.yamikitchen.domain.account;

import com.xiaobudian.yamikitchen.domain.merchant.Merchant;
import com.xiaobudian.yamikitchen.domain.order.Order;
import com.xiaobudian.yamikitchen.repository.account.AccountRepository;
import com.xiaobudian.yamikitchen.repository.account.TransactionTypeRepository;
import com.xiaobudian.yamikitchen.repository.merchant.MerchantRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * Created by johnson1 on 5/19/15.
 */
@Component(value = "settlementCenter")
public class SettlementCenter {
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

    private double getShareScale(Long merchantId) {
        Merchant merchant = merchantRepository.findOne(merchantId);
        return (merchant.getSharing() + serviceCharge) / 100.00;
    }

    public void settle(Order order) {
        settleShare(order);
        settleDeliverPrice(order);
    }

    private void settleShare(Order order) {
        double scale = getShareScale(order.getMerchantId());
        transactionHandler.handle(order, order.shareOfMerchant(scale), 1002);
        transactionHandler.handle(order, 0 - order.shareOfMerchant(scale), 2002);
        transactionHandler.handle(order, 0 - order.shareOfPlatform(scale), 2007);
        transactionHandler.handleWithinPlatform(order, order.shareOfPlatform(scale), 1007);
    }

    private void settleDeliverPrice(Order order) {
        double deliverPrice = order.deliverPriceAsDouble();
        if (order.deliverByDaDa()) {
            transactionHandler.handleWithinPlatform(order, deliverPrice, 1008);
            transactionHandler.handle(order, 0 - deliverPrice, 2008);
        } else {
            transactionHandler.handle(order, deliverPrice, 2009);
            transactionHandler.handle(order, 0 - deliverPrice, 2009);
        }
    }
}
