package com.xiaobudian.yamikitchen.domain.order;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.joda.time.DateTime;
import org.joda.time.Days;

/**
 * Created by Johnson on 2015/4/22.
 */
@Entity
@Table(name = "[order]")
public class Order implements Serializable {
    private static final long serialVersionUID = -7595215227337455292L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderNo;
    private Integer totalQuantity;
    private Long price;
    private Date createDate = new Date();
    private Date deliverDate;
    private Date outDate;
    private Date expectDate;
    @NotNull(message = "order.paymentMethod.not.empty")
    private Integer paymentMethod;
    private Date acceptDate;
    private Long uid;
    private String nickName;
    private String headPic;
    private String phone;
    private Long merchantId;
    private String merchantNo;
    private String merchantName;
    private String merchantHeadPic;
    private String merchantPhone;
    private String merchantAddress;
    private String remark;
    private Long deliverPrice;
    @NotNull(message = "order.deliverMethod.not.empty")
    private Integer deliverMethod;
    private Integer paymentTimeLimit = 15;
    private String address;
    private Long addressId;
    private String name;
    private Integer status = 1;
    private boolean cancelable = true;
    private boolean payable = true;
    private boolean hasPaid = false;
    private boolean complainable = false;
    private boolean commentable = false;
    private boolean refundable;
    private String distance;
    private String checkFlag = "I";
    private Long couponId;
    private boolean firstDeal;
    private Double longitude;
    private Double latitude;
    private Integer deliverGroup;
    private boolean directCancelable = false;
    
    // 第三方配送机构的订单状态
    // 达达：1待接单 2待取货 3执行中 4已完成 5已取消
    private Integer deliverGroupOrderStatus;
    // 配送员id
    private Integer diliverymanId;
    // 配送员姓名
    private String diliverymanName;
    // 配送员手机
    private String diliverymanMobile;
    // 更新时间
    private Date updateTime;

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

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getDeliverDate() {
        return deliverDate;
    }

    public void setDeliverDate(Date deliverDate) {
        this.deliverDate = deliverDate;
    }

    public Date getOutDate() {
        return outDate;
    }

    public void setOutDate(Date outDate) {
        this.outDate = outDate;
    }

    public Integer getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(Integer paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantHeadPic() {
        return merchantHeadPic;
    }

    public void setMerchantHeadPic(String merchantHeadPic) {
        this.merchantHeadPic = merchantHeadPic;
    }

    public String getMerchantPhone() {
        return merchantPhone;
    }

    public void setMerchantPhone(String merchantPhone) {
        this.merchantPhone = merchantPhone;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getDeliverPrice() {
        return deliverPrice;
    }

    public void setDeliverPrice(Long deliverPrice) {
        this.deliverPrice = deliverPrice;
    }

    public Integer getPaymentTimeLimit() {
        return paymentTimeLimit;
    }

    public void setPaymentTimeLimit(Integer paymentTimeLimit) {
        this.paymentTimeLimit = paymentTimeLimit;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public boolean isCancelable() {
        return cancelable;
    }

    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    public boolean isPayable() {
        return payable;
    }

    public void setPayable(boolean payable) {
        this.payable = payable;
    }

    public boolean isHasPaid() {
        return hasPaid;
    }

    public void setHasPaid(boolean hasPaid) {
        this.hasPaid = hasPaid;
    }

    public boolean isComplainable() {
        return complainable;
    }

    public void setComplainable(boolean complainable) {
        this.complainable = complainable;
    }

    public boolean isCommentable() {
        return commentable;
    }

    public void setCommentable(boolean commentable) {
        this.commentable = commentable;
    }

    public String getCheckFlag() {
        return checkFlag;
    }

    public void setCheckFlag(String checkFlag) {
        this.checkFlag = checkFlag;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public boolean isFirstDeal() {
        return firstDeal;
    }

    public void setFirstDeal(boolean firstDeal) {
        this.firstDeal = firstDeal;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public boolean isRefundable() {
        return refundable;
    }

    public void setRefundable(boolean refundable) {
        this.refundable = refundable;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public Integer getDeliverMethod() {
        return deliverMethod;
    }

    public void setDeliverMethod(Integer deliverMethod) {
        this.deliverMethod = deliverMethod;
    }

    public Date getExpectDate() {
        return expectDate;
    }

    public void setExpectDate(Date expectDate) {
        this.expectDate = expectDate;
    }

    public Integer getDeliverGroup() {
        return deliverGroup;
    }

    public void setDeliverGroup(Integer deliverGroup) {
        this.deliverGroup = deliverGroup;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public Date getAcceptDate() {
        return acceptDate;
    }

    public void setAcceptDate(Date acceptDate) {
        this.acceptDate = acceptDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getMerchantAddress() {
        return merchantAddress;
    }

    public void setMerchantAddress(String merchantAddress) {
        this.merchantAddress = merchantAddress;
    }

    public void confirm() {
        setStatus(deliverMethod == 0 ? 3 : 6);
        this.setDirectCancelable(false);
        this.setAcceptDate(new Date());
    }

    public boolean isDirectCancelable() {
        return directCancelable;
    }

    public void setDirectCancelable(boolean directCancelable) {
        this.directCancelable = directCancelable;
    }

    @Transient
    public boolean isToday() {
        return expectDate != null && Days.daysBetween(DateTime.now(), new DateTime(expectDate)).getDays() == 0;
    }

    @Transient
    public boolean isTomorrow() {
        return expectDate != null && Days.daysBetween(DateTime.now(), new DateTime(expectDate)).getDays() == 1;
    }

	public Integer getDeliverGroupOrderStatus() {
		return deliverGroupOrderStatus;
	}

	public void setDeliverGroupOrderStatus(Integer deliverGroupOrderStatus) {
		this.deliverGroupOrderStatus = deliverGroupOrderStatus;
	}

	public Integer getDiliverymanId() {
		return diliverymanId;
	}

	public void setDiliverymanId(Integer diliverymanId) {
		this.diliverymanId = diliverymanId;
	}

	public String getDiliverymanName() {
		return diliverymanName;
	}

	public void setDiliverymanName(String diliverymanName) {
		this.diliverymanName = diliverymanName;
	}

	public String getDiliverymanMobile() {
		return diliverymanMobile;
	}

	public void setDiliverymanMobile(String diliverymanMobile) {
		this.diliverymanMobile = diliverymanMobile;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
