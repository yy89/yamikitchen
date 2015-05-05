package com.xiaobudian.yamikitchen.web;

import com.xiaobudian.yamikitchen.common.Result;
import com.xiaobudian.yamikitchen.domain.User;
import com.xiaobudian.yamikitchen.domain.merchant.Merchant;
import com.xiaobudian.yamikitchen.domain.merchant.Product;
import com.xiaobudian.yamikitchen.service.MemberService;
import com.xiaobudian.yamikitchen.service.MerchantService;
import com.xiaobudian.yamikitchen.web.dto.MerchantResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johnson on 2015/4/24.
 */
@RestController
public class MerchantController {
    @Inject
    private MerchantService merchantService;
    @Inject
    private MemberService memberService;

    @RequestMapping(value = "/merchants", method = RequestMethod.GET)
    @ResponseBody
    public Result getMerchants(@RequestParam("page") Integer page,
                               @RequestParam("size") Integer size,
                               @RequestParam("lat") Double longitude,
                               @RequestParam("lng") Double latitude,
                               @AuthenticationPrincipal User user) {
        List<Merchant> merchants = merchantService.getMerchants(page, size, longitude, latitude);
        List<MerchantResponse> responses = new ArrayList<>();
        for (Merchant merchant : merchants) {
            merchant.setDistance(String.valueOf(Math.pow(Math.abs(merchant.getLongitude() - longitude) % 360, 2) + Math.pow(Math.abs(merchant.getLatitude() - latitude) % 360, 2)));
            responses.add(new MerchantResponse.Builder()
                    .merchant(merchant)
                    .hasFavorite(user != null && merchantService.hasFavorite(merchant.getId(), user.getId()))
                    .user(memberService.getUser(merchant.getCreator()))
                    .products(merchantService.gteMainProduct(merchant.getId())).build());
        }
        return Result.successResult(responses);
    }

