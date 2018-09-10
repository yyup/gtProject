document.write('<script type= "text/javascript">var online = new Array(); var urlPath="../../"</script>');
document.write('<script type= "text/javascript" src="http://webpresence.qq.com/getonline?Type=1&1587970167:1413611942:3275516900:" ></script>');
var isPcFlag=true;
$(function(){
	//$(".k-window-titlebar ").html("title");

	commonpath=$("#commonpath").attr("commonpath");//公共的路径部分--相对路径
	 picturepath=window.location.protocol+"//"+window.location.host+"/wl";//图片路径--绝对路径	 
	 isPcFlag=isPC();
	});

function initClick(){
	 if(commonpath=="../html/newsite/"||commonpath=="../html/engsite/"){
		 urlPath="../"; 
	 }	 
	  //头部6个模块路径
	  $("#bs-example-navbar-collapse-1 li").eq(0).children("a").attr("href",commonpath+"index.html");
	  $("#bs-example-navbar-collapse-1 li").eq(1).children("a").attr("href",commonpath+"aboutWaterRower.html");
	  $("#bs-example-navbar-collapse-1 li").eq(2).children("a").attr("href",commonpath+"homeExercise.html");
	  $("#bs-example-navbar-collapse-1 li").eq(3).children("a").attr("href",commonpath+"specialtyUsersBody.html");
	  $("#bs-example-navbar-collapse-1 li").eq(4).children("a").attr("href",commonpath+"productCenter.html");
	  $("#bs-example-navbar-collapse-1 li").eq(5).children("a").attr("href",commonpath+"customerCenterBody.html");
	//点击qq客服
		  $("#headQqIcon").click(function(){
			      var qqshowState=$(this).attr("showState");//qq是否显示qqContain
				        if(qqshowState=="unselect"){
                         $(this).attr("showState","select").attr("src",picturepath+"/style/prj/images/qq_select.png");
						   $("#qqContain").show();
						}else{
						 $(this).attr("showState","unselect").attr("src",picturepath+"/style/prj/images/qq_unselect.jpg");
						   $("#qqContain").hide();
						}
				    //联系我们隐藏
                  $("#headTelIcon").attr("showState","unselect").attr("src",picturepath+"/style/prj/images/tel_unselect.png");//
                  $("#contactContain").hide();

		  });
		  //点击电话客服
		   $("#headTelIcon").click(function(){
			           var telshowState=$(this).attr("showState");//qq是否显示qqContain
				        if(telshowState=="unselect"){
                         $(this).attr("showState","select").attr("src",picturepath+"/style/prj/images/tel_select.png");
						   $("#contactContain").show();
						}else{
						   $(this).attr("showState","unselect").attr("src",picturepath+"/style/prj/images/tel_unselect.png");
						   $("#contactContain").hide();
						}
				    //qq隐藏
                  $("#headQqIcon").attr("showState","unselect").attr("src",picturepath+"/style/prj/images/qq_unselect.jpg");//
                  $("#qqContain").hide();
		  });
		  //点击具体的qq联系我们
		   $(".customQQ").click(function(){
			    index=$(this).index();
				var qqCode="";
				if(index==1){//第一个qq
					qqCode="1587970167";
				}else if(index==2){//第二qq
					qqCode="1413611942";
				}else{//第三个qq
					qqCode="3275516900";
				}
				//$(".activeQQ").removeClass("activeQQ");
				//$(this).addClass("activeQQ");
				url='http://wpa.qq.com/msgrd?v=3&uin='+qqCode+'&site=qq&menu=yes';
				window.open(url);      
          });
			$(".buyCar").click(function(){
			    platform=getSystemPlatform();//computer电脑mobile手机
				 //if(platform=="computer"){//如果是电脑，才允许弹开收缩购物车
					 isOpen=$(this).attr("isopen");
					 if(isOpen=="0"){//当前是收缩，变展开
                        $(this).attr("isopen","1");
							  $("#headBuyCarContain").show();
							  //$(this).text("∨");
					 }else{//当前是展开，变收缩
                        $(this).attr("isopen","0");
							  $("#headBuyCarContain").hide();
							   //$(this).text("∧");
					 }
				 //}
			});
			//是否登录判断
			 joy.getJSON(urlPath+"wl/cm/wlCmMemberAction.dox?action=getIsLogin", function(
						resultObject) {
				  if(resultObject!=""){
					  var mphone=""
					  if(resultObject.length!=11){
					    	mphone = resultObject.substr(0, 1) + '**' + resultObject.substr(resultObject.length-1);
					    }else{
					    	mphone = '****' + resultObject.substr(7);
					    }
						$("#loginAccount").html('<a id="aPhone" onclick="toPersonInfo()" style="font-size:12px;">'+mphone+'</a>');
						$("#loginAccount").append("|<a id='logout' style='text-decoration: none;font-size:12px;' onclick='loginOut()'>SIGN OUT</a>");
						$("#loginAccount").show();
						//$("#loginAccount").attr("onclick","toPersonInfo()");
						$("#myPersion").hide();
						initShopCar();//有登录才请求购物车接口
						isLogin=true;
				  }else{
					  //$(".land").html("<a href="+contentPath+"/html/site/login.html>会员登录</a>|<a href="+contentPath+"/html/site/register.html>注册</a>");	
					  $("#myPersion").attr("onclick","toLoginPage()");
					  $("#myPersion").show();
					  $("#loginAccount").text("");
					  $("#loginAccount").hide();
					  isLogin=false;
				  }
				});
			 
			//购物车
			$("#viewCart").attr("onclick","goToShopCar()");
			$("#headBuyCarNum").text("0"); 
			//$("#anotherBuyCarCount").text("0");
			$("#divAnotherBuyCarCount").html("<span>You have 0 items in your shopping cart</span>");
			$("#headBuyCarList").html("");
			
			//initShopCar();			
			
		//	var u = navigator.userAgent;
	    //    var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端

			  $("#wlBody").mouseup(function (e) {
				    $("#bs-example-navbar-collapse-1").removeClass("in");
			    	$("#bs-example-navbar-collapse-1").attr("aria-expanded",false);		
			    	shoppingCarHidden();
			    	qqHidden();
			    });
	
			    $("#footerPart").mouseup(function (e) {
			    	$("#bs-example-navbar-collapse-1").removeClass("in");
			    	$("#bs-example-navbar-collapse-1").attr("aria-expanded",false);	
			    	shoppingCarHidden();
			    	qqHidden();
			    });
			    
			    var focus=true;
			    $("#searchQ").focus(function(){
		    		focus=false;
		        });
			    
			    $(window).scroll(function() {
			    	if(focus){
				    	$("#bs-example-navbar-collapse-1").removeClass("in");
				    	$("#bs-example-navbar-collapse-1").attr("aria-expanded",false);	
				    }else{
				    	focus=true;
				    }
			    	shoppingCarHidden();
			    	qqHidden();
				});
		    	 var htmlName=getHtmlDocName();
			     window.onresize = function(){
			    	if(htmlName.indexOf("downloadCenter")<0){
			    		var height=$("#footerPart").height()-1;
				    	$("#wlBody").attr("style","padding-bottom:"+height+"px");
			    	}			    	
			     };
			    
			
}
//是否是pc浏览
	function isPC() {
	  var userAgentInfo = navigator.userAgent;
	  var Agents = ["Android", "iPhone",
	        "SymbianOS", "Windows Phone",
	        "iPad", "iPod"];
	  var flag = true;
	  for (var v = 0; v < Agents.length; v++) {
	    if (userAgentInfo.indexOf(Agents[v]) > 0) {
	      flag = false;
	      break;
	    }
	  }
	  return flag;
	}


