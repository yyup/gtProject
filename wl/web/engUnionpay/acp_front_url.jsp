<%
/* *
 功能：银联页面跳转同步通知页面（商户前台通知）
 版本：5.0
 日期：2014-07
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
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
 <head>
	<meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1, user-scalable=no">
	<meta name="Generator" content="waterrower Team">
	<meta name="Author" content="waterrowerTeam">
	<meta name="Keywords"
		content="沃特罗伦WaterRower（直译水划船机）始创于1988年。由美国国家划船队John Duke与耶鲁大学联合设计，并获发明专利。不仅是品牌，更是划船器水阻化的标志性里程碑。">
	<meta name="Description"
		content="沃特罗伦WaterRower（直译水划船机）始创于1988年。由美国国家划船队John Duke与耶鲁大学联合设计，并获发明专利。不仅是品牌，更是划船器水阻化的标志性里程碑。">
	<script src="../js/newsite/jquery.min.js"></script>
	<script src="../js/joy.loader.js" modules="dataview"></script>
	<script src="../js/newsite/bootstrap.js"></script>
	<script src="../js/newsite/getSystemPlatform.js"></script>
	<script src="../js/joy.loader.js" modules=""></script>
	<script src="../js/engsite/wlcommon.js"></script>
	<script src="../js/newsite/cnzz.js"></script>
	<link rel="stylesheet" href="../style/prj/engsite/header.css" />
	<link rel="stylesheet" href="../style/prj/engsite/footer.css" />
	<link rel="stylesheet" href="../style/prj/engsite/wlcommon.css" />
	<link rel="stylesheet" href="../style/prj/engsite/bootstrap.min.css" />
	<link rel="stylesheet" href="../style/prj/engsite/acp_font_url.css" />
  </head>
<body>
   <!--头部开始-->
	<div id="top">
		<!--公共文件的路径公共部分-->
		<div id="commonpath" style="display: none;" commonpath="../html/newsite/"></div>
	</div>
  <!--头部结束-->
<%
	SDKConfig.getConfig().loadPropertiesFromSrc();// 从classpath加载acp_sdk.properties文件
	
	LogUtil.writeLog("FrontRcvResponse前台接收报文返回开始");
	
	request.setCharacterEncoding("ISO-8859-1");
	String encoding = request.getParameter(SDKConstants.param_encoding);	
	Map<String, String> respParam = RequestUtil.getAllRequestParam(request);	
	// 打印请求报文
	LogUtil.printRequestLog(respParam);
	
	Map<String, String> valideData = null;
	StringBuffer sbf = new StringBuffer();
	if (null != respParam && !respParam.isEmpty()) {
		Iterator<Entry<String, String>> it = respParam.entrySet().iterator();
		valideData = new HashMap<String, String>(respParam.size());
		while (it.hasNext()) {
			Entry<String, String> e = it.next();
			String key = (String) e.getKey();
			String value = (String) e.getValue();
			value = new String(value.getBytes("ISO-8859-1"), encoding);			
			valideData.put(key, value);
		}
	}
	if (!SDKUtil.validate(valideData, encoding)) {//验证失败
		//该页面可做页面美工编辑
		//out.println("验证失败");
		LogUtil.writeLog("验证签名结果[失败].");
	%>	
	<div id="wlBody"  class="contentMargin">
		 <div class="container" style="margin-top: 250px; margin-bottom: 100px;">
	          <div class="row text-center" >
	              <div class="col-sm-12 text-center" style="height: 50px;">
	                  <img  src="../style/prj/images/email.png" class="img-responsive payErrorImg">
	                  <span class="payErrorText">Payment failure, please try again</span>
	              </div>
	          </div>
	          <div class="row text-center" >
	             <input type="button" onclick="toOrder()" value="Continue to pay" class="continuePay"/>
	          </div>
	          <div class="row text-center" >
	              <div class="contact">Please feel free to contact 400-808-4546 if you have any questions.(Working hours: Monday to Friday,09:00-18:00)</div>
	          </div>
	      </div>
	      </div>
	<%
	}else {//验证成功
		//商户订单号
		String orderNo=valideData.get("orderId");
	    //银联交易流水号
	    String traceNo=valideData.get("traceNo");
	
	%>
	<div id="wlBody"  class="contentMargin">
 	 <div class="container" style="margin-top: 250px; margin-bottom: 180px;" >
          <div class="row" style="margin-bottom: 130px;">
              <div class="col-md-12 title text-center" >Purchase success!</div>
          </div>
          <row>
              <div class="col-md-6"> <div style="text-align: center;"><input type="button" class="bottomButton " onclick="toOrder()" value="Order Details"></div></div>
              <div class="col-md-6">  <div style="text-align: center;"><input type="button" class="bottomButton"  onclick="toShop()" value="Continue to pay"></div></div>
          </row>
      </div>
      </div>
<%
	//LogUtil.writeLog("验证签名结果[成功].");		
}		
%>

	<!--尾部开始-->
		<div id="footerPart"></div>
	<!--尾部结束-->

</body>
	<script> 
		$(function(){
			initTop();
			initClick();
			initBottom();
			$(".btn-default").attr("style","height:34px;");
			/* $("#online_qq_layer").attr("show","1");
			init(); */
				
       });
		
		function toOrder(){
			window.location="../html/engsite/hasBuyGoods.html";
		}
		function toShop(){
			window.location="../html/engsite/productCenter.html";
		}
		<%-- function init(){
			
				 //orderNo=getParam("orderId");//在线支付用的参数
				 orderNo=<%=valideData.get("orderId")%>;
				if(orderNo!=null){
					joy.getJSON("../wl/es/wlEsOrderAction.web?action=getWlEsOrderByOrderNo",{orderNo:orderNo},	function(resultObject){
						 showContent(resultObject);
					});
				}
			
		}
		function showContent(orderId){
			
			joy.getJSON("../wl/es/wlEsOrderAction.web?action=getData",{orderId:orderId},	function(resultObject){
				
				//在线支付，货到付
				 /*   if(resultObject.payModeEk==="0"){
				  		$("#payModeEkImg").attr("src","../style/prj/images/bookSuccess.png");
				    }else{
				  		$("#payModeEkImg").attr("src","../style/prj/images/paySuccess.png");
				    }*/
			   //商品信息	
				var txt = $("#goodSInfoTable tr:first").prop('outerHTML');
				var lastTxt = $("#goodSInfoTable tr:last").prop('outerHTML');
		        $("#goodSInfoList").empty();
		        var goodList=resultObject.goodList;
		        for(i=0;i<goodList.length;i++){
		        	ob=goodList[i];
		        	txt+='<tr>';
		        	txt+='<td >'+ob.productName+'</td>';
		        	txt+='<td style="text-align:center;">'+ob.price+'</td>';
		        	txt+='<td style="text-align:center;">'+ob.num+'</td>';
		        	txt+='<td style="text-align:right;">'+ob.price*ob.num+'</td>';  
		        	txt+='</tr>';
		        	
		        }
		        txt+=lastTxt;
		    	$("#goodSInfoList").append(txt);
		    	$("#total").html("合计："+resultObject.amt+"元");
		    	//收货地址
		           //$("#deliverAddrContent").empty();
		         //  $("#deliverAddrContent").append(resultObject.province+resultObject.city+resultObject.area+resultObject.addr);
			      $("#deliverAddress").append("地址:"+resultObject.province+resultObject.city+resultObject.area+resultObject.addr);
			      $("#reciver").append("收货人:"+resultObject.receiver);
			       $("#call").append("电话:"+resultObject.mobile);
			});
			
		} --%>
    </script>
</html>