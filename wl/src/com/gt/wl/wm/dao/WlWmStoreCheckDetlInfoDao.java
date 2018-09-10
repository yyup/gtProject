package com.gt.wl.wm.dao;

import java.util.List;

import org.joyone.dao.BaseDao;
import org.joyone.dao.dialect.FormatUtil;
import org.joyone.lang.BaseException;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.wm.model.WlWmStoreCheckDetlInfo;

/**
 * 盘点单据明细（具体设备序号条码）
 * @author liuyj
 * 
 */
@Repository("wl.wm.WlWmStoreCheckDetlInfoDao")
public class WlWmStoreCheckDetlInfoDao extends BaseDao {

	public WlWmStoreCheckDetlInfoDao() {
		modelClass = WlWmStoreCheckDetlInfo.class;
		defaultOrder = "";
	}

	/**
	 * 通过序列号获取对象
	 * @param serialNo 序列号
	 * @return 明细对象（没有则返回null）
	 */
	public WlWmStoreCheckDetlInfo getInfoBySerialNo(String serialNo) {
		try {
			WlWmStoreCheckDetlInfo info = null;
			String hql = "from WlWmStoreCheckDetlInfo where serialNo=?";
			List list = this.find(hql, serialNo);
			if (!ValidateUtil.isEmpty(list)) {
				info = (WlWmStoreCheckDetlInfo) list.get(0);
			}
			return info;
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
			String hql = "from WlWmStoreCheckDetlInfo where storeCheckDetlId=?";
			return this.find(hql, storeCheckDetlId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过盘点单从表ID删除其下序号
	 * @param storeCheckDetlId 盘点单从表ID
	 */
	public void removeDellInfo(String storeCheckDetlId) {
		try {
			String hql = "delete WlWmStoreCheckDetlInfo where storeCheckDetlId=" + FormatUtil.formatStrForDB(storeCheckDetlId);
			this.executeHql(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}
