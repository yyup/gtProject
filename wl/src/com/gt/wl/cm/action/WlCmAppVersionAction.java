package com.gt.wl.cm.action;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.joyone.action.BaseAction;
import org.joyone.lang.BaseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * APP自动升级管理Action层
 * @author liuyj
 * 
 */
@Controller
@RequestMapping("/app/cm/wlCmAppVersionAction.do")
public class WlCmAppVersionAction extends BaseAction {
	/**
	 * 获取当前最新版本信息
	 * @param appType 终端类型（ANDROID-安卓，IOS-IOS）
	 * @return 版本对象
	 */
	@RequestMapping(params = "action=getLatestVersion")
	@ResponseBody
	public String getLatestVersion(String appType) {
		try {
			Map map = new HashMap();
			// 获取版本信息
			ResourceBundle config = ResourceBundle.getBundle("version");
			map.put("appName", new String(config.getString("appName").getBytes("ISO-8859-1"), "UTF-8"));
			map.put("versionCode", config.getString("versionCode"));
			map.put("versionName", config.getString("versionName"));
			map.put("apkUrl", config.getString("apkUrl"));
			map.put("changeLog", new String(config.getString("changeLog").getBytes("ISO-8859-1"), "UTF-8"));
			map.put("updateTips", new String(config.getString("updateTips").getBytes("ISO-8859-1"), "UTF-8"));
			return this.getJson(true, map);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}
