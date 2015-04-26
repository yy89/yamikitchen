package com.xiaobudian.yamikitchen.service;

import com.xiaobudian.yamikitchen.domain.Product;

/**
 * Created by hackcoder on 2015/4/24.
 */
public interface ProductService {
    public Product save(Product product);
    public Product edit(Product product);
    public void putOff(long id);
    public void recover(long id);
}
