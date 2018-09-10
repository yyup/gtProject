package com.gt.wl.cm.service;

import java.util.List;

import org.joyone.lang.BaseException;
import org.joyone.service.BaseService;
import org.joyone.spring.Sc;
import org.springframework.stereotype.Service;

import com.gt.wl.cm.dao.WlCmBizAuditLogDao;

@Service("wl.cm.WlCmBizAuditLogService")
public class WlCmBizAuditLogService extends BaseService {
	private WlCmBizAuditLogDao wlCmBizAuditLogDao = (WlCmBizAuditLogDao) Sc.getBean("wl.cm.WlCmBizAuditLogDao");

	public WlCmBizAuditLogService() {
		baseDao = wlCmBizAuditLogDao;
	}

	/**
	 * 业务审核日志列表
	 * @param bizObjectId 业务对象id
	 * @return 业务审核日志列表
	 */
	public List findBizAuditList(String bizObjectId) {
		try {
			return wlCmBizAuditLogDao.findBizAuditList(bizObjectId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}