package com.gt.wl.wm.action;

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
import com.gt.wl.cm.model.WlCmStorage;
import com.gt.wl.cm.service.WlCmItemService;
import com.gt.wl.cm.service.WlCmStorageService;
import com.gt.wl.wm.model.WlWmInventory;
import com.gt.wl.wm.service.WlWmInventoryService;

/**
 * 库存管理APP端Action
 * @author liuyj
 * 
 */
@Controller
@RequestMapping("/app/wm/wlWmAppInventoryAction.do")
public class WlWmAppInventoryAction extends BaseAction {
	private final static String SYSTEM_NAME = Lang.getString("wl.wm.SYSTEM_NAME");
	private final static String MODULE_NAME = Lang.getString("wl.wm.wlWmInventoryAction.MODULE_NAME");
	private WlWmInventoryService wlWmInventoryService = (WlWmInventoryService) Sc.getBean("wl.wm.WlWmInventoryService");
	private WlCmStorageService wlCmStorageService = (WlCmStorageService) Sc.getBean("wl.cm.WlCmStorageService");
	private WlCmItemService wlCmItemService = (WlCmItemService) Sc.getBean("wl.cm.WlCmItemService");

	/**
	 * 初始化
	 * @param storeId 仓库ID
	 * @return 默认仓库及其货品种数、库存过多件数、库存不足件数
	 */
	@RequestMapping(params = "action=init")
	@ResponseBody
	public String init(String storeId) {
		String storageId = "";
		String storageName = "请选择仓库";
		WlCmStorage wlCmStorage = null;
		int itemTotal = 0;// 货品种类统计
		int muchTotal = 0;// 库存过多统计
		int lowTotal = 0;// 库存不足统计
		try {
			Map resultMap = new HashMap();
			if (ValidateUtil.isEmpty(storeId)) {// 未选仓库
				// 取默认进货或出库仓库
				wlCmStorage = wlCmStorageService.getDefaultStorage();
				if (!ValidateUtil.isNull(wlCmStorage)) {// 有默认仓库
					storageId = wlCmStorage.getStorageId();
					storageName = wlCmStorage.getStorageName();
					// 统计
					Map map = wlWmInventoryService.getMuchAndLowTotalByStorageId(storageId);
					itemTotal = Integer.parseInt(map.get("itemTotal").toString());
					muchTotal = Integer.parseInt(map.get("muchTotal").toString());
					lowTotal = Integer.parseInt(map.get("lowTotal").toString());
				}
			}
			else {// 已选仓库
				wlCmStorage = (WlCmStorage) wlCmStorageService.getObject(storeId);
				storageId = wlCmStorage.getStorageId();
				storageName = wlCmStorage.getStorageName();
				// 统计
				Map map = wlWmInventoryService.getMuchAndLowTotalByStorageId(storageId);
				itemTotal = Integer.parseInt(map.get("itemTotal").toString());
				muchTotal = Integer.parseInt(map.get("muchTotal").toString());
				lowTotal = Integer.parseInt(map.get("lowTotal").toString());
			}
			resultMap.put("storageId", storageId);// 仓库ID
			resultMap.put("storageName", storageName);// 仓库名称
			resultMap.put("itemTotal", itemTotal);// 货品种类统计
			resultMap.put("muchTotal", muchTotal);// 库存过多统计
			resultMap.put("lowTotal", lowTotal);// 库存不足统计
			return this.getJson(true, resultMap);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取仓库列表
	 * @return 仓库列表
	 */
	@RequestMapping(params = "action=findStorageList")
	@ResponseBody
	public String findStorageList() {
		try {
			List<Map> resultList = wlCmStorageService.findStorageList();
			return this.getJson(true, resultList);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 搜索库存物料
	 * @param pageSize 页大小
	 * @param currPage 当前页数
	 * @param storageId 仓库ID
	 * @param content 查询内容(物料名称、编码、规格)
	 * @return 物料列表
	 */
	@RequestMapping(params = "action=search")
	@ResponseBody
	public String search(int pageSize, int currPage, String storageId, String content) {
		try {
			Page page = wlWmInventoryService.search(currPage, pageSize, storageId, content);
			return this.getJson(true, page);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 搜索库存过多物料
	 * @param pageSize 页大小
	 * @param currPage 当前页数
	 * @param storageId 仓库ID
	 * @param content 查询内容(物料名称、编码、规格)
	 * @return 物料列表
	 */
	@RequestMapping(params = "action=searchMuchStore")
	@ResponseBody
	public String searchMuchStore(int pageSize, int currPage, String storageId, String content) {
		try {
			Page page = wlWmInventoryService.searchMuchStore(currPage, pageSize, storageId, content);
			return this.getJson(true, page);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 搜索库存不足物料
	 * @param pageSize 页大小
	 * @param currPage 当前页数
	 * @param storageId 仓库ID
	 * @param content 查询内容(物料名称、编码、规格)
	 * @return 物料列表
	 */
	@RequestMapping(params = "action=searchLowerStore")
	@ResponseBody
	public String searchLowerStore(int pageSize, int currPage, String storageId, String content) {
		try {
			Page page = wlWmInventoryService.searchLowerStore(currPage, pageSize, storageId, content);
			return this.getJson(true, page);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取库存物料详细
	 * @param inventoryId 库存ID
	 * @return 库存物料详细
	 */
	@RequestMapping(params = "action=getData")
	@ResponseBody
	public String getData(String inventoryId) {
		Map map = new HashMap();
		try {
			WlWmInventory wlWmInventory = (WlWmInventory) wlWmInventoryService.getData(inventoryId);
			map.put("itemCd", wlWmInventory.getItemCd());
			map.put("itemName", wlWmInventory.getItemName());
			map.put("spec", wlWmInventory.getSpec());
			map.put("categoryName", wlWmInventory.getCategoryName());
			map.put("baseUnitQty", wlWmInventory.getBaseUnitQty());
			map.put("baseUnitName", wlWmInventory.getBaseUnitName());
			map.put("upperLimit", wlWmInventory.getUpperLimit());
			map.put("lowerLimit", wlWmInventory.getLowerLimit());
			map.put("storageName", wlWmInventory.getStorageName());
			WlCmItem item = (WlCmItem) wlCmItemService.getObject(wlWmInventory.getItemId());
			map.put("barCd", item.getBarCd());
			return this.getJson(true, map);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}
