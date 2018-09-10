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

import com.gt.wl.cm.dao.WlCmStorageDao;
import com.gt.wl.cm.model.WlCmStorage;
import com.gt.wl.wm.dao.WlWmNoticeCheckDao;
import com.gt.wl.wm.dao.WlWmNoticeCheckDetlDao;
import com.gt.wl.wm.dao.WlWmStoreCheckDetlDao;
import com.gt.wl.wm.model.WlWmNoticeCheck;
import com.gt.wl.wm.model.WlWmNoticeCheckDetl;

/**
 * 盘点通知Service层
 * @author liuyj
 * 
 */
@Service("wl.wm.WlWmNoticeCheckService")
public class WlWmNoticeCheckService extends BaseService {
	private WlWmNoticeCheckDao wlWmNoticeCheckDao = (WlWmNoticeCheckDao) Sc.getBean("wl.wm.WlWmNoticeCheckDao");
	private WlWmNoticeCheckDetlDao wlWmNoticeCheckDetlDao = (WlWmNoticeCheckDetlDao) Sc.getBean("wl.wm.WlWmNoticeCheckDetlDao");
	private WlCmStorageDao wlCmStorageDao = (WlCmStorageDao) Sc.getBean("wl.cm.WlCmStorageDao");
	private WlWmStoreCheckDetlDao wlWmStoreCheckDetlDao = (WlWmStoreCheckDetlDao) Sc.getBean("wl.wm.WlWmStoreCheckDetlDao");

	public WlWmNoticeCheckService() {
		baseDao = wlWmNoticeCheckDao;
	}

