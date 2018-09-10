package com.gt.wl.wm.dao;

import java.util.List;
import java.util.Map;

import org.joyone.dao.BaseDao;
import org.joyone.dao.dialect.FormatUtil;
import org.joyone.lang.BaseException;
import org.joyone.sys.Page;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.wm.model.WlWmStoreOut;

@Repository("wl.wm.WlWmStoreOutDao")
public class WlWmStoreOutDao extends BaseDao {

	public WlWmStoreOutDao() {
		modelClass = WlWmStoreOut.class;
		defaultOrder = "";
	}

	/**
	 * 查询出库单列表
	 * @param flag 调用标志(入库单据管理为wlWmStoreOut，入库明细查为wlwmStoreOutQuery）
	 * @param billNo 单据编号
	 * @param beginDate 制单开始时间
	 * @param endDate 制单结束时间
	 * @param storageId 仓库id
	 * @param createor 经办人
	 * @param wmOutTypeEk 业务类型
	 * @param orgId 往来单位id
	 * @param itemCd 物料编码
	 * @param itemName 物料名称
	 * @param spec 规格型号
	 * @param lockFlagEk 单据状态
	 * @param consignee 收货人
	 * @return 出库单列表
	 */
	public List<Map> findStoreOutList(String flag, String billNo, String beginDate, String endDate, String storageId, String createor, String wmOutTypeEk, String orgId, String itemCd, String itemName, String spec, String lockFlagEk, String consignee) {
		try {
			String hql = "";
			if ("wlWmStoreOut".equals(flag)) {
				hql = " select new map(a.storeOutId as storeOutId,a.billNo as billNo,a.totalQty as totalQty,a.wmOutTypeEk as wmOutTypeEk,a.storageName as storageName,a.outDate as outDate ";
				hql += ",a.createor as createor,a.lockFlagEk as lockFlagEk)";
				hql += "  from WlWmStoreOut a where 1=1 ";
			}
			else if ("wlwmStoreOutQuery".equals(flag)) {
				hql = " select new map(";
				hql += "a.storageName as storageName,a.wmOutTypeEk as wmOutTypeEk,a.outDate as outDate ,a.billNo as billNo ,a.orgName as orgName,";
				hql += "b.itemCd as itemCd,b.itemName as itemName,b.spec as spec,";
				hql += "c.manuSpec as manuSpec,c.manuItemName as manuItemName,c.baseUnitName as baseUnitName,b.baseUnitQty as baseUnitQty";
				hql += ",b.consignee as consignee";
				hql += ")";
				hql += "  from WlWmStoreOut a,WlWmStoreOutDetl b,WlCmItem c  where a.storeOutId=b.storeOutId and b.itemId=c.itemId and b.baseUnitQty!=0 ";
			}

			// 单据编号

			if (!ValidateUtil.isEmpty(billNo)) {
				hql += " and (a.billNo like " + FormatUtil.formatStrForDB("%" + billNo + "%") + " or a.noticeNo like " + FormatUtil.formatStrForDB("%" + billNo + "%") + ")";
			}

			// 制单开始时间
			if (!ValidateUtil.isEmpty(beginDate)) {
				hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("a.outDate"));
				hql += " >= " + sqlUtil.string2ShortDate(FormatUtil.formatStrForDB(beginDate));
			}

			// 制单结束时间

			if (!ValidateUtil.isEmpty(endDate)) {
				hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("a.outDate"));
				hql += " <=" + sqlUtil.string2ShortDate(FormatUtil.formatStrForDB(endDate));
			}
			// 仓库
			if (!ValidateUtil.isEmpty(storageId)) {
				hql += " and a.storageId=" + FormatUtil.formatStrForDB(storageId);
			}
			// 经办人
			if (!ValidateUtil.isEmpty(createor)) {
				hql += " and a.createor like " + FormatUtil.formatStrForDB("%" + createor + "%");

			}
			// 业务类型
			if (!ValidateUtil.isEmpty(wmOutTypeEk)) {
				hql += " and a.wmOutTypeEk=" + FormatUtil.formatStrForDB(wmOutTypeEk);
			}
			// 往来单位
			if (!ValidateUtil.isEmpty(orgId)) {
				hql += " and a.orgId = " + FormatUtil.formatStrForDB(orgId);
			}
			// 物料编码
			if (!ValidateUtil.isEmpty(itemCd)) {
				hql += " and b.itemCd like " + FormatUtil.formatStrForDB("%" + itemCd + "%");
			}
			// 物料名称
			if (!ValidateUtil.isEmpty(itemName)) {
				hql += " and b.itemName like " + FormatUtil.formatStrForDB("%" + itemName + "%");
			}
			// 规格型号
			if (!ValidateUtil.isEmpty(spec)) {
				hql += " and b.spec like " + FormatUtil.formatStrForDB("%" + spec + "%");
			}
			// 单据状态
			if (!ValidateUtil.isEmpty(lockFlagEk)) {
				hql += " and a.lockFlagEk=" + FormatUtil.formatStrForDB(lockFlagEk);
			}
			// 收货人
			if (!ValidateUtil.isEmpty(consignee)) {
				hql += " and b.consignee like" + FormatUtil.formatStrForDB("%" + consignee + "%");
			}
			hql += "  order by a.createTime desc";
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询出库单分页数据
	 * @param flag 调用标志(出库单据管理为wlWmStoreOut，出库明细查为wlwmStoreOutQuery）
	 * @param paraMap 条件
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @return 出库单分页数据
	 */
	public Page search(String flag, Map paraMap, int pageSize, int currPage) {
		try {

			String hql = "";
			if ("wlWmStoreOut".equals(flag)) {
				hql = "  from WlWmStoreOut a where 1=1 ";
			}
			else if ("wlwmStoreOutQuery".equals(flag)) {
				hql = " select new map(";
				hql += "a.storageName as storageName,a.wmOutTypeEk as wmOutTypeEk,a.outDate as outDate ,a.billNo as billNo ,a.orgName as orgName,";
				hql += "b.itemCd as itemCd,b.itemName as itemName,b.spec as spec,";
				hql += "c.manuSpec as manuSpec,c.manuItemName as manuItemName,c.baseUnitName as baseUnitName,b.baseUnitQty as baseUnitQty";
				hql += ",b.consignee as consignee,a.storeOutId as storeOutId";
				hql += ")";
				hql += "  from WlWmStoreOut a,WlWmStoreOutDetl b,WlCmItem c  where a.storeOutId=b.storeOutId and b.itemId=c.itemId and b.baseUnitQty!=0 ";
			}
			// 单据编号
			if (paraMap.containsKey("billNo")) {
				String billNo = paraMap.get("billNo").toString();
				if (!ValidateUtil.isEmpty(billNo)) {
					hql += " and (a.billNo like " + FormatUtil.formatStrForDB("%" + billNo + "%") + " or a.noticeNo like " + FormatUtil.formatStrForDB("%" + billNo + "%") + ")";
				}
			}
			// 制单开始时间
			if (paraMap.containsKey("beginDate")) {
				String beginDate = paraMap.get("beginDate").toString();
				if (!ValidateUtil.isEmpty(beginDate)) {
					hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("a.outDate"));
					hql += " >= " + sqlUtil.string2ShortDate(FormatUtil.formatStrForDB(beginDate));
				}
			}
			// 制单结束时间
			if (paraMap.containsKey("endDate")) {
				String endDate = paraMap.get("endDate").toString();
				if (!ValidateUtil.isEmpty(endDate)) {
					hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("a.outDate"));
					hql += " <=" + sqlUtil.string2ShortDate(FormatUtil.formatStrForDB(endDate));
				}
			}
			// 仓库
			if (paraMap.containsKey("storageId")) {
				String storageId = paraMap.get("storageId").toString();
				if (!ValidateUtil.isEmpty(storageId)) {
					hql += " and a.storageId=" + FormatUtil.formatStrForDB(storageId);
				}
			}
			// 经办人
			if (paraMap.containsKey("createor")) {
				String createor = paraMap.get("createor").toString();
				if (!ValidateUtil.isEmpty(createor)) {
					hql += " and a.createor like " + FormatUtil.formatStrForDB("%" + createor + "%");
				}
			}
			// 业务类型
			if (paraMap.containsKey("wmOutTypeEk")) {
				String wmOutTypeEk = paraMap.get("wmOutTypeEk").toString();
				if (!ValidateUtil.isEmpty(wmOutTypeEk)) {
					hql += " and a.wmOutTypeEk=" + FormatUtil.formatStrForDB(wmOutTypeEk);
				}
			}
			// 往来单位
			if (paraMap.containsKey("orgName")) {
				String orgName = paraMap.get("orgName").toString();
				if (!ValidateUtil.isEmpty(orgName)) {
					hql += " and a.orgName like " + FormatUtil.formatStrForDB("%" + orgName + "%");
				}
			}
			// 物料编码
			if (paraMap.containsKey("itemCd")) {
				String itemCd = paraMap.get("itemCd").toString();
				if (!ValidateUtil.isEmpty(itemCd)) {
					hql += " and b.itemCd like " + FormatUtil.formatStrForDB("%" + itemCd + "%");
				}
			}
			// 物料编码（出库明细查询用）
			if (paraMap.containsKey("itemCdAsLike")) {
				String itemCd = paraMap.get("itemCdAsLike").toString();
				if (!ValidateUtil.isEmpty(itemCd)) {
					hql += " and b.itemCd like" + FormatUtil.formatStrForDB("%" + itemCd + "%");
				}
			}
			// 物料名称
			if (paraMap.containsKey("itemName")) {
				String itemName = paraMap.get("itemName").toString();
				if (!ValidateUtil.isEmpty(itemName)) {
					hql += " and b.itemName like " + FormatUtil.formatStrForDB("%" + itemName + "%");
				}
			}
			// 规格型号
			if (paraMap.containsKey("spec")) {
				String spec = paraMap.get("spec").toString();
				if (!ValidateUtil.isEmpty(spec)) {
					hql += " and b.spec like " + FormatUtil.formatStrForDB("%" + spec + "%");
				}
			}
			// 通知单号
			if (paraMap.containsKey("noticeNo")) {
				String noticeNo = paraMap.get("noticeNo").toString();
				if (!ValidateUtil.isEmpty(noticeNo)) {
					hql += " and a.noticeNo = " + FormatUtil.formatStrForDB(noticeNo);
				}
			}
			// 单据状态
			if (paraMap.containsKey("lockFlagEk")) {
				String lockFlagEk = paraMap.get("lockFlagEk").toString();
				if (!ValidateUtil.isEmpty(lockFlagEk)) {
					hql += " and a.lockFlagEk = " + FormatUtil.formatStrForDB(lockFlagEk);
				}
			}
			// 往来单位
			if (paraMap.containsKey("orgName")) {
				String orgName = paraMap.get("orgName").toString();
				if (!ValidateUtil.isEmpty(orgName)) {
					hql += " and a.orgName like " + FormatUtil.formatStrForDB("%" + orgName + "%");
				}
			}
			// 往来单位
			if (paraMap.containsKey("orgId")) {
				String orgId = paraMap.get("orgId").toString();
				if (!ValidateUtil.isEmpty(orgId)) {
					hql += " and a.orgId = " + FormatUtil.formatStrForDB(orgId);
				}
			}
			// 收货人
			if (paraMap.containsKey("consignee")) {
				String consignee = paraMap.get("consignee").toString();
				if (!ValidateUtil.isEmpty(consignee)) {
					hql += " and b.consignee like " + FormatUtil.formatStrForDB("%" + consignee + "%");
				}
			}

			hql += "  order by a.createTime desc";
			return this.findPage(hql, currPage, pageSize);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取当前最新的单据编号
	 * @return 最新单据编号
	 */
	public String getNewBillNo() {
		try {
			String billNo = "";
			String hql = "select max(billNo) from WlWmStoreOut";
			List list = this.find(hql);
			if (!ValidateUtil.isEmpty(list)) {
				billNo = (String) list.get(0);
			}
			return billNo;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取今日单据数
	 * @param today 今日日期
	 * @param storageId 仓库ID（为空或null则查询所有仓库）
	 * @return 单据数
	 */
	public long getBillCount(String today, String storageId) {
		try {
			String hql = "select count(*) from WlWmStoreOut where 1=1";
			if (!ValidateUtil.isEmpty(storageId)) {
				hql += " and storageId=" + FormatUtil.formatStrForDB(storageId);
			}
			hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("outDate"));
			hql += " =" + sqlUtil.string2ShortDate(FormatUtil.formatStrForDB(today));
			Long total = (Long) this.find(hql).get(0);
			return total;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取今日单据列表
	 * @param today 今日日期
	 * @return 单据列表
	 */
	public List<Map> getBill(String today, String filterWmOutTypeEk) {
		try {
			String hql = "select new map(";
			hql += "a.outDate as outDate,a.orgName as orgName,b.spec as spec,b.consignee as consignee,b.contactWay as contactWay";
			hql += ",b.addr as addr,c.serialNo as serialNo,b.itemName as itemName";
			hql += ") from  WlWmStoreOut a,WlWmStoreOutDetl b,WlWmStoreOutDetlInfo c  ";
			hql += " where a.storeOutId=b.storeOutId and b.storeOutDetlId=c.storeOutDetlId";
			/*
			 * if (!ValidateUtil.isEmpty(today)) {
			 * hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("a.outDate"));
			 * hql += " =" + sqlUtil.string2ShortDate(FormatUtil.formatStrForDB(today));
			 * }
			 */
			if (!ValidateUtil.isEmpty(filterWmOutTypeEk)) {
				hql += " and a.wmOutTypeEk!=" + FormatUtil.formatStrForDB(filterWmOutTypeEk);
			}
			if (!ValidateUtil.isEmpty(today)) {
				hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("b.modifyTime"));
				hql += " =" + sqlUtil.string2ShortDate(FormatUtil.formatStrForDB(today));
			}
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查找出库发货的相关信息
	 * @param storeOutIds 格式为('1','2','3')
	 * @return 出库发货的相关信息
	 */
	public List findStoreOutAndDetlList(String storeOutIds) {
		try {
			String hql = "select new map(a.consignee as consignee,a.contactWay as contactWay ,a.addr as addr,a.orgName as orgName,a.wmOutTypeEk as wmOutTypeEk,b.spec as spec) from WlWmStoreOut a,WlWmStoreOutDetl b where a.storeOutId=b.storeOutId";
			if (!ValidateUtil.isEmpty(storeOutIds)) {
				hql += " and a.storeOutId in" + storeOutIds;
			}
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/***
	 * 获取出库日期
	 * @param paraMap 条件
	 * @return 出库日期列表
	 */
	public List<Map> findOutDate(Map paraMap) {
		try {
			String hql = "select new map(a.outDate as outDate";
			hql += ") from WlWmStoreOut a ,WlWmStoreOutDetl b where a.storeOutId=b.storeOutId";
			// 日期
			if (paraMap.containsKey("date")) {
				String date = paraMap.get("date").toString();
				if (!ValidateUtil.isEmpty(date)) {
					hql += " and " + sqlUtil.string2Date(sqlUtil.date2String("a.outDate"), "yyyy-MM");
					hql += " =" + sqlUtil.string2Date(FormatUtil.formatStrForDB(date), "yyyy-MM");
				}
			}
			// 物料编码
			if (paraMap.containsKey("itemCd")) {
				String itemCd = paraMap.get("itemCd").toString();
				if (!ValidateUtil.isEmpty(itemCd)) {
					hql += " and b.itemCd=" + FormatUtil.formatStrForDB(itemCd);
				}
			}
			// 仓库
			if (paraMap.containsKey("storageId")) {
				String storageId = paraMap.get("storageId").toString();
				if (!ValidateUtil.isEmpty(storageId)) {
					hql += " and b.storageId=" + FormatUtil.formatStrForDB(storageId);
				}
			}
			hql += "  order by a.outDate";
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/***
	 * 获取出库总数量
	 * @param paraMap 条件
	 * @return 出库总数量
	 */
	public double getOutQty(Map paraMap) {
		try {
			double sumQty = 0;
			String hql = "select sum(b.baseUnitQty)";
			hql += "  from WlWmStoreOut a ,WlWmStoreOutDetl b where a.storeOutId=b.storeOutId";
			// 日期(年月)
			if (paraMap.containsKey("date")) {
				String date = paraMap.get("date").toString();
				if (!ValidateUtil.isEmpty(date)) {
					hql += " and " + sqlUtil.string2Date(sqlUtil.date2String("a.outDate"), "yyyy-MM");
					hql += " <" + sqlUtil.string2Date(FormatUtil.formatStrForDB(date), "yyyy-MM");
				}
			}
			// 日期(天)
			if (paraMap.containsKey("day")) {
				String day = paraMap.get("day").toString();
				if (!ValidateUtil.isEmpty(day)) {
					hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("a.outDate"));
					hql += " =" + sqlUtil.string2ShortDate(FormatUtil.formatStrForDB(day));
				}
			}

			// 物料编码
			if (paraMap.containsKey("itemCd")) {
				String itemCd = paraMap.get("itemCd").toString();
				if (!ValidateUtil.isEmpty(itemCd)) {
					hql += " and b.itemCd=" + FormatUtil.formatStrForDB(itemCd);
				}
			}
			// 物料编码(模糊查询)
			if (paraMap.containsKey("itemCdAsLike")) {
				String itemCd = paraMap.get("itemCdAsLike").toString();
				if (!ValidateUtil.isEmpty(itemCd)) {
					hql += " and b.itemCd like" + FormatUtil.formatStrForDB("%" + itemCd + "%");
				}
			}
			// 仓库
			if (paraMap.containsKey("storageId")) {
				String storageId = paraMap.get("storageId").toString();
				if (!ValidateUtil.isEmpty(storageId)) {
					hql += " and b.storageId=" + FormatUtil.formatStrForDB(storageId);
				}
			}
			// 业务类型
			if (paraMap.containsKey("wmOutTypeEk")) {
				String wmOutTypeEk = paraMap.get("wmOutTypeEk").toString();
				if (!ValidateUtil.isEmpty(wmOutTypeEk)) {
					hql += " and a.wmOutTypeEk=" + FormatUtil.formatStrForDB(wmOutTypeEk);
				}
			}
			// 开始日期
			if (paraMap.containsKey("beginDate")) {
				String beginDate = paraMap.get("beginDate").toString();
				if (!ValidateUtil.isEmpty(beginDate)) {
					hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("a.outDate"));
					hql += " >=" + sqlUtil.string2ShortDate(FormatUtil.formatStrForDB(beginDate));
					// sqlUtil.string2Date(arg0, arg1)
				}
			}
			// 结束日期
			if (paraMap.containsKey("endDate")) {
				String endDate = paraMap.get("endDate").toString();
				if (!ValidateUtil.isEmpty(endDate)) {
					hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("a.outDate"));
					hql += " <=" + sqlUtil.string2ShortDate(FormatUtil.formatStrForDB(endDate));
					// sqlUtil.string2Date(arg0, arg1)
				}
			}

			// 物料名称
			if (paraMap.containsKey("itemName")) {
				String itemName = paraMap.get("itemName").toString();
				if (!ValidateUtil.isEmpty(itemName)) {
					hql += " and b.itemName like " + FormatUtil.formatStrForDB("%" + itemName + "%");
				}
			}
			// 规格型号
			if (paraMap.containsKey("spec")) {
				String spec = paraMap.get("spec").toString();
				if (!ValidateUtil.isEmpty(spec)) {
					hql += " and b.spec like " + FormatUtil.formatStrForDB("%" + spec + "%");
				}
			}
			// 经销商id
			if (paraMap.containsKey("orgId")) {
				String orgId = paraMap.get("orgId").toString();
				if (!ValidateUtil.isEmpty(orgId)) {
					hql += " and a.orgId = " + FormatUtil.formatStrForDB(orgId);
				}
			}
			// 收货人
			if (paraMap.containsKey("consignee")) {
				String consignee = paraMap.get("consignee").toString();
				if (!ValidateUtil.isEmpty(consignee)) {
					hql += " and b.consignee like " + FormatUtil.formatStrForDB("%" + consignee + "%");
				}
			}
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

	/***
	 * 获取出库统计列表
	 * @param paraMap 条件
	 * @return 出库统计列表
	 */
	public List<Map> findOutDetailTotalList(Map paraMap) {
		try {
			String hql = "select new map(b.storageName as storageName,a.wmOutTypeEk as wmOutTypeEk";
			hql += ",b.itemCd as itemCd,b.itemName as itemName,b.spec as spec,sum(b.baseUnitQty) as outTotal";
			hql += ") from  WlWmStoreOut a ,WlWmStoreOutDetl b where a.storeOutId=b.storeOutId";
			// 入库类型
			if (paraMap.containsKey("wmOutTypeEk")) {
				String wmOutTypeEk = paraMap.get("wmOutTypeEk").toString();
				if (!ValidateUtil.isEmpty(wmOutTypeEk)) {
					hql += " and a.wmOutTypeEk=" + FormatUtil.formatStrForDB(wmOutTypeEk);
				}
			}
			// 开始日期
			if (paraMap.containsKey("beginDate")) {
				String beginDate = paraMap.get("beginDate").toString();
				if (!ValidateUtil.isEmpty(beginDate)) {
					hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("a.outDate"));
					hql += " >=" + sqlUtil.string2ShortDate(FormatUtil.formatStrForDB(beginDate));
					// sqlUtil.string2Date(arg0, arg1)
				}
			}
			// 结束日期
			if (paraMap.containsKey("endDate")) {
				String endDate = paraMap.get("endDate").toString();
				if (!ValidateUtil.isEmpty(endDate)) {
					hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("a.outDate"));
					hql += " <=" + sqlUtil.string2ShortDate(FormatUtil.formatStrForDB(endDate));
					// sqlUtil.string2Date(arg0, arg1)
				}
			}

			// 仓库
			if (paraMap.containsKey("storageId")) {
				String storageId = paraMap.get("storageId").toString();
				if (!ValidateUtil.isEmpty(storageId)) {
					hql += " and b.storageId=" + FormatUtil.formatStrForDB(storageId);
				}
			}
			// 物料编码
			if (paraMap.containsKey("itemCd")) {
				String itemCd = paraMap.get("itemCd").toString();
				if (!ValidateUtil.isEmpty(itemCd)) {
					hql += " and b.itemCd like " + FormatUtil.formatStrForDB("%" + itemCd + "%");
				}
			}
			// 物料编码(模糊查询)
			if (paraMap.containsKey("itemCdAsLike")) {
				String itemCd = paraMap.get("itemCdAsLike").toString();
				if (!ValidateUtil.isEmpty(itemCd)) {
					hql += " and b.itemCd like" + FormatUtil.formatStrForDB("%" + itemCd + "%");
				}
			}
			// 物料名称
			if (paraMap.containsKey("itemName")) {
				String itemName = paraMap.get("itemName").toString();
				if (!ValidateUtil.isEmpty(itemName)) {
					hql += " and b.itemName like " + FormatUtil.formatStrForDB("%" + itemName + "%");
				}
			}

			// 规格型号
			if (paraMap.containsKey("spec")) {
				String spec = paraMap.get("spec").toString();
				if (!ValidateUtil.isEmpty(spec)) {
					hql += " and b.spec like " + FormatUtil.formatStrForDB("%" + spec + "%");
				}
			}
			// 经销商
			if (paraMap.containsKey("orgName")) {
				String orgName = paraMap.get("orgName").toString();
				if (!ValidateUtil.isEmpty(orgName)) {
					hql += " and a.orgName like " + FormatUtil.formatStrForDB("%" + orgName + "%");
				}
			}
			// 经销商id
			if (paraMap.containsKey("orgId")) {
				String orgId = paraMap.get("orgId").toString();
				if (!ValidateUtil.isEmpty(orgId)) {
					hql += " and a.orgId = " + FormatUtil.formatStrForDB(orgId);
				}
			}
			// 收货人
			if (paraMap.containsKey("consignee")) {
				String consignee = paraMap.get("consignee").toString();
				if (!ValidateUtil.isEmpty(consignee)) {
					hql += " and b.consignee like " + FormatUtil.formatStrForDB("%" + consignee + "%");
				}
			}
			hql += " group by a.wmOutTypeEk,b.storageId,b.itemCd order by b.itemCd";
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/***
	 * 出库单信息
	 * @param map 条件
	 * @return 出库单信息
	 */
	public List<Map> findStoreOutDetlList(Map map) {
		try {
			String hql = "select new map(a.outDate as outDate,b.spec as spec,a.orgName as orgName";
			hql += ")from  WlWmStoreOut a ,WlWmStoreOutDetl b,WlWmStoreOutDetlInfo c where a.storeOutId=b.storeOutId";
			hql += " and b.storeOutDetlId=c.storeOutDetlId";
			if (map.containsKey("serialNo")) {
				String serialNo = (String) map.get("serialNo");
				if (!ValidateUtil.isEmpty(serialNo)) {
					hql += " and c.serialNo=" + FormatUtil.formatStrForDB(serialNo);
				}
			}
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}
