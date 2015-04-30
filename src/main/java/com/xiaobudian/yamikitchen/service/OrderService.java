package com.xiaobudian.yamikitchen.service;

import com.xiaobudian.yamikitchen.domain.Order;
import com.xiaobudian.yamikitchen.domain.OrderItem;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

/**
 * Created by johnson1 on 4/27/15.
 */
public interface OrderService {
    public List<OrderItem> addProductInCart(Long uid, Long rid, Long productId);

    public List<OrderItem> removeProductInCart(Long id, Long rid, Long productId);

    public List<OrderItem> getItemsInCart(Long id);

    public List<Order> getOrdersByMerchantIdAndStatusAndCreateDateBetween(int page, int pageSize,long rid,long status,Date dateFrom,Date dateTo);

    public List<OrderItem>  getItemsInOrder(String orderNo);


}
