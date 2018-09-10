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

import com.gt.wl.wm.model.WlWmNoticeOut;

/**
 * 出库通知DAO层
 * @author liuyj
 * 
 */
@Repository("wl.wm.WlWmNoticeOutDao")
public class WlWmNoticeOutDao extends BaseDao {

	public WlWmNoticeOutDao() {
		modelClass = WlWmNoticeOut.class;
		defaultOrder = "";
	}

	/**
	 * 获取待办单数
	 * @param storageId 仓库ID（为空或null则查询所有仓库）
	 * @return 待办单数
	 */
	public long getTodoCount(String storageId) {
		try {
			// 单据状态为“已下发”并且结果为“未执行”
			String hql = "select count(*) from WlWmNoticeOut where billStateEk='ISSUE' and resultEk='NOT_EXECUTE'";
			if (!ValidateUtil.isEmpty(storageId)) {
				hql += " and storageId=" + FormatUtil.formatStrForDB(storageId);
			}
			Long total = (Long) this.find(hql).get(0);
			return total;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询出库通知单（分页）
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param paraMap 条件
	 * @return 分页结果
	 */
	public Page search(int currPage, int pageSize, Map paraMap) {
		try {
			String hql = " from WlWmNoticeOut where 1=1 ";
			// 通知单号
			if (paraMap.containsKey("noticeNo")) {
				String noticeNo = paraMap.get("noticeNo").toString();
				if (!ValidateUtil.isEmpty(noticeNo)) {
					hql += " and (noticeNo like " + FormatUtil.formatStrForDB("%" + noticeNo + "%") + " or linkedBillNo like" + FormatUtil.formatStrForDB("%" + noticeNo + "%") + ")";
				}
			}
			// 仓库
			if (paraMap.containsKey("storageId")) {
				String storageId = paraMap.get("storageId").toString();
				if (!ValidateUtil.isEmpty(storageId)) {
					hql += " and storageId =" + FormatUtil.formatStrForDB(storageId);
				}
			}
			// 库区
			if (paraMap.containsKey("storageAreaId")) {
				String storageAreaId = paraMap.get("storageAreaId").toString();
				if (!ValidateUtil.isEmpty(storageAreaId)) {
					hql += " and storageAreaId =" + FormatUtil.formatStrForDB(storageAreaId);
				}
			}
			// 出库业务类型
			if (paraMap.containsKey("wmOutTypeEk")) {
				String wmOutTypeEk = paraMap.get("wmOutTypeEk").toString();
				if (!ValidateUtil.isEmpty(wmOutTypeEk)) {
					hql += " and wmOutTypeEk =" + FormatUtil.formatStrForDB(wmOutTypeEk);
				}
			}
			// 单据状态
			if (paraMap.containsKey("billStateEk")) {
				String billStateEk = paraMap.get("billStateEk").toString();
				if (!ValidateUtil.isEmpty(billStateEk)) {
					hql += " and billStateEk =" + FormatUtil.formatStrForDB(billStateEk);
				}
			}
			// 结果状态
			if (paraMap.containsKey("resultEk")) {
				String resultEk = paraMap.get("resultEk").toString();
				if (!ValidateUtil.isEmpty(resultEk)) {
					hql += " and resultEk =" + FormatUtil.formatStrForDB(resultEk);
				}
			}
			// 制单开始时间
			if (paraMap.containsKey("beginDate")) {
				String beginDate = paraMap.get("beginDate").toString();
				if (!ValidateUtil.isEmpty(beginDate)) {
					hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("createTime"));
					hql = hql + "  >= " + this.sqlUtil.string2Date(FormatUtil.formatStrForDB(beginDate));
				}
			}
			// 制单结束时间
			if (paraMap.containsKey("endDate")) {
				String endDate = paraMap.get("endDate").toString();
				if (!ValidateUtil.isEmpty(endDate)) {
					hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("createTime"));
					hql = hql + "  <=" + this.sqlUtil.string2Date(FormatUtil.formatStrForDB(endDate));
				}
			}
			hql += " order by createTime desc";

			return this.findPage(hql, currPage, pageSize);

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询出库通知单（分页）按型号分组
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param paraMap 条件
	 * @return 分页结果
	 */
	public Page searchSpec(int currPage, int pageSize, Map paraMap) {
		try {
			String noticeSql = " SELECT mm.NOTICE_OUT_ID,mm.NOTICE_NO,mm.STORAGE_NAME,ss.NOTICE_OUT_DETL_ID,ss.ITEM_ID,ss.ITEM_NAME,ss.SPEC,ss.BASE_UNIT_QTY,ss.CONSIGNEE,ss.CONTACT_WAY " + " FROM wl_wm_notice_out mm,wl_wm_notice_out_detl ss "
					+ " WHERE mm.NOTICE_OUT_ID=ss.NOTICE_OUT_ID AND mm.BILL_STATE_EK='ISSUE' ";
			String storeSql = " SELECT mm.store_out_id,mm.notice_no,ss.store_out_detl_id, ss.item_id,ss.base_unit_qty,ss.consignee,ss.contact_way " + " FROM wl_wm_store_out mm,wl_wm_store_out_detl ss " + " WHERE mm.store_out_id=ss.store_out_id";
			// 仓库
			if (paraMap.containsKey("storageId")) {
				String storageId = (String) paraMap.get("storageId");
				if (!ValidateUtil.isEmpty(storageId)) {
					noticeSql += " and mm.storage_Id =" + FormatUtil.formatStrForDB(storageId);
				}
			}
			// 预计出库日期
			if (paraMap.containsKey("expectOutDate")) {
				String expectOutDate = (String) paraMap.get("expectOutDate");
				if (!ValidateUtil.isEmpty(expectOutDate)) {
					noticeSql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("mm.expect_Out_Date"));
					noticeSql = noticeSql + " = " + this.sqlUtil.string2Date(FormatUtil.formatStrForDB(expectOutDate));
				}
			}
			// 型号
			if (paraMap.containsKey("spec")) {
				String spec = (String) paraMap.get("spec");
				if (!ValidateUtil.isEmpty(spec)) {
					noticeSql += " and ss.spec like " + FormatUtil.formatStrForDB("%" + spec + "%");
				}
			}
			// 物料类别id，格式为('1','2','3')
			if (paraMap.containsKey("categoryId")) {
				String categoryId = (String) paraMap.get("categoryId");
				if (!ValidateUtil.isEmpty(categoryId)) {
					noticeSql += " and ss.CATEGORY_ID in " + categoryId;

				}
			}
			// 组合sql
			String sql = "SELECT m.notice_out_id AS notice_out_id,m.notice_no AS notice_no,m.storage_name AS storage_name," + "m.item_id AS item_id,m.item_name AS item_name,m.spec AS spec,m.base_unit_qty AS base_unit_qty,"
					+ "m.consignee AS consignee,m.contact_way AS contact_way,s.store_out_id AS store_out_id,s.base_unit_qty AS has_base_unit_qty,m.notice_out_detl_id,s.store_out_detl_id " + " FROM (" + noticeSql + ") m LEFT JOIN (" + storeSql
					+ ") s " + " ON m.notice_no=s.notice_no AND m.item_id=s.item_id AND m.consignee=s.consignee AND m.contact_way=s.contact_way" + " WHERE (s.base_unit_qty IS NULL OR m.base_unit_qty<>s.base_unit_qty) " + " ORDER BY m.item_id";
			return this.findPageByFreeSQL(sql, currPage, pageSize);
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
	public List getNewNoticeNoCode() {
		try {
			String hqlWhere = " 1=1 ";
			String hql = "select max(noticeNo) from WlWmNoticeOut  where " + hqlWhere;
			Session session = getSession();
			Query query = session.createQuery(hql);
			return query.list();

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询出库通知单对象数组
	 * @param noticeOutIds 入库通知单ids，格式为('1','2','3')
	 * @param billStateEk 单据状态
	 * @param resultEk 结果状态
	 * @return 通知单对象列表
	 */
	public List findNoticeOutList(String noticeOutIds, String billStateEk, String resultEk) {
		try {
			String hql = "from WlWmNoticeOut  where 1=1 ";
			if (!ValidateUtil.isEmpty(noticeOutIds)) {
				hql += " and noticeOutId in " + noticeOutIds;
			}
			if (!ValidateUtil.isEmpty(billStateEk)) {
				hql += " and billStateEk !=" + FormatUtil.formatStrForDB(billStateEk);

			}
			if (!ValidateUtil.isEmpty(resultEk)) {
				hql += " and resultEk !=" + FormatUtil.formatStrForDB(resultEk);

			}
			return this.find(hql);

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 更改出库通知单的状态
	 * @param noticeOutIds 出库通知单ids，格式为('1','2','3')
	 * @param billStateEk 单据状态
	 * @param issuer 发布人
	 * @param date 发布时间(格式yyyy-MM-dd HH:mm:ss)
	 */
	public void updateNoticeOutState(String noticeOutIds, String billStateEk, String issuer, String date) {
		try {
			String hql = "update WlWmNoticeOut  set ";
			if (!ValidateUtil.isEmpty(billStateEk)) {
				hql += "  billStateEk =" + FormatUtil.formatStrForDB(billStateEk);

			}
			if (!ValidateUtil.isEmpty(issuer) && !ValidateUtil.isEmpty(date)) {
				hql += " ,issuer =" + FormatUtil.formatStrForDB(issuer);
				hql += " ,issueTime=" + FormatUtil.formatStrForDB(date);
			}
			if (!ValidateUtil.isEmpty(noticeOutIds)) {
				hql += "  where  noticeOutId in " + noticeOutIds;
			}

			this.executeHql(hql);

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 删除出库通知单
	 * @param noticeOutIds id数组转化成dbString，格式为('1','2','3')
	 */
	public void removeData(String noticeOutIds) {
		try {
			String hqlWhere = "  a.noticeOutId in " + noticeOutIds;
			String hql = "delete from WlWmNoticeOut  a where " + hqlWhere;
			this.executeHql(hql);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过通知单号获取出库通知单
	 * @param noticeNo 通知单号
	 * @return 出库通知单对象
	 */
	public WlWmNoticeOut getData(String noticeNo) {
		try {
			WlWmNoticeOut wlWmNoticeOut = null;
			String hql = "from WlWmNoticeOut where noticeNo=?";
			List list = this.find(hql, noticeNo);
			if (!ValidateUtil.isEmpty(list)) {
				wlWmNoticeOut = (WlWmNoticeOut) list.get(0);
			}
			return wlWmNoticeOut;
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查找出库发货的相关信息
	 * @param noticeOutIds id数组转化成dbString，格式为('1','2','3')
	 * @return 出库发货的相关信息
	 */
	public List findNoticeOutAndDetlList(String noticeOutIds) {
		try {
			String hql = "select new map(b.consignee as consignee,b.contactWay as contactWay,b.addr as addr,a.orgName as orgName,b.spec as spec,b.baseUnitQty as baseUnitQty) from WlWmNoticeOut a,WlWmNoticeOutDetl b";
			hql += " where a.noticeOutId=b.noticeOutId";
			if (!ValidateUtil.isEmpty(noticeOutIds)) {
				hql += " and a.noticeOutId in" + noticeOutIds;
			}
			return this.find(hql);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 备货数据
	 * @param paraMap 条件
	 * @return 备货数据
	 */
	public List findStoreOutStockList(Map paraMap) {
		try {
			String hql = "select new map(a.storageName as storageName,a.wmOutTypeEk as wmOutTypeEk,a.expectOutDate as expectOutDate";
			hql += " ,b.itemCd as itemCd,b.itemName as itemName, b.spec as spec ,b.baseUnitName as baseUnitName,sum(b.baseUnitQty) as baseUnitQty)";
			hql += " from WlWmNoticeOut a,WlWmNoticeOutDetl b where a.noticeOutId=b.noticeOutId ";
			/**
			 * 仓库
			 */
			if (paraMap.containsKey("storageId")) {
				String storageId = paraMap.get("storageId").toString();
				if (!ValidateUtil.isEmpty(storageId)) {
					hql += " and a.storageId=" + FormatUtil.formatStrForDB(storageId);
				}
			}
			/**
			 * 业务类型
			 * */
			if (paraMap.containsKey("wmOutTypeEk")) {
				String wmOutTypeEk = paraMap.get("wmOutTypeEk").toString();
				if (!ValidateUtil.isEmpty(wmOutTypeEk)) {
					hql += " and a.wmOutTypeEk=" + FormatUtil.formatStrForDB(wmOutTypeEk);
				}
			}
			// 制单开始时间
			if (paraMap.containsKey("beginDate")) {
				String beginDate = paraMap.get("beginDate").toString();
				if (!ValidateUtil.isEmpty(beginDate)) {
					hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("a.expectOutDate"));
					hql = hql + "  >= " + this.sqlUtil.string2Date(FormatUtil.formatStrForDB(beginDate));
				}
			}
			// 制单结束时间
			if (paraMap.containsKey("endDate")) {
				String endDate = paraMap.get("endDate").toString();
				if (!ValidateUtil.isEmpty(endDate)) {
					hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("a.expectOutDate"));
					hql = hql + "  <=" + this.sqlUtil.string2Date(FormatUtil.formatStrForDB(endDate));
				}
			}
			/**
			 * 物料编码
			 */
			if (paraMap.containsKey("itemCd")) {
				String itemCd = paraMap.get("itemCd").toString();
				if (!ValidateUtil.isEmpty(itemCd)) {
					hql += " and b.itemCd like " + FormatUtil.formatStrForDB("%" + itemCd + "%");
				}
			}
			/**
			 * 物料名称
			 */
			if (paraMap.containsKey("itemName")) {
				String itemName = paraMap.get("itemName").toString();
				if (!ValidateUtil.isEmpty(itemName)) {
					hql += " and b.itemName like " + FormatUtil.formatStrForDB("%" + itemName + "%");
				}
			}
			/**
			 * 规格型号
			 */
			if (paraMap.containsKey("spec")) {
				String spec = paraMap.get("spec").toString();
				if (!ValidateUtil.isEmpty(spec)) {
					hql += " and b.spec like " + FormatUtil.formatStrForDB("%" + spec + "%");
				}
			}
			hql += " group by a.storageId,a.wmOutTypeEk,a.expectOutDate,b.itemId";
			hql += " order by a.expectOutDate desc";

			return this.find(hql);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}
}
