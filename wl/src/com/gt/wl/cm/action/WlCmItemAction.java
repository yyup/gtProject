package com.gt.wl.cm.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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


import com.gt.wl.cm.model.WlCmCategory;
import com.gt.wl.cm.model.WlCmItem;
import com.gt.wl.cm.model.WlCmItemAttr;
import com.gt.wl.cm.model.WlCmSell;
import com.gt.wl.cm.model.WlCmUnit;
import com.gt.wl.cm.service.WlCmCategoryService;
import com.gt.wl.cm.service.WlCmItemService;
import com.gt.wl.cm.service.WlCmProductTypeService;
import com.gt.wl.cm.service.WlCmSecurityService;
import com.gt.wl.cm.service.WlCmSellService;
import com.gt.wl.cm.service.WlCmTypeService;
import com.gt.wl.cm.service.WlCmUnitService;
import com.gt.wl.wm.service.WlWmInventoryService;

@Controller
@RequestMapping("/wl/cm/wlCmItemAction.do")
public class WlCmItemAction extends BaseAction {
	private WlCmItemService wlCmItemService = (WlCmItemService) Sc.getBean("wl.cm.WlCmItemService");
	private WlCmSellService wlCmSellService = (WlCmSellService) Sc.getBean("wl.cm.WlCmSellService");
	private WlCmProductTypeService wlCmProductTypeService = (WlCmProductTypeService) Sc.getBean("wl.cm.WlCmProductTypeService");
	private WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");
	private WlCmUnitService wlCmUnitService = (WlCmUnitService) Sc.getBean("wl.cm.WlCmUnitService");
	private WlCmCategoryService wlCmCategoryService = (WlCmCategoryService) Sc.getBean("wl.cm.WlCmCategoryService");
	private WlWmInventoryService wlWmInventoryService = (WlWmInventoryService) Sc.getBean("wl.wm.WlWmInventoryService");
	private final static String MODULE_CODE = "0304004";// 模块代码
	private final static String SYSTEM_NAME = Lang.getString("wl.cm.SYSTEM_NAME");
	private final static String MODULE_NAME = Lang.getString("wl.cm.wlCmItemAction.MODULE_NAME");

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
			mapResult.put("unitEnum", wlCmUnitService.getObjects());
			return this.getJson(true, mapResult);
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
	 * @param filterItemId 用于过滤自身（物料）Id
	 * @param itemCd 物料编码
	 * @param itemName 物料名称
	 * @param spec 规格型号
	 * @return 结果
	 */
	@RequestMapping(params = "action=findDataList")
	@ResponseBody
	public String findDataList(int pageSize, int currPage, String categoryId,String filterItemId,String itemCd,String itemName,String spec) {

		try {
			String categoryIds=wlCmItemService.getSearchCategoryId(categoryId);
			Page page;
			if(!ValidateUtil.isEmpty(categoryIds)){//末级节点子孙物料类别的id不为空时
				page = wlCmItemService.search(pageSize, currPage, "", "",filterItemId,itemCd,itemName,spec,categoryIds);
			}else{
				page = wlCmItemService.search(pageSize, currPage, categoryId, "",filterItemId,itemCd,itemName,spec,"");
			}
			

			return this.getJson(true, page);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 增加数据
	 * @param wlCmItem w物料管理对象
	 * @param user 当前登录用户
	 * @return 结果
	 */
	@RequestMapping(params = "action=saveData")
	@ResponseBody
	public String saveData(WlCmItem wlCmItem, User user) {

		try {
			if (ValidateUtil.isEmpty(wlCmItem.getItemId())) {// 新建
				wlCmItem.setCreator(user.getName());
				wlCmItem.setCreateTime(new Date());
				wlCmItem.setIsEnableFlag("1");
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.add"), wlCmItem.getItemCd());
			}
			else {// 编辑
				wlCmItem.setModifier(user.getName());
				wlCmItem.setModifyTime(new Date());
				WlCmItem oldWlCmItem = (WlCmItem) wlCmItemService.getObject(wlCmItem.getItemId());
				String itemCd = oldWlCmItem.getItemCd();
				if (!wlCmItem.getItemCd().equals(oldWlCmItem.getItemCd())) {// 物料编码有修改，记录在日志里面
					itemCd = Lang.getString("wl.cm.wlCmItemAction.fromItemCd") + itemCd;
					itemCd += Lang.getString("wl.cm.wlCmItemAction.toItemCd") + wlCmItem.getItemCd();
				}
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.edit"), itemCd);
			}
			WlCmUnit wlCmUnit = (WlCmUnit) wlCmUnitService.getObject(wlCmItem.getBaseUnitId());
			wlCmItem.setBaseUnitName(wlCmUnit.getUnitName());
			wlCmItemService.saveData(wlCmItem);
			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 修改往来单位的状态
	 * @param itemIds 物料管理id数组
	 * @param isEnableFlag 启用标志（1表示当前要启用，0表示当前要禁用）
	 * @param user 当前用户
	 * @return 操作结果
	 */
	@RequestMapping(params = "action=updateItemState")
	@ResponseBody
	public String updateItemState(String[] itemIds, String isEnableFlag, User user) {
		try {
			String result = "";
			List list = wlCmItemService.findItemList(itemIds, isEnableFlag);
			if (!ValidateUtil.isEmpty(list)) {
				if ("1".equals(isEnableFlag)) {// 当前要启用，但勾选的数据里面已存在启用的物料
					result = Lang.getString("wl.cm.wlCmItemAction.existsEnable");
				}
				else if ("0".equals(isEnableFlag)) {// 当前要禁用，但勾选的数据里面已存在禁用的物料
					result = Lang.getString("wl.cm.wlCmItemAction.existsDisable");
				}
			}
			else {
				String itemCds = "";
				for (String itemId : itemIds) {// 修改物料状态并构造物料编码，用逗号隔开
					// 获取物料管理对象
					WlCmItem wlCmItem = (WlCmItem) wlCmItemService.getObject(itemId);
					itemCds += wlCmItem.getItemCd() + ",";
					wlCmItem.setIsEnableFlag(isEnableFlag);
					wlCmItemService.updateObject(wlCmItem);
				}
				// 添加日志
				itemCds = itemCds.substring(0, itemCds.length() - 1);
				if ("1".equals(isEnableFlag)) {
					this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.set"), itemCds + Lang.getString("wl.cm.wlCmItemAction.USE"));
				}
				else {
					this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.set"), itemCds + Lang.getString("wl.cm.wlCmItemAction.LOCK"));

				}
			}
			return this.getJson(true, result);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据单位id获取物料管理对象
	 * @param itemId 物料管理id
	 * @return 对象
	 */
	@RequestMapping(params = "action=getData")
	@ResponseBody
	public String getData(String itemId) {
		try {
			// 获取物料管理对象
			WlCmItem wlCmItem = (WlCmItem) wlCmItemService.getObject(itemId);
			WlCmCategory wlCmCategory = (WlCmCategory) wlCmCategoryService.getObject(wlCmItem.getCategoryId());
			wlCmItem.setCategoryName(wlCmCategory.getName());
			List list = wlWmInventoryService.findDataList("", itemId);
			if (ValidateUtil.isEmpty(list)) {// 库存表没有被引用，允许修改物料编码
				wlCmItem.setItemCdEdit(true);
			}
			else {// 库存表没有被引用，不允许修改物料编码
				wlCmItem.setItemCdEdit(false);
			}
			return this.getJson(true, wlCmItem);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取物料管理对象
	 * @param key 物料管理的字段
	 * @param value key对应的字段的值
	 * @param itemId 物料管理id
	 * @return 物料管理对象列表
	 */
	@RequestMapping(params = "action=findWlCmItemList")
	@ResponseBody
	public String findWlCmItemList(String key, String value, String itemId) {
		try {
			List list = wlCmItemService.findWlCmItemList(key, value, itemId);
			if (!ValidateUtil.isEmpty(list)) {
				return this.getJson(true, list);
			}
			else {
				return this.getJson(true, "");
			}

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 删除物料管理
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
				WlCmItem wlCmItem = (WlCmItem) wlCmItemService.getObject(id);
				List list = wlWmInventoryService.findDataList("", id);
				if (!ValidateUtil.isEmpty(list)) {// 如果此物料在库存表中存在，不允许删除
					String result = wlCmItem.getItemName();
					if (!ValidateUtil.isEmpty(wlCmItem.getSpec())) {
						result += "(" + wlCmItem.getSpec() + ")";
					}
					result += Lang.getString("wl.cm.wlCmItemAction.existInventory");
					throw new BaseException(result);
				}
				if (ValidateUtil.isEmpty(logs)) {
					logs = wlCmItem.getItemName();
				}
				else {
					logs += "," + wlCmItem.getItemName();
				}
			}
			wlCmItemService.removeData(ids);
			this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.delete"), logs);
			return this.getJson(true, "");
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
	 * @param itemId 物料Id号
	 * @return 商品类型树形json
	 */
	@RequestMapping(params = "action=getTree")
	@ResponseBody
	public String getTree(User user, String isEnableFlag, String rootId, String isLastFlag,String itemId) {
		try {

			Map map = wlCmCategoryService.getTree(rootId, isEnableFlag, isLastFlag, "",itemId);

			return this.getJson(true, map);
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
	public String findItemAttrList(String itemId) {
		try {
			List itemAttrList = wlCmItemService.findItemAttrList(itemId,"");
			return this.getJson(true, itemAttrList);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
	/**
	 * 保存物料属性
	 * @param itemAttrJson 物料属性json
	 * @param delAttrId 要删除的物料属性
	 * @param user 用户
	 * @return 空
	 */
	@RequestMapping(params = "action=saveAttr")
	@ResponseBody
	public String saveAttr(String itemAttrJson, String[] delAttrId,User user) {
		try {

			List<WlCmItemAttr> itemAttrList = JsonUtil.toList(itemAttrJson, WlCmItemAttr.class);
			wlCmItemService.saveAttr(itemAttrList,delAttrId);

			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}