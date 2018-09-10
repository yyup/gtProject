package com.gt.wl.cm.dao;

import java.util.List;

import org.joyone.dao.BaseDao;
import org.joyone.lang.BaseException;
import org.springframework.stereotype.Repository;

import com.gt.wl.cm.model.WlCmMenu;

/**
 * 菜单DAO层
 * @author liuyj
 * 
 */
@Repository("wl.cm.WlCmMenuDao")
public class WlCmMenuDao extends BaseDao {

	public WlCmMenuDao() {
		modelClass = WlCmMenu.class;
		defaultOrder = "";
	}

	/**
	 * 根据状态返回对象列表
	 * @param state 状态
	 * @return list 对象列表
	 */
	public List findAllByState(int state) {
		try {
			String hql = "from WlCmMenu o where 1 = 1 ";
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

	/**
	 * 根据状态返回对象列表
	 * @param state 状态
	 * @param target 目标
	 * @return list 对象列表
	 */
	public List findAllByStateAndTarget(int state, String target) {
		try {
			String hql = "from WlCmMenu o where ( o.target = ? or o.target = 'root' )";
			if (state == 0 || state == 1) {
				hql += " and o.state=" + state;
			}
			hql += " order by o.sequ desc";
			Object a[] = new Object[1];
			a[0] = target;
			return this.find(hql, a);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}
