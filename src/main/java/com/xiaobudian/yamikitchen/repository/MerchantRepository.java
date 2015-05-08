package com.xiaobudian.yamikitchen.repository;

import com.xiaobudian.yamikitchen.common.Result;
import com.xiaobudian.yamikitchen.domain.merchant.Merchant;
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
public interface MerchantRepository extends JpaRepository<Merchant, Long> {
    @Query("select m from Merchant m order by POWER(MOD(ABS(m.longitude - ?1),360),2) + POWER(ABS(m.latitude - ?2),2)")
    public List<Merchant> findByLongitudeAndLatitude(Double longitude, Double latitude, Pageable pageable);

    @Query("select m from Favorite f, Merchant m  where m.id = f.merchantId and f.uid = ?1 order by f.createDate desc")
    public List<Merchant> findByUidFavorite(Long uid, Pageable pageable);

    @Modifying
    @Query("update Merchant set isDelete = true where id = ?1")
    @Transactional
    public void removeById(long id);

    @Query("select count(*) from Merchant where creator = ?1")
    public int countByCreator(long uid);

    public Merchant findByCreator(long creator);

}
