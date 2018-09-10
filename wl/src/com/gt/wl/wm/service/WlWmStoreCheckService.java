package com.gt.wl.wm.service;

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
import com.gt.wl.wm.dao.WlWmStoreCheckDao;
import com.gt.wl.wm.dao.WlWmStoreCheckDetlDao;
import com.gt.wl.wm.dao.WlWmStoreCheckDetlInfoDao;
import com.gt.wl.wm.model.WlWmNoticeCheck;
import com.gt.wl.wm.model.WlWmStoreCheck;
import com.gt.wl.wm.model.WlWmStoreCheckDetl;
import com.gt.wl.wm.model.WlWmStoreCheckDetlInfo;

/**
 * 盘点单据管理Service层
 * @author liuyj
 * 
 */
@Service("wl.wm.WlWmStoreCheckService")
public class WlWmStoreCheckService extends BaseService {
	private WlWmStoreCheckDao wlWmStoreCheckDao = (WlWmStoreCheckDao) Sc.getBean("wl.wm.WlWmStoreCheckDao");
	private WlWmStoreCheckDetlDao wlWmStoreCheckDetlDao = (WlWmStoreCheckDetlDao) Sc.getBean("wl.wm.WlWmStoreCheckDetlDao");
	private WlWmStoreCheckDetlInfoDao wlWmStoreCheckDetlInfoDao = (WlWmStoreCheckDetlInfoDao) Sc.getBean("wl.wm.WlWmStoreCheckDetlInfoDao");
	private WlWmNoticeCheckService wlWmNoticeCheckService = (WlWmNoticeCheckService) Sc.getBean("wl.wm.WlWmNoticeCheckService");
	private WlCmItemService wlCmItemService = (WlCmItemService) Sc.getBean("wl.cm.WlCmItemService");

	public WlWmStoreCheckService() {
		baseDao = wlWmStoreCheckDao;
	}

