package com.xiaobudian.yamikitchen.service;

import com.xiaobudian.yamikitchen.common.Util;
import com.xiaobudian.yamikitchen.domain.account.*;
import com.xiaobudian.yamikitchen.domain.coupon.Coupon;
import com.xiaobudian.yamikitchen.domain.member.Bank;
import com.xiaobudian.yamikitchen.domain.member.BankCard;
import com.xiaobudian.yamikitchen.domain.merchant.Merchant;
import com.xiaobudian.yamikitchen.domain.message.NoticeEvent;
import com.xiaobudian.yamikitchen.domain.operation.PlatformAccount;
import com.xiaobudian.yamikitchen.domain.operation.PlatformTransactionFlow;
import com.xiaobudian.yamikitchen.domain.order.Order;
import com.xiaobudian.yamikitchen.domain.order.OrderDetail;
import com.xiaobudian.yamikitchen.domain.order.OrderPostHandler;
import com.xiaobudian.yamikitchen.domain.order.OrderStatus;
import com.xiaobudian.yamikitchen.repository.PlatformAccountRepository;
import com.xiaobudian.yamikitchen.repository.PlatformTransactionFlowRepository;
import com.xiaobudian.yamikitchen.repository.account.*;
import com.xiaobudian.yamikitchen.repository.coupon.CouponRepository;
import com.xiaobudian.yamikitchen.repository.member.BankCardRepository;
import com.xiaobudian.yamikitchen.repository.merchant.MerchantRepository;
import com.xiaobudian.yamikitchen.repository.merchant.ProductRepository;
import com.xiaobudian.yamikitchen.repository.order.OrderItemRepository;
import com.xiaobudian.yamikitchen.repository.order.OrderRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johnson on 2015/5/15.
 */
@Service(value = "accountService")
@Transactional
public class AccountServiceImpl implements AccountService, ApplicationEventPublisherAware {
    @Value(value = "${alipay.partner}")
    private String partner;
    @Value(value = "${alipay.seller}")
    private String seller;
    @Value(value = "${alipay.privateKey}")
    private String privateKey;
    @Value(value = "${alipay.orderString.template}")
    private String orderStringTemplate;
    @Inject
    private AlipayHistoryRepository alipayHistoryRepository;
    @Inject
    private OrderRepository orderRepository;
    @Inject
    private OrderPostHandler orderPostHandler;
    @Inject
    private AccountRepository accountRepository;
    @Inject
    private TransactionFlowRepository transactionFlowRepository;
    @Inject
    private MerchantRepository merchantRepository;
    @Inject
    private BankCardRepository bankCardRepository;
    @Inject
    private TransactionProcessor transactionProcessor;
    @Inject
    private TransactionTypeRepository transactionTypeRepository;
    @Inject
    private OrderItemRepository orderItemRepository;
    private ApplicationEventPublisher applicationEventPublisher;
    @Inject
    private SettlementCenter settlementCenter;
    @Inject
    private ProductRepository productRepository;
    @Inject
    private PlatformAccountRepository platformAccountRepository;
    @Inject
    private PlatformTransactionFlowRepository platformTransactionFlowRepository;
    @Inject
    private CouponRepository couponRepository;
    private List<String> inQueueList = new ArrayList<>();

    public void writePaymentHistory(AlipayHistory history) {
        if (inQueueList.indexOf(history.getOut_trade_no()) > -1) return;
        inQueueList.add(history.getOut_trade_no());
        Order order = orderRepository.findByOrderNo(history.getOut_trade_no());
        if (order.isHasPaid()) return;
        alipayHistoryRepository.save(history);
        transactionProcessor.process(payOrder(order), 1001);
        Merchant merchant = merchantRepository.findOne(order.getMerchantId());
        merchant.updateTurnOver(order);
        updateCouponStatus(order);
        applicationEventPublisher.publishEvent(new NoticeEvent(this, OrderStatus.from(order.getStatus()).getNotices(merchant, order)));
    }


    private void updateCouponStatus(Order order) {
        if (order.getCouponId() == null) return;
        Coupon coupon = couponRepository.findOne(order.getCouponId());
        coupon.setHasUsed(true);
        coupon.setLocked(false);
        couponRepository.save(coupon);
    }

    private Order payOrder(Order order) {
        order.pay();
        orderPostHandler.handle(new OrderDetail(order, orderItemRepository.findByOrderNo(order.getOrderNo())), null);
        return orderRepository.save(order);
    }

    @Override
    public void refundOrder(Order order) {
        order.setPrice(0 - order.getPrice());
        transactionProcessor.process(order, 2004);
    }



    @Override
    public String getOrderStringOfAlipay(Order order) {
        String signTemplate = StringUtils.substringBefore(orderStringTemplate, "&sign");
        String signMessage = MessageFormat.format(signTemplate, order.getOrderNo(), order.getPrice() / 100.00d);
        return MessageFormat.format(orderStringTemplate, order.getOrderNo(), order.getPrice() / 100.00d, Util.signContent(signMessage, privateKey, "UTF-8"));
    }

    @Override
    public List<Account> getAccounts(Long uid) {
        return accountRepository.findByUid(uid);
    }

    @Override
    public AccountSummary getAccountSummary(Long uid) {
        List<Account> accounts = getAccounts(uid);
        Merchant merchant = merchantRepository.findByCreator(uid);
        return new AccountSummary(accounts.get(0).getBalance(),
                accounts.get(1).getBalance(), accounts.get(2).getBalance(),
                merchant.getTurnover(), getBindingBankCard(uid));
    }

    @Override
    public PlatformAccount getPlatformAccount() {
        return platformAccountRepository.findOne(1l);
    }

    @Override
    public BankCard getBindingBankCard(Long uid) {
        return bankCardRepository.findByUid(uid);
    }

    @Override
    public TransactionFlow writeTransactionFlow(TransactionFlow flow) {
        return transactionFlowRepository.save(flow);
    }

    @Override
    public List<TransactionFlow> getTransactionFlowsBy(String orderNo) {
        return transactionFlowRepository.findByOrderNo(orderNo);
    }

    @Override
    public List<PlatformTransactionFlow> getTransactionFlowsOfPlatform() {
        return platformTransactionFlowRepository.findAll();
    }

    @Override
    public List<TransactionFlow> getTransactionFlows(Long uid) {
        return transactionFlowRepository.findByUid(uid);
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Scheduled(cron = "0 13 15 * * ?")
    public void executeDailyJob() {
        merchantRepository.updateTurnover();
        productRepository.updateRest();
    }
}
