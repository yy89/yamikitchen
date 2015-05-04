package com.xiaobudian.yamikitchen.service;

import com.xiaobudian.yamikitchen.domain.merchant.FavoriteResult;
import com.xiaobudian.yamikitchen.domain.merchant.Merchant;
import com.xiaobudian.yamikitchen.domain.merchant.Product;
import com.xiaobudian.yamikitchen.web.dto.MerchantResponse;

import java.util.List;

/**
 * Created by Johnson on 2015/4/24.
 */
public interface MerchantService {
    public List<Merchant> getMerchants(int page, int pageSize, Double longitude, Double latitude);

    public Merchant saveMerchant(Merchant merchant);

    public void removeMerchant(long id);

    public List<Product> getProductsBy(Long rid, Integer page, Integer size);

    public Product saveProduct(Product product);

    public void removeProduct(long id);

    public FavoriteResult addFavorite(Long rid, Long uid);

    public boolean hasFavorite(Long rid, Long uid);

    public FavoriteResult removeFavorite(Long rid, Long uid);

    public List<Merchant> getFavorites(Long uid, Integer pageFrom, Integer pageSize);

    public List<Product> gteMainProduct(Long rid);

    public Merchant getMerchantBy(Long rid);

    public Product getProductBy(long pid);
}