    @RequestMapping(value = "/merchants/{rid}/products", method = RequestMethod.GET)
    @ResponseBody
    public Result getProductsOfMerchant(@PathVariable Long rid, @RequestParam("page") Integer page,
                                        @RequestParam("size") Integer size, @AuthenticationPrincipal User user) {
        Merchant merchant = merchantService.getMerchantBy(rid);
        MerchantResponse response = new MerchantResponse.Builder()
                .merchant(merchant)
                .hasFavorite(user != null && merchantService.hasFavorite(merchant.getId(), user.getId()))
                .user(memberService.getUser(merchant.getCreator()))
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


    @RequestMapping(value = "/merchants", method = RequestMethod.POST)
    @ResponseBody
    public Result addMerchant(@RequestBody Merchant merchant, @AuthenticationPrincipal User user) {
        Long userId = user.getId();
        int count  = merchantService.countMerhcantsByCreator(userId);
        if(count!=0){
            throw new IllegalArgumentException("用户对应的商户已存在，不能重复创建");
        }
        merchant.setCreator(user.getId());
        merchantService.saveMerchant(merchant);
        return Result.successResult(merchant);
    }

    @RequestMapping(value = "/merchants", method = RequestMethod.PUT)
    @ResponseBody
    public Result editMerchant(@RequestBody Merchant merchant,@AuthenticationPrincipal User user) {
        Merchant merchantTmp = merchantService.getMerchantBy(merchant.getId());
        if(merchantTmp.getCreator().longValue()!=user.getId()){
            throw new IllegalArgumentException("Can't operation does not belong to his own merchants");
        }
        Merchant merchantdb = merchantService.getMerchantBy(merchant.getId());
        if(StringUtils.isNotEmpty(merchant.getPictures())){
            merchantdb.setPictures(merchant.getPictures());
        }
        if(StringUtils.isNotEmpty(merchant.getName())){
            merchantdb.setName(merchant.getName());
        }
        if(StringUtils.isNotEmpty(merchant.getHeadPic())){
            merchantdb.setHeadPic(merchant.getHeadPic());
            user.setHeadPic(user.getHeadPic());
        }
        if(merchant.getType()!=null){
            merchantdb.setType(merchant.getType());
        }
        if(StringUtils.isNotEmpty(merchant.getVoiceIntroduction())){
            merchantdb.setVoiceIntroduction(merchant.getVoiceIntroduction());
        }
        if(StringUtils.isNotEmpty(merchant.getAddress())){
            merchantdb.setAddress(merchant.getAddress());
        }
        if(StringUtils.isNotEmpty(merchant.getPhone())){
            merchantdb.setPhone(merchant.getPhone());
        }
        if(StringUtils.isNotEmpty(merchant.getRealName())){
            merchantdb.setRealName(merchant.getRealName());
            user.setUsername(merchant.getRealName());
        }
        if(merchant.getGender()!=null){
            merchantdb.setGender(merchant.getGender());
            user.setGender(user.getGender());
        }
        if(StringUtils.isNotEmpty(merchant.getRegion())){
            merchantdb.setRegion(merchant.getRegion());
            user.setRegion(merchant.getRegion());
        }
        if(StringUtils.isNotEmpty(merchant.getGoodCuisine())){
            merchantdb.setGoodCuisine(merchant.getGoodCuisine());
        }
        if(StringUtils.isNotEmpty(merchant.getBusinessHours())){
            merchantdb.setBusinessHours(merchant.getBusinessHours());
        }
        if(StringUtils.isNotEmpty(merchant.getBusinessDayPerWeek())){
            merchantdb.setBusinessDayPerWeek(merchant.getBusinessDayPerWeek());
        }
        if(merchant.isSupportDelivery()!=merchantdb.isSupportDelivery()){
            merchantdb.setSupportDelivery(merchant.isSupportDelivery());
        }
        if(merchant.getDeliverFee()!=null){
            merchantdb.setDeliverFee(merchant.getDeliverFee());
        }
        if(StringUtils.isNotEmpty(merchant.getDeliverComment())){
            merchantdb.setDeliverComment(merchant.getDeliverComment());
        }
        if(merchant.isMessHall()!=merchantdb.isMessHall()){
            merchantdb.setMessHall(merchant.isMessHall());
        }
        if(merchant.getCountOfMessHall()!=null){
            merchantdb.setCountOfMessHall(merchant.getCountOfMessHall());
        }
        if(merchant.isSelfPickup()!=merchantdb.isSelfPickup()){
            merchantdb.setCountOfMessHall(merchant.getCountOfMessHall());
        }
        if(StringUtils.isNotEmpty(merchant.getDescription())){
            merchantdb.setDescription(merchant.getDescription());
            user.setDescription(merchant.getDescription());
        }
        merchantService.saveMerchant(merchantdb);
        memberService.saveUser(user);
        return Result.successResult(merchantdb);
    }

    @RequestMapping(value = "/merchants/info", method = RequestMethod.GET)
    @ResponseBody
    public Result getMerchant(@AuthenticationPrincipal User user) {
        Merchant merchant = merchantService.getMerchantByCreator(user.getId());
        return Result.successResult(merchant);
    }

    @RequestMapping(value = "/merchants/{rid}", method = RequestMethod.DELETE)
    @ResponseBody
    public Result removeMerchant(@PathVariable long rid,@AuthenticationPrincipal User user) {
        Merchant merchant = merchantService.getMerchantBy(rid);
        if(merchant.getCreator().longValue()!=user.getId()){
            throw new IllegalArgumentException("Can't operation does not belong to his own merchants");
        }
        merchantService.removeMerchant(rid);
        return Result.successResultWithoutData();
    }

    @RequestMapping(value="/merchants/{rid}/reject",method = RequestMethod.POST)
    @ResponseBody
    public Result rejectMerchants(@PathVariable long rid,@AuthenticationPrincipal User user){
        merchantService.rejectMerchants(rid);
        return Result.successResultWithoutData();
    }

    @RequestMapping(value="/merchants/{rid}/pass",method = RequestMethod.POST)
    @ResponseBody
    public Result passMerchants(@PathVariable long rid,@AuthenticationPrincipal User user){
        merchantService.passMerchants(rid);
        return Result.successResultWithoutData();
    }



    @RequestMapping(value = "/products", method = RequestMethod.POST)
    @ResponseBody
    public Result addProduct(@RequestBody Product product,@AuthenticationPrincipal User user) {
        Merchant merchant = merchantService.getMerchantBy(product.getMerchantId());
        if(merchant.getCreator().longValue()!=user.getId()){
            throw new IllegalArgumentException("Can't operation does not belong to his own merchants");
        }
        merchantService.saveProduct(product);
        return Result.successResult(product);
    }

    @RequestMapping(value = "/products", method = RequestMethod.PUT)
    @ResponseBody
    public Result editProduct(@RequestBody Product product,@AuthenticationPrincipal User user) {
        Merchant merchant = merchantService.getMerchantByProductId(product.getId());
        if(merchant.getCreator().longValue()!=user.getId()){
            throw new IllegalArgumentException("Can't operation does not belong to his own merchants");
        }
        Product productdb = merchantService.getProductBy(product.getId());
        if(StringUtils.isNotEmpty(product.getName())){
            productdb.setName(product.getName());
        }
        if(StringUtils.isNotEmpty(product.getPictures())){
            productdb.setPictures(product.getPictures());
        }
        if(product.getPrice()!=null){
            productdb.setPrice(product.getPrice());
        }
        if(StringUtils.isNotEmpty(product.getTags())){
            productdb.setTags(product.getTags());
        }
        if(StringUtils.isNotEmpty(product.getAvailableTime())){
            productdb.setAvailableTime(product.getAvailableTime());
        }
        if(StringUtils.isNotEmpty(product.getSummary())){
            productdb.setSummary(product.getSummary());
        }
        if(product.getSupplyPerDay()!=null){
            productdb.setSupplyPerDay(product.getSupplyPerDay());
        }
        if(product.isAvailable()!=productdb.isAvailable()){
            productdb.setAvailable(product.isAvailable());
        }
        if(product.isMain()!=productdb.isMain()){
            productdb.setMain(product.isMain());
        }
        merchantService.saveProduct(productdb);
        return Result.successResult(productdb);
    }

    @RequestMapping(value = "/products/{pid}", method = RequestMethod.DELETE)
    @ResponseBody
    public Result removeProduct(@PathVariable long pid,@AuthenticationPrincipal User user) {
        Product product = merchantService.getProductBy(pid);
        Merchant merchant = merchantService.getMerchantBy(product.getMerchantId());
        if(merchant.getCreator().longValue()!=user.getId()){
            throw new IllegalArgumentException("Can't operation does not belong to his own merchants");
        }
        merchantService.removeProduct(pid);
        return Result.successResultWithoutData();
    }

    @RequestMapping(value = "/products/{pid}", method = RequestMethod.GET)
    @ResponseBody
    public Result getProduct(@PathVariable long pid,@AuthenticationPrincipal User user) {
        Product product = merchantService.getProductBy(pid);
        return Result.successResult(product);
    }

}
