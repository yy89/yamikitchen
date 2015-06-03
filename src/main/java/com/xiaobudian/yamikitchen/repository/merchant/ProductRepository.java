package com.xiaobudian.yamikitchen.repository.merchant;

import com.xiaobudian.yamikitchen.domain.merchant.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Johnson on 2015/4/22.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("select p from Product p where p.removed <> true and p.merchantId =?1")
    public List<Product> findByMerchantId(Long merchantId, Pageable pageable);

    @Query("select p from Product p where p.removed <> true and p.available<>false and p.merchantId =?1")
    public List<Product> findAvailableByMerchantId(Long merchantId, Pageable pageable);

    @Query("select p from Product p where p.main = true and p.removed <> true and p.merchantId =?1")
    public List<Product> findMainProducts(Long merchantId);

    @Modifying
    @Query("update  Product set removed = true where merchantId = ?1")
    @Transactional
    public void removeByMerchantId(long merchantId);

    @Modifying
    @Query("update Product set removed = true where id = ?1")
    @Transactional
    public void removeById(long pid);

    @Modifying
    @Query("update Product p set p.main = false where p.merchantId = ?1")
    @Transactional
    public void disableMain(Long merchantId);

    @Modifying
    @Query("update Product p set p.restCount = p.twRestCount, p.twRestCount = p.supplyPerDay")
    @Transactional
    public void updateRest();
}
