package com.gt.wl.cm.dao;

import java.util.List;
import java.util.Map;

import org.joyone.dao.BaseDao;
import org.joyone.dao.dialect.FormatUtil;
import org.joyone.lang.BaseException;
import org.joyone.sys.Page;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.cm.model.WlCmSellEn;

/**
 * 上架产品dao层
 * @author huangbj
 * 
 */
@Repository("wl.cm.WlCmSellEnDao")
public class WlCmSellEnDao extends BaseDao {

	public WlCmSellEnDao() {
		modelClass = WlCmSellEn.class;
		defaultOrder = "";
	}

	/**
	 * 返回上架商品列表
	 * @param pageSize 每页条数
	 * @param currPage 当前页码
	 * @param sellStateEk 状态
	 * @param typeId 商品类型id
	 * @param productName 商品名称
	 * @param isThumFlag 是否缩略图
	 * @param sellIds 上架商品sellIds 格式"('1','2','3')"
	 * @param filterSellIds 上架商品ids，过滤这些指定的商品 ，格式"('1','2','3')"
	 * @param auditNodeEk 审核环节代码
	 * @param auditState 审核标志
	 * @return 上架商品列表
	 */
	public Page findSellsPages(int pageSize, int currPage, String sellStateEk, String typeId, String productName, String isThumFlag, String sellIds, String filterSellIds, String auditNodeEk, String auditState) {
		try {

			String hql = "select new Map(s.sellId as sellId,s.productName as productName ,s.typeId as typeId,s.isHasGift as isHasGift, ";
			hql += "s.itemId as itemId,s.outStockNum as outStockNum,s.lackStockNum as lackStockNum,";
			if (!ValidateUtil.isEmpty(isThumFlag)) {
				hql += " w.path as path,s.sellStateEk as sellStateEk,s.price as price,s.sellTime as sellTime ,p.typeName as typeName ) ";
				hql += "from WlCmSellEn as s,WlCmProductTypeEn as p,WlCmSellPicEn as w where 1=1 and s.typeId=p.typeId  ";
				hql += " and s.sellId=w.sellId and w.isThumFlag='" + isThumFlag + "' ";
			}
			else {
				hql += " s.sellStateEk as sellStateEk,s.price as price,s.sellTime as sellTime ,p.typeName as typeName,s.itemCd as itemCd";
				hql += ",s.auditNodeEk as auditNodeEk,s.auditState as auditState) ";
				hql += "from WlCmSellEn as s,WlCmProductTypeEn as p where 1=1 and s.typeId=p.typeId  ";
			}
			if (!ValidateUtil.isEmpty(sellStateEk)) {
				hql += " and s.sellStateEk='" + sellStateEk + "' ";

			}
			if (!ValidateUtil.isEmpty(typeId)) {
				hql += " and s.typeId='" + typeId + "'";
			}
			if (!ValidateUtil.isEmpty(productName)) {
				hql += " and s.productName like " + FormatUtil.formatStrForDB("%" + productName + "%");

			}
			if (!ValidateUtil.isEmpty(sellIds)) {
				hql += " and s.sellId in" + sellIds;
			}
			if (!ValidateUtil.isEmpty(filterSellIds)) {
				hql += " and s.sellId not in" + filterSellIds;
			}
			if (!ValidateUtil.isEmpty(auditNodeEk)) {
				hql += " and s.auditNodeEk =" + FormatUtil.formatStrForDB(auditNodeEk);
			}
			if (!ValidateUtil.isEmpty(auditState)) {
				hql += " and s.auditState =" + FormatUtil.formatStrForDB(auditState);
			}
			return this.findPage(hql, currPage, pageSize);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取上架商品
	 * @param sellStateEk 状态
	 * @param typeId 商品类型id
	 * @param productName 商品名称
	 * @param isThumFlag 是否缩略图
	 * @param isIndexEk 首页是否显示
	 */
	public List findSellList(String sellStateEk, String typeId, String productName, String isThumFlag,String isIndexEk) {
		try {
			String hql = "select new Map(s.sellId as sellId,s.memo as memo,s.productName as productName ,s.typeId as typeId,s.isHasGift as isHasGift, ";
			hql += "s.itemId as itemId,s.outStockNum as outStockNum,s.lackStockNum as lackStockNum,p.typeName as typeName,p.memo as pmemo,";
			hql += " w.path as path,s.sellStateEk as sellStateEk,s.price as price,s.sellTime as sellTime ,p.typeName as typeName,s.tmallLink as tmallLink,s.jdLink as jdLink) ";
			hql += "from WlCmSellEn as s,WlCmProductTypeEn as p,WlCmSellPicEn as w where 1=1 and s.typeId=p.typeId  ";
			hql += " and s.sellId=w.sellId and w.isThumFlag='" + isThumFlag + "' ";
			if (!ValidateUtil.isEmpty(sellStateEk)) {
				hql += " and s.sellStateEk='" + sellStateEk + "' ";
			}
			if (!ValidateUtil.isEmpty(typeId)) {
				hql += " and s.typeId='" + typeId + "'";
			}
			if (!ValidateUtil.isEmpty(productName)) {
				hql += " and s.productName like " + FormatUtil.formatStrForDB("%" + productName + "%");
			}
			if (!ValidateUtil.isEmpty(isIndexEk)) {
				hql += " and s.isIndexEk='" + isIndexEk + "'";
			}
			hql += " order by s.sequ";
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
	
	/**
	 * 查询上架产品列表
	 * @param sellIds 上架产品Ids
	 * @return 上架产品列表
	 */
	public List findSellListByIds(String sellIds){
		try {
			String hql="select new Map(s.sellId as sellId,s.productName as productName,s.price as price,p.typeName as typeName,p.typeId as typeId) ";
			hql += " from WlCmSellEn as s,WlCmProductTypeEn as p where s.typeId=p.typeId ";
			if (!ValidateUtil.isEmpty(sellIds)) {
				hql += "and s.sellId in" + sellIds;
			}
			return this.find(hql);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 删除上架商品
	 * @param strId id数组转化成dbString
	 */
	public void removeData(String strId) {
		try {
			String hqlWhere = "  a.sellId in " + strId;
			String hql = "delete from WlCmSellEn  a where " + hqlWhere;
			this.executeHql(hql);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据上架商品id和状态查询商品条数
	 * @author huangbj
	 * @param sellIds 上架商品的id 格式为('1','2')
	 * @param sellState 上架状态
	 * @return 查询的条数
	 */
	public int getSellCountBySellIdsAndState(String sellIds, String sellState) {
		try {
			String hqlWhere = " sellId in " + sellIds;
			// String hql = " update TpEsSell set sellStateEk ='" + ek + "' where " + hqlWhere;
			String hql = "from WlCmSellEn where sellStateEk='" + sellState + "' and " + hqlWhere;
			return this.countDataTotal(hql, getSession());
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取上架商品
	 * @author huangbj
	 * @param sellIds 上架商品的id 格式为('1','2')
	 * @param sellState 上架状态
	 * @return 上架商品对象列表
	 */
	public List findTpEsSellBySellIdsAndState(String sellIds, String sellState) {
		try {

			String sql = "SELECT m.* from WL_CM_SELL m LEFT JOIN WL_CM_SELL_PIC s ON m.SELL_ID=s.SELL_ID";
			sql += " where s.PIC_ID is NULL and m.SELL_STATE_EK='" + sellState + "'";
			sql += " and m.SELL_ID in" + sellIds;
			return this.findByFreeSQL(sql, WlCmSellEn.class);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据上架状态获取上架商品
	 * @author dongyl
	 * @param sellState 上架状态
	 * @return 上架商品对象列表
	 */
	public List findEsSellByState(String sellState) {
		try {

			String sql = "SELECT m.* from WL_CM_SELL_EN m where 1=1 ";
			if(!ValidateUtil.isEmpty(sellState)){
				sql+="and m.SELL_STATE_EK='"+ sellState + "'";
			}			
			return this.findByFreeSQL(sql, WlCmSellEn.class);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 修改商品上架状态
	 * @param strHql 修改的id,例如（‘12’，‘23’）
	 * @param sellTime 上架时间
	 * @param ek 上架的ek字段
	 */
	public void updateState(String strHql, String ek, String sellTime) {
		try {
			String hql = " update WlCmSellEn set sellStateEk =" + FormatUtil.formatStrForDB(ek);
			if (!ValidateUtil.isEmpty(sellTime)) {
				hql += ", sellTime=" + FormatUtil.formatStrForDB(sellTime);
			}
			else if (sellTime == null) {
				hql += ", sellTime=null";
			}
			hql += " where sellId in" + strHql;
			this.executeHql(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/***
	 * 根据物料编码获取上架商品
	 * @param itemCd 物料编码
	 * @return 上架商品
	 */
	public WlCmSellEn getWlCmSellByItemCd(String itemCd) {
		try {
			String hql = "from WlCmSellEn where 1=1 ";
			if (!ValidateUtil.isEmpty(itemCd)) {
				hql += " and itemCd=" + FormatUtil.formatStrForDB(itemCd);
			}
			List list = this.find(hql);
			WlCmSellEn sell = null;
			if (!ValidateUtil.isEmpty(list)) {
				sell = (WlCmSellEn) list.get(0);
			}
			return sell;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 返回商品列表
	 * @param map 条件
	 * @return 商品列表
	 */
	public List<Map> findSellListByMap(Map map) {
		try {
			String hql = "select new map(s.sellId as sellId,s.productName as productName";
			hql += ",s.price as price,s.unitName as unitName";
			hql += ") from WlCmSellEn s where 1=1 ";
			// 商品上架状态
			if (map.containsKey("sellStateEk")) {
				String sellStateEk = (String) map.get("sellStateEk");
				if (!ValidateUtil.isEmpty(sellStateEk)) {
					hql += " and s.sellStateEk=" + FormatUtil.formatStrForDB(sellStateEk);
				}
			}
			// 上架商品ids，格式为('1','2','3')
			if (map.containsKey("sellIds")) {
				String sellIds = (String) map.get("sellIds");
				if (!ValidateUtil.isEmpty(sellIds)) {
					hql += " and s.sellId in" + sellIds;
				}
			}
			// 审核环节
			if (map.containsKey("auditNodeEk")) {
				String auditNodeEk = (String) map.get("auditNodeEk");
				if (!ValidateUtil.isEmpty(auditNodeEk)) {
					hql += " and s.auditNodeEk=" + FormatUtil.formatStrForDB(auditNodeEk);
				}
			}
			// 审核状态
			if (map.containsKey("auditState")) {
				String auditState = (String) map.get("auditState");
				if (!ValidateUtil.isEmpty(auditState)) {
					hql += " and s.auditState=" + FormatUtil.formatStrForDB(auditState);
				}
			}
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}
