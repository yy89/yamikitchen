package com.xiaobudian.yamikitchen.repository;

import com.xiaobudian.yamikitchen.domain.order.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by hackcoder on 2015/4/29.
 */
public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {

    public List<OrderItem> findByOrderNo(String orderNo);

}
