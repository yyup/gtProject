package com.gt.wl.wm.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joyone.action.BaseAction;
import org.joyone.lang.BaseException;
import org.joyone.spring.Sc;
import org.joyone.sys.Lang;
import org.joyone.sys.Page;
import org.joyone.util.DateUtil;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gt.wl.cm.model.WlCmItem;
import com.gt.wl.cm.service.WlCmItemService;
import com.gt.wl.cm.service.WlCmTypeService;
import com.gt.wl.util.Type;
import com.gt.wl.wm.model.WlWmInventory;
import com.gt.wl.wm.model.WlWmStoreCheck;
import com.gt.wl.wm.model.WlWmStoreCheckDetl;
import com.gt.wl.wm.model.WlWmStoreCheckDetlInfo;
import com.gt.wl.wm.model.WlWmStoreIn;
import com.gt.wl.wm.model.WlWmStoreInDetl;
import com.gt.wl.wm.model.WlWmStoreInDetlInfo;
import com.gt.wl.wm.model.WlWmStoreOut;
import com.gt.wl.wm.model.WlWmStoreOutDetl;
import com.gt.wl.wm.model.WlWmStoreOutDetlInfo;
import com.gt.wl.wm.service.WlWmInventoryService;
import com.gt.wl.wm.service.WlWmStoreCheckService;
import com.gt.wl.wm.service.WlWmStoreInService;
import com.gt.wl.wm.service.WlWmStoreOutService;

/**
 * 单据APP端Action
 * @author liuyj
 * 
 */
@Controller
@RequestMapping("/app/wm/wlWmAppBillAction.do")
public class WlWmAppBillAction extends BaseAction {
	private WlCmTypeService wlCmTypeService = (WlCmTypeService) Sc.getBean("wl.cm.WlCmTypeService");
	private WlCmItemService wlCmItemService = (WlCmItemService) Sc.getBean("wl.cm.WlCmItemService");
	private WlWmStoreInService wlWmStoreInService = (WlWmStoreInService) Sc.getBean("wl.wm.WlWmStoreInService");
	private WlWmStoreOutService wlWmStoreOutService = (WlWmStoreOutService) Sc.getBean("wl.wm.WlWmStoreOutService");
	private WlWmStoreCheckService wlWmStoreCheckService = (WlWmStoreCheckService) Sc.getBean("wl.wm.WlWmStoreCheckService");
	private WlWmInventoryService wlWmInventoryService = (WlWmInventoryService) Sc.getBean("wl.wm.WlWmInventoryService");

