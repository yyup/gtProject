package com.gt.wl.es.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.joyone.dao.BaseDao;
import org.joyone.dao.dialect.FormatUtil;
import org.joyone.lang.BaseException;
import org.joyone.sys.Page;
import org.joyone.util.DateUtil;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.es.model.WlEsOrder;

/**
 * 订单DAO层
 * @author liuyj
 * 
 */
@Repository("wl.es.WlEsOrderDao")
public class WlEsOrderDao extends BaseDao {

	public WlEsOrderDao() {
		modelClass = WlEsOrder.class;
		defaultOrder = "";
	}

	/**
	 * 通过会员ID得到其订单总数
	 * @param memberId 会员id
	 * @return 订单总数
	 */
	public int getOrderTotalByMememberId(String memberId) {
		try {
			String hql = "select count(*) from WlEsOrder  where memberId =" + FormatUtil.formatStrForDB(memberId);
			Long total = (Long) this.find(hql).get(0);
			return total.intValue();
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过会员ID得到已消费金额
	 * 订单状态为1,2,3(1：待发货、2:已发货、3：成功订单)
	 * @param memberId 会员id
	 * @return 已消费金额
	 */
	public double getAmountCountByMemId(String memberId) {
		try {
			String hql = "select sum(amt) from WlEsOrder  where memberId = " + FormatUtil.formatStrForDB(memberId);
			hql += " and orderStateEk in ('1','2','3')";
			return (Double) this.find(hql).get(0);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据会员ID得到订单信息
	 * @param memberId 会员ID
	 * @return 订单信息
	 */
	public WlEsOrder getOrderByMemId(String memberId) {
		try {
			String hql = "from WlEsOrder where memberId = " + FormatUtil.formatStrForDB(memberId);
			List list = this.find(hql);
			if (!ValidateUtil.isEmpty(list)) {
				return (WlEsOrder) list.get(0);
			}
			return null;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询订单信息(分页)
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param orderNo 订单号
	 * @param orderStateEk 订单状态
	 * @param memberId 会员ID
	 * @return 分页结果
	 */
	public Page search(int currPage, int pageSize, String memberId, String orderNo, String orderStateEk) {
		try {

			String hql = "from WlEsOrder  where memberId = " + FormatUtil.formatStrForDB(memberId);

			if (!ValidateUtil.isEmpty(orderStateEk)) {
				hql += " and orderStateEk = " + FormatUtil.formatStrForDB(orderStateEk);
			}
			if (!ValidateUtil.isEmpty(orderNo)) {
				hql += " and orderNo like " + FormatUtil.formatStrForDB("%" + orderNo + "%");
			}
			return this.findPage(hql, currPage, pageSize);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询订单信息(分页，供订单管理四个Tab页使用)
	 * @param currPage 当前页
	 * @param pageSize 页数
	 * @param orderStateEk 订单状态
	 * @param values 字段名
	 * @param namesValue 字段值
	 * @param evaluateState 评价状态
	 * @param goodName 商品名称
	 * @return 订单信息
	 */
	public Page search(int pageSize, int currPage, String orderStateEk, String values, String namesValue, String evaluateState,String goodName) {
		try {
			String hql = "select new map(m.orderId as orderId,m.orderNo as orderNo,m.orderTime as orderTime,m.amt as amt,m.payAmt as payAmt,m.payModeEk as payModeEk,m.orderStateEk as orderStateEk,"
					+ "m.goodNum as goodNum,m.deliveryNo as deliveryNo,m.deliveryTime as deliveryTime,m.closeTime as closeTime,m.province as province,m.city as city ";
			if ("1".equals(orderStateEk) || "2".equals(orderStateEk)) {// 待发货和已发货(因天猫订单无会员)
				hql += ") from WlEsOrder m where 1=1 ";
			}
			else {
				hql += ",s.name as memberName,s.mobile as mobile) from WlEsOrder m,WlCmMember s where m.memberId=s.memberId ";
			}
			//商品名称
			if(!ValidateUtil.isEmpty(goodName)){
				hql += " and m.orderId in (select g.orderId from WlEsOrderGood g where 1=1 ";
				hql += " and g.productName like '%" + goodName + "%' ";
				hql += " ) ";
			}
			// 订单状态
			if (!ValidateUtil.isEmpty(orderStateEk)) {
				if ("0".equals(orderStateEk)) {// 待付款
					hql += " and m.orderStateEk ='0' and m.payModeEk<>'0' ";
				}
				else if ("1".equals(orderStateEk)) {// 待发货
					hql += " and (m.orderStateEk ='1' or ( m.orderStateEk ='0' and m.payModeEk='0' ))";
				}
				else if ("2".equals(orderStateEk)) {// 已发货
					hql += " and m.orderStateEk ='2' ";
				}
				else if ("4".equals(orderStateEk)) {// 无效订单
					hql += " and (m.orderStateEk ='4' or m.orderStateEk ='5')";
				}
			}
		
			// 其他key值
			if (!ValidateUtil.isEmpty(values) && !ValidateUtil.isEmpty(namesValue)) {
				hql += " and " + values + " like '%" + namesValue + "%' ";
			}
			hql += " order by m.orderTime desc ";
			return this.findPage(hql, currPage, pageSize);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询订单信息(分页)
	 * @param currPage 当前页
	 * @param pageSize 页数
	 * @param paraMap 参数Map对象
	 * @return 订单信息
	 */
	public Page search(int pageSize, int currPage, Map paraMap) {
		try {
			String hql = "select new map(m.orderId as orderId,m.orderNo as orderNo,m.orderTime as orderTime,m.amt as amt,m.payAmt as payAmt,m.payModeEk as payModeEk,m.orderStateEk as orderStateEk,"
					+ "m.goodNum as goodNum,m.deliveryNo as deliveryNo,m.deliveryTime as deliveryTime,m.closeTime as closeTime,m.province as province,m.city as city,s.name as memberName,s.mobile as mobile) "
					+ "from WlEsOrder m,WlCmMember s where m.memberId=s.memberId ";
			// 订单状态
			if (paraMap.containsKey("orderStateEk")) {
				String orderStateEk = (String) paraMap.get("orderStateEk");
				if (!ValidateUtil.isEmpty(orderStateEk)) {
					hql += " and m.orderStateEk = " + FormatUtil.formatStrForDB(orderStateEk);
				}
			}

			// 订单开始日期
			if (paraMap.containsKey("fromDate")) {
				String fromDate = (String) paraMap.get("fromDate");
				if (!ValidateUtil.isEmpty(fromDate)) {
					hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("m.orderTime"));
					hql += " >= " + sqlUtil.string2ShortDate(FormatUtil.formatStrForDB(fromDate));
				}
			}

			// 订单结束日期
			if (paraMap.containsKey("toDate")) {
				String toDate = (String) paraMap.get("toDate");
				if (!ValidateUtil.isEmpty(toDate)) {
					hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("m.orderTime"));
					hql += " <= " + sqlUtil.string2ShortDate(FormatUtil.formatStrForDB(toDate));
				}
			}
			hql += " order by m.orderTime desc ";
			return this.findPage(hql, currPage, pageSize);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询订单信息
	 * @param orderStateEk 状态
	 * @param fromDate 开始时间
	 * @param toDate 结束时间
	 * @return 数据列表
	 */
	public List<Map> findOrderList(String orderStateEk, String fromDate, String toDate) {
		try {
			String hql = "select new map(m.orderId as orderId,m.orderNo as orderNo,m.orderTime as orderTime,m.amt as amt,m.payAmt as payAmt,m.payModeEk as payModeEk,m.orderStateEk as orderStateEk,"
					+ "m.goodNum as goodNum,m.deliveryNo as deliveryNo,m.deliveryTime as deliveryTime,m.closeTime as closeTime,m.province as province,m.city as city,s.name as memberName,s.mobile as mobile) "
					+ "from WlEsOrder m,WlCmMember s where m.memberId=s.memberId ";

			// 订单状态
			if (!ValidateUtil.isEmpty(orderStateEk)) {
				hql += " and m.orderStateEk = " + FormatUtil.formatStrForDB(orderStateEk);
			}
			// 日期开始
			if (!ValidateUtil.isEmpty(fromDate)) {
				hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("m.orderTime"));
				hql += " >= " + sqlUtil.string2ShortDate(FormatUtil.formatStrForDB(fromDate));
			}
			// 日期结束
			if (!ValidateUtil.isEmpty(toDate)) {
				hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("m.orderTime"));
				hql += " <= " + sqlUtil.string2ShortDate(FormatUtil.formatStrForDB(toDate));
			}
			hql += " order by m.orderTime desc ";
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据订单编号返回订单对象
	 * @param orderNo 订单编号
	 * @return 订单对象
	 */
	public WlEsOrder getWlEsOrderByOrderNo(String orderNo) {
		try {
			String hql = "from WlEsOrder t where t.orderNo = ?";
			List list = this.find(hql, orderNo);
			if (!ValidateUtil.isEmpty(list)) {
				return (WlEsOrder) list.get(0);
			}
			return null;

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取新的订单流水编号
	 * @param buyChannelEk 购买渠道
	 * @return 新的企业编号
	 */
	public List getNewOrderCode(String buyChannelEk) {
		try {
			String hqlWhere = " 1=1 ";
			if (!ValidateUtil.isEmpty(buyChannelEk)) {
				hqlWhere += " and buyChannelEk=" + FormatUtil.formatStrForDB(buyChannelEk);
			}
			String hql = "select max(orderNo) from WlEsOrder  where " + hqlWhere;
			Session session = getSession();
			Query query = session.createQuery(hql);
			return query.list();

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取当前登录用户的订单
	 * @param currPage 当前页
	 * @param pageSize 页数
	 * @param type 获取数据时间类型，0：一个月内，1：一个月前
	 * @param memberId 会员id
	 * @return 用户订单列表
	 */
	public Page findOrderPageByMember(int pageSize, int currPage, String type, String memberId) {
		try {
			String hqlWhere = " 1=1 and  memberId='" + memberId + "' ";
			Date begin = new Date();
			begin = DateUtil.getAnotherTimeFromThisTime(begin, -30);
			if ("1".endsWith(type)) {// 一个月前
				hqlWhere += " and orderTime <= '" + DateUtil.dateFormatFromDateToString(begin, "yyyy-MM-dd HH:mm:ss") + "'";
			}
			else {
				hqlWhere += " and orderTime >= '" + DateUtil.dateFormatFromDateToString(begin, "yyyy-MM-dd HH:mm:ss") + "'";
			}
			String hql = "from WlEsOrder  where " + hqlWhere + " order by orderNo desc";
			return this.findPage(hql, currPage, pageSize);

		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 将指定日期之前的待付款订单关闭
	 * @param orderStateEk 订单状态
	 * @param curDate 当前时间
	 * @param date 指定时间
	 */
	public void updateOrderState(String orderStateEk, String curDate, String date) {
		try {
			if (!ValidateUtil.isEmpty(orderStateEk)) {// closeTime orderTime
				String hql = "update WlEsOrder  set orderStateEk=" + FormatUtil.formatStrForDB(orderStateEk);
				if (!ValidateUtil.isEmpty(curDate)) {
					hql += " ,closeTime=" + FormatUtil.formatStrForDB(curDate);
				}
				if (!ValidateUtil.isEmpty(date)) {
					hql += " where orderStateEk='0' and orderTime<=" + FormatUtil.formatStrForDB(date);
				}
				this.executeHql(hql);
			}
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}
}
