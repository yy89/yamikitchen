package com.xiaobudian.yamikitchen.domain.merchant;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;

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
    private Integer type;
    private String voiceIntroduction;
    private Double longitude;
    private Double latitude;
    private String address;
    private String headPic;
    private String phone;
    private String pictures;
    private boolean messHall;
    private Integer countOfMessHall;
    private boolean selfPickup;
    private boolean supportDelivery;
    private boolean isRest = true;
    private String restMark;
    private Integer mLevel = 1;
    private boolean hasOrder = false;
    private Integer soldCount;
    private Integer monthlySoldCount;
    private String comment;
    private String description;
    private String tags;
    private Long favoriteCount = 0l;
    private Long commentCount = 0l;
    @Transient
    private Double distance;
    private Long creator;

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

    public boolean isMessHall() {
        return messHall;
    }

    public void setMessHall(boolean messHall) {
        this.messHall = messHall;
    }

    public Integer getCountOfMessHall() {
        return countOfMessHall;
    }

    public void setCountOfMessHall(Integer countOfMessHall) {
        this.countOfMessHall = countOfMessHall;
    }

    public boolean isSelfPickup() {
        return selfPickup;
    }

    public void setSelfPickup(boolean selfPickup) {
        this.selfPickup = selfPickup;
    }

    public boolean isSupportDelivery() {
        return supportDelivery;
    }

    public void setSupportDelivery(boolean supportDelivery) {
        this.supportDelivery = supportDelivery;
    }

    public boolean isRest() {
        return isRest;
    }

    public void setRest(boolean isRest) {
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

    public boolean isHasOrder() {
        return hasOrder;
    }

    public void setHasOrder(boolean hasOrder) {
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

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }
}
