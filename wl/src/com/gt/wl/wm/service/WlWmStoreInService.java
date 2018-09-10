package com.gt.wl.wm.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.joyone.lang.BaseException;
import org.joyone.service.BaseService;
import org.joyone.spring.Sc;
import org.joyone.sys.Lang;
import org.joyone.sys.Page;
import org.joyone.util.DateUtil;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Service;

import com.gt.wl.cm.model.WlCmItem;
import com.gt.wl.cm.service.WlCmItemService;
import com.gt.wl.wm.dao.WlWmStoreInDao;
import com.gt.wl.wm.dao.WlWmStoreInDetlDao;
import com.gt.wl.wm.dao.WlWmStoreInDetlInfoDao;
import com.gt.wl.wm.model.WlWmInventory;
import com.gt.wl.wm.model.WlWmInventoryDtl;
import com.gt.wl.wm.model.WlWmNoticeIn;
import com.gt.wl.wm.model.WlWmStoreIn;
import com.gt.wl.wm.model.WlWmStoreInDetl;
import com.gt.wl.wm.model.WlWmStoreInDetlInfo;

@Service("wl.wm.WlWmStoreInService")
public class WlWmStoreInService extends BaseService {
	private WlWmStoreInDao wlWmStoreInDao = (WlWmStoreInDao) Sc.getBean("wl.wm.WlWmStoreInDao");
	private WlWmStoreInDetlDao wlWmStoreInDetlDao = (WlWmStoreInDetlDao) Sc.getBean("wl.wm.WlWmStoreInDetlDao");
	private WlWmStoreInDetlInfoDao wlWmStoreInDetlInfoDao = (WlWmStoreInDetlInfoDao) Sc.getBean("wl.wm.WlWmStoreInDetlInfoDao");
	private WlWmNoticeInService wlWmNoticeInService = (WlWmNoticeInService) Sc.getBean("wl.wm.WlWmNoticeInService");
	private WlCmItemService wlCmItemService = (WlCmItemService) Sc.getBean("wl.cm.WlCmItemService");
	private WlWmInventoryService wlWmInventoryService = (WlWmInventoryService) Sc.getBean("wl.wm.WlWmInventoryService");

	public WlWmStoreInService() {
		baseDao = wlWmStoreInDao;
	}

