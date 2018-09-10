package com.gt.wl.es.dao;

import java.util.List;

import org.joyone.dao.BaseDao;
import org.joyone.dao.dialect.FormatUtil;
import org.joyone.lang.BaseException;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.es.model.WlEsOrderGood;

/**
 * 订单货物DAO层
 * @author liuyj
 * 
 */
@Repository("wl.es.WlEsOrderGoodDao")
public class WlEsOrderGoodDao extends BaseDao {

	public WlEsOrderGoodDao() {
		modelClass = WlEsOrderGood.class;
		defaultOrder = "";
	}

	/**
	 * 通过订单id查找商品信息列表
	 * @param orderId 订单id
	 * @return 商品信息
	 */
	public List<WlEsOrderGood> findOrderGoodList(String orderId) {
		try {
			String hql = "from WlEsOrderGood  where orderId=?";
			return this.find(hql, orderId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 订单商品
	 * @param orderId 订单id
	 * @param sellId 上架商品id
	 * @return 订单商品
	 */
	public WlEsOrderGood getWlEsOrderGood(String orderId, String sellId) {
		try {
			String hql = "from WlEsOrderGood  where 1=1";
			if (!ValidateUtil.isEmpty(orderId)) {
				hql += " and orderId=" + FormatUtil.formatStrForDB(orderId);
			}
			if (!ValidateUtil.isEmpty(sellId)) {
				hql += " and sellId=" + FormatUtil.formatStrForDB(sellId);
			}
			List list = this.find(hql);
			WlEsOrderGood wlEsOrderGood = null;
			if (!ValidateUtil.isEmpty(list)) {
				wlEsOrderGood = (WlEsOrderGood) list.get(0);
			}
			return wlEsOrderGood;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 删除订单下的所有商品
	 * @param orderId 订单id
	 */
	public void deleteOrderGood(String orderId) {
		try {
			String hql = "delete WlEsOrderGood  where orderId='" + orderId + "'";
			this.executeHql(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}
