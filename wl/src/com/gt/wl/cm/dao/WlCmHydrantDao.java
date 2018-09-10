package com.gt.wl.cm.dao;

import java.util.List;

import org.joyone.dao.BaseDao;
import org.joyone.lang.BaseException;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.cm.model.WlCmHydrant;

@Repository("wl.cm.WlCmHydrantDao")
public class WlCmHydrantDao extends BaseDao {

	public WlCmHydrantDao() {
		modelClass = WlCmHydrant.class;
		defaultOrder = "";
	}

	/**
	 * 通过标签号查找消防栓信息
	 * @param labelNo 标签号
	 * @return 消防栓信息对象
	 */
	public WlCmHydrant getWlCmHydrant(String labelNo) {
		String hql = "from WlCmHydrant where labelNo=?";
		try {
			List list = this.find(hql, labelNo);
			if (!ValidateUtil.isEmpty(list)) {
				return (WlCmHydrant) list.get(0);
			}
			return null;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}
