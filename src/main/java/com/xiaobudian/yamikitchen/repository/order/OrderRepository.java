package com.xiaobudian.yamikitchen.repository.order;

import com.xiaobudian.yamikitchen.domain.order.Order;
import com.xiaobudian.yamikitchen.domain.order.OrderDetail;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by Johnson on 2015/4/22.
 */
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("select o from Order o where o.merchantId = ?1 and o.status in (?2) and o.createDate>=?3 and o.createDate<=?4 order by o.status ASC, o.createDate DESC")
    public List<Order> findOrdersBy(Long merchantId, Collection<Integer> statuses, Date dateFrom, Date dateTo, Pageable pageable);

    @Query("from Order o, OrderItem oi where o.orderNo = oi.orderNo and o.merchantId = ?1 and o.status in (?2) and o.createDate>=?3 and o.createDate<=?4 order by o.status ASC, o.createDate DESC")
    public List<OrderDetail> findOrdersWithDetail(Long merchantId, Collection<Integer> statuses, Date dateFrom, Date dateTo, Pageable pageable);

    @Query("select o from Order o where o.uid=?1 order by o.createDate desc")
    public List<Order> findByUid(Long uid);

    @Query("from Order o , OrderItem oi where o.orderNo = oi.orderNo and o.status = 2 and o.uid = ?1")
    public List<OrderDetail> findUnconfirmedOrders(Long uid);

    @Query("from Order o , OrderItem oi where o.orderNo = oi.orderNo and o.status = 2 and o.uid = ?1 and createDate >= ?2 order by createDate desc")
    public List<OrderDetail> findUnconfirmedOrders(Long uid, Date createDate);

    public Order findByOrderNo(String orderNo);

    @Query("select o, oi from Order o , OrderItem oi where o.orderNo = oi.orderNo and o.orderNo = ?1")
    public OrderDetail findByOrderNoWithDetail(String orderNo);

    @Query("select o from Order o where o.uid=?1 and o.createDate >=?2 order by o.createDate desc")
    public List<Order> findByUidAndCreateDateAfter(Long uid, Date date);

    @Query("select o from Order o where o.uid=?1 and o.status in ?2 order by o.createDate desc")
    public List<Order> findByUidAndStatusIn(Long uid, Collection<Integer> statuses);

    @Query("select o from Order o where o.uid=?1 and o.status = ?2 and o.commentable = true order by o.createDate desc")
    public List<Order> findByUidAndStatusAndCommentableTrue(Long uid, int status);
}
