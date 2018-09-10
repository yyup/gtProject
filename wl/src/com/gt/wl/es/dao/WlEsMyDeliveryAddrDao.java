package com.gt.wl.es.dao;

import java.util.List;

import org.joyone.dao.BaseDao;
import org.joyone.lang.BaseException;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.es.model.WlEsMyDeliveryAddr;

/**
 * 我的收货地址DAO层
 * @author liuyj
 */
@Repository("wl.es.WlEsMyDeliveryAddrDao")
public class WlEsMyDeliveryAddrDao extends BaseDao {

	public WlEsMyDeliveryAddrDao() {
		modelClass = WlEsMyDeliveryAddr.class;
		defaultOrder = "";
	}

	/**
	 * 通过会员ID得到会员默认收货地址
	 * @param memberId 会员ID
	 * @return 收货地址
	 */
	public String getDeliverAddrByMemId(String memberId) {
		String addr = "";
		try {
			String hql = "select addr from WlEsMyDeliveryAddr  where memberId = '" + memberId + "' and isDefaultFlag = '1'";
			List list = this.find(hql);
			if (!ValidateUtil.isEmpty(list)) {
				addr = list.get(0).toString();
			}
			return addr;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过会员ID得到收货地址列表
	 * @param memberId 会员id
	 * @return 收货地址列表
	 */
	public List<WlEsMyDeliveryAddr> findMyDeliveryAddrList(String memberId) {
		try {
			String hql = " from WlEsMyDeliveryAddr  where memberId = '" + memberId + "' order by isDefaultFlag desc";
			return this.find(hql);

		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过会员ID得到默认收货地址
	 * @author luozp app接口
	 * @param memberId 会员id
	 * @param isDefaultFlag 是否默认地址
	 * @return 地址列表
	 */
	public WlEsMyDeliveryAddr getMyDeliveryAddrData(String memberId, String isDefaultFlag) {
		try {
			String hql = " from WlEsMyDeliveryAddr  where memberId = '" + memberId + "'";
			if (!ValidateUtil.isEmpty(isDefaultFlag)) {
				hql += " and isDefaultFlag = '" + isDefaultFlag + "'";
			}
			hql += " order by createTime desc";
			List list = this.find(hql);
			if (!ValidateUtil.isEmpty(list)) {
				return (WlEsMyDeliveryAddr) list.get(0);
			}
			return null;
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}
}
