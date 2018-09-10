package com.gt.wl.wm.dao;

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

import com.gt.wl.wm.model.WlWmStoreLoss;

@Repository("wl.wm.WlWmStoreLossDao")
public class WlWmStoreLossDao extends BaseDao {

	public WlWmStoreLossDao() {
		modelClass = WlWmStoreLoss.class;
		defaultOrder = "";
	}

	/**
	 * 获取新的通知单流水编号
	 * @return 新的通知单号
	 * @author
	 */
	public List getNewBillNoCode() {
		try {
			String hqlWhere = " 1=1 ";
			String hql = "select max(billNo) from WlWmStoreLoss  where " + hqlWhere;
			Session session = getSession();
			Query query = session.createQuery(hql);
			return query.list();

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取损益列表
	 * @param linkedBillNo 关联订单号
	 * @return 新的通知单号
	 * @author
	 */
	public List findWlWmStoreLossList(String linkedBillNo) {
		try {
			String hql = " from WlWmStoreLoss  where 1=1";
			if (!ValidateUtil.isEmpty(linkedBillNo)) {
				hql += " and linkedBillNo=" + FormatUtil.formatStrForDB(linkedBillNo);
			}
			return this.find(hql);

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询损益单（分页）
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param paraMap 条件
	 * @return 分页结果
	 */
	public Page search(int currPage, int pageSize, Map paraMap) {
		try {
			String hql = " from WlWmStoreLoss where 1=1 ";
			// 单据编号
			if (paraMap.containsKey("billNo")) {
				String billNo = paraMap.get("billNo").toString();
				if (!ValidateUtil.isEmpty(billNo)) {
					hql += " and (billNo like " + FormatUtil.formatStrForDB("%" + billNo + "%") + " or linkedBillNo like " + FormatUtil.formatStrForDB("%" + billNo + "%") + ")";
				}
			}
			// 审核状态
			if (paraMap.containsKey("auditState")) {
				String auditState = paraMap.get("auditState").toString();
				if (!ValidateUtil.isEmpty(auditState)) {
					if ("12".equals(auditState)) {// 12代表审核通过和驳回两种状态
						hql += "  and (auditState =" + FormatUtil.formatStrForDB("1") + " or auditState =" + FormatUtil.formatStrForDB("2") + ")";
					}
					else {
						hql += "  and auditState =" + FormatUtil.formatStrForDB(auditState);

					}

				}
			}
			hql += " order by createTime desc ";
			return this.findPage(hql, currPage, pageSize);

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}
