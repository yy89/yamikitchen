package com.xiaobudian.yamikitchen.web;

import java.util.Date;

import com.xiaobudian.yamikitchen.common.Result;
import com.xiaobudian.yamikitchen.domain.order.Order;
import com.xiaobudian.yamikitchen.domain.User;
import com.xiaobudian.yamikitchen.service.OrderService;
import com.xiaobudian.yamikitchen.web.dto.DadaResultDto;

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

    @RequestMapping(value = "/orders/getUnconfirmedOrders", method = RequestMethod.GET)
    public Result getUnconfirmedOrders(@AuthenticationPrincipal User user) {
        return Result.successResult(orderService.getUnconfirmedOrders(user.getId(), null));
    }

    @RequestMapping(value = "/orders/getUnconfirmedOrders/{createDate}", method = RequestMethod.GET)
    public Result getUnconfirmedOrdersByCreateDate(@PathVariable Long createDate, @AuthenticationPrincipal User user) {
        return Result.successResult(orderService.getUnconfirmedOrders(user.getId(), new Date(createDate)));
    }

    @RequestMapping(value = "/orders/confirmOrder/{orderNo}", method = RequestMethod.GET)
    public Result confirmOrder(@PathVariable String orderNo, @AuthenticationPrincipal User user) {
        return Result.successResult(orderService.confirmOrder(user.getId(), orderNo));
    }

    @RequestMapping(value = "/orders/chooseDeliverGroup/{orderNo}/{deliverGroup}", method = RequestMethod.POST)
    public Result chooseDeliverGroup(@PathVariable String orderNo, @PathVariable Integer deliverGroup, @AuthenticationPrincipal User user) {
        return Result.successResult(orderService.chooseDeliverGroup(user.getId(), orderNo, deliverGroup));
    }
    
    @RequestMapping(value = "/orders/dadaCallBack", method = RequestMethod.POST)
    public Result dadaCallBack(@RequestBody DadaResultDto dadaResultDto) {
        return Result.successResult(orderService.dadaCallBack(dadaResultDto));
    }
    
}
