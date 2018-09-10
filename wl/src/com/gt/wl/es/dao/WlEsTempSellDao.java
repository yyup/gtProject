package com.gt.wl.es.dao;

import java.util.List;

import org.joyone.dao.BaseDao;
import org.joyone.dao.dialect.FormatUtil;
import org.joyone.lang.BaseException;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.es.model.WlEsTempSell;

/**
 * 适用上架商品DAO层
 * @author huangbj
 * 
 */
@Repository("wl.es.WlEsTempSellDao")
public class WlEsTempSellDao extends BaseDao {

	public WlEsTempSellDao() {
		modelClass = WlEsTempSell.class;
		defaultOrder = "";
	}

	/**
	 * 删除试用商品
	 * @param tempId 模板id
	 */
	public void removeData(String tempId) {
		try {
			String hql = "delete from WlEsTempSell s where 1=1";
			if (!ValidateUtil.isEmpty(tempId)) {
				hql += " and  s.tempId = " + FormatUtil.formatStrForDB(tempId);
			}
			this.executeHql(hql);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 返回模板下适用的商品
	 * @param tempId 运费模板id
	 * @param sellId 产品id
	 * @param sellIds 产品ids
	 * @return 模板下适用的商品
	 */
	public List findTempSellList(String tempId,String sellId,String sellIds) {
		try {
			String hql = "from WlEsTempSell s where 1=1";
			if (!ValidateUtil.isEmpty(tempId)) {
				hql += " and s.tempId=" + FormatUtil.formatStrForDB(tempId);
			}
			if (!ValidateUtil.isEmpty(sellId)) {
				hql += " and s.sellId=" + FormatUtil.formatStrForDB(sellId);
			}
			if (!ValidateUtil.isEmpty(sellIds)) {
				hql += " and s.sellId in " + sellIds;
			}
			return this.find(hql);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

}
