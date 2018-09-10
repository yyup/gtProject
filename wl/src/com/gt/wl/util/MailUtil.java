package com.gt.wl.util;

import javax.mail.internet.MimeUtility;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;
import org.joyone.spring.Sc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gt.wl.cm.service.WlCmCommonSetService;

public class MailUtil {
	public final static Logger log = LoggerFactory.getLogger(MailUtil.class);
	private WlCmCommonSetService wlCmCommonSetService = (WlCmCommonSetService) Sc.getBean("wl.cm.WlCmCommonSetService");
	// smtp服务器
	private static String HOST_NAME = "";
	// 帐号与密码
	private static String USER_NAME = "";
	private static String PASSWORD = "";
	// 发件人
	private static String FROM_ADDRESS = "";
	// 发件人姓名
	private static String FROM_NAME = "";

	public MailUtil() {
		HOST_NAME = wlCmCommonSetService.getWlCmCommonSetByKey("SMTP_SERVER").getSetValue();
		USER_NAME = wlCmCommonSetService.getWlCmCommonSetByKey("USER_NAME").getSetValue();
		PASSWORD = wlCmCommonSetService.getWlCmCommonSetByKey("PASSWORD").getSetValue();
		FROM_ADDRESS = wlCmCommonSetService.getWlCmCommonSetByKey("FROM_ADDRESS").getSetValue();
		FROM_NAME = wlCmCommonSetService.getWlCmCommonSetByKey("FROM_NAME").getSetValue();
	}

	/**
	 * 发送简单邮件，类似一条信息
	 * @param toAddress 收件人邮箱地址
	 * @param subJect 主题
	 * @param content 内容
	 */
	public static void sendSimpleEmail(String toAddress, String subJect, String content) {
		try {
			SimpleEmail email = new SimpleEmail();
			// 设置smtp服务器
			email.setHostName(HOST_NAME);
			// email.setSmtpPort(25);
			// 设置授权信息
			email.setAuthentication(USER_NAME, PASSWORD);
			email.setCharset("utf-8");
			// 设置发件人信息
			email.setFrom(FROM_ADDRESS, FROM_NAME);
			// 设置收件人信息
			email.addTo(toAddress);
			// 设置主题
			email.setSubject(subJect);
			// 设置邮件内容
			email.setMsg(content);
			// 发送邮件
			email.send();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送Html内容的邮件
	 * @param toAddress 收件人邮箱地址
	 * @param subJect 主题
	 * @param content 内容
	 */
	public static void sendHtmlEmail(String toAddress, String subJect, String content) {
		try {
			HtmlEmail email = new HtmlEmail();
			email.setHostName(HOST_NAME);
			email.setAuthentication(USER_NAME, PASSWORD);
			email.setCharset("utf-8");
			email.addTo(toAddress);
			email.setFrom(FROM_ADDRESS, FROM_NAME);
			email.setSubject(subJect);
			email.setHtmlMsg(content);
			email.send();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 发送复杂的邮件，包含附件等
	 * @param toAddress 收件人邮箱地址
	 * @param subJect 主题
	 * @param content 内容
	 * @param filePath 本地文件路径（例D:\\rfid.log）
	 * @param fileName 附件显示文件名
	 */
	public static void sendMultiPartEmail(String toAddress, String subJect, String content, String filePath, String fileName) {
		try {
			MultiPartEmail email = null;
			email = new MultiPartEmail();
			email.setHostName(HOST_NAME);
			email.setAuthentication(USER_NAME, PASSWORD);
			email.setCharset("utf-8");
			email.addTo(toAddress);
			email.setFrom(FROM_ADDRESS, FROM_NAME);
			email.setSubject(subJect);
			email.setMsg(content);

			// 为邮件添加附加内容
			EmailAttachment attachment = new EmailAttachment();
			attachment.setPath(filePath);// 本地文件
			// attachment.setURL(new URL("http://xxx/a.gif"));//远程文件
			// attachment.setDisposition(EmailAttachment.ATTACHMENT);
			// attachment.setDescription("描述信息");
			// 设置附件显示名字，必须要编码，不然中文会乱码
			attachment.setName(MimeUtility.encodeText(fileName));
			// 将附件添加到邮件中
			email.attach(attachment);
			email.send();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		// 收件人与收件人名字
		String toAddress = "1498392140@qq.com";
		MailUtil test = new MailUtil();
		MailUtil.sendSimpleEmail(toAddress, "这是简单邮件", "测试");
		MailUtil.sendHtmlEmail(toAddress, "这是HTML邮件", "<a href='www.baidu.com'>测试</a>");
		MailUtil.sendMultiPartEmail(toAddress, "这是带附件邮件", "测试", "D:\\rfid.log", "邮件.log");
		System.out.println("发送完成");
	}

}
