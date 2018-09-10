package com.gt.wl.cm.dao;

import java.util.List;

import org.joyone.dao.BaseDao;
import org.joyone.dao.dialect.FormatUtil;
import org.joyone.lang.BaseException;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.cm.model.WlCmBizAuditLog;

@Repository("wl.cm.WlCmBizAuditLogDao")
public class WlCmBizAuditLogDao extends BaseDao {

	public WlCmBizAuditLogDao() {
		modelClass = WlCmBizAuditLog.class;
		defaultOrder = "";
	}

	/**
	 * 业务审核日志列表
	 * @param bizObjectId 业务对象id
	 * @return 业务审核日志列表
	 */
	public List findBizAuditList(String bizObjectId) {
		try {
			String hql = "from WlCmBizAuditLog where auditNodeEk is not null ";
			if (!ValidateUtil.isEmpty(bizObjectId)) {
				hql += " and bizObjectId=" + FormatUtil.formatStrForDB(bizObjectId);
			}
			hql += " order by auditTime ";
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}