	/**
	 * 查询盘点单（分页）
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param paraMap 条件
	 * @return 分页结果
	 */
	public Page search(int currPage, int pageSize, Map paraMap) {
		try {
			return wlWmStoreCheckDao.search(currPage, pageSize, paraMap);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询盘点单从表的所有物料名称
	 * @param storeCheckId 盘点单据ID
	 * @param flag 调用标志（web端调用为0，APP端调用为1）
	 * @return 物料的名字，格式为"划船器等6种物料"
	 */
	public String getAllItemName(String storeCheckId, int flag) {
		try {
			String result = "";
			List<WlWmStoreCheckDetl> list = wlWmStoreCheckDetlDao.findStoreCheckDetlList(null, storeCheckId, null);
			if (!ValidateUtil.isEmpty(list)) {
				if (list.size() == 1) {
					WlWmStoreCheckDetl wlWmStoreCheckDetl = list.get(0);
					result += wlWmStoreCheckDetl.getItemName() + "(" + wlWmStoreCheckDetl.getSpec() + ")";
				}
				else if (list.size() > 1) {
					WlWmStoreCheckDetl wlWmStoreCheckDetl = list.get(0);
					result += wlWmStoreCheckDetl.getItemName() + "(" + wlWmStoreCheckDetl.getSpec() + ")" + Lang.getString("wl.wm.WlWmStoreCheckService.kinds");
					if (flag == 0) {
						result += list.size() + Lang.getString("wl.wm.WlWmStoreCheckService.items");
					}
					else {
						result += list.size() + Lang.getString("wl.wm.WlWmStoreCheckService.itemName");
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
	 * 查询异常数据列表（盘点单关联库存）
	 * @param noticeNo 盘点通知单号
	 * @param storeCheckId 盘点单据ID
	 * @param profitAndLoss (1损益)
	 * @return 对象数组
	 */
	public List findStoreCheckDetlList(String noticeNo, String storeCheckId, String profitAndLoss) {
		try {
			return wlWmStoreCheckDetlDao.findStoreCheckDetlList(noticeNo, storeCheckId, profitAndLoss);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取盘点单明细列表
	 * @param storeCheckId 盘点单ID
	 * @return 盘点单明细列表
	 */
	public List findCheckDetlList(String storeCheckId) {
		try {
			return wlWmStoreCheckDetlDao.findCheckDetlList(storeCheckId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过盘点单从表ID获取其下序号列表
	 * @param storeCheckDetlId 盘点单从表ID
	 * @return 序号列表
	 */
	public List<WlWmStoreCheckDetlInfo> findDetlInfoList(String storeCheckDetlId) {
		try {
			return wlWmStoreCheckDetlInfoDao.findDetlInfoList(storeCheckDetlId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 盘点单据保存
	 * @param wlWmStoreCheck 盘点单主表
	 * @param detlList 货品数据列表
	 * @param detlInfoList 序号数据列表
	 */
	public void saveData(WlWmStoreCheck wlWmStoreCheck, List<WlWmStoreCheckDetl> detlList, List<WlWmStoreCheckDetlInfo> detlInfoList) {
		try {
			boolean isNormal = true;// 是否正常
			// 通过通知单ID查找通知单(noticeId暂存noticeNo中)
			WlWmNoticeCheck wlwmNoticeCheck = (WlWmNoticeCheck) wlWmNoticeCheckService.getObject(wlWmStoreCheck.getNoticeNo());
			// 盘点单主表
			wlWmStoreCheck.setNoticeNo(wlwmNoticeCheck.getNoticeNo());
			wlWmStoreCheck.setBillNo(getNewBillNo());
			wlWmStoreCheck.setLockFlagEk("NOLOCK");// 未锁
			// 制单员
			wlWmStoreCheck.setCreateTime(new Date());
			wlWmStoreCheck.setCheckDate(DateUtil.dateFormatFromStringToDate(DateUtil.dateToShortDateStr(new Date()), "yyyy-MM-dd"));
			// 仓库
			wlWmStoreCheck.setStorageId(wlwmNoticeCheck.getStorageId());
			wlWmStoreCheck.setStorageName(wlwmNoticeCheck.getStorageName());
			// 业务类型
			wlWmStoreCheck.setWmCheckTypeEk(wlwmNoticeCheck.getWmCheckTypeEk());
			for (WlWmStoreCheckDetl detl : detlList) {
				if (detl.getBaseUnitQty() != detl.getHasUnitQty()) {
					isNormal = false;
					break;
				}
			}
			// 更改通知单状态
			wlwmNoticeCheck.setIsExecutedFlag("1");// 已执行
			if (isNormal) {
				wlwmNoticeCheck.setResultEk("NORMAL");// 结果状态为正常
			}
			else {
				wlwmNoticeCheck.setResultEk("ABNORMAL");// 结果状态为异常
			}
			wlWmNoticeCheckService.saveObject(wlwmNoticeCheck);
			// 保存主表
			this.saveObject(wlWmStoreCheck);
			int sequ = 1;
			// 保存货品明细从表
			for (WlWmStoreCheckDetl wlWmStoreCheckDetl : detlList) {
				// 设置主表ID
				wlWmStoreCheckDetl.setStoreCheckId(wlWmStoreCheck.getStoreCheckId());
				// 仓库
				wlWmStoreCheckDetl.setStorageId(wlWmStoreCheck.getStorageId());
				wlWmStoreCheckDetl.setStorageName(wlWmStoreCheck.getStorageName());
				// 物料
				WlCmItem item = (WlCmItem) wlCmItemService.getItemByItemCd(wlWmStoreCheckDetl.getItemCd());
				wlWmStoreCheckDetl.setItemId(item.getItemId());
				wlWmStoreCheckDetl.setCategoryId(item.getCategoryId());
				wlWmStoreCheckDetl.setBaseUnitId(item.getBaseUnitId());
				wlWmStoreCheckDetl.setBaseUnitName(item.getBaseUnitName());
				wlWmStoreCheckDetl.setBookBaseQty(wlWmStoreCheckDetl.getBaseUnitQty());
				wlWmStoreCheckDetl.setCheckBaseQty(wlWmStoreCheckDetl.getHasUnitQty());
				wlWmStoreCheckDetl.setSequ(sequ);
				sequ++;
				// 保存
				wlWmStoreCheckDetlDao.saveObject(wlWmStoreCheckDetl);
			}
			// 保存序号明细从表
			for (WlWmStoreCheckDetlInfo wlWmStoreCheckDetlInfo : detlInfoList) {
				wlWmStoreCheckDetlInfo.setSerialNo(wlWmStoreCheckDetlInfo.getSeqBarCd());
				// 循环货品明细，找到物料编码一致的，挂在其下
				for (WlWmStoreCheckDetl wlWmStoreCheckDetl : detlList) {
					if (wlWmStoreCheckDetlInfo.getItemNo().equals(wlWmStoreCheckDetl.getItemCd())) {
						wlWmStoreCheckDetlInfo.setStoreCheckDetlId(wlWmStoreCheckDetl.getStoreCheckDetlId());
						break;
					}
				}
				wlWmStoreCheckDetlInfoDao.saveObject(wlWmStoreCheckDetlInfo);
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 盘点单据修改
	 * @param billId 入库单主表ID
	 * @param userName 用户名
	 * @param detlList 货品数据列表
	 * @param detlInfoList 序号数据列表
	 */
	public void updateData(String billId, String userName, List<WlWmStoreCheckDetl> detlList, List<WlWmStoreCheckDetlInfo> detlInfoList) {
		try {
			boolean isNormal = true;// 是否正常
			WlWmStoreCheck wlWmStoreCheck = (WlWmStoreCheck) this.getObject(billId);
			for (WlWmStoreCheckDetl detl : detlList) {
				if (detl.getBaseUnitQty() != detl.getHasUnitQty()) {
					isNormal = false;
					break;
				}
			}
			// 更改通知单状态
			WlWmNoticeCheck wlWmNoticeCheck = wlWmNoticeCheckService.getData(wlWmStoreCheck.getNoticeNo());
			if (isNormal) {
				wlWmNoticeCheck.setResultEk("NORMAL");// 结果状态为正常
			}
			else {
				wlWmNoticeCheck.setResultEk("ABNORMAL");// 结果状态为异常
			}
			wlWmNoticeCheckService.saveObject(wlWmNoticeCheck);
			// 保存主表
			this.saveObject(wlWmStoreCheck);
			int sequ = 1;
			double diff = 0;// 相差
			// 保存货品明细从表
			for (WlWmStoreCheckDetl wlWmStoreCheckDetl : detlList) {
				// 通过从表ID获取对象
				WlWmStoreCheckDetl detl = (WlWmStoreCheckDetl) wlWmStoreCheckDetlDao.getObject(wlWmStoreCheckDetl.getDetlId());
				diff = 0;// 原盘点量与本次盘点量之间的差
				// 判断是否有更改盘点数量
				if (detl.getCheckBaseQty() != wlWmStoreCheckDetl.getHasUnitQty()) {
					diff = detl.getCheckBaseQty() - wlWmStoreCheckDetl.getHasUnitQty();
					detl.setBaseUnitQty(wlWmStoreCheckDetl.getHasUnitQty());
					detl.setCheckBaseQty(wlWmStoreCheckDetl.getHasUnitQty());
					detl.setModifier(userName);
					detl.setModifyTime(new Date());
					// 保存
					wlWmStoreCheckDetlDao.saveObject(detl);
				}
				// 删除其下序号
				wlWmStoreCheckDetlInfoDao.removeDellInfo(detl.getStoreCheckDetlId());
				// 循环序号明细从表
				for (WlWmStoreCheckDetlInfo wlWmStoreCheckDetlInfo : detlInfoList) {
					wlWmStoreCheckDetlInfo.setSerialNo(wlWmStoreCheckDetlInfo.getSeqBarCd());
					// 循环货品明细，找到物料编码一致的，挂在其下
					if (wlWmStoreCheckDetlInfo.getItemNo().equals(wlWmStoreCheckDetl.getItemCd())) {
						wlWmStoreCheckDetlInfo.setStoreCheckDetlId(wlWmStoreCheckDetl.getStoreCheckDetlId());
						wlWmStoreCheckDetlInfoDao.saveObject(wlWmStoreCheckDetlInfo);
					}
				}
			}
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
			String billNo = wlWmStoreCheckDao.getNewBillNo();
			Date nowDate = new Date();
			String newCode = "";
			if (!ValidateUtil.isEmpty(billNo)) {// 库中有单据编号
				String nowStr = DateUtil.dateFormatFromDateToString(nowDate, "yyyyMM");
				nowStr = "P" + nowStr;
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
				nowStr = "P" + nowStr;
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
			return wlWmStoreCheckDao.getBillCount(today, storageId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取单据下实盘总数量
	 * @param storeCheckId 盘点单据ID
	 * @return 实盘总数量
	 */
	public double getTotalQty(String storeCheckId) {
		try {
			return wlWmStoreCheckDetlDao.getTotalQty(storeCheckId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}