package com.xiaobudian.yamikitchen.repository;

import com.xiaobudian.yamikitchen.domain.merchant.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Johnson on 2015/4/22.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {
    public List<Product> findByMerchantId(Long merchantId, Pageable pageable);
}
