package com.xiaobudian.yamikitchen.domain.cart;

import com.xiaobudian.yamikitchen.domain.order.OrderItem;
import org.apache.commons.lang3.math.NumberUtils;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.*;

/**
 * Created by johnson1 on 4/27/15.
 */
public class Cart implements Serializable {
    private static final long serialVersionUID = -7529238667745324994L;
    public static final String ZERO = "0";
    private Long uid;
    private Long merchantId;
    private String merchantName;
    private Long totalAmount = 0l;
    private Integer totalQuantity = 0;
    private Integer deliverMethod = 0;
    private Integer paymentMethod = 0;
    private List<OrderItem> items = new ArrayList<>();
    @Transient
    private Map<String, String> extra = new HashMap<>();

    public Cart() {
    }

    public Cart(Long uid, Long merchantId, String merchantName, Long totalAmount, List<OrderItem> items) {
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

    public Long getTotalAmount() {
        for (OrderItem item : getItems()) {
            totalAmount += item.getAmount();
        }
        return totalAmount + (deliverMethod == 1 ? 0l : deliverPrice());
    }

    public Integer getTotalQuantity() {
        for (OrderItem item : getItems()) {
            totalQuantity += item.getQuantity();
        }
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public Map<String, String> getExtra() {
        return extra;
    }

    public void setExtra(Map<String, String> extra) {
        this.extra = extra;
    }

    public void putExtra(String key, String value) {
        extra.put(key, value);
    }

    public Integer getDeliverMethod() {
        return deliverMethod;
    }

    public void setDeliverMethod(Integer deliverMethod) {
        this.deliverMethod = deliverMethod;
    }

    public Integer getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(Integer paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Long deliverPrice() {
        String priceString = extra.get(new ArrayList<>(extra.keySet()).get(0));
        return NumberUtils.createLong(priceString);
    }
}
