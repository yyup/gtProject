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
import org.joyone.sys.User;
import org.joyone.util.DateUtil;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Service;

import com.gt.wl.wm.dao.WlWmStoreLossDao;
import com.gt.wl.wm.dao.WlWmStoreLossDetlDao;
import com.gt.wl.wm.model.WlWmStoreCheck;
import com.gt.wl.wm.model.WlWmStoreCheckDetl;
import com.gt.wl.wm.model.WlWmStoreIn;
import com.gt.wl.wm.model.WlWmStoreInDetl;
import com.gt.wl.wm.model.WlWmStoreLoss;
import com.gt.wl.wm.model.WlWmStoreLossDetl;
import com.gt.wl.wm.model.WlWmStoreOut;
import com.gt.wl.wm.model.WlWmStoreOutDetl;

@Service("wl.wm.WlWmStoreLossService")
public class WlWmStoreLossService extends BaseService {
	private WlWmStoreLossDao wlWmStoreLossDao = (WlWmStoreLossDao) Sc.getBean("wl.wm.WlWmStoreLossDao");
	private WlWmStoreCheckService wlWmStoreCheckService = (WlWmStoreCheckService) Sc.getBean("wl.wm.WlWmStoreCheckService");
	private WlWmStoreLossDetlDao wlWmStoreLossDetlDao = (WlWmStoreLossDetlDao) Sc.getBean("wl.wm.WlWmStoreLossDetlDao");
	private WlWmStoreInService wlWmStoreInService = (WlWmStoreInService) Sc.getBean("wl.wm.WlWmStoreInService");
	private WlWmStoreOutService wlWmStoreOutService = (WlWmStoreOutService) Sc.getBean("wl.wm.WlWmStoreOutService");

	public WlWmStoreLossService() {
		baseDao = wlWmStoreLossDao;
	}

