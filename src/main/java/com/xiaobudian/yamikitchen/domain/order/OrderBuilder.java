package com.xiaobudian.yamikitchen.domain.order;

import com.xiaobudian.yamikitchen.common.Util;
import com.xiaobudian.yamikitchen.domain.cart.Cart;
import com.xiaobudian.yamikitchen.domain.member.User;
import com.xiaobudian.yamikitchen.domain.merchant.Merchant;
import com.xiaobudian.yamikitchen.domain.merchant.UserAddress;

/**
 * Created by Johnson on 2015/5/12.
 */
public class OrderBuilder {

    private final Order order;

    public OrderBuilder(Order order) {
        this.order = order;
        order.setStatus(order.getPaymentMethod() == 0 ? 1 : 2);
    }

    public OrderBuilder merchant(Merchant merchant) {
        this.order.setMerchantName(merchant.getName());
        this.order.setMerchantId(merchant.getId());
        this.order.setMerchantNo(merchant.getMerchantNo());
        this.order.setMerchantHeadPic(merchant.getHeadPic());
        this.order.setMerchantPhone(merchant.getPhone());
        this.order.setAddress(merchant.getAddress());
        return this;
    }

    public OrderBuilder cart(Cart cart) {
        this.order.setPrice(cart.getTotalAmount());
        this.order.setDeliverPrice(cart.deliverPrice());
        this.order.setTotalQuantity(cart.getTotalQuantity());
        return this;
    }

    public OrderBuilder user(User user) {
        this.order.setHeadPic(user.getHeadPic());
        this.order.setNickName(user.getNickName());
        return this;
    }

    public OrderBuilder address(UserAddress address) {
        this.order.setPhone(address.getPhone());
        this.order.setName(address.getName());
        this.order.setLatitude(address.getLatitude());
        this.order.setLongitude(address.getLongitude());
        return this;
    }

    public OrderBuilder distance(Merchant merchant) {
        this.order.setDistance(Util.calculateDistanceAsString(merchant.getLongitude(), order.getLongitude(), merchant.getLatitude(), order.getLatitude()));
        return this;
    }

    public OrderBuilder orderNo(String orderNo) {
        this.order.setOrderNo(orderNo);
        return this;
    }

    public Order build() {
        return this.order;
    }
}
