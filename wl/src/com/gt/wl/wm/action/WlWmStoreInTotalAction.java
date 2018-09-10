package com.gt.wl.wm.action;

import java.util.HashMap;
import java.util.Map;

import org.joyone.action.BaseAction;
import org.joyone.action.ParaMap;
import org.joyone.lang.BaseException;
import org.joyone.spring.Sc;
import org.joyone.sys.Lang;
import org.joyone.sys.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gt.wl.cm.service.WlCmOrgService;
import com.gt.wl.cm.service.WlCmSecurityService;
import com.gt.wl.cm.service.WlCmStorageService;
import com.gt.wl.cm.service.WlCmTypeService;
import com.gt.wl.wm.service.WlWmStoreInService;

@Controller
@RequestMapping("/wl/wm/WlWmStoreInTotalAction.do")
public class WlWmStoreInTotalAction extends BaseAction {
	private WlCmStorageService wlCmStorageService = (WlCmStorageService) Sc.getBean("wl.cm.WlCmStorageService");
	private WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");
	private WlWmStoreInService wlWmStoreInService = (WlWmStoreInService) Sc.getBean("wl.wm.WlWmStoreInService");
	private WlCmOrgService wlCmOrgService = (WlCmOrgService) Sc.getBean("wl.cm.WlCmOrgService");

	private final static String MODULE_CODE = "0305013";// 模块代码
	private final static String SYSTEM_NAME = Lang.getString("wl.wm.SYSTEM_NAME");

	// private final static String MODULE_NAME = Lang.getString("wl.wm.wlWmNoticeInAction.MODULE_NAME");

	/**
	 * 界面初始化
	 * @param enumTypes 枚举类型
	 * @param user 当前登录用户
	 * @return 权限与枚举值
	 */
	@RequestMapping(params = "action=init")
	@ResponseBody
	public String init(String[] enumTypes, User user) {
		try {
			Map mapResult = wlCmSecurityService.getSmcMap(user, MODULE_CODE);// 根据模块代码获取权限
			WlCmTypeService wlCmTypeService = (WlCmTypeService) Sc.getBean("wl.cm.WlCmTypeService");
			mapResult.putAll(wlCmTypeService.getEnumType(enumTypes));// 到字典表获取枚举数据
			mapResult.put("allStorageEnum", wlCmStorageService.getObjects());
			mapResult.put("orgEnum", wlCmOrgService.getObjects());
			return this.getJson(true, mapResult);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/***
	 * 获取入库统计列表
	 * @param paraMap 条件
	 * @return 入库统计列表
	 */
	@RequestMapping(params = "action=findInDetailTotalList")
	@ResponseBody
	public String findInDetailTotalList(ParaMap paraMap) {
		try {
			Map map = paraMap.getMap();
			Map resultMap = new HashMap();
			resultMap.put("inTotalList", wlWmStoreInService.findInDetailTotalList(map));
			resultMap.put("inQty", wlWmStoreInService.getInQty(map));
			return this.getJson(true, resultMap);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}