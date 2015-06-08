package com.xiaobudian.yamikitchen.domain.account;

import com.xiaobudian.yamikitchen.domain.order.Order;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Johnson on 2015/6/2.
 */
@Entity
public class RefundForAlipay implements Serializable {
    private static final long serialVersionUID = -2349381605676776409L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tradeNo;
    private String price;
    private String orderNo;
    private String remark;
    private Long uid;
    private Boolean hasRefund = false;
    private Date createDate = new Date();
    private Date disposeDate;

    public RefundForAlipay() {
    }

    public RefundForAlipay(Order order, AlipayHistory history) {
        this.orderNo = order.getOrderNo();
        this.uid = order.getUid();
        this.price = history.getPrice();
        this.tradeNo = history.getTrade_no();
        this.setHasRefund(false);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Boolean getHasRefund() {
        return hasRefund;
    }

    public void setHasRefund(Boolean hasRefund) {
        this.hasRefund = hasRefund;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getDisposeDate() {
        return disposeDate;
    }

    public void setDisposeDate(Date disposeDate) {
        this.disposeDate = disposeDate;
    }
}