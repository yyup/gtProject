package com.gt.wl.cm.dao;

import java.util.List;

import org.joyone.dao.BaseDao;
import org.joyone.lang.BaseException;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.cm.model.WlCmArea;

/**
 * 区域管理dao层
 * @author huangbj
 * 
 */
@Repository("wl.cm.WlCmAreaDao")
public class WlCmAreaDao extends BaseDao {

	public WlCmAreaDao() {
		modelClass = WlCmArea.class;
		defaultOrder = "";
	}

	/**
	 * 执行传入的sql语句，搜索数据库返回一个List
	 * @param levelEK 省市区的层级
	 * @return 搜索返回的List
	 * @author huangbj
	 */
	public List getByHql(String levelEK) {
		try {
			String hqlWhere = " 1 = 1 and u.levelEk<=" + levelEK + "";
			String hql = "from WlCmArea u where " + hqlWhere;
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 返回中国 省市地域树型数据
	 * @param parentAreaId 父id
	 * @return 对应父节点下的区域列表
	 */
	public List findAreByParentId(String parentAreaId) {
		try {
			String hql = "from WlCmArea a where a.parentAreaId ='" + parentAreaId + "'";
			List<WlCmArea> list = this.find(hql);
			return list;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 返回层级以内的区域列表
	 * @param levelId 等级ID
	 * @return 对应父节点下的区域列表
	 */
	public List findAreByLevelId(String levelId) {
		try {
			String hql = "from WlCmArea a where a.levelEk <='" + levelId + "'";
			List<WlCmArea> list = this.find(hql);
			return list;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据名字查找省、市、区id App
	 * @author luozp
	 * @param levelEk 级别
	 * @param name 名字
	 * @return 省id
	 */
	public List<WlCmArea> findAreaProvinceId(String levelEk, String name) {
		try {
			String hql = " from  WlCmArea a where a.levelEk ='" + levelEk + "' and a.name = '" + name + "'";
			List list = this.find(hql);
			if (!ValidateUtil.isEmpty(list)) {
				return list;
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
