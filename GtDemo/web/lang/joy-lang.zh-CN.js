(function(window, undefined) {
	var joy = window.joy || (window.joy = {});
	joy.lang ={};
	joy.pluginLang = {
		name : "zh-CN",
		numberFormat : {
			pattern : [ "-n" ],
			decimals : 2,
			"," : ",",
			"." : ".",
			groupSize : [ 3 ],
			percent : {
				pattern : [ "-n%", "n%" ],
				decimals : 2,
				"," : ",",
				"." : ".",
				groupSize : [ 3 ], 
				symbol : "%"
			},
			currency : {
				pattern : [ "$-n", "$n" ],
				decimals : 2,
				"," : ",",
				"." : ".",
				groupSize : [ 3 ],
				symbol : "¥"
			}
		},
		calendars : {
			standard : {
				days : {
					names : [ "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" ],
					namesAbbr : [ "周日", "周一", "周二", "周三", "周四", "周五", "周六" ],
					namesShort : [ "日", "一", "二", "三", "四", "五", "六" ]
				},
				months : {
					names : [ "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月",
							"九月", "十月", "十一月", "十二月", "" ],
					namesAbbr : [ "一月", "二月", "三月", "四月", "五月", "六月", "七月",
							"八月", "九月", "十月", "十一月", "十二月", "" ]
				},
				AM : [ "上午", "上午", "上午" ],
				PM : [ "下午", "下午", "下午" ],
				patterns : {
					d : "yyyy-M-d",
					D : "yyyy'年'M'月'd'日'",
					F : "yyyy'年'M'月'd'日' H:mm:ss",
					g : "yyyy-M-d H:mm",
					G : "yyyy-M-d H:mm:ss",
					m : "M'月'd'日'",
					M : "M'月'd'日'",
					s : "yyyy'-'MM'-'dd'T'HH':'mm':'ss",
					t : "H:mm",
					T : "H:mm:ss",
					u : "yyyy'-'MM'-'dd HH':'mm':'ss'Z'",
					y : "yyyy'年'M'月'",
					Y : "yyyy'年'M'月'"
				},
				"/" : "/",
				":" : ":",
				firstDay : 0
			}
		},
		button : {
			msg : "按钮消息"
		},
		grid : {
			delete_confirm : "确定删除此条记录？",
			add_new_record : "新增",
			cancel_changes : "取消修改",
			save_changes : "保存修改",
			delete_record : "删除",
			edit : "编辑",
			update : "保存",
			cancel : "取消",
		    control: "操作"
		},
		groupable : {
			empty : "拖动列标题到这里，表内容将以此列进行分组"
		},
		pager : {
			display : "显示{0} - {1} 共 {2} 项",
			empty : "无结果项",
			page : "第",
			of : "页    共{0}页 ",
			itemsPerPage : "项每页",
			first : "首页",
			previous : "前一页",
			next : "下一页",
			last : "末页",
			refresh : "刷新",
			morePages : "更多"
		},
		filtermenu:{
			EQ:"等于",
			NEQ:"不等于",
			 string: { 
                 startswith: "以什么字符开头",
                 contains: "包含",
                 doesnotcontain: "不包含",
                 endswith: "以什么字符结尾"
             },
             number: { 
                 gte: "大于或等于",
                 gt: "大于",
                 lte: "小于或等于",
                 lt: "小于"
             },
             date: { 
                 gte: "这个日期之后且包含",
                 gt: "在这个日期之后",
                 lte: "这个日期之前且包含",
                 lt: "在这个日期之前"
             },
             info: "显示项的值:",
             isTrue: "为真",
             isFalse: "为假",
             filter: "过滤",
             clear: "清空",
             and: "和",
             or: "或",
             selectValue: "-选择值-",
             operator: "操作员",
             value: "值",
             cancel: "取消",
             left: "slide",
             right: "slide:right" 
		},
		columnmenu:{
			sortAscending: "升序",
            sortDescending: "降序",
            filter: "过滤",
            columns: "显示列",
            done: "完成",
            settings: "列设置",
            lock: "锁定",
            unlock: "解锁"
		},
		editor : {
			bold : "粗体",
			italic : "斜体",
			underline : "下划线",
			strikethrough : "删除线",
			superscript : "上标",
			subscript : "下标",
			justifyCenter : "居中",
			justifyLeft : "左对齐",
			justifyRight : "右对齐",
			justifyFull : "两端对齐",
			insertUnorderedList : "插入无序列表",
			insertOrderedList : "插入排序列表",
			indent : "缩进",
			outdent : "减少缩进",
			createLink : "插入超链接",
			unlink : "删除超链接",
			insertImage : "插入图片",
			insertHtml : "插入HTML",
			viewHtml : "查看HTML",
			fontName : "选择字体",
			fontNameInherit : "(继承字体)",
			fontSize : "选择字号",
			fontSizeInherit : "(继承字号)",
			formatBlock : "格式",
			formatting : "格式",
			foreColor : "颜色",
			backColor : "背景色",
			style : "样式",
			emptyFolder : "空文件夹",
			uploadFile : "上传",
			orderBy : "排序:",
			orderBySize : "大小",
			orderByName : "名称",
			invalidFileType : "选择的文件 \"{0}\" 无效. 支持的文件格式为 {1}.",
			deleteFile : '确定删除  "{0}"?',
			overwriteFile : '文件名为 "{0}" 的文件在文件夹中已经存在y. 是否覆盖?',
			directoryNotFound : "未找到此文件夹.",
			imageWebAddress : "网址",
			imageAltText : "替换文字",
			imageWidth : "宽度 (px)",
			imageHeight : "高度 (px)",
			linkWebAddress : "网址",
			linkText : "文本",
			linkToolTip : "提示框",
			linkOpenInNewWindow : "在新的窗体中打开文本",
			dialogUpdate : "更新",
			dialogInsert : "添加",
			dialogButtonSeparator : "或",
			dialogCancel : "取消",
			createTable : "新建表",
			addColumnLeft : "在左边添加列",
			addColumnRight : "在右边添加列",
			addRowAbove : "在上面添加行",
			addRowBelow : "在下面添加行",
			deleteRow : "删除行",
			deleteColumn : "删除列",
		    createTableA:"新建一个{0} x {1}表",
		    cancelCreateTable:"关闭"
		},
		numerictextbox:{
			upArrowText: "增加",
            downArrowText: "减少"
		},
		slider:{
			increaseButtonTitle: "增加",
            decreaseButtonTitle: "减少",
            dragHandleTitle: "拖动"
		},
		message : {
			okCommand : "确定",
			cancelCommand : "取消",
			yesCommand : "是的",
			noCommand : "不是",
            dialogTitle : "提示窗口"
		},
		datepicker:{
			minInfo:"输入的时间必须大于{0}",
			maxInfo:"输入的时间必须小于{0}",		
			yearRuleInfo:"年度格式有误,如:2014",
			monthRuleInfo:"年月格式有误,如:2014-6",
			dayRuleInfo:"日期格式有误,如:2014-6-30",
			dayTimeRuleInfo:"时间格式有误,如:2014-6-30 11:00",
			timeRuleInfo:"时间格式有误,如:11:00"
		},
		form:{
			requiredInfo:"请输入{0}信息",
			minByteInfo:"长度须大于{0}字节,已输入{1}",
			maxByteInfo:"长度须小于{0}字节,已输入{1}"
		},
		textbox:{
			email:"电子邮件格式有误,如:abc@163.com",
			postCode:"邮政编码格式有误",
			postCodeRule:/^(\d{6}|())$/,
			tel:"电话格式有误,如:0592-2188888",
			telRule:/^(((\d+\-?\d*)+\,?)*|())$/,
			mobileTel:"手机号码格式有误",
			mobileTelRule:/^(((\d+\-?\d*)+\,?)*|())$/,
			idCard:"身份证号码格式有误",
			idCardRule:/^((QT-\S+)|(\d{15})|((\d{17})(X|x|\d{1}))|())$/, 
			entCode:"法人代码格式有误，如：435834567(9位)",
			entCodeRule:/^([a-zA-Z0-9]{9}|())$/,
			IP:"IP地址有误,如：192.168.1.1" 
		},
		upload:{
			"select": "选择文件...",
            "cancel": "取消",
            "retry": "重试",
            "remove": "删除",
            "uploadSelectedFiles": "上传文件",
            "dropFilesHere": "拖动文件到这里上传",
            "statusUploading": "正在上传",
            "statusUploaded": "上传结束",
            "statusWarning": "警告",
            "statusFailed": "失败",
            "headerStatusUploading": "正在上传...",
            "headerStatusUploaded": "完成",
            "maxSize":"选择的文件大小超过限制，大小不得超过：",
            "allowType":"上传文件格式不符合规范，支持格式为："
		},
		common:{
			ajaxError:"服务请求出错"
		},
		mapcontrol:{
			map: "地图",
			image: "影像",
			panN: "向上平移",
			panW: "向左平移",
			panE: "向右平移",
			panS: "向下平移",
			kilometer: "千米",
			squareKilometers: "平方千米"
		},
		colorpicker:{
			apply:"确定",
			cancel:"取消",
			ARIATemplate:"选择的颜色是#=data||''#"
		},
		scheduler:{
			   messages: {
	                today: "今天",
	                save: "保存",
	                cancel: "取消",
	                destroy: "删除",
	                deleteWindowTitle: "删除事件",
	                ariaSlotLabel: "选择从 {0:t} 到 {1:t}",
	                ariaEventLabel: "{0} on {1:D} at {2:t}",
	                views: {
	                    day: "日视图",
	                    week: "周视图",
	                    workWeek: "工作日视图",
	                    agenda: "日程",
	                    month: "月视图"
	                },
	                recurrenceMessages: {
	                    deleteWindowTitle: "删除重复项",
	                    deleteWindowOccurrence: "删除当前项",
	                    deleteWindowSeries: "删除列",
	                    editWindowTitle: "编辑重复项",
	                    editWindowOccurrence: "编辑当前发生项",
	                    editWindowSeries: "编辑列"
	                },
	                editor: {
	                    title: "标题",
	                    start: "开始",
	                    end: "结束",
	                    allDayEvent: "全天事件",
	                    description: "描述",
	                    repeat: "",
	                    timezone: " ",
	                    startTimezone: "开始时区",
	                    endTimezone: "结束时区",
	                    separateTimezones: "使用单独的开始和结束时区",
	                    timezoneEditorTitle: "时区",
	                    timezoneEditorButton: "时区",
	                    timezoneTitle: "时区",
	                    noTimezone: "无时区",
	                    editorTitle: "事件"
	                }
	            },
	            agendaview:{
	            	messages:{
	                    event: "事件",
	                    date: "日期",
	                    time: "时间"
	                } 
	            },
	            dayview:{
	            	messages: {
	                    allDay: "全天",
	                    showFullDay: "全时段",
	                    showWorkDay: "工作时段"
	                }
	            }
		}
		
	}
})(this);