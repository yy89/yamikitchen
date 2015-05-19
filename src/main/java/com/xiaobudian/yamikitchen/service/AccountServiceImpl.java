package com.xiaobudian.yamikitchen.service;

import com.xiaobudian.yamikitchen.common.Util;
import com.xiaobudian.yamikitchen.domain.account.Account;
import com.xiaobudian.yamikitchen.domain.account.AccountType;
import com.xiaobudian.yamikitchen.domain.account.AlipayHistory;
import com.xiaobudian.yamikitchen.domain.account.TransactionFlow;
import com.xiaobudian.yamikitchen.domain.order.AsyncPostHandler;
import com.xiaobudian.yamikitchen.domain.order.Order;
import com.xiaobudian.yamikitchen.domain.order.OrderDetail;
import com.xiaobudian.yamikitchen.repository.account.AccountRepository;
import com.xiaobudian.yamikitchen.repository.account.AlipayHistoryRepository;
import com.xiaobudian.yamikitchen.repository.account.TransactionFlowRepository;
import com.xiaobudian.yamikitchen.repository.merchant.MerchantRepository;
import com.xiaobudian.yamikitchen.repository.order.OrderRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
    private AsyncPostHandler asyncPostHandler;
    @Inject
    private AccountRepository accountRepository;
    @Inject
    private TransactionFlowRepository transactionFlowRepository;
    @Inject
    private MerchantRepository merchantRepository;


    public void writePaymentHistory(AlipayHistory history) {
        AlipayHistory his = alipayHistoryRepository.save(history);
        OrderDetail orderDetail = orderRepository.findByOrderNoWithDetail(his.getOut_trade_no());
        orderDetail.getOrder().setPayable(false);
        orderDetail.getOrder().setHasPaid(true);
        orderDetail.getOrder().setStatus(2);
        orderRepository.save(orderDetail.getOrder());
        Long merchantId = orderDetail.getOrder().getMerchantId();
        Long uid = merchantRepository.getOne(merchantId).getCreator();
        asyncPostHandler.handle(orderDetail.getOrder(), orderDetail.getItems());
        Account account = accountRepository.findByUidAndType(uid, AccountType.WAIT_CONFIRM);
        if (account == null) return;
        TransactionFlow flow = new TransactionFlow(account.getId(), his.getOut_trade_no(), merchantId, uid, his.getPrice(), 0);
        writeTransactionFlow(flow);
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
    public TransactionFlow writeTransactionFlow(TransactionFlow flow) {
        return transactionFlowRepository.save(flow);
    }

    @Override
    public List<TransactionFlow> getTransactionFlowsBy(Long accountId) {
        return transactionFlowRepository.findByAccount(accountId);
    }
}
