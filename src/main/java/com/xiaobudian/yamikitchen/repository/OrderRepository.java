package com.xiaobudian.yamikitchen.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xiaobudian.yamikitchen.domain.order.Order;
import com.xiaobudian.yamikitchen.domain.order.OrderDetail;

/**
 * Created by Johnson on 2015/4/22.
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    public List<Order> findByMerchantIdAndStatusInAndExpectDateBetween(long merchantId,List<Integer> statuses,Date dateFrom ,Date dateTo,Pageable pageable);

    List<Order> findByUid(Long uid);

    @Query("from Order o , OrderItem oi where o.orderNo = oi.orderNo and o.status = 2 and o.uid = ?1")
    List<OrderDetail> getUnconfirmedOrders(Long uid);
    
    @Query("from Order where id = ?1")
    Order getOrderById(Long orderId);
    
}
