package com.gt.wl.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.joyone.util.Md5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 短信发送服务
 * 使用互亿无线短信平台
 * @author liuyj
 * 
 */
public class SendSms {
	public final static Logger log = LoggerFactory.getLogger(SendSms.class);
	private static String url = "http://106.ihuyi.cn/webservice/sms.php?method=Submit";// 互亿无线短信http接口

	/**
	 * 发送短信
	 * @param mobile 手机号码
	 * @param identifyCode 验证码
	 * @param content 发送的内容
	 * @return 发送成功返回true，否则返回false
	 */
	public static boolean sendMsg(String mobile, int identifyCode,String content) {
		try {
			HttpClient client = new HttpClient();
			PostMethod method = new PostMethod(url);
			// 设置字符集为UTF8
			client.getParams().setContentCharset("UTF-8");
			method.setRequestHeader("ContentType", "application/x-www-form-urlencoded;charset=UTF-8");
			// 拼接短信内容
			//String content = new String("您的验证码是：" + identifyCode + "。请不要把验证码泄露给其他人。如非本人操作，可不用理会！");
			// 提交短信内容
			// 密码可以使用明文密码或使用32位MD5加密
			// new NameValuePair("password",StringUtil.MD5Encode("密码")),
			NameValuePair[] data = { new NameValuePair("account", "cf_inprise"), new NameValuePair("password", Md5Util.encrypt("800328")), new NameValuePair("mobile", mobile), new NameValuePair("content", content), };

			method.setRequestBody(data);
			// 执行
			client.executeMethod(method);
			// 得到结果
			String SubmitResult = method.getResponseBodyAsString();
			// System.out.println("SubmitResult=" + SubmitResult);
			// 解析结果xml
			Document doc = DocumentHelper.parseText(SubmitResult);
			Element root = doc.getRootElement();
			String code = root.elementText("code");

			// String msg = root.elementText("msg");
			// String smsid = root.elementText("smsid");
			// System.out.println("msg=" + msg);
			if ("2".equals(code)) {
				// System.out.println("短信提交成功");
				return true;
			}

		}
		catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return false;
		}
		return false;
	}

	/**
	 * 发送短信
	 * @param mobile 手机号码
	 * @return 发送成功返回true，否则返回false
	 */
	public static boolean sendMsg(String mobile) {
		// 随机产生验证码
		int identifyCode = (int) ((Math.random() * 9 + 1) * 100000);
		return sendMsg(mobile, identifyCode,"");
	}

}
