package com.gt.wl.wm.dao;

import java.util.List;

import org.joyone.dao.BaseDao;
import org.joyone.dao.dialect.FormatUtil;
import org.joyone.lang.BaseException;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.cm.model.WlCmItem;
import com.gt.wl.wm.model.WlWmStoreOutDetl;
import com.gt.wl.wm.model.WlWmStoreOutDetlInfo;

@Repository("wl.wm.WlWmStoreOutDetlDao")
public class WlWmStoreOutDetlDao extends BaseDao {

	public WlWmStoreOutDetlDao() {
		modelClass = WlWmStoreOutDetl.class;
		defaultOrder = "";
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
			String hql = "from  WlWmStoreOutDetl where 1=1 ";
			if (!ValidateUtil.isEmpty(storeOutId)) {
				hql += " and storeOutId=" + FormatUtil.formatStrForDB(storeOutId);
			}
			if (!ValidateUtil.isEmpty(date)) {
				hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("modifyTime"));
				hql += "  = " + this.sqlUtil.string2Date(FormatUtil.formatStrForDB(date));
			}
			if (!ValidateUtil.isEmpty(itemCd)) {
				hql += " and itemCd=" + FormatUtil.formatStrForDB(itemCd);
			}
			return this.find(hql);
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
			String sql = "select a.ITEM_CD as ITEM_CD,a.ITEM_NAME as ITEM_NAME,a.SPEC as SPEC,a.BASE_UNIT_QTY as BASE_UNIT_QTY";
			sql += ",a.BASE_UNIT_NAME as BASE_UNIT_NAME,a.MEMO as MEMO,b.SERIAL_NO as SERIAL_NO ";
			sql += "from WL_WM_STORE_OUT_DETL a  left join WL_WM_STORE_OUT_DETL_INFO b on a.STORE_OUT_DETL_ID=b.STORE_OUT_DETL_ID where 1=1 ";
			if (!ValidateUtil.isEmpty(storeOutId)) {
				sql += " and a.STORE_OUT_ID=" + FormatUtil.formatStrForDB(storeOutId);
			}
			return this.findByFreeSQL(sql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据序列号获取物料信息
	 * @param serialNo
	 * @return 
	 */
	public WlCmItem findItemId(String serialNo) {
		try {
			WlCmItem ob = null;
			String sql = "select c.* ";
			sql += " from WlWmStoreOutDetl a  , WlWmStoreOutDetlInfo b, WlCmItem c where 1=1 AND a.storeOutDetlId=b.storeOutDetlId AND a.itemId=c.itemId ";
			if (!ValidateUtil.isEmpty(serialNo)) {
				sql += " and b.serialNo=" + FormatUtil.formatStrForDB(serialNo);
			}
			List list = this.find(sql);
			if (!ValidateUtil.isEmpty(list)) {
				ob =  (WlCmItem)list.get(0);
			}
			return ob;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	public WlCmItem findItemIdForEN(String serialNo) {
		try {
			WlCmItem ob = null;
			String sql = "select c.* ";
			sql += " from WlWmStoreOutDetl a  , WlWmStoreOutDetlInfo b, WlCmItem c where 1=1 AND a.storeOutDetlId=b.storeOutDetlId AND a.itemId=c.itemId ";
			if (!ValidateUtil.isEmpty(serialNo)) {
				sql += " and b.serialNo=" + FormatUtil.formatStrForDB(serialNo);
			}
			List list = this.find(sql);
			if (!ValidateUtil.isEmpty(list)) {
				ob =  (WlCmItem)list.get(0);
			}
			return ob;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}
