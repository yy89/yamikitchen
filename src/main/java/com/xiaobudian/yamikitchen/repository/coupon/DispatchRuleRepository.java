package com.xiaobudian.yamikitchen.repository.coupon;

import com.xiaobudian.yamikitchen.domain.coupon.DispatchRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Johnson on 2015/5/23.
 */
public interface DispatchRuleRepository extends JpaRepository<DispatchRule, Long> {
    public List<DispatchRule> findByAvailableIsTrue();
}
