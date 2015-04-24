package com.xiaobudian.yamikitchen.repository;

import com.xiaobudian.yamikitchen.domain.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Johnson on 2015/4/22.
 */
public interface MerchantRepository extends JpaRepository<Merchant, Long> {
}
