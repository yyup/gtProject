package com.gt.wl.es.dao;

import java.util.List;

import org.joyone.dao.BaseDao;
import org.joyone.lang.BaseException;
import org.joyone.sys.Page;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.es.model.WlEsMyShoppingCart;

/**
 * 我的购物车DAO层
 * @author liuyj
 * 
 */
@Repository("wl.es.WlEsMyShoppingCartDao")
public class WlEsMyShoppingCartDao extends BaseDao {

	public WlEsMyShoppingCartDao() {
		modelClass = WlEsMyShoppingCart.class;
		defaultOrder = "";
	}

	/**
	 * 查询我的购物车列表
	 * @param memberId 会员Id
	 * @return 我的购物车列表
	 */
	public List findMyShoppingCartList(String memberId) {
		try {
			String hql = "select new map(m.shoppingCartId as shoppingCartId,m.sellId as sellId,s.path as path,m.productName as productName,m.num as num,m.price as price) from WlEsMyShoppingCart m,WlCmSellPic s "
					+ "where m.memberId=? and m.sellId=s.sellId and s.isThumFlag='1' ";
			return this.find(hql, memberId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询我的购物车的数量
	 * @param memberId 会员Id
	 * @return 购物车的数量
	 */
	public List findMyShoppingCartTotalNum(String memberId) {
		try {
			String hql = "select t.num from WlEsMyShoppingCart where memberId=?";
			return this.find(hql, memberId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询我的购物车列表已存在商品
	 * @param sellId 商品Id
	 * @param memberId 会员Id
	 * @return 已存在商品购物车
	 */
	public WlEsMyShoppingCart getMyShoppingCart(String sellId, String memberId) {
		try {
			String hql = "from WlEsMyShoppingCart t where 1=1";
			if (!ValidateUtil.isEmpty(sellId)) {
				hql += " and t.sellId='" + sellId + "'";
			}
			if (!ValidateUtil.isEmpty(memberId)) {
				hql += " and t.memberId='" + memberId + "'";
			}
			List list = this.find(hql);
			if (!ValidateUtil.isEmpty(list)) {
				return (WlEsMyShoppingCart) list.get(0);
			}
			return null;
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过 购车id数组转化db字符串搜索对应购物车列表
	 * @param strCartIds 购车id数组转化db字符串
	 * @return 购物车列表
	 */
	public List findShoppingCartListByCartIds(String strCartIds) {
		try {
			String hqlWhere = " where t.sellId =s.sellId and t.shoppingCartId in " + strCartIds;
			String hqlOrder = " order by s.corpId ";
			String hql = "select new WlEsMyShoppingCart(t.name as name,t.price as price,t.num as num,t.unitName as unitName,t.shoppingCartId as shoppingCartId,s.sellId as sellId)";
			hql += "from WlEsMyShoppingCart t,WlCmSell as s " + hqlWhere + hqlOrder;
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询我的购物车列表
	 * @param pageSize 每页条数
	 * @param currPage 当前页码
	 * @param memberId 会员Id
	 * @param isThumFlag 是否缩略图
	 * @param sellIds 上架商品sellIds 格式"('1','2','3')"
	 * @return 我的购物车列表
	 */
	public Page findMyShoppingCartPage(int pageSize, int currPage, String memberId, String isThumFlag, String sellIds) {
		try {
			String hql = "select new map(m.shoppingCartId as shoppingCartId,m.sellId as sellId,sp.path as path,m.num as num";
			hql += ",s.itemId as itemId,s.outStockNum as outStockNum,s.lackStockNum as lackStockNum";
			hql += ",s.productName as productName,s.price as price,s.price*m.num as amt,m.sellStateEk as sellStateEk) from WlEsMyShoppingCart m,WlCmSellPic sp ,WlCmSell s ";
			hql += " where m.sellId=s.sellId and s.sellId=sp.sellId";
			if (!ValidateUtil.isEmpty(memberId)) {
				hql += " and m.memberId='" + memberId + "' ";
			}
			if (!ValidateUtil.isEmpty(isThumFlag)) {
				hql += " and sp.isThumFlag='" + isThumFlag + "' ";
			}
			if (!ValidateUtil.isEmpty(sellIds)) {
				hql += " and s.sellId in" + sellIds;
			}
			return this.findPage(hql, currPage, pageSize);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
	
	/**
	 * 查询我的购物车列表
	 * @param pageSize 每页条数
	 * @param currPage 当前页码
	 * @param memberId 会员Id
	 * @param isThumFlag 是否缩略图
	 * @param sellIds 上架商品sellIds 格式"('1','2','3')"
	 * @return 我的购物车列表
	 */
	public Page findMyShoppingCartEnPage(int pageSize, int currPage, String memberId, String isThumFlag, String sellIds) {
		try {
			String hql = "select new map(m.shoppingCartId as shoppingCartId,m.sellId as sellId,sp.path as path,m.num as num";
			hql += ",s.itemId as itemId,s.outStockNum as outStockNum,s.lackStockNum as lackStockNum";
			hql += ",s.productName as productName,s.price as price,s.price*m.num as amt,m.sellStateEk as sellStateEk) from WlEsMyShoppingCart m,WlCmSellPicEn sp ,WlCmSellEn s ";
			hql += " where m.sellId=s.sellId and s.sellId=sp.sellId";
			if (!ValidateUtil.isEmpty(memberId)) {
				hql += " and m.memberId='" + memberId + "' ";
			}
			if (!ValidateUtil.isEmpty(isThumFlag)) {
				hql += " and sp.isThumFlag='" + isThumFlag + "' ";
			}
			if (!ValidateUtil.isEmpty(sellIds)) {
				hql += " and s.sellId in" + sellIds;
			}
			return this.findPage(hql, currPage, pageSize);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 删除购物车
	 * @param sellIds 格式('1','2')
	 */
	public void deleteMyShoppingCart(String sellIds) {
		try {
			String hql = "delete from WlEsMyShoppingCart where 1=1 ";
			if (!ValidateUtil.isEmpty(sellIds)) {
				hql += " and sellId in " + sellIds;
			}
			this.executeHql(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 修改购物车里商品上架状态
	 * @param sellIds 修改的id,例如（‘12’，‘23’）
	 * @param sellStateEk 上架的ek字段
	 */
	public void updateSellStateEk(String sellIds, String sellStateEk) {
		try {
			String hqlWhere = " sellId in " + sellIds;
			String hql = " update WlEsMyShoppingCart set sellStateEk ='" + sellStateEk + "' where " + hqlWhere;
			this.executeHql(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}
