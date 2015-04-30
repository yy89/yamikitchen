package com.xiaobudian.yamikitchen.domain.cart;

import com.xiaobudian.yamikitchen.domain.OrderItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnson1 on 4/27/15.
 */
public class Cart implements Serializable {
    private static final long serialVersionUID = -7529238667745324994L;
    private Long uid;
    private Long merchantId;
    private String merchantName;
    private Double totalAmount = 0.00d;
    private List<OrderItem> items = new ArrayList<>();

    public Cart() {
    }

    public Cart(Long uid, Long merchantId, String merchantName, Double totalAmount, List<OrderItem> items) {
        this();
        this.uid = uid;
        this.merchantId = merchantId;
        this.merchantName = merchantName;
        this.totalAmount = totalAmount;
        this.items = items;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
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

    public Double getTotalAmount() {
        Double sum = 0.00d;
        for (OrderItem item : getItems()) {
            sum += item.getAmount();
        }
        return sum;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }


}
