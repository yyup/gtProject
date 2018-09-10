package com.gt.wl.util;

import java.util.ResourceBundle;

/**
 * Gotye参数类
 * Gotye.properties中配置
 * @author huangbj
 * 
 */
public class CommonConf {
	private final static ResourceBundle CONFIG;
	static {
		CONFIG = ResourceBundle.getBundle("common");
	}
	public final static String storageId = CONFIG.getString("storageId");

}
