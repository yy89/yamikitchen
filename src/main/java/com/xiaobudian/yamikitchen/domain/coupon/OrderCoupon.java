package com.xiaobudian.yamikitchen.domain.coupon;

import com.xiaobudian.yamikitchen.domain.member.WeChatUser;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Johnson on 2015/5/23.
 */
@Entity
public class OrderCoupon implements Serializable {
    private static final long serialVersionUID = -4912941180248632782L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderNo;
    private String mobile;
    private Double amount;
    private Date receivedDate = new Date();
    private String weChatId;
    private String weChatNickName;
    private String weChatHeadPic;
    private Long uid;
    private Long couponId;

    public OrderCoupon() {
    }

    public OrderCoupon(String orderNo, String mobile, Double amount, Date receivedDate, String weChatId, String weChatNickName, String weChatHeadPic, Long uid, Long couponId) {
        this();
        this.orderNo = orderNo;
        this.mobile = mobile;
        this.amount = amount;
        this.receivedDate = receivedDate;
        this.weChatId = weChatId;
        this.weChatNickName = weChatNickName;
        this.weChatHeadPic = weChatHeadPic;
        this.uid = uid;
        this.couponId = couponId;
    }

    public OrderCoupon(String orderNo, WeChatUser weChatUser, Coupon coupon) {
        this(orderNo, weChatUser.getMobile(), coupon.getAmount(), new Date(), weChatUser.getOpenId(),
                weChatUser.getNickName(), weChatUser.getHeadImgUrl(), coupon.getUid(), coupon.getId());
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getWeChatId() {
        return weChatId;
    }

    public void setWeChatId(String weChatId) {
        this.weChatId = weChatId;
    }

    public String getWeChatNickName() {
        return weChatNickName;
    }

    public void setWeChatNickName(String weChatNickName) {
        this.weChatNickName = weChatNickName;
    }

    public String getWeChatHeadPic() {
        return weChatHeadPic;
    }

    public void setWeChatHeadPic(String weChatHeadPic) {
        this.weChatHeadPic = weChatHeadPic;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }
}
