package com.xiaobudian.yamikitchen.service;

import com.xiaobudian.yamikitchen.domain.account.Account;
import com.xiaobudian.yamikitchen.domain.account.AccountSummary;
import com.xiaobudian.yamikitchen.domain.account.AlipayHistory;
import com.xiaobudian.yamikitchen.domain.account.TransactionFlow;
import com.xiaobudian.yamikitchen.domain.member.Bank;
import com.xiaobudian.yamikitchen.domain.member.BankCard;
import com.xiaobudian.yamikitchen.domain.operation.PlatformAccount;
import com.xiaobudian.yamikitchen.domain.operation.PlatformTransactionFlow;
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

    public PlatformAccount getPlatformAccount();

    public BankCard getBindingBankCard(Long uid);

    public TransactionFlow writeTransactionFlow(TransactionFlow flow);

    public List<TransactionFlow> getTransactionFlowsBy(String orderNo);

    public List<PlatformTransactionFlow> getTransactionFlowsOfPlatform();

    public List<TransactionFlow> getTransactionFlows(Long uid);

	public void refundOrder(Order order);

    public Bank getBankByName(String bankName);

    public Bank getBankByBinCode(String binCode);


}
