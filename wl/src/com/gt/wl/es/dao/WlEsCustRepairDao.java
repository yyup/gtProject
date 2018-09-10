package com.gt.wl.es.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.joyone.dao.BaseDao;
import org.joyone.dao.dialect.FormatUtil;
import org.joyone.lang.BaseException;
import org.joyone.sys.Page;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.es.model.WlEsCustRepair;

/**
 * 售后维修DAO层
 * @author liuyj
 * 
 */
@Repository("wl.es.WlEsCustRepairDao")
public class WlEsCustRepairDao extends BaseDao {

	public WlEsCustRepairDao() {
		modelClass = WlEsCustRepair.class;
		defaultOrder = "";
	}

	/**
	 * 查询售后维修信息(分页)
	 * @param map 参数
	 * @param currPage 当前页
	 * @param pageSize 页数
	 * @return 售后维修信息
	 */
	public Page search(Map map, int currPage, int pageSize) {
		try {
			String hql = "from WlEsCustRepair where 1=1 ";
			// 字段名、字段值
			if (map.containsKey("keyId") && map.containsKey("keyValue")) {
				String keyId = (String) map.get("keyId");
				String keyValue = (String) map.get("keyValue");
				if (!ValidateUtil.isEmpty(keyId) && !ValidateUtil.isEmpty(keyValue)) {
					hql += " and " + keyId + " like " + FormatUtil.formatStrForDB("%" + keyValue + "%");
				}
			}
			// 处理状态
			if (map.containsKey("processStatusEk")) {
				String processStatusEk = (String) map.get("processStatusEk");
				if (!ValidateUtil.isEmpty(processStatusEk)) {
					hql += " and processStatusEk like " + FormatUtil.formatStrForDB("%" + processStatusEk + "%");
				}
			}
			// 验证状态
			if (map.containsKey("serialVerfEk")) {
				String serialVerfEk = (String) map.get("serialVerfEk");
				if (!ValidateUtil.isEmpty(serialVerfEk)) {
					hql += " and serialVerfEk=" + FormatUtil.formatStrForDB(serialVerfEk);
				}
			}
			// 费用支付状态
			if (map.containsKey("paymentStatusEk")) {
				String paymentStatusEk = (String) map.get("paymentStatusEk");
				if (!ValidateUtil.isEmpty(paymentStatusEk)) {
					hql += " and paymentStatusEk=" + FormatUtil.formatStrForDB(paymentStatusEk);
				}
			}
			// 申请时间开始
			if (map.containsKey("applyTimeStart")) {
				String applyTimeStart = (String) map.get("applyTimeStart");
				if (!ValidateUtil.isEmpty(applyTimeStart)) {
					hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("applyTime"));
					hql += " >= " + sqlUtil.string2ShortDate(FormatUtil.formatStrForDB(applyTimeStart));
				}
			}
			// 申请时间结束
			if (map.containsKey("applyTimeEnd")) {
				String applyTimeEnd = (String) map.get("applyTimeEnd");
				if (!ValidateUtil.isEmpty(applyTimeEnd)) {
					hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("applyTime"));
					hql += " <= " + sqlUtil.string2ShortDate(FormatUtil.formatStrForDB(applyTimeEnd));
				}
			}
			// 会员ID
			if (map.containsKey("memberId")) {
				String memberId = (String) map.get("memberId");
				if (!ValidateUtil.isEmpty(memberId)) {
					hql += " and memberId=" + FormatUtil.formatStrForDB(memberId);
				}
			}
			// 单据来源多个用逗号隔开
			if (map.containsKey("billSourceEk")) {
				String billSourceEk = (String) map.get("billSourceEk");
				if (!ValidateUtil.isEmpty(billSourceEk)) {
					String[] billSourceEks = billSourceEk.split(",");
					String nut = "";
					hql += " and (";
					for (String str : billSourceEks) {
						hql += nut + "billSourceEk=" + FormatUtil.formatStrForDB(str);
						nut = " or ";
					}
					hql += " ) ";
				}
			}
			// 收货人
			if (map.containsKey("receiver")) {
				String receiver = (String) map.get("receiver");
				if (!ValidateUtil.isEmpty(receiver)) {
					hql += " and receiver like" + FormatUtil.formatStrForDB("%" + receiver + "%");
				}
			}
			// 服务时间开始
			if (map.containsKey("serviceTimeStart")) {
				String serviceTimeStart = (String) map.get("serviceTimeStart");
				if (!ValidateUtil.isEmpty(serviceTimeStart)) {
					hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("serviceTime"));
					hql += " >= " + sqlUtil.string2ShortDate(FormatUtil.formatStrForDB(serviceTimeStart));
				}
			}
			// 服务时间结束
			if (map.containsKey("serviceTimeEnd")) {
				String serviceTimeEnd = (String) map.get("serviceTimeEnd");
				if (!ValidateUtil.isEmpty(serviceTimeEnd)) {
					hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("serviceTime"));
					hql += " <= " + sqlUtil.string2ShortDate(FormatUtil.formatStrForDB(serviceTimeEnd));
				}
			}
			// 手机号码
			if (map.containsKey("mobile")) {
				String mobile = (String) map.get("mobile");
				if (!ValidateUtil.isEmpty(mobile)) {
					hql += " and mobile like" + FormatUtil.formatStrForDB("%" + mobile + "%");
				}
			}
			hql += "  order by createTime desc";

			return this.findPage(hql, currPage, pageSize);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取新的订单流水编号
	 * @return 新的企业编号
	 * @author wangts 201-12-31
	 */
	public List getNewrepairNoCode() {
		try {
			String hql = "select max(repairNo) from WlEsCustRepair";
			Session session = getSession();
			Query query = session.createQuery(hql);
			return query.list();
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/***
	 * 根据订单号获取售后维修对象
	 * @param orderNo 订单号
	 * @return 售后维修对象
	 */
	public WlEsCustRepair getWlEsCustRepairByOrderNo(String orderNo) {
		try {
			String hql = "from WlEsCustRepair t where t.orderNo = ?";
			List list = this.find(hql, orderNo);
			if (!ValidateUtil.isEmpty(list)) {
				return (WlEsCustRepair) list.get(0);
			}
			return null;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}
