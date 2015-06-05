package com.xiaobudian.yamikitchen.domain.merchant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xiaobudian.yamikitchen.domain.account.Account;
import com.xiaobudian.yamikitchen.domain.account.AccountType;
import com.xiaobudian.yamikitchen.domain.order.Order;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Johnson on 2015/4/22.
 */
@Entity
public class Merchant implements Serializable {
    private static final long serialVersionUID = 6502202745663309545L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(value = "rid")
    private Long id;
    private String name;
    @Column(insertable = false, columnDefinition = "int default 0")
    private Integer type = 0;
    private String merchantNo;
    private String voiceIntroduction;
    private Double longitude;
    private Double latitude;
    private String address;
    private String headPic;
    private String phone;
    private String pictures;
    private Boolean messHall = false;
    private Integer countOfMessHall = 0;
    private Boolean selfPickup;
    private Boolean supportDelivery;
    @Column(insertable = false, columnDefinition = "bit default 0")
    private Boolean isRest = false;
    private String restMark;
    @Column(insertable = false, columnDefinition = "int default 1")
    private Integer mLevel = 1;
    @Column(insertable = false, columnDefinition = "bit default 0")
    private Boolean hasOrder = false;
    @Column(insertable = false, columnDefinition = "int default 0")
    private Integer soldCount = 0;
    @Column(insertable = false, columnDefinition = "int default 0")
    private Integer monthlySoldCount = 0;
    private String comment;
    private String description;
    private String tags;
    @Column(insertable = false, columnDefinition = "bigint default 0")
    private Long favoriteCount = 0l;
    @Column(insertable = false, columnDefinition = "bigint default 0")
    private Long commentCount = 0l;
    @Transient
    private String distance;
    @Column(unique = true, nullable = false)
    private Long creator;
    private String businessHours;
    private String goodCuisine;
    private String businessDayPerWeek;
    private Long deliverFee;
    private String deliverComment;
    @Column(insertable = false, columnDefinition = "bit default 0")
    private Boolean removed = false;
    @Column(insertable = false, columnDefinition = "int default 0")
    private Integer verifyStatus = 0;
    private double turnover = 0.00d;
    @JsonIgnore
    private double sharing;
    @Column(insertable = false, columnDefinition = "bit default 0")
    private Boolean isAutoOpen;
    private Date createDate;
    private Date lastModifiedDate = new Date();
    private double star = 0.00d;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getVoiceIntroduction() {
        return voiceIntroduction;
    }

    public void setVoiceIntroduction(String voiceIntroduction) {
        this.voiceIntroduction = voiceIntroduction;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    public Boolean getMessHall() {
        return messHall;
    }

    public void setMessHall(Boolean messHall) {
        this.messHall = messHall;
    }

    public Integer getCountOfMessHall() {
        return countOfMessHall;
    }

    public void setCountOfMessHall(Integer countOfMessHall) {
        this.countOfMessHall = countOfMessHall;
    }

    public Boolean getSelfPickup() {
        return selfPickup;
    }

    public void setSelfPickup(Boolean selfPickup) {
        this.selfPickup = selfPickup;
    }

    public Boolean getSupportDelivery() {
        return supportDelivery;
    }

    public void setSupportDelivery(Boolean supportDelivery) {
        this.supportDelivery = supportDelivery;
    }

    public Boolean getIsRest() {
        return isRest;
    }

    public void setIsRest(Boolean isRest) {
        this.isRest = isRest;
    }

    public String getRestMark() {
        return restMark;
    }

    public void setRestMark(String restMark) {
        this.restMark = restMark;
    }

    public Integer getmLevel() {
        return mLevel;
    }

    public void setmLevel(Integer mLevel) {
        this.mLevel = mLevel;
    }

    public Boolean getHasOrder() {
        return hasOrder;
    }

    public void setHasOrder(Boolean hasOrder) {
        this.hasOrder = hasOrder;
    }

    public Integer getSoldCount() {
        return soldCount;
    }

    public void setSoldCount(Integer soldCount) {
        this.soldCount = soldCount;
    }

    public Integer getMonthlySoldCount() {
        return monthlySoldCount;
    }

    public void setMonthlySoldCount(Integer monthlySoldCount) {
        this.monthlySoldCount = monthlySoldCount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Long getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(Long favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public Long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Long commentCount) {
        this.commentCount = commentCount;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public String getBusinessHours() {
        return businessHours;
    }

    public void setBusinessHours(String businessHours) {
        this.businessHours = businessHours;
    }

    public String getGoodCuisine() {
        return goodCuisine;
    }

    public void setGoodCuisine(String goodCuisine) {
        this.goodCuisine = goodCuisine;
    }

    public String getBusinessDayPerWeek() {
        return businessDayPerWeek;
    }

    public void setBusinessDayPerWeek(String businessDayPerWeek) {
        this.businessDayPerWeek = businessDayPerWeek;
    }

    public Long getDeliverFee() {
        return deliverFee;
    }

    public void setDeliverFee(Long deliverFee) {
        this.deliverFee = deliverFee;
    }

    public String getDeliverComment() {
        return deliverComment;
    }

    public void setDeliverComment(String deliverComment) {
        this.deliverComment = deliverComment;
    }

    public Boolean getRemoved() {
        return removed;
    }

    public void setRemoved(Boolean removed) {
        this.removed = removed;
    }

    public Integer getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(Integer verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    @Transient
    public boolean isCreateBy(Long uid) {
        return uid != null && uid.equals(creator);
    }

    public List<Account> createAccounts() {
        List<Account> result = new ArrayList<>();
        for (AccountType accountType : AccountType.values()) {
            final String accountNo = String.format(Account.ACCOUNT_NO_PATTERN, creator, id, accountType.ordinal());
            result.add(new Account(id, getCreator(), accountNo, accountType));
        }
        return result;
    }

    public double getTurnover() {
        return turnover;
    }

    public void setTurnover(double turnover) {
        this.turnover = turnover;
    }

    public double getSharing() {
        return sharing;
    }

    public void setSharing(double sharing) {
        this.sharing = sharing;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public void updateTurnOver(Order order) {
        this.turnover += order.getPrice();
    }

    public Boolean getIsAutoOpen() {
        return isAutoOpen;
    }

    public void setIsAutoOpen(Boolean isAutoOpen) {
        this.isAutoOpen = isAutoOpen;
    }

    public boolean isApproved() {
        return verifyStatus != null && verifyStatus == 1;
    }

    public double getStar() {
        return star;
    }

    public void setStar(double star) {
        this.star = star;
    }
}
