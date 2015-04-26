package com.xiaobudian.yamikitchen.repository;

import com.xiaobudian.yamikitchen.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by Johnson on 2015/4/22.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Modifying
    @Query("update Product p set isExist = false where id = :id")
    public void putOff(@Param("id")long id);

    @Modifying
    @Query("update Product p set isExist = false where id = :id")
    public void putOn(@Param("id")long id);
}
