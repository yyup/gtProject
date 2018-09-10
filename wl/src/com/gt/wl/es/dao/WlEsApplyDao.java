package com.gt.wl.es.dao;

import java.util.List;

import org.joyone.dao.BaseDao;
import org.joyone.dao.dialect.FormatUtil;
import org.joyone.lang.BaseException;
import org.joyone.sys.Page;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.es.model.WlEsApply;

/**
 * 配件申请DAO层
 * @author liuyj
 * 
 */
@Repository("wl.es.WlEsApplyDao")
public class WlEsApplyDao extends BaseDao {

	public WlEsApplyDao() {
		modelClass = WlEsApply.class;
		defaultOrder = "";
	}

	/**
	 * 查询配件申请
	 * @param currPage 当前页
	 * @param pageSize 页数
	 * @param keyId 字段名
	 * @param keyValue 字段值
	 * @param isDeliveryFlag 是否已发货
	 * @param applyTimeStart 申请时间开始
	 * @param applyTimeEnd 申请时间结束
	 * @param deliveryTimeStart 发货时间开始
	 * @param deliveryTimeEnd 发货时间结束
	 * @param paymentStatusEk 费用支付状态
	 * @return 分页对象
	 */
	public Page search(int currPage, int pageSize, String keyId, String keyValue, String isDeliveryFlag, String applyTimeStart, String applyTimeEnd, String deliveryTimeStart, String deliveryTimeEnd, String paymentStatusEk) {
		try {
			String hql = "from WlEsApply where 1=1 ";
			if (!ValidateUtil.isEmpty(keyId) && !ValidateUtil.isEmpty(keyValue)) {
				hql += " and " + keyId + " like '%" + keyValue + "%'";
			}
			// 是否已发货
			if (!ValidateUtil.isEmpty(isDeliveryFlag)) {
				hql += " and isDeliveryFlag=" + FormatUtil.formatStrForDB(isDeliveryFlag);
			}

			// 申请时间开始
			if (!ValidateUtil.isEmpty(applyTimeStart)) {
				hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("applyTime"));
				hql += " >= " + sqlUtil.string2ShortDate(FormatUtil.formatStrForDB(applyTimeStart));
			}
			// 申请时间结束
			if (!ValidateUtil.isEmpty(applyTimeEnd)) {
				hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("applyTime"));
				hql += " <= " + sqlUtil.string2ShortDate(FormatUtil.formatStrForDB(applyTimeEnd));
			}
			// 发货时间开始
			if (!ValidateUtil.isEmpty(deliveryTimeStart)) {
				hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("deliveryTime"));
				hql += " >= " + sqlUtil.string2ShortDate(FormatUtil.formatStrForDB(deliveryTimeStart));
			}
			// 发货时间结束
			if (!ValidateUtil.isEmpty(deliveryTimeEnd)) {
				hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("deliveryTime"));
				hql += " <= " + sqlUtil.string2ShortDate(FormatUtil.formatStrForDB(deliveryTimeEnd));
			}
			// 费用支付状态
			if (!ValidateUtil.isEmpty(paymentStatusEk)) {
				hql += " and paymentStatusEk=" + FormatUtil.formatStrForDB(paymentStatusEk);
			}
			hql += "  order by createTime desc";
			return this.findPage(hql, currPage, pageSize);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据订单编号返回配件申请对象
	 * @param orderNo 订单编号
	 * @return 配件申请对象
	 */
	public WlEsApply getWlEsApplyByOrderNo(String orderNo) {
		try {
			String hql = "from WlEsApply t where t.orderNo = ?";
			List list = this.find(hql, orderNo);
			if (!ValidateUtil.isEmpty(list)) {
				return (WlEsApply) list.get(0);
			}
			return null;

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询配件申请列表
	 * @param keyId 字段名
	 * @param keyValue 字段值
	 * @param isDeliveryFlag 是否已发货
	 * @param applyTimeStart 申请时间开始
	 * @param applyTimeEnd 申请时间结束
	 * @param deliveryTimeStart 发货时间开始
	 * @param deliveryTimeEnd 发货时间结束
	 * @param paymentStatusEk 费用支付状态
	 * @return 配件申请列表
	 */
	public List<WlEsApply> findDataList(String keyId, String keyValue, String isDeliveryFlag, String applyTimeStart, String applyTimeEnd, String deliveryTimeStart, String deliveryTimeEnd, String paymentStatusEk) {
		try {
			String hql = "from WlEsApply where 1=1 ";
			if (!ValidateUtil.isEmpty(keyId) && !ValidateUtil.isEmpty(keyValue)) {
				hql += " and " + keyId + " like '%" + keyValue + "%'";
			}
			// 是否已发货
			if (!ValidateUtil.isEmpty(isDeliveryFlag)) {
				hql += " and isDeliveryFlag=" + FormatUtil.formatStrForDB(isDeliveryFlag);
			}

			// 申请时间开始
			if (!ValidateUtil.isEmpty(applyTimeStart)) {
				hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("applyTime"));
				hql += " >= " + sqlUtil.string2ShortDate(FormatUtil.formatStrForDB(applyTimeStart));
			}
			// 申请时间结束
			if (!ValidateUtil.isEmpty(applyTimeEnd)) {
				hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("applyTime"));
				hql += " <= " + sqlUtil.string2ShortDate(FormatUtil.formatStrForDB(applyTimeEnd));
			}
			// 发货时间开始
			if (!ValidateUtil.isEmpty(deliveryTimeStart)) {
				hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("deliveryTime"));
				hql += " >= " + sqlUtil.string2ShortDate(FormatUtil.formatStrForDB(deliveryTimeStart));
			}
			// 发货时间结束
			if (!ValidateUtil.isEmpty(deliveryTimeEnd)) {
				hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("deliveryTime"));
				hql += " <= " + sqlUtil.string2ShortDate(FormatUtil.formatStrForDB(deliveryTimeEnd));
			}
			// 费用支付状态
			if (!ValidateUtil.isEmpty(paymentStatusEk)) {
				hql += " and paymentStatusEk=" + FormatUtil.formatStrForDB(paymentStatusEk);
			}
			hql += "  order by createTime desc";
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}
