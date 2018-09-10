package com.gt.wl.es.dao;

import java.util.List;

import org.joyone.dao.BaseDao;
import org.joyone.dao.dialect.FormatUtil;
import org.joyone.lang.BaseException;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.es.model.WlEsAgencyOrderAudit;

/**
 * 订单审核DAO层
 * @author liuyj
 * 
 */
@Repository("wl.es.WlEsAgencyOrderAuditDao")
public class WlEsAgencyOrderAuditDao extends BaseDao {

	public WlEsAgencyOrderAuditDao() {
		modelClass = WlEsAgencyOrderAudit.class;
		defaultOrder = "";
	}

	/**
	 * 查询经销商订单审核明细
	 * @param orderId 经销商订单id
	 * @return 经销商订单审核对象数组
	 */
	public List findAgencyOrderAuditList(String orderId) {
		try {
			String hql = "from WlEsAgencyOrderAudit where 1=1 ";
			if (!ValidateUtil.isEmpty(orderId)) {
				hql += " and orderId=" + FormatUtil.formatStrForDB(orderId);
			}
			hql += "order by auditTime";
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}
