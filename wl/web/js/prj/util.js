/**
 * 数据判断是否为ARRAY，如果不是则创建长度未1的数组，并将传入的值作为数组值
 */
function arrayObjectJudge(obj)
{
	if(obj==null||obj=="")
		{
		return "";
		}
	if(obj.constructor!= Array)
		{
		 var  list=new Array(0);
		  list[0]=obj;
		  return list;
		}
	else
		{
		return obj;
		}
}
/**
 * 为Date添加一个原型方法，可以返回不同的时间格式
 * author:wangts
 */
Date.prototype.Format = function (fmt) { 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}