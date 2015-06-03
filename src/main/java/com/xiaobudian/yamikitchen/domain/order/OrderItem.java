package com.xiaobudian.yamikitchen.domain.order;

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
    private Double price = 0.00d;
    private Integer quantity = 0;
    private Double amount = 0.00d;

    public OrderItem() {
    }

    public OrderItem(Product product, Integer quantity) {
        this();
        this.productId = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.quantity = quantity;
        this.amount = quantity * price;
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

    public Double getPrice() {
        return price;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setPrice(Double price) {
        this.price = price;
        amount = quantity * price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        amount = quantity * price;
    }
}
