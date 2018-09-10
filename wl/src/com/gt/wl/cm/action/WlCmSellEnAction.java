package com.gt.wl.cm.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.joyone.action.BaseAction;
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
import com.gt.wl.cm.model.WlCmProductTypeEn;
import com.gt.wl.cm.model.WlCmSellAllocationEn;
import com.gt.wl.cm.model.WlCmSellEn;
import com.gt.wl.cm.model.WlCmSellPicEn;
import com.gt.wl.cm.service.WlCmBizAuditLogService;
import com.gt.wl.cm.service.WlCmCategoryService;
import com.gt.wl.cm.service.WlCmItemService;
import com.gt.wl.cm.service.WlCmProductTypeEnService;
import com.gt.wl.cm.service.WlCmSecurityService;
import com.gt.wl.cm.service.WlCmSellEnService;
import com.gt.wl.cm.service.WlCmSellPicEnService;
import com.gt.wl.cm.service.WlCmTypeService;
import com.gt.wl.util.CommonConf;
import com.gt.wl.wm.model.WlWmInventory;
import com.gt.wl.wm.service.WlWmInventoryService;

/**
 * 产品上架
 * @author huangbj
 * 
 */
@Controller
@RequestMapping({ "/wl/cm/wlCmSellEnAction.do", "/wl/cm/wlCmSellEnAction.dox", "/wl/cm/wlCmSellEnAction.web" })
public class WlCmSellEnAction extends BaseAction {
	private WlCmSellEnService wlCmSellService = (WlCmSellEnService) Sc.getBean("wl.cm.WlCmSellEnService");
	private WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");
	private WlCmProductTypeEnService wlCmProductTypeService = (WlCmProductTypeEnService) Sc.getBean("wl.cm.WlCmProductTypeEnService");
	private WlCmSellPicEnService wlCmSellPicService = (WlCmSellPicEnService) Sc.getBean("wl.cm.WlCmSellPicEnService");
	private WlCmCategoryService wlCmCategoryService = (WlCmCategoryService) Sc.getBean("wl.cm.WlCmCategoryService");
	private WlCmItemService wlCmItemService = (WlCmItemService) Sc.getBean("wl.cm.WlCmItemService");
	private WlWmInventoryService wlWmInventoryService = (WlWmInventoryService) Sc.getBean("wl.wm.WlWmInventoryService");
	private WlCmBizAuditLogService wlCmBizAuditLogService = (WlCmBizAuditLogService) Sc.getBean("wl.cm.WlCmBizAuditLogService");

