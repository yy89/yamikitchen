package com.xiaobudian.yamikitchen.service;

import com.xiaobudian.yamikitchen.domain.Product;
import com.xiaobudian.yamikitchen.repository.ProductRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * Created by hackcoder on 2015/4/24.
 */
@Service("productService")
public class ProductServiceImpl implements ProductService{
    @Inject
    private ProductRepository productRepository;

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product edit(Product product) {
        if(product!=null){
            Product productdb = productRepository.findOne(product.getId());
            if(productdb!=null){//数据库中存在修改
                productRepository.save(product);
            }
        }
        return null;
    }

    @Override
    public void putOff(long id) {
         productRepository.putOff(id);
    }

    @Override
    public void recover(long id) {
        productRepository.putOn(id);
    }
}
