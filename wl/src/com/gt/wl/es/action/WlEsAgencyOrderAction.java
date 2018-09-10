package com.gt.wl.es.action;

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
import org.joyone.util.JsonUtil;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gt.wl.cm.model.WlCmItem;
import com.gt.wl.cm.model.WlCmOrg;
import com.gt.wl.cm.model.WlCmOrgUser;
import com.gt.wl.cm.service.WlCmCategoryService;
import com.gt.wl.cm.service.WlCmItemService;
import com.gt.wl.cm.service.WlCmOrgService;
import com.gt.wl.cm.service.WlCmOrgUserService;
import com.gt.wl.cm.service.WlCmSecurityService;
import com.gt.wl.cm.service.WlCmTypeService;
import com.gt.wl.es.model.WlEsAgencyOrder;
import com.gt.wl.es.model.WlEsAgencyOrderDetl;
import com.gt.wl.es.service.WlEsAgencyOrderService;

/**
 * 经销商订单Actionchen层
 * @author liuyj
 * 
 */
@Controller
@RequestMapping("/wl/es/wlEsAgencyOrderAction.agency")
public class WlEsAgencyOrderAction extends BaseAction {
	private WlEsAgencyOrderService wlEsAgencyOrderService = (WlEsAgencyOrderService) Sc.getBean("wl.es.WlEsAgencyOrderService");
	private WlCmItemService wlCmItemService = (WlCmItemService) Sc.getBean("wl.cm.WlCmItemService");
	private WlCmTypeService wlCmTypeService = (WlCmTypeService) Sc.getBean("wl.cm.WlCmTypeService");
	private WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");
	private WlCmOrgService wlCmOrgService = (WlCmOrgService) Sc.getBean("wl.cm.WlCmOrgService");
	private WlCmCategoryService wlCmCategoryService = (WlCmCategoryService) Sc.getBean("wl.cm.WlCmCategoryService");
	private WlCmOrgUserService wlCmOrgUserService = (WlCmOrgUserService) Sc.getBean("wl.cm.WlCmOrgUserService");
	private final static String MODULE_CODE = "0306002";// 模块代码
	private final static String SYSTEM_NAME = Lang.getString("wl.es.SYSTEM_NAME");
	private final static String MODULE_NAME = Lang.getString("wl.es.WlEsAgencyOrderAction.MODULE_NAME");

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
			WlCmOrgUser wlCmOrgUser = (WlCmOrgUser) wlCmOrgUserService.getObject(user.getId());
			// WlCmOrg wlCmOrg = (WlCmOrg) wlCmOrgService.getObject(wlCmOrgUser.getOrgId());
			mapResult.put("contact", wlCmOrgUser.getContact());
			mapResult.put("mobile", wlCmOrgUser.getMobile());
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
	 * @param user 当前登录用户
	 * @return 分页结果
	 */
	@RequestMapping(params = "action=findDataList")
	@ResponseBody
	public String findDataList(ParaMap paraMap, int currPage, int pageSize, User user) {
		try {
			Map pMap = paraMap.getMap();
			pMap.put("userId", user.getId());
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
	 * 查询物料管理，获取翻页数据
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param categoryId 物料类别id
	 * @return 结果
	 */
	@RequestMapping(params = "action=findItemDataList")
	@ResponseBody
	public String findItemDataList(int pageSize, int currPage, String categoryId) {
		try {
			Page page = wlCmItemService.search(pageSize, currPage, categoryId, "1","","","","","");
			/*
			 * List list = page.getItems();
			 * List resultList = new ArrayList();
			 * for (int i = 0; i < list.size(); i++) {
			 * Object[] object = (Object[]) list.get(i);
			 * Map map = new HashMap();
			 * map.put("itemId", object[0]);
			 * map.put("itemName", object[1]);
			 * map.put("itemCd", object[2]);
			 * map.put("spec", object[3]);
			 * map.put("baseUnitName", object[4]);
			 * // map.put("baseUnitQty", object[5]);
			 * resultList.add(map);
			 * }
			 * Page tempPage = new Page(resultList, page.getTotalCount());
			 */
			return this.getJson(true, page);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取物料管理对象
	 * @param itemIds 物料管理ids
	 * @param itemQtys 跟itemIds一一对应，对应的当前已添加的数量
	 * @return 物料管理对象列表
	 */
	@RequestMapping(params = "action=findItemList")
	@ResponseBody
	public String findItemList(String[] itemIds, double[] itemQtys) {
		try {
			int i = 0;
			for (String itemId : itemIds) {
				double canUseQty = wlEsAgencyOrderService.getCanUseInventoryQty(itemId);
				if (itemQtys[i] > canUseQty) {// 添加的数量超过可用数量
					WlCmItem wlCmItem = (WlCmItem) wlCmItemService.getObject(itemId);
					String spec = ValidateUtil.isEmpty(wlCmItem.getSpec()) ? wlCmItem.getSpec() : "-" + wlCmItem.getSpec();
					throw new BaseException("订单保存失败(" + wlCmItem.getItemName() + spec + "库存不足）");
				}
				i++;
			}
			List<Map> list = wlCmItemService.findItemList(itemIds, null);
			return this.getJson(true, list);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/***
	 * 新增/编辑经销商订单
	 * @param wlEsAgencyOrder 经销商订单对象
	 * @param wlEsAgencyOrderDetls 经销商订单从表对象数组
	 * @param user 当前登录用户
	 * @return 空
	 */
	@RequestMapping(params = "action=saveData")
	@ResponseBody
	public String saveData(WlEsAgencyOrder wlEsAgencyOrder, String wlEsAgencyOrderDetls, User user) {
		try {
			boolean isNew = false;// 是否新增
			String memo = wlEsAgencyOrder.getMemo();
			if (!ValidateUtil.isEmpty(memo)) {
				memo = memo.replaceAll("\\t", "");
				wlEsAgencyOrder.setMemo(memo);
			}
			if (ValidateUtil.isEmpty(wlEsAgencyOrder.getOrderId())) {// 新增
				isNew = true;
			}
			List<WlEsAgencyOrderDetl> wlWmNoticeInDetlList = JsonUtil.toList(wlEsAgencyOrderDetls, WlEsAgencyOrderDetl.class);
			wlEsAgencyOrderService.saveData(wlEsAgencyOrder, wlWmNoticeInDetlList, user);
			if (isNew) {
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.add"), Lang.getString("wl.es.WlEsAgencyOrderAction.orderNo") + wlEsAgencyOrder.getOrderNo());

			}
			else {
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.add"), Lang.getString("wl.es.WlEsAgencyOrderAction.orderNo") + wlEsAgencyOrder.getOrderNo());

			}
			return this.getJson(true, "");

		}
		catch (Exception e) {
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

			// 当前登录用户的往来单位联系方式和联系人
			WlCmOrg wlCmOrg = (WlCmOrg) wlCmOrgService.getObject(user.getCorpId());
			resultMap.put("contactWay", "");
			resultMap.put("contact", "");
			if (!ValidateUtil.isNull(wlCmOrg)) {
				resultMap.put("contact", wlCmOrg.getContact());
				if (!ValidateUtil.isEmpty(wlCmOrg.getMobile())) {
					resultMap.put("contactWay", wlCmOrg.getMobile());
				}
				else if (!ValidateUtil.isEmpty(wlCmOrg.getTel())) {
					resultMap.put("contactWay", wlCmOrg.getTel());
				}
			}
			return this.getJson(true, resultMap);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询经销商单位的联系人和联系方式
	 * @param user 当前登录用户
	 * @return 经销商单位的联系人和联系方式
	 */
	@RequestMapping(params = "action=getOrgContactAndContactWay")
	@ResponseBody
	public String getOrgContactAndContactWay(User user) {
		try {
			Map<String, String> resultMap = new HashMap<String, String>();
			WlCmOrg wlCmOrg = (WlCmOrg) wlCmOrgService.getObject(user.getCorpId());

			resultMap.put("contactWay", "");
			resultMap.put("contact", "");
			if (!ValidateUtil.isNull(wlCmOrg)) {
				resultMap.put("contact", wlCmOrg.getContact());
				if (!ValidateUtil.isEmpty(wlCmOrg.getMobile())) {
					resultMap.put("contactWay", wlCmOrg.getMobile());
				}
				else if (!ValidateUtil.isEmpty(wlCmOrg.getTel())) {
					resultMap.put("contactWay", wlCmOrg.getTel());
				}
			}
			return this.getJson(true, resultMap);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 更改订单状态为审核中
	 * @param orderId 经销商订单id
	 * @return 空
	 */
	@RequestMapping(params = "action=updateOrderState")
	@ResponseBody
	public String updateOrderState(String orderId) {
		try {
			List<WlEsAgencyOrderDetl> list = wlEsAgencyOrderService.findAgencyOrderDetlList(orderId);
			Map<String, Double> itemQtyMap = new HashMap<String, Double>();// key是itemId，value是此物料下单的的总数量
			for (WlEsAgencyOrderDetl wlEsAgencyOrderDetl : list) {
				double canUseQty = wlEsAgencyOrderService.getCanUseInventoryQty(wlEsAgencyOrderDetl.getItemId());
				if (itemQtyMap.containsKey(wlEsAgencyOrderDetl.getItemId())) {// 包含
					itemQtyMap.put(wlEsAgencyOrderDetl.getItemId(), wlEsAgencyOrderDetl.getBaseUnitQty() + itemQtyMap.get(wlEsAgencyOrderDetl.getItemId()));
				}
				else {
					itemQtyMap.put(wlEsAgencyOrderDetl.getItemId(), wlEsAgencyOrderDetl.getBaseUnitQty());
				}
				if (itemQtyMap.get(wlEsAgencyOrderDetl.getItemId()) > canUseQty) {// 如果下单的数量大于可用数量，则停止
					String spec = ValidateUtil.isEmpty(wlEsAgencyOrderDetl.getSpec()) ? wlEsAgencyOrderDetl.getSpec() : "-" + wlEsAgencyOrderDetl.getSpec();
					throw new BaseException("订单保存失败(" + wlEsAgencyOrderDetl.getItemName() + spec + "库存不足）");
				}
			}

			WlEsAgencyOrder wlEsAgencyOrder = (WlEsAgencyOrder) wlEsAgencyOrderService.getObject(orderId);
			wlEsAgencyOrder.setAgencyOrderStateEk("0");
			wlEsAgencyOrderService.saveObject(wlEsAgencyOrder);
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 删除经销商订单
	 * @param orderIds 经销商id数组
	 * @return 异常信息或空
	 */
	@RequestMapping(params = "action=deleteData")
	@ResponseBody
	public String deleteData(String[] orderIds) {
		try {
			List list = wlEsAgencyOrderService.findOrderList(orderIds, "N");
			if (!ValidateUtil.isEmpty(list)) {
				return this.getJson(true, Lang.getString("wl.es.WlEsAgencyOrderAction.existNonDraft"));
			}
			else {
				wlEsAgencyOrderService.deleteOrderByOrderId(orderIds);
				return this.getJson(true, "");
			}

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 保存上传数据
	 * @param path 上传文件ID
	 * @param user 当前登录用户
	 * @return 结果
	 */
	@RequestMapping(params = "action=saveExcelData")
	@ResponseBody
	public String saveExcelData(String path, User user) {
		try {
			wlEsAgencyOrderService.saveExcelData(path, user);
			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取所有的栏目类型树
	 * @param user 当前用户
	 * @param isEnableFlag 栏目的使用状态
	 * @param rootId 跟节点
	 * @param isLastFlag 是否末级节点
	 * @return 商品类型树形json
	 */
	@RequestMapping(params = "action=getTree")
	@ResponseBody
	public String getTree(User user, String isEnableFlag, String rootId, String isLastFlag) {
		try {
			Map map = wlCmCategoryService.getTree(rootId, isEnableFlag, isLastFlag, "1","");
			return this.getJson(true, map);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 判断下单数量是否超过可用数量
	 * @param itemId 物料id
	 * @param itemQty 当前下单数量
	 * @return 查过返回提示语
	 */
	@RequestMapping(params = "action=getItemQtyQtCanUserQty")
	@ResponseBody
	public String getItemQtyQtCanUserQty(String itemId, double itemQty) {
		try {
			double canUseQty = wlEsAgencyOrderService.getCanUseInventoryQty(itemId);
			if (itemQty > canUseQty) {
				WlCmItem wlCmItem = (WlCmItem) wlCmItemService.getObject(itemId);
				String spec = ValidateUtil.isEmpty(wlCmItem.getSpec()) ? wlCmItem.getSpec() : "-" + wlCmItem.getSpec();
				// throw new BaseException("订单保存失败(" + wlCmItem.getItemName() + spec + "库存不足）");
				return this.getJson(true, wlCmItem.getItemName() + spec + "库存不足");
			}
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}