package com.xiaobudian.yamikitchen.domain.account;

import com.xiaobudian.yamikitchen.domain.member.BankCard;

/**
 * Created by Johnson on 2015/5/20.
 */
public class AccountSummary {
    private Double waitConfirmAmount;
    private Double withdrawAmount;
    private Double cashAmount;
    private Double turnover;
    private BankCard bankCard;

    public AccountSummary() {
    }

    public AccountSummary(Double waitConfirmAmount, Double withdrawAmount, Double balanceAmount, Double turnover, BankCard bankCard) {
        this();
        this.waitConfirmAmount = waitConfirmAmount;
        this.withdrawAmount = withdrawAmount;
        this.cashAmount = balanceAmount;
        this.turnover = turnover;
        this.bankCard = bankCard;
    }

    public Double getWaitConfirmAmount() {
        return waitConfirmAmount;
    }

    public void setWaitConfirmAmount(Double waitConfirmAmount) {
        this.waitConfirmAmount = waitConfirmAmount;
    }

    public Double getWithdrawAmount() {
        return withdrawAmount;
    }

    public void setWithdrawAmount(Double withdrawAmount) {
        this.withdrawAmount = withdrawAmount;
    }

    public Double getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(Double cashAmount) {
        this.cashAmount = cashAmount;
    }

    public Double getTurnover() {
        return turnover;
    }

    public void setTurnover(Double turnover) {
        this.turnover = turnover;
    }

    public BankCard getBankCard() {
        return bankCard;
    }

    public void setBankCard(BankCard bankCard) {
        this.bankCard = bankCard;
    }
}
