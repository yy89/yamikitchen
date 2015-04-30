package com.xiaobudian.yamikitchen.service;

import com.xiaobudian.yamikitchen.domain.Order;
import com.xiaobudian.yamikitchen.domain.OrderItem;
import com.xiaobudian.yamikitchen.domain.cart.Cart;

import java.util.List;

/**
 * Created by johnson1 on 4/27/15.
 */
public interface OrderService {
    public Cart addProductInCart(Long uid, Long rid, Long productId);

    public Cart removeProductInCart(Long id, Long rid, Long productId);

    public Cart getCart(Long id);

    public boolean removeCart(Long id);
}
