package com.gt.wl.cm.dao;

import org.joyone.dao.BaseDao;
import org.springframework.stereotype.Repository;

import com.gt.wl.cm.model.WlCmMemberLevel;

/**
 * 会员等级DAO层
 * @author liuyj
 * 
 */
@Repository("wl.cm.WlCmMemberLevelDao")
public class WlCmMemberLevelDao extends BaseDao {

	public WlCmMemberLevelDao() {
		modelClass = WlCmMemberLevel.class;
		defaultOrder = "";
	}

}
