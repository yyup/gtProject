package com.gt.wl.cm.dao;

import java.util.List;

import org.joyone.dao.BaseDao;
import org.joyone.lang.BaseException;
import org.springframework.stereotype.Repository;

import com.gt.wl.cm.model.WlCmType;

/**
 * 枚举DAO层
 * @author liuyj
 * 
 */
@Repository("wl.cm.WlCmTypeDao")
public class WlCmTypeDao extends BaseDao {

	public WlCmTypeDao() {
		modelClass = WlCmType.class;
		defaultOrder = "";
	}

	/**
	 * 通过Code获得字典对象数组
	 * @param code 代码
	 * @return 枚举对象列表
	 */
	public List findListByCode(String code) {
		try {
			String hql = "from WlCmType o where o.code= ? and o.state = 1 order by o.sequ desc ";
			return this.find(hql, code);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据状态和安全级别结果
	 * @param state 状态
	 * @param security 安全级别，0：普通，1：开发者可管理,2:不可管理
	 * @return list
	 */
	public List<Object> findByStateAndSecurity(int state, int security) {
		try {
			String hql = "from WlCmType t where t.securityLevel <= " + security + " ";
			if (state == 0 || state == 1) {
				hql += " and t.state=" + state;
			}
			hql += " order by t.sequ desc ";

			return this.find(hql);

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据枚举CODE 类型，返回不带根节点的列表数据
	 * @param enumType 枚举CODE类型
	 * @return 对象的list
	 */
	public List findListByCodeWithoutRoot(String enumType) {
		try {
			String hql = "from WlCmType o where o.code=? and o.state = 1 and o.isParent = 0 order by o.sequ desc ";

			return this.find(hql, enumType);

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 
	 * 根据CODE 获取根节点ID
	 * @param code CODE代码
	 * @return 根节点ID
	 */
	public String findParentIdByCode(String code) {
		try {
			String hql = "from WlCmType o where o.code=? and o.state = 1 and o.isParent = 1 order by o.sequ desc ";
			List list = this.find(hql, code);
			if (list.size() > 0) {
				WlCmType type = (WlCmType) list.get(0);
				return type.getId();
			}
			return null;

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}
