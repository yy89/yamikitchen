package com.xiaobudian.yamikitchen.domain;

import java.util.Date;

/**
 * Created by Johnson on 2015/5/3.
 */
public class CouponSummary {
    private Long id;
    private Long couponId;
    private String name;
    private Long usedTimes;
    private Long receivedTimes;
    private Long amount;
    private Date day = new Date();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUsedTimes() {
        return usedTimes;
    }

    public void setUsedTimes(Long usedTimes) {
        this.usedTimes = usedTimes;
    }

    public Long getReceivedTimes() {
        return receivedTimes;
    }

    public void setReceivedTimes(Long receivedTimes) {
        this.receivedTimes = receivedTimes;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }
}
