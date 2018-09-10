package com.gt.wl.cm.dao;

import java.util.List;

import org.joyone.dao.BaseDao;
import org.joyone.lang.BaseException;
import org.springframework.stereotype.Repository;

import com.gt.wl.cm.model.WlCmUserGroup;

/**
 * 用户组管理DAO层
 * @author liuyj
 * 
 */
@Repository("wl.cm.WlCmUserGroupDao")
public class WlCmUserGroupDao extends BaseDao {

	public WlCmUserGroupDao() {
		modelClass = WlCmUserGroup.class;
		defaultOrder = "";
	}

	/**
	 * 用户是否属于该组（职务与角色）
	 * @param userId 用户ID
	 * @param groupId 组ID
	 * @param type 1表示职务，2表示角色
	 * @return boolean
	 */
	public boolean isBelongToGroup(String userId, String groupId, int type) {
		try {
			Object[] v = new Object[2];
			v[0] = userId;
			v[1] = groupId;
			String hql = "from WlCmUserGroup o where o.userId=? and o.groupId=? and o.type=" + type;
			return this.find(hql, v).size() != 0;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 用户是否属于该部门下的该组（职务与角色）
	 * @param userId 用户ID
	 * @param departmentId 部门ID
	 * @param groupId 组ID
	 * @param type 类型1表示职务，2表示角色
	 * @return boolean
	 */
	public boolean isBelongToGroup(String userId, String departmentId, String groupId, int type) {
		try {
			Object[] v = new Object[3];
			v[0] = userId;
			v[1] = departmentId;
			v[2] = groupId;
			String hql = "from WlCmUserGroup o where o.userId=? and o.departmentId=? and o.groupId=? and o.type=" + type;
			return this.find(hql, v).size() != 0;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 用户是否属于该部门
	 * @param userId 用户ID
	 * @param departmentId 部门ID
	 * @param type 类型1表示职务，2表示角色
	 * @return boolean
	 */
	public boolean isBelongToDepartment(String userId, String departmentId, int type) {
		try {
			Object[] v = new Object[2];
			v[0] = userId;
			v[1] = departmentId;
			String hql = "from WlCmUserGroup o where o.userId=? and o.departmentId=? and o.type=" + type;
			return this.find(hql, v).size() != 0;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过用户ID查找组集合
	 * @param userId 用户ID
	 * @param type 类型
	 * @return 组列表
	 */
	public List findByUserId(String userId, int type) {
		try {
			String hql = "from WlCmUserGroup o where o.userId='" + userId + "' and o.type=" + type + " order by sequ desc";
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过用户ID查找组集合
	 * @param userId 用户ID
	 * @return 组列表
	 */
	public List findByUserId(String userId) {
		try {
			String hql = "from WlCmUserGroup o where o.userId='" + userId + "' order by sequ desc";
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据用户ID删除职务角色信息
	 * @param userId 用户ID
	 * @return 删除记录数
	 */
	public int deleteByUserId(String userId) {
		try {
			String hql = "delete WlCmUserGroup o where o.userId='" + userId + "'";
			return this.executeHql(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}
