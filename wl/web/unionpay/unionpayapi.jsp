<%
/* *
 *功能：银联网页支付接入页（6-2消费类交易）
 *版本： 5.0    
 **********************************************
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.unionpay.acp.sdk.SDKConfig"%>
<%@ page import="com.unionpay.acp.util.*"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.text.SimpleDateFormat"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>银联网页支付</title>
</head>
<body>
<%
	SDKConfig.getConfig().loadPropertiesFromSrc();// 从classpath加载acp_sdk.properties文件
	/**
	 * 组装请求报文
	 */
	Map<String, String> data = new HashMap<String, String>();
	// 版本号
	data.put("version", "5.0.0");
	// 字符集编码 默认"UTF-8"
	data.put("encoding", "UTF-8");
	// 签名方法 01 RSA
	data.put("signMethod", "01");
	// 交易类型 01-消费
	data.put("txnType", "01");
	// 交易子类型 01:自助消费 02:订购 03:分期付款
	data.put("txnSubType", "01");
	// 业务类型
	data.put("bizType", "000201");
	// 渠道类型，07-PC，08-手机
	data.put("channelType", "07");
	// 前台通知地址 ，控件接入方式无作用
	// data.put("frontUrl", "http://localhost:9090/ACPTest/acp_front_url.do");
	String language = new String(request.getParameter("WIDlanguage").getBytes("ISO-8859-1"),"UTF-8");
	if("english".equals(language)){
		data.put("frontUrl",  AcpUtil.frontUrlEn);
	}else{
		data.put("frontUrl",  AcpUtil.frontUrl);
	}
	
	// 后台通知地址
	data.put("backUrl", AcpUtil.backUrl);
	// 接入类型，商户接入填0 0- 商户 ， 1： 收单， 2：平台商户
	data.put("accessType", "0");
	// 商户号码，请改成自己的商户号
	data.put("merId", "898350259410300");
	// 商户订单号，8-40位数字字母
	String out_trade_no = new String(request.getParameter("WIDout_trade_no").getBytes("ISO-8859-1"),"UTF-8");	
	data.put("orderId", out_trade_no);
	// 订单发送时间，取系统时间
	data.put("txnTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
	//付款金额,单位分
	String total_fee = new String(request.getParameter("WIDtotal_fee").getBytes("ISO-8859-1"),"UTF-8");
	//System.out.println("total_fee="+total_fee);
	double txnAmt=  Double.parseDouble(total_fee)*100;	
	data.put("txnAmt", String.valueOf((long)txnAmt));
	
	
	// 交易币种
	data.put("currencyCode", "156");
	// 请求方保留域，透传字段，查询、通知、对账文件中均会原样出现
	// data.put("reqReserved", "透传信息");
	// 订单描述，可不上送，上送时控件中会显示该信息
	// data.put("orderDesc", "订单描述");
	
	Map<String, String> submitFromData = AcpUtil.signData(data);
	
	// 交易请求url 从配置文件读取
	String requestFrontUrl = SDKConfig.getConfig().getFrontRequestUrl();
	
	/**
	 * 创建表单
	 */
	String html = AcpUtil.createHtml(requestFrontUrl, submitFromData);
	out.println(html);
%>

</body>
</html>