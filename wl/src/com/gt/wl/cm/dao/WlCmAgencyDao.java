package com.gt.wl.cm.dao;

import org.joyone.dao.BaseDao;
import org.joyone.lang.BaseException;
import org.joyone.sys.Page;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.cm.model.WlCmAgency;

/**
 * 经销商管理dao层
 * @author chench
 * 
 */
@Repository("wl.cm.WlCmAgencyDao")
public class WlCmAgencyDao extends BaseDao {

	public WlCmAgencyDao() {
		modelClass = WlCmAgency.class;
		defaultOrder = "";
	}

	/**
	 * 查询经销商列表数据
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param agencyName 经销商名称
	 * @return 分页结果
	 */

	public Page search(int currPage, int pageSize, String agencyName) {
		try {
			String where = " where  1 = 1 ";
			if (!ValidateUtil.isEmpty(agencyName)) {
				where += " and t.agencyName like '%" + agencyName + "%' ";
			}
			String hql = " from WlCmAgency as t " + where;
			return this.findPage(hql, currPage, pageSize);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}
