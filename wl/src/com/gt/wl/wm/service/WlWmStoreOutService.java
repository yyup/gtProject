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
import org.joyone.util.ConvertUtil;
import org.joyone.util.DateUtil;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Service;

import com.gt.wl.cm.model.WlCmItem;
import com.gt.wl.cm.service.WlCmItemService;
import com.gt.wl.wm.dao.WlWmStoreOutDao;
import com.gt.wl.wm.dao.WlWmStoreOutDetlDao;
import com.gt.wl.wm.dao.WlWmStoreOutDetlInfoDao;
import com.gt.wl.wm.model.WlWmInventory;
import com.gt.wl.wm.model.WlWmInventoryDtl;
import com.gt.wl.wm.model.WlWmNoticeOut;
import com.gt.wl.wm.model.WlWmStoreOut;
import com.gt.wl.wm.model.WlWmStoreOutDetl;
import com.gt.wl.wm.model.WlWmStoreOutDetlInfo;

/**
 * 出库单据管理Service层
 * @author liuyj
 * 
 */
@Service("wl.wm.WlWmStoreOutService")
public class WlWmStoreOutService extends BaseService {
	private WlWmStoreOutDao wlWmStoreOutDao = (WlWmStoreOutDao) Sc.getBean("wl.wm.WlWmStoreOutDao");
	private WlWmStoreOutDetlDao wlWmStoreOutDetlDao = (WlWmStoreOutDetlDao) Sc.getBean("wl.wm.WlWmStoreOutDetlDao");
	private WlWmStoreOutDetlInfoDao wlWmStoreOutDetlInfoDao = (WlWmStoreOutDetlInfoDao) Sc.getBean("wl.wm.WlWmStoreOutDetlInfoDao");
	private WlWmNoticeOutService wlWmNoticeOutService = (WlWmNoticeOutService) Sc.getBean("wl.wm.WlWmNoticeOutService");
	private WlCmItemService wlCmItemService = (WlCmItemService) Sc.getBean("wl.cm.WlCmItemService");
	private WlWmInventoryService wlWmInventoryService = (WlWmInventoryService) Sc.getBean("wl.wm.WlWmInventoryService");

	public WlWmStoreOutService() {
		baseDao = wlWmStoreOutDao;
	}

