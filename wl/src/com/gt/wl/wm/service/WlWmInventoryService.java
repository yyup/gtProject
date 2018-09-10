package com.gt.wl.wm.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joyone.lang.BaseException;
import org.joyone.service.BaseService;
import org.joyone.spring.Sc;
import org.joyone.sys.Page;
import org.joyone.util.DateUtil;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Service;

import com.gt.wl.cm.model.WlCmItem;
import com.gt.wl.cm.service.WlCmCategoryService;
import com.gt.wl.cm.service.WlCmItemService;
import com.gt.wl.es.model.WlEsTodoMsg;
import com.gt.wl.es.service.WlEsTodoMsgService;
import com.gt.wl.util.CommonConf;
import com.gt.wl.wm.dao.WlWmInventoryDao;
import com.gt.wl.wm.dao.WlWmInventoryDtlDao;
import com.gt.wl.wm.model.WlWmInventory;
import com.gt.wl.wm.model.WlWmInventoryDtl;

/**
 * 库存管理Service层
 * @author liuyj
 * 
 */
@Service("wl.wm.WlWmInventoryService")
public class WlWmInventoryService extends BaseService {
	private WlWmInventoryDao wlWmInventoryDao = (WlWmInventoryDao) Sc.getBean("wl.wm.WlWmInventoryDao");
	private WlWmInventoryDtlDao wlWmInventoryDtlDao = (WlWmInventoryDtlDao) Sc.getBean("wl.wm.WlWmInventoryDtlDao");
	private WlCmItemService wlCmItemService = (WlCmItemService) Sc.getBean("wl.cm.WlCmItemService");
	private WlCmCategoryService wlCmCategoryService = (WlCmCategoryService) Sc.getBean("wl.cm.WlCmCategoryService");

	public WlWmInventoryService() {
		baseDao = wlWmInventoryDao;
	}

