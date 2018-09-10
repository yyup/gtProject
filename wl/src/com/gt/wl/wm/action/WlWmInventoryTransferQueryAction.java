package com.gt.wl.wm.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
import com.gt.wl.wm.service.WlWmStoreInService;
import com.gt.wl.wm.service.WlWmStoreOutService;

@Controller
@RequestMapping("/wl/wm/WlWmInventoryTransferQueryAction.do")
public class WlWmInventoryTransferQueryAction extends BaseAction {
	private WlCmStorageService wlCmStorageService = (WlCmStorageService) Sc.getBean("wl.cm.WlCmStorageService");
	// private WlCmTypeService wlCmTypeService = (WlCmTypeService) Sc.getBean("wl.cm.WlCmTypeService");
	private WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");
	private WlWmStoreOutService wlWmStoreOutService = (WlWmStoreOutService) Sc.getBean("wl.wm.WlWmStoreOutService");
	private WlWmStoreInService wlWmStoreInService = (WlWmStoreInService) Sc.getBean("wl.wm.WlWmStoreInService");

	private final static String MODULE_CODE = "0305012";// 模块代码
	private final static String SYSTEM_NAME = Lang.getString("wl.wm.SYSTEM_NAME");

	// private final static String MODULE_NAME = Lang.getString("wl.wm.WlWmInventoryTransferQueryAction.MODULE_NAME");// Todo

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
			// WlCmTypeService wlCmTypeService = (WlCmTypeService) Sc.getBean("wl.cm.WlCmTypeService");
			// mapResult.putAll(wlCmTypeService.getEnumType(enumTypes));// 到字典表获取枚举数据
			mapResult.put("allStorageEnum", wlCmStorageService.getObjects());
			return this.getJson(true, mapResult);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/***
	 * 查询库存结转
	 * @param paraMap 条件
	 * @return 返回库存结存列表
	 */
	@RequestMapping(params = "action=findDataList")
	@ResponseBody
	public String findDataList(ParaMap paraMap) {
		try {
			List<Map> resultList = new ArrayList<Map>();// 存放结果
			Map<String, String> resultMap = new HashMap<String, String>();
			Map map = paraMap.getMap();// 查询条件
			TreeMap<String, String> dateMap = new TreeMap<String, String>();// 存放取得的日期列表
			List<Map> inDateList = wlWmStoreInService.findInDate(map);// 取得入库日期列表
			List<Map> outDateList = wlWmStoreOutService.findOutDate(map);// 取得出库日期列表
			// 将出入库日期存入dateMap，并去重复
			for (Map inMap : inDateList) {
				String inDate = inMap.get("inDate").toString();
				dateMap.put(inDate, inDate);
			}
			for (Map outMap : outDateList) {
				String outDate = outMap.get("outDate").toString();
				dateMap.put(outDate, outDate);
			}
			// 取得指定月份前的入库数量
			double inQty = wlWmStoreInService.getInQty(map);// 取得指定月份前的入库总数量
			double outQty = wlWmStoreOutService.getOutQty(map);// 取得指定月份前的出库总数量
			double balance = inQty - outQty;// 取得初始化结转数量
			map.remove("date");// 移除date
			for (String day : dateMap.values()) {
				map.put("day", day);//
				inQty = wlWmStoreInService.getInQty(map);// 取得指定天（年月日）入库总数量
				outQty = wlWmStoreOutService.getOutQty(map);// 取得指定天（年月日）出库总数量
				// 构造返回数据
				resultMap = new HashMap<String, String>();
				resultMap.put("date", day);
				resultMap.put("balance", balance + "");
				resultMap.put("inQty", inQty + "");
				resultMap.put("outQty", outQty + "");
				// 重新计算下一条数据的结转数量
				balance = balance + inQty - outQty;
				resultMap.put("finalInventory", balance + "");
				resultList.add(resultMap);
			}
			return this.getJson(true, resultList);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}