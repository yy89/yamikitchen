package com.xiaobudian.yamikitchen.web;

import com.xiaobudian.yamikitchen.common.Result;
import com.xiaobudian.yamikitchen.domain.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by johnson1 on 4/27/15.
 */
@RestController
public class OrderController {
    @RequestMapping(value = "/carts/products/{productId}", method = RequestMethod.POST)
    public Result addProductForCart(@PathVariable Long productId, @AuthenticationPrincipal User user) {



    }

    @RequestMapping(value = "/carts/products/{productId}", method = RequestMethod.DELETE)
    public Result removeProductForCart(@PathVariable Long productId, @AuthenticationPrincipal User user) {

    }

    @RequestMapping(value = "/carts", method = RequestMethod.GET)
    public Result getProductForCart(@AuthenticationPrincipal User user) {

    }


}
