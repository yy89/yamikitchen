package com.xiaobudian.yamikitchen.web.dto;

import com.xiaobudian.yamikitchen.domain.member.User;
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
    private User user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isHasFavorite() {
        return hasFavorite;
    }

    public void setHasFavorite(boolean hasFavorite) {
        this.hasFavorite = hasFavorite;
    }

    public static class Builder {
        private MerchantResponse response;

        public Builder() {
            this.response = new MerchantResponse();
        }

        public Builder merchant(Merchant merchant) {
            this.response.setMerchant(merchant);
            return this;
        }

        public Builder products(List<Product> products) {
            this.response.setProducts(products);
            return this;
        }

        public Builder hasFavorite(boolean hasFavorite) {
            this.response.setHasFavorite(hasFavorite);
            return this;
        }

        public Builder user(User user) {
            this.response.setUser(user);
            return this;
        }

        public MerchantResponse build() {
            return this.response;
        }
    }
}
