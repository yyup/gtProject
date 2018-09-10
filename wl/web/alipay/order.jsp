<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>订单支付</title>
</head>

<body>
<form id="form1" name="form1" method="post" action="alipayapi.jsp">

<table width="539" border="1">
  <tr>
    <td width="92">商户订单号</td>
    <td width="245"><label for="orderNo"></label>
      <input type="text" name="WIDout_trade_no" id="WIDout_trade_no" />
      <label for="WIDsubject"></label></td>
  </tr>
  <tr>
    <td>订单名称</td>
    <td><input type="text" name="WIDsubject" id="WIDsubject" /></td>
  </tr>
  <tr>
    <td>付款金额</td>
    <td><label for="WIDprice"></label>
      <input type="text" name="WIDtotal_fee" id="WIDtotal_fee" /></td>
  </tr>
  <tr>
    <td>订单描述</td>
    <td><label for="WIDbody"></label>
      <input type="text" name="WIDbody" id="WIDbody" /></td>
  </tr>
  <tr>
    <td>商品展示地址</td>
    <td><label for="WIDshow_url"></label>
      <input type="text" name="WIDshow_url" id="WIDshow_url" /></td>
  </tr>
  
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td><input type="submit" name="submit" id="submit" value="提交" /></td>
  </tr>
</table>
</form>
</body>
</html>