//购物车显示
function shoppingCarShow(){
	  $("#buyCar").attr("isopen","1");
	  $("#headBuyCarContain").show();
}
//购物车隐藏
function shoppingCarHidden(){
	  $("#buyCar").attr("isopen","0");
	  $("#headBuyCarContain").hide();
}


//qq显示
function qqShow(){
	$("#headQqIcon").attr("showState","select").attr("src",picturepath+"/style/prj/images/qq_select.png");
	$("#qqContain").show();
	//联系我们隐藏
	$("#headTelIcon").attr("showState","unselect").attr("src",picturepath+"/style/prj/images/tel_unselect.png");//
	$("#contactContain").hide();
}
//qq隐藏
function qqHidden(){
	$("#headQqIcon").attr("showState","unselect").attr("src",picturepath+"/style/prj/images/qq_unselect.jpg");
	$("#qqContain").hide();
 //联系我们隐藏
  $("#headTelIcon").attr("showState","unselect").attr("src",picturepath+"/style/prj/images/tel_unselect.png");//
  $("#contactContain").hide();
}

//联系我们显示
function phoneShow(){
	$("#headTelIcon").attr("showState","select").attr("src",picturepath+"/style/prj/images/tel_select.png");
	$("#contactContain").show();	
	//qq隐藏
	$("#headQqIcon").attr("showState","unselect").attr("src",picturepath+"/style/prj/images/qq_unselect.jpg");//
	$("#qqContain").hide();
}
//联系我们隐藏
function phoneHidden(){
	$("#headTelIcon").attr("showState","unselect").attr("src",picturepath+"/style/prj/images/tel_unselect.png");
	$("#contactContain").hide();
	//qq隐藏
	$("#headQqIcon").attr("showState","unselect").attr("src",picturepath+"/style/prj/images/qq_unselect.jpg");//
	$("#qqContain").hide();
}


