package com.xiaobudian.yamikitchen.domain.coupon;

import java.util.Date;

/**
 * Created by johnson1 on 5/4/15.
 */
public class Coupon {
    private Long id;
    private String name;
    private Long amount;
    private String usageCondition;
    private Date availableDate;
    private Date expireDate;
    private String phone;
    private boolean hasUsed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getUsageCondition() {
        return usageCondition;
    }

    public void setUsageCondition(String usageCondition) {
        this.usageCondition = usageCondition;
    }

    public Date getAvailableDate() {
        return availableDate;
    }

    public void setAvailableDate(Date availableDate) {
        this.availableDate = availableDate;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public boolean isHasUsed() {
        return hasUsed;
    }

    public void setHasUsed(boolean hasUsed) {
        this.hasUsed = hasUsed;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