	/**
	 * 查询出库单列表
	 * @param flag 调用标志(入库单据管理为wlWmStoreOut，入库明细查为wlwmStoreOutQuery）
	 * @param billNo 单据编号
	 * @param beginDate 制单开始时间
	 * @param endDate 制单结束时间
	 * @param storageId 仓库id
	 * @param createor 经办人
	 * @param wmOutTypeEk 业务类型
	 * @param orgId 往来单位id
	 * @param itemCd 物料编码
	 * @param itemName 物料名称
	 * @param spec 规格型号
	 * @param lockFlagEk 单据状态
	 * @param consignee 收货人
	 * @return 出库单列表
	 */
	public List<Map> findStoreOutList(String flag, String billNo, String beginDate, String endDate, String storageId, String createor, String wmOutTypeEk, String orgId, String itemCd, String itemName, String spec, String lockFlagEk, String consignee) {
		try {
			return wlWmStoreOutDao.findStoreOutList(flag, billNo, beginDate, endDate, storageId, createor, wmOutTypeEk, orgId, itemCd, itemName, spec, lockFlagEk, consignee);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询出库单从表的所有物料名称
	 * @param storeOutId 出库单ID
	 * @param flag 调用标志（web端调用为0，APP端调用为1）
	 * @return 物料的名字，格式为"划船器等6种物料"
	 */
	public String getAllItemName(String storeOutId, int flag) {
		try {
			String result = "";
			List<WlWmStoreOutDetl> list = wlWmStoreOutDetlDao.findWlWmStoreOutDetlList(storeOutId, "", "");
			if (!ValidateUtil.isEmpty(list)) {
				if (list.size() == 1) {
					WlWmStoreOutDetl wlWmStoreOutDetl = list.get(0);
					result += wlWmStoreOutDetl.getItemName() + "(" + wlWmStoreOutDetl.getSpec() + ")";
				}
				else if (list.size() > 1) {
					WlWmStoreOutDetl wlWmStoreOutDetl = list.get(0);
					result += wlWmStoreOutDetl.getItemName() + "(" + wlWmStoreOutDetl.getSpec() + ")" + Lang.getString("wl.wm.WlWmStoreInService.kinds");
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
	 * 查询出库单分页数据
	 * @param flag 调用标志(出库单据管理为wlWmStoreOut，出库明细查为wlwmStoreOutQuery）
	 * @param paraMap 条件
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @return 出库单分页数据
	 */
	public Page search(String flag, Map paraMap, int pageSize, int currPage) {
		try {
			return wlWmStoreOutDao.search(flag, paraMap, pageSize, currPage);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询出库单从表对象数组
	 * @param storeOutId 出库单ID
	 * @param date 出库日期
	 * @param itemCd 物料编码
	 * @return 出库单从表对象数组
	 */
	public List findWlWmStoreOutDetlList(String storeOutId, String date, String itemCd) {
		try {
			return wlWmStoreOutDetlDao.findWlWmStoreOutDetlList(storeOutId, date, itemCd);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过出库单从表ID获取其下序号列表
	 * @param storeOutDetlId 出库单从表ID
	 * @return 序号列表
	 */
	public List<WlWmStoreOutDetlInfo> findDetlInfoList(String storeOutDetlId) {
		try {

			return wlWmStoreOutDetlInfoDao.findDetlInfoList(storeOutDetlId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/***
	 * 获取序号列表
	 * @param storeOutId 出库单id
	 * @param storeOutDetlId 出库单从表ID
	 * @return 序列号列表
	 */
	public List<Map> findSerialNoList(String storeOutId, String storeOutDetlId) {
		try {
			return wlWmStoreOutDetlInfoDao.findSerialNoList(storeOutId, storeOutDetlId);
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
	public WlWmStoreOutDetlInfo getInfoBySerialNo(String serialNo) {
		try {
			return wlWmStoreOutDetlInfoDao.getInfoBySerialNo(serialNo);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 出库单据保存
	 * @param wlWmStoreOut 出库单主表
	 * @param detlList 货品数据列表
	 * @param detlInfoList 序号数据列表
	 */
	public void saveData(WlWmStoreOut wlWmStoreOut, List<WlWmStoreOutDetl> detlList, List<WlWmStoreOutDetlInfo> detlInfoList) {
		try {
			double totalQty = 0;// 出库总数量
			boolean result = true;// 执行结果是否正常，默认正常
			// 通过通知单ID查找通知单(noticeId暂存noticeNo中)
			WlWmNoticeOut wlwmNoticeOut = (WlWmNoticeOut) wlWmNoticeOutService.getObject(wlWmStoreOut.getNoticeNo());
			// 出库单主表
			wlWmStoreOut.setNoticeNo(wlwmNoticeOut.getNoticeNo());
			wlWmStoreOut.setBillNo(getNewBillNo());
			wlWmStoreOut.setLockFlagEk("NOLOCK");// 未锁
			// 制单员
			wlWmStoreOut.setCreateTime(new Date());
			wlWmStoreOut.setOutDate(DateUtil.dateFormatFromStringToDate(DateUtil.dateToShortDateStr(new Date()), "yyyy-MM-dd"));
			// 仓库
			wlWmStoreOut.setStorageId(wlwmNoticeOut.getStorageId());
			wlWmStoreOut.setStorageName(wlwmNoticeOut.getStorageName());
			wlWmStoreOut.setStorageAreaId(wlwmNoticeOut.getStorageAreaId());
			wlWmStoreOut.setStorageAreaName(wlwmNoticeOut.getStorageAreaName());
			wlWmStoreOut.setSourceStorageId(wlwmNoticeOut.getSourceStorageId());
			wlWmStoreOut.setSourceStorageName(wlwmNoticeOut.getSourceStorageName());
			wlWmStoreOut.setTargetStorageId(wlwmNoticeOut.getTargetStorageId());
			wlWmStoreOut.setTargetStorageName(wlwmNoticeOut.getTargetStorageName());
			// 业务类型
			wlWmStoreOut.setWmOutTypeEk(wlwmNoticeOut.getWmOutTypeEk());
			// 往来单位
			wlWmStoreOut.setOrgId(wlwmNoticeOut.getOrgId());
			wlWmStoreOut.setOrgName(wlwmNoticeOut.getOrgName());
			wlWmStoreOut.setObjectTypeEk(wlwmNoticeOut.getObjectTypeEk());
			// 收货人
			wlWmStoreOut.setConsignee(wlwmNoticeOut.getConsignee());
			wlWmStoreOut.setContactWay(wlwmNoticeOut.getContactWay());
			wlWmStoreOut.setAddr(wlwmNoticeOut.getAddr());
			for (WlWmStoreOutDetl wlWmStoreOutDetl : detlList) {
				wlWmStoreOutDetl.setNoticeBaseQty(wlWmStoreOutDetl.getBaseUnitQty());// 设置通知数量
				wlWmStoreOutDetl.setBaseUnitQty(wlWmStoreOutDetl.getHasUnitQty());
				totalQty += wlWmStoreOutDetl.getHasUnitQty();
				if (wlWmStoreOutDetl.getNoticeBaseQty() != wlWmStoreOutDetl.getBaseUnitQty()) {
					result = false;
				}
			}
			wlWmStoreOut.setTotalQty(totalQty);
			// 更改通知单状态
			wlwmNoticeOut.setIsExecutedFlag("1");// 已执行
			if (result) {
				wlwmNoticeOut.setResultEk("NORMAL");// 结果状态为正常
			}
			else {
				wlwmNoticeOut.setResultEk("ABNORMAL");// 结果状态为异常
			}
			wlWmNoticeOutService.saveObject(wlwmNoticeOut);
			// 保存主表
			this.saveObject(wlWmStoreOut);
			int sequ = 1;
			// 保存货品明细从表
			for (WlWmStoreOutDetl wlWmStoreOutDetl : detlList) {
				// 设置主表ID
				wlWmStoreOutDetl.setStoreOutId(wlWmStoreOut.getStoreOutId());
				// 仓库
				wlWmStoreOutDetl.setStorageId(wlWmStoreOut.getStorageId());
				wlWmStoreOutDetl.setStorageName(wlWmStoreOut.getStorageName());
				// 物料
				WlCmItem item = (WlCmItem) wlCmItemService.getItemByItemCd(wlWmStoreOutDetl.getItemCd());
				wlWmStoreOutDetl.setItemId(item.getItemId());
				wlWmStoreOutDetl.setCategoryId(item.getCategoryId());
				wlWmStoreOutDetl.setBaseUnitId(item.getBaseUnitId());
				wlWmStoreOutDetl.setBaseUnitName(item.getBaseUnitName());
				wlWmStoreOutDetl.setSequ(sequ);
				sequ++;
				// 收货人
				if (ValidateUtil.isEmpty(wlWmStoreOutDetl.getConsignee())) {
					wlWmStoreOutDetl.setConsignee(wlWmStoreOut.getConsignee());
					wlWmStoreOutDetl.setContactWay(wlWmStoreOut.getContactWay());
					wlWmStoreOutDetl.setAddr(wlWmStoreOut.getAddr());
				}
				// 保存
				wlWmStoreOutDetlDao.saveObject(wlWmStoreOutDetl);
			}
			// 保存序号明细从表
			for (WlWmStoreOutDetlInfo wlWmStoreOutDetlInfo : detlInfoList) {
				wlWmStoreOutDetlInfo.setSerialNo(wlWmStoreOutDetlInfo.getSeqBarCd());
				// 循环货品明细，找到物料编码一致的，挂在其下
				for (WlWmStoreOutDetl wlWmStoreOutDetl : detlList) {
					if (wlWmStoreOutDetlInfo.getItemNo().equals(wlWmStoreOutDetl.getItemCd())) {
						wlWmStoreOutDetlInfo.setStoreOutDetlId(wlWmStoreOutDetl.getStoreOutDetlId());
						break;
					}
				}
				wlWmStoreOutDetlInfoDao.saveObject(wlWmStoreOutDetlInfo);
			}

			// 更改库存信息（库存中找到对应货品，更改其库存量-）
			for (WlWmStoreOutDetl wlWmStoreOutDetl : detlList) {
				List<WlWmInventoryDtl> inventoryDtlList = new ArrayList<WlWmInventoryDtl>();
				// 获取库存主表
				WlWmInventory wlWmInventory = wlWmInventoryService.getData(wlWmStoreOutDetl.getStorageId(), wlWmStoreOutDetl.getItemId());
				// 如该物料不存在库存表，则返回提示
				if (ValidateUtil.isNull(wlWmInventory)) {
					throw new BaseException(wlWmStoreOutDetl.getItemCd() + Lang.getString("wl.wm.WlWmStoreOutService.noExistsStore"));
				}
				wlWmInventory.setBaseUnitQty(wlWmInventory.getBaseUnitQty() - wlWmStoreOutDetl.getBaseUnitQty());
				// 循环序号列表，找到物料下的序号
				for (WlWmStoreOutDetlInfo wlWmStoreOutDetlInfo : detlInfoList) {
					if (wlWmStoreOutDetlInfo.getStoreOutDetlId().equals(wlWmStoreOutDetl.getStoreOutDetlId())) {
						WlWmInventoryDtl inventoryDtl = new WlWmInventoryDtl();
						inventoryDtl.setIsStore("1");
						inventoryDtl.setSerialNo(wlWmStoreOutDetlInfo.getSerialNo());
						inventoryDtlList.add(inventoryDtl);
					}
				}
				// 保存
				wlWmInventoryService.updateData(wlWmInventory, inventoryDtlList, 1);
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 出库单据修改
	 * @param billId 出库单主表ID
	 * @param userName 用户名
	 * @param detlList 货品数据列表
	 * @param detlInfoList 序号数据列表
	 */
	public void updateData(String billId, String userName, List<WlWmStoreOutDetl> detlList, List<WlWmStoreOutDetlInfo> detlInfoList) {
		try {
			double totalQty = 0;// 出库总数量
			boolean isFlag = true;
			// 通过单据ID获取出库单对象
			WlWmStoreOut wlWmStoreOut = (WlWmStoreOut) this.getObject(billId);

			for (WlWmStoreOutDetl wlWmStoreOutDetl : detlList) {
				wlWmStoreOutDetl.setNoticeBaseQty(wlWmStoreOutDetl.getBaseUnitQty());// 设置通知数量
				wlWmStoreOutDetl.setBaseUnitQty(wlWmStoreOutDetl.getHasUnitQty());
				totalQty += wlWmStoreOutDetl.getHasUnitQty();
				// 出库物料中通知数量与已出库数量不一致，则标志置为false
				if (wlWmStoreOutDetl.getNoticeBaseQty() != wlWmStoreOutDetl.getBaseUnitQty()) {
					isFlag = false;
				}
			}
			wlWmStoreOut.setTotalQty(totalQty);
			// 更改通知单结果状态
			WlWmNoticeOut wlWmNoticeOut = wlWmNoticeOutService.getData(wlWmStoreOut.getNoticeNo());
			if (isFlag) {
				wlWmNoticeOut.setResultEk("NORMAL");// 结果状态为正常
			}
			else {
				wlWmNoticeOut.setResultEk("ABNORMAL");// 结果状态为异常
			}
			wlWmNoticeOutService.saveObject(wlWmNoticeOut);
			// 保存主表
			this.saveObject(wlWmStoreOut);
			double diff = 0;// 相差
			// 保存货品明细从表
			for (WlWmStoreOutDetl wlWmStoreOutDetl : detlList) {
				// 通过从表ID获取对象
				WlWmStoreOutDetl detl = (WlWmStoreOutDetl) wlWmStoreOutDetlDao.getObject(wlWmStoreOutDetl.getDetlId());
				diff = 0;// 原出库量与本次出库量之间的差
				// 判断是否有更改出库数量
				if (detl.getBaseUnitQty() != wlWmStoreOutDetl.getHasUnitQty()) {
					diff = detl.getBaseUnitQty() - wlWmStoreOutDetl.getHasUnitQty();
					detl.setBaseUnitQty(wlWmStoreOutDetl.getHasUnitQty());
					detl.setModifier(userName);
					detl.setModifyTime(new Date());
				}
				// 保存从表
				wlWmStoreOutDetlDao.saveObject(detl);
				// 删除其下序号
				wlWmStoreOutDetlInfoDao.removeDellInfo(detl.getStoreOutDetlId());
				List<WlWmInventoryDtl> inventoryDtlList = new ArrayList<WlWmInventoryDtl>();
				// 循环序号明细从表
				for (WlWmStoreOutDetlInfo wlWmStoreOutDetlInfo : detlInfoList) {
					wlWmStoreOutDetlInfo.setSerialNo(wlWmStoreOutDetlInfo.getSeqBarCd());

					// 2016-02-18 liuyj update
					// 判断detlId是否为空，如否则循环序号明细，找到detlId与从表一致的，挂在其下
					// 如为空，则循环序号明细，找到物料编码与从表一致的，挂在其下
					if (!ValidateUtil.isEmpty(wlWmStoreOutDetlInfo.getDetlId())) {// 不为空
						// if (wlWmStoreOutDetlInfo.getItemNo().equals(detl.getItemCd())) {
						if (wlWmStoreOutDetlInfo.getDetlId().equals(detl.getStoreOutDetlId())) {
							wlWmStoreOutDetlInfo.setStoreOutDetlId(detl.getStoreOutDetlId());
							WlWmInventoryDtl inventoryDtl = new WlWmInventoryDtl();
							inventoryDtl.setIsStore("1");
							inventoryDtl.setSerialNo(wlWmStoreOutDetlInfo.getSerialNo());
							inventoryDtlList.add(inventoryDtl);
						}
					}
					else {// 为空
						if (wlWmStoreOutDetlInfo.getItemNo().equals(detl.getItemCd())) {
							wlWmStoreOutDetlInfo.setStoreOutDetlId(detl.getStoreOutDetlId());
							WlWmInventoryDtl inventoryDtl = new WlWmInventoryDtl();
							inventoryDtl.setIsStore("1");
							inventoryDtl.setSerialNo(wlWmStoreOutDetlInfo.getSerialNo());
							inventoryDtlList.add(inventoryDtl);
						}
					}
				}
				// 更改库存信息（库存中找到对应货品，更改其库存量-）
				// 获取库存主表
				WlWmInventory wlWmInventory = wlWmInventoryService.getData(detl.getStorageId(), detl.getItemId());
				wlWmInventory.setBaseUnitQty(wlWmInventory.getBaseUnitQty() + diff);
				// 保存
				wlWmInventoryService.updateData(wlWmInventory, inventoryDtlList, 1);
			}
			// 保存序号明细从表
			wlWmStoreOutDetlInfoDao.saveList(detlInfoList);
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
			String billNo = wlWmStoreOutDao.getNewBillNo();
			Date nowDate = new Date();
			String newCode = "";
			if (!ValidateUtil.isEmpty(billNo)) {// 库中有单据编号
				String nowStr = DateUtil.dateFormatFromDateToString(nowDate, "yyyyMM");
				nowStr = "O" + nowStr;
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
				nowStr = "O" + nowStr;
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
			return wlWmStoreOutDao.getBillCount(today, storageId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取今日单据列表
	 * @return 今日单据列表
	 */
	public List<Map> getTodayBill() {
		try {
			String today = DateUtil.dateToShortDateStr(new Date());
			return wlWmStoreOutDao.getBill(today, "ALLOCATEOUT");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询出库单从表和从表明细列表对象数组
	 * @param storeOutId 出库单ID
	 * @return 出库单从表和从表明细列表对象数组
	 */
	public List findWlWmStoreOutDetlAndInfoList(String storeOutId) {
		try {
			return wlWmStoreOutDetlDao.findWlWmStoreOutDetlAndInfoList(storeOutId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 出库单据保存(供盘亏调用)
	 * @param wlWmStoreOut 出库单主表
	 * @param detlList 货品数据列表
	 */
	public void saveData(WlWmStoreOut wlWmStoreOut, List<WlWmStoreOutDetl> detlList) {
		try {
			// 保存主表
			this.saveObject(wlWmStoreOut);
			// 保存从表
			for (WlWmStoreOutDetl wlWmStoreOutDetl : detlList) {
				wlWmStoreOutDetl.setStoreOutId(wlWmStoreOut.getStoreOutId());
			}
			wlWmStoreOutDetlDao.saveList(detlList);
			// 更改库存信息（库存中找到对应货品，更改其库存量-）
			for (WlWmStoreOutDetl wlWmStoreOutDetl : detlList) {
				List<WlWmInventoryDtl> inventoryDtlList = new ArrayList<WlWmInventoryDtl>();
				// 获取库存主表
				WlWmInventory wlWmInventory = wlWmInventoryService.getData(wlWmStoreOutDetl.getStorageId(), wlWmStoreOutDetl.getItemId());
				wlWmInventory.setBaseUnitQty(wlWmInventory.getBaseUnitQty() - wlWmStoreOutDetl.getBaseUnitQty());
				// 保存
				wlWmInventoryService.updateData(wlWmInventory, inventoryDtlList, 1);
			}

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 出库发货的相关信息
	 * @param storeOutIds 出库单id数组
	 * @return 出库发货的相关信息
	 */
	public List findStoreOutAndDetlList(String[] storeOutIds) {
		try {
			String result = "";
			if (!ValidateUtil.isEmpty(storeOutIds)) {
				result = ConvertUtil.toDbString(storeOutIds);
			}
			return wlWmStoreOutDao.findStoreOutAndDetlList(result);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/***
	 * 获取出库日期列表
	 * @param paraMap 条件
	 * @return 出库日期列表
	 */
	public List<Map> findOutDate(Map paraMap) {
		try {
			return wlWmStoreOutDao.findOutDate(paraMap);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/***
	 * 获取出库总数量
	 * @param paraMap 条件
	 * @return 出库总数量
	 */
	public double getOutQty(Map paraMap) {
		try {
			return wlWmStoreOutDao.getOutQty(paraMap);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/***
	 * 获取出库统计列表
	 * @param paraMap 条件
	 * @return 出库统计列表
	 */
	public List<Map> findOutDetailTotalList(Map paraMap) {
		try {
			return wlWmStoreOutDao.findOutDetailTotalList(paraMap);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/***
	 * 出库单信息
	 * @param paraMap 条件
	 * @return 出库单信息
	 */
	public List<Map> findStoreOutDetlList(Map paraMap) {
		try {
			return wlWmStoreOutDao.findStoreOutDetlList(paraMap);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}