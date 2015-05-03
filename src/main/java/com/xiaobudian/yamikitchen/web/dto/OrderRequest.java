package com.xiaobudian.yamikitchen.web.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Johnson on 2015/5/2.
 */
public class OrderRequest implements Serializable {
    private static final long serialVersionUID = 1289599085868738616L;
    private Integer paymentMethod;
    private Integer deliverMethod;
    private Long couponId;
    private Date expectDate = new Date();
    private String address;
    private String remark;

    public OrderRequest() {
    }

    public OrderRequest(Integer paymentMethod, Integer deliverMethod, Long couponId) {
        this.paymentMethod = paymentMethod;
        this.deliverMethod = deliverMethod;
        this.couponId = couponId;
    }

    public Integer getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(Integer paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Integer getDeliverMethod() {
        return deliverMethod;
    }

    public void setDeliverMethod(Integer deliverMethod) {
        this.deliverMethod = deliverMethod;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public Date getExpectDate() {
        return expectDate;
    }

    public void setExpectDate(Date expectDate) {
        this.expectDate = expectDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
