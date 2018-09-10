package com.gt.wl.cm.dao;

import org.joyone.dao.BaseDao;
import org.springframework.stereotype.Repository;

import com.gt.wl.cm.model.WlCmUnit;

/**
 * 单位DAO层
 * @author huangbj
 * 
 */
@Repository("tp.cm.TpCmUnitDao")
public class WlCmUnitDao extends BaseDao {

	public WlCmUnitDao() {
		modelClass = WlCmUnit.class;
		defaultOrder = "";
	}

}