	/**
	 * 初始化
	 * @param storageId 仓库ID
	 * @return 今日单据数（盘点单据数、入库单据数、出库单据数
	 */
	@RequestMapping(params = "action=init")
	@ResponseBody
	public String init(String storageId) {
		long inTotal = 0;// 入库单据数
		long outTotal = 0;// 出库单据数
		long checkTotal = 0;// 盘点单据数
		try {
			Map resultMap = new HashMap();
			checkTotal = wlWmStoreCheckService.getTodayBillCount(storageId);
			inTotal = wlWmStoreInService.getTodayBillCount(storageId);
			outTotal = wlWmStoreOutService.getTodayBillCount(storageId);
			resultMap.put("billCheckTotal", checkTotal);
			resultMap.put("billInTotal", inTotal);
			resultMap.put("billOutTotal", outTotal);
			return this.getJson(true, resultMap);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 入库单据查询（分页）
	 * @param storageId 仓库ID
	 * @param beginDate 开始日期
	 * @param endDate 结束日期
	 * @param content 内容
	 * @param currPage 当前页
	 * @param pageSize 页大小
	 * @return 分页结果
	 */
	@RequestMapping(params = "action=searchForIn")
	@ResponseBody
	public String searchForIn(int currPage, int pageSize, String storageId, String beginDate, String endDate, String content) {
		try {
			Map paraMap = new HashMap();
			List resultList = new ArrayList();
			List<Type> typeList = wlCmTypeService.findByCode("WM_IN_TYPE_EK");
			paraMap.put("storageId", storageId);
			paraMap.put("beginDate", beginDate);
			paraMap.put("endDate", endDate);
			paraMap.put("billNo", content);
			Page page = wlWmStoreInService.search("wlWmStoreIn", paraMap, pageSize, currPage);
			List<WlWmStoreIn> list = page.getItems();
			// 循环，转为map
			for (WlWmStoreIn wlWmStoreIn : list) {
				Map resultMap = new HashMap();
				resultMap.put("billId", wlWmStoreIn.getStoreInId());
				resultMap.put("billNo", wlWmStoreIn.getBillNo());
				resultMap.put("lockFlag", wlWmStoreIn.getLockFlagEk());
				// 设置业务类型枚举
				for (Type type : typeList) {
					if (wlWmStoreIn.getWmInTypeEk().equals(type.getId())) {
						resultMap.put("typeEk", type.getLable());
						break;
					}
				}
				// 设置货品名称
				resultMap.put("itemName", wlWmStoreInService.getAllItemName(wlWmStoreIn.getStoreInId(), 1));
				resultMap.put("storageName", wlWmStoreIn.getStorageName());
				resultMap.put("totalQty", wlWmStoreIn.getTotalQty());
				resultList.add(resultMap);
			}
			return this.getJson(true, new Page(resultList, page.getTotalCount()));
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 出库单据查询（分页）
	 * @param storageId 仓库ID
	 * @param beginDate 开始日期
	 * @param endDate 结束日期
	 * @param content 内容
	 * @param currPage 当前页
	 * @param pageSize 页大小
	 * @return 分页结果
	 */
	@RequestMapping(params = "action=searchForOut")
	@ResponseBody
	public String searchForOut(int currPage, int pageSize, String storageId, String beginDate, String endDate, String content) {
		try {
			Map paraMap = new HashMap();
			List resultList = new ArrayList();
			List<Type> typeList = wlCmTypeService.findByCode("WM_OUT_TYPE_EK");
			paraMap.put("storageId", storageId);
			paraMap.put("beginDate", beginDate);
			paraMap.put("endDate", endDate);
			paraMap.put("billNo", content);
			Page page = wlWmStoreOutService.search("wlWmStoreOut", paraMap, pageSize, currPage);
			List<WlWmStoreOut> list = page.getItems();
			// 循环，转为map
			for (WlWmStoreOut wlWmStoreOut : list) {
				Map resultMap = new HashMap();
				resultMap.put("billId", wlWmStoreOut.getStoreOutId());
				resultMap.put("billNo", wlWmStoreOut.getBillNo());
				resultMap.put("lockFlag", wlWmStoreOut.getLockFlagEk());
				// 设置业务类型枚举
				for (Type type : typeList) {
					if (wlWmStoreOut.getWmOutTypeEk().equals(type.getId())) {
						resultMap.put("typeEk", type.getLable());
						break;
					}
				}
				// 设置货品名称
				resultMap.put("itemName", wlWmStoreOutService.getAllItemName(wlWmStoreOut.getStoreOutId(), 1));
				resultMap.put("storageName", wlWmStoreOut.getStorageName());
				resultMap.put("totalQty", wlWmStoreOut.getTotalQty());
				resultList.add(resultMap);
			}
			return this.getJson(true, new Page(resultList, page.getTotalCount()));
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 盘点单据查询（分页）
	 * @param storageId 仓库ID
	 * @param beginDate 开始日期
	 * @param endDate 结束日期
	 * @param content 内容
	 * @param currPage 当前页
	 * @param pageSize 页大小
	 * @return 分页结果
	 */
	@RequestMapping(params = "action=searchForCheck")
	@ResponseBody
	public String searchForCheck(int currPage, int pageSize, String storageId, String beginDate, String endDate, String content) {
		try {
			Map paraMap = new HashMap();
			List resultList = new ArrayList();
			List<Type> typeList = wlCmTypeService.findByCode("WM_CHECK_TYPE_EK");
			paraMap.put("storageId", storageId);
			paraMap.put("beginDate", beginDate);
			paraMap.put("endDate", endDate);
			paraMap.put("billNo", content);
			Page page = wlWmStoreCheckService.search(currPage, pageSize, paraMap);
			List<WlWmStoreCheck> list = page.getItems();
			// 循环，转为map
			for (WlWmStoreCheck wlWmStoreCheck : list) {
				Map resultMap = new HashMap();
				resultMap.put("billId", wlWmStoreCheck.getStoreCheckId());
				resultMap.put("billNo", wlWmStoreCheck.getBillNo());
				resultMap.put("lockFlag", wlWmStoreCheck.getLockFlagEk());
				// 设置业务类型枚举
				for (Type type : typeList) {
					if (wlWmStoreCheck.getWmCheckTypeEk().equals(type.getId())) {
						resultMap.put("typeEk", type.getLable());
						break;
					}
				}
				// 获取实盘总数量
				double totalQty = wlWmStoreCheckService.getTotalQty(wlWmStoreCheck.getStoreCheckId());
				resultMap.put("totalQty", totalQty);
				// 设置货品名称
				resultMap.put("itemName", wlWmStoreCheckService.getAllItemName(wlWmStoreCheck.getStoreCheckId(), 1));
				resultMap.put("storageName", wlWmStoreCheck.getStorageName());
				resultMap.put("checkDate", wlWmStoreCheck.getCheckDate());

				resultList.add(resultMap);
			}
			return this.getJson(true, new Page(resultList, page.getTotalCount()));
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 单据详细信息
	 * @param busType 单据类型 0-盘点单 1-入库单 2-出库单
	 * @param billId 单据ID
	 * @return 结果
	 */
	@RequestMapping(params = "action=getBillData")
	@ResponseBody
	public String getBillData(int busType, String billId) {
		try {
			Map resultMap = new HashMap();
			List resultList = new ArrayList();
			List resultSeqList = new ArrayList();
			switch (busType) {
			case 0:// 盘点单
					// 主表数据
				WlWmStoreCheck wlWmStoreCheck = (WlWmStoreCheck) wlWmStoreCheckService.getObject(billId);
				List<Type> typeList = wlCmTypeService.findByCode("WM_CHECK_TYPE_EK");
				resultMap.put("billNo", wlWmStoreCheck.getBillNo());
				for (Type type : typeList) {
					if (wlWmStoreCheck.getWmCheckTypeEk().equals(type.getId())) {
						resultMap.put("typeEk", type.getLable());
						break;
					}
				}
				resultMap.put("checkDate", wlWmStoreCheck.getCheckDate());
				resultMap.put("storageName", wlWmStoreCheck.getStorageName());
				resultMap.put("storageId", wlWmStoreCheck.getStorageId());
				// 从表数据
				List<WlWmStoreCheckDetl> checkDtlList = wlWmStoreCheckService.findCheckDetlList(billId);
				resultList = new ArrayList();
				for (WlWmStoreCheckDetl wlWmStoreCheckDetl : checkDtlList) {
					Map map = new HashMap();
					map.put("detlId", wlWmStoreCheckDetl.getStoreCheckDetlId());
					map.put("itemName", wlWmStoreCheckDetl.getItemName());
					map.put("itemNo", wlWmStoreCheckDetl.getItemCd());
					map.put("spec", wlWmStoreCheckDetl.getSpec());
					// 设置帐面数量
					map.put("baseUnitQty", wlWmStoreCheckDetl.getBookBaseQty());
					// 设置盘点数量
					map.put("hasUnitQty", wlWmStoreCheckDetl.getCheckBaseQty());
					WlCmItem wlCmItem = (WlCmItem) wlCmItemService.getObject(wlWmStoreCheckDetl.getItemId());
					map.put("barCd", wlCmItem.getBarCd());
					map.put("isScanFlag", wlCmItem.getIsScanFlag());

					// 获取从表明细(序号)数据
					List<WlWmStoreCheckDetlInfo> infoList = wlWmStoreCheckService.findDetlInfoList(wlWmStoreCheckDetl.getStoreCheckDetlId());
					map.put("hasSeqQty", infoList.size());
					for (WlWmStoreCheckDetlInfo wlWmStoreCheckDetlInfo : infoList) {
						Map seqMap = new HashMap();
						seqMap.put("itemNo", wlWmStoreCheckDetl.getItemCd());
						seqMap.put("serialNo", wlWmStoreCheckDetlInfo.getSerialNo());
						resultSeqList.add(seqMap);
					}
					resultList.add(map);
				}
				resultMap.put("items", resultList);
				resultMap.put("seqItems", resultSeqList);
				break;
			case 1:// 入库单
					// 主表数据
				WlWmStoreIn wlWmStoreIn = (WlWmStoreIn) wlWmStoreInService.getObject(billId);
				List<Type> inTypeList = wlCmTypeService.findByCode("WM_IN_TYPE_EK");
				resultMap.put("billNo", wlWmStoreIn.getBillNo());
				for (Type type : inTypeList) {
					if (wlWmStoreIn.getWmInTypeEk().equals(type.getId())) {
						resultMap.put("typeEk", type.getLable());
						break;
					}
				}
				resultMap.put("date", DateUtil.dateToShortDateStr(wlWmStoreIn.getInDate()));
				resultMap.put("storageName", wlWmStoreIn.getStorageName());
				resultMap.put("storageId", wlWmStoreIn.getStorageId());
				// 从表数据
				List<WlWmStoreInDetl> inDtlList = wlWmStoreInService.findWlWmStoreInDetlList(billId);
				resultList = new ArrayList();
				for (WlWmStoreInDetl wlWmStoreInDetl : inDtlList) {
					Map map = new HashMap();
					map.put("detlId", wlWmStoreInDetl.getStoreInDetlId());
					map.put("itemName", wlWmStoreInDetl.getItemName());
					map.put("itemNo", wlWmStoreInDetl.getItemCd());
					map.put("spec", wlWmStoreInDetl.getSpec());
					// 通知数量
					map.put("baseUnitQty", wlWmStoreInDetl.getNoticeBaseQty());
					// 已入数量
					map.put("hasUnitQty", wlWmStoreInDetl.getBaseUnitQty());
					WlCmItem wlCmItem = (WlCmItem) wlCmItemService.getObject(wlWmStoreInDetl.getItemId());
					map.put("barCd", wlCmItem.getBarCd());
					map.put("isScanFlag", wlCmItem.getIsScanFlag());
					// 获取从表明细(序号)数据
					List<WlWmStoreInDetlInfo> infoList = wlWmStoreInService.findDetlInfoList(wlWmStoreInDetl.getStoreInDetlId());
					map.put("hasSeqQty", infoList.size());
					for (WlWmStoreInDetlInfo wlWmStoreInDetlInfo : infoList) {
						Map seqMap = new HashMap();
						seqMap.put("detlId", wlWmStoreInDetl.getStoreInDetlId());
						seqMap.put("itemNo", wlWmStoreInDetl.getItemCd());
						seqMap.put("serialNo", wlWmStoreInDetlInfo.getSerialNo());
						resultSeqList.add(seqMap);
					}
					resultList.add(map);
				}
				resultMap.put("items", resultList);
				resultMap.put("seqItems", resultSeqList);
				break;
			case 2:// 出库单
					// 主表数据
				WlWmStoreOut wlWmStoreOut = (WlWmStoreOut) wlWmStoreOutService.getObject(billId);
				List<Type> outTypeList = wlCmTypeService.findByCode("WM_OUT_TYPE_EK");
				resultMap.put("billNo", wlWmStoreOut.getBillNo());
				for (Type type : outTypeList) {
					if (wlWmStoreOut.getWmOutTypeEk().equals(type.getId())) {
						resultMap.put("typeEk", type.getLable());
						break;
					}
				}
				resultMap.put("date", DateUtil.dateToShortDateStr(wlWmStoreOut.getOutDate()));
				resultMap.put("storageName", wlWmStoreOut.getStorageName());
				resultMap.put("storageId", wlWmStoreOut.getStorageId());
				// 从表数据
				List<WlWmStoreOutDetl> outDtlList = wlWmStoreOutService.findWlWmStoreOutDetlList(billId, "", "");
				resultList = new ArrayList();
				for (WlWmStoreOutDetl wlWmStoreOutDetl : outDtlList) {
					Map map = new HashMap();
					map.put("detlId", wlWmStoreOutDetl.getStoreOutDetlId());
					map.put("itemName", wlWmStoreOutDetl.getItemName());
					map.put("itemNo", wlWmStoreOutDetl.getItemCd());
					map.put("spec", wlWmStoreOutDetl.getSpec());
					// 通知数量
					map.put("baseUnitQty", wlWmStoreOutDetl.getNoticeBaseQty());
					// 已出数量
					map.put("hasUnitQty", wlWmStoreOutDetl.getBaseUnitQty());
					WlCmItem wlCmItem = (WlCmItem) wlCmItemService.getObject(wlWmStoreOutDetl.getItemId());
					map.put("barCd", wlCmItem.getBarCd());
					map.put("isScanFlag", wlCmItem.getIsScanFlag());
					// 获取从表明细(序号)数据
					List<WlWmStoreOutDetlInfo> infoList = wlWmStoreOutService.findDetlInfoList(wlWmStoreOutDetl.getStoreOutDetlId());
					map.put("hasSeqQty", infoList.size());
					for (WlWmStoreOutDetlInfo wlWmStoreOutDetlInfo : infoList) {
						Map seqMap = new HashMap();
						seqMap.put("detlId", wlWmStoreOutDetlInfo.getStoreOutDetlId());
						seqMap.put("itemNo", wlWmStoreOutDetl.getItemCd());
						seqMap.put("serialNo", wlWmStoreOutDetlInfo.getSerialNo());
						resultSeqList.add(seqMap);
					}
					resultList.add(map);
				}
				resultMap.put("items", resultList);
				resultMap.put("seqItems", resultSeqList);
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
	 * @return 结果
	 */
	@RequestMapping(params = "action=validateStoreInByBarCd")
	@ResponseBody
	public String validateStoreInByBarCd(String barCd) {
		try {
			WlWmStoreInDetlInfo info = wlWmStoreInService.getInfoBySerialNo(barCd);
			if (ValidateUtil.isNull(info)) {
				return this.getJson(true, "");
			}
			else {
				return this.getJson(false, Lang.getString("wl.wm.wlWmAppNoticeTodoAction.existSeqBarCd"));
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 校验序号条形码是否已存在
	 * @param barCd 序号条形码
	 * @return 结果
	 */
	@RequestMapping(params = "action=validateStoreOutByBarCd")
	@ResponseBody
	public String validateStoreOutByBarCd(String barCd) {
		try {
			WlWmStoreOutDetlInfo info = wlWmStoreOutService.getInfoBySerialNo(barCd);
			if (ValidateUtil.isNull(info)) {
				return this.getJson(true, "");
			}
			else {
				return this.getJson(false, Lang.getString("wl.wm.wlWmAppNoticeTodoAction.outExistSeqBarCd"));
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
