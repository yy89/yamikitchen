package com.xiaobudian.yamikitchen.domain.cart;

import com.xiaobudian.yamikitchen.domain.merchant.Merchant;
import com.xiaobudian.yamikitchen.domain.order.OrderItem;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.util.CollectionUtils;

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
    private Long totalAmount = 0l;
    private Integer totalQuantity = 0;
    private Integer deliverMethod = 0;
    private Integer paymentMethod = 0;
    private boolean today = true;
    private List<OrderItem> items = new ArrayList<>();
    private Map<String, String> extra = new HashMap<>();

    public Cart() {
    }

    public Cart(Long uid, Merchant merchant, String extraFieldName, String deliverPrice, boolean today) {
        this();
        this.uid = uid;
        this.merchantId = merchant.getId();
        this.merchantName = merchant.getName();
        this.today = today;
        putExtra(extraFieldName, deliverPrice);
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
        totalAmount = 0l;
        for (OrderItem item : getItems()) {
            totalAmount += item.getAmount();
        }
        totalAmount += (deliverMethod == 1 ? 0l : deliverPrice());
        return totalAmount;
    }

    public Integer getTotalQuantity() {
        totalQuantity = 0;
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

    public boolean isToday() {
        return today;
    }

    public void setToday(boolean today) {
        this.today = today;
    }

    public void addItem(OrderItem orderItem) {
        for (OrderItem item : getItems()) {
            if (!item.getProductId().equals(orderItem.getProductId())) continue;
            item.setQuantity(item.getQuantity() + 1);
            return;
        }
        items.add(orderItem);
    }

    public void removeItem(Long productId) {
        for (OrderItem item : getItems()) {
            if (!item.getProductId().equals(productId)) continue;
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
                return;
            }
            items.remove(item);
            return;
        }
    }

    public Long deliverPrice() {
        String priceString = extra.get(new ArrayList<>(extra.keySet()).get(0));
        return CollectionUtils.isEmpty(items) ? 0l : NumberUtils.createLong(priceString);
    }
}
