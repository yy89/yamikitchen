package com.xiaobudian.yamikitchen.web;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.xiaobudian.yamikitchen.common.Result;
import com.xiaobudian.yamikitchen.service.thirdparty.dada.DadaService;
import com.xiaobudian.yamikitchen.web.dto.thirdparty.DadaDto;

/**
 * @author Liuminglu 2015/5/19.
 */
@RestController
public class ThirdPartyCallBackController {
	@Inject
	DadaService dadaService;
	
    @RequestMapping(value = "/thirdParty/dadaCallBack", method = RequestMethod.POST)
    public Result dadaCallBack(@RequestBody DadaDto dadaDto, HttpServletRequest request, HttpServletResponse response) {
        System.out.println(request.getParameter("order_id"));
        System.out.println("---------------------------------------------------");
    	return Result.successResult(dadaService.dadaCallBack(dadaDto));
    }

}
