package com.xiaobudian.yamikitchen.domain.account;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "[RefundForAlipay]")
public class RefundForAlipay {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	private String tradeNo;
	private String price;
	private String orderNo;
	private String remark;
	private Long uid;
	private Boolean hasRefund = false;
	private Date createDate;
	private Date disposeDate;
	
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
