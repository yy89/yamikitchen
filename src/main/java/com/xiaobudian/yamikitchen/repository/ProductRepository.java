package com.xiaobudian.yamikitchen.repository;

import com.xiaobudian.yamikitchen.domain.merchant.Merchant;
import com.xiaobudian.yamikitchen.domain.merchant.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Johnson on 2015/4/22.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {
    public List<Product> findByMerchantId(Long merchantId, Pageable pageable);

    public List<Product> findByMerchantIdAndMainIsTrue(Long merchantId);

    @Modifying
    @Query("update  Product set isDelete = true where merchantId = ?1")
    @Transactional
    public void removeByMerchantId(long merchantId);

    @Modifying
    @Query("update Product set isDelete = true where id = ?1")
    @Transactional
    public void removeById(long pid);

}
