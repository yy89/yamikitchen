package com.xiaobudian.yamikitchen.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.gson.Gson;
import com.xiaobudian.yamikitchen.domain.merchant.Merchant;
import com.xiaobudian.yamikitchen.domain.order.Order;
import com.xiaobudian.yamikitchen.util.Constants;
import com.xiaobudian.yamikitchen.util.MD5Util;
import com.xiaobudian.yamikitchen.web.dto.DadaResultDto;

@SuppressWarnings("deprecation")
@Service(value = "httpclientService")
public class HttpclientServiceImpl implements HttpclientService {
	// 达达签名算法字符串
	private static final String DADA = "dada";
	// 达达appkey
	private static final String DADA_APPKEY = "dada19bcdc6149a4895f";
	// 测试环境地址
	private static final String DADA_URL = "http://public.ga.dev.imdada.cn";
	// 正式环境地址
//	private static final String DADA_URL = "http://URL:public.imdada.cn";
	// 获取授权码
	private static final String GET_GRANT_CODE = "%s/oauth/authorize/?scope=dada_base&app_key=%s";
	// 获取access_token
	private static final String GET_ACCESS_TOKEN = "%s/oauth/access_token/?grant_type=authorization_code&app_key=%s&grant_code=%s";
	// 添加订单
	private static final String ADD_ORDER_DADA = "%s/v1_0/addOrder/";
	
	@Inject
	private MerchantService merchantService;
	
	@Override
	public String getGrantCode() {
		String requestUrl = String.format(GET_GRANT_CODE, DADA_URL, DADA_APPKEY);
		String resultJson = httpGet(requestUrl);
		DadaResultDto dadaResultDto = fromJson(resultJson);
		if (dadaResultDto == null || dadaResultDto.getResult() == null) {
			return null;
		}
		return dadaResultDto.getResult().getGrant_code();
	}
	
	@Override
	public DadaResultDto getAccessToken(String grantCode) {
		Assert.notNull(grantCode, "params can't be null : grantCode");
		String requestUrl = String.format(GET_ACCESS_TOKEN, DADA_URL, DADA_APPKEY, grantCode);
		String resultJson = httpGet(requestUrl);
		return fromJson(resultJson);
	}
	
	@Override
	public DadaResultDto getAccessToken() {
		return getAccessToken(getGrantCode());
	}
	
	@Override
	public DadaResultDto addOrderToDada(Order order, String token) {
		String requestUrl = String.format(ADD_ORDER_DADA, DADA_URL);
		
		Map<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("token", token);
		Date currentDate = new Date();
		requestMap.put("timestamp", String.valueOf(currentDate.getTime()));
		requestMap.put("token", token);
		requestMap.put("signature", getSignature(currentDate, token));
		requestMap.put("origin_id", order.getOrderNo());
		requestMap.put("city_name", "上海");
		requestMap.put("city_code", "021");
		requestMap.put("pay_for_supplier_fee", "0");
		requestMap.put("fetch_from_receiver_fee", "0");
		requestMap.put("deliver_fee", "0");
		requestMap.put("create_time", String.valueOf(order.getCreateDate().getTime()));
		requestMap.put("info", order.getRemark());
		requestMap.put("cargo_type", "1");
		requestMap.put("cargo_weight", "1");
		requestMap.put("cargo_price", String.valueOf(order.getPrice()));
		requestMap.put("cargo_num", String.valueOf(order.getTotalQuantity()));
		requestMap.put("is_prepay", "0");
		requestMap.put("expected_fetch_time", "0");
		requestMap.put("expected_finish_time", String.valueOf(order.getDeliverDate()));
		requestMap.put("supplier_id", String.valueOf(order.getMerchantId()));
		requestMap.put("supplier_name", order.getMerchantName());
		
		Merchant merchant = merchantService.getMerchantByCreator(order.getMerchantId());
		requestMap.put("supplier_address", merchant == null ? "丫米厨房" : merchant.getAddress());
		requestMap.put("supplier_phone", String.valueOf(order.getMerchantPhone()));
		requestMap.put("supplier_tel", null);
		requestMap.put("supplier_lat", String.valueOf(merchant == null ? null : merchant.getLatitude()));
		requestMap.put("supplier_lng", String.valueOf(merchant == null ? null : merchant.getLongitude()));
		requestMap.put("invoice_title", null);
		requestMap.put("receiver_name", order.getNickName());
		requestMap.put("receiver_address", order.getAddress());
		requestMap.put("receiver_phone", String.valueOf(order.getPhone()));
		requestMap.put("receiver_tel", null);
		requestMap.put("receiver_lat", String.valueOf(order.getLatitude()));
		requestMap.put("receiver_lng", String.valueOf(order.getLongitude()));
		requestMap.put("callback", Constants.DADA_CALL_BACK_URL);
		
		String resultJson = httpPost(requestUrl, requestMap);
		return fromJson(resultJson);
	}
	
	private String getSignature(Date currentDate, String token) {
		String timestamp = String.valueOf(new Date().getTime());
		List<String> list = new ArrayList<String>();
		list.add(token);
		list.add(timestamp);
		list.add(DADA);
		Collections.sort(list);
		String signString = list.toString();
		signString = signString.replace(" ", "").replace(",", "").replace("[", "").replace("]", "");
		return MD5Util.md5(signString);
	}
	
	private DadaResultDto fromJson(String json) {
		if (StringUtils.isBlank(json)) {
			return null;
		}
		try {
			Gson gson = new Gson();
			return gson.fromJson(json, DadaResultDto.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String httpGet(String getUrl) {
		@SuppressWarnings("resource")
		HttpClient httpclient = new DefaultHttpClient();
		try {
			// 创建httpget
			HttpGet httpget = new HttpGet(getUrl);
			// 执行get请求
			HttpResponse response = httpclient.execute(httpget);
			// 获取响应实体
			HttpEntity entity = response.getEntity();
			if (entity == null) {
				return null;
			}
			return EntityUtils.toString(entity);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			httpclient.getConnectionManager().shutdown();
		}
		return null;
	}
	
	private String httpPost(String postUrl, Map<String, String> requestParams) {
		@SuppressWarnings("resource")
		HttpClient httpclient = new DefaultHttpClient();
		// 建立HttpPost对象
		HttpPost httppost = new HttpPost(postUrl);
		// 建立NameValuePair数组，用于存储请求的参数
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> entry : requestParams.entrySet()) {  
            params.add(new BasicNameValuePair((String) entry.getKey(), (String) entry.getValue()));  
        }
		try {
			// 添加参数
			httppost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			// 设置编码
			HttpResponse response = httpclient.execute(httppost);
			// 发送Post并返回HttpResponse对象
			if (response.getStatusLine().getStatusCode() == 200) {// 如果状态码为200,就是正常返回
				return EntityUtils.toString(response.getEntity());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			httpclient.getConnectionManager().shutdown();
		}
		return null;
	}

}