	private final static String MODULE_CODE = "0307004";// 模块代码
	private final static String SYSTEM_NAME = Lang.getString("wl.cm.SYSTEM_NAME");
	private final static String MODULE_NAME = Lang.getString("wl.cm.WlCmSellEnAction.MODULE_NAME");

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
	 * 获取所有的商品类型树
	 * @param user 当前用户
	 * @return 商品类型树形json
	 */
	@RequestMapping(params = "action=getTree")
	@ResponseBody
	public String getTree(User user) {
		try {
			Map map = wlCmProductTypeService.getTree("0");

			return this.getJson(true, map);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 保存单件商品上架信息
	 * @param user 登录用户
	 * @param sell 上架object对象
	 * @return 执行成功
	 */
	@RequestMapping(params = "action=saveSell")
	@ResponseBody
	public String saveSell(User user, WlCmSellEn sell) {
		try {
			String content = sell.getContent();
			if (!ValidateUtil.isEmpty(content)) {
				content = content.replaceAll("\\t", "");
				sell.setContent(content);
			}
			if (ValidateUtil.isEmpty(sell.getSellId())) {// 添加

				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.add"), sell.getProductName());
				sell.setSellStateEk("NO_SHELVE");
			}
			else {// 修改
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.edit"), sell.getProductName());
			}
			wlCmSellService.saveSell(sell, user);

			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 返回上架商品列表
	 * @param user 登录用户
	 * @param pageSize 每页条数
	 * @param currPage 当前页码
	 * @param sellStateEk 状态
	 * @param typeId 商品类型id
	 * @param productName 上架商品名称
	 * @param isThumFlag 是否缩略图
	 * @param sellIds 上架商品sellIds
	 * @return 上架商品列表
	 */
	@RequestMapping(params = "action=findSellsPages")
	@ResponseBody
	public String findSellsPages(User user, int pageSize, int currPage, String sellStateEk, String typeId, String productName, String isThumFlag, String[] sellIds) {
		try {
			Page page = wlCmSellService.findSellsPages(pageSize, currPage, sellStateEk, typeId, productName, isThumFlag, sellIds);
			return this.getJson(true, page);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取上架商品
	 * @param sellStateEk 状态
	 * @param typeId 商品类型id
	 * @param productName 商品名称
	 * @param isThumFlag 是否缩略图
	 * @param isIndexEk 是否首页显示
	 */
	@RequestMapping(params = "action=findSellList")
	@ResponseBody
	public String findSellList(String sellStateEk, String typeId, String productName, String isThumFlag,String isIndexEk) {
		try {
			List resultList = new ArrayList();// 用于存放结果上架商品
			wlCmSellService.findSellList(resultList, sellStateEk, typeId, productName, isThumFlag,isIndexEk);
			return this.getJson(true, resultList);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}
	
	/**
	 * 查询上架产品列表
	 * @param sellIds 上架产品Ids
	 * @return 上架产品列表
	 */
	@RequestMapping(params = "action=findSellListByIds")
	@ResponseBody
	public String findSellListByIds(String[] sellIds) {
		try {
			return this.getJson(true, wlCmSellService.findSellListByIds(sellIds));
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 返回上架商品列表
	 * @param user 登录用户
	 * @param pageSize 每页条数
	 * @param currPage 当前页码
	 * @param sellStateEk 状态
	 * @param typeId 商品类型id
	 * @param productName 上架商品名称
	 * @param isThumFlag 是否缩略图
	 * @param sellIds 上架商品sellIds
	 * @param filterSellIds 用于过滤的上架产品id
	 * @return 上架商品列表
	 */
	@RequestMapping(params = "action=searchSellPage")
	@ResponseBody
	public String searchSellPage(User user, int pageSize, int currPage, String sellStateEk, String typeId, String productName, String isThumFlag, String auditNodeEk, String auditState,String[] filterSellIds) {
		try {
			Page page = wlCmSellService.searchSellPage(pageSize, currPage, sellStateEk, typeId, productName, isThumFlag, auditNodeEk, auditState,filterSellIds);
			return this.getJson(true, page);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 返回上架商品
	 * @param user 登录用户
	 * @param sellId 上架商品id
	 * @return 上架商品列表
	 */
	@RequestMapping(params = "action=getData")
	@ResponseBody
	public String getData(User user, String sellId) {
		try {
			WlCmSellEn sell = (WlCmSellEn) wlCmSellService.getObject(sellId);
			WlCmProductTypeEn wlCmProductType = (WlCmProductTypeEn) wlCmProductTypeService.getObject(sell.getTypeId());
			sell.setTypeName(wlCmProductType.getTypeName());
			WlWmInventory wlWmInventory = wlWmInventoryService.getData(CommonConf.storageId, sell.getItemId());
			double baseUnitQty = 0;// 库存数量,默认0个
			if (!ValidateUtil.isNull(wlWmInventory)) {
				baseUnitQty = wlWmInventory.getBaseUnitQty();// 库存数量
			}
			double outStockNum = sell.getOutStockNum();// 缺货警示数量
			double lackStockNum = sell.getLackStockNum();// 库存紧张警示数量
			if (baseUnitQty <= outStockNum) {// 缺货
				sell.setInventoryState("1");
			}
			else if (baseUnitQty <= lackStockNum) {// 库存紧张
				sell.setInventoryState("2");
			}
			else {// 库存正常
				sell.setInventoryState("0");
			}
			return this.getJson(true, sell);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 返回上架商品
	 * @param sellId 上架商品id
	 * @return 上架商品列表
	 */
	@RequestMapping(params = "action=getSellDetail")
	@ResponseBody
	public String getSellDetail(String sellId) {
		try {
			WlCmSellEn sell = (WlCmSellEn) wlCmSellService.getObject(sellId);
			WlCmProductTypeEn wlCmProductType = (WlCmProductTypeEn) wlCmProductTypeService.getObject(sell.getTypeId());
			sell.setTypeName(wlCmProductType.getTypeName());
			/*
			 * // 库存状态
			 * String storageId = CommonConf.storageId;
			 * long outStockNum = sell.getOutStockNum();
			 * long lackStockNum = sell.getLackStockNum();
			 * WlWmInventory wlWmInventory = wlWmInventoryService.getData(storageId, sell.getSellId());
			 * double baseUnitQty = 0;// 库存数量，默认为0个
			 * if (!ValidateUtil.isNull(wlWmInventory)) {// 库存存在
			 * baseUnitQty = wlWmInventory.getBaseUnitQty();
			 * }
			 * if (baseUnitQty <= outStockNum) {// 缺货
			 * sell.setInventoryState("暂无现货");
			 * }
			 * else if (baseUnitQty <= lackStockNum) {// 库存紧张
			 * sell.setInventoryState("库存紧张");
			 * }
			 * else {// 库存正常
			 * sell.setInventoryState("库存充足");
			 * }
			 */
			Map map = new HashMap();
			map.put("sell", sell);
			map.put("bizAuditLog", wlCmBizAuditLogService.findBizAuditList(sellId));
			return this.getJson(true, map);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 删除上架商品
	 * @param user 登录用户
	 * @param ids id数组
	 * @return 执行成功
	 */
	@RequestMapping(params = "action=deleteData")
	@ResponseBody
	public String deleteData(User user, String[] ids) {
		try {
			String logs = "";
			for (String id : ids) {
				WlCmSellEn sell = (WlCmSellEn) wlCmSellService.getObject(id);

				if (ValidateUtil.isEmpty(logs)) {
					logs = sell.getProductName();
				}
				else {
					logs += "," + sell.getProductName();
				}
			}
			wlCmSellService.removeData(ids);
			this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.delete"), logs);
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 商品上架
	 * @param user 登录用户
	 * @param sellIds 上架单id
	 * @return 执行成功
	 */
	@RequestMapping(params = "action=updateUpState")
	@ResponseBody
	public String updateUpState(User user, String[] sellIds) {
		try {
			wlCmSellService.updateState(sellIds, "HAS_SHELVE");
			String logs = "";
			Date date = new Date();
			for (String sellId : sellIds) {
				WlCmSellEn sell = (WlCmSellEn) wlCmSellService.getObject(sellId);
				if (ValidateUtil.isEmpty(logs)) {
					logs = sell.getProductName();
				}
				else {
					logs += "," + sell.getProductName();
				}
				// sell.setSellTime(date);// 上架时间
				// wlCmSellService.saveObject(sell);
			}
			this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.set"), Lang.getString("wl.cm.WlCmSellAction.up") + " " + logs);
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 商品下架
	 * @param user 登录用户
	 * @param sellIds 下架单id
	 * @return 执行成功
	 */
	@RequestMapping(params = "action=updateDownState")
	@ResponseBody
	public String updateDownState(User user, String[] sellIds) {
		try {
			wlCmSellService.updateState(sellIds, "NO_SHELVE");
			String logs = "";
			for (String sellId : sellIds) {
				WlCmSellEn sell = (WlCmSellEn) wlCmSellService.getObject(sellId);

				if (ValidateUtil.isEmpty(logs)) {
					logs = sell.getProductName();
				}
				else {
					logs += "," + sell.getProductName();
				}
				// sell.setSellTime(null);// 上架时间置空
				// wlCmSellService.saveObject(sell);
			}
			this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.set"), Lang.getString("wl.cm.WlCmSellAction.down") + " " + logs);
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取上架商品的图片列表
	 * @param user 登录用户
	 * @param sellId 上架商品id
	 * @param isThumFlag 是否缩略图
	 * @param sellStateEk 商品上架状态
	 * @return 上架商品的图片列表
	 */
	@RequestMapping(params = "action=findSellPic")
	@ResponseBody
	public String findSellPic(User user, String sellId, String isThumFlag, String sellStateEk) {
		try {
			return this.getJson(true, wlCmSellService.findSellPic(sellId, isThumFlag, sellStateEk));
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 图片删除
	 * @param user 登录用户
	 * @param sellId 商品上架id
	 * @param picId 图片id
	 * @return 执行操作成功
	 */
	@RequestMapping(params = "action=removePic")
	@ResponseBody
	public String removePic(User user, String sellId, String picId) {
		try {
			wlCmSellService.saveAndReomovePic(user, sellId, picId, false, "");
			WlCmSellEn sell = (WlCmSellEn) wlCmSellService.getObject(sellId);
			this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.edit"), Lang.getString("wl.cm.WlCmSellAction.picDown") + " " + sell.getProductName());
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 图片上传
	 * @param user 登录用户
	 * @param sellId 商品上架id
	 * @param picId 图片id
	 * @param imgCount 上传的图片数量
	 * @return 执行操作成功
	 */
	@RequestMapping(params = "action=savePic")
	@ResponseBody
	public String savePic(User user, String sellId, String picId, String imgCount) {
		try {
			wlCmSellService.saveAndReomovePic(user, sellId, picId, true, imgCount);
			WlCmSellEn sell = (WlCmSellEn) wlCmSellService.getObject(sellId);
			this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.edit"), Lang.getString("wl.cm.WlCmSellAction.picUp") + " " + sell.getProductName());
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 图片上传
	 * @param user 登录用户
	 * @param initThumId 页面初始化时的缩略图path
	 * @param curThumId 用户更改后的缩略图path
	 * @return 执行操作成功
	 */
	@RequestMapping(params = "action=saveThum")
	@ResponseBody
	public String saveThum(User user, String initThumId, String curThumId) {
		try {
			WlCmSellPicEn wlCmSellPic = null;
			if (!ValidateUtil.isEmpty(initThumId)) {
				wlCmSellPic = wlCmSellPicService.getWlCmSellPic(initThumId);
				if (!ValidateUtil.isNull(wlCmSellPic)) {
					wlCmSellPic.setIsThumFlag("0");
					wlCmSellPicService.saveObject(wlCmSellPic);
				}
			}
			if (!ValidateUtil.isEmpty(curThumId)) {
				wlCmSellPic = wlCmSellPicService.getWlCmSellPic(curThumId);
				wlCmSellPic.setIsThumFlag("1");
				wlCmSellPicService.saveObject(wlCmSellPic);
			}

			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据上架状态获取上架商品
	 * @param sellState 上架状态
	 * @return 上架商品的图片列表
	 */
	@RequestMapping(params = "action=findEsSellByState")
	@ResponseBody
	public String findEsSellByState(String sellState) {
		try {
			return this.getJson(true, wlCmSellService.findEsSellByState(sellState));
		}
		catch (Exception e) {
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
	@RequestMapping(params = "action=getCategoryTree")
	@ResponseBody
	public String getCategoryTree(User user, String isEnableFlag, String rootId, String isLastFlag) {
		try {

			Map map = wlCmCategoryService.getTree(rootId, isEnableFlag, isLastFlag, "","");

			return this.getJson(true, map);
		}
		catch (Exception e) {
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
			Page page = wlCmItemService.search(pageSize, currPage, categoryId, "","","","","","");

			return this.getJson(true, page);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取物料信息
	 * @param itemId 上架商品修改后的物料id
	 * @param curItemCd 上架商品修改前的物料id
	 * @return 物料对象
	 */
	@RequestMapping(params = "action=getItem")
	@ResponseBody
	public String getItem(String itemId, String curItemCd) {
		try {
			WlCmItem wlCmItem = (WlCmItem) wlCmItemService.getObject(itemId);
			WlCmSellEn wlCmSell = wlCmSellService.getWlCmSellByItemCd(wlCmItem.getItemCd());
			// 商品已存在（过滤掉自身）
			if (!ValidateUtil.isNull(wlCmSell) && !wlCmItem.getItemCd().equals(curItemCd)) {
				throw new BaseException(wlCmItem.getItemCd() + Lang.getString("wl.cm.WlCmSellAction.itemCdHasExist"));
			}
			return this.getJson(true, wlCmItem);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取上架商品tree
	 * @return map树
	 */
	@RequestMapping(params = "action=getSellTree")
	@ResponseBody
	public String getSellTree() {
		try {
			Map resultMap = new HashMap();
			Map sellTreeMap = wlCmSellService.getSellTree();
			resultMap.put("sellTreeMap", sellTreeMap);
			return this.getJson(true, resultMap);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 更为为初审通过或初审驳回
	 * @param sellIds 上架商品id数组
	 * @param auditRemark 审核意见
	 * @param auditNodeEk 审核环节
	 * @param auditState 审核状态
	 * @param user 当前登录用户
	 */
	@RequestMapping(params = "action=updateFirstAudit")
	@ResponseBody
	public String updateFirstAudit(String[] sellIds, String auditRemark, String auditNodeEk, String auditState, User user) {
		try {
			wlCmSellService.updateFirstAudit(sellIds, auditRemark, auditNodeEk, auditState, user);
			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 更改为终审通过或终审驳回
	 * @param sellIds 上架商品id数组
	 * @param auditRemark 审核意见
	 * @param auditNodeEk 审核环节
	 * @param auditState 审核状态
	 * @param user 当前登录用户
	 * 
	 */
	@RequestMapping(params = "action=updateFinalAudit")
	@ResponseBody
	public String updateFinalAudit(String[] sellIds, String auditRemark, String auditNodeEk, String auditState, User user) {
		try {
			wlCmSellService.updateFinalAudit(sellIds, auditRemark, auditNodeEk, auditState, user);
			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 将编辑预览的信息缓存在session
	 * @param type set表示设置值到session，get表示从session取值出来
	 * @param typeName 产品类型名称
	 * @param content 预览信息对象内容
	 * @param productName 产品名称
	 * @param request 请求
	 * @return 预览信息或空
	 */
	@RequestMapping(params = "action=getOrSetInfo")
	@ResponseBody
	public String getOrSetInfo(String type, String typeName, String content, String productName, HttpServletRequest request) {
		try {
			HttpSession session = request.getSession(true);
			if ("set".equals(type)) {// 设置
				session.setAttribute("content", content);
				session.setAttribute("typeName", typeName);
				session.setAttribute("productName", productName);
			}
			else if ("get".equals(type)) {// 取值
				Map map = new HashMap();
				map.put("content", session.getAttribute("content"));
				map.put("typeName", session.getAttribute("typeName"));
				map.put("productName", session.getAttribute("productName"));
				return this.getJson(true, map);
			}
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
	
	/***
	 * 保存配置列表
	 * @param allocationJson 配置列表
	 * @param delAllocationId 要删除的配置id
	 * @return 空
	 */
	@RequestMapping(params = "action=saveAllocation")
	@ResponseBody
	public String saveAllocation(String allocationJson,String[] delAllocationId) {
		try {
			List<WlCmSellAllocationEn> allocationList = JsonUtil.toList(allocationJson, WlCmSellAllocationEn.class);
			wlCmSellService.saveAllocation(allocationList,delAllocationId);
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
	
	/**
	 * 查询配置列表
	 * @param parentSellId 关联上架产品id
	 * @return 查询配置列表
	 */
	@RequestMapping(params = "action=findAllocationList")
	@ResponseBody
	public String findAllocationList(String parentSellId) {
		try {
			List allocationList = wlCmSellService.findAllocationList(parentSellId);

			return this.getJson(true, allocationList);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
	/**
	 * 查询配置列表
	 * @param sellId
	 * @return
	 */
	@RequestMapping(params = "action=findSellAllocationList")
	@ResponseBody
	public String findSellAllocationList(String sellId) {
		try {
			List allocationList = wlCmSellService.findSellAllocationList(sellId);

			return this.getJson(true, allocationList);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
	
	/**
	 * 查询物料自定义属性表
	 * @param itemId 物料ID
	 * @return 物料自定义属性表
	 */
	@RequestMapping(params = "action=findItemAttrList")
	@ResponseBody
	public String findItemAttrList(String itemId, String languageType) {
		try {

			List itemAttrList = wlCmItemService.findItemAttrList(itemId, languageType);

			return this.getJson(true, itemAttrList);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
	

}