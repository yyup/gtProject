package com.gt.wl.cm.dao;

import java.util.List;

import org.joyone.dao.BaseDao;
import org.joyone.lang.BaseException;
import org.springframework.stereotype.Repository;

import com.gt.wl.cm.model.WlCmSecurityUrl;

/**
 * 网址DAO层
 * @author liuyj
 * 
 */
@Repository("wl.cm.WlCmSecurityUrlDao")
public class WlCmSecurityUrlDao extends BaseDao {

	public WlCmSecurityUrlDao() {
		modelClass = WlCmSecurityUrl.class;
		defaultOrder = "";
	}

	/**
	 * 通过权限ID号查找相关网址
	 * @param securityId 权限ID号
	 * @return 网址列表
	 */
	public List findBySecurityId(String securityId) {
		try {
			String hql = "from WlCmSecurityUrl o where o.securityId= ? order by o.id";
			return this.find(hql, securityId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据权限ID 删除权限路径数据
	 * @param securityId 权限ID
	 * @return 删除总数
	 */
	public int deleteBySecurityId(String securityId) {
		try {
			String hql = "delete WlCmSecurityUrl o where  o.securityId='" + securityId + "'";
			return this.executeHql(hql);

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}
