package com.gt.wl.wm.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joyone.action.BaseAction;
import org.joyone.lang.BaseException;
import org.joyone.spring.Sc;
import org.joyone.sys.Page;
import org.joyone.sys.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gt.wl.cm.model.WlCmItem;
import com.gt.wl.cm.service.WlCmItemService;
import com.gt.wl.cm.service.WlCmSecurityService;
import com.gt.wl.cm.service.WlCmTypeService;
import com.gt.wl.util.CommonConf;
import com.gt.wl.wm.service.WlWmInventoryService;

@Controller
@RequestMapping("/wl/wm/WlWmSafeInventoryQueryAction.agency")
public class WlWmSafeInventoryQueryAction extends BaseAction {
	private WlWmInventoryService wlWmInventoryService = (WlWmInventoryService) Sc.getBean("wl.wm.WlWmInventoryService");
	private WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");
	private WlCmItemService wlCmItemService = (WlCmItemService) Sc.getBean("wl.cm.WlCmItemService");

	private final static String MODULE_CODE = "0306003";// 模块代码

	// private final static String SYSTEM_NAME = Lang.getString("wl.cm.SYSTEM_NAME");

	// private final static String MODULE_NAME = Lang.getString("wl.cm.wlCmStorageAction.MODULE_NAME");

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
			return this.getJson(true, mapResult);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 搜索库存物料
	 * @param pageSize 页大小
	 * @param currPage 当前页数
	 * @param paraMap 条件
	 * @return 物料列表
	 */
	@RequestMapping(params = "action=search")
	@ResponseBody
	public String search(int pageSize, int currPage, String content) {
		try {
			Map map = new HashMap();
			map.put("content", content);
			map.put("storageId", CommonConf.storageId);// 翔安仓库id
			map.put("isShowAgency", "1");
			Page page = wlWmInventoryService.searchInventory(currPage, pageSize, map);
			List<Map> itemList = page.getItems();
			for (Map itemMap : itemList) {
				double baseUnitQty = (Double) itemMap.get("baseUnitQty");
				String itemCd = (String) itemMap.get("itemCd");
				WlCmItem wlCmItem = wlCmItemService.getItemByItemCd(itemCd);
				baseUnitQty = baseUnitQty - wlCmItem.getLowerLimit();
				if (baseUnitQty < 0) {
					baseUnitQty = 0;
				}
				itemMap.put("baseUnitQty", baseUnitQty);
			}
			return this.getJson(true, page);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}