package com.gt.wl.wm.action;

import java.util.ArrayList;
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

import com.gt.wl.cm.model.WlCmStorage;
import com.gt.wl.cm.model.WlCmStorageArea;
import com.gt.wl.cm.service.WlCmCategoryService;
import com.gt.wl.cm.service.WlCmItemService;
import com.gt.wl.cm.service.WlCmSecurityService;
import com.gt.wl.cm.service.WlCmStorageAreaService;
import com.gt.wl.cm.service.WlCmStorageService;
import com.gt.wl.cm.service.WlCmTypeService;
import com.gt.wl.es.service.WlEsTodoMsgService;
import com.gt.wl.util.Type;
import com.gt.wl.wm.model.WlWmInventory;
import com.gt.wl.wm.model.WlWmNoticeCheck;
import com.gt.wl.wm.model.WlWmNoticeCheckDetl;
import com.gt.wl.wm.model.WlWmStoreCheckDetl;
import com.gt.wl.wm.service.WlWmInventoryService;
import com.gt.wl.wm.service.WlWmNoticeCheckService;
import com.gt.wl.wm.service.WlWmPushService;

@Controller
@RequestMapping("/wl/wm/wlWmNoticeCheckAction.do")
public class WlWmNoticeCheckAction extends BaseAction {
	private WlWmNoticeCheckService wlWmNoticeCheckService = (WlWmNoticeCheckService) Sc.getBean("wl.wm.WlWmNoticeCheckService");
	private WlCmStorageService wlCmStorageService = (WlCmStorageService) Sc.getBean("wl.cm.WlCmStorageService");
	private WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");
	private WlCmTypeService wlCmTypeService = (WlCmTypeService) Sc.getBean("wl.cm.WlCmTypeService");
	private WlCmCategoryService wlCmCategoryService = (WlCmCategoryService) Sc.getBean("wl.cm.WlCmCategoryService");
	private WlCmItemService wlCmItemService = (WlCmItemService) Sc.getBean("wl.cm.WlCmItemService");
	private WlWmPushService wlWmPushService = (WlWmPushService) Sc.getBean("wl.wm.WlWmPushService");
	private WlCmStorageAreaService wlCmStorageAreaService = (WlCmStorageAreaService) Sc.getBean("wl.cm.WlCmStorageAreaService");
	private WlEsTodoMsgService wlEsTodoMsgService = (WlEsTodoMsgService) Sc.getBean("wl.es.WlEsTodoMsgService");
	private WlWmInventoryService wlWmInventoryService = (WlWmInventoryService) Sc.getBean("wl.wm.WlWmInventoryService");
	private final static String MODULE_CODE = "0305005";// 模块代码
	private final static String SYSTEM_NAME = Lang.getString("wl.wm.SYSTEM_NAME");
	private final static String MODULE_NAME = Lang.getString("wl.wm.wlWmNoticeCheckAction.MODULE_NAME");

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
			mapResult.put("enAbleStorageEnum", wlCmStorageService.findStorageList());
			mapResult.put("allStorageEnum", wlCmStorageService.getObjects());
			return this.getJson(true, mapResult);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取盘点通知单和盘点通单从表
	 * @param noticeCheckId 盘点通知单id
	 * @return 盘点通知单和盘点通单从表，仓库是否启用
	 */
	@RequestMapping(params = "action=getData")
	@ResponseBody
	public String getData(String noticeCheckId) {
		try {
			WlWmNoticeCheck wlWmNoticeCheck = (WlWmNoticeCheck) wlWmNoticeCheckService.getObject(noticeCheckId);
			List<WlWmNoticeCheckDetl> list = wlWmNoticeCheckService.findNoticeCheckDtlList(noticeCheckId);// 盘点通知单从表
			for (WlWmNoticeCheckDetl wlWmNoticeCheckDetl : list) {
				WlWmInventory wlWmInventory = wlWmInventoryService.getData(wlWmNoticeCheckDetl.getStorageId(), wlWmNoticeCheckDetl.getItemId());
				if (!ValidateUtil.isNull(wlWmInventory)) {// 库存不为空
					wlWmNoticeCheckDetl.setInventoryBaseUnitQty(wlWmInventory.getBaseUnitQty());
				}
			}
			WlCmStorage wlCmStorage = (WlCmStorage) wlCmStorageService.getObject(wlWmNoticeCheck.getStorageId());
			WlCmStorageArea wlCmStorageArea = null;
			if (!ValidateUtil.isEmpty(wlWmNoticeCheck.getStorageAreaId())) {
				wlCmStorageArea = (WlCmStorageArea) wlCmStorageAreaService.getObject(wlWmNoticeCheck.getStorageAreaId());
			}
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("noticeCheck", wlWmNoticeCheck);
			resultMap.put("noticeCheckDetl", list);
			resultMap.put("storageIsEnableFlag", wlCmStorage.getIsEnableFlag());
			resultMap.put("storageAreaIsEnableFlag", "1");
			if (!ValidateUtil.isNull(wlCmStorageArea)) {
				resultMap.put("storageAreaIsEnableFlag", wlCmStorageArea.getIsEnableFlag());
			}
			return this.getJson(true, resultMap);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 新增/编辑数据
	 * @param wlWmNoticeCheck 盘点通知单对象
	 * @param wlWmNoticeCheckDetls 盘点通知单从表对象（json格式）
	 * @param user 当前登录用户
	 * @return 空
	 */
	@RequestMapping(params = "action=saveData")
	@ResponseBody
	public String saveData(WlWmNoticeCheck wlWmNoticeCheck, String wlWmNoticeCheckDetls, User user) {
		try {
			boolean isNew = false;// 是否新建
			String memo = wlWmNoticeCheck.getMemo();
			if (!ValidateUtil.isEmpty(memo)) {
				memo = memo.replaceAll("\\t", "");
				wlWmNoticeCheck.setMemo(memo);
			}
			if (ValidateUtil.isEmpty(wlWmNoticeCheck.getNoticeCheckId())) {// 新增
				isNew = true;
			}
			else {// 编辑
				isNew = false;
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.add"), "[" + wlWmNoticeCheck.getNoticeNo() + "]");
			}
			List<WlWmNoticeCheckDetl> wlWmNoticeCheckDetlList = JsonUtil.toList(wlWmNoticeCheckDetls, WlWmNoticeCheckDetl.class);
			wlWmNoticeCheckService.saveData(wlWmNoticeCheck, wlWmNoticeCheckDetlList, user);
			// 新增日志
			if (isNew) {
				wlEsTodoMsgService.saveToDoMsg(wlWmNoticeCheck.getNoticeCheckId(), "NOTICECHECK");
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.add"), "[" + wlWmNoticeCheck.getNoticeNo() + "]");
			}
			return this.getJson(true, "");

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询盘点通知单(分页)
	 * @param paraMap 前端参数
	 * @param currPage 当前页
	 * @param pageSize 页数
	 * @return 盘点通知单
	 */
	@RequestMapping(params = "action=findDataList")
	@ResponseBody
	public String findDataList(ParaMap paraMap, int currPage, int pageSize) {
		try {
			Map pMap = paraMap.getMap();
			Page page = wlWmNoticeCheckService.search(currPage, pageSize, pMap);
			List<WlWmNoticeCheck> list = page.getItems();
			List<Type> resultEkList = wlCmTypeService.findByCode("RESULT_EK");

			// 组合所用物料的名字，格式为"划船器等6种物料"
			for (WlWmNoticeCheck wlWmNoticeCheck : list) {
				wlWmNoticeCheck.setAllItemName(wlWmNoticeCheckService.getAllItemName(wlWmNoticeCheck.getNoticeCheckId()));
				for (Type type : resultEkList) {
					if (type.getId().equals(wlWmNoticeCheck.getResultEk())) {
						wlWmNoticeCheck.setResultEkValue(type.getLable());
						break;
					}
				}
			}
			return this.getJson(true, page);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 更改盘点通知单为下发
	 * @param noticeCheckIds 盘点通知单ids
	 * @param user 当前登录用户
	 * @return 异常数据提示或为空
	 */
	@RequestMapping(params = "action=updateBillStateToIssue")
	@ResponseBody
	public String updateBillStateToIssue(String[] noticeCheckIds, User user) {
		try {
			List list = wlWmNoticeCheckService.findNoticeCheckList(noticeCheckIds, "NO_ISSUE", "");
			if (!ValidateUtil.isEmpty(list)) {// 含有已下发或作废的数据
				return this.getJson(true, Lang.getString("wl.wm.wlWmNoticeCheckAction.notAllNoIssue"));
			}
			else {// 全为未下发数据
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.wm.wlWmNoticeCheckAction.issue"), wlWmNoticeCheckService.getNoticeNo(noticeCheckIds));
				wlWmNoticeCheckService.updateNoticeCheckState(noticeCheckIds, "ISSUE", user);// 更为为已下发
				try {
					wlWmPushService.getMessage(0);
				}
				catch (Exception e) {
					return this.getJson(true, "");
				}
				return this.getJson(true, "");
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 更改盘点通知单为作废
	 * @param noticeCheckIds 盘点通知单ids
	 * @param user 当前登录用户
	 * @return 异常数据提示或为空
	 */
	@RequestMapping(params = "action=updateBillStateToCancle")
	@ResponseBody
	public String updateBillStateToCancle(String[] noticeCheckIds, User user) {
		try {
			List list = wlWmNoticeCheckService.findNoticeCheckList(noticeCheckIds, "", "NOT_EXECUTE");
			if (!ValidateUtil.isEmpty(list)) {// 含有未执行的数据
				return this.getJson(true, Lang.getString("wl.wm.wlWmNoticeCheckAction.notAllNoExecute"));
			}
			else {// 全为未下发数据
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.wm.wlWmNoticeCheckAction.cancle"), wlWmNoticeCheckService.getNoticeNo(noticeCheckIds));
				wlWmNoticeCheckService.updateNoticeCheckState(noticeCheckIds, "CANCEL", user);// 更为为已下发
				return this.getJson(true, "");
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 删除盘点通知单
	 * @param noticeCheckIds 盘点通知单ids
	 * @param user 当前登录用户
	 * @return 异常数据提示或为空
	 */
	@RequestMapping(params = "action=deleteData")
	@ResponseBody
	public String deleteData(String[] noticeCheckIds, User user) {
		try {
			List list = wlWmNoticeCheckService.findNoticeCheckList(noticeCheckIds, "NO_ISSUE", "");
			if (!ValidateUtil.isEmpty(list)) {// 含有已下发或作废的数据
				return this.getJson(true, Lang.getString("wl.wm.wlWmNoticeCheckAction.noDel"));
			}
			else {// 全为未下发数据
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.delete"), wlWmNoticeCheckService.getNoticeNo(noticeCheckIds));
				wlWmNoticeCheckService.removeData(noticeCheckIds);// 删除数据
				return this.getJson(true, "");
			}
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
	@RequestMapping(params = "action=getTree")
	@ResponseBody
	public String getTree(User user, String isEnableFlag, String rootId, String isLastFlag) {
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
	 * @param storageId 仓库id
	 * @param itemIds 物料ids数组
	 * @param categoryId 物料类别id
	 * @param itemName 物料名称
	 * @param itemCd 物料编码
	 * @return 结果
	 */
	@RequestMapping(params = "action=findItemDataList")
	@ResponseBody
	public String findItemDataList(int pageSize, int currPage, String storageId, String[] itemIds, String categoryId,String itemName,String itemCd) {

		try {
			Page page = wlCmItemService.search2(pageSize, currPage, storageId, itemIds, categoryId, "0",itemName,itemCd);
			List list = page.getItems();
			List resultList = new ArrayList();
			for (int i = 0; i < list.size(); i++) {
				Object[] object = (Object[]) list.get(i);
				Map map = new HashMap();
				map.put("itemId", object[0]);
				map.put("itemName", object[1]);
				map.put("itemCd", object[2]);
				map.put("spec", object[3]);
				map.put("baseUnitName", object[4]);
				map.put("baseUnitQty", object[5]);
				resultList.add(map);
			}
			Page tempPage = new Page(resultList, page.getTotalCount());
			return this.getJson(true, tempPage);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取物料管理对象
	 * @param itemIds 物料管理ids
	 * @param storageId 仓库id
	 * @return 物料管理对象列表
	 */
	@RequestMapping(params = "action=findItemList")
	@ResponseBody
	public String findItemList(String[] itemIds, String storageId) {
		try {
			List<Map> list = wlCmItemService.findItemList(itemIds, null);
			for (Map map : list) {
				String itemId = map.get("itemId").toString();
				WlWmInventory wlWmInventory = wlWmInventoryService.getData(storageId, itemId);
				map.put("inventoryBaseUnitQty", wlWmInventory.getBaseUnitQty());
			}
			return this.getJson(true, list);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询异常数据列表（盘点单关联库存）
	 * @param noticeCheckId 盘点通知单id
	 * @return 异常数据提示或为空
	 */
	@RequestMapping(params = "action=getAbnormalData")
	@ResponseBody
	public String getAbnormalData(String noticeCheckId) {
		try {
			WlWmNoticeCheck wlWmNoticeCheck = (WlWmNoticeCheck) wlWmNoticeCheckService.getObject(noticeCheckId);
			List<WlWmStoreCheckDetl> list = wlWmNoticeCheckService.findAbnormalDataList(wlWmNoticeCheck.getNoticeNo(), null);
			for (WlWmStoreCheckDetl wlWmStoreCheckDetl : list) {
				double bookBaseQty = wlWmStoreCheckDetl.getBookBaseQty();
				double checkBaseQty = wlWmStoreCheckDetl.getCheckBaseQty();
				if (checkBaseQty == bookBaseQty) {
					wlWmStoreCheckDetl.setResult(Lang.getString("wl.wm.wlWmNoticeCheckAction.checkQtyEqBookQty"));
				}
				else if (checkBaseQty > bookBaseQty) {
					wlWmStoreCheckDetl.setResult(Lang.getString("wl.wm.wlWmNoticeCheckAction.checkQtyGtBookQty") + (checkBaseQty - bookBaseQty));
				}
				else if (checkBaseQty < bookBaseQty) {
					wlWmStoreCheckDetl.setResult(Lang.getString("wl.wm.wlWmNoticeCheckAction.checkQtyLtBookQty") + (bookBaseQty - checkBaseQty));

				}
			}
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("noticeCheck", wlWmNoticeCheck);
			resultMap.put("abnormal", list);
			return this.getJson(true, resultMap);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据仓库storageId获取仓库下库区对象
	 * @param storageId 仓库id
	 * @param isEnableFlag 是否启用
	 * 
	 * @return 对象
	 */
	@RequestMapping(params = "action=findWlCmStorageAreaList")
	@ResponseBody
	public String findWlCmStorageAreaList(String storageId, String isEnableFlag) {
		try {
			Map mapResult = new HashMap();
			// 获取仓库信息对象
			mapResult.put("storageAreaEnum", wlCmStorageAreaService.findWlCmStorageAreaList(storageId, isEnableFlag));
			return this.getJson(true, mapResult);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/***
	 * 获取打印数据(盘点单和从表数据)
	 * @param noticeCheckId 盘点单id
	 * @return 打印数据(盘点单和从表数据)
	 */
	@RequestMapping(params = "action=getPrintData")
	@ResponseBody
	public String getPrintData(String noticeCheckId) {
		try {
			WlWmNoticeCheck wlWmNoticeCheck = (WlWmNoticeCheck) wlWmNoticeCheckService.getObject(noticeCheckId);

			List list = wlWmNoticeCheckService.findNoticeCheckDetlAndInventory(noticeCheckId, wlWmNoticeCheck.getStorageId());
			List resultList = new ArrayList();
			for (int i = 0; i < list.size(); i++) {
				Object[] object = (Object[]) list.get(i);
				Map map = new HashMap();
				map.put("itemCd", object[0]);
				map.put("itemName", object[1]);
				map.put("spec", object[2]);
				map.put("baseUnitName", object[3]);
				map.put("baseUnitQty", object[4]);
				resultList.add(map);
			}
			List<Type> wmCheckTypeEkList = wlCmTypeService.findByCode("WM_CHECK_TYPE_EK");
			for (Type type : wmCheckTypeEkList) {
				if (type.getId().equals(wlWmNoticeCheck.getWmCheckTypeEk())) {
					wlWmNoticeCheck.setWmCheckTypeEkValue(type.getLable());
					break;
				}
			}

			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("wlWmNoticeCheck", wlWmNoticeCheck);
			resultMap.put("wlWmNoticeCheckDetlList", resultList);
			return this.getJson(true, resultMap);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}