package com.xiaobudian.yamikitchen.service;

import com.xiaobudian.yamikitchen.domain.account.Account;
import com.xiaobudian.yamikitchen.domain.account.AccountSummary;
import com.xiaobudian.yamikitchen.domain.account.AlipayHistory;
import com.xiaobudian.yamikitchen.domain.account.TransactionFlow;
import com.xiaobudian.yamikitchen.domain.member.BankCard;
import com.xiaobudian.yamikitchen.domain.order.Order;

import java.util.List;

/**
 * Created by Johnson on 2015/5/15.
 */
public interface AccountService {
    public void writePaymentHistory(AlipayHistory history);

    public String getOrderStringOfAlipay(Order order);

    public List<Account> getAccounts(Long uid);

    public AccountSummary getAccountSummary(Long uid);

    public BankCard getBindingBankCard(Long uid);

    public TransactionFlow writeTransactionFlow(TransactionFlow flow);

    public List<TransactionFlow> getTransactionFlowsBy(Long accountId);

	public void unconfirmedOrderRefund(Order order);

}
