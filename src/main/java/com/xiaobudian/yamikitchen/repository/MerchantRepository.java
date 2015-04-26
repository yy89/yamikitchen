package com.xiaobudian.yamikitchen.repository;

import com.xiaobudian.yamikitchen.domain.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by Johnson on 2015/4/22.
 */
public interface MerchantRepository extends JpaRepository<Merchant, Long> {
    @Modifying
    @Query("update Merchant set isRest = true where id = :id")
    public void rest(@Param("id")long id);

    @Modifying
    @Query("update Merchant set isRest = false where id = :id")
    public void reopen(@Param("id")long id);
}
