package com.gt.wl.cm.dao;

import java.util.List;

import org.joyone.dao.BaseDao;
import org.joyone.lang.BaseException;
import org.springframework.stereotype.Repository;

import com.gt.wl.cm.model.WlCmDepartment;

/**
 * 部门机构DAO层
 * @author liuyj
 * 
 */
@Repository("wl.cm.WlCmDepartmentDao")
public class WlCmDepartmentDao extends BaseDao {

	public WlCmDepartmentDao() {
		modelClass = WlCmDepartment.class;
		defaultOrder = "";
	}

	/**
	 * 根据状态返回对象集
	 * @param state 状态
	 * @return list
	 */
	public List findByState(int state) {
		try {
			String hql = "from WlCmDepartment o where 1 = 1 ";
			if (state == 0 || state == 1) {
				hql += " and o.state=" + state;
			}
			hql += " order by o.sequ desc";
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}
