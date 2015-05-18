package com.xiaobudian.yamikitchen.service;

import com.xiaobudian.yamikitchen.domain.order.Order;
import com.xiaobudian.yamikitchen.web.dto.DadaResultDto;

public interface HttpclientService {
	
	String getGrantCode();

	DadaResultDto getAccessToken(String grantCode);

	DadaResultDto getAccessToken();

	DadaResultDto addOrderToDada(Order order, String token);
}
