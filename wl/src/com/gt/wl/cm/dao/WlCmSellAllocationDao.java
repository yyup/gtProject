package com.gt.wl.cm.dao;

import java.util.List;

import org.joyone.dao.BaseDao;
import org.joyone.dao.dialect.FormatUtil;
import org.joyone.lang.BaseException;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.cm.model.WlCmItemAttr;
import com.gt.wl.cm.model.WlCmSellAllocation;
import com.gt.wl.cm.model.WlCmUnit;

/**
 * 产品配置
 * @author Lizj
 *
 */
@Repository("wl.cm.WlCmSellAllocationDao")
public class WlCmSellAllocationDao extends BaseDao {

	public WlCmSellAllocationDao() {
		modelClass = WlCmSellAllocation.class;
		defaultOrder = "";
	}
	/**
	 * 查询配置列表
	 * @param parentSellId 关联上架产品id
	 * @return 查询配置列表
	 */
	public List findAllocationList(String parentSellId){
		try {
			String hql="from WlCmSellAllocation where 1=1 ";
			if (!ValidateUtil.isEmpty(parentSellId)) {
				hql += "and parentSellId =" + FormatUtil.formatStrForDB(parentSellId);
			}
			return this.find(hql);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}
	/**
	 * 查询配置列表(产品图片)
	 * @param sellId
	 * @return
	 */
	public List findSellAllocationList(String sellId) {
		try {
			String hql= " select new Map(sl.allocationId as allocationId, sl.sellId as sellId, sl.parentSellId as parentSellId, s.productName as productName, s.price as price, sp.path as picPath) ";
			hql += " from WlCmSellAllocation sl ,WlCmSellPic sp ,WlCmSell s";
			hql += " where sl.sellId = sp.sellId and sl.sellId =s.sellId  and sp.isThumFlag='1' ";
			if (!ValidateUtil.isEmpty(sellId)){
				hql += " and sl.parentSellId = " +FormatUtil.formatStrForDB(sellId) ;
			}
			List list = this.find(hql);
			return list;
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}
	
	/**
	 * 删除产品配置
	 * @param sellId 产品id
	 * @param parentSellId 父产品id
	 */
	public void removeData(String sellId,String parentSellId) {
		try {
			if (!ValidateUtil.isEmpty(sellId)||!ValidateUtil.isEmpty(parentSellId)) {
				String hqlWhere = " ( a.sellId in " + sellId + "or a.parentSellId in "+ parentSellId+" )";
				String hql = "delete from WlCmSellAllocation  a where " + hqlWhere;
				this.executeHql(hql);
			}
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}
}
