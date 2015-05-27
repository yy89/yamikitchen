package com.xiaobudian.yamikitchen.web;

import com.xiaobudian.yamikitchen.common.Result;
import com.xiaobudian.yamikitchen.common.Util;
import com.xiaobudian.yamikitchen.domain.member.User;
import com.xiaobudian.yamikitchen.domain.merchant.Merchant;
import com.xiaobudian.yamikitchen.domain.merchant.Product;
import com.xiaobudian.yamikitchen.service.MemberService;
import com.xiaobudian.yamikitchen.service.MerchantService;
import com.xiaobudian.yamikitchen.service.OrderService;
import com.xiaobudian.yamikitchen.web.dto.MerchantResponse;
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
    @Inject
    private OrderService orderService;

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
            merchant.setDistance(Util.calculateDistanceAsString(merchant.getLongitude(), longitude, merchant.getLatitude(), latitude));
            responses.add(new MerchantResponse.Builder()
                    .merchant(merchant)
                    .hasFavorite(user != null && merchantService.hasFavorite(merchant.getId(), user.getId()))
                    .user(memberService.getUser(merchant.getCreator()))
                    .products(merchantService.getMainProducts(merchant.getId())).build());
        }
        return Result.successResult(responses);
    }

    @RequestMapping(value = "/merchants/products", method = RequestMethod.GET)
    @ResponseBody
    public Result getMyProducts(@RequestParam("page") Integer page,
                                @RequestParam("size") Integer size,
                                @AuthenticationPrincipal User user) {
        Merchant merchant = merchantService.getMerchantByCreator(user.getId());
        if (merchant == null) throw new RuntimeException("user.merchant.not.create");
        MerchantResponse response = new MerchantResponse.Builder()
                .merchant(merchant)
                .user(user)
                .products(merchantService.getProductsBy(merchant.getId(), page, size)).build();
        return Result.successResult(response);
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
        if (merchantService.getMerchantByCreator(user.getId()) != null)
            throw new RuntimeException("user.merchant.exists");
        merchant.setCreator(user.getId());
        return Result.successResult(merchantService.saveMerchant(merchant));
    }

    @RequestMapping(value = "/merchants", method = RequestMethod.PUT)
    @ResponseBody
    public Result editMerchant(@RequestBody Merchant merchant, @AuthenticationPrincipal User user) {
        Merchant m = merchantService.getMerchantByCreator(user.getId());
        if (m == null) throw new RuntimeException("merchant.does.not.exist");
        if (!m.isCreateBy(user.getId())) throw new RuntimeException("user.merchant.unauthorized");
        merchant.setId(m.getId());
        return Result.successResult(merchantService.updateMerchant(merchant));
    }

    @RequestMapping(value = "/merchants/info", method = RequestMethod.GET)
    @ResponseBody
    public Result getMerchant(@AuthenticationPrincipal User user) {
        Merchant merchant = merchantService.getMerchantByCreator(user.getId());
        return Result.successResult(merchant);
    }

    @RequestMapping(value = "/merchants/{rid}", method = RequestMethod.DELETE)
    @ResponseBody
    public Result removeMerchant(@PathVariable("rid") long rid, @AuthenticationPrincipal User user) {
        Merchant merchant = merchantService.getMerchantBy(rid);
        if (merchant == null) throw new RuntimeException("merchant.does.not.exist");
        if (!merchant.isCreateBy(user.getId())) throw new RuntimeException("user.merchant.unauthorized");
        return Result.successResult(merchantService.removeMerchant(rid));
    }

    @RequestMapping(value = "/merchants/rest/{isRest}", method = RequestMethod.POST)
    public Result openMerchant(@PathVariable boolean isRest, @AuthenticationPrincipal User user) {
        Merchant merchant = merchantService.getMerchantByCreator(user.getId());
        if (merchant == null) throw new RuntimeException("user.merchant.unauthorized");
        if(merchant.getVerifyStatus() !=1){
            return Result.failResult("商户未审核通过，不允许营业！");
        }
        if(merchant.getIsAutoOpen()==false){
            return Result.failResult("未通过自由开店，请联系客服！");
        }
        return Result.successResult(merchantService.changeMerchantRestStatus(merchant.getId(), isRest));
    }

    @RequestMapping(value = "/products", method = RequestMethod.POST)
    @ResponseBody
    public Result addProduct(@RequestBody Product product, @AuthenticationPrincipal User user) {
        Merchant merchant = merchantService.getMerchantByCreator(user.getId());
        if (!merchant.isCreateBy(user.getId())) throw new RuntimeException("user.merchant.product.unauthorized");
        product.setMerchantId(merchant.getId());
        return Result.successResult(merchantService.saveProduct(product));
    }

    @RequestMapping(value = "/products", method = RequestMethod.PUT)
    @ResponseBody
    public Result editProduct(@RequestBody Product product, @AuthenticationPrincipal User user) {
        Merchant merchant = merchantService.getMerchantByCreator(user.getId());
        if (!merchant.isCreateBy(user.getId())) throw new RuntimeException("user.merchant.product.unauthorized");
        return Result.successResult(merchantService.updateProduct(product));
    }

    @RequestMapping(value = "/products/{pid}", method = RequestMethod.DELETE)
    @ResponseBody
    public Result removeProduct(@PathVariable long pid, @AuthenticationPrincipal User user) {
        Product product = merchantService.getProductBy(pid);
        Merchant merchant = merchantService.getMerchantBy(product.getMerchantId());
        if (!merchant.isCreateBy(user.getId())) throw new RuntimeException("user.merchant.product.unauthorized");
        return Result.successResult(merchantService.removeProduct(pid));
    }

    @RequestMapping(value = "/products/{pid}", method = RequestMethod.GET)
    @ResponseBody
    public Result getProduct(@PathVariable Long pid) {
        return Result.successResult(merchantService.getProductBy(pid));
    }

    @RequestMapping(value = "/products/{pid}/available/{available}", method = RequestMethod.POST)
    public Result putOnProduct(@PathVariable Long pid, @PathVariable boolean available, @AuthenticationPrincipal User user) {
        Product product = merchantService.getProductBy(pid);
        Merchant merchant = merchantService.getMerchantBy(product.getMerchantId());
        if (!merchant.isCreateBy(user.getId())) throw new RuntimeException("user.merchant.product.unauthorized");
        return Result.successResult(merchantService.changeProductAvailability(pid, available));
    }

    @RequestMapping(value = "/products/{pid}/main/{isMain}", method = RequestMethod.POST)
    public Result setProductMain(@PathVariable Long pid, @PathVariable boolean isMain, @AuthenticationPrincipal User user) {
        Product product = merchantService.getProductBy(pid);
        Merchant merchant = merchantService.getMerchantBy(product.getMerchantId());
        if (!merchant.isCreateBy(user.getId())) throw new RuntimeException("user.merchant.product.unauthorized");
        return Result.successResult(merchantService.changeProductMain(pid, isMain));
    }

    @RequestMapping(value = "/merchants/today/pending/orders", method = RequestMethod.GET)
    public Result getMerchantTodayPendingOrders(@RequestParam("page") Integer page,
                                                @RequestParam("size") Integer size,
                                                @AuthenticationPrincipal User user) {
        Merchant merchant = merchantService.getMerchantByCreator(user.getId());
        return Result.successResult(orderService.getTodayPendingOrders(merchant.getId(), page, size));
    }

    @RequestMapping(value = "/merchants/orders/today/completed", method = RequestMethod.GET)
    public Result getMerchantTodayCompletedOrders(@RequestParam("page") Integer page,
                                                  @RequestParam("size") Integer size,
                                                  @AuthenticationPrincipal User user) {
        Merchant merchant = merchantService.getMerchantByCreator(user.getId());
        return Result.successResult(orderService.getTodayCompletedOrders(merchant.getId(), page, size));
    }
}
