package com.gt.wl.wm.dao;

import java.util.List;

import org.joyone.dao.BaseDao;
import org.joyone.dao.dialect.FormatUtil;
import org.joyone.lang.BaseException;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.wm.model.WlWmStoreCheckDetl;

@Repository("wl.wm.WlWmStoreCheckDetlDao")
public class WlWmStoreCheckDetlDao extends BaseDao {

	public WlWmStoreCheckDetlDao() {
		modelClass = WlWmStoreCheckDetl.class;
		defaultOrder = "";
	}

	/**
	 * 查询盘点单从表对象数组
	 * @param noticeNo 盘点通知单id
	 * @param storeCheckId 盘点单据ID
	 * @param profitAndLoss (损益-1)
	 * @return 盘点单从表对象列表
	 */
	public List findStoreCheckDetlList(String noticeNo, String storeCheckId, String profitAndLoss) {
		try {
			String hql = "from WlWmStoreCheckDetl  where 1=1 ";
			if (!ValidateUtil.isEmpty(noticeNo)) {
				hql += " and storeCheckId=(select storeCheckId from WlWmStoreCheck where  noticeNo='" + noticeNo + "')";
			}
			if (!ValidateUtil.isEmpty(storeCheckId)) {
				hql += " and storeCheckId=" + FormatUtil.formatStrForDB(storeCheckId);
			}
			if ("1".equals(profitAndLoss)) {
				hql += " and bookBaseQty!=checkBaseQty ";
			}
			return this.find(hql);

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
			String hql = "from WlWmStoreCheckDetl  where storeCheckId=?";
			return this.find(hql, storeCheckId);
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
			String hql = "select sum(checkBaseQty) from WlWmStoreCheckDetl where storeCheckId=" + FormatUtil.formatStrForDB(storeCheckId);
			Double total = (Double) this.find(hql).get(0);
			return total;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}
