package com.gt.wl.wm.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

import com.gt.wl.cm.model.WlCmOrg;
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
import com.gt.wl.wm.model.WlWmNoticeOut;
import com.gt.wl.wm.model.WlWmNoticeOutDetl;
import com.gt.wl.wm.model.WlWmStoreOut;
import com.gt.wl.wm.model.WlWmStoreOutDetl;
import com.gt.wl.wm.service.WlWmNoticeOutService;
import com.gt.wl.wm.service.WlWmPushService;
import com.gt.wl.wm.service.WlWmStoreOutService;

@Controller
@RequestMapping("/wl/wm/wlWmNoticeOutAction.do")
public class WlWmNoticeOutAction extends BaseAction {
	private WlWmNoticeOutService wlWmNoticeOutService = (WlWmNoticeOutService) Sc.getBean("wl.wm.WlWmNoticeOutService");
	private WlCmStorageService wlCmStorageService = (WlCmStorageService) Sc.getBean("wl.cm.WlCmStorageService");
	private WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");
	private WlCmUnitService wlCmUnitService = (WlCmUnitService) Sc.getBean("wl.cm.WlCmUnitService");
	private WlCmOrgService wlCmOrgService = (WlCmOrgService) Sc.getBean("wl.cm.WlCmOrgService");
	private WlCmStorageAreaService wlCmStorageAreaService = (WlCmStorageAreaService) Sc.getBean("wl.cm.WlCmStorageAreaService");
	private WlCmTypeService wlCmTypeService = (WlCmTypeService) Sc.getBean("wl.cm.WlCmTypeService");
	private WlCmCategoryService wlCmCategoryService = (WlCmCategoryService) Sc.getBean("wl.cm.WlCmCategoryService");
	private WlCmItemService wlCmItemService = (WlCmItemService) Sc.getBean("wl.cm.WlCmItemService");
	private WlWmStoreOutService wlWmStoreOutService = (WlWmStoreOutService) Sc.getBean("wl.wm.WlWmStoreOutService");
	private WlEsTodoMsgService wlEsTodoMsgService = (WlEsTodoMsgService) Sc.getBean("wl.es.WlEsTodoMsgService");
	private final static String MODULE_CODE = "0305003";// 模块代码
	private final static String SYSTEM_NAME = Lang.getString("wl.wm.SYSTEM_NAME");
	private final static String MODULE_NAME = Lang.getString("wl.wm.wlWmNoticeOutAction.MODULE_NAME");
	private WlWmPushService wlWmPushService = (WlWmPushService) Sc.getBean("wl.wm.WlWmPushService");

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
	 * 获取出库通知单和通知单从表
	 * @param noticeOutId 出库通知单id
	 * @return 出库通知单和通知单从表、仓库与库区是否启用
	 */
	@RequestMapping(params = "action=getData")
	@ResponseBody
	public String getData(String noticeOutId) {
		try {
			WlWmNoticeOut wlWmNoticeOut = (WlWmNoticeOut) wlWmNoticeOutService.getObject(noticeOutId);
			List list = wlWmNoticeOutService.findNoticeOutList(noticeOutId);
			WlCmStorage wlCmStorage = (WlCmStorage) wlCmStorageService.getObject(wlWmNoticeOut.getStorageId());
			WlCmStorageArea wlCmStorageArea = null;
			if (!ValidateUtil.isEmpty(wlWmNoticeOut.getStorageAreaId())) {
				wlCmStorageArea = (WlCmStorageArea) wlCmStorageAreaService.getObject(wlWmNoticeOut.getStorageAreaId());
			}
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("noticeOut", wlWmNoticeOut);
			resultMap.put("noticeOutDetl", list);
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
	 * @param wlWmNoticeOut 出库通知单对象
	 * @param wlWmNoticeOutDetls 出库通知单从表对象（json格式）
	 * @param user 当前登录用户
	 * @return 操作结果
	 */
	@RequestMapping(params = "action=saveData")
	@ResponseBody
	public String saveData(WlWmNoticeOut wlWmNoticeOut, String wlWmNoticeOutDetls, User user) {
		try {
			boolean isNew = false;// 是否新建
			String memo = wlWmNoticeOut.getMemo();
			if (!ValidateUtil.isEmpty(memo)) {
				memo = memo.replaceAll("\\t", "");
				wlWmNoticeOut.setMemo(memo);
			}
			String contactWay = wlWmNoticeOut.getContactWay();
			if (!ValidateUtil.isEmpty(contactWay)) {
				contactWay = contactWay.replaceAll("[^0-9,]", "");
				wlWmNoticeOut.setContactWay(contactWay);
			}
			String addr = wlWmNoticeOut.getAddr();
			if (!ValidateUtil.isEmpty(addr)) {
				addr = addr.replaceAll("\\t", "");
				wlWmNoticeOut.setAddr(addr);
			}

			if (ValidateUtil.isEmpty(wlWmNoticeOut.getNoticeOutId())) {// 新增
				isNew = true;
			}
			else {// 编辑
				isNew = false;
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.add"), "[" + wlWmNoticeOut.getNoticeNo() + "]");
			}
			List<WlWmNoticeOutDetl> wlWmNoticeOutDetlList = JsonUtil.toList(wlWmNoticeOutDetls, WlWmNoticeOutDetl.class);
			wlWmNoticeOutService.saveData(wlWmNoticeOut, wlWmNoticeOutDetlList, user);
			// 新增日志
			if (isNew) {
				wlEsTodoMsgService.saveToDoMsg(wlWmNoticeOut.getNoticeOutId(), "NOTICEOUT");
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.add"), "[" + wlWmNoticeOut.getNoticeNo() + "]");
			}
			return this.getJson(true, "");

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询出库通知单(分页)
	 * @param paraMap 前端参数
	 * @param currPage 当前页
	 * @param pageSize 页数
	 * @return 出库通知单
	 */
	@RequestMapping(params = "action=findDataList")
	@ResponseBody
	public String findDataList(ParaMap paraMap, int currPage, int pageSize) {
		try {
			Map pMap = paraMap.getMap();
			Page page = wlWmNoticeOutService.search(currPage, pageSize, pMap);
			List<WlWmNoticeOut> list = page.getItems();
			List<Type> resultEkList = wlCmTypeService.findByCode("RESULT_EK");

			// 组合所用物料的名字，格式为"划船器等6种物料"
			for (WlWmNoticeOut wlWmNoticeOut : list) {
				wlWmNoticeOut.setAllItemName(wlWmNoticeOutService.getAllItemName(wlWmNoticeOut.getNoticeOutId(), 0));
				for (Type type : resultEkList) {
					if (type.getId().equals(wlWmNoticeOut.getResultEk())) {
						wlWmNoticeOut.setResultEkValue(type.getLable());
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
	 * 更改出库通知单为下发
	 * @param noticeOutIds 出库通知单ids
	 * @param user 当前登录用户
	 * @return 异常数据提示或为空
	 */
	@RequestMapping(params = "action=updateBillStateToIssue")
	@ResponseBody
	public String updateBillStateToIssue(String[] noticeOutIds, User user) {
		try {
			List list = wlWmNoticeOutService.findNoticeOutList(noticeOutIds, "NO_ISSUE", "");
			if (!ValidateUtil.isEmpty(list)) {// 含有已下发或作废的数据
				return this.getJson(true, Lang.getString("wl.wm.wlWmNoticeOutAction.notAllNoIssue"));
			}
			else {// 全为未下发数据
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.wm.wlWmNoticeOutAction.issue"), wlWmNoticeOutService.getNoticeNo(noticeOutIds));
				wlWmNoticeOutService.updateNoticeOutState(noticeOutIds, "ISSUE", user);// 更为为已下发
				try {
					wlWmPushService.getMessage(2);
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
	 * 更改出库通知单为作废
	 * @param noticeOutIds 出库通知单ids
	 * @param user 当前登录用户
	 * @return 异常数据提示或为空
	 */
	@RequestMapping(params = "action=updateBillStateToCancle")
	@ResponseBody
	public String updateBillStateToCancle(String[] noticeOutIds, User user) {
		try {
			List list = wlWmNoticeOutService.findNoticeOutList(noticeOutIds, "", "NOT_EXECUTE");
			if (!ValidateUtil.isEmpty(list)) {// 含有未执行以外的数据
				return this.getJson(true, Lang.getString("wl.wm.wlWmNoticeOutAction.notAllNoExecute"));
			}
			else {// 全为未下发数据
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.wm.wlWmNoticeOutAction.cancle"), wlWmNoticeOutService.getNoticeNo(noticeOutIds));
				wlWmNoticeOutService.updateNoticeOutState(noticeOutIds, "CANCEL", user);// 更为为已下发
				return this.getJson(true, "");
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 删除出库通知单
	 * @param noticeOutIds 出库通知单ids
	 * @param user 当前登录用户
	 * @return 异常数据提示或为空
	 */
	@RequestMapping(params = "action=deleteData")
	@ResponseBody
	public String deleteData(String[] noticeOutIds, User user) {
		try {
			List list = wlWmNoticeOutService.findNoticeOutList(noticeOutIds, "NO_ISSUE", "");
			if (!ValidateUtil.isEmpty(list)) {// 含有已下发或作废的数据
				return this.getJson(true, Lang.getString("wl.wm.wlWmNoticeOutAction.noDel"));
			}
			else {// 全为未下发数据
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.delete"), wlWmNoticeOutService.getNoticeNo(noticeOutIds));
				wlWmNoticeOutService.removeData(noticeOutIds);// 更删除数据
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
	 * 获取出库通知单和通知单从表与出库单关联的数据
	 * @param noticeOutId 入库通知单id
	 * @return 异常数据提示或为空
	 */
	@RequestMapping(params = "action=getAbnormalData")
	@ResponseBody
	public String getAbnormalData(String noticeOutId) {
		try {
			WlWmNoticeOut wlWmNoticeOut = (WlWmNoticeOut) wlWmNoticeOutService.getObject(noticeOutId);
			Map paraMap = new HashMap();
			paraMap.put("noticeNo", wlWmNoticeOut.getNoticeNo());
			Page page = wlWmStoreOutService.search("wlWmStoreOut", paraMap, 1, 1);
			List<WlWmStoreOut> wlWmStoreOutList = page.getItems();
			WlWmStoreOut wlWmStoreOut = wlWmStoreOutList.get(0);
			List<WlWmStoreOutDetl> list = wlWmStoreOutService.findWlWmStoreOutDetlList(wlWmStoreOut.getStoreOutId(), "", "");
			List resultList = new ArrayList();
			for (WlWmStoreOutDetl wlWmStoreOutDetl : list) {
				Map map = new HashMap();
				map.put("itemCd", wlWmStoreOutDetl.getItemCd());
				map.put("itemName", wlWmStoreOutDetl.getItemName());
				map.put("spec", wlWmStoreOutDetl.getSpec());
				map.put("storeBaseUnitQty", wlWmStoreOutDetl.getBaseUnitQty());
				map.put("baseUnitId", wlWmStoreOutDetl.getBaseUnitId());
				map.put("baseUnitQty", wlWmStoreOutDetl.getNoticeBaseQty());
				map.put("baseUnitName", wlWmStoreOutDetl.getBaseUnitName());
				map.put("consignee", wlWmStoreOutDetl.getConsignee());
				map.put("contactWay", wlWmStoreOutDetl.getContactWay());
				if (wlWmStoreOutDetl.getBaseUnitQty() == wlWmStoreOutDetl.getNoticeBaseQty()) {
					map.put("result", Lang.getString("wl.wm.wlWmNoticeOutAction.storeQtyEqNoticeQty"));
				}
				else if (wlWmStoreOutDetl.getBaseUnitQty() > wlWmStoreOutDetl.getNoticeBaseQty()) {
					map.put("result", Lang.getString("wl.wm.wlWmNoticeOutAction.storeQtyGtNoticeQty") + (wlWmStoreOutDetl.getBaseUnitQty() - wlWmStoreOutDetl.getNoticeBaseQty()));
				}
				else if (wlWmStoreOutDetl.getBaseUnitQty() < wlWmStoreOutDetl.getNoticeBaseQty()) {
					map.put("result", Lang.getString("wl.wm.wlWmNoticeOutAction.storeQtyLtNoticeQty") + (wlWmStoreOutDetl.getNoticeBaseQty() - wlWmStoreOutDetl.getBaseUnitQty()));
				}
				resultList.add(map);
			}
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("noticeOut", wlWmNoticeOut);
			resultMap.put("abnormal", resultList);
			return this.getJson(true, resultMap);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取出库通知单列表
	 * @param noticeOutIds 出库通知单ids
	 * @param request 请求
	 * @return 出库通知单列表和当前服务器的地址
	 */
	@RequestMapping(params = "action=findNoticeOutAndDetlList")
	@ResponseBody
	public String findNoticeOutAndDetlList(String[] noticeOutIds, HttpServletRequest request) {
		try {
			List list = wlWmNoticeOutService.findNoticeOutAndDetlList(noticeOutIds);
			String path = request.getContextPath();
			String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("wlWmNoticeOutList", list);// 要打印的列表
			resultMap.put("basePath", basePath);// 地址
			return this.getJson(true, resultMap);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 保存上传数据
	 * @param path 上传文件ID
	 * @param orgId 经销商id
	 * @param storageId 仓库id
	 * @param expectOutDate 预计出库日期
	 * @param wmOutTypeEk 出库类型
	 * @param user 当前登录用户
	 * @return 结果
	 */
	@RequestMapping(params = "action=saveExcelData")
	@ResponseBody
	public String saveExcelData(String path, String orgId, String storageId, Date expectOutDate, String wmOutTypeEk, User user) {
		try {
			WlCmOrg wlCmOrg = (WlCmOrg) wlCmOrgService.getObject(orgId);
			WlCmStorage wlCmStorage = (WlCmStorage) wlCmStorageService.getObject(storageId);
			wlWmNoticeOutService.saveExcelData(path, wlCmOrg, wlCmStorage, expectOutDate, wmOutTypeEk, user);
			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}
}