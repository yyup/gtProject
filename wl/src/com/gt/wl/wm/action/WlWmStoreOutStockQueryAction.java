package com.gt.wl.wm.action;

import java.util.List;
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

import com.gt.wl.cm.service.WlCmSecurityService;
import com.gt.wl.cm.service.WlCmStorageService;
import com.gt.wl.cm.service.WlCmTypeService;
import com.gt.wl.util.Type;
import com.gt.wl.wm.service.WlWmNoticeOutService;

@Controller
@RequestMapping("/wl/wm/WlWmStoreOutStockQueryAction.do")
public class WlWmStoreOutStockQueryAction extends BaseAction {
	private WlWmNoticeOutService wlWmNoticeOutService = (WlWmNoticeOutService) Sc.getBean("wl.wm.WlWmNoticeOutService");
	private WlCmTypeService wlCmTypeService = (WlCmTypeService) Sc.getBean("wl.cm.WlCmTypeService");
	private WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");
	private WlCmStorageService wlCmStorageService = (WlCmStorageService) Sc.getBean("wl.cm.WlCmStorageService");
	private final static String MODULE_CODE = "0305011";// 模块代码
	private final static String SYSTEM_NAME = Lang.getString("wl.wm.SYSTEM_NAME");

	// private final static String MODULE_NAME = Lang.getString("wl.wm.wlWmStoreInQueryAction.MODULE_NAME");

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
			return this.getJson(true, mapResult);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 备货数据
	 * @param paraMap 条件
	 * @return 备货数据
	 */
	@RequestMapping(params = "action=findStoreOutStockList")
	@ResponseBody
	public String findStoreOutStockList(ParaMap paraMap) {
		try {
			Map map = paraMap.getMap();
			List<Map> storeOutStockList = wlWmNoticeOutService.findStoreOutStockList(map);
			List<Type> wmOutTypeEkList = wlCmTypeService.findByCode("WM_OUT_TYPE_EK");
			int i = 1;
			for (Map storeOutStockMap : storeOutStockList) {
				storeOutStockMap.put("sequ", i++);
				for (Type type : wmOutTypeEkList) {
					if (type.getId().equals(storeOutStockMap.get("wmOutTypeEk").toString())) {
						storeOutStockMap.put("wmOutTypeEkValue", type.getLable());
						break;
					}
				}

			}
			return this.getJson(true, storeOutStockList);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}