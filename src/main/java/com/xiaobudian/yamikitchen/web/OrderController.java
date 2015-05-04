package com.xiaobudian.yamikitchen.web;

import com.xiaobudian.yamikitchen.common.Result;
import com.xiaobudian.yamikitchen.domain.Order;
import com.xiaobudian.yamikitchen.domain.OrderItem;
import com.xiaobudian.yamikitchen.domain.User;
import com.xiaobudian.yamikitchen.service.OrderService;
import com.xiaobudian.yamikitchen.util.DateUtils;
import com.xiaobudian.yamikitchen.web.dto.OrderResponse;
import org.springframework.format.annotation.DateTimeFormat;
import com.xiaobudian.yamikitchen.domain.User;
import com.xiaobudian.yamikitchen.service.OrderService;
import com.xiaobudian.yamikitchen.web.dto.OrderRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.ws.rs.QueryParam;
import java.util.*;

/**
 * Created by johnson1 on 4/27/15.
 */
@RestController
public class OrderController {
    @Inject
    private OrderService orderService;

    @RequestMapping(value = "/carts/merchants/{rid}/{productId}", method = RequestMethod.POST)
    public Result addProductForCart(@PathVariable Long rid, @PathVariable Long productId, @AuthenticationPrincipal User user) {
        return Result.successResult(orderService.addProductInCart(user.getId(), rid, productId));
    }

    @RequestMapping(value = "/carts/merchants/{rid}/{productId}", method = RequestMethod.DELETE)
    public Result removeProductForCart(@PathVariable Long rid, @PathVariable Long productId, @AuthenticationPrincipal User user) {
        return Result.successResult(orderService.removeProductInCart(user.getId(), rid, productId));
    }

    @RequestMapping(value = "/carts", method = RequestMethod.GET)
    public Result getProductForCart(@AuthenticationPrincipal User user) {
        return Result.successResult(orderService.getCart(user.getId()));
    }

    @RequestMapping(value = "/carts", method = RequestMethod.DELETE)
    public Result removeCart(@AuthenticationPrincipal User user) {
        return Result.successResult(orderService.removeCart(user.getId()));
    }

    @RequestMapping(value = "/{rid}/orders", method = RequestMethod.GET)
    @ResponseBody
    public Result getOrders(@PathVariable("rid") Long rid,
                            @RequestParam("status") Integer status,
                            @RequestParam("page") Integer page,
                            @RequestParam("size") Integer size,
                            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        List<Order> orders = orderService.getOrdersByMerchantIdAndStatusAndCreateDateBetween(page, size, rid, status, date, DateUtils.nextDay(date));
        List<OrderResponse> responses = new ArrayList<>();
        for (Order order : orders) {
            List<OrderItem> orderItems = orderService.getItemsInOrder(order.getOrderNo());
            OrderResponse orderResponse = new OrderResponse.Builder().order(order).orderItems(orderItems).build();
            responses.add(orderResponse);
        }
        return Result.successResult(responses);
    }

    @RequestMapping(value = "{rid}/today/handing/orders/", method = RequestMethod.GET)
    @ResponseBody
    public Result getTodayHandingOrders(@PathVariable("rid") Long rid,
                                 @RequestParam("page") Integer page,
                                 @RequestParam("size") Integer size) {
        List<Order> orders = orderService.getTodayHandingOrdersBy(page, size, rid);
        List<OrderResponse> responses = new ArrayList<>();
        for (Order order : orders) {
            List<OrderItem> orderItems = orderService.getItemsInOrder(order.getOrderNo());
            OrderResponse orderResponse = new OrderResponse.Builder().order(order).orderItems(orderItems).build();
            responses.add(orderResponse);
        }
        return Result.successResult(responses);
    }

    @RequestMapping(value = "{rid}/today/solved/orders/", method = RequestMethod.GET)
    @ResponseBody
    public Result getTodaySolvedOrders(@PathVariable("rid") Long rid,
                                        @RequestParam("page") Integer page,
                                        @RequestParam("size") Integer size) {
        List<Order> orders = orderService.getTodaySolvedOrdersBy(page,size,rid);
        List<OrderResponse> responses = new ArrayList<>();
        for (Order order : orders) {
            List<OrderItem> orderItems = orderService.getItemsInOrder(order.getOrderNo());
            OrderResponse orderResponse = new OrderResponse.Builder().order(order).orderItems(orderItems).build();
            responses.add(orderResponse);
        }
        return Result.successResult(responses);
    }

    @RequestMapping(value = "/settlement", method = RequestMethod.POST)
    public Result settlementOrder(@RequestBody OrderRequest orderRequest, @AuthenticationPrincipal User user) {
        return Result.successResult(orderService.initOrder(orderRequest));
    }

    @RequestMapping(value = "/orders", method = RequestMethod.POST)
    public Result createOrder(@RequestBody Order order, @AuthenticationPrincipal User user) {
        return Result.successResult(orderService.createOrder(order));
    }

    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public Result getOrders(@RequestBody Order order, @AuthenticationPrincipal User user) {
        return Result.successResult(orderService.getOrders(user.getId()));
    }



}
