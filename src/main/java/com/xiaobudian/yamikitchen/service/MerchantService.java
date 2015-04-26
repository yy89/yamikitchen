package com.xiaobudian.yamikitchen.service;

import com.xiaobudian.yamikitchen.domain.merchant.FavoriteResult;
import com.xiaobudian.yamikitchen.domain.merchant.Merchant;
import com.xiaobudian.yamikitchen.domain.merchant.Product;

import java.util.List;

/**
 * Created by Johnson on 2015/4/24.
 */
public interface MerchantService {
    public List<Merchant> getMerchants(int page, int pageSize, Double longitude, Double latitude);

    public List<Product> getProductsBy(Long rid, Integer page, Integer size);

    public FavoriteResult addFavorite(Long rid, Long uid);

    public FavoriteResult removeFavorite(Long rid, Long uid);

    public List<Merchant> getFavorites(Long uid, Integer pageFrom, Integer pageSize);
}