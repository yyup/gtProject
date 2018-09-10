package com.gt.wl.wm.dao;

import java.util.List;
import java.util.Map;

import org.joyone.dao.BaseDao;
import org.joyone.dao.dialect.FormatUtil;
import org.joyone.lang.BaseException;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.wm.model.WlWmStoreInDetlInfo;

/**
 * 入库单据明细（具体设备序号条码）
 * @author liuyj
 * 
 */
@Repository("wl.wm.WlWmStoreInDetlInfoDao")
public class WlWmStoreInDetlInfoDao extends BaseDao {

	public WlWmStoreInDetlInfoDao() {
		modelClass = WlWmStoreInDetlInfo.class;
		defaultOrder = "";
	}

	/**
	 * 通过序列号获取对象
	 * @param serialNo 序列号
	 * @return 明细对象（没有则返回null）
	 */
	public WlWmStoreInDetlInfo getInfoBySerialNo(String serialNo) {
		try {
			WlWmStoreInDetlInfo info = null;
			String hql = "from WlWmStoreInDetlInfo where serialNo=?";
			List list = this.find(hql, serialNo);
			if (!ValidateUtil.isEmpty(list)) {
				info = (WlWmStoreInDetlInfo) list.get(0);
			}
			return info;
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
			String hql = "from WlWmStoreInDetlInfo where storeInDetlId=?";
			return this.find(hql, storeInDetlId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/***
	 * 通过入库单id获取其下序列号列表
	 * @param storeInId 入库单id
	 * @param storeInDetlId 入库单从表id
	 * @return 序列号列表
	 */
	public List<Map> findSerialNoList(String storeInId, String storeInDetlId) {
		try {
			String hql = "select new map(detlInfo.serialNo as serialNo )from WlWmStoreInDetl detl, WlWmStoreInDetlInfo detlInfo";
			hql += " where detl.storeInDetlId=detlInfo.storeInDetlId";
			if (!ValidateUtil.isEmpty(storeInId)) {
				hql += " and detl.storeInId=" + FormatUtil.formatStrForDB(storeInId);
			}
			if (!ValidateUtil.isEmpty(storeInDetlId)) {
				hql += " and detlInfo.storeInDetlId=" + FormatUtil.formatStrForDB(storeInDetlId);
			}
			hql += "  order by detl.itemCd";
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过入库单从表ID删除其下序号
	 * @param storeInDetlId 入库单从表ID
	 */
	public void removeDellInfo(String storeInDetlId) {
		try {
			String hql = "delete WlWmStoreInDetlInfo where storeInDetlId=" + FormatUtil.formatStrForDB(storeInDetlId);
			this.executeHql(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}
