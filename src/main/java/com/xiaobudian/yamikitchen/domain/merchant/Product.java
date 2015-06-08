package com.xiaobudian.yamikitchen.domain.merchant;

import com.xiaobudian.yamikitchen.common.Util;
import com.xiaobudian.yamikitchen.domain.cart.Cart;
import org.apache.commons.lang3.ArrayUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Johnson on 2015/4/22.
 */
@Entity
public class Product implements Serializable {
    private static final long serialVersionUID = 5001979457200046819L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long merchantId;
    private String name;
    private Double price;
    private String summary;
    private String pictures;
    private String availableTime;
    private Long restCount;
    private Long twRestCount;
    private String tags;
    private Long soldCount;
    private Long supplyPerDay;
    private Long commentCount;
    private Long favoriteCount;
    private Boolean available;
    private Boolean removed;
    private Boolean main;
    private boolean soldOut;
    private Date createDate = new Date();
    private Date lastModifiedDate = new Date();

    public Product() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    public String getAvailableTime() {
        return availableTime;
    }

    public void setAvailableTime(String availableTime) {
        this.availableTime = availableTime;
    }

    public Long getRestCount() {
        return restCount;
    }

    public void setRestCount(Long restCount) {
        this.restCount = restCount;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Long getSoldCount() {
        return soldCount;
    }

    public void setSoldCount(Long soldCount) {
        this.soldCount = soldCount;
    }

    public Long getSupplyPerDay() {
        return supplyPerDay;
    }

    public void setSupplyPerDay(Long supplyPerDay) {
        this.supplyPerDay = supplyPerDay;
    }

    public Long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Long commentCount) {
        this.commentCount = commentCount;
    }

    public Long getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(Long favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Boolean getRemoved() {
        return removed;
    }

    public void setRemoved(Boolean removed) {
        this.removed = removed;
    }

    public Boolean getMain() {
        return main;
    }

    public void setMain(Boolean main) {
        this.main = main;
    }

    public Long getTwRestCount() {
        return twRestCount;
    }

    public void setTwRestCount(Long twRestCount) {
        this.twRestCount = twRestCount;
    }

    public boolean isSoldOut(boolean isToday) {
        return isToday ? restCount < 1 : twRestCount < 1;
    }

    public boolean isSoldOut(boolean isToday, Cart cart) {
        if (cart == null) return isSoldOut(isToday);
        return isSoldOut(isToday) || (isToday ? cart.getQuantityBy(id) > restCount - 1 : cart.getQuantityBy(id) > twRestCount - 1);
    }

    public boolean isSoldOut() {
        return soldOut;
    }

    public void setSoldOut(boolean soldOut) {
        this.soldOut = soldOut;
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

    public void init() {
        available = true;
        favoriteCount = 0l;
        commentCount = 0l;
        soldCount = 0l;
        removed = false;
        main = false;
        restCount = supplyPerDay;
        twRestCount = supplyPerDay;
    }
}
