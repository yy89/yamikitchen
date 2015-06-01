package com.xiaobudian.yamikitchen.repository.order;

import com.xiaobudian.yamikitchen.domain.order.Order;
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
    @Query("from Order o where o.merchantId = ?1 and o.status in (?2) and o.createDate>=?3 and o.createDate<=?4 order by o.status ASC, o.createDate DESC")
    public List<Order> findOrders(Long merchantId, Collection<Integer> statuses, Date d1, Date d2, Pageable pageable);

    @Query("from Order o where o.merchantId = ?1 and o.status in (?2) order by o.paymentDate DESC")
    public List<Order> findOrders(Long merchantId, Collection<Integer> statuses);

    @Query("from Order o where o.merchantId = ?1 and o.status in (?2) and o.paymentDate >=?3 order by o.paymentDate DESC")
    public List<Order> findByPaymentDate(Long merchantId, Collection<Integer> statuses, Date p1);

    @Query("select o from Order o where o.uid=?1 order by o.createDate desc")
    public List<Order> findByUid(Long uid);

    public int countByUidAndHasPaidTrue(Long uid);

    public Order findByOrderNo(String orderNo);

    @Query("select o from Order o where o.uid=?1 and o.createDate >=?2 order by o.createDate desc")
    public List<Order> findByUidAndCreateDateAfter(Long uid, Date date);

    @Query("select o from Order o where o.uid=?1 and o.status in ?2 order by o.createDate desc")
    public List<Order> findByUidAndStatusIn(Long uid, Collection<Integer> statuses);

    @Query("select o from Order o where o.uid=?1 and o.status = ?2 and o.commentable = true order by o.createDate desc")
    public List<Order> findByUidAndStatusAndCommentableTrue(Long uid, int status);
}
