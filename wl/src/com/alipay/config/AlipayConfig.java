package com.alipay.config;

import java.util.ResourceBundle;

public class AlipayConfig {
	private final static ResourceBundle CONFIG;
	static {
		CONFIG = ResourceBundle.getBundle("AliPay");
	}
	// ↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	// 合作身份者ID，以2088开头由16位纯数字组成的字符串
	public static String partner = CONFIG.getString("partner");;

	// 收款支付宝账号
	public static String seller_email = CONFIG.getString("seller_email");
	// 商户的私钥
	public static String key = CONFIG.getString("key");

	// ↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

	// 调试用，创建TXT日志文件夹路径
	public static String log_path = "D:\\";

	// 字符编码格式 目前支持 gbk 或 utf-8
	public static String input_charset = "utf-8";

	// 签名方式 不需修改
	public static String sign_type = "MD5";

	// 请求地址配置
	public static String notify_url = CONFIG.getString("notify_url");
	public static String call_back_url = CONFIG.getString("call_back_url");
	public static String merchant_url = CONFIG.getString("merchant_url");
	public static String call_back_url_en = CONFIG.getString("call_back_url_en");

}
