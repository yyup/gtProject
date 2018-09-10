<%
/* *
 功能：银联服务器异步通知页面(商户后台通知类)
 版本：5.0
 //***********页面功能说明***********
 创建该页面文件时，请留心该页面文件中无任何HTML代码及空格。
 该页面不能在本机电脑测试，请到服务器上做测试。请确保外部可以访问该页面。
 //********************************
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.Map.Entry"%>
<%@ page import="com.unionpay.acp.sdk.LogUtil"%>
<%@ page import="com.unionpay.acp.sdk.SDKConfig"%>
<%@ page import="com.unionpay.acp.sdk.SDKConstants"%>
<%@ page import="com.unionpay.acp.sdk.SDKUtil"%>
<%@ page import="com.unionpay.acp.util.*"%>
<%@ page import="org.joyone.spring.Sc" %>
<%@ page import="com.gt.wl.es.service.WlEsOrderService" %>
<%
SDKConfig.getConfig().loadPropertiesFromSrc();// 从classpath加载acp_sdk.properties文件
//LogUtil.writeLog("BackRcvResponse接收后台通知开始");

request.setCharacterEncoding("ISO-8859-1");
String encoding = request.getParameter(SDKConstants.param_encoding);
// 获取请求参数中所有的信息
Map<String, String> reqParam = RequestUtil.getAllRequestParam(request);
// 打印请求报文
LogUtil.printRequestLog(reqParam);

Map<String, String> valideData = null;
if (null != reqParam && !reqParam.isEmpty()) {
	Iterator<Entry<String, String>> it = reqParam.entrySet().iterator();
	valideData = new HashMap<String, String>(reqParam.size());
	while (it.hasNext()) {
		Entry<String, String> e = it.next();
		String key = (String) e.getKey();
		String value = (String) e.getValue();
		value = new String(value.getBytes("ISO-8859-1"), encoding);
		valideData.put(key, value);
	}
}

// 验证签名
if (!SDKUtil.validate(valideData, encoding)) {
	LogUtil.writeLog("验证签名结果[失败].");
} else {
	//商户订单号
	String orderNo=valideData.get("orderId");
    //银联交易流水号
    String traceNo=valideData.get("traceNo");
    //状态
    String respMsg=valideData.get("respMsg");   
    //请在这里加上商户的业务逻辑程序代码    
	if(respMsg.equals("Success!")){
		//商户的业务逻辑程序代码
		WlEsOrderService wlEsOrderService = (WlEsOrderService) Sc.getBean("wl.es.WlEsOrderService"); 
		wlEsOrderService.updateOrderStateToPayed(orderNo);  		
		out.println("success");	//请不要修改或删除
	}
}

LogUtil.writeLog("BackRcvResponse接收后台通知结束");
%>