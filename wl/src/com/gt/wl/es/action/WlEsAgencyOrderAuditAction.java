package com.gt.wl.es.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joyone.action.BaseAction;
import org.joyone.action.ParaMap;
import org.joyone.lang.BaseException;
import org.joyone.model.EnumModel;
import org.joyone.spring.Sc;
import org.joyone.sys.Lang;
import org.joyone.sys.Page;
import org.joyone.sys.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gt.wl.cm.model.WlCmStorage;
import com.gt.wl.cm.service.WlCmItemService;
import com.gt.wl.cm.service.WlCmOrgService;
import com.gt.wl.cm.service.WlCmSecurityService;
import com.gt.wl.cm.service.WlCmStorageService;
import com.gt.wl.cm.service.WlCmTypeService;
import com.gt.wl.es.model.WlEsAgencyOrder;
import com.gt.wl.es.model.WlEsAgencyOrderDetl;
import com.gt.wl.es.service.WlEsAgencyOrderService;
import com.gt.wl.wm.service.WlWmPushService;

/**
 * 经销商订单Actionchen层
 * @author liuyj
 * 
 */
@Controller
@RequestMapping("/wl/es/WlEsAgencyOrderAuditAction.do")
public class WlEsAgencyOrderAuditAction extends BaseAction {
	private WlEsAgencyOrderService wlEsAgencyOrderService = (WlEsAgencyOrderService) Sc.getBean("wl.es.WlEsAgencyOrderService");
	private WlCmItemService wlCmItemService = (WlCmItemService) Sc.getBean("wl.cm.WlCmItemService");
	private WlCmTypeService wlCmTypeService = (WlCmTypeService) Sc.getBean("wl.cm.WlCmTypeService");
	private WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");
	private WlCmOrgService wlCmOrgService = (WlCmOrgService) Sc.getBean("wl.cm.WlCmOrgService");
	private WlCmStorageService wlCmStorageService = (WlCmStorageService) Sc.getBean("wl.cm.WlCmStorageService");
	private WlWmPushService wlWmPushService = (WlWmPushService) Sc.getBean("wl.wm.WlWmPushService");

	private final static String MODULE_CODE = "0302007";// 模块代码
	private final static String SYSTEM_NAME = Lang.getString("wl.es.SYSTEM_NAME");
	private final static String MODULE_NAME = Lang.getString("wl.es.WlEsAgencyOrderAuditAction.MODULE_NAME");

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
			Map map = wlCmTypeService.getEnumType(enumTypes);
			// 去掉草稿
			List<EnumModel> list = (List<EnumModel>) map.get("AGENCY_ORDER_STATE_EKEnum");
			List<EnumModel> tempList = new ArrayList();
			for (EnumModel enumModel : list) {
				if (!"N".equals(enumModel.getValue())) {
					tempList.add(enumModel);
				}
			}
			map.put("AGENCY_ORDER_STATE_EKEnum", tempList);
			mapResult.putAll(map);// 到字典表获取枚举数据
			mapResult.put("enAbleStorageEnum", wlCmStorageService.findStorageList());
			WlCmStorage outStorage = wlCmStorageService.getDefaultOutStorage("");
			mapResult.put("defaultOutStorageId", outStorage.getStorageId());
			return this.getJson(true, mapResult);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询经销商订单（分页）
	 * @param paraMap 条件
	 * @param currPage 当前页
	 * @param pageSize 页数
	 * @return 分页结果
	 */
	@RequestMapping(params = "action=findDataList")
	@ResponseBody
	public String findDataList(ParaMap paraMap, int currPage, int pageSize) {
		try {
			Map pMap = paraMap.getMap();
			Page page = wlEsAgencyOrderService.search(currPage, pageSize, pMap);
			List<WlEsAgencyOrder> list = page.getItems();
			// 组合所用物料的名字，格式为"划船器等6种物料"
			for (WlEsAgencyOrder wlEsAgencyOrder : list) {
				wlEsAgencyOrder.setAllItemName(wlEsAgencyOrderService.getAllItemName(wlEsAgencyOrder.getOrderId()));
			}
			return this.getJson(true, page);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 获取经销商订单和经销商订单从表
	 * @param orderId 经销商订单id
	 * @param user 当前登录用户
	 * @return 经销商订单和经销商订单从表
	 */
	@RequestMapping(params = "action=getData")
	@ResponseBody
	public String getData(String orderId, User user) {
		try {
			WlEsAgencyOrder wlEsAgencyOrder = (WlEsAgencyOrder) wlEsAgencyOrderService.getObject(orderId);
			List<WlEsAgencyOrderDetl> agencyOrderDetlList = wlEsAgencyOrderService.findAgencyOrderDetlList(orderId);
			// 经销商订单
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("wlEsAgencyOrder", wlEsAgencyOrder);
			// 订单明细
			resultMap.put("agencyOrderDetlList", agencyOrderDetlList);
			// 订单审核结果
			resultMap.put("agencyOrderAuditList", wlEsAgencyOrderService.findAgencyOrderAuditList(orderId));
			return this.getJson(true, resultMap);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 更改订单状态为驳回
	 * @param orderId 经销商订单id
	 * @param auditResult 审核结论
	 * @param user 当前登录用户
	 * @return 空
	 */
	@RequestMapping(params = "action=updateOrderToReJect")
	@ResponseBody
	public String updateOrderToReJect(String orderId, String auditResult, User user) {
		try {
			wlEsAgencyOrderService.updateOrderToReJect(orderId, "2", auditResult, user);
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 更改订单状态为审核通过
	 * @param orderId 经销商订单id
	 * @param storageId 仓库id
	 * @param expectOutDate 预计出库日期
	 * @param user 当前登录用户
	 * @return 空
	 */
	@RequestMapping(params = "action=updateOrderToAudit")
	@ResponseBody
	public String updateOrderToAudit(String orderId, String storageId, Date expectOutDate, User user) {
		try {
			wlEsAgencyOrderService.updateOrderToAudit(orderId, "1", storageId, expectOutDate, user);
			try {
				wlWmPushService.getMessage(2);
			}
			catch (Exception e) {
				return this.getJson(true, "");
			}
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 保存发货单号
	 * @param orderId 发货id
	 * @param deliveryNo 发货单号
	 * @param logisticEk 物料
	 * @param user 当前登录用户
	 * @return 空
	 */
	@RequestMapping(params = "action=saveDeliveryData")
	@ResponseBody
	public String saveDeliveryData(String orderId, String deliveryNo, String logisticEk, User user) {
		try {
			WlEsAgencyOrder wlEsAgencyOrder = (WlEsAgencyOrder) wlEsAgencyOrderService.getObject(orderId);
			wlEsAgencyOrder.setDeliveryNo(deliveryNo);
			wlEsAgencyOrder.setDeliveryTime(new Date());
			wlEsAgencyOrder.setCorpUser(user.getName());
			wlEsAgencyOrder.setCorpUserId(user.getId());
			wlEsAgencyOrder.setLogisticEk(logisticEk);
			wlEsAgencyOrderService.saveObject(wlEsAgencyOrder);
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}