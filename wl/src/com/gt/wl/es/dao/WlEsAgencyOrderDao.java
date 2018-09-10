package com.gt.wl.es.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.joyone.dao.BaseDao;
import org.joyone.dao.dialect.FormatUtil;
import org.joyone.lang.BaseException;
import org.joyone.sys.Page;
import org.joyone.util.ConvertUtil;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.es.model.WlEsAgencyOrder;

/**
 * 经销商订单DAO层
 * @author liuyj
 * 
 */
@Repository("wl.es.WlEsAgencyOrderDao")
public class WlEsAgencyOrderDao extends BaseDao {

	public WlEsAgencyOrderDao() {
		modelClass = WlEsAgencyOrder.class;
		defaultOrder = "";
	}

	/**
	 * 查询经销商订单（分页）
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param paraMap 条件
	 * @return 分页结果
	 */
	public Page search(int currPage, int pageSize, Map paraMap) {
		try {
			String hql = " from WlEsAgencyOrder where 1=1 ";
			// 单号
			if (paraMap.containsKey("orderNo")) {
				String orderNo = paraMap.get("orderNo").toString();
				if (!ValidateUtil.isEmpty(orderNo)) {
					hql += " and orderNo like " + FormatUtil.formatStrForDB("%" + orderNo + "%");
				}
			}
			// 单据状态
			if (paraMap.containsKey("agencyOrderStateEk")) {
				String agencyOrderStateEk = paraMap.get("agencyOrderStateEk").toString();
				if (!ValidateUtil.isEmpty(agencyOrderStateEk)) {
					hql += " and agencyOrderStateEk =" + FormatUtil.formatStrForDB(agencyOrderStateEk);
				}
			}
			// 如果是经销商订单审核，排除草稿状态
			if (paraMap.containsKey("agencyOrderAudit")) {
				String agencyOrderStateEk = paraMap.get("agencyOrderAudit").toString();
				hql += " and agencyOrderStateEk !=" + FormatUtil.formatStrForDB(agencyOrderStateEk);
			}
			// 订单开始日期
			if (paraMap.containsKey("fromDate")) {
				String fromDate = (String) paraMap.get("fromDate");
				if (!ValidateUtil.isEmpty(fromDate)) {
					hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("orderTime"));
					hql += " >= " + sqlUtil.string2ShortDate(FormatUtil.formatStrForDB(fromDate));
				}
			}

			// 订单结束日期
			if (paraMap.containsKey("toDate")) {
				String toDate = (String) paraMap.get("toDate");
				if (!ValidateUtil.isEmpty(toDate)) {
					hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("orderTime"));
					hql += " <= " + sqlUtil.string2ShortDate(FormatUtil.formatStrForDB(toDate));
				}
			}
			// 下单单位
			if (paraMap.containsKey("orgName")) {
				String orgName = paraMap.get("orgName").toString();
				if (!ValidateUtil.isEmpty(orgName)) {
					hql += " and orgName like " + FormatUtil.formatStrForDB("%" + orgName + "%");
				}
			}
			// 当前登录用户
			if (paraMap.containsKey("userId")) {
				String userId = paraMap.get("userId").toString();
				if (!ValidateUtil.isEmpty(userId)) {
					hql += " and userId =" + FormatUtil.formatStrForDB(userId);
				}
			}
			hql += "  order by orderTime desc";
			return this.findPage(hql, currPage, pageSize);

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取新的通知单流水编号
	 * @return 新的通知单号
	 * @author
	 */
	public List getNewOrderNoCode() {
		try {
			String hqlWhere = " 1=1 ";
			String hql = "select max(orderNo) from WlEsAgencyOrder  where " + hqlWhere;
			Session session = getSession();
			Query query = session.createQuery(hql);
			return query.list();

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 经销商订单 ID 删除经销商订单数据
	 * @param orderIds 经销商订单 Ids，格式为('1','2','3')
	 * @return 删除总数
	 */
	public int deleteOrderByOrderId(String orderIds) {
		try {
			String hql = "delete WlEsAgencyOrder o where  o.orderId in" + orderIds;
			return this.executeHql(hql);

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询经销商订单列表
	 * @param orderIds 经销商订单 Ids，格式为('1','2','3')
	 * @param agencyOrderStateEk 订单状态（排除）
	 * @return 经销商订单列表
	 */
	public List findOrderList(String orderIds, String agencyOrderStateEk) {
		try {
			String hql = "from WlEsAgencyOrder  where 1=1";
			if (!ValidateUtil.isEmpty(orderIds)) {
				hql += " and orderId in" + orderIds;
			}
			if (!ValidateUtil.isEmpty(agencyOrderStateEk)) {
				hql += " and agencyOrderStateEk !=" + FormatUtil.formatStrForDB(agencyOrderStateEk);
			}
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取物料的下单数量
	 * @param itemId 物料id
	 * @param agencyOrderStateEk 订单状态 ，多个用英文逗号隔开
	 */
	public double getOrderItemCount(String itemId, String agencyOrderStateEk) {
		try {
			String hql = "select sum(od.baseUnitQty) from WlEsAgencyOrder o,WlEsAgencyOrderDetl od where o.orderId=od.orderId";
			if (!ValidateUtil.isEmpty(itemId)) {
				hql += " and od.itemId=" + FormatUtil.formatStrForDB(itemId);
			}
			if (!ValidateUtil.isEmpty(agencyOrderStateEk)) {
				hql += " and o.agencyOrderStateEk in" + ConvertUtil.toDbString(agencyOrderStateEk.split(","));
			}
			double sumQty = 0;
			List list = this.find(hql);
			if (!ValidateUtil.isEmpty(list)) {
				sumQty = Double.parseDouble(list.get(0).toString());
			}
			return sumQty;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取已经实际出库的数量
	 * @param itemId 物料id
	 * @param agencyOrderStateEk 订单状态 ，多个用英文逗号隔开
	 */
	public double getOrderOutCount(String itemId, String agencyOrderStateEk) {
		try {
			String hql = "select sum(sod.baseUnitQty) from WlEsAgencyOrder ao,WlWmNoticeOut no,WlWmStoreOut so,WlWmStoreOutDetl sod";
			hql += " where ao.orderId=no.linkedMasId and no.noticeNo=so.noticeNo and so.storeOutId=sod.storeOutId";
			if (!ValidateUtil.isEmpty(itemId)) {
				hql += " and sod.itemId=" + FormatUtil.formatStrForDB(itemId);
			}
			if (!ValidateUtil.isEmpty(agencyOrderStateEk)) {
				hql += " and ao.agencyOrderStateEk in" + ConvertUtil.toDbString(agencyOrderStateEk.split(","));
			}
			double sumQty = 0;
			List list = this.find(hql);
			if (!ValidateUtil.isEmpty(list)) {
				sumQty = Double.parseDouble(list.get(0).toString());
			}
			return sumQty;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}
