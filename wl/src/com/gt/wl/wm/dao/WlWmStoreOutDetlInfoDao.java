package com.gt.wl.wm.dao;

import java.util.List;
import java.util.Map;

import org.joyone.dao.BaseDao;
import org.joyone.dao.dialect.FormatUtil;
import org.joyone.lang.BaseException;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.wm.model.WlWmStoreOutDetlInfo;

/**
 * 出库单据明细（具体设备序号条码）
 * @author liuyj
 * 
 */
@Repository("wl.wm.WlWmStoreOutDetlInfoDao")
public class WlWmStoreOutDetlInfoDao extends BaseDao {

	public WlWmStoreOutDetlInfoDao() {
		modelClass = WlWmStoreOutDetlInfo.class;
		defaultOrder = "";
	}

	/**
	 * 通过序列号获取对象
	 * @param serialNo 序列号
	 * @return 明细对象（没有则返回null）
	 */
	public WlWmStoreOutDetlInfo getInfoBySerialNo(String serialNo) {
		try {
			WlWmStoreOutDetlInfo info = null;
			String hql = "from WlWmStoreOutDetlInfo where serialNo=?";
			List list = this.find(hql, serialNo);
			if (!ValidateUtil.isEmpty(list)) {
				info = (WlWmStoreOutDetlInfo) list.get(0);
			}
			return info;
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
			String hql = "from WlWmStoreOutDetlInfo where storeOutDetlId=?";
			return this.find(hql, storeOutDetlId);
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
			String hql = "select new map(detlInfo.serialNo as serialNo ,detl.consignee as consignee)from WlWmStoreOutDetl detl, WlWmStoreOutDetlInfo detlInfo";
			hql += " where detl.storeOutDetlId=detlInfo.storeOutDetlId";
			if (!ValidateUtil.isEmpty(storeOutId)) {
				hql += " and detl.storeOutId=" + FormatUtil.formatStrForDB(storeOutId);
			}
			if (!ValidateUtil.isEmpty(storeOutDetlId)) {
				hql += " and detlInfo.storeOutDetlId=" + FormatUtil.formatStrForDB(storeOutDetlId);
			}
			hql += "  order by detl.itemCd";
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过出库单从表ID删除其下序号
	 * @param storeOutDetlId 出库单从表ID
	 */
	public void removeDellInfo(String storeOutDetlId) {
		try {
			String hql = "delete WlWmStoreOutDetlInfo where storeOutDetlId=" + FormatUtil.formatStrForDB(storeOutDetlId);
			this.executeHql(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}
