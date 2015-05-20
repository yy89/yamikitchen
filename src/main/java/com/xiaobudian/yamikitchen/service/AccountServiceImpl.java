package com.xiaobudian.yamikitchen.service;

import com.xiaobudian.yamikitchen.common.Util;
import com.xiaobudian.yamikitchen.domain.account.*;
import com.xiaobudian.yamikitchen.domain.member.BankCard;
import com.xiaobudian.yamikitchen.domain.merchant.Merchant;
import com.xiaobudian.yamikitchen.domain.order.OrderPostHandler;
import com.xiaobudian.yamikitchen.domain.order.Order;
import com.xiaobudian.yamikitchen.domain.order.OrderDetail;
import com.xiaobudian.yamikitchen.repository.account.AccountRepository;
import com.xiaobudian.yamikitchen.repository.account.AlipayHistoryRepository;
import com.xiaobudian.yamikitchen.repository.account.TransactionFlowRepository;
import com.xiaobudian.yamikitchen.repository.member.BankCardRepository;
import com.xiaobudian.yamikitchen.repository.merchant.MerchantRepository;
import com.xiaobudian.yamikitchen.repository.order.OrderRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.text.MessageFormat;
import java.util.List;

/**
 * Created by Johnson on 2015/5/15.
 */
@Service(value = "accountService")
public class AccountServiceImpl implements AccountService {
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

    public void writePaymentHistory(AlipayHistory history) {
        AlipayHistory his = alipayHistoryRepository.save(history);
        payOrder(his.getTrade_no());

//        Long merchantId = orderDetail.getOrder().getMerchantId();
//        Long uid = merchantRepository.getOne(merchantId).getCreator();
//        Account account = accountRepository.findByUidAndType(uid, AccountType.WAIT_CONFIRM);
//        TransactionFlow flow = new TransactionFlow(account.getId(), his.getOut_trade_no(), merchantId, uid, NumberUtils.createDouble(his.getPrice()), 0);
//        writeTransactionFlow(flow);
    }

    private void payOrder(String orderNo) {
        OrderDetail detail = orderRepository.findByOrderNoWithDetail(orderNo);
        detail.getOrder().pay();
        orderRepository.save(detail.getOrder());
        orderPostHandler.handle(detail);
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
    public BankCard getBindingBankCard(Long uid) {
        return bankCardRepository.findByUid(uid);
    }


    @Override
    public TransactionFlow writeTransactionFlow(TransactionFlow flow) {
        return transactionFlowRepository.save(flow);
    }

    @Override
    public List<TransactionFlow> getTransactionFlowsBy(Long accountId) {
        return transactionFlowRepository.findByAccount(accountId);
    }
}
