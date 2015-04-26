package com.xiaobudian.yamikitchen.web;

import com.xiaobudian.yamikitchen.common.Result;
import com.xiaobudian.yamikitchen.domain.Merchant;
import com.xiaobudian.yamikitchen.service.MerchantService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

/**
 * Created by hackcoder on 2015/4/24.
 */
@RestController
public class MerchantController {
    @Inject
    private MerchantService merchantService;

    @RequestMapping(value = "/merchant/add",method = RequestMethod.POST)
    @ResponseBody
    public Result addMerchant(Merchant merchant){
        merchantService.save(merchant);
        return Result.successResultWithoutData();
    }

    @RequestMapping(value = "/merchant/edit",method = RequestMethod.POST)
    @ResponseBody
    public Result editMerchant(Merchant merchant){
        merchantService.edit(merchant);
        return Result.successResultWithoutData();
    }

    @RequestMapping(value = "/merchant/rest",method = RequestMethod.POST)
    @ResponseBody
    public Result restMerchant(long id){
        merchantService.rest(id);
        return Result.successResultWithoutData();
    }

    @RequestMapping(value = "/merchant/reopen",method = RequestMethod.POST)
    @ResponseBody
    public Result reopenMerchant(long id){
        merchantService.reopen(id);
        return Result.successResultWithoutData();
    }


}
