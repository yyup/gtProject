package com.gt.wl.es.dao;

import java.util.List;

import org.joyone.dao.BaseDao;
import org.joyone.dao.dialect.FormatUtil;
import org.joyone.lang.BaseException;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.es.model.WlEsAgencyOrderDetl;

/**
 * 经销商订单明细DAO层
 * @author liuyj
 * 
 */
@Repository("wl.es.WlEsAgencyOrderDetlDao")
public class WlEsAgencyOrderDetlDao extends BaseDao {

	public WlEsAgencyOrderDetlDao() {
		modelClass = WlEsAgencyOrderDetl.class;
		defaultOrder = "";
	}

	/**
	 * 查询经销商订单明细
	 * @param orderId 经销商订单id
	 * @return 经销商订单明细对象数组
	 */
	public List findAgencyOrderDetlList(String orderId) {
		try {
			String hql = "from WlEsAgencyOrderDetl where 1=1 ";
			if (!ValidateUtil.isEmpty(orderId)) {
				hql += " and orderId=" + FormatUtil.formatStrForDB(orderId);
			}
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 经销商订单 ID 删除经销商订单从表数据
	 * @param orderIds 经销商订单 Ids，格式为('1','2','3')
	 * @return 删除总数
	 */
	public int deleteOrderDetlByOrderId(String orderIds) {
		try {
			String hql = "delete WlEsAgencyOrderDetl o where  o.orderId in" + orderIds;
			return this.executeHql(hql);

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}