	/**
	 * 保存损益表和损益从表
	 * @param wlWmStoreCheck 盘点单对象
	 * @param list 盘点单从表列表
	 * @param user 当前登录用户
	 */
	public void saveStoreLoss(WlWmStoreCheck wlWmStoreCheck, List<WlWmStoreCheckDetl> list, User user) {
		try {
			wlWmStoreCheckService.saveObject(wlWmStoreCheck);// 保存盘点单
			WlWmStoreLoss wlWmStoreLoss = this.getWlWmStoreLoss(wlWmStoreCheck.getBillNo());// 查询已存在的损益单
			if (ValidateUtil.isNull(wlWmStoreLoss)) {// 如果损益单不存在，，生成一个新的损益单
				wlWmStoreLoss = new WlWmStoreLoss();
				wlWmStoreLoss.setBillNo(this.getNewBillNoCode());
				wlWmStoreLoss.setStorageId(wlWmStoreCheck.getStorageId());
				wlWmStoreLoss.setStorageName(wlWmStoreCheck.getStorageName());
				wlWmStoreLoss.setCreateOprId(user.getId());
				wlWmStoreLoss.setCreateor(user.getName());
				wlWmStoreLoss.setCreateTime(new Date());
				wlWmStoreLoss.setProfitLossSourceEk("PL_COUNT");
				wlWmStoreLoss.setLinkedBillNo(wlWmStoreCheck.getBillNo());
				wlWmStoreLoss.setLinkedMasId(wlWmStoreCheck.getStoreCheckId());

			}
			wlWmStoreLoss.setAuditState("0");
			wlWmStoreLoss.setAuditTime(new Date());
			wlWmStoreLoss.setAuditor(user.getName());
			this.saveObject(wlWmStoreLoss);// 保存损益主表
			wlWmStoreLossDetlDao.deleteByStoreLossId(wlWmStoreLoss.getStoreLossId());// 删除损益单从表数据
			for (WlWmStoreCheckDetl wlWmStoreCheckDetl : list) {// 生成损益单从表数据
				if (wlWmStoreCheckDetl.getBookBaseQty() != wlWmStoreCheckDetl.getCheckBaseQty()) {// 如果盘点数量和账面数量不相等，保存到损益从表
					WlWmStoreLossDetl wlWmStoreLossDetl = new WlWmStoreLossDetl();
					wlWmStoreLossDetl.setStoreLossId(wlWmStoreLoss.getStoreLossId());
					wlWmStoreLossDetl.setStorageId(wlWmStoreLoss.getStorageId());
					wlWmStoreLossDetl.setStorageName(wlWmStoreLoss.getStorageName());
					wlWmStoreLossDetl.setItemId(wlWmStoreCheckDetl.getItemId());
					wlWmStoreLossDetl.setItemCd(wlWmStoreCheckDetl.getItemCd());
					wlWmStoreLossDetl.setItemName(wlWmStoreCheckDetl.getItemName());
					wlWmStoreLossDetl.setCategoryId(wlWmStoreCheckDetl.getCategoryId());
					wlWmStoreLossDetl.setSpec(wlWmStoreCheckDetl.getSpec());
					wlWmStoreLossDetl.setBaseUnitId(wlWmStoreCheckDetl.getBaseUnitId());
					wlWmStoreLossDetl.setBaseUnitName(wlWmStoreCheckDetl.getBaseUnitName());
					wlWmStoreLossDetl.setBookBaseQty(wlWmStoreCheckDetl.getBookBaseQty());
					wlWmStoreLossDetl.setCheckBaseQty(wlWmStoreCheckDetl.getCheckBaseQty());
					wlWmStoreLossDetl.setDiffBaseQty(wlWmStoreCheckDetl.getCheckBaseQty() - wlWmStoreCheckDetl.getBookBaseQty());
					wlWmStoreLossDetlDao.saveObject(wlWmStoreLossDetl);// 保存损益从表
				}

			}
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据关联订单号获取损益单对象
	 * @param linkedBillNo 关联订单号
	 * @return 损益单对象
	 */
	public WlWmStoreLoss getWlWmStoreLoss(String linkedBillNo) {
		try {
			List list = wlWmStoreLossDao.findWlWmStoreLossList(linkedBillNo);
			WlWmStoreLoss wlWmStoreLoss = null;
			if (!ValidateUtil.isEmpty(list)) {
				wlWmStoreLoss = (WlWmStoreLoss) list.get(0);
			}
			return wlWmStoreLoss;
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取新的通知单流水编号
	 * @return 通知单号
	 * @author
	 */
	public String getNewBillNoCode() {
		try {
			List<String> list = wlWmStoreLossDao.getNewBillNoCode();
			Date nowDate = new Date();
			String newCode = "";
			if (!ValidateUtil.isEmpty(list)) {
				String nowStr = DateUtil.dateFormatFromDateToString(nowDate, "yyyyMM");
				nowStr = "Y" + nowStr;
				String maxCodeString = list.get(0);
				if (ValidateUtil.isEmpty(maxCodeString)) {// 无数据的时候返回第一个订单编号
					newCode = nowStr + "001";
				}
				else {
					if (maxCodeString.contains(nowStr)) {// 如果存在同一月的编号，则为之后的流水号添加1
						String maxStrCode = maxCodeString.replaceAll(nowStr, "");
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
					else {// 否则直接生成这一分钟的第一个流水号
						newCode = nowStr + "001";
					}
				}
			}
			else {
				String nowStr = DateUtil.dateFormatFromDateToString(nowDate, "yyyyMM");
				nowStr = "Y" + nowStr;
				newCode = nowStr + "001";
			}

			return newCode;
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询损益单对象（分页）
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param paraMap 条件
	 * @return 分页结果
	 */
	public Page search(int currPage, int pageSize, Map paraMap) {
		try {
			return wlWmStoreLossDao.search(currPage, pageSize, paraMap);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询损益单从表的所有物料名称
	 * @param storeLossId s损益单单据ID
	 * @param flag 调用标志（web端调用为0，APP端调用为1）
	 * @return 物料的名字，格式为"划船器等6种物料"
	 */
	public String getAllItemName(String storeLossId, int flag) {
		try {
			String result = "";
			List<WlWmStoreLossDetl> list = wlWmStoreLossDetlDao.findStoreLossDetlList(storeLossId);
			if (!ValidateUtil.isEmpty(list)) {
				if (list.size() == 1) {
					WlWmStoreLossDetl wlWmStoreLossDetl = list.get(0);
					result += wlWmStoreLossDetl.getItemName() + "(" + wlWmStoreLossDetl.getSpec() + ")";
				}
				else if (list.size() > 1) {
					WlWmStoreLossDetl wlWmStoreLossDetl = list.get(0);
					result += wlWmStoreLossDetl.getItemName() + "(" + wlWmStoreLossDetl.getSpec() + ")" + Lang.getString("wl.wm.WlWmStoreLossService.kinds");
					if (flag == 0) {
						result += list.size() + Lang.getString("wl.wm.WlWmStoreLossService.items");
					}
					else {
						result += list.size() + Lang.getString("wl.wm.WlWmStoreLossService.itemName");
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
	 * 查询损益单从表对象数组
	 * @param storeLossId 损益ID
	 * @return 损益单从表对象数组
	 */
	public List findStoreLossDetlList(String storeLossId) {
		try {
			return wlWmStoreLossDetlDao.findStoreLossDetlList(storeLossId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 审核通过，生成出入单
	 * @param wlWmStoreLoss 损益单主表
	 * @param user 当前登录用户
	 */
	public void saveStoreInOrStoreOut(WlWmStoreLoss wlWmStoreLoss, User user) {
		try {
			this.updateObject(wlWmStoreLoss);// 保存盈亏单
			List<WlWmStoreInDetl> inDetlList = new ArrayList();// 入库单从表
			List<WlWmStoreOutDetl> outDetlList = new ArrayList();// 出库单从表
			double inTotal = 0;// 入库单总数量
			double outTotal = 0;// 出库单总数量
			Date date = new Date();
			List<WlWmStoreLossDetl> list = wlWmStoreLossDetlDao.findStoreLossDetlList(wlWmStoreLoss.getStoreLossId());// 损益单从表
			for (WlWmStoreLossDetl wlWmStoreLossDetl : list) {
				if (wlWmStoreLossDetl.getCheckBaseQty() > wlWmStoreLossDetl.getBookBaseQty()) {// 生成入库单从表list
					WlWmStoreInDetl wlWmStoreInDetl = new WlWmStoreInDetl();
					wlWmStoreInDetl.setStorageId(wlWmStoreLossDetl.getStorageId());
					wlWmStoreInDetl.setStorageName(wlWmStoreLossDetl.getStorageName());
					wlWmStoreInDetl.setItemId(wlWmStoreLossDetl.getItemId());
					wlWmStoreInDetl.setItemCd(wlWmStoreLossDetl.getItemCd());
					wlWmStoreInDetl.setItemName(wlWmStoreLossDetl.getItemName());
					wlWmStoreInDetl.setCategoryId(wlWmStoreLossDetl.getCategoryId());
					wlWmStoreInDetl.setSpec(wlWmStoreLossDetl.getSpec());
					wlWmStoreInDetl.setBaseUnitId(wlWmStoreLossDetl.getBaseUnitId());
					wlWmStoreInDetl.setBaseUnitName(wlWmStoreLossDetl.getBaseUnitName());
					wlWmStoreInDetl.setBaseUnitQty(wlWmStoreLossDetl.getCheckBaseQty() - wlWmStoreLossDetl.getBookBaseQty());
					inTotal += wlWmStoreLossDetl.getCheckBaseQty() - wlWmStoreLossDetl.getBookBaseQty();
					wlWmStoreInDetl.setModifier(user.getName());
					wlWmStoreInDetl.setModifyTime(date);
					inDetlList.add(wlWmStoreInDetl);
				}
				else if (wlWmStoreLossDetl.getCheckBaseQty() < wlWmStoreLossDetl.getBookBaseQty()) {// 生成出库单从表list
					WlWmStoreOutDetl wlWmStoreOutDetl = new WlWmStoreOutDetl();
					wlWmStoreOutDetl.setStorageId(wlWmStoreLossDetl.getStorageId());
					wlWmStoreOutDetl.setStorageName(wlWmStoreLossDetl.getStorageName());
					wlWmStoreOutDetl.setItemId(wlWmStoreLossDetl.getItemId());
					wlWmStoreOutDetl.setItemCd(wlWmStoreLossDetl.getItemCd());
					wlWmStoreOutDetl.setItemName(wlWmStoreLossDetl.getItemName());
					wlWmStoreOutDetl.setCategoryId(wlWmStoreLossDetl.getCategoryId());
					wlWmStoreOutDetl.setSpec(wlWmStoreLossDetl.getSpec());
					wlWmStoreOutDetl.setBaseUnitId(wlWmStoreLossDetl.getBaseUnitId());
					wlWmStoreOutDetl.setBaseUnitName(wlWmStoreLossDetl.getBaseUnitName());
					wlWmStoreOutDetl.setBaseUnitQty(wlWmStoreLossDetl.getBookBaseQty() - wlWmStoreLossDetl.getCheckBaseQty());
					outTotal += wlWmStoreLossDetl.getBookBaseQty() - wlWmStoreLossDetl.getCheckBaseQty();
					wlWmStoreOutDetl.setModifier(user.getName());
					wlWmStoreOutDetl.setModifyTime(date);
					outDetlList.add(wlWmStoreOutDetl);
				}
			}
			// 入库单主表
			if (!ValidateUtil.isEmpty(inDetlList)) {// 如果有入库明细则生成入库单
				WlWmStoreIn wlWmStoreIn = new WlWmStoreIn();
				wlWmStoreIn.setBillNo(wlWmStoreInService.getNewBillNo());
				wlWmStoreIn.setStorageId(wlWmStoreLoss.getStorageId());
				wlWmStoreIn.setStorageName(wlWmStoreLoss.getStorageName());
				wlWmStoreIn.setWmInTypeEk("COUNT_SURP");
				wlWmStoreIn.setInDate(date);
				wlWmStoreIn.setTotalQty(inTotal);
				wlWmStoreIn.setCreateOprId(user.getId());
				wlWmStoreIn.setCreateor(user.getName());
				wlWmStoreIn.setCreateTime(date);
				wlWmStoreIn.setLockFlagEk("LOCK");
				wlWmStoreIn.setNoticeNo(wlWmStoreLoss.getBillNo());// 关联盈亏单
				wlWmStoreInService.saveData(wlWmStoreIn, inDetlList);
			}
			// 出库单主表
			if (!ValidateUtil.isEmpty(outDetlList)) {// 如果有出库明细则生成出库单
				WlWmStoreOut wlWmStoreOut = new WlWmStoreOut();
				wlWmStoreOut.setBillNo(wlWmStoreOutService.getNewBillNo());
				wlWmStoreOut.setStorageId(wlWmStoreLoss.getStorageId());
				wlWmStoreOut.setStorageName(wlWmStoreLoss.getStorageName());
				wlWmStoreOut.setWmOutTypeEk("COUNT_DEC");
				wlWmStoreOut.setOutDate(date);
				wlWmStoreOut.setTotalQty(outTotal);
				wlWmStoreOut.setCreateOprId(user.getId());
				wlWmStoreOut.setCreateor(user.getName());
				wlWmStoreOut.setCreateTime(date);
				wlWmStoreOut.setLockFlagEk("LOCK");
				wlWmStoreOut.setNoticeNo(wlWmStoreLoss.getBillNo());// 关联盈亏单
				wlWmStoreOutService.saveData(wlWmStoreOut, outDetlList);
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}