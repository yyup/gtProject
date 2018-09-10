package com.gt.wl.es.dao;

import java.util.List;

import org.joyone.dao.BaseDao;
import org.joyone.lang.BaseException;
import org.springframework.stereotype.Repository;

import com.gt.wl.es.model.WlEsCustRepairSug;

/**
 * 售后维修处理意见DAO层
 * @author liuyj
 * 
 */
@Repository("wl.es.WlEsCustRepairSugDao")
public class WlEsCustRepairSugDao extends BaseDao {

	public WlEsCustRepairSugDao() {
		modelClass = WlEsCustRepairSug.class;
		defaultOrder = "";
	}

	/**
	 * 查询售后维修单下的处理意见列表
	 * @param repairId 售后维修ID
	 * @return 处理意见列表
	 */
	public List<WlEsCustRepairSug> findRepairSugList(String repairId) {
		try {
			String hql = "from WlEsCustRepairSug where repairId=?";
			return this.find(hql, repairId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}
