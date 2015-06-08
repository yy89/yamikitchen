package com.xiaobudian.yamikitchen.domain.account;

import com.xiaobudian.yamikitchen.domain.merchant.Merchant;
import com.xiaobudian.yamikitchen.domain.order.Order;
import com.xiaobudian.yamikitchen.repository.account.AccountRepository;
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
    private TransactionProcessor transactionProcessor;

    private double getShareScale(Long merchantId) {
        Merchant merchant = merchantRepository.findOne(merchantId);
        return (1 - merchant.getSharing()) + serviceCharge;
    }

    public void settle(Order order) {
        settleShare(order);
        settleDeliverPrice(order);
    }

    private void settleShare(Order order) {
        double scale = getShareScale(order.getMerchantId());
        transactionProcessor.process(order, order.shareOfMerchant(scale), 1002);
        transactionProcessor.process(order, order.shareOfMerchant(scale), 2002);
        transactionProcessor.processByPlatform(order, order.shareOfPlatform(scale), 1007);
        transactionProcessor.process(order, order.shareOfPlatform(scale), 2007);
    }

    private void settleDeliverPrice(Order order) {
        if (order.deliverByDaDa()) {
            transactionProcessor.processByPlatform(order, order.getDeliverPrice(), 1008);
            transactionProcessor.process(order, order.getDeliverPrice(), 2008);
        } else {
            transactionProcessor.process(order, order.getDeliverPrice(), 1009);
            transactionProcessor.process(order, order.getDeliverPrice(), 2009);
        }
    }
}
