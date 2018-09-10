package com.gt.wl.wm.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.joyone.lang.BaseException;
import org.joyone.service.BaseService;
import org.joyone.spring.Sc;
import org.joyone.sys.Lang;
import org.joyone.sys.Page;
import org.joyone.sys.User;
import org.joyone.util.ConvertUtil;
import org.joyone.util.DateUtil;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Service;

import com.gt.wl.cm.dao.WlCmOrgDao;
import com.gt.wl.cm.dao.WlCmStorageAreaDao;
import com.gt.wl.cm.dao.WlCmStorageDao;
import com.gt.wl.cm.model.WlCmOrg;
import com.gt.wl.cm.model.WlCmStorage;
import com.gt.wl.cm.model.WlCmStorageArea;
import com.gt.wl.wm.dao.WlWmNoticeInDao;
import com.gt.wl.wm.dao.WlWmNoticeInDetlDao;
import com.gt.wl.wm.dao.WlWmStoreInDetlDao;
import com.gt.wl.wm.model.WlWmNoticeIn;
import com.gt.wl.wm.model.WlWmNoticeInDetl;

@Service("wl.wm.WlWmNoticeInService")
public class WlWmNoticeInService extends BaseService {
	private WlWmNoticeInDao wlWmNoticeInDao = (WlWmNoticeInDao) Sc.getBean("wl.wm.WlWmNoticeInDao");
	private WlCmOrgDao wlCmOrgDao = (WlCmOrgDao) Sc.getBean("wl.cm.WlCmOrgDao");
	private WlCmStorageDao wlCmStorageDao = (WlCmStorageDao) Sc.getBean("wl.cm.WlCmStorageDao");
	private WlCmStorageAreaDao wlCmStorageAreaDao = (WlCmStorageAreaDao) Sc.getBean("wl.cm.WlCmStorageAreaDao");
	private WlWmNoticeInDetlDao wlWmNoticeInDetlDao = (WlWmNoticeInDetlDao) Sc.getBean("wl.wm.WlWmNoticeInDetlDao");
	private WlWmStoreInDetlDao wlWmStoreInDetlDao = (WlWmStoreInDetlDao) Sc.getBean("wl.wm.WlWmStoreInDetlDao");

	public WlWmNoticeInService() {
		baseDao = wlWmNoticeInDao;
	}