//初始化购物车栏目
function initShopCar(){
	 joy.getJSON(urlPath+"wl/cm/WlCmIndexEnAction.dox?action=findMyShoppingCartPage",
			 {pageSize:2,currPage:1,isThumFlag:"1"},function(resultObject){
				 var totalCount = resultObject.totalCount;//总数
				 var txt = "";
				 var data = resultObject.items;
				 for(var i = 0; i < data.length; i++){
					  txt+=
						 '<div class="headBuyCarItem">'+
							'<img class="headProductImg" class="img-responsive" style="height:80px;width:128px;" src="'+picturepath+'/wl/cm/wlCmDocumentAction.dox?action=downloadFile&id=' +data[i]["path"] + ' ">'+
							'<div class="headProductDesc">'+
								'<div class="headProductName">'+data[i]["productName"]+'</div>'+
								'<div>￥'+data[i]["price"]+' <span class="headDeleteCarBut" onclick="delGoods('+i+',\''+data[i].shoppingCartId+'\',$(this))">Remove</span>'+
								'</div>'+
							'</div>'+
						'</div>';
				 }
				$("#headBuyCarList").html("");
				$("#headBuyCarList").append(txt);
				if(totalCount-data.length != 0){
					$("#divAnotherBuyCarCount").html('You have <span id="anotherBuyCarCount"></span> items in your shopping cart');
					$("#anotherBuyCarCount").text(totalCount-data.length);
				}else{
					$("#divAnotherBuyCarCount").html("<span>You have 0 items in your shopping cart</span>");
				}
				
				$("#headBuyCarNum").text(totalCount);
	 });
}
	//进行删除商品操作
	function delGoods(i,shoppingCartId,obj){	  	 
	  joy.showMessageDialog("Delete this product?", "ok,cancel", function(e) {
			if (e.btnName == "ok") {
					  joy.getJSON(urlPath+"wl/cm/WlCmIndexAction.dox?action=delCarGoods",{shoppingCartId:shoppingCartId},function(resultObject){	
						  obj.parents(".headBuyCarItem").remove();
						  initShopCar();
					 });
			}
		});
	  $(".k-window-title").text("Prompt");
	   $(".k-message-ok").text("Confirm");
	   $(".k-message-cancel").text("Cancel");
	}

	//去购物车
	function goToShopCar(){
		window.location = commonpath+"shoppingCar.html";
	}

	function toLoginPage(){
		window.location=commonpath+"login.html";
	}

	function toPersonInfo(){
		window.location=commonpath+"personInfo.html";
	}
	
	function loginOut(){
	   joy.showMessageDialog("Log out?", "ok,cancel", function(e) {
			if (e.btnName == "ok") { 	
				var eng = "english";
				window.location=urlPath+'wl/cm/wlCmMemberAction.dox?action=loginOut&language='+eng;
				isLogin=false;
				
			}
		});
	   $(".k-window-title").text("Prompt");
	   $(".k-message-ok").text("Confirm");
	   $(".k-message-cancel").text("Cancel");   
	}
	/**
	 * 账户信息
	 */
	function initAccountInfo(){
		var txt="";
		txt+='<div class="col-md-3" style="margin-bottom:80px;">';
		txt+='<div class="title" ><img src="'+picturepath+'/style/prj/images/account.jpg" class="titileImg">ACCOUNT INFORMATION</div>';
		txt+='<div><a href="'+commonpath+'personInfo.html" class="titleContent personInfo">Contact Information</a></div>';
		txt+='<div><a href="'+commonpath+'addrList.html" class="titleContent addrList">Address Book</a></div>';
		txt+='<div class="title"><img src="'+picturepath+'/style/prj/images/order.png" class="titileImg">ACCOUNT DASHBOARD</div>';
		txt+='<div><a href="'+commonpath+'shoppingCar.html" class="titleContent shoppingCar">Shopping Cart</a></div>';
		txt+='<div><a href="'+commonpath+'hasBuyGoods.html" class="titleContent hasBuyGoods">My Order</a></div>';
		txt+='<div class="title" style="margin-bottom: 0px;"><img src="'+picturepath+'/style/prj/images/device.png" class="titileImg">WATERROWER FORMS</div>';
		txt+='<div><a href="'+commonpath+'serialRegistDetail.html" class="titleContent serialRegistDetail">Owners Registration</a></div>';
		txt+='<div><a href="'+commonpath+'waterSliceApplyDetail.html" class="titleContent waterSliceApplyDetail">Purification Application</a></div>';
		txt+='<div><a href="'+commonpath+'repair.html" class="titleContent repair">Repair Application</a></div>';
		txt+='</div>';
		$("#rightLeft").prepend(txt);
		var topTxt="";
		topTxt+='<div class=" smallCenter" >';
		topTxt+='<div class="col-sm-4">';
		topTxt+='<div class="title"><img src="'+picturepath+'/style/prj/images/account.jpg" class="titileImg">ACCOUNT INFORMATION</div>';
		topTxt+='<div><a href="'+commonpath+'personInfo.html" class="titleContent personInfo">Contact Information</a></div>';
		topTxt+='<div><a href="'+commonpath+'addrList.html" class="titleContent addrList">Address Book</a></div>';
		topTxt+='</div>';
		topTxt+='<div class="col-sm-4">';
		topTxt+='<div class="title"><img src="'+picturepath+'/style/prj/images/order.png" class="titileImg">ACCOUNT DASHBOARD</div>';
		topTxt+='<div><a href="'+commonpath+'shoppingCar.html" class="titleContent shoppingCar">Shopping Cart</a></div>';
		topTxt+='<div ><a href="'+commonpath+'hasBuyGoods.html" class="titleContent hasBuyGoods">My Order</a></div>';
		topTxt+='</div>';
		topTxt+='<div class="col-sm-4" style="margin-bottom: 30px;">';
		topTxt+='<div class="title"><img src="'+picturepath+'/style/prj/images/order.png" class="titileImg">WATERROWER FORMS</div>';
		topTxt+='<div><a href="'+commonpath+'serialRegistDetail.html" class="titleContent serialRegistDetail">Owners Registration</a></div>';
		topTxt+='<div><a href="'+commonpath+'waterSliceApplyDetail.html" class="titleContent waterSliceApplyDetail">Purification Application</a></div>';
		topTxt+='<div><a href="'+commonpath+'repair.html" class="titleContent repair">Repair Application</a></div>';
		topTxt+='</div>';
		topTxt+='</div>';
		$("#topBottom").prepend(topTxt);
		
	}

	/**
	 * 加载底部
	 */
	function initBottom(){
		var txt="";
		txt+='<div class="container">';
		txt+='<div class="row footerInfo imgRow">';
		txt+='<div class="col-md-12">';
		txt+='<div class="pull-left" style="margin-bottom:10px;">';
		txt+='<img src="'+picturepath+'/style/prj/images/waterrower.png" class="img-responsive centerHorizontally">';
		txt+='</div>';
		txt+='<ul class="list-inline pull-right">';
		txt+='<li><a href="https://waterrower.tmall.com" target="_blank"><img src="'+picturepath+'/style/prj/images/tianmao.png" class="img-responsive" />';
		txt+='</a>';
		txt+='</li>';
		txt+='<li><a href="https://waterrower.jd.com" target="_blank"><img src="'+picturepath+'/style/prj/images/jingdong.png"class="img-responsive" /> </a>';
		txt+='</li>';
		txt+='<li><a href="https://weibo.com/u/6372013783" target="_blank"><img src="'+picturepath+'/style/prj/images/weibo.png" class="img-responsive" />';
		txt+='</a>';
		txt+='</li>';
		txt+='</ul>';
		txt+='</div>';
		txt+='</div>';
		txt+='<div class="row footerInfo">';
		txt+='<div class="col-md-12 pull-left">';
		txt+='<dl>';
		txt+='<dd>';
		txt+='<span class="block textColor" class="textColor">Address: 2F, No.3 Xiang-Ming Road, Xiang-An District, Xiamen</span>';
		txt+='</dd>';
		txt+='<dd>';
		txt+='<span class="block textColor" class="textColor">Work Hours: M-F, 8:30-18:00</span>';
		txt+='</dd>';
		txt+='<dd>';
		txt+='<span class="block textColor" class="textColor">Hotline: 400-808-4546</span>';
		txt+='</dd>';
		txt+='<dd>';
		txt+='<span class="block textColor" class="textColor">Telephone: 86-592-5923510</span>';
		txt+='</dd>';
		txt+='<dd>';
		txt+='<span class="block textColor" class="textColor">WeChat: 18150072212</span>';
		txt+='</dd>';
		txt+='<dd>';
		txt+='<span class="block textColor" class="textColor">Email: info@waterrower.com.cn</span>';
		txt+='</dd>';
		txt+='</dl>';
		txt+='</div>';
		txt+='</div>';
		txt+='<div class="underline" style="margin-bottom:15px;"></div>';
		txt+='<div class="row footerInfo waterrowerInfoRow">';
		txt+='<div class="col-md-12">';
		txt+='<div class="pull-left">';
		txt+='<span class="textColor">Copyright2018@WaterRower</span>';
		txt+='</div>';
		txt+='<ul class="list-inline pull-right">';
		txt+='<small class="textColor">';
		txt+='Copyright 2018 © WaterRower.com.cn';
		txt+='</small>';
		txt+='</ul>';
		txt+='</div>';
		txt+='</div>';
		txt+='</div>';
		$("#footerPart").prepend(txt);
		 var htmlName=getHtmlDocName();
    	 if(htmlName.indexOf("downloadCenter")<0){
    		var height=$("#footerPart").height()-1;
	    	$("#wlBody").attr("style","padding-bottom:"+height+"px");
         }			    	
//		var height=$("#footerPart").height();
//		$("#wlBody").attr("style","padding-bottom:"+height+"px");
	}
	

	function initTop() {
	var txt = "";
	txt += ' <nav id="headerPart" class="navbar navbar-default navbar-fixed-top ">';
	txt += ' <div class="container-fluid">';
	txt += ' <!-- Brand and toggle get grouped for better mobile display -->';
	txt += ' <div class="navbar-header">';
	txt += ' <button type="button" class="navbar-toggle collapsed"';
	txt += ' data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"';
	txt += ' aria-expanded="false">';
	txt += ' <span class="sr-only">sdafds navigation</span> <span';
	txt += ' class="icon-bar"></span> <span class="icon-bar"></span> <span';
	txt += ' class="icon-bar"></span>';
	txt += ' </button>';
	txt += ' <a class="navbar-brand headLogo" href="index.html"></a>';
	txt += ' </div>';
	txt += ' <!-- Collect the nav links, forms, and other content for toggling -->';
	txt += ' <div class="collapse navbar-collapse"';
	txt += ' id="bs-example-navbar-collapse-1">';
	txt += ' <ul class="nav navbar-nav">';
	txt += '			<li><a href="'+commonpath+'index.html" class="white index">HOME<span class="sr-only">(current)</span>';
	txt += '			</a></li>';
	txt += '		<li><a href="'+commonpath+'aboutWaterRower.html" class="white aboutWaterRower">RENOWNED DESIGN</a></li>';
	txt += '		<li><a href="'+commonpath+'homeExercise.html" class="white homeExercise">PERFECT EXERCISE</a></li>';
	txt += '		<li><a href="'+commonpath+'specialtyUsersBody.html" class="white specialtyUsersBody">IN THE GYM</a></li>';
	txt += '		<li><a href="'+commonpath+'productCenter.html" class="white productCenter">SHOP</a></li>';
	txt += '		<li><a href="'+commonpath+'customerCenterBody.html" class="white customerCenterBody">OWNER RESOURCES</a></li>';
	txt += '		</ul>';
	txt += '		<form class="navbar-form navbar-left">';
	txt += '			<div class="form-group">';
	txt += '			<div class="input-group">';
	txt += '			<input type="text" id="searchQ" class="form-control"';
	txt += 'aria-label="Text input with multiple buttons">';
	txt += '<div class="input-group-btn">';
	txt += '<button onclick="toSearch()" type="button" class="btn btn-default" aria-label="Help" >';
	txt += '<span class="glyphicon glyphicon-search"></span>';
	txt += '</button>';
	txt += '</div>';
	txt += '</div>';
	txt += '</div>';
	txt += '</form>';
	txt += ''
	txt += '			</div>';
	txt += '				<!-- /.navbar-collapse -->';
	txt += '			</div>';
	txt += '				<!-- /.container-fluid -->';
	txt += '			</nav>';
	txt += '	';
	txt += '			<div class="langBuy clearfix ">';
	txt += '			<div class="language">';
	txt += '			<span class="engAndChi" id="chinese" onclick="toCnHtml()" style="font-size:12px;" >中文</span>|<span class="engAndChi" id="english" onclick="toEnHtml()" style="font-size:12px;">English</span>';
	txt += '		</div>';
	txt += '		<div class="loginInfo">';
	txt += '			<div id="headBuyCarIconContain">';
	if(isPcFlag){
		txt += ' <img id="buyCar" src="'+picturepath+'/style/prj/images/buyCar.png" onmouseover = "shoppingCarShow()"  onmouseout = "shoppingCarHidden()" class="buyCar" isopen="0" style="width:25px;"/>';
	}else{
		txt += ' <img id="buyCar" src="'+picturepath+'/style/prj/images/buyCar.png"  class="buyCar" isopen="0" style="width:25px;"/>';
	}
	txt += '		<span id="headArrow" isopen="0"></span>';
	txt += '			<!--∧-->';
	txt += '		<span style="cursor:pointer;" class="buyCar" id="headBuyCarNum">1</span>';
	txt += '			</div>';
	txt += '			<div id="loginAccount" style="cursor:pointer; line-height: 25px;" class="white" style="display: none;">18859230385</div>';
	txt += '				<!--登录后-->';
	txt += '			<img id="myPersion" style="cursor:pointer;width: 14px;" src="'+picturepath+'/style/prj/images/myPerson.png" />';
	txt += '				<!--登录前-->';
	txt += '			</div>';
	txt += '			</div>';
	txt += '				<!--头部结束-->';
	txt += '				<!--qq和联系我们开始-->';
	txt += '			<div id="onlineContact">';
	if(isPcFlag){
		txt += '			<div class="floatRight">';
		txt += '			<img id="headQqIcon" src="'+picturepath+'/style/prj/images/qq_unselect.jpg"';
		txt += '		showState="unselect" style="cursor:pointer;" class="floatRight" onmouseover = "qqShow()"  onmouseout = "qqHidden()"/>';
		txt += '			<div>';
		txt += '			<img id="headTelIcon" src="'+picturepath+'/style/prj/images/tel_unselect.png"';
		txt += '		showState="unselect" style="cursor:pointer;" class="floatRight" onmouseover = "phoneShow()"  onmouseout = "phoneHidden()" />';
		txt += '			</div>';
	}else{
		txt += '			<div class="floatRight">';
		txt += '			<img id="headQqIcon" src="'+picturepath+'/style/prj/images/qq_unselect.jpg"';
		txt += '		showState="unselect" style="cursor:pointer;" class="floatRight" />';
		txt += '			<div>';
		txt += '			<img id="headTelIcon" src="'+picturepath+'/style/prj/images/tel_unselect.png"';
		txt += '		showState="unselect" style="cursor:pointer;" class="floatRight" />';
		txt += '			</div>';
	}
	txt += '			</div>';
	txt += '			<div class="floatRight">';
	txt += '				<!--三个qq客服容器开始-->';
	txt += '			<div id="qqContain" onmouseover = "qqShow()"  onmouseout = "qqHidden()">';
	txt += '			<div class="white" style="color: white;">QQ</div>';
	 if(online.length==0){
		  txt += '<span class="activeQQ customQQ"><img src="'+picturepath+'/style/prj/images/qq.png" /><span class="white qqContactUsText" style="color: white;">CONTACT ME</span> </span> ';
		  txt += '<span class="activeQQ customQQ"><img src="'+picturepath+'/style/prj/images/qq.png" /><span class="white qqContactUsText" style="color: white;">CONTACT ME</span> </span> ';
		  txt += '<span class="activeQQ customQQ"><img src="'+picturepath+'/style/prj/images/qq.png" /><span class="white qqContactUsText" style="color: white;">CONTACT ME</span> </span> ';
	  }
	 else{
	  for (var i = 0; i < online.length; i++) { 
	        if (online[i] == 0) { 
	        	txt += '<span class="customQQ"><img src="'+picturepath+'/style/prj/images/qq.png" /><span class="white qqContactUsText" style="color: white;">CONTACT ME</span> </span> ';
	        } else { 
	        	txt += '<span class="activeQQ customQQ"><img src="'+picturepath+'/style/prj/images/qq.png" /><span class="white qqContactUsText" style="color: white;">CONTACT ME</span> </span> ';
	        } 
	    }  
	 }
	txt += '		</div>';
	txt += '			<!--三个qq客服容器结束-->';
	txt += '			<!--电话开始-->';
	txt += '		<div id="contactContain" onmouseover = "phoneShow()"  onmouseout = "phoneHidden()">';
	txt += '			<div class="headContactUsTxt">CONTACT ME</div>';
	txt += '			<div>HOTLINE：400-808-4546</div>';
	txt += '		<div>TEL：0592-5923128</div>';
	txt += '		</div>';
	txt += '			<!--电话结束-->';
	txt += '		</div>';
	txt += '		</div>';
	txt += '			<!--qq和联系我们结束-->';
	txt += '			<!--头部购物车开始-->';
	txt += '		<div id="headBuyCarContain" onmouseover = "shoppingCarShow()" onmouseout = "shoppingCarHidden()">';
	txt += '				<!--标题开始-->';
	txt += '			<div class="hasAddBuyCarTitle">MY CART</div>';
	txt += '				<!--商品列表部分开始-->';
	txt += '			<div id="headBuyCarList">';
	txt += '			</div>';
	txt += '				<!--商品列表部分结束-->';
	txt += '				<!--灰色线-->';
	txt += '			<div class="headGrayLine"></div>';
	txt += '				<!--购物车里还有-->';
	txt += '			<div class="anotherBuyCarCount" id="divAnotherBuyCarCount">';
	txt += '			There are <span id="anotherBuyCarCount"></span> products in your shopping cart';
	txt += '			</div>';
	txt += '			<a href="#"><div id="viewCart">VIEW CART</div> </a>';
	txt += '		</div>';
	$("#top").prepend(txt);
}
	
//搜索
function toSearch() {
	var searchQ =document.getElementById("searchQ").value;
	var myurl = "search.html" + "?" + "searchQ=" + searchQ;
	window.location.assign(encodeURI(myurl));
}       

//改变分页的样式
function changeStyle(){
	  if($(window).width() < 460){
			$("#pagination").parents(".col-xs-12").css({"paddingLeft":"8px","paddingRight":"8px;","fontSize":"10px"});
			$("#pagination li a").css({"paddingLeft":"6px","paddingRight":"6px;"});
			//$("input[type=button]").css({"width":"35px"}); 
		}else{
			$("#pagination").parents(".col-xs-12").css({"paddingLeft":"12px","paddingRight":"12px;","fontSize":"14px"});
			$("#pagination li a").css({"paddingLeft":"12px","paddingRight":"12px;"});
			//$("input[type=button]").css({"width":"45px"}); 
		}
}
//获取html文件名
function getHtmlDocName() {
    var str = window.location.href;  
    if(str.indexOf("return_url")>=0){
    	str=str.substring(str.indexOf("return_url"),str.length);
    	
    }else if(str.indexOf("acp_front_url")>=0){
    	str=str.substring(str.indexOf("acp_front_url"),str.length);
    }else{
    	str = str.substring(str.lastIndexOf("/") + 1);
    }
    
    return str;
}
//跳转到中文页面
function toCnHtml(){
	var str = getHtmlDocName();
	if(str.indexOf("return_url")>=0){
		window.location = "../alipay/"+str;
	}else if(str.indexOf("acp_front_url")>=0){
		window.location = "../unionpay/"+str;
	}else{
		window.location = "../newsite/"+str;
	}	
}
//跳转到英文页面
function toEnHtml(){
	var str = getHtmlDocName();
	if(str.indexOf("return_url")>=0){
		window.location = "../engAlipay/"+str;
	}else if(str.indexOf("acp_front_url")>=0){
		window.location = "../engUnionpay/"+str;
	}else{
		window.location = "../engsite/"+str;
	}	
	
}