	/**
	 * 获取待办单数
	 * @param storageId 仓库ID（为空或null则查询所有仓库）
	 * @return 待办单数
	 */
	public long getTodoCount(String storageId) {
		try {
			return wlWmNoticeCheckDao.getTodoCount(storageId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询盘点通知单（分页）
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param paraMap 条件
	 * @return 分页结果
	 */
	public Page search(int currPage, int pageSize, Map paraMap) {
		try {
			return wlWmNoticeCheckDao.search(currPage, pageSize, paraMap);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过盘点通知单ID获取盘点通知单明细列表
	 * @param noticeCheckId 盘点通知单ID
	 * @return 列表
	 */
	public List<WlWmNoticeCheckDetl> findNoticeCheckDtlList(String noticeCheckId) {
		try {
			return wlWmNoticeCheckDetlDao.findNoticeCheckDtlList(noticeCheckId);
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
			List<String> list = wlWmNoticeCheckDao.getNewNoticeNoCode();
			Date nowDate = new Date();
			String newCode = "";
			if (!ValidateUtil.isEmpty(list)) {
				String nowStr = DateUtil.dateFormatFromDateToString(nowDate, "yyyyMM");
				nowStr = "NP" + nowStr;
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
				nowStr = "NP" + nowStr;
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
	 * @param wlWmNoticeCheck 盘点通知单对象
	 * @param wlWmNoticeCheckDetlList 盘点通知单从表对象数组
	 * @param user 当前登录用户
	 * @return
	 */
	public void saveData(WlWmNoticeCheck wlWmNoticeCheck, List<WlWmNoticeCheckDetl> wlWmNoticeCheckDetlList, User user) {
		try {

			WlCmStorage wlCmStorage = (WlCmStorage) wlCmStorageDao.getObject(wlWmNoticeCheck.getStorageId());
			wlWmNoticeCheck.setStorageName(wlCmStorage.getStorageName());
			if (ValidateUtil.isEmpty(wlWmNoticeCheck.getNoticeCheckId())) {// 新增
				wlWmNoticeCheck.setBillStateEk("NO_ISSUE");
				wlWmNoticeCheck.setResultEk("NOT_EXECUTE");
				wlWmNoticeCheck.setCreateor(user.getName());
				wlWmNoticeCheck.setCreateTime(new Date());
				wlWmNoticeCheck.setNoticeNo(this.getNewNoticeNoCode());
			}
			this.saveObject(wlWmNoticeCheck);
			if (!ValidateUtil.isEmpty(wlWmNoticeCheckDetlList)) {
				String[] noticeCheckIds = new String[1];
				noticeCheckIds[0] = wlWmNoticeCheck.getNoticeCheckId();
				wlWmNoticeCheckDetlDao.deleteByNoticeCheckId(ConvertUtil.toDbString(noticeCheckIds));
				for (WlWmNoticeCheckDetl wlWmNoticeCheckDetl : wlWmNoticeCheckDetlList) {
					WlWmNoticeCheckDetl newWlWmNoticeCheckDetl = new WlWmNoticeCheckDetl();
					newWlWmNoticeCheckDetl.setNoticeCheckId(wlWmNoticeCheck.getNoticeCheckId());
					newWlWmNoticeCheckDetl.setStorageId(wlCmStorage.getStorageId());
					newWlWmNoticeCheckDetl.setStorageName(wlCmStorage.getStorageName());
					newWlWmNoticeCheckDetl.setItemId(wlWmNoticeCheckDetl.getItemId());
					newWlWmNoticeCheckDetl.setItemName(wlWmNoticeCheckDetl.getItemName());
					newWlWmNoticeCheckDetl.setItemCd(wlWmNoticeCheckDetl.getItemCd());
					newWlWmNoticeCheckDetl.setCategoryId(wlWmNoticeCheckDetl.getCategoryId());
					newWlWmNoticeCheckDetl.setSpec(wlWmNoticeCheckDetl.getSpec());
					newWlWmNoticeCheckDetl.setBaseUnitId(wlWmNoticeCheckDetl.getBaseUnitId());
					newWlWmNoticeCheckDetl.setBaseUnitName(wlWmNoticeCheckDetl.getBaseUnitName());
					newWlWmNoticeCheckDetl.setBaseUnitQty(wlWmNoticeCheckDetl.getBaseUnitQty());
					newWlWmNoticeCheckDetl.setModifier(user.getName());
					newWlWmNoticeCheckDetl.setModifyTime(new Date());
					wlWmNoticeCheckDetlDao.saveObject(newWlWmNoticeCheckDetl);
				}
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询盘点通知单从表的所有物料名称
	 * @param noticeCheckId 盘点通知单id
	 * @return 物料的名字，格式为"划船器等6种物料"
	 */
	public String getAllItemName(String noticeCheckId) {
		try {
			String result = "";
			List<WlWmNoticeCheckDetl> list = wlWmNoticeCheckDetlDao.findNoticeCheckDtlList(noticeCheckId);
			if (!ValidateUtil.isEmpty(list)) {
				if (list.size() == 1) {
					WlWmNoticeCheckDetl wlWmNoticeCheckDetl = list.get(0);
					result += wlWmNoticeCheckDetl.getItemName() + "(" + wlWmNoticeCheckDetl.getSpec() + ")";
				}
				else if (list.size() > 1) {
					WlWmNoticeCheckDetl wlWmNoticeCheckDetl = list.get(0);
					result += wlWmNoticeCheckDetl.getItemName() + "(" + wlWmNoticeCheckDetl.getSpec() + ")" + Lang.getString("wl.wm.WlWmStoreCheckService.kinds");
					result += list.size() + Lang.getString("wl.wm.WlWmStoreCheckService.items");
				}
			}
			return result;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询盘点通知单对象数组
	 * @param noticeCheckIds 盘点通知单ids数组
	 * @param billStateEk 单据状态
	 * @param resultEk 结果状态
	 * @return 盘点通知单对象列表
	 */
	public List findNoticeCheckList(String[] noticeCheckIds, String billStateEk, String resultEk) {
		try {
			String result = "";
			if (!ValidateUtil.isEmpty(noticeCheckIds)) {
				result = ConvertUtil.toDbString(noticeCheckIds);
			}
			return wlWmNoticeCheckDao.findNoticeCheckList(result, billStateEk, resultEk);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取盘点通知单的通知单单号，用,隔开
	 * @param noticeCheckIds 盘点通知单ids数组
	 * @return 通知单号，单号之间用，隔开，格式为1,2,3
	 */
	public String getNoticeNo(String[] noticeCheckIds) {
		try {
			String result = "";
			for (String noticeCheckId : noticeCheckIds) {
				WlWmNoticeCheck wlWmNoticeCheck = (WlWmNoticeCheck) this.getObject(noticeCheckId);
				result += wlWmNoticeCheck.getNoticeNo() + ",";
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
	 * 更改盘点通知单的状态
	 * @param noticeCheckIds 盘点通知单ids数组
	 * @param billStateEk 单据状态
	 * @param user 当前登录用户
	 */
	public void updateNoticeCheckState(String[] noticeCheckIds, String billStateEk, User user) {
		try {
			String result = "";
			if (!ValidateUtil.isEmpty(noticeCheckIds)) {
				result = ConvertUtil.toDbString(noticeCheckIds);
			}
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			wlWmNoticeCheckDao.updateNoticeCheckState(result, billStateEk, user.getName(), sdf.format(date));
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 删除盘点通知单通知单
	 * @param noticeCheckIds 盘点通知单id数组
	 */
	public void removeData(String[] noticeCheckIds) {
		try {
			wlWmNoticeCheckDetlDao.deleteByNoticeCheckId(ConvertUtil.toDbString(noticeCheckIds));
			wlWmNoticeCheckDao.removeData(ConvertUtil.toDbString(noticeCheckIds));
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询异常数据列表（盘点单关联库存）
	 * @param noticeNo 盘点通知单号
	 * @param storeCheckId 盘点单据ID
	 * @return 对象数组
	 */
	public List findAbnormalDataList(String noticeNo, String storeCheckId) {
		try {
			return wlWmStoreCheckDetlDao.findStoreCheckDetlList(noticeNo, storeCheckId, null);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过通知单号获取盘点通知单
	 * @param noticeNo 通知单号
	 * @return 盘点通知单对象
	 */
	public WlWmNoticeCheck getData(String noticeNo) {
		try {
			return wlWmNoticeCheckDao.getData(noticeNo);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取盘点单从表数据（含库存）
	 * @param noticeCheckId 盘点通知单id
	 * @param storageId 仓库id
	 * @return 盘点单从表数据（含库存）
	 */
	public List findNoticeCheckDetlAndInventory(String noticeCheckId, String storageId) {
		try {
			return wlWmNoticeCheckDetlDao.findDataList(noticeCheckId, storageId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}