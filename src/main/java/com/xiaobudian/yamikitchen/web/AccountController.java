package com.xiaobudian.yamikitchen.web;

import com.xiaobudian.yamikitchen.common.Result;
import com.xiaobudian.yamikitchen.domain.account.Account;
import com.xiaobudian.yamikitchen.domain.account.AlipayHistory;
import com.xiaobudian.yamikitchen.domain.member.User;
import com.xiaobudian.yamikitchen.domain.order.Order;
import com.xiaobudian.yamikitchen.repository.order.OrderRepository;
import com.xiaobudian.yamikitchen.service.AccountService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by Johnson on 2015/5/14.
 */
@RestController
public class AccountController {
    private static final String SUCCESS = "SUCCESS";
    @Inject
    private AccountService accountService;
    @Inject
    private OrderRepository orderRepository;

    @RequestMapping(value = "/payment/callback", method = RequestMethod.POST)
    public String payment(AlipayHistory history) {
        accountService.writePaymentHistory(history);
        return SUCCESS;
    }

    @RequestMapping(value = "/alipay/orders/{orderNo}/orderString", method = RequestMethod.GET)
    @ResponseBody
    public Result getAlipayOrderString(@PathVariable String orderNo) {
        Order order = orderRepository.findByOrderNo(orderNo);
        return Result.successResult(accountService.getOrderStringOfAlipay(order));
    }

    @RequestMapping(value = "/accounts", method = RequestMethod.GET)
    @ResponseBody
    public Result getAccounts(@AuthenticationPrincipal User authenticationUser) {
        return Result.successResult(accountService.getAccounts(authenticationUser.getId()));
    }

    @RequestMapping(value = "/accounts/{accountId}/transactionFlows", method = RequestMethod.GET)
    @ResponseBody
    public Result getTransactionFlows(@PathVariable Long accountId, @AuthenticationPrincipal User authenticationUser) {
        return Result.successResult(accountService.getTransactionFlowsBy(accountId));
    }


    @RequestMapping(value = "/account/summary", method = RequestMethod.GET)
    @ResponseBody
    public Result getAccountSummary(@AuthenticationPrincipal User user) {
        return Result.successResult(accountService.getAccountSummary(user.getId()));
    }

}
