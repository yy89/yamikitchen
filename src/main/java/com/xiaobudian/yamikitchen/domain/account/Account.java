package com.xiaobudian.yamikitchen.domain.account;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Johnson on 2015/4/22.
 */
@Entity
public class Account implements Serializable {
    private static final long serialVersionUID = 1613210083709671526L;
    public static final String ACCOUNT_NO_PATTERN = "%d-%d-%d";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long uid;
    private String accountNo;
    private Long merchantId;
    private Double balance = 0.00d;
    private Double availableBalance = 0.00d;
    @Enumerated(EnumType.ORDINAL)
    private AccountType type;
    private Double cashLimit;
    private Integer methodOfCashFee;
    private int status = 1;
    private Date createDate = new Date();

    public Account() {

    }

    public Account(Long uid, String accountNo, AccountType type) {
        this.uid = uid;
        this.accountNo = accountNo;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(Double availableBalance) {
        this.availableBalance = availableBalance;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public Double getCashLimit() {
        return cashLimit;
    }

    public void setCashLimit(Double cashLimit) {
        this.cashLimit = cashLimit;
    }

    public Integer getMethodOfCashFee() {
        return methodOfCashFee;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public void setMethodOfCashFee(Integer methodOfCashFee) {
        this.methodOfCashFee = methodOfCashFee;
    }
}
