package com.xiaobudian.yamikitchen.web;

import com.xiaobudian.yamikitchen.common.Result;
import com.xiaobudian.yamikitchen.domain.User;
import com.xiaobudian.yamikitchen.domain.merchant.Merchant;
import com.xiaobudian.yamikitchen.domain.merchant.Product;
import com.xiaobudian.yamikitchen.domain.order.Order;
import com.xiaobudian.yamikitchen.domain.order.OrderItem;
import com.xiaobudian.yamikitchen.service.MemberService;
import com.xiaobudian.yamikitchen.service.MerchantService;
import com.xiaobudian.yamikitchen.service.OrderService;
import com.xiaobudian.yamikitchen.util.DateUtils;
import com.xiaobudian.yamikitchen.web.dto.MerchantResponse;
import com.xiaobudian.yamikitchen.web.dto.OrderResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import java.util.ArrayList;
import java.util.Date;
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
            throw new RuntimeException("user.cannot.create.merchant.duplicate");
        }
        merchant.setCreator(user.getId());
        merchantService.saveMerchant(merchant);
        return Result.successResult(merchant);
    }

    @RequestMapping(value = "/merchants", method = RequestMethod.PUT)
    @ResponseBody
    public Result editMerchant(@RequestBody Merchant merchant,@AuthenticationPrincipal User user) {
        Merchant merchantdb = merchantService.getMerchantByCreator(user.getId());
        merchant.setId(merchantdb.getId());
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
    public Result removeMerchant(@PathVariable("rid")long rid,@AuthenticationPrincipal User user) {
        Merchant merchant = merchantService.getMerchantBy(rid);
        if(merchant==null){
            throw new RuntimeException("merchant.notexists");
        }
        if(merchant.getCreator()!=user.getId()) {
            throw new RuntimeException("user.cannot.operate.other.merchant");
        }
        merchantService.removeMerchant(rid);
        return Result.successResultWithoutData();
    }

    @RequestMapping(value = "/merchants/open",method = RequestMethod.POST)
    public Result openMerchant(@AuthenticationPrincipal User user){
        Merchant merchant = merchantService.getMerchantByCreator(user.getId());
        if(merchant==null){
            new RuntimeException("user.not_create_merchant");
        }
        return Result.successResult(merchantService.openMerchant(merchant.getId()));
    }

    @RequestMapping(value = "merchants/close",method = RequestMethod.POST)
    public Result closeMerchant(@AuthenticationPrincipal User user){
        Merchant merchant = merchantService.getMerchantByCreator(user.getId());
        if(merchant==null){
            new RuntimeException("user.not_create_merchant");
        }
        return Result.successResult(merchantService.closeMerchant(merchant.getId()));
    }

    @RequestMapping(value = "/products", method = RequestMethod.POST)
    @ResponseBody
    public Result addProduct(@RequestBody Product product,@AuthenticationPrincipal User user) {
        Merchant merchant = merchantService.getMerchantByCreator(user.getId());
        if(merchant.getCreator().longValue()!=user.getId()){
            throw new RuntimeException("user.cannot.operate.other.merchant");
        }
        product.setMerchantId(merchant.getId());
        merchantService.saveProduct(product);
        return Result.successResult(product);
    }

    @RequestMapping(value = "/products", method = RequestMethod.PUT)
    @ResponseBody
    public Result editProduct(@RequestBody Product product,@AuthenticationPrincipal User user) {
        Merchant merchant = merchantService.getMerchantByProductId(product.getId());
        if(merchant.getCreator().longValue()!=user.getId()){
            throw new RuntimeException("user.cannot.operate.other.merchant.product");
        }
        return Result.successResult(merchantService.updateProduct(product));
    }

    @RequestMapping(value = "/products/{pid}", method = RequestMethod.DELETE)
    @ResponseBody
    public Result removeProduct(@PathVariable long pid,@AuthenticationPrincipal User user) {
        Product product = merchantService.getProductBy(pid);
        Merchant merchant = merchantService.getMerchantBy(product.getMerchantId());
        if(merchant.getCreator().longValue()!=user.getId()){
            throw new RuntimeException("user.cannot.operate.other.merchant.product");
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

    /**
     * 上架菜品
     * @param pid
     * @param user
     * @return
     */
    @RequestMapping(value = "/products/{pid}/puton",method = RequestMethod.POST)
    public Result putOnProduct(@PathVariable long pid,@AuthenticationPrincipal User user){
        Product product = merchantService.getProductBy(pid);
        Merchant merchant = merchantService.getMerchantBy(product.getMerchantId());
        if(merchant.getCreator().longValue()!=user.getId()){
            throw new RuntimeException("user.cannot.operate.other.merchant.product");
        }
        return Result.successResult(merchantService.putOnProduct(pid));
    }

    @RequestMapping(value = "/products/{pid}/putoff",method = RequestMethod.POST)
    public Result putOffProduct(@PathVariable long pid,@AuthenticationPrincipal User user){
        Product product = merchantService.getProductBy(pid);
        Merchant merchant = merchantService.getMerchantBy(product.getMerchantId());
        if(merchant.getCreator().longValue()!=user.getId()){
            throw new RuntimeException("user.cannot.operate.other.merchant.product");
        }
        return Result.successResult(merchantService.putOffProduct(pid));
    }

    @RequestMapping(value = "/products/{pid}/main",method = RequestMethod.POST)
    public Result setProductMain(@PathVariable long pid,@AuthenticationPrincipal User user){
        Product product = merchantService.getProductBy(pid);
        Merchant merchant = merchantService.getMerchantBy(product.getMerchantId());
        if(merchant.getCreator().longValue()!=user.getId()){
            throw new RuntimeException("user.cannot.operate.other.merchant.product");
        }
        return Result.successResult(merchantService.setProductMain(pid));
    }

    @RequestMapping(value = "/products/{pid}/unmain",method = RequestMethod.POST)
    public Result setProductUnmain(@PathVariable long pid,@AuthenticationPrincipal User user){
        Product product = merchantService.getProductBy(pid);
        Merchant merchant = merchantService.getMerchantBy(product.getMerchantId());
        if(merchant.getCreator().longValue()!=user.getId()){
            throw new RuntimeException("user.cannot.operate.other.merchant.product");
        }
        return Result.successResult(merchantService.setProductUnmain(pid));
    }

    @RequestMapping(value = "/merchants/today/pending/orders",method = RequestMethod.GET)
    public Result getMerchantTodayPendingOrders(@RequestParam("page") Integer page,
                                                @RequestParam("size") Integer size,
                                                @AuthenticationPrincipal User user){
        Merchant merchant = merchantService.getMerchantByCreator(user.getId());
        List<OrderResponse> orderResponses = new ArrayList<OrderResponse>();
        List<Order> orders = orderService.getTodayPandingOrdersBy(page, size, merchant.getId());
        for(Order order:orders){
            List<OrderItem> orderItems = orderService.getItemsInOrder(order.getOrderNo());
            orderResponses.add(new OrderResponse.Builder().order(order).orderItems(orderItems).build());
        }
        return Result.successResult(orderResponses);
    }

    @RequestMapping(value = "/merchants/today/completed/orders",method = RequestMethod.GET)
        public Result getMerchantTodayCompletedOrders(@RequestParam("page") Integer page,
                @RequestParam("size") Integer size,
                @AuthenticationPrincipal User user){
        Merchant merchant = merchantService.getMerchantByCreator(user.getId());
        List<OrderResponse> orderResponses = new ArrayList<OrderResponse>();
        List<Order> orders = orderService.getTodayCompletedOrdersBy(page, size, merchant.getId());
        for(Order order:orders){
            List<OrderItem> orderItems = orderService.getItemsInOrder(order.getOrderNo());
            orderResponses.add(new OrderResponse.Builder().order(order).orderItems(orderItems).build());
        }
        return Result.successResult(orderResponses);
    }
}
