//var JOY_LANGUAGE="en-US"; 
var JOY_LANGUAGE="zh-CN"; 
var JOY_THEMES="default";  
var NoSecurityUrl = "../../html/base/login.html";

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


var _joyModules = {
		common:['common/joy.lang.js',
		        'common/joy.router.js',
		        'common/joy.data.js',
		        'common/joy.data.xml.js',
		        'common/joy.data.odata.js',
		        'common/joy.popup.js',
		        'common/joy.label.js',
		        'common/joy.button.js',
		        'common/joy.calendar.js',
		        'common/joy.userevents.js',
		        'common/joy.draganddrop.js',
		        'common/joy.binder.js',
		        'common/joy.fx.js',
		        'common/joy.window.js',
		        'common/joy.notification.js',
		        'common/joy.message.js'],
		form:['form/joy.form.js',
		      'form/joy.formelement.js',
		      'form/joy.list.js',
		      'form/joy.combobox.js',
		      'form/joy.multiselect.js',
		      'form/joy.numerictextbox.js',
		      'form/joy.datepicker.js',
		      'form/joy.timepicker.js',
		      'form/joy.datetimepicker.js',
		      'form/joy.validator.js',
		      'form/joy.dropdownlist.js',
		      'form/joy.textbox.js',
		      'form/joy.radio.js',
		      'form/joy.checkbox.js',
		      'form/joy.textarea.js',
		      'form/joy.colorpicker.js',
		      'form/joy.imagebrowser.js',
		      'form/joy.slider.js',
		      'form/joy.upload.js',
		      'form/joy.autocomplete.js'],
		layout:['layout/joy.tabstrip.js',
		        'layout/joy.accordion.js'],
		menu:['menu/joy.menu.js'],  
		dataview:['dataview/joy.filtermenu.js',
		          'dataview/joy.pager.js',
		          'dataview/joy.columnmenu.js',
		          'dataview/joy.editable.js',
		          'dataview/joy.groupable.js',
		          'dataview/joy.reorderable.js',
		          'dataview/joy.resizable.js',
		          'dataview/joy.selectable.js',
		          'dataview/joy.sortable.js',
		          'dataview/joy.grid.js', 
		          'dataview/joy.listview.js',
		          'dataview/joy.treeview.js'],
		advgrid:['advgrid/joy.resizable.js', 
		         'advgrid/joy.pagination.js', 
		         'advgrid/joy.linkbutton.js', 
		         'advgrid/joy.panel.js',
		         'advgrid/joy.datagrid.js',  
		         'advgrid/joy.treegrid.js'],
		editor:['editor/joy.editor.js'],
		schedule:['schedule/joy.scheduler.recurrence.js',
		          'schedule/joy.scheduler.view.js',
		          'schedule/joy.scheduler.dayview.js',
		          'schedule/joy.scheduler.agendaview.js',
		          'schedule/joy.scheduler.monthview.js',
		          'schedule/joy.scheduler.js'],
		mapcontrol:['map/joy.mapcontrol.js',
		            'map/joy.drawtool.js',
		            "map/joy.measuretool.js",
		            "map/joy.zoomtool.js",
		            "map/joy.toc.js"],
		chart:['chart/joy.core.js',
		         'chart/joy.svg.js',
		         'chart/joy.vml.js',
		         'chart/joy.themes.js',
		         'chart/joy.canvas.js', 
		         'chart/joy.barcode.js',
		         'chart/joy.chart.js',
		         'chart/joy.chart.funnel.js',
		         'chart/joy.chart.polar.js',
		         'chart/joy.gauge.js',
		         'chart/joy.qrcode.js',
		         'chart/joy.sparkline.js',
		         'chart/joy.stock.js'],
		diagram:['diagram/joy.diagram.js']
	};

//当前项目公用js
document.write('<script src="' + _joyLoader.rootPath + 'js/prj/util.js" type="text/javascript" ></script>');
//language
document.write('<script src="' + _joyLoader.rootPath + 'lang/joy-lang.' + JOY_LANGUAGE+ '.js" type="text/javascript" ></script>');
document.write('<script src="' + _joyLoader.rootPath + 'lang/project-common-lang.' + JOY_LANGUAGE+ '.js" type="text/javascript" ></script>');
document.write('<script src="' + _joyLoader.langPath + '/joy-lang.' + JOY_LANGUAGE+ '.js" type="text/javascript" ></script>');

//module
document.write('<script src="' + _joyLoader.rootPath + 'js/joy.core.js" type="text/javascript" ></script>');

//other
document.write('<script src="' + _joyLoader.rootPath + 'js/joy.xdm.min.js" type="text/javascript" ></script>');
document.write('<script src="' + _joyLoader.rootPath + 'js/joy.xml2json.min.js" type="text/javascript" ></script>');

for (var i = 0; i < _joyLoader.modules.length; i++) {
	var module=_joyModules[_joyLoader.modules[i]];
	for (var j = 0; j < module.length; j++) {
		document.write('<script src="' + _joyLoader.rootPath + 'js/' + module[j] + '" type="text/javascript" ></script>');
	}
}

//themes
document.write('<link href="' + _joyLoader.rootPath + 'style/joy.common.core.css" rel="stylesheet" type="text/css" />');
document.write('<link href="' + _joyLoader.rootPath + 'style/joy.common.css" rel="stylesheet" type="text/css" />');
document.write('<link href="' + _joyLoader.rootPath + 'style/' + JOY_THEMES +  '/joy.css" rel="stylesheet" type="text/css" />');



	
