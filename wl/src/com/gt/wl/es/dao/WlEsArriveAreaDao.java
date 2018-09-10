package com.gt.wl.es.dao;

import java.util.List;

import org.joyone.dao.BaseDao;
import org.joyone.dao.dialect.FormatUtil;
import org.joyone.lang.BaseException;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.es.model.WlEsArriveArea;

/**
 * 到达地区DAO层
 * @author huangbj
 * 
 */
@Repository("wl.es.WlEsArriveAreaDao")
public class WlEsArriveAreaDao extends BaseDao {

	public WlEsArriveAreaDao() {
		modelClass = WlEsArriveArea.class;
		defaultOrder = "";
	}

	/**
	 * 删除到达地区
	 * @param rowId 模板行id
	 * @param tempId 模板id
	 */
	public void removeData(String rowId, String tempId) {
		try {

			String hql = "delete from WlEsArriveArea s where 1=1";
			if (!ValidateUtil.isEmpty(rowId)) {
				hql += " and  s.rowId = " + FormatUtil.formatStrForDB(rowId);
			}
			if (!ValidateUtil.isEmpty(tempId)) {
				hql += " and  s.rowId in (select rowId from WlEsFeeTempRow";
				hql += " where tempId=" + FormatUtil.formatStrForDB(tempId) + " )";
			}
			this.executeHql(hql);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 模板行下的运费到达地区
	 * @param rowId 模板行id
	 * @return 模板行下的运费到达地区
	 */
	public List findArriveAreaList(String rowId) {
		try {
			String hql = "from WlEsArriveArea s where 1=1";
			if (!ValidateUtil.isEmpty(rowId)) {
				hql += " and s.rowId=" + FormatUtil.formatStrForDB(rowId);
			}
			return this.find(hql);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}
}
