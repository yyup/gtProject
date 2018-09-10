var isLogin=false;
var contentPath="";
var  contentUrl="";
$(function(){
	//(此contentPath变量名不能更改，在index和index点下去的页面有用到)
	contentPath=window.location.protocol+"//"+window.location.host+"/wl";//"/wl"是项目contentPath，如为空，此处需要更改
   $(".listMenu li").eq(0).addClass("firstItem");
   $("body").attr("style","padding:0");
   /*joy.getJSON(contentUrl+"/wl/cm/wlCmMemberAction.dox?action=getUrl",{},function(resultObject){
	   contentPath=resultObject;
	   */
   //头部菜单
	$(".listMenu li").eq(0).children("a").attr("href",contentPath+"/index.html");
	$(".listMenu li").eq(1).children("a").attr("href",contentPath+"/html/site/wlCmInfo.html?columnId=0101");
    $(".listMenu li").eq(2).children("a").attr("href",contentPath+"/html/site/wlCmInfo.html?columnId=0102");
	$(".listMenu li").eq(3).children("a").attr("href",contentPath+"/html/site/wlCmInfo.html?columnId=0103");
	$(".listMenu li").eq(4).children("a").attr("href",contentPath+"/html/site/wlCmSell.html");
	$(".listMenu li").eq(5).children("a").attr("href",contentPath+"/html/site/wlCmCustomerService.html?columnId=0104");
	$(".listMenu li").eq(6).children("a").attr("href",contentPath+"/html/site/wlCmUser.html");
	
	//底部关于我们
	$(".footerInfoRight div a").eq(0).attr("href",contentPath+"/html/site/wlCmcontactUs.html?columnId=010501");
	$(".footerInfoRight div a").eq(1).attr("href",contentPath+"/html/site/wlCmcontactUs.html?columnId=010502");
	$(".footerInfoRight div a").eq(2).attr("href",contentPath+"/html/site/wlCmcontactUs.html?columnId=010503");
	$(".footerInfoRight div a").eq(3).attr("href",contentPath+"/html/site/wlCmcontactUs.html?columnId=010504");
	$(".footerInfoRight div a").eq(4).attr("href",contentPath+"/html/site/wlCmcontactUs.html?columnId=010505");
	//qq开始
	   var firstQQ="3275516900";
	   var secondQQ="1910446750";
       var threeQQ="1413611942";
	  $("#onlineType1 ul").html("");
	  txt='<li><a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin='+firstQQ+'&site=qq&menu=yes" ><img border="0" src="'+contentPath+'/style/prj/images/qq.jpg" alt="我们竭诚为您服务！" title="我们竭诚为您服务！"/></a></li>';
	  $("#onlineType1 ul").append(txt);
	  txt='<li><a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin='+secondQQ+'&site=qq&menu=yes" ><img border="0" src="'+contentPath+'/style/prj/images/qq.jpg" alt="我们竭诚为您服务！" title="我们竭诚为您服务！"/></a></li>';
	  $("#onlineType1 ul").append(txt);
	  txt='<li><a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin='+threeQQ+'&site=qq&menu=yes" ><img border="0" src="'+contentPath+'/style/prj/images/qq.jpg" alt="我们竭诚为您服务！" title="我们竭诚为您服务！"/></a></li>';
	  $("#onlineType1 ul").append(txt);
	  $("#onlineType1 ul").append('<li style="height:1px;"></li>');
	//qq结束
	  joy.getJSON(contentPath+"/wl/cm/wlCmMemberAction.dox?action=getIsLogin", function(
				resultObject) {
		  if(resultObject!=""){
				$(".land").html(resultObject+"|"+"<a onclick='loginOut()'>退出</a>");	
				isLogin=true;
		  }else{
			  $(".land").html("<a href="+contentPath+"/html/site/login.html>会员登录</a>|<a href="+contentPath+"/html/site/register.html>注册</a>");	
			  isLogin=false;
		  }
		});
   //   });
	});
   function loginOut(){
	   joy.showMessageDialog("确定退出登录?", "ok,cancel", function(e) {
			if (e.btnName == "ok") { 	
				window.location=contentPath+'/wl/cm/wlCmMemberAction.dox?action=loginOut';
				isLogin=false;
			}
		});
	   
   }



