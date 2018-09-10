package com.gt.wl.wm.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joyone.action.BaseAction;
import org.joyone.lang.BaseException;
import org.joyone.spring.Sc;
import org.joyone.sys.Lang;
import org.joyone.sys.Page;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gt.wl.cm.model.WlCmItem;
import com.gt.wl.cm.service.WlCmItemService;
import com.gt.wl.cm.service.WlCmTypeService;
import com.gt.wl.util.Type;
import com.gt.wl.wm.model.WlWmInventory;
import com.gt.wl.wm.model.WlWmInventoryDtl;
import com.gt.wl.wm.model.WlWmNoticeCheck;
import com.gt.wl.wm.model.WlWmNoticeCheckDetl;
import com.gt.wl.wm.model.WlWmNoticeIn;
import com.gt.wl.wm.model.WlWmNoticeInDetl;
import com.gt.wl.wm.model.WlWmNoticeOut;
import com.gt.wl.wm.model.WlWmNoticeOutDetl;
import com.gt.wl.wm.service.WlWmInventoryService;
import com.gt.wl.wm.service.WlWmNoticeCheckService;
import com.gt.wl.wm.service.WlWmNoticeInService;
import com.gt.wl.wm.service.WlWmNoticeOutService;
import com.gt.wl.wm.service.WlWmStoreInService;
import com.gt.wl.wm.service.WlWmStoreOutService;

/**
 * 待办APP端Action
 * @author liuyj
 * 
 */
@Controller
@RequestMapping("/app/wm/wlWmAppNoticeTodoAction.do")
public class WlWmAppNoticeTodoAction extends BaseAction {
	private WlWmNoticeCheckService wlWmNoticeCheckService = (WlWmNoticeCheckService) Sc.getBean("wl.wm.WlWmNoticeCheckService");
	private WlWmNoticeInService wlWmNoticeInService = (WlWmNoticeInService) Sc.getBean("wl.wm.WlWmNoticeInService");
	private WlWmNoticeOutService wlWmNoticeOutService = (WlWmNoticeOutService) Sc.getBean("wl.wm.WlWmNoticeOutService");
	private WlCmTypeService wlCmTypeService = (WlCmTypeService) Sc.getBean("wl.cm.WlCmTypeService");
	private WlCmItemService wlCmItemService = (WlCmItemService) Sc.getBean("wl.cm.WlCmItemService");
	private WlWmStoreInService wlWmStoreInService = (WlWmStoreInService) Sc.getBean("wl.wm.WlWmStoreInService");
	private WlWmStoreOutService wlWmStoreOutService = (WlWmStoreOutService) Sc.getBean("wl.wm.WlWmStoreOutService");
	private WlWmInventoryService wlWmInventoryService = (WlWmInventoryService) Sc.getBean("wl.wm.WlWmInventoryService");

