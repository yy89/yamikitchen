package com.xiaobudian.yamikitchen.repository.merchant;

import com.xiaobudian.yamikitchen.domain.merchant.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Johnson on 2015/4/22.
 */
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    public Favorite findByMerchantIdAndUid(Long merchantId, Long uid);
}
