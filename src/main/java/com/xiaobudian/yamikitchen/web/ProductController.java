package com.xiaobudian.yamikitchen.web;

import com.xiaobudian.yamikitchen.common.Result;
import com.xiaobudian.yamikitchen.domain.Product;
import com.xiaobudian.yamikitchen.service.ProductService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

/**
 * Created by hackcoder on 2015/4/24.
 */
@RestController
public class ProductController {
    @Inject
    private ProductService productService;

    @ResponseBody
    @RequestMapping(value = "/product/add",method = RequestMethod.POST)
    public Result addProduct(Product product){
        productService.save(product);
        return Result.successResultWithoutData();
    }

    @ResponseBody
    @RequestMapping(value = "/product/edit",method = RequestMethod.POST)
    public Result editProduct(Product product){
        productService.edit(product);
        return Result.successResultWithoutData();
    }

    @ResponseBody
    @RequestMapping(value = "/product/putOff",method = RequestMethod.POST)
    public Result putOffProduct(long id){
        productService.putOff(id);
        return Result.successResultWithoutData();
    }

    @ResponseBody
    @RequestMapping(value = "/product/recover",method = RequestMethod.POST)
    public Result recoverProduct(long id){
        productService.recover(id);
        return Result.successResultWithoutData();
    }
}
