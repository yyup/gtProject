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
import com.gt.wl.cm.service.WlCmOrgService;
import com.gt.wl.cm.service.WlCmSecurityService;
import com.gt.wl.cm.service.WlCmStorageAreaService;
import com.gt.wl.cm.service.WlCmStorageService;
import com.gt.wl.cm.service.WlCmTypeService;
import com.gt.wl.cm.service.WlCmUnitService;
import com.gt.wl.es.service.WlEsTodoMsgService;
import com.gt.wl.util.Type;
import com.gt.wl.wm.model.WlWmNoticeIn;
import com.gt.wl.wm.model.WlWmNoticeInDetl;
import com.gt.wl.wm.model.WlWmStoreIn;
import com.gt.wl.wm.model.WlWmStoreInDetl;
import com.gt.wl.wm.service.WlWmNoticeInService;
import com.gt.wl.wm.service.WlWmPushService;
import com.gt.wl.wm.service.WlWmStoreInService;

@Controller
@RequestMapping("/wl/wm/wlWmNoticeInAction.do")
public class WlWmNoticeInAction extends BaseAction {
	private WlWmNoticeInService wlWmNoticeInService = (WlWmNoticeInService) Sc.getBean("wl.wm.WlWmNoticeInService");
	private WlCmStorageService wlCmStorageService = (WlCmStorageService) Sc.getBean("wl.cm.WlCmStorageService");
	private WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");
	private WlCmUnitService wlCmUnitService = (WlCmUnitService) Sc.getBean("wl.cm.WlCmUnitService");
	private WlCmOrgService wlCmOrgService = (WlCmOrgService) Sc.getBean("wl.cm.WlCmOrgService");
	private WlCmStorageAreaService wlCmStorageAreaService = (WlCmStorageAreaService) Sc.getBean("wl.cm.WlCmStorageAreaService");
	private WlCmTypeService wlCmTypeService = (WlCmTypeService) Sc.getBean("wl.cm.WlCmTypeService");
	private WlCmCategoryService wlCmCategoryService = (WlCmCategoryService) Sc.getBean("wl.cm.WlCmCategoryService");
	private WlCmItemService wlCmItemService = (WlCmItemService) Sc.getBean("wl.cm.WlCmItemService");
	private WlWmPushService wlWmPushService = (WlWmPushService) Sc.getBean("wl.wm.WlWmPushService");
	private WlWmStoreInService wlWmStoreInService = (WlWmStoreInService) Sc.getBean("wl.wm.WlWmStoreInService");
	private WlEsTodoMsgService wlEsTodoMsgService = (WlEsTodoMsgService) Sc.getBean("wl.es.WlEsTodoMsgService");
	private final static String MODULE_CODE = "0305001";// 模块代码
	private final static String SYSTEM_NAME = Lang.getString("wl.wm.SYSTEM_NAME");
	private final static String MODULE_NAME = Lang.getString("wl.wm.wlWmNoticeInAction.MODULE_NAME");

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
			mapResult.put("orgEnum", wlCmOrgService.findWlCmOrgList("", "", "1", null));
			mapResult.put("unitEnum", wlCmUnitService.getObjects());
			return this.getJson(true, mapResult);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取入库通知单和通知单从表
	 * @param noticeInId 入库通知单id
	 * @return 入库通知单和通知单从表列表数据，仓库和库区的状态
	 */
	@RequestMapping(params = "action=getData")
	@ResponseBody
	public String getData(String noticeInId) {
		try {
			WlWmNoticeIn wlWmNoticeIn = (WlWmNoticeIn) wlWmNoticeInService.getObject(noticeInId);
			List list = wlWmNoticeInService.findNoticeInDetlList(noticeInId);
			WlCmStorage wlCmStorage = (WlCmStorage) wlCmStorageService.getObject(wlWmNoticeIn.getStorageId());
			WlCmStorageArea wlCmStorageArea = null;
			if (!ValidateUtil.isEmpty(wlWmNoticeIn.getStorageAreaId())) {
				wlCmStorageArea = (WlCmStorageArea) wlCmStorageAreaService.getObject(wlWmNoticeIn.getStorageAreaId());
			}
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("noticeIn", wlWmNoticeIn);
			resultMap.put("noticeInDetl", list);
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
	 * @param wlWmNoticeIn 入库通知单对象
	 * @param wlWmNoticeInDetls 入库通知单从表对象（json格式）
	 * @param user 当前登录用户
	 * @return 空
	 */
	@RequestMapping(params = "action=saveData")
	@ResponseBody
	public String saveData(WlWmNoticeIn wlWmNoticeIn, String wlWmNoticeInDetls, User user) {
		try {
			boolean isNew = false;// 是否新建
			String memo = wlWmNoticeIn.getMemo();
			if (!ValidateUtil.isEmpty(memo)) {
				memo = memo.replaceAll("\\t", "");
				wlWmNoticeIn.setMemo(memo);
			}
			if (ValidateUtil.isEmpty(wlWmNoticeIn.getNoticeInId())) {// 新增
				isNew = true;
			}
			else {// 编辑
				isNew = false;
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.add"), "[" + wlWmNoticeIn.getNoticeNo() + "]");
			}
			List<WlWmNoticeInDetl> wlWmNoticeInDetlList = JsonUtil.toList(wlWmNoticeInDetls, WlWmNoticeInDetl.class);
			wlWmNoticeInService.saveData(wlWmNoticeIn, wlWmNoticeInDetlList, user);
			// 添加新增日志
			if (isNew) {// 新增
				wlEsTodoMsgService.saveToDoMsg(wlWmNoticeIn.getNoticeInId(), "NOTICEIN");
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.add"), "[" + wlWmNoticeIn.getNoticeNo() + "]");
			}
			return this.getJson(true, "");

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询入库通知单(分页)
	 * @param paraMap 前端参数
	 * @param currPage 当前页
	 * @param pageSize 页数
	 * @return 入库通知单
	 */
	@RequestMapping(params = "action=findDataList")
	@ResponseBody
	public String findDataList(ParaMap paraMap, int currPage, int pageSize) {
		try {
			Map pMap = paraMap.getMap();
			Page page = wlWmNoticeInService.search(currPage, pageSize, pMap);
			List<WlWmNoticeIn> list = page.getItems();
			List<Type> resultEkList = wlCmTypeService.findByCode("RESULT_EK");

			// 组合所用物料的名字，格式为"划船器等6种物料"
			for (WlWmNoticeIn wlWmNoticeIn : list) {
				wlWmNoticeIn.setAllItemName(wlWmNoticeInService.getAllItemName(wlWmNoticeIn.getNoticeInId(), 0));
				for (Type type : resultEkList) {
					if (type.getId().equals(wlWmNoticeIn.getResultEk())) {
						wlWmNoticeIn.setResultEkValue(type.getLable());
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
	 * 更改入库通知单为下发
	 * @param noticeInIds 入库通知单ids
	 * @param user 当前登录用户
	 * @return 异常数据提示或为空
	 */
	@RequestMapping(params = "action=updateBillStateToIssue")
	@ResponseBody
	public String updateBillStateToIssue(String[] noticeInIds, User user) {
		try {
			List list = wlWmNoticeInService.findNoticeInList(noticeInIds, "NO_ISSUE", "");
			if (!ValidateUtil.isEmpty(list)) {// 含有已下发或作废的数据
				return this.getJson(true, Lang.getString("wl.wm.wlWmNoticeInAction.notAllNoIssue"));
			}
			else {// 全为未下发数据
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.wm.wlWmNoticeInAction.issue"), wlWmNoticeInService.getNoticeNo(noticeInIds));
				wlWmNoticeInService.updateNoticeInState(noticeInIds, "ISSUE", user);// 更为为已下发
				try {
					wlWmPushService.getMessage(1);
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
	 * 更改入库通知单为作废
	 * @param noticeInIds 入库通知单ids
	 * @param user 当前登录用户
	 * @return 异常数据提示或为空
	 */
	@RequestMapping(params = "action=updateBillStateToCancle")
	@ResponseBody
	public String updateBillStateToCancle(String[] noticeInIds, User user) {
		try {
			List list = wlWmNoticeInService.findNoticeInList(noticeInIds, "", "NOT_EXECUTE");
			if (!ValidateUtil.isEmpty(list)) {// 含有未执行以外的数据
				return this.getJson(true, Lang.getString("wl.wm.wlWmNoticeInAction.notAllNoExecute"));
			}
			else {// 全为未下发数据
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.wm.wlWmNoticeInAction.cancle"), wlWmNoticeInService.getNoticeNo(noticeInIds));
				wlWmNoticeInService.updateNoticeInState(noticeInIds, "CANCEL", user);// 更为为已下发
				return this.getJson(true, "");
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 删除入库通知单
	 * @param noticeInIds 入库通知单ids
	 * @param user 当前登录用户
	 * @return 异常数据提示或为空
	 */
	@RequestMapping(params = "action=deleteData")
	@ResponseBody
	public String deleteData(String[] noticeInIds, User user) {
		try {
			List list = wlWmNoticeInService.findNoticeInList(noticeInIds, "NO_ISSUE", "");
			if (!ValidateUtil.isEmpty(list)) {// 含有已下发或作废的数据
				return this.getJson(true, Lang.getString("wl.wm.wlWmNoticeInAction.noDel"));
			}
			else {// 全为未下发数据
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.delete"), wlWmNoticeInService.getNoticeNo(noticeInIds));
				wlWmNoticeInService.removeData(noticeInIds);// 删除数据
				return this.getJson(true, "");
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取入库通知单和通知单从表与入库单关联的数据
	 * @param noticeInId 入库通知单id
	 * @return 异常数据列表
	 */
	@RequestMapping(params = "action=getAbnormalData")
	@ResponseBody
	public String getAbnormalData(String noticeInId) {
		try {
			WlWmNoticeIn wlWmNoticeIn = (WlWmNoticeIn) wlWmNoticeInService.getObject(noticeInId);
			Map paraMap = new HashMap();
			paraMap.put("noticeNo", wlWmNoticeIn.getNoticeNo());
			Page page = wlWmStoreInService.search("wlWmStoreIn", paraMap, 1, 1);
			List<WlWmStoreIn> wlWmStoreInList = page.getItems();
			WlWmStoreIn wlWmStoreIn = wlWmStoreInList.get(0);// 获取入库单对象
			List<WlWmStoreInDetl> list = wlWmStoreInService.findWlWmStoreInDetlList(wlWmStoreIn.getStoreInId());
			List resultList = new ArrayList();
			for (WlWmStoreInDetl wlWmStoreInDetl : list) {
				Map map = new HashMap();
				map.put("itemCd", wlWmStoreInDetl.getItemCd());
				map.put("itemName", wlWmStoreInDetl.getItemName());
				map.put("spec", wlWmStoreInDetl.getSpec());
				map.put("storeBaseUnitQty", wlWmStoreInDetl.getBaseUnitQty());
				map.put("baseUnitId", wlWmStoreInDetl.getBaseUnitId());
				map.put("baseUnitQty", wlWmStoreInDetl.getNoticeBaseQty());
				map.put("baseUnitName", wlWmStoreInDetl.getBaseUnitName());

				if (wlWmStoreInDetl.getNoticeBaseQty() == 0) {// 通知单物料不存在
					map.put("baseUnitQty", "");
					map.put("result", Lang.getString("wl.wm.wlWmNoticeInAction.noNoticeIn") + wlWmStoreInDetl.getBaseUnitQty());

				}
				else {// 通知单物料存在
					if (wlWmStoreInDetl.getBaseUnitQty() == wlWmStoreInDetl.getNoticeBaseQty()) {
						map.put("result", Lang.getString("wl.wm.wlWmNoticeInAction.storeQtyEqNoticeQty"));
					}
					else if (wlWmStoreInDetl.getBaseUnitQty() > wlWmStoreInDetl.getNoticeBaseQty()) {
						map.put("result", Lang.getString("wl.wm.wlWmNoticeInAction.storeQtyGtNoticeQty") + (wlWmStoreInDetl.getBaseUnitQty() - wlWmStoreInDetl.getNoticeBaseQty()));
					}
					else if (wlWmStoreInDetl.getBaseUnitQty() < wlWmStoreInDetl.getNoticeBaseQty()) {
						map.put("result", Lang.getString("wl.wm.wlWmNoticeInAction.storeQtyLtNoticeQty") + (wlWmStoreInDetl.getNoticeBaseQty() - wlWmStoreInDetl.getBaseUnitQty()));
					}
				}
				resultList.add(map);
			}
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("noticeIn", wlWmNoticeIn);
			resultMap.put("abnormal", resultList);
			return this.getJson(true, resultMap);
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
	 * 获取物料管理对象
	 * @param itemIds 物料管理ids
	 * @return 物料管理对象列表
	 */
	@RequestMapping(params = "action=findItemList")
	@ResponseBody
	public String findItemList(String[] itemIds) {
		try {
			return this.getJson(true, wlCmItemService.findItemList(itemIds, null));
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
			Page page = wlCmItemService.search2(pageSize, currPage, storageId, itemIds, categoryId, "1",itemName,itemCd);
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
}