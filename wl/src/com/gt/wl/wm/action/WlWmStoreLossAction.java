package com.gt.wl.wm.action;

import java.util.Date;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gt.wl.cm.service.WlCmSecurityService;
import com.gt.wl.cm.service.WlCmTypeService;
import com.gt.wl.wm.model.WlWmStoreCheck;
import com.gt.wl.wm.model.WlWmStoreLoss;
import com.gt.wl.wm.model.WlWmStoreLossDetl;
import com.gt.wl.wm.service.WlWmStoreCheckService;
import com.gt.wl.wm.service.WlWmStoreLossService;

@Controller
@RequestMapping("/wl/wm/wlWmStoreLossAction.do")
public class WlWmStoreLossAction extends BaseAction {
	private WlWmStoreLossService wlWmStoreLossService = (WlWmStoreLossService) Sc.getBean("wl.wm.WlWmStoreLossService");
	private WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");
	private WlWmStoreCheckService wlWmStoreCheckService = (WlWmStoreCheckService) Sc.getBean("wl.wm.WlWmStoreCheckService");
	private final static String MODULE_CODE = "0305007";// 模块代码
	private final static String SYSTEM_NAME = Lang.getString("wl.wm.SYSTEM_NAME");
	private final static String MODULE_NAME = Lang.getString("wl.wm.WlWmStoreLossAction.MODULE_NAME");

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
	 * 查询损益单(分页)
	 * @param paraMap 前端参数
	 * @param currPage 当前页
	 * @param pageSize 页数
	 * @return 损益单分页对象
	 */
	@RequestMapping(params = "action=findDataList")
	@ResponseBody
	public String findDataList(ParaMap paraMap, int currPage, int pageSize) {
		try {
			Map pMap = paraMap.getMap();
			Page page = wlWmStoreLossService.search(currPage, pageSize, pMap);
			List<WlWmStoreLoss> list = page.getItems();
			double overage = 0;// 盘盈
			double shortage = 0;// 盘亏
			// 组合所用物料的名字，格式为"划船器等6种物料",计算盘亏，计算盘盈
			for (WlWmStoreLoss wlWmStoreLoss : list) {
				WlWmStoreCheck wlWmStoreCheck = (WlWmStoreCheck) wlWmStoreCheckService.getObject(wlWmStoreLoss.getLinkedMasId());
				wlWmStoreLoss.setCheckDate(wlWmStoreCheck.getCheckDate());// 盘点日期
				wlWmStoreLoss.setAllItemName(wlWmStoreLossService.getAllItemName(wlWmStoreLoss.getStoreLossId(), 0));
				List<WlWmStoreLossDetl> wlWmStoreLossDetlList = wlWmStoreLossService.findStoreLossDetlList(wlWmStoreLoss.getStoreLossId());
				overage = 0;
				shortage = 0;
				for (WlWmStoreLossDetl wlWmStoreLossDetl : wlWmStoreLossDetlList) {
					if (wlWmStoreLossDetl.getCheckBaseQty() > wlWmStoreLossDetl.getBookBaseQty()) {// 盘盈
						overage += (wlWmStoreLossDetl.getCheckBaseQty() - wlWmStoreLossDetl.getBookBaseQty());
					}
					else if (wlWmStoreLossDetl.getCheckBaseQty() < wlWmStoreLossDetl.getBookBaseQty()) {// 盘亏
						shortage += (wlWmStoreLossDetl.getBookBaseQty() - wlWmStoreLossDetl.getCheckBaseQty());
					}
				}
				wlWmStoreLoss.setOverage(overage);
				wlWmStoreLoss.setShortage(shortage);
			}
			return this.getJson(true, page);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 将损益单审核状态更新成指定的状态
	 * @param storeLossId 盈亏单id
	 * @param auditState 要更新成的状态
	 * @param user 当前登录用户
	 * @return 空
	 */
	@RequestMapping(params = "action=updateAuditState")
	@ResponseBody
	public String updateAuditState(String storeLossId, String auditState, User user) {
		try {
			WlWmStoreLoss wlWmStoreLoss = (WlWmStoreLoss) wlWmStoreLossService.getObject(storeLossId);
			wlWmStoreLoss.setAuditTime(new Date());
			wlWmStoreLoss.setAuditState(auditState);
			wlWmStoreLoss.setAuditor(user.getName());
			if ("2".equals(auditState)) {// 驳回
				wlWmStoreLossService.updateObject(wlWmStoreLoss);
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.wm.WlWmStoreLossAction.deny"), wlWmStoreLoss.getBillNo());

			}
			else if ("1".equals(auditState)) {// 审核通过
				wlWmStoreLossService.saveStoreInOrStoreOut(wlWmStoreLoss, user);
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.wm.WlWmStoreLossAction.pass"), wlWmStoreLoss.getBillNo());

			}

			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取损益单和损益单从表列表
	 * @param storeLossId 盈亏单id
	 * @return 损益单和损益单从表列表
	 */
	@RequestMapping(params = "action=getData")
	@ResponseBody
	public String getData(String storeLossId) {
		try {
			// 获取损益单对象
			WlWmStoreLoss wlWmStoreLoss = (WlWmStoreLoss) wlWmStoreLossService.getObject(storeLossId);
			WlWmStoreCheck wlWmStoreCheck = (WlWmStoreCheck) wlWmStoreCheckService.getObject(wlWmStoreLoss.getLinkedMasId());
			wlWmStoreLoss.setCheckDate(wlWmStoreCheck.getCheckDate());
			List<WlWmStoreLossDetl> wlWmStoreLossDetlList = wlWmStoreLossService.findStoreLossDetlList(wlWmStoreLoss.getStoreLossId());
			for (WlWmStoreLossDetl wlWmStoreLossDetl : wlWmStoreLossDetlList) {
				if (wlWmStoreLossDetl.getCheckBaseQty() == wlWmStoreLossDetl.getBookBaseQty()) {// 正常
					wlWmStoreLossDetl.setOverage(0);
					wlWmStoreLossDetl.setShortage(0);
				}
				else if (wlWmStoreLossDetl.getCheckBaseQty() > wlWmStoreLossDetl.getBookBaseQty()) {// 盘盈
					wlWmStoreLossDetl.setOverage(wlWmStoreLossDetl.getCheckBaseQty() - wlWmStoreLossDetl.getBookBaseQty());
					wlWmStoreLossDetl.setShortage(0);
				}
				else if (wlWmStoreLossDetl.getCheckBaseQty() < wlWmStoreLossDetl.getBookBaseQty()) {// 盘亏
					wlWmStoreLossDetl.setOverage(0);
					wlWmStoreLossDetl.setShortage(wlWmStoreLossDetl.getBookBaseQty() - wlWmStoreLossDetl.getCheckBaseQty());
				}
			}
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("storeLoss", wlWmStoreLoss);
			resultMap.put("storeLossDetlList", wlWmStoreLossDetlList);
			return this.getJson(true, resultMap);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}
}