	/**
	 * 查询入库单列表
	 * @param flag 调用标志(入库单据管理为wlWmStoreIn，入库明细查为wlwmStoreInQuery）
	 * @param billNo 单据编号
	 * @param beginDate 制单开始时间
	 * @param endDate 制单结束时间
	 * @param storageId 仓库id
	 * @param createor 经办人
	 * @param wmInTypeEk 业务类型
	 * @param orgId 往来单位id
	 * @param itemCd 物料编码
	 * @param itemName 物料名称
	 * @param spec 规格型号
	 * @param storageAreaId 库区
	 * @param lockFlagEk 单据状态
	 * @return 入库单列表
	 */
	public List<Map> findStoreInList(String flag, String billNo, String beginDate, String endDate, String storageId, String createor, String wmInTypeEk, String orgId, String itemCd, String itemName, String spec, String storageAreaId,
			String lockFlagEk) {
		try {
			return wlWmStoreInDao.findStoreInList(flag, billNo, beginDate, endDate, storageId, createor, wmInTypeEk, orgId, itemCd, itemName, spec, storageAreaId, lockFlagEk);

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询入库单分页数据
	 * @param flag 调用标志(入库单据管理为wlWmStoreIn，入库明细查为wlwmStoreInQuery）
	 * @param paraMap 条件
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @return 入库单分页数据
	 */
	public Page search(String flag, Map paraMap, int pageSize, int currPage) {
		try {
			return wlWmStoreInDao.search(flag, paraMap, pageSize, currPage);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询入库单从表对象数组
	 * @param storeInId 入库单ID
	 * @return 入库单从表对象数组
	 */
	public List findWlWmStoreInDetlList(String storeInId) {
		try {

			return wlWmStoreInDetlDao.findWlWmStoreInDetlList(storeInId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过入库单从表ID获取其下序号列表
	 * @param storeInDetlId 入库单从表ID
	 * @return 序号列表
	 */
	public List<WlWmStoreInDetlInfo> findDetlInfoList(String storeInDetlId) {
		try {

			return wlWmStoreInDetlInfoDao.findDetlInfoList(storeInDetlId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过入库单id获取其下序列号列表
	 * @param storeInId 入库单ID
	 * @param storeInDetlId 入库单从表id
	 * @return 序列号列表
	 */
	public List<Map> findSerialNoList(String storeInId, String storeInDetlId) {
		try {

			return wlWmStoreInDetlInfoDao.findSerialNoList(storeInId, storeInDetlId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询入库单从表的所有物料名称
	 * @param storeInId 入库单ID
	 * @param flag 调用标志（web端调用为0，APP端调用为1）
	 * @return 物料的名字，格式为"划船器等6种物料"
	 */
	public String getAllItemName(String storeInId, int flag) {
		try {
			String result = "";
			List<WlWmStoreInDetl> list = wlWmStoreInDetlDao.findWlWmStoreInDetlList(storeInId);
			if (!ValidateUtil.isEmpty(list)) {
				if (list.size() == 1) {
					WlWmStoreInDetl wlWmStoreInDetl = list.get(0);
					result += wlWmStoreInDetl.getItemName() + "(" + wlWmStoreInDetl.getSpec() + ")";
				}
				else if (list.size() > 1) {
					WlWmStoreInDetl wlWmStoreInDetl = list.get(0);
					result += wlWmStoreInDetl.getItemName() + "(" + wlWmStoreInDetl.getSpec() + ")" + Lang.getString("wl.wm.WlWmStoreInService.kinds");
					if (flag == 0) {
						result += list.size() + Lang.getString("wl.wm.WlWmStoreInService.items");
					}
					else {
						result += list.size() + Lang.getString("wl.wm.WlWmStoreInService.itemName");
					}
				}
			}
			return result;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过序列号获取对象
	 * @param serialNo 序列号
	 * @return 明细对象（没有则返回null）
	 */
	public WlWmStoreInDetlInfo getInfoBySerialNo(String serialNo) {
		try {
			return wlWmStoreInDetlInfoDao.getInfoBySerialNo(serialNo);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过入库单明细ID获取对象
	 * @param storeInDetlId 入库单明细ID
	 * @return 入库单明细对象
	 */
	public WlWmStoreInDetl getStoreInDetl(String storeInDetlId) {
		try {
			return (WlWmStoreInDetl) wlWmStoreInDetlDao.getObject(storeInDetlId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 入库单据保存
	 * @param wlWmStoreIn 入库单主表
	 * @param detlList 货品数据列表
	 * @param detlInfoList 序号数据列表
	 */
	public void saveData(WlWmStoreIn wlWmStoreIn, List<WlWmStoreInDetl> detlList, List<WlWmStoreInDetlInfo> detlInfoList) {
		try {
			double totalQty = 0;// 入库总数量
			boolean result = true;// 执行结果是否正常，默认正常
			// 通过通知单ID查找通知单(noticeId暂存noticeNo中)
			WlWmNoticeIn wlwmNoticeIn = (WlWmNoticeIn) wlWmNoticeInService.getObject(wlWmStoreIn.getNoticeNo());
			// 入库单主表
			wlWmStoreIn.setNoticeNo(wlwmNoticeIn.getNoticeNo());
			wlWmStoreIn.setBillNo(getNewBillNo());
			wlWmStoreIn.setLockFlagEk("NOLOCK");// 未锁
			// 制单员
			wlWmStoreIn.setCreateTime(new Date());
			wlWmStoreIn.setInDate(DateUtil.dateFormatFromStringToDate(DateUtil.dateToShortDateStr(new Date()), "yyyy-MM-dd"));
			// 仓库
			wlWmStoreIn.setStorageId(wlwmNoticeIn.getStorageId());
			wlWmStoreIn.setStorageName(wlwmNoticeIn.getStorageName());
			wlWmStoreIn.setStorageAreaId(wlwmNoticeIn.getStorageAreaId());
			wlWmStoreIn.setStorageAreaName(wlwmNoticeIn.getStorageAreaName());
			wlWmStoreIn.setSourceStorageId(wlwmNoticeIn.getSourceStorageId());
			wlWmStoreIn.setSourceStorageName(wlwmNoticeIn.getSourceStorageName());
			wlWmStoreIn.setTargetStorageId(wlwmNoticeIn.getTargetStorageId());
			wlWmStoreIn.setTargetStorageName(wlwmNoticeIn.getTargetStorageName());
			// 业务类型
			wlWmStoreIn.setWmInTypeEk(wlwmNoticeIn.getWmInTypeEk());
			// 往来单位
			wlWmStoreIn.setOrgId(wlwmNoticeIn.getOrgId());
			wlWmStoreIn.setOrgName(wlwmNoticeIn.getOrgName());
			wlWmStoreIn.setObjectTypeEk(wlwmNoticeIn.getObjectTypeEk());
			for (WlWmStoreInDetl wlWmStoreInDetl : detlList) {
				wlWmStoreInDetl.setNoticeBaseQty(wlWmStoreInDetl.getBaseUnitQty());// 设置通知数量
				wlWmStoreInDetl.setBaseUnitQty(wlWmStoreInDetl.getHasUnitQty());
				totalQty += wlWmStoreInDetl.getHasUnitQty();
				if (wlWmStoreInDetl.getNoticeBaseQty() != wlWmStoreInDetl.getBaseUnitQty()) {
					result = false;
				}
			}
			wlWmStoreIn.setTotalQty(totalQty);
			// 更改通知单状态
			wlwmNoticeIn.setIsExecutedFlag("1");// 已执行
			if (result) {
				wlwmNoticeIn.setResultEk("NORMAL");// 结果状态为正常
			}
			else {
				wlwmNoticeIn.setResultEk("ABNORMAL");// 结果状态为异常
			}
			wlWmNoticeInService.saveObject(wlwmNoticeIn);
			// 保存主表
			this.saveObject(wlWmStoreIn);
			int sequ = 1;
			// 保存货品明细从表
			for (WlWmStoreInDetl wlWmStoreInDetl : detlList) {
				// 设置主表ID
				wlWmStoreInDetl.setStoreInId(wlWmStoreIn.getStoreInId());
				// 仓库
				wlWmStoreInDetl.setStorageId(wlWmStoreIn.getStorageId());
				wlWmStoreInDetl.setStorageName(wlWmStoreIn.getStorageName());
				// 物料
				WlCmItem item = (WlCmItem) wlCmItemService.getItemByItemCd(wlWmStoreInDetl.getItemCd());
				wlWmStoreInDetl.setItemId(item.getItemId());
				wlWmStoreInDetl.setCategoryId(item.getCategoryId());
				wlWmStoreInDetl.setBaseUnitId(item.getBaseUnitId());
				wlWmStoreInDetl.setBaseUnitName(item.getBaseUnitName());
				wlWmStoreInDetl.setSequ(sequ);
				sequ++;
				// 保存
				wlWmStoreInDetlDao.saveObject(wlWmStoreInDetl);
			}
			// 保存序号明细从表
			for (WlWmStoreInDetlInfo wlWmStoreInDetlInfo : detlInfoList) {
				wlWmStoreInDetlInfo.setSerialNo(wlWmStoreInDetlInfo.getSeqBarCd());
				// 循环货品明细，找到物料编码一致的，挂在其下
				for (WlWmStoreInDetl wlWmStoreInDetl : detlList) {
					if (wlWmStoreInDetlInfo.getItemNo().equals(wlWmStoreInDetl.getItemCd())) {
						wlWmStoreInDetlInfo.setStoreInDetlId(wlWmStoreInDetl.getStoreInDetlId());
						break;
					}
				}
				wlWmStoreInDetlInfoDao.saveObject(wlWmStoreInDetlInfo);
			}

			// 更改库存信息（库存中能找到对应货品，则更改库存量，否则新增一条库存）
			for (WlWmStoreInDetl wlWmStoreInDetl : detlList) {
				List<WlWmInventoryDtl> inventoryDtlList = new ArrayList<WlWmInventoryDtl>();
				// 获取库存主表
				WlWmInventory wlWmInventory = wlWmInventoryService.getData(wlWmStoreInDetl.getStorageId(), wlWmStoreInDetl.getItemId());
				if (ValidateUtil.isNull(wlWmInventory)) {// 库存中不存在该物料，新增一条
					wlWmInventory = new WlWmInventory();
					wlWmInventory.setStorageId(wlWmStoreInDetl.getStorageId());
					wlWmInventory.setStorageName(wlWmStoreInDetl.getStorageName());
					wlWmInventory.setItemId(wlWmStoreInDetl.getItemId());
					wlWmInventory.setItemCd(wlWmStoreInDetl.getItemCd());
					wlWmInventory.setItemName(wlWmStoreInDetl.getItemName());
					wlWmInventory.setSpec(wlWmStoreInDetl.getSpec());
					wlWmInventory.setCategoryId(wlWmStoreInDetl.getCategoryId());
					wlWmInventory.setBaseUnitId(wlWmStoreInDetl.getBaseUnitId());
					wlWmInventory.setBaseUnitName(wlWmStoreInDetl.getBaseUnitName());
					wlWmInventory.setBaseUnitQty(wlWmStoreInDetl.getBaseUnitQty());
					wlWmInventory.setIsEnableFlag("1");
					wlWmInventory.setCreator(wlWmStoreIn.getCreateor());
					wlWmInventory.setCreateTime(new Date());
				}
				else {// 存在-库存量增加
					wlWmInventory.setBaseUnitQty(wlWmInventory.getBaseUnitQty() + wlWmStoreInDetl.getBaseUnitQty());
				}
				// 循环序号列表，找到物料下的序号
				for (WlWmStoreInDetlInfo wlWmStoreInDetlInfo : detlInfoList) {
					if (wlWmStoreInDetlInfo.getStoreInDetlId().equals(wlWmStoreInDetl.getStoreInDetlId())) {
						WlWmInventoryDtl inventoryDtl = new WlWmInventoryDtl();
						inventoryDtl.setIsStore("1");
						inventoryDtl.setSerialNo(wlWmStoreInDetlInfo.getSerialNo());
						inventoryDtlList.add(inventoryDtl);
					}
				}
				// 保存
				wlWmInventoryService.saveData(wlWmInventory, inventoryDtlList);
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 入库单据修改
	 * @param billId 入库单主表ID
	 * @param userName 用户名
	 * @param detlList 货品数据列表
	 * @param detlInfoList 序号数据列表
	 */
	public void updateData(String billId, String userName, List<WlWmStoreInDetl> detlList, List<WlWmStoreInDetlInfo> detlInfoList) {
		try {
			double totalQty = 0;// 入库总数量
			boolean isFlag = true;
			// 通过单据ID查找入库单对象
			WlWmStoreIn wlWmStoreIn = (WlWmStoreIn) this.getObject(billId);

			for (WlWmStoreInDetl wlWmStoreInDetl : detlList) {
				wlWmStoreInDetl.setNoticeBaseQty(wlWmStoreInDetl.getBaseUnitQty());// 设置通知数量
				wlWmStoreInDetl.setBaseUnitQty(wlWmStoreInDetl.getHasUnitQty());
				totalQty += wlWmStoreInDetl.getHasUnitQty();
				// 入库物料中通知数量与已入库数量不一致，则标志置为false
				if (wlWmStoreInDetl.getNoticeBaseQty() != wlWmStoreInDetl.getBaseUnitQty()) {
					isFlag = false;
				}
			}
			wlWmStoreIn.setTotalQty(totalQty);
			// 更改通知单状态
			WlWmNoticeIn wlWmNoticeIn = wlWmNoticeInService.getData(wlWmStoreIn.getNoticeNo());
			if (isFlag) {
				wlWmNoticeIn.setResultEk("NORMAL");// 结果状态为正常
			}
			else {
				wlWmNoticeIn.setResultEk("ABNORMAL");// 结果状态为异常
			}
			wlWmNoticeInService.saveObject(wlWmNoticeIn);
			// 保存主表
			this.saveObject(wlWmStoreIn);
			// 保存货品明细从表
			for (WlWmStoreInDetl wlWmStoreInDetl : detlList) {
				// 判断明细从表是新增或修改
				if (ValidateUtil.isEmpty(wlWmStoreInDetl.getDetlId())) {// 新增货品
					// 设置主表ID
					wlWmStoreInDetl.setStoreInId(wlWmStoreIn.getStoreInId());
					// 仓库
					wlWmStoreInDetl.setStorageId(wlWmStoreIn.getStorageId());
					wlWmStoreInDetl.setStorageName(wlWmStoreIn.getStorageName());
					wlWmStoreInDetl.setModifier(wlWmStoreIn.getCreateor());
					addStoreInDetl(wlWmStoreInDetl, detlInfoList);
				}
				else {// 已有货品
					updateStoreInDetl(wlWmStoreInDetl, detlInfoList);
				}
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 新增入库单明细处理
	 * @param wlWmStoreInDetl 入库单明细
	 * @param detlInfoList 序号列表
	 */
	private void addStoreInDetl(WlWmStoreInDetl wlWmStoreInDetl, List<WlWmStoreInDetlInfo> detlInfoList) {
		try {
			// 物料
			WlCmItem item = (WlCmItem) wlCmItemService.getItemByItemCd(wlWmStoreInDetl.getItemCd());
			wlWmStoreInDetl.setItemId(item.getItemId());
			wlWmStoreInDetl.setCategoryId(item.getCategoryId());
			wlWmStoreInDetl.setBaseUnitId(item.getBaseUnitId());
			wlWmStoreInDetl.setBaseUnitName(item.getBaseUnitName());
			// 1 保存从表
			wlWmStoreInDetlDao.saveObject(wlWmStoreInDetl);

			// 2 循环序号明细从表
			for (WlWmStoreInDetlInfo wlWmStoreInDetlInfo : detlInfoList) {
				wlWmStoreInDetlInfo.setSerialNo(wlWmStoreInDetlInfo.getSeqBarCd());
				// 循环序号明细，找到物料编码与从表一致的，挂在其下
				if (wlWmStoreInDetlInfo.getItemNo().equals(wlWmStoreInDetl.getItemCd())) {
					wlWmStoreInDetlInfo.setStoreInDetlId(wlWmStoreInDetl.getStoreInDetlId());
					// 保存序号
					wlWmStoreInDetlInfoDao.saveObject(wlWmStoreInDetlInfo);
				}
			}

			// 3 更改库存信息（库存中能找到对应货品，则更改库存量，否则新增一条库存）
			List<WlWmInventoryDtl> inventoryDtlList = new ArrayList<WlWmInventoryDtl>();
			// 获取库存主表
			WlWmInventory wlWmInventory = wlWmInventoryService.getData(wlWmStoreInDetl.getStorageId(), wlWmStoreInDetl.getItemId());
			if (ValidateUtil.isNull(wlWmInventory)) {// 库存中不存在该物料，新增一条
				wlWmInventory = new WlWmInventory();
				wlWmInventory.setStorageId(wlWmStoreInDetl.getStorageId());
				wlWmInventory.setStorageName(wlWmStoreInDetl.getStorageName());
				wlWmInventory.setItemId(wlWmStoreInDetl.getItemId());
				wlWmInventory.setItemCd(wlWmStoreInDetl.getItemCd());
				wlWmInventory.setItemName(wlWmStoreInDetl.getItemName());
				wlWmInventory.setSpec(wlWmStoreInDetl.getSpec());
				wlWmInventory.setCategoryId(wlWmStoreInDetl.getCategoryId());
				wlWmInventory.setBaseUnitId(wlWmStoreInDetl.getBaseUnitId());
				wlWmInventory.setBaseUnitName(wlWmStoreInDetl.getBaseUnitName());
				wlWmInventory.setBaseUnitQty(wlWmStoreInDetl.getBaseUnitQty());
				wlWmInventory.setIsEnableFlag("1");
				wlWmInventory.setCreator(wlWmStoreInDetl.getModifier());
				wlWmInventory.setCreateTime(new Date());
			}
			else {// 存在-库存量增加
				wlWmInventory.setBaseUnitQty(wlWmInventory.getBaseUnitQty() + wlWmStoreInDetl.getBaseUnitQty());
			}
			// 循环序号列表，找到物料下的序号
			for (WlWmStoreInDetlInfo wlWmStoreInDetlInfo : detlInfoList) {
				if (wlWmStoreInDetlInfo.getItemNo().equals(wlWmStoreInDetl.getItemCd())) {
					WlWmInventoryDtl inventoryDtl = new WlWmInventoryDtl();
					inventoryDtl.setIsStore("1");
					inventoryDtl.setSerialNo(wlWmStoreInDetlInfo.getSerialNo());
					inventoryDtlList.add(inventoryDtl);
				}
			}
			// 保存
			wlWmInventoryService.saveData(wlWmInventory, inventoryDtlList);

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 修改入库单明细处理
	 * @param wlWmStoreInDetl 入库单明细
	 */
	private void updateStoreInDetl(WlWmStoreInDetl wlWmStoreInDetl, List<WlWmStoreInDetlInfo> detlInfoList) {
		try {
			// 通过从表ID获取对象
			WlWmStoreInDetl detl = (WlWmStoreInDetl) wlWmStoreInDetlDao.getObject(wlWmStoreInDetl.getDetlId());
			double diff = 0;// 原入库量与本次入库量之间的差
			// 判断是否有更改入库数量
			if (detl.getBaseUnitQty() != wlWmStoreInDetl.getHasUnitQty()) {
				diff = detl.getBaseUnitQty() - wlWmStoreInDetl.getHasUnitQty();
				detl.setBaseUnitQty(wlWmStoreInDetl.getHasUnitQty());
				detl.setModifier(wlWmStoreInDetl.getModifier());
				detl.setModifyTime(new Date());
			}
			// 保存从表
			wlWmStoreInDetlDao.saveObject(detl);
			// 删除其下序号
			wlWmStoreInDetlInfoDao.removeDellInfo(detl.getStoreInDetlId());
			List<WlWmInventoryDtl> inventoryDtlList = new ArrayList<WlWmInventoryDtl>();
			// 循环序号明细从表
			for (WlWmStoreInDetlInfo wlWmStoreInDetlInfo : detlInfoList) {
				wlWmStoreInDetlInfo.setSerialNo(wlWmStoreInDetlInfo.getSeqBarCd());
				// 循环序号明细，找到物料编码与从表一致的，挂在其下
				if (wlWmStoreInDetlInfo.getItemNo().equals(detl.getItemCd())) {
					wlWmStoreInDetlInfo.setStoreInDetlId(detl.getStoreInDetlId());
					WlWmInventoryDtl inventoryDtl = new WlWmInventoryDtl();
					inventoryDtl.setIsStore("1");
					inventoryDtl.setSerialNo(wlWmStoreInDetlInfo.getSerialNo());
					inventoryDtlList.add(inventoryDtl);
					// 保存序号
					wlWmStoreInDetlInfoDao.saveObject(wlWmStoreInDetlInfo);
				}
			}
			// 更改库存信息（库存中找到对应货品，更改其库存量-）
			// 获取库存主表
			WlWmInventory wlWmInventory = wlWmInventoryService.getData(detl.getStorageId(), detl.getItemId());
			wlWmInventory.setBaseUnitQty(wlWmInventory.getBaseUnitQty() - diff);
			// 保存
			wlWmInventoryService.updateData(wlWmInventory, inventoryDtlList, 0);

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取当前最新的单据编号
	 * @return 最新单据编号
	 */
	public synchronized String getNewBillNo() {
		try {
			String billNo = wlWmStoreInDao.getNewBillNo();
			Date nowDate = new Date();
			String newCode = "";
			if (!ValidateUtil.isEmpty(billNo)) {// 库中有单据编号
				String nowStr = DateUtil.dateFormatFromDateToString(nowDate, "yyyyMM");
				nowStr = "I" + nowStr;
				if (billNo.contains(nowStr)) {// 如果存在同一天的编号，则为之后的流水号添加1
					String maxStrCode = billNo.replaceAll(nowStr, "");
					int maxCode = Integer.parseInt(maxStrCode);
					maxCode++;
					maxStrCode = "" + maxCode;
					int maxStrCodeLength = maxStrCode.length();
					if (maxStrCode.length() < 3) {
						for (int i = 1; i <= 3 - maxStrCodeLength; i++) {
							maxStrCode = "0" + maxStrCode;
						}
					}
					newCode = nowStr + maxStrCode;
				}
				else {// 否则直接生成当前月的第一个流水号
					newCode = nowStr + "001";
				}

			}
			else {// 库中无单据编号,当前为第一条
				String nowStr = DateUtil.dateFormatFromDateToString(nowDate, "yyyyMM");
				nowStr = "I" + nowStr;
				newCode = nowStr + "001";
			}

			return newCode;
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取今日单据数
	 * @param storageId 仓库ID（为空或null则查询所有仓库）
	 * @return 单据数
	 */
	public long getTodayBillCount(String storageId) {
		try {
			String today = DateUtil.dateToShortDateStr(new Date());
			return wlWmStoreInDao.getBillCount(today, storageId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询入库单从表和从表明细列表对象数组
	 * @param storeInId 入库单ID
	 * @return 入库单从表和从表明细列表对象数组
	 */
	public List findWlWmStoreInDetlAndInfoList(String storeInId) {
		try {
			return wlWmStoreInDetlDao.findWlWmStoreInDetlAndInfoList(storeInId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 入库单据保存(供盘盈调用)
	 * @param wlWmStoreIn 入库单主表
	 * @param detlList 货品数据列表
	 */
	public void saveData(WlWmStoreIn wlWmStoreIn, List<WlWmStoreInDetl> detlList) {
		try {
			// 保存主表
			this.saveObject(wlWmStoreIn);
			// 保存从表
			for (WlWmStoreInDetl wlWmStoreInDetl : detlList) {
				wlWmStoreInDetl.setStoreInId(wlWmStoreIn.getStoreInId());
			}
			wlWmStoreInDetlDao.saveList(detlList);
			// 更改库存信息（库存中能找到对应货品，则更改库存量，否则新增一条库存）
			for (WlWmStoreInDetl wlWmStoreInDetl : detlList) {
				List<WlWmInventoryDtl> inventoryDtlList = new ArrayList<WlWmInventoryDtl>();
				// 获取库存主表
				WlWmInventory wlWmInventory = wlWmInventoryService.getData(wlWmStoreInDetl.getStorageId(), wlWmStoreInDetl.getItemId());
				if (ValidateUtil.isNull(wlWmInventory)) {// 库存中不存在该物料，新增一条
					wlWmInventory = new WlWmInventory();
					wlWmInventory.setStorageId(wlWmStoreInDetl.getStorageId());
					wlWmInventory.setStorageName(wlWmStoreInDetl.getStorageName());
					wlWmInventory.setItemId(wlWmStoreInDetl.getItemId());
					wlWmInventory.setItemCd(wlWmStoreInDetl.getItemCd());
					wlWmInventory.setItemName(wlWmStoreInDetl.getItemName());
					wlWmInventory.setSpec(wlWmStoreInDetl.getSpec());
					wlWmInventory.setCategoryId(wlWmStoreInDetl.getCategoryId());
					wlWmInventory.setBaseUnitId(wlWmStoreInDetl.getBaseUnitId());
					wlWmInventory.setBaseUnitName(wlWmStoreInDetl.getBaseUnitName());
					wlWmInventory.setBaseUnitQty(wlWmStoreInDetl.getBaseUnitQty());
					wlWmInventory.setIsEnableFlag("1");
					wlWmInventory.setCreator(wlWmStoreIn.getCreateor());
					wlWmInventory.setCreateTime(new Date());
				}
				else {// 存在-库存量增加
					wlWmInventory.setBaseUnitQty(wlWmInventory.getBaseUnitQty() + wlWmStoreInDetl.getBaseUnitQty());
				}
				// 保存
				wlWmInventoryService.saveData(wlWmInventory, inventoryDtlList);
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/***
	 * 获取入库日期
	 * @param paraMap 条件
	 * @return 入库日期列表
	 */
	public List<Map> findInDate(Map paraMap) {
		try {
			return wlWmStoreInDao.findInDate(paraMap);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/***
	 * 获取入库总数量
	 * @param paraMap 条件
	 * @return 人库总数量
	 */
	public double getInQty(Map paraMap) {
		try {
			return wlWmStoreInDao.getInQty(paraMap);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/***
	 * 获取入库统计列表
	 * @param paraMap 条件
	 * @return 入库统计列表
	 */
	public List<Map> findInDetailTotalList(Map paraMap) {
		try {
			return wlWmStoreInDao.findInDetailTotalList(paraMap);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}