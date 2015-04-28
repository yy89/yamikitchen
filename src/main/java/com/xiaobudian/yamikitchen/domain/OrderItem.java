package com.xiaobudian.yamikitchen.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xiaobudian.yamikitchen.domain.merchant.Product;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by Johnson on 2015/4/22.
 */
@Entity
public class OrderItem implements Serializable {
    private static final long serialVersionUID = -1970139687740352118L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    @JsonIgnore
    private String orderNo;
    private Long productId;
    private String name;
    private Long price;
    private Integer quality;
    private Long amount;

    public OrderItem() {
    }

    public OrderItem(Product product, Integer quality) {
        this();
        this.productId = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.quality = quality;
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

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrice() {
        return price;
    }

    public Long getAmount() {
        return price * quality;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Integer getQuality() {
        return quality;
    }

    public void setQuality(Integer quality) {
        this.quality = quality;
    }
}
