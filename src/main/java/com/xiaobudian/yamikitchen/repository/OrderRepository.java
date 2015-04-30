package com.xiaobudian.yamikitchen.repository;

import com.xiaobudian.yamikitchen.domain.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by Johnson on 2015/4/22.
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    public List<Order> findByMerchantIdAndStatusInAndCreateDateBetween(long merchantId,List<Integer> statuses,Date dateFrom ,Date dateTo,Pageable pageable);

    public List<Order> findByMerchantIdAndStatusAndCreateDateBetween(long merchantId,Integer status,Date dateFrom ,Date dateTo,Pageable pageable);

}
