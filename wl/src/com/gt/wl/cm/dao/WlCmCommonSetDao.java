package com.gt.wl.cm.dao;

import java.util.List;

import org.joyone.dao.BaseDao;
import org.joyone.dao.dialect.FormatUtil;
import org.joyone.lang.BaseException;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.cm.model.WlCmCommonSet;

/**
 * 参数设置DAO层
 * @author liuyj
 * 
 */
@Repository("wl.cm.WlCmCommonSetDao")
public class WlCmCommonSetDao extends BaseDao {

	public WlCmCommonSetDao() {
		modelClass = WlCmCommonSet.class;
		defaultOrder = "";
	}

	/**
	 * 返回参数设置对象
	 * @param setKey 设置KEY
	 * @return 参数设置对象
	 */
	public WlCmCommonSet getWlCmCommonSetByKey(String setKey) {
		try {
			WlCmCommonSet wlCmCommonSet = null;
			String hql = "from WlCmCommonSet where setKey=" + FormatUtil.formatStrForDB(setKey);
			List list = this.find(hql);
			if (!ValidateUtil.isEmpty(list)) {
				wlCmCommonSet = (WlCmCommonSet) list.get(0);
			}
			return wlCmCommonSet;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}
