package com.gt.wl.cm.dao;

import java.util.List;

import org.joyone.dao.BaseDao;
import org.joyone.lang.BaseException;
import org.joyone.sys.Page;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.cm.model.WlCmUser;
import com.gt.wl.cm.model.WlCmUserGroup;

/**
 * 用户管理DAO层
 * @author liuyj
 * 
 */
@Repository("wl.cm.WlCmUserDao")
public class WlCmUserDao extends BaseDao {

	public WlCmUserDao() {
		modelClass = WlCmUser.class;
		defaultOrder = "";
	}

	/**
	 * 通过登录名查找用户
	 * @param loginName 登录名
	 * @return 用户对象，没有找到则返回null
	 */
	public WlCmUser findByLoginName(String loginName) {
		String hql = "from WlCmUser where loginName=?";
		try {
			List<WlCmUser> userList = this.find(hql, loginName);
			if (!ValidateUtil.isEmpty(userList)) {
				return userList.get(0);
			}
			return null;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查找全部有效的用户
	 * @return 用户对象列表
	 */
	public List<WlCmUser> findAllUser() {
		try {
			String hql = "from WlCmUser where state=1 order by sequ desc";
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过复合条件进行查询
	 * @param currPage 当前页
	 * @param pageSize 页记录数
	 * @param name 用户名
	 * @param state 用户状态
	 * @return 数据集合
	 */
	public Page search(int currPage, int pageSize, String name, int state) {
		try {
			String hqlWhere = " 1 = 1 ";
			if (state == 0 || state == 1) {
				hqlWhere = "u.isAdmin<>1 and u.state=" + state;
			}
			if (!ValidateUtil.isEmpty(name)) {
				hqlWhere += " and u.name like '%" + name + "%'";
			}
			String hql = "from WlCmUser u where " + hqlWhere;
			hql += " order by u.sequ desc";
			return this.findPage(hql, currPage, pageSize);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取当前用户相同用户组的用户列表
	 * @param usergroup 用户组
	 * @return 用户列表
	 */
	public List<WlCmUser> findGroupUsers(List<WlCmUserGroup> usergroup) {
		try {
			if (usergroup.size() > 0) {
				String hql = "from WlCmUser t where t.id in (select m.userId from WlCmUserGroup m where( ";
				String where = " 1 = 1 ";
				String nut = " and ";
				for (WlCmUserGroup group : usergroup) {
					where += nut + "( m.departmentId = '" + group.getDepartmentId() + "' and m.groupId = '" + group.getGroupId() + "') ";
					nut = " or ";
				}
				hql += where;
				hql += " ))";

				return this.find(hql);
			}
			else {
				return null;
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}
