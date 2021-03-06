package com.xiaobudian.yamikitchen.domain.account;

import com.xiaobudian.yamikitchen.domain.order.Order;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Johnson on 2015/4/22.
 */
@Entity
public class TransactionFlow implements Serializable {
    private static final long serialVersionUID = 6863837446350831109L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderNo;
    private Long merchantId;
    private String accountNo;
    private Long uid;
    private Date operateDate = new Date();
    private Double amount;
    private Double currentBalance = 0.00d;
    private Double fee = 0.00d;
    private Integer transactionType;
    private String transactionName;

    public TransactionFlow() {
    }

    public TransactionFlow(String accountNo, String orderNo, Long merchantId, Long uid, Double amount, TransactionType type) {
        this.orderNo = orderNo;
        this.merchantId = merchantId;
        this.uid = uid;
        this.amount = amount;
        this.transactionType = type.getCode();
        this.transactionName = type.getName();
        this.accountNo = accountNo;
    }

    public TransactionFlow(Account account, Order order, double amount, TransactionType type) {
        this(account.getAccountNo(), order.getOrderNo(), order.getMerchantId(), account.getUid(), amount, type);
        this.currentBalance = account.getBalance();
    }

    public TransactionFlow(Account account, double amount, TransactionType type) {
        this(account, null, amount, type);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Date getOperateDate() {
        return operateDate;
    }

    public void setOperateDate(Date operateDate) {
        this.operateDate = operateDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(Double currentBalance) {
        this.currentBalance = currentBalance;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public Integer getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(Integer transactionType) {
        this.transactionType = transactionType;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }
}
