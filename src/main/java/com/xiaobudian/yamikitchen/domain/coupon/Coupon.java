package com.xiaobudian.yamikitchen.domain.coupon;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by johnson1 on 5/4/15.
 */
@Entity
public class Coupon implements Serializable {
    private static final long serialVersionUID = 4324002654818738897L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long uid;
    private String name;
    private Double amount;
    private Double usageCondition;
    private Date availableDate = new Date();
    private Date expireDate;
    private String phone;
    private boolean locked = false;
    private boolean hasUsed = false;
    private String orderNo;

    public Coupon() {
    }

    public Coupon(Long uid, String name, Double amount, Double usageCondition, Date expireDate, String phone) {
        this();
        this.uid = uid;
        this.name = name;
        this.amount = amount;
        this.usageCondition = usageCondition;
        this.expireDate = expireDate;
        this.phone = phone;
    }

    public Coupon(CouponType couponType, CouponAccount account) {
        this(account.getUid(), couponType.getName(), couponType.getAmount(), couponType.getUsageCondition(), couponType.actualExpireDate(), account.getMobile());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getUsageCondition() {
        return usageCondition;
    }

    public void setUsageCondition(Double usageCondition) {
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isHasUsed() {
        return hasUsed;
    }

    public void setHasUsed(boolean hasUsed) {
        this.hasUsed = hasUsed;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