	/**
	 * 获取待办单数
	 * @param storageId 仓库ID（为空或null则查询所有仓库）
	 * @return 待办单数
	 */
	public long getTodoCount(String storageId) {
		try {
			return wlWmNoticeInDao.getTodoCount(storageId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询入库通知单（分页）
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param paraMap 条件
	 * @return 分页结果
	 */
	public Page search(int currPage, int pageSize, Map paraMap) {
		try {
			return wlWmNoticeInDao.search(currPage, pageSize, paraMap);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取新的通知单流水编号
	 * @return 通知单号
	 * @author
	 */
	public String getNewNoticeNoCode() {
		try {
			List<String> list = wlWmNoticeInDao.getNewNoticeNoCode();
			Date nowDate = new Date();
			String newCode = "";
			if (!ValidateUtil.isEmpty(list)) {
				String nowStr = DateUtil.dateFormatFromDateToString(nowDate, "yyyyMM");
				nowStr = "NI" + nowStr;
				String maxCodeString = list.get(0);
				if (ValidateUtil.isEmpty(maxCodeString)) {// 无数据的时候返回第一个订单编号
					newCode = nowStr + "001";
				}
				else {
					if (maxCodeString.contains(nowStr)) {// 如果存在同一天的编号，则为之后的流水号添加1
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
				nowStr = "NI" + nowStr;
				newCode = nowStr + "001";
			}

			return newCode;
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 新增/编辑数据
	 * @param wlWmNoticeIn 入库通知单对象
	 * @param wlWmNoticeInDetlList 入库通知单从表对象数组
	 * @param user 当前登录用户
	 * @return
	 */
	public void saveData(WlWmNoticeIn wlWmNoticeIn, List<WlWmNoticeInDetl> wlWmNoticeInDetlList, User user) {
		try {
			double totalQty = 0;
			WlCmStorage wlCmStorage = (WlCmStorage) wlCmStorageDao.getObject(wlWmNoticeIn.getStorageId());
			wlWmNoticeIn.setStorageName(wlCmStorage.getStorageName());
			if (!ValidateUtil.isEmpty(wlWmNoticeIn.getStorageAreaId())) {
				WlCmStorageArea wlCmStorageArea = (WlCmStorageArea) wlCmStorageAreaDao.getObject(wlWmNoticeIn.getStorageAreaId());
				wlWmNoticeIn.setStorageAreaName(wlCmStorageArea.getStorageAreaName());
			}
			if (!ValidateUtil.isEmpty(wlWmNoticeIn.getOrgId())) {
				WlCmOrg wlCmOrg = (WlCmOrg) wlCmOrgDao.getObject(wlWmNoticeIn.getOrgId());
				wlWmNoticeIn.setOrgName(wlCmOrg.getOrgName());
			}
			if (ValidateUtil.isEmpty(wlWmNoticeIn.getNoticeInId())) {// 新增
				wlWmNoticeIn.setBillStateEk("NO_ISSUE");
				wlWmNoticeIn.setResultEk("NOT_EXECUTE");
				wlWmNoticeIn.setCreateor(user.getName());
				wlWmNoticeIn.setCreateTime(new Date());
				wlWmNoticeIn.setNoticeNo(this.getNewNoticeNoCode());
			}
			this.saveObject(wlWmNoticeIn);
			if (!ValidateUtil.isEmpty(wlWmNoticeInDetlList)) {
				String[] noticeInIds = new String[1];
				noticeInIds[0] = wlWmNoticeIn.getNoticeInId();
				wlWmNoticeInDetlDao.deleteByNoticeInId(ConvertUtil.toDbString(noticeInIds));
				for (WlWmNoticeInDetl wlWmNoticeInDetl : wlWmNoticeInDetlList) {
					WlWmNoticeInDetl newWlWmNoticeInDetl = new WlWmNoticeInDetl();
					newWlWmNoticeInDetl.setNoticeInId(wlWmNoticeIn.getNoticeInId());
					newWlWmNoticeInDetl.setStorageId(wlWmNoticeIn.getStorageId());
					newWlWmNoticeInDetl.setStorageName(wlCmStorage.getStorageName());
					newWlWmNoticeInDetl.setItemId(wlWmNoticeInDetl.getItemId());
					newWlWmNoticeInDetl.setItemName(wlWmNoticeInDetl.getItemName());
					newWlWmNoticeInDetl.setItemCd(wlWmNoticeInDetl.getItemCd());
					newWlWmNoticeInDetl.setCategoryId(wlWmNoticeInDetl.getCategoryId());
					newWlWmNoticeInDetl.setSpec(wlWmNoticeInDetl.getSpec());
					newWlWmNoticeInDetl.setBaseUnitId(wlWmNoticeInDetl.getBaseUnitId());
					newWlWmNoticeInDetl.setBaseUnitName(wlWmNoticeInDetl.getBaseUnitName());
					newWlWmNoticeInDetl.setBaseUnitQty(wlWmNoticeInDetl.getBaseUnitQty());
					totalQty += wlWmNoticeInDetl.getBaseUnitQty();
					newWlWmNoticeInDetl.setModifier(user.getName());
					newWlWmNoticeInDetl.setModifyTime(new Date());
					wlWmNoticeInDetlDao.saveObject(newWlWmNoticeInDetl);
				}
				wlWmNoticeIn.setTotalQty(totalQty);
				this.saveObject(wlWmNoticeIn);
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询入库通知单从表的所有物料名称
	 * @param noticeInId 入库通知单id
	 * @param flag 调用标志（web端调用为0，APP端调用为1）
	 * @return 物料的名字，格式为"划船器等6种物料"
	 */
	public String getAllItemName(String noticeInId, int flag) {
		try {
			String result = "";
			List<WlWmNoticeInDetl> list = wlWmNoticeInDetlDao.findNoticeInDetlList(noticeInId);
			if (!ValidateUtil.isEmpty(list)) {
				if (list.size() == 1) {
					WlWmNoticeInDetl wlWmNoticeInDetl = list.get(0);
					result += wlWmNoticeInDetl.getItemName() + "(" + wlWmNoticeInDetl.getSpec() + ")";
				}
				else if (list.size() > 1) {
					WlWmNoticeInDetl wlWmNoticeInDetl = list.get(0);
					result += wlWmNoticeInDetl.getItemName() + "(" + wlWmNoticeInDetl.getSpec() + ")" + Lang.getString("wl.wm.WlWmNoticeInDetlService.kinds");
					if (flag == 0) {
						result += list.size() + Lang.getString("wl.wm.WlWmNoticeInDetlService.items");
					}
					else {
						result += list.size() + Lang.getString("wl.wm.WlWmNoticeInDetlService.itemName");
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
	 * 查询入库通知单对象数组
	 * @param noticeInIds 入库通知单ids数组
	 * @param billStateEk 单据状态
	 * @param resultEk 结果状态
	 * @return 通知单对象列表
	 */
	public List findNoticeInList(String[] noticeInIds, String billStateEk, String resultEk) {
		try {
			String result = "";
			if (!ValidateUtil.isEmpty(noticeInIds)) {
				result = ConvertUtil.toDbString(noticeInIds);
			}
			return wlWmNoticeInDao.findNoticeInList(result, billStateEk, resultEk);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 更改入库通知单的状态
	 * @param noticeInIds 入库通知单ids数组
	 * @param billStateEk 单据状态
	 * @param user 当前登录用户
	 */
	public void updateNoticeInState(String[] noticeInIds, String billStateEk, User user) {
		try {
			String result = "";
			if (!ValidateUtil.isEmpty(noticeInIds)) {
				result = ConvertUtil.toDbString(noticeInIds);
			}
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			wlWmNoticeInDao.updateNoticeInState(result, billStateEk, user.getName(), sdf.format(date));
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 删除入库通知单
	 * @param noticeInIds id数组转化成dbString，格式为('1','2','3')
	 */
	public void removeData(String[] noticeInIds) {
		try {
			wlWmNoticeInDetlDao.deleteByNoticeInId(ConvertUtil.toDbString(noticeInIds));
			wlWmNoticeInDao.removeData(ConvertUtil.toDbString(noticeInIds));
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取入库通知单的通知单单号，用,隔开
	 * @param noticeInIds 入库通知单ids数组
	 * @return 通知单号，单号之间用，隔开，格式为1,2,3
	 */
	public String getNoticeNo(String[] noticeInIds) {
		try {
			String result = "";
			for (String noticeInId : noticeInIds) {
				WlWmNoticeIn wlWmNoticeIn = (WlWmNoticeIn) this.getObject(noticeInId);
				result += wlWmNoticeIn.getNoticeNo() + ",";
			}
			if (result.length() > 0) {
				result = result.substring(0, result.length() - 1);
			}
			return result;
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询入库通知单从表
	 * @param noticeInId 入库通知单id
	 * @return 对象数组
	 */
	public List findNoticeInDetlList(String noticeInId) {
		try {
			return wlWmNoticeInDetlDao.findNoticeInDetlList(noticeInId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过通知单号获取入库通知单
	 * @param noticeNo 通知单号
	 * @return 入库通知单对象
	 */
	public WlWmNoticeIn getData(String noticeNo) {
		try {
			return wlWmNoticeInDao.getData(noticeNo);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}