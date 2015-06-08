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
public class CouponAccount implements Serializable {
    private static final long serialVersionUID = 7551158425529385813L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String mobile;
    private Long uid;
    private Boolean available = true;
    private Date createDate = new Date();
    private Date updateDate = new Date();

    public CouponAccount() {
    }

    public CouponAccount(Long uid, String mobile) {
        this();
        this.uid = uid;
        this.mobile = mobile;
    }

    public CouponAccount(String mobile) {
        this(null, mobile);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
