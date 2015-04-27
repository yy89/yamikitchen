package com.xiaobudian.yamikitchen.web;

import com.xiaobudian.yamikitchen.common.Result;
import com.xiaobudian.yamikitchen.domain.User;
import com.xiaobudian.yamikitchen.domain.merchant.Merchant;
import com.xiaobudian.yamikitchen.domain.merchant.Product;
import com.xiaobudian.yamikitchen.service.MerchantService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

/**
 * Created by Johnson on 2015/4/24.
 */
@RestController
public class MerchantController {
    @Inject
    private MerchantService merchantService;

    @RequestMapping(value = "/merchants", method = RequestMethod.GET)
    @ResponseBody
    public Result getMerchants(@RequestParam("page") Integer page,
                               @RequestParam("size") Integer size,
                               @RequestParam("lat") Double longitude,
                               @RequestParam("lng") Double latitude) {
        return Result.successResult(merchantService.getMerchants(page, size, longitude, latitude));

    }

    @RequestMapping(value = "/merchants/add",method = RequestMethod.POST)
    @ResponseBody
    public Result addMerchant(Merchant merchant,@AuthenticationPrincipal  User user){
        merchantService.addMerchat(merchant);
        return Result.successResultWithoutData();
    }

    @RequestMapping(value = "/merchants/edit",method = RequestMethod.POST)
    @ResponseBody
    public Result editMerchant(Merchant merchant,@AuthenticationPrincipal  User user){
        merchantService.editMerchat(merchant);
        return Result.successResultWithoutData();
    }

    @RequestMapping(value = "/merchants/{rid}/remove",method = RequestMethod.POST)
    @ResponseBody
    public Result removeMerchant(@PathVariable long rid ,@AuthenticationPrincipal  User user){
        merchantService.removeMerchant(rid);
        return Result.successResultWithoutData();
    }

    @RequestMapping(value = "/merchants/{rid}/products", method = RequestMethod.GET)
    @ResponseBody
    public Result getProductsOfMerchant(@PathVariable Long rid, @RequestParam("page") Integer page,
                                        @RequestParam("size") Integer size) {
        return Result.successResult(merchantService.getProductsBy(rid, page, size));
    }

    @RequestMapping(value = "/merchants/{rid}/favorites", method = RequestMethod.POST)
    @ResponseBody
    public Result addFavorite(@PathVariable Long rid, @AuthenticationPrincipal User user) {
        return Result.successResult(merchantService.addFavorite(rid, user.getId()));
    }

    @RequestMapping(value = "/merchants/{rid}/favorites/cancel", method = RequestMethod.POST)
    @ResponseBody
    public Result removeFavorite(@PathVariable Long rid, @AuthenticationPrincipal User user) {
        return Result.successResult(merchantService.removeFavorite(rid, user.getId()));
    }

    @RequestMapping(value = "/merchants/favorites", method = RequestMethod.GET)
    @ResponseBody
    public Result getFavorites(@RequestParam("from") Integer pageFrom,
                               @RequestParam("size") Integer pageSize, @AuthenticationPrincipal User user) {
        return Result.successResult(merchantService.getFavorites(user.getId(), pageFrom, pageSize));
    }

    @RequestMapping(value = "/products/add",method = RequestMethod.POST)
    @ResponseBody
    public Result addProduct(Product product,@AuthenticationPrincipal  User user){
        merchantService.addProduct(product);
        return Result.successResultWithoutData();
    }

    @RequestMapping(value = "/products/edit",method = RequestMethod.POST)
    @ResponseBody
    public Result editProduct(Product product,@AuthenticationPrincipal  User user){
        merchantService.editProduct(product);
        return Result.successResultWithoutData();
    }

    @RequestMapping(value = "/products/{id}/remove",method = RequestMethod.POST)
    @ResponseBody
    public Result removeProduct(@PathVariable long id ,@AuthenticationPrincipal  User user){
        merchantService.removeProduct(id);
        return Result.successResultWithoutData();
    }

}