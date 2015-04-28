package com.xiaobudian.yamikitchen.web.dto;

import com.xiaobudian.yamikitchen.domain.merchant.Merchant;
import com.xiaobudian.yamikitchen.domain.merchant.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnson1 on 4/27/15.
 */
public class MerchantResponse {
    private Merchant merchant;
    private List<Product> products = new ArrayList<>();
    private boolean hasFavorite = false;

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }


    public boolean isHasFavorite() {
        return hasFavorite;
    }

    public void setHasFavorite(boolean hasFavorite) {
        this.hasFavorite = hasFavorite;
    }
}
