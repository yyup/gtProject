package com.gt.wl.cm.dao;

import java.util.List;

import org.joyone.dao.BaseDao;
import org.joyone.lang.BaseException;
import org.joyone.sys.Page;
import org.springframework.stereotype.Repository;

import com.gt.wl.cm.model.WlCmDutyrole;

/**
 * 角色职务DAO层
 * @author liuyj
 * 
 */
@Repository("wl.cm.WlCmDutyroleDao")
public class WlCmDutyroleDao extends BaseDao {
	public WlCmDutyroleDao() {
		modelClass = WlCmDutyrole.class;
		defaultOrder = "";
	}

	/**
	 * 返回翻页对象
	 * @param pageSize 页大小
	 * @param currPage 当前页
	 * @param state 状态
	 * @param code 类型 DUTY ROLE GROUP
	 * @param name 名称
	 * @return 翻页对象
	 */
	public Page search(int pageSize, int currPage, int state, String code, String name) {
		try {
			String hql = "from WlCmDutyrole as t where  t.code = '" + code + "' ";
			if (state == 0 || state == 1) {
				hql += " and t.state=" + state;
			}
			if (!"".equals(name) && name != null) {
				hql += " and t.name like '%" + name + "%'";
			}
			hql += " order by t.sequ desc";
			return this.findPage(hql, currPage, pageSize);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据类型返回职务（角色）结果列表
	 * @param code 类型 DUTY（职务）　ROLE（角色) GROUP （用户组）
	 * @param state 状态
	 * @return 结果集
	 */
	public List findByCodeAndState(String code, int state) {
		try {
			String hql = "from WlCmDutyrole t where t.code = ? ";
			if (state == 0 || state == 1) {
				hql += " and t.state=" + state;
			}
			hql += " order by t.sequ desc";
			Object a[] = new Object[1];
			a[0] = code;
			return this.find(hql, a);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 根据名称查找
	 * @param name 名称
	 * @return 列表
	 */
	public List findByName(String name) {
		try {
			String hql = "from WlCmDutyrole t where t.name = ?";
			return this.find(hql, name);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}
