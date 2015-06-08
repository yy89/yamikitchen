package com.xiaobudian.yamikitchen.domain.member;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Johnson on 2015/6/1.
 */
@Entity
public class WeChatUser implements Serializable {
    private static final long serialVersionUID = -7314976135144768062L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String openId;
    private String nickName;
    private String province;
    private String city;
    private String country;
    private String headImgUrl;
    private String mobile;
    private Date createDate = new Date();

    public WeChatUser() {
    }

    public WeChatUser(String openId, String nickName, String province, String city, String country, String headImgUrl) {
        this();
        this.openId = openId;
        this.nickName = nickName;
        this.province = province;
        this.city = city;
        this.country = country;
        this.headImgUrl = headImgUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public boolean hasMobile() {
        return StringUtils.isNotEmpty(mobile);
    }

    public String forwardUrlPrefix() {
        return hasMobile() ? "hongbao" : "mobile";
    }
}
