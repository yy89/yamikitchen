package com.xiaobudian.yamikitchen.service;

import com.xiaobudian.yamikitchen.domain.Merchant;

/**
 * Created by hackcoder on 2015/4/24.
 */
public interface MerchantService {
    public Merchant save(Merchant merchant);
    public Merchant edit(Merchant merchant);
    public void rest(long id );//ÐÝµê
    public void reopen(long id);//ÖØÐÂ¿ªµê
}
