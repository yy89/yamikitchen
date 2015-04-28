package com.xiaobudian.yamikitchen.web;

import com.xiaobudian.yamikitchen.common.Result;
import com.xiaobudian.yamikitchen.domain.User;
import com.xiaobudian.yamikitchen.domain.merchant.Merchant;
import com.xiaobudian.yamikitchen.domain.merchant.Product;
import com.xiaobudian.yamikitchen.service.MerchantService;
import com.xiaobudian.yamikitchen.web.dto.MerchantResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        List<Merchant> merchants = merchantService.getMerchants(page, size, longitude, latitude);
        List<MerchantResponse> responses = new ArrayList<>();
        for (Merchant merchant : merchants) {
            merchant.setDistance(Math.pow(Math.abs(merchant.getLongitude() - longitude) % 360, 2) + Math.pow(Math.abs(merchant.getLatitude() - latitude) % 360, 2));
            responses.add(new MerchantResponse.Builder()
                    .merchant(merchant)
                    .hasFavorite(false)
                    .products(merchantService.gteMainProduct(merchant.getId())).build());
        }
        return Result.successResult(responses);
    }

    @RequestMapping(value = "/merchants", method = RequestMethod.POST)
    @ResponseBody
    public Result addMerchant(@RequestBody Merchant merchant, @AuthenticationPrincipal User user) {
        merchant.setCreator(user.getId());
        merchantService.saveMerchant(merchant);
        return Result.successResult(merchant);
    }

    @RequestMapping(value = "/merchants", method = RequestMethod.PUT)
    @ResponseBody
    public Result editMerchant(@RequestBody Merchant merchant) {
        merchantService.saveMerchant(merchant);
        return Result.successResult(merchant);
    }

    @RequestMapping(value = "/merchants/{rid}", method = RequestMethod.DELETE)
    @ResponseBody
    public Result removeMerchant(@PathVariable long rid) {
        merchantService.removeMerchant(rid);
        return Result.successResultWithoutData();
    }

    @RequestMapping(value = "/merchants/{rid}/products", method = RequestMethod.GET)
    @ResponseBody
    public Result getProductsOfMerchant(@PathVariable Long rid, @RequestParam("page") Integer page,
                                        @RequestParam("size") Integer size) {
        MerchantResponse response = new MerchantResponse.Builder()
                .merchant(merchantService.getMerchantBy(rid))
                .hasFavorite(false)
                .products(merchantService.getProductsBy(rid, page, size)).build();
        return Result.successResult(response);
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

    @RequestMapping(value = "/products", method = RequestMethod.POST)
    @ResponseBody
    public Result addProduct(@RequestBody Product product) {
        merchantService.saveProduct(product);
        return Result.successResult(product);
    }

    @RequestMapping(value = "/products", method = RequestMethod.PUT)
    @ResponseBody
    public Result editProduct(@RequestBody Product product) {
        merchantService.saveProduct(product);
        return Result.successResult(product);
    }

    @RequestMapping(value = "/products/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Result removeProduct(@PathVariable long id) {
        merchantService.removeProduct(id);
        return Result.successResultWithoutData();
    }

}