	/**
	 * 初始化
	 * @param storageId 仓库ID
	 * @return 待办单数（盘点通知单数、入库通知单数、出库通知单数、合计单数）
	 */
	@RequestMapping(params = "action=init")
	@ResponseBody
	public String init(String storageId) {
		long inTotal = 0;// 入库通知单数
		long outTotal = 0;// 出库通知单数
		long checkTotal = 0;// 盘点通知单数
		long allTotal = 0;// 合计
		try {
			Map resultMap = new HashMap();
			checkTotal = wlWmNoticeCheckService.getTodoCount(storageId);
			inTotal = wlWmNoticeInService.getTodoCount(storageId);
			outTotal = wlWmNoticeOutService.getTodoCount(storageId);
			allTotal = checkTotal + inTotal + outTotal;
			resultMap.put("noticeAllTotal", allTotal);
			resultMap.put("noticeCheckTotal", checkTotal);
			resultMap.put("noticeInTotal", inTotal);
			resultMap.put("noticeOutTotal", outTotal);
			return this.getJson(true, resultMap);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 入库通知单查询（分页）
	 * @param storageId 仓库ID
	 * @param currPage 当前页
	 * @param pageSize 页大小
	 * @return 分页结果
	 */
	@RequestMapping(params = "action=searchForIn")
	@ResponseBody
	public String searchForIn(int currPage, int pageSize, String storageId) {
		try {
			Map paraMap = new HashMap();
			List resultList = new ArrayList();
			List<Type> typeList = wlCmTypeService.findByCode("WM_IN_TYPE_EK");
			paraMap.put("storageId", storageId);
			paraMap.put("billStateEk", "ISSUE");
			paraMap.put("resultEk", "NOT_EXECUTE");
			Page page = wlWmNoticeInService.search(currPage, pageSize, paraMap);
			List<WlWmNoticeIn> list = page.getItems();
			// 循环，转为map
			for (WlWmNoticeIn wlWmNoticeIn : list) {
				Map resultMap = new HashMap();
				resultMap.put("noticeId", wlWmNoticeIn.getNoticeInId());
				resultMap.put("noticeNo", wlWmNoticeIn.getNoticeNo());
				// 设置业务类型枚举
				for (Type type : typeList) {
					if (wlWmNoticeIn.getWmInTypeEk().equals(type.getId())) {
						resultMap.put("typeEk", type.getLable());
						break;
					}
				}
				// 设置货品名称
				resultMap.put("itemName", wlWmNoticeInService.getAllItemName(wlWmNoticeIn.getNoticeInId(), 1));
				resultMap.put("storageName", wlWmNoticeIn.getStorageName());
				resultMap.put("totalQty", wlWmNoticeIn.getTotalQty());
				resultList.add(resultMap);
			}
			return this.getJson(true, new Page(resultList, page.getTotalCount()));
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 出库通知单查询（分页）
	 * @param storageId 仓库ID
	 * @param currPage 当前页
	 * @param pageSize 页大小
	 * @return 分页结果
	 */
	@RequestMapping(params = "action=searchForOut")
	@ResponseBody
	public String searchForOut(int currPage, int pageSize, String storageId) {
		try {
			Map paraMap = new HashMap();
			List resultList = new ArrayList();
			List<Type> typeList = wlCmTypeService.findByCode("WM_OUT_TYPE_EK");
			paraMap.put("storageId", storageId);
			paraMap.put("billStateEk", "ISSUE");
			paraMap.put("resultEk", "NOT_EXECUTE");
			Page page = wlWmNoticeOutService.search(currPage, pageSize, paraMap);
			List<WlWmNoticeOut> list = page.getItems();
			// 循环，转为map
			for (WlWmNoticeOut wlWmNoticeOut : list) {
				Map resultMap = new HashMap();
				resultMap.put("noticeId", wlWmNoticeOut.getNoticeOutId());
				resultMap.put("noticeNo", wlWmNoticeOut.getNoticeNo());
				// 设置业务类型枚举
				for (Type type : typeList) {
					if (wlWmNoticeOut.getWmOutTypeEk().equals(type.getId())) {
						resultMap.put("typeEk", type.getLable());
						break;
					}
				}
				// 设置货品名称
				resultMap.put("itemName", wlWmNoticeOutService.getAllItemName(wlWmNoticeOut.getNoticeOutId(), 1));
				resultMap.put("storageName", wlWmNoticeOut.getStorageName());
				resultMap.put("totalQty", wlWmNoticeOut.getTotalQty());
				resultMap.put("consignee", wlWmNoticeOut.getConsignee() + "(" + wlWmNoticeOut.getContactWay() + ")");
				resultList.add(resultMap);
			}
			return this.getJson(true, new Page(resultList, page.getTotalCount()));
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 出库通知单查询（分页）按型号分组
	 * @param storageId 仓库ID
	 * @param expectOutDate 预出库日期
	 * @param spec 型号
	 * @param currPage 当前页
	 * @param pageSize 页大小
	 * @param categoryId 物料类别id，多个用逗号隔开
	 * @return 分页结果
	 */
	@RequestMapping(params = "action=searchSpecForOut")
	@ResponseBody
	public String searchSpecForOut(int currPage, int pageSize, String storageId, String expectOutDate, String spec, String categoryId) {
		try {
			Map paraMap = new HashMap();
			List resultList = new ArrayList();
			List<Type> typeList = wlCmTypeService.findByCode("WM_OUT_TYPE_EK");
			paraMap.put("storageId", storageId);
			paraMap.put("expectOutDate", expectOutDate);
			paraMap.put("spec", spec);
			paraMap.put("billStateEk", "ISSUE");
			// paraMap.put("resultEk", "NOT_EXECUTE");
			Page page = wlWmNoticeOutService.searchSpec(currPage, pageSize, paraMap, categoryId);
			List<Object[]> list = page.getItems();
			// 循环，转为map
			for (Object[] obj : list) {
				Map resultMap = new HashMap();
				resultMap.put("noticeId", obj[0]);
				resultMap.put("noticeNo", obj[1]);
				resultMap.put("storageName", (String) obj[2]);
				// 设置货品名称
				resultMap.put("itemName", (String) obj[4] + "(" + (String) obj[5] + ")");
				BigDecimal totalQty = (BigDecimal) obj[6];
				resultMap.put("totalQty", totalQty.doubleValue());
				resultMap.put("consignee", (String) obj[7]);
				resultMap.put("contactWay", (String) obj[8]);
				resultMap.put("storeOutId", (String) obj[9]);
				BigDecimal hasBaseQty = (BigDecimal) obj[10];
				resultMap.put("hasBaseQty", hasBaseQty == null ? 0 : hasBaseQty.doubleValue());
				resultMap.put("noticeOutDetlId", (String) obj[11]);
				resultMap.put("storeOutDetlId", (String) obj[12]);
				resultList.add(resultMap);
			}
			return this.getJson(true, new Page(resultList, page.getTotalCount()));
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 盘点通知单查询（分页）
	 * @param storageId 仓库ID
	 * @param currPage 当前页
	 * @param pageSize 页大小
	 * @return 分页结果
	 */
	@RequestMapping(params = "action=searchForCheck")
	@ResponseBody
	public String searchForCheck(int currPage, int pageSize, String storageId) {
		try {
			Map paraMap = new HashMap();
			List resultList = new ArrayList();
			List<Type> typeList = wlCmTypeService.findByCode("WM_CHECK_TYPE_EK");
			paraMap.put("storageId", storageId);
			paraMap.put("billStateEk", "ISSUE");
			paraMap.put("resultEk", "NOT_EXECUTE");
			Page page = wlWmNoticeCheckService.search(currPage, pageSize, paraMap);
			List<WlWmNoticeCheck> list = page.getItems();
			// 循环，转为map
			for (WlWmNoticeCheck wlWmNoticeCheck : list) {
				Map resultMap = new HashMap();
				resultMap.put("noticeId", wlWmNoticeCheck.getNoticeCheckId());
				resultMap.put("noticeNo", wlWmNoticeCheck.getNoticeNo());
				// 设置业务类型枚举
				for (Type type : typeList) {
					if (wlWmNoticeCheck.getWmCheckTypeEk().equals(type.getId())) {
						resultMap.put("typeEk", type.getLable());
						break;
					}
				}
				// 设置货品名称
				resultMap.put("itemName", wlWmNoticeCheckService.getAllItemName(wlWmNoticeCheck.getNoticeCheckId()));
				resultMap.put("storageName", wlWmNoticeCheck.getStorageName());
				resultMap.put("checkDate", wlWmNoticeCheck.getCheckDate());
				resultList.add(resultMap);
			}
			return this.getJson(true, new Page(resultList, page.getTotalCount()));
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通知单详细信息
	 * @param busType 通知单类型 0-盘点通知单 1-入库通知单 2-出库通知单
	 * @param noticeId 通知单ID
	 * @return 结果
	 */
	@RequestMapping(params = "action=getNoticeData")
	@ResponseBody
	public String getNoticeData(int busType, String noticeId) {
		try {
			Map resultMap = new HashMap();
			List resultList = new ArrayList();
			switch (busType) {
			case 0:// 盘点通知
					// 主表数据
				WlWmNoticeCheck wlWmNoticeCheck = (WlWmNoticeCheck) wlWmNoticeCheckService.getObject(noticeId);
				List<Type> typeList = wlCmTypeService.findByCode("WM_CHECK_TYPE_EK");
				resultMap.put("noticeNo", wlWmNoticeCheck.getNoticeNo());
				for (Type type : typeList) {
					if (wlWmNoticeCheck.getWmCheckTypeEk().equals(type.getId())) {
						resultMap.put("typeEk", type.getLable());
						break;
					}
				}
				resultMap.put("checkDate", wlWmNoticeCheck.getCheckDate());
				resultMap.put("storageName", wlWmNoticeCheck.getStorageName());
				resultMap.put("storageId", wlWmNoticeCheck.getStorageId());
				// 从表数据
				List<WlWmNoticeCheckDetl> checkDtlList = wlWmNoticeCheckService.findNoticeCheckDtlList(noticeId);
				resultList = new ArrayList();
				for (WlWmNoticeCheckDetl wlWmNoticeCheckDetl : checkDtlList) {
					Map map = new HashMap();
					map.put("detlId", wlWmNoticeCheckDetl.getNoticeCheckDetlId());
					map.put("itemName", wlWmNoticeCheckDetl.getItemName());
					map.put("itemNo", wlWmNoticeCheckDetl.getItemCd());
					map.put("spec", wlWmNoticeCheckDetl.getSpec());
					// 设置帐面数量
					WlWmInventory wlWmInventory = wlWmInventoryService.getData(wlWmNoticeCheckDetl.getStorageId(), wlWmNoticeCheckDetl.getItemId());
					if (!ValidateUtil.isNull(wlWmInventory)) {
						map.put("baseUnitQty", wlWmInventory.getBaseUnitQty());
					}
					else {
						map.put("baseUnitQty", wlWmNoticeCheckDetl.getBaseUnitQty());
					}
					map.put("hasUnitQty", 0);
					WlCmItem wlCmItem = (WlCmItem) wlCmItemService.getObject(wlWmNoticeCheckDetl.getItemId());
					map.put("barCd", wlCmItem.getBarCd());
					map.put("isScanFlag", wlCmItem.getIsScanFlag());
					resultList.add(map);
				}
				resultMap.put("items", resultList);
				break;
			case 1:// 入库通知
					// 主表数据
				WlWmNoticeIn wlWmNoticeIn = (WlWmNoticeIn) wlWmNoticeInService.getObject(noticeId);
				List<Type> inTypeList = wlCmTypeService.findByCode("WM_IN_TYPE_EK");
				resultMap.put("noticeNo", wlWmNoticeIn.getNoticeNo());
				for (Type type : inTypeList) {
					if (wlWmNoticeIn.getWmInTypeEk().equals(type.getId())) {
						resultMap.put("typeEk", type.getLable());
						break;
					}
				}
				resultMap.put("storageName", wlWmNoticeIn.getStorageName());
				resultMap.put("storageId", wlWmNoticeIn.getStorageId());
				// 从表数据
				List<WlWmNoticeInDetl> inDtlList = wlWmNoticeInService.findNoticeInDetlList(noticeId);
				resultList = new ArrayList();
				for (WlWmNoticeInDetl wlWmNoticeInDetl : inDtlList) {
					Map map = new HashMap();
					map.put("detlId", wlWmNoticeInDetl.getNoticeInDetlId());
					map.put("itemName", wlWmNoticeInDetl.getItemName());
					map.put("itemNo", wlWmNoticeInDetl.getItemCd());
					map.put("spec", wlWmNoticeInDetl.getSpec());
					map.put("baseUnitQty", wlWmNoticeInDetl.getBaseUnitQty());
					map.put("hasUnitQty", 0);
					WlCmItem wlCmItem = (WlCmItem) wlCmItemService.getObject(wlWmNoticeInDetl.getItemId());
					map.put("barCd", wlCmItem.getBarCd());
					map.put("isScanFlag", wlCmItem.getIsScanFlag());
					resultList.add(map);
				}
				resultMap.put("items", resultList);
				break;
			case 2:// 出库通知
					// 主表数据
				WlWmNoticeOut wlWmNoticeOut = (WlWmNoticeOut) wlWmNoticeOutService.getObject(noticeId);
				List<Type> outTypeList = wlCmTypeService.findByCode("WM_OUT_TYPE_EK");
				resultMap.put("noticeNo", wlWmNoticeOut.getNoticeNo());
				for (Type type : outTypeList) {
					if (wlWmNoticeOut.getWmOutTypeEk().equals(type.getId())) {
						resultMap.put("typeEk", type.getLable());
						break;
					}
				}
				resultMap.put("storageName", wlWmNoticeOut.getStorageName());
				resultMap.put("storageId", wlWmNoticeOut.getStorageId());
				// 从表数据
				List<WlWmNoticeOutDetl> outDtlList = wlWmNoticeOutService.findNoticeOutList(noticeId);
				resultList = new ArrayList();
				for (WlWmNoticeOutDetl wlWmNoticeOutDetl : outDtlList) {
					Map map = new HashMap();
					map.put("detlId", wlWmNoticeOutDetl.getNoticeOutDetlId());
					map.put("itemName", wlWmNoticeOutDetl.getItemName());
					map.put("itemNo", wlWmNoticeOutDetl.getItemCd());
					map.put("spec", wlWmNoticeOutDetl.getSpec());
					map.put("baseUnitQty", wlWmNoticeOutDetl.getBaseUnitQty());
					map.put("hasUnitQty", 0);
					map.put("consignee", wlWmNoticeOutDetl.getConsignee());
					map.put("contactWay", wlWmNoticeOutDetl.getContactWay());
					map.put("addr", wlWmNoticeOutDetl.getAddr());
					WlCmItem wlCmItem = (WlCmItem) wlCmItemService.getObject(wlWmNoticeOutDetl.getItemId());
					map.put("barCd", wlCmItem.getBarCd());
					map.put("isScanFlag", wlCmItem.getIsScanFlag());
					resultList.add(map);
				}
				resultMap.put("items", resultList);
				break;
			}
			return this.getJson(true, resultMap);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 物料详细信息
	 * @param barCd 条形码
	 * @return 结果
	 */
	@RequestMapping(params = "action=getItemByBarCd")
	@ResponseBody
	public String getItemByBarCd(String barCd) {
		try {
			WlCmItem wlCmItem = wlCmItemService.getItemByBarCd(barCd);
			if (!ValidateUtil.isNull(wlCmItem)) {
				Map map = new HashMap();
				map.put("detlId", "");
				map.put("itemName", wlCmItem.getItemName());
				map.put("itemNo", wlCmItem.getItemCd());
				map.put("spec", wlCmItem.getSpec());
				map.put("barCd", wlCmItem.getBarCd());
				map.put("baseUnitQty", 0);
				map.put("hasUnitQty", 0);
				return this.getJson(true, map);
			}
			else {
				return this.getJson(false, Lang.getString("wl.cm.wlCmItemAction.noExistBarCd"));
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 校验序号条形码是否已存在
	 * @param barCd 序号条形码
	 * @param storageId 仓库ID
	 * @param itemNo 物料代码
	 * @return 结果
	 */
	@RequestMapping(params = "action=validateStoreInByBarCd")
	@ResponseBody
	public String validateStoreInByBarCd(String barCd, String storageId, String itemNo) {
		try {
			// WlWmStoreInDetlInfo info = wlWmStoreInService.getInfoBySerialNo(barCd);
			// if (ValidateUtil.isNull(info)) {
			// return this.getJson(true, "");
			// }
			// else {
			// return this.getJson(false, Lang.getString("wl.wm.wlWmAppNoticeTodoAction.existSeqBarCd"));
			// }
			WlWmInventoryDtl wlWmInventoryDtl = wlWmInventoryService.getDataBySerialNo(storageId, itemNo, barCd);
			if (ValidateUtil.isNull(wlWmInventoryDtl) || "0".equals(wlWmInventoryDtl.getIsStore())) {
				return this.getJson(true, "");
			}
			// 序号条形码已入库，不允许重复扫描！
			return this.getJson(false, Lang.getString("wl.wm.wlWmAppNoticeTodoAction.existSeqBarCd"));
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 校验序号条形码是否已存在
	 * @param barCd 序号条形码
	 * @param storageId 仓库ID
	 * @param itemNo 物料编码
	 * @return 结果
	 */
	@RequestMapping(params = "action=validateStoreOutByBarCd")
	@ResponseBody
	public String validateStoreOutByBarCd(String barCd, String storageId, String itemNo) {
		try {
			// WlWmStoreOutDetlInfo info = wlWmStoreOutService.getInfoBySerialNo(barCd);
			// if (ValidateUtil.isNull(info)) {// 序号不存在出库明细信息表中
			// // 判断该序号是否存在入库表中，如存在，则进一步判断入库时扫的序号与当前扫的序号对应的物料编码是否一致，
			// // 如不一致，则提示”该序号所对应货品编码为xxxxx，不允许在非该编码下扫描！“
			// WlWmStoreInDetlInfo inInfo = wlWmStoreInService.getInfoBySerialNo(barCd);
			// if (!ValidateUtil.isNull(inInfo)) {// 在入库中存在
			// WlWmStoreInDetl wlWmStoreInDetl = wlWmStoreInService.getStoreInDetl(inInfo.getStoreInDetlId());
			// if (!wlWmStoreInDetl.getItemCd().equals(itemNo)) {// 入库时扫的序号与当前序号对应的物料编码不一致
			// return this.getJson(false, Lang.getString("wl.wm.wlWmAppNoticeTodoAction.outExistInSeqBarCdHead") + wlWmStoreInDetl.getItemCd() + Lang.getString("wl.wm.wlWmAppNoticeTodoAction.outExistInSeqBarCdFoot"));
			// }
			// }
			// return this.getJson(true, "");
			// }
			// else {
			// return this.getJson(false, Lang.getString("wl.wm.wlWmAppNoticeTodoAction.outExistSeqBarCd"));
			// }

			WlWmInventoryDtl wlWmInventoryDtl = wlWmInventoryService.getDataBySerialNo(storageId, itemNo, barCd);
			if (!ValidateUtil.isNull(wlWmInventoryDtl)) {// 序列号存在,有入库过
				if ("0".equals(wlWmInventoryDtl.getIsStore())) {// 目前不在库中
					return this.getJson(false, Lang.getString("wl.wm.wlWmAppNoticeTodoAction.outExistSeqBarCd"));
				}
				return this.getJson(true, "");
			}
			else {// 序列号不存在
				return this.getJson(false, Lang.getString("wl.wm.wlWmAppNoticeTodoAction.outNoExistSeqBarCd"));
			}

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过序号条码获取其对应物料编码
	 * @param barCd 序号条形码
	 * @return 结果
	 */
	@RequestMapping(params = "action=getItemNoByBarCd")
	@ResponseBody
	public String getItemNoByBarCd(String barCd) {
		try {
			Map resultMap = new HashMap();
			WlWmInventory wlWmInventory = wlWmInventoryService.getDataBySerialNo(barCd);
			if (ValidateUtil.isNull(wlWmInventory)) {
				return this.getJson(false, "");
			}
			else {
				resultMap.put("itemNo", wlWmInventory.getItemCd());
				return this.getJson(true, resultMap);
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}