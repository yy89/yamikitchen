package com.xiaobudian.yamikitchen.service.thirdparty.dada;

import com.xiaobudian.yamikitchen.domain.order.Order;
import com.xiaobudian.yamikitchen.web.dto.thirdparty.DadaDto;

/**
 * @author Liuminglu 2015/5/19.
 */
public interface DadaService {
	
	String getGrantCode();

	DadaDto getAccessToken(String grantCode);

	DadaDto getAccessToken();

	DadaDto addOrderToDada(Order order, String token);

	void addOrderToDada(Order order);
	
    Order dadaCallBack(DadaDto dadaDto);

	
}
