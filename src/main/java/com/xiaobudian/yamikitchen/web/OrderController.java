package com.xiaobudian.yamikitchen.web;

import com.xiaobudian.yamikitchen.common.Result;
import com.xiaobudian.yamikitchen.domain.order.Order;
import com.xiaobudian.yamikitchen.domain.User;
import com.xiaobudian.yamikitchen.service.OrderService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

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

    @RequestMapping(value = "/settlement", method = RequestMethod.POST)
    public Result settlementOrder(@AuthenticationPrincipal User user) {
        return Result.successResult(orderService.getSettlement(user.getId()));
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
