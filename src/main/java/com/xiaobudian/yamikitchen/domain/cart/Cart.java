package com.xiaobudian.yamikitchen.domain.cart;

import com.xiaobudian.yamikitchen.domain.order.OrderItem;
import org.apache.commons.lang3.math.NumberUtils;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by johnson1 on 4/27/15.
 */
public class Cart implements Serializable {
    private static final long serialVersionUID = -7529238667745324994L;
    private Long uid;
    private Long merchantId;
    private String merchantName;
    private Long totalAmount;
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
        Long sum = 0l;
        for (OrderItem item : getItems()) {
            sum += item.getAmount();
        }
        return sum + deliverPrice();
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

    public Long deliverPrice() {
        String priceString = extra.get(new ArrayList<>(extra.keySet()).get(0));
        return NumberUtils.createLong(priceString);
    }
}
