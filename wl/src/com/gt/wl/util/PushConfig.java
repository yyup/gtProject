package com.gt.wl.util;

import java.util.ResourceBundle;

/**
 * push服务器配置获取
 * @author liuyj
 * 
 */
public class PushConfig {
	private final static ResourceBundle CONFIG;
	static {
		CONFIG = ResourceBundle.getBundle("push");
	}

	// push服务器地址
	public static String PUSH_SERVER = CONFIG.getString("push_server");

	// 推送端口
	public static String PUSH_PORT = CONFIG.getString("push_port");

}