	/**
	 * 通过仓库ID获取其下物料总数、库存过多物料数、库存不足物料数
	 * @param storageId 仓库ID
	 * @return map对象（物料总数、库存过多物料数、库存不足物料数）
	 */
	public Map getMuchAndLowTotalByStorageId(String storageId) {
		Map map = new HashMap();
		try {
			// 物料总数
			map.put("itemTotal", wlWmInventoryDao.getItemTotalByStorageId(storageId));
			// 库存过多
			map.put("muchTotal", wlWmInventoryDao.getMuchTotalByStorageId(storageId));
			// 库存不足
			map.put("lowTotal", wlWmInventoryDao.getLowTotalByStorageId(storageId));
			return map;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询库存物料分页数据
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param storageId 仓库ID
	 * @param content 查询内容
	 * @return 分页结果
	 */
	public Page search(int currPage, int pageSize, String storageId, String content) {
		try {
			return wlWmInventoryDao.search(currPage, pageSize, storageId, content);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询库存物料分页数据
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param paraMap 条件
	 * @return 分页结果
	 */
	public Page searchInventory(int currPage, int pageSize, Map paraMap) {
		try {
			return wlWmInventoryDao.searchInventory(currPage, pageSize, paraMap);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询库存物料数据
	 * @param storageId 仓库ID
	 * @param content 查询内容
	 * @param storageAreaId 库区id
	 * @return 结果
	 */
	public List findInventoryList(String storageId, String content, String storageAreaId) {
		try {
			return wlWmInventoryDao.findInventoryList(storageId, content, storageAreaId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询库存过多物料分页数据
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param storageId 仓库ID
	 * @param content 查询内容
	 * @return 分页结果
	 */
	public Page searchMuchStore(int currPage, int pageSize, String storageId, String content) {
		try {
			return wlWmInventoryDao.searchMuchStore(currPage, pageSize, storageId, content);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询库存不足物料分页数据
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param storageId 仓库ID
	 * @param content 查询内容
	 * @return 分页结果
	 */
	public Page searchLowerStore(int currPage, int pageSize, String storageId, String content) {
		try {
			return wlWmInventoryDao.searchLowerStore(currPage, pageSize, storageId, content);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取库存货品详情
	 * @param inventoryId 库存ID
	 * @return 库存对象
	 */
	public WlWmInventory getData(String inventoryId) {
		WlWmInventory wlWmInventory = null;
		List<String> categoryNameList = new ArrayList<String>();
		try {
			// 获取库存对象
			wlWmInventory = (WlWmInventory) wlWmInventoryDao.getObject(inventoryId);
			// 通过物料ID获取其库存上限、库存下限、类别
			WlCmItem wlCmItem = (WlCmItem) wlCmItemService.getObject(wlWmInventory.getItemId());
			wlWmInventory.setUpperLimit(wlCmItem.getUpperLimit());
			wlWmInventory.setLowerLimit(wlCmItem.getLowerLimit());
			wlCmCategoryService.getAllParentCategoryName(wlCmItem.getCategoryId(), categoryNameList);
			String categoryName = "";
			for (String str : categoryNameList) {
				categoryName = str + "/" + categoryName;
			}
			wlWmInventory.setCategoryName(categoryName);
			return wlWmInventory;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取库存货品详情(通过仓库ID及物料ID)
	 * @param storageId 仓库ID
	 * @param itemId 物料ID
	 * @return 库存对象，无则返回null
	 */
	public WlWmInventory getData(String storageId, String itemId) {
		WlWmInventory wlWmInventory = null;
		try {
			// 获取库存对象
			wlWmInventory = (WlWmInventory) wlWmInventoryDao.getData(storageId, itemId);
			return wlWmInventory;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 获取库存货品详情(通过序列号)
	 * @param serialNo 序列号
	 * @return 库存对象，无则返回null
	 */
	public WlWmInventory getDataBySerialNo(String serialNo) {
		WlWmInventory wlWmInventory = null;
		try {
			// 获取库存明细（序列号）
			WlWmInventoryDtl wlWmInventoryDtl = wlWmInventoryDtlDao.getDataBySerialNo(serialNo);
			if (ValidateUtil.isNull(wlWmInventoryDtl)) {
				return null;
			}
			// 获取库存对象
			wlWmInventory = (WlWmInventory) wlWmInventoryDao.getObject(wlWmInventoryDtl.getInventoryId());
			return wlWmInventory;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 获取库存明细(通过仓库ID、物料CD、序列号)
	 * @param storageId 仓库ID
	 * @param itemCd 物料代码
	 * @param serialNo 序列号
	 * @return 序号明细对象，无则返回null
	 */
	public WlWmInventoryDtl getDataBySerialNo(String storageId, String itemCd, String serialNo) {
		try {
			return wlWmInventoryDtlDao.getDataBySerialNo(storageId, itemCd, serialNo);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 保存库存数据
	 * @param wlWmInventory 库存主表
	 * @param dtlList 库存明细列表
	 */
	public void saveData(WlWmInventory wlWmInventory, List<WlWmInventoryDtl> dtlList) {
		try {
			// 保存主表
			this.saveObject(wlWmInventory);
			// 保存从表
			for (WlWmInventoryDtl dtl : dtlList) {
				// 判断物料下序列号是否存在，如存在则更新isStore为1，否则新增
				WlWmInventoryDtl wlWmInventoryDtl = wlWmInventoryDtlDao.getData(wlWmInventory.getInventoryId(), dtl.getSerialNo());
				if (ValidateUtil.isNull(wlWmInventoryDtl)) {// 不存在，新增
					dtl.setInventoryId(wlWmInventory.getInventoryId());
					wlWmInventoryDtlDao.saveObject(dtl);
				}
				else {// 存在，更新isStore为1
					wlWmInventoryDtl.setIsStore("1");
					wlWmInventoryDtlDao.saveObject(wlWmInventoryDtl);
				}
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 更改库存数据
	 * @param wlWmInventory 库存主表
	 * @param dtlList 库存明细列表
	 * @param type 0-入库 1-出库
	 */
	public void updateData(WlWmInventory wlWmInventory, List<WlWmInventoryDtl> dtlList, int type) {
		try {
			String[] serialNoArray = new String[dtlList.size()];
			// 保存主表
			this.saveObject(wlWmInventory);
			// int i = 0;
			// // 明细序号组成数组
			// for (WlWmInventoryDtl dtl : dtlList) {
			// serialNoArray[i] = dtl.getSerialNo();
			// i++;
			// }
			// if (serialNoArray.length > 0) {
			// wlWmInventoryDtlDao.updateData(serialNoArray);
			// }
			switch (type) {
			case 0:// 入库
					// 保存从表
				for (WlWmInventoryDtl dtl : dtlList) {
					// 判断物料下序列号是否存在，如存在则更新isStore为1，否则新增
					WlWmInventoryDtl wlWmInventoryDtl = wlWmInventoryDtlDao.getData(wlWmInventory.getInventoryId(), dtl.getSerialNo());
					if (ValidateUtil.isNull(wlWmInventoryDtl)) {// 不存在，新增
						dtl.setInventoryId(wlWmInventory.getInventoryId());
						wlWmInventoryDtlDao.saveObject(dtl);
					}
					else {// 存在，更新isStore为1
						wlWmInventoryDtl.setIsStore("1");
						wlWmInventoryDtlDao.saveObject(wlWmInventoryDtl);
					}
				}
				break;
			case 1:// 出库
					// 保存从表
				for (WlWmInventoryDtl dtl : dtlList) {
					// 判断物料下序列号是否存在，如存在则更新isStore为0
					WlWmInventoryDtl wlWmInventoryDtl = wlWmInventoryDtlDao.getData(wlWmInventory.getInventoryId(), dtl.getSerialNo());
					if (!ValidateUtil.isNull(wlWmInventoryDtl)) {// 存在，更新isStore为0
						wlWmInventoryDtl.setIsStore("0");
						wlWmInventoryDtlDao.saveObject(wlWmInventoryDtl);
					}
				}
				break;
			}

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/***
	 * 将库存不足的保存到待发送消息
	 */
	public void saveToDoMsg() {
		try {
			WlEsTodoMsgService wlEsTodoMsgService = (WlEsTodoMsgService) Sc.getBean("wl.es.WlEsTodoMsgService");
			// List<WlWmInventory> list = this.getObjects();// 获取库存列表
			List<WlWmInventory> list = wlWmInventoryDao.findDataList(CommonConf.storageId, "");// 根据storageId获取翔安仓库库存列表
			for (WlWmInventory wlWmInventory : list) {
				WlCmItem wlCmItem = (WlCmItem) wlCmItemService.getObject(wlWmInventory.getItemId());
				if (wlWmInventory.getBaseUnitQty() < wlCmItem.getLowerLimit()) {// 如果库存数量小于物料下限值
					WlEsTodoMsg wlEsTodoMsg = wlEsTodoMsgService.getWlEsTodoMsg(wlWmInventory.getInventoryId());
					if (ValidateUtil.isNull(wlEsTodoMsg)) {// 如果待发送消息不存在，则创建
						wlEsTodoMsgService.saveToDoMsg(wlWmInventory.getInventoryId(), "INVENTORYNOTICE");
					}
					else {// 待发送消息已存在
						if ("1".equals(wlEsTodoMsg.getMailSend())) {// 如果邮件已发送
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							Date modifyTime = sdf.parse(sdf.format(wlEsTodoMsg.getModifyTime()));// 修改时间
							Date curDate = sdf.parse(sdf.format(new Date()));// 当前时间
							Date threeCurDate = DateUtil.dateAdd(Calendar.DAY_OF_MONTH, -3, curDate);// 3天前
							if (threeCurDate.equals(modifyTime) || threeCurDate.after(modifyTime)) {// 如果邮件在三天或三天前已发送，将其更改为待发送
								wlEsTodoMsg.setMailSend("0");// 将邮件状态更新为未发送
								wlEsTodoMsgService.updateObject(wlEsTodoMsg);
							}
						}
					}

				}
			}

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/***
	 * 统计库存数量
	 * @param paraMap 条件
	 * @return 库存数量
	 */
	public double getTotalQty(Map paraMap) {
		try {
			return wlWmInventoryDao.getTotalQty(paraMap);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/***
	 * 查找库存列表
	 * @param storageId 仓库ID
	 * @param itemId 物料id
	 * @return 库存列表
	 */
	public List<WlWmInventory> findDataList(String storageId, String itemId) {
		try {
			return wlWmInventoryDao.findDataList(storageId, itemId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查找序列号列表
	 * @param inventoryId 库存id
	 * @param serialNo 序列号
	 * @param isStore 是否在库
	 * @return 序列号列表
	 */
	public List<WlWmInventoryDtl> findSerialNoList(String inventoryId, String serialNo, String isStore) {
		try {
			return wlWmInventoryDtlDao.findSerialNoList(inventoryId, serialNo, isStore);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}