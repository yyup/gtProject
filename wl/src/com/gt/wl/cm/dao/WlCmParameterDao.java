package com.gt.wl.cm.dao;

import java.util.List;

import org.joyone.dao.BaseDao;
import org.joyone.lang.BaseException;
import org.joyone.sys.Page;
import org.joyone.sys.User;
import org.springframework.stereotype.Repository;

import com.gt.wl.cm.model.WlCmParameter;

/**
 * 参数DAO层
 * @author liuyj
 * 
 */
@Repository("wl.cm.WlCmParameterDao")
public class WlCmParameterDao extends BaseDao {

	public WlCmParameterDao() {
		modelClass = WlCmParameter.class;
		defaultOrder = "";
	}

	/**
	 * 获得用户ID拥有操作权限的参数数据列表
	 * @param user 用户
	 * @param currPage 当前页码
	 * @param pageSize 每页行数
	 * @return 参数页对象
	 */
	public Page findPageByUser(User user, int currPage, int pageSize) {
		try {
			String hql = "from WlCmParameter o where 1=1 ";
			if (!user.isProgrammer() && !user.isAdmin()) {
				hql += " and o.userIds like '%{" + user.getId() + "}%'";
			}
			hql += "order by o.id desc";
			return this.findPage(hql, currPage, pageSize);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过Code获得参数对象
	 * @param code 代码
	 * @return 用户参数
	 */
	public WlCmParameter findByCode(String code) {
		try {
			String hql = "from WlCmParameter o where o.code='" + code + "'";
			List list = this.find(hql);
			if (list.size() > 0) {
				return (WlCmParameter) list.get(0);
			}
			else {
				return null;
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查找符合条件的记录集
	 * @param searchField 搜索字段
	 * @param searchValue 搜索值
	 * @param user 用户
	 * @param currPage 当前页码
	 * @param pageSize 每页行数
	 * @return 结果页对象
	 */
	public Page search(String searchField, String searchValue, User user, int currPage, int pageSize) {
		try {

			String hql = "from WlCmParameter o where " + searchField + " like '%" + searchValue + "%'";
			if (!user.isProgrammer() && !user.isAdmin()) {
				hql += " and o.userIds like '%{" + user.getId() + "}%'";
			}
			hql += "order by o.id desc";
			return this.findPage(hql, currPage, pageSize);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}
