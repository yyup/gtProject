package com.gt.wl.wm.dao;

import java.util.List;

import org.joyone.dao.BaseDao;
import org.joyone.dao.dialect.FormatUtil;
import org.joyone.lang.BaseException;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.wm.model.WlWmStoreInDetl;

@Repository("wl.wm.WlWmStoreInDetlDao")
public class WlWmStoreInDetlDao extends BaseDao {

	public WlWmStoreInDetlDao() {
		modelClass = WlWmStoreInDetl.class;
		defaultOrder = "";
	}

	/**
	 * 查询入库单从表对象数组
	 * @param storeInId 入库单ID
	 * @return 入库单从表对象数组
	 */
	public List findWlWmStoreInDetlList(String storeInId) {
		try {
			String hql = "from  WlWmStoreInDetl where 1=1 ";
			if (!ValidateUtil.isEmpty(storeInId)) {
				hql += " and storeInId=" + FormatUtil.formatStrForDB(storeInId);
			}
			return this.find(hql);
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
			String sql = "select a.ITEM_CD as ITEM_CD,a.ITEM_NAME as ITEM_NAME,a.SPEC as SPEC,a.BASE_UNIT_QTY as BASE_UNIT_QTY";
			sql += ",a.BASE_UNIT_NAME as BASE_UNIT_NAME,a.MEMO as MEMO,b.SERIAL_NO as SERIAL_NO ";
			sql += "from WL_WM_STORE_IN_DETL a  left join WL_WM_STORE_IN_DETL_INFO b on a.STORE_IN_DETL_ID=b.STORE_IN_DETL_ID where 1=1 ";
			if (!ValidateUtil.isEmpty(storeInId)) {
				sql += " and a.STORE_IN_ID=" + FormatUtil.formatStrForDB(storeInId);
			}
			return this.findByFreeSQL(sql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}
