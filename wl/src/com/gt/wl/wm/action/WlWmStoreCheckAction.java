package com.gt.wl.wm.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joyone.action.BaseAction;
import org.joyone.action.ParaMap;
import org.joyone.lang.BaseException;
import org.joyone.spring.Sc;
import org.joyone.sys.Lang;
import org.joyone.sys.Page;
import org.joyone.sys.User;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gt.wl.cm.service.WlCmSecurityService;
import com.gt.wl.cm.service.WlCmStorageService;
import com.gt.wl.cm.service.WlCmTypeService;
import com.gt.wl.wm.model.WlWmStoreCheck;
import com.gt.wl.wm.model.WlWmStoreCheckDetl;
import com.gt.wl.wm.service.WlWmStoreCheckService;
import com.gt.wl.wm.service.WlWmStoreLossService;

@Controller
@RequestMapping("/wl/wm/wlWmStoreCheckAction.do")
public class WlWmStoreCheckAction extends BaseAction {
	private WlWmStoreCheckService wlWmStoreCheckService = (WlWmStoreCheckService) Sc.getBean("wl.wm.WlWmStoreCheckService");
	private WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");
	private WlWmStoreLossService wlWmStoreLossService = (WlWmStoreLossService) Sc.getBean("wl.wm.WlWmStoreLossService");
	private final static String MODULE_CODE = "0305006";// 模块代码
	private final static String SYSTEM_NAME = Lang.getString("wl.wm.SYSTEM_NAME");
	private final static String MODULE_NAME = Lang.getString("wl.wm.WlWmStoreCheckAction.MODULE_NAME");
	private WlCmStorageService wlCmStorageService = (WlCmStorageService) Sc.getBean("wl.cm.WlCmStorageService");

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
	 * 查询盘点单(分页)
	 * @param paraMap 前端参数
	 * @param currPage 当前页
	 * @param pageSize 页数
	 * @return 盘点单
	 */
	@RequestMapping(params = "action=findDataList")
	@ResponseBody
	public String findDataList(ParaMap paraMap, int currPage, int pageSize) {
		try {
			Map pMap = paraMap.getMap();
			Page page = wlWmStoreCheckService.search(currPage, pageSize, pMap);
			List<WlWmStoreCheck> list = page.getItems();
			// 组合所用物料的名字，格式为"划船器等6种物料"
			for (WlWmStoreCheck wlWmStoreCheck : list) {
				wlWmStoreCheck.setAllItemName(wlWmStoreCheckService.getAllItemName(wlWmStoreCheck.getStoreCheckId(), 0));
			}
			return this.getJson(true, page);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 更改盘点单单据性质
	 * @param storeCheckId 盘点单id
	 * @param user 当前登录用户
	 * @return 操作结果
	 */
	@RequestMapping(params = "action=updateLockFlagState")
	@ResponseBody
	public String updateLockFlagState(String storeCheckId, User user) {
		try {
			// 获取盘点单对象
			WlWmStoreCheck wlWmStoreCheck = (WlWmStoreCheck) wlWmStoreCheckService.getObject(storeCheckId);
			// 修改使用状态（已锁-LOCK,未锁-NOLOCK)
			if ("LOCK".equals(wlWmStoreCheck.getLockFlagEk())) {
				wlWmStoreCheck.setLockFlagEk("NOLOCK");
				wlWmStoreCheckService.updateObject(wlWmStoreCheck);
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.wm.WlWmStoreCheckAction.noLock"), wlWmStoreCheck.getBillNo() + Lang.getString("wl.wm.WlWmStoreCheckAction.noLock"));
			}
			else if ("NOLOCK".equals(wlWmStoreCheck.getLockFlagEk())) {
				wlWmStoreCheck.setLockFlagEk("LOCK");
				List list = wlWmStoreCheckService.findStoreCheckDetlList(null, storeCheckId, "1");
				if (ValidateUtil.isEmpty(list)) {// 如果不存在损益
					wlWmStoreCheckService.updateObject(wlWmStoreCheck);
				}
				else {// 如果存在损益
					wlWmStoreLossService.saveStoreLoss(wlWmStoreCheck, list, user);
				}
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.wm.WlWmStoreCheckAction.lock"), wlWmStoreCheck.getBillNo() + Lang.getString("wl.wm.WlWmStoreCheckAction.lock"));
			}

			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取盘点单和盘点单对应的从表数据
	 * @param storeCheckId 盘点单id
	 * @return 盘点单和盘点单对应的从表数据
	 */
	@RequestMapping(params = "action=getData")
	@ResponseBody
	public String getData(String storeCheckId) {
		try {
			// 获取盘点单对象
			WlWmStoreCheck wlWmStoreCheck = (WlWmStoreCheck) wlWmStoreCheckService.getObject(storeCheckId);
			List<WlWmStoreCheckDetl> list = wlWmStoreCheckService.findStoreCheckDetlList(null, storeCheckId, null);
			for (WlWmStoreCheckDetl wlWmStoreCheckDetl : list) {
				wlWmStoreCheckDetl.setProfitOrLoss(wlWmStoreCheckDetl.getCheckBaseQty() - wlWmStoreCheckDetl.getBookBaseQty());// 盈亏数量
			}
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("StoreCheck", wlWmStoreCheck);
			resultMap.put("StoreCheckDetlList", list);
			return this.getJson(true, resultMap);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询是否有损益
	 * @param storeCheckId 盘点单据ID
	 * @return 损益返回1，无损益返回空
	 */
	@RequestMapping(params = "action=getIsProfitAndLoss")
	@ResponseBody
	public String getIsProfitAndLoss(String storeCheckId) {
		try {
			List list = wlWmStoreCheckService.findStoreCheckDetlList(null, storeCheckId, "1");
			if (ValidateUtil.isEmpty(list)) {
				return this.getJson(true, "");
			}
			else {
				return this.getJson(true, "1");
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}