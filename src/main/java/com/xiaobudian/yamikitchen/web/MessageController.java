package com.xiaobudian.yamikitchen.web;

import com.xiaobudian.yamikitchen.common.Result;
import com.xiaobudian.yamikitchen.domain.member.User;
import com.xiaobudian.yamikitchen.domain.message.Notice;
import com.xiaobudian.yamikitchen.service.MessageService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

/**
 * Created by Johnson on 2015/5/12.
 */
@RestController
public class MessageController {
    @Inject
    private MessageService messageService;

    @RequestMapping(value = "/notices", method = RequestMethod.GET)
    @ResponseBody
    public Result getNotices(@RequestParam("from") Integer pageFrom,
                             @RequestParam("size") Integer pageSize,
                             @AuthenticationPrincipal User user) {
        return Result.successResult(messageService.getNotices(user.getId(), pageFrom, pageSize));
    }

    @RequestMapping(value = "/notices/{noticeId}", method = RequestMethod.DELETE)
    @ResponseBody
    public Result getNotices(@PathVariable Long noticeId, @AuthenticationPrincipal User user) {
        return Result.successResult(messageService.removeNotice(user.getId(), noticeId));
    }

    @RequestMapping(value = "/notices", method = RequestMethod.POST)
    @ResponseBody
    public Result addNotice(@RequestBody Notice notice) {
        return Result.successResult(messageService.addNotice(notice));
    }

    @RequestMapping(value = "/messages/type/{type}", method = RequestMethod.GET)
    @ResponseBody
    public Result getMessages(@PathVariable Integer type,
                              @RequestParam("from") Integer pageFrom,
                              @RequestParam("size") Integer pageSize,
                              @AuthenticationPrincipal User user) {
        return new Result(messageService.getMessages(type, user.getId(), pageFrom, pageSize));
    }
}
