package com.gt.wl.cm.dao;

import java.util.List;
import java.util.Vector;

import org.joyone.dao.BaseDao;
import org.joyone.dao.dialect.FormatUtil;
import org.joyone.lang.BaseException;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.cm.model.WlCmUserSecurity;

/**
 * 用户权限DAO层
 * @author liuyj
 * 
 */
@Repository("wl.cm.WlCmUserSecurityDao")
public class WlCmUserSecurityDao extends BaseDao {

	public WlCmUserSecurityDao() {
		modelClass = WlCmUserSecurity.class;
		defaultOrder = "";
	}

	/**
	 * 通过模块ID号和功能ID数组查找所有的权限定义节点
	 * @param securityId 模块ID号
	 * @param idArray 功能ID数组
	 * @return List
	 */
	public List findAllNode(String securityId, String[] idArray) {
		try {
			if (idArray.length == 0) {
				return new Vector();
			}
			String hql = "from WlCmUserSecurity o where o.securityId='" + securityId + "'";
			String hqlArray = "1<>1";
			for (int i = 0; i < idArray.length; i++) {
				hqlArray += " or o.securityArray like '%" + idArray[i] + "%'";
			}
			hql = hql + " and (" + hqlArray + ")";
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过模块ID号或功能ID查找所有的权限定义节点
	 * @param idArray 功能ID或模块ID数组
	 * @return List
	 */
	public List findAllNode(String[] idArray) {
		try {
			if (idArray.length == 0) {
				return new Vector();
			}
			String hql = "from WlCmUserSecurity o where ";
			String hqlId = "1<>1";
			String hqlArray = "1<>1";
			for (int i = 0; i < idArray.length; i++) {
				hqlId += " or o.securityId='" + idArray[i] + "'";
				hqlArray += " or o.securityArray like '%" + idArray[i] + "%'";
			}
			hql = hql + "(" + hqlId + ") or (" + hqlArray + ")";
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过模块ID号或功能ID查找所有的权限定义节点
	 * @param securityId 模块ID号或功能ID
	 * @return List
	 */
	public List findAllNode(String securityId) {
		try {
			Object[] v = new Object[2];
			v[0] = securityId;
			v[1] = "%" + securityId + "%";

			String hql = "from WlCmUserSecurity o where o.securityId=?  or o.securityArray like ? ";
			return this.find(hql, v);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过模块ID号查找所有的权限定义
	 * @param securityId 功能ID
	 * @return List
	 */
	public List findAllBySecurityId(String securityId) {
		try {
			String hql = "from WlCmUserSecurity o where o.securityId=? ";
			return this.find(hql, securityId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 
	 * @param ownerId 根据拥有者查找权限
	 * @return 拥有者权限列表
	 */
	public List findListByOwnerId(String ownerId) {
		try {
			String hql = "from WlCmUserSecurity o where 1=1 ";
			if (!ValidateUtil.isEmpty(ownerId)) {
				hql += " and o.ownerId=" + FormatUtil.formatStrForDB(ownerId);
			}
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 删除指定角色的权限
	 * @param ownerId 角色id
	 */
	public void deleteData(String ownerId) {
		try {
			String hql = "delete from WlCmUserSecurity o where 1=1";
			if (!ValidateUtil.isEmpty(ownerId)) {
				hql += " and o.ownerId=" + FormatUtil.formatStrForDB(ownerId);
			}
			this.executeHql(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}
