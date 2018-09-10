<%
/* *
 功能：支付宝页面跳转同步通知页面
 版本：3.2
 日期：2011-03-17
 说明：
 以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 该代码仅供学习和研究支付宝接口使用，只是提供一个参考。

 //***********页面功能说明***********
 该页面可在本机电脑测试
 可放入HTML等美化页面的代码、商户业务逻辑程序代码
 TRADE_FINISHED(表示交易已经成功结束，并不能再对该交易做后续操作);
 TRADE_SUCCESS(表示交易已经成功结束，可以对该交易做后续操作，如：分润、退款等);
 //********************************
 * */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="java.util.Map"%>
<%@ page import="com.alipay.util.*"%>
<%@ page import="com.alipay.config.*"%>
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
	<link rel="stylesheet" href="../style/prj/engsite/return_url.css" />
  </head>
  <body>
   <!--头部开始-->
	<div id="top">
		<!--公共文件的路径公共部分-->
		<div id="commonpath" style="display: none;" commonpath="../html/engsite/"></div>
	</div>
  <!--头部结束-->
<%
	//获取支付宝GET过来反馈信息
	Map<String,String> params = new HashMap<String,String>();
	Map requestParams = request.getParameterMap();
	for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
		String name = (String) iter.next();
		String[] values = (String[]) requestParams.get(name);
		String valueStr = "";
		for (int i = 0; i < values.length; i++) {
			valueStr = (i == values.length - 1) ? valueStr + values[i]
					: valueStr + values[i] + ",";
			
		}
		//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
		//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
		  //System.out.println("name"+name+"valueStr"+valueStr);
		params.put(name, valueStr);
	}
	
	//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
	//商户订单号
	String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

	//支付宝交易号
	String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

	//交易状态
	String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");

	//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
	
	//计算得出通知验证结果
	boolean verify_result = AlipayNotify.verify(params);
	
	if(verify_result){//验证成功
		//////////////////////////////////////////////////////////////////////////////////////////
		//请在这里加上商户的业务逻辑程序代码
		
		//——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
		
		if(trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")){
			//判断该笔订单是否在商户网站中已经做过处理
				//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				//如果有做过处理，不执行商户的业务程序
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
		<%}
		
		//该页面可做页面美工编辑
		//out.println("验证成功<br />");
		
		//——请根据您的业务逻辑来编写程序（以上代码仅作参考）——

		//////////////////////////////////////////////////////////////////////////////////////////
	}else{%>
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
		//该页面可做页面美工编辑
		//out.println("验证失败");
	}%>


	<!--尾部开始-->
		<div id="footerPart"></div>
	<!--尾部结束-->

  </body>
  
	<script> 
		$(function(){
				//$("#online_qq_layer").attr("show","1");
				//init();
			initTop();
			initClick();
			initBottom();
			$(".btn-default").attr("style","height:34px;");
       });
		function toOrder(){
			window.location="../html/engsite/hasBuyGoods.html";
		}
		function toShop(){
			window.location="../html/engsite/productCenter.html";
		}
			
    </script>
  
</html>