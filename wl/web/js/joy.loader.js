//var JOY_LANGUAGE="en-US"; 
var JOY_LANGUAGE="zh-CN"; 
var JOY_THEMES="joystyle";  
var NoSecurityUrl = "../../html/base/login.html"; 
var WebNoSecurityUrl = loginHref(); 

var AgencyNoSecurityUrl = "../../html/agency/login.html"; 
var rootUrl = "http://"+window.location.host+"/wl";
var ImageBrowser = {
        transport: {
             read: { //删除
                 url: rootUrl+"/wl/cm/wlCmDocumentAction.dox?action=searchFile",
                 type: "POST",
                 dataType:"json"
             },
             destroy: { //删除
                 url: rootUrl+"/wl/cm/wlCmDocumentAction.dox?action=deleteFile",
                 type: "POST",
                 dataType:"json"
             },
             thumbnailUrl:rootUrl+"/wl/cm/wlCmDocumentAction.dox?action=downloadFile", //缩略图
             uploadUrl: rootUrl+"/wl/cm/wlCmDocumentAction.dox?action=uploadEditorFile",
             imageUrl: rootUrl+"/wl/cm/wlCmDocumentAction.dox?action=downloadFile&id={0}"
        }
     };//图片管理器定义

__CreateJSPath = function (js) {	
    var scripts = document.getElementsByTagName("script");
    var path = "";
	var modules;
    for (var i = 0, l = scripts.length; i < l; i++) {
        var src = scripts[i].src;			
        if (src.indexOf(js) != -1) {
            var ss = src.split(js); 
            path = ss[0];
			modules=("common," + scripts[i].getAttribute("modules")).split(",");
            break;
        } 
    }
    var href = location.href; 	
    href = href.split("#")[0];
    href = href.split("?")[0];
    var ss = href.split("/");
    var fileName=ss[ss.length-1];
    var files=fileName.split(".");
    files.length = files.length - 1;
    fileName=files.join(".");    
    ss.length = ss.length - 1;
    href = ss.join("/");	
    if (path.indexOf("https:") == -1 && path.indexOf("http:") == -1 && path.indexOf("file:") == -1 && path.indexOf("\/") != 0) {
        path = href + "/" + path;
    }
    var index=href.lastIndexOf("/html");
	var langPath=href.substring(0,index) + "/lang/" + href.substr(index+6);
	var elementAttr={rootPath:path,modules:modules,langPath:langPath,fileName:fileName}
    return elementAttr;
}

var _joyLoader = __CreateJSPath("js/joy.loader.js");

var JOY_FILE_NAME=_joyLoader.fileName;


var _joyModules = {common:[''],
		form:[''],
		layout:[''],
		menu:[''],  
		dataview:[''],
		advgrid:[''],
		editor:[''],
		schedule:[''],
		mapcontrol:[''],
		chart:[''],
		diagram:['']
	};

//判断跳转到中英文登录界面
function loginHref(){
	var href = window.location.href;
	var url="../../html/newsite/login.html";
	if(href.indexOf("engsite")>0){
		url = "../../html/engsite/login.html"; 
	}
	return url;
}

//当前项目公用js
document.write('<script src="' + _joyLoader.rootPath + 'js/prj/util.js" type="text/javascript" ></script>');
//language
document.write('<script src="' + _joyLoader.rootPath + 'lang/joy-lang.' + JOY_LANGUAGE+ '.js" type="text/javascript" ></script>');
document.write('<script src="' + _joyLoader.rootPath + 'lang/project-common-lang.' + JOY_LANGUAGE+ '.js" type="text/javascript" ></script>');
document.write('<script src="' + _joyLoader.langPath + '/joy-lang.' + JOY_LANGUAGE+ '.js" type="text/javascript" ></script>');

//module
document.write('<script src="' + _joyLoader.rootPath + 'js/joy.core.js" type="text/javascript" ></script>');

//other
document.write('<script src="' + _joyLoader.rootPath + 'js/joy.xdm.js" type="text/javascript" ></script>');
document.write('<script src="' + _joyLoader.rootPath + 'js/joy.xml2json.js" type="text/javascript" ></script>');

for (var i = 0; i < _joyLoader.modules.length; i++) { 
	 document.write('<script src="' + _joyLoader.rootPath + 'js/joy.' + _joyLoader.modules[i] + '.js" type="text/javascript" ></script>');
}
// uploaden module
document.write('<script src="' + _joyLoader.rootPath + 'js/joy.uploaden.js" type="text/javascript" ></script>');
//themes
document.write('<link href="' + _joyLoader.rootPath + 'style/joy.common.core.css" rel="stylesheet" type="text/css" />');
document.write('<link href="' + _joyLoader.rootPath + 'style/joy.common.css" rel="stylesheet" type="text/css" />');
document.write('<link href="' + _joyLoader.rootPath + 'style/' + JOY_THEMES +  '/joy.css" rel="stylesheet" type="text/css" />');
