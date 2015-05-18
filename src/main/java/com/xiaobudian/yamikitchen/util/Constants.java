package com.xiaobudian.yamikitchen.util;

/**
 * 常量类
 * @author Liuminglu
 */
public class Constants {
	
	/**
	 * 订单配送方式：配送
	 */
	public static final int DELIVER_METHOD_0 = 0;
	/**
	 * 订单配送方式：自提
	 */
	public static final int DELIVER_METHOD_1 = 1;
	
	/**
	 * 订单状态：等待支付
	 */
	public static final int ORDER_STATUS_1 = 1;
	/**
	 * 订单状态：等待订单确认
	 */
	public static final int ORDER_STATUS_2 = 2;
	/**
	 * 订单状态：等待配送
	 */
	public static final int ORDER_STATUS_3 = 3;
	/**
	 * 订单状态：外卖配送中
	 */
	public static final int ORDER_STATUS_4 = 4;
	/**
	 * 订单状态：订单完成
	 */
	public static final int ORDER_STATUS_5 = 5;
	/**
	 * 订单状态：等待自提
	 */
	public static final int ORDER_STATUS_6 = 6;
	/**
	 * 订单状态：订单取消
	 */
	public static final int ORDER_STATUS_7 = 7;
	
	/**
	 * 达达接口请求返回状态：成功
	 */
	public static final String DADA_RESPONSE_STATUS_OK = "ok";
	/**
	 * 达达接口请求返回状态：失败
	 */
	public static final String DADA_RESPONSE_STATUS_FAIL = "fail";
	/**
	 * 达达回调接口 115.28.38.232
	 */
	public static final String DADA_CALL_BACK_URL = "http://115.28.38.232:8080/yamikitchen/api/orders/dadaCallBack";
	
	
	
}
