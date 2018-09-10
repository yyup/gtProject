package com.gt.wl.cm.dao;

import java.util.List;

import org.joyone.dao.BaseDao;
import org.joyone.lang.BaseException;
import org.springframework.stereotype.Repository;

import com.gt.wl.cm.model.WlCmUserParameter;

/**
 * 用户参数DAO层
 * @author liuyj
 * 
 */
@Repository("wl.cm.WlCmUserParameterDao")
public class WlCmUserParameterDao extends BaseDao {

	public WlCmUserParameterDao() {
		modelClass = WlCmUserParameter.class;
		defaultOrder = "";
	}

	/**
	 * 通过用户ID与代码查找用户参数对象
	 * @param userId 用户ID
	 * @param code 代码
	 * @return 用户参数对象
	 */
	public WlCmUserParameter findByUserId(long userId, String code) {
		try {
			String hql = "from WlCmUserParameter o where o.userId=" + userId + " and o.code='" + code + "'";
			List list = this.find(hql);
			if (list.size() > 0) {
				return (WlCmUserParameter) list.get(0);
			}
			return null;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}
}
