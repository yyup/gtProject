package com.gt.wl.es.dao;

import java.util.List;
import java.util.Map;

import org.joyone.dao.BaseDao;
import org.joyone.dao.dialect.FormatUtil;
import org.joyone.lang.BaseException;
import org.joyone.sys.Page;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.es.model.WlEsFeeTemp;

/**
 * 运费模板DAO层
 * @author huangbj
 * 
 */
@Repository("wl.es.WlEsFeeTempDao")
public class WlEsFeeTempDao extends BaseDao {

	public WlEsFeeTempDao() {
		modelClass = WlEsFeeTemp.class;
		defaultOrder = "";
	}

	/**
	 * 运费模板详细信息
	 * @param sellIds 上架商品id 格式为('1','2')
	 * @param areaIds 达到地区id格式为('1','2')
	 * @param tempId 运费模板id(过滤自身)
	 * @return 运费模板详细信息
	 */
	public List findListBySellIdAndAreaId(String sellIds, String areaIds, String tempId) {
		try {
			String hql = "select new map(ft.tempName as tempName,ts.productName as productName,aa.name as name";
			hql += ",ts.sellId as sellId,ftr.firstFee as firstFee,ftr.firstInterval as firstInterval";
			hql += ",ftr.addFee as addFee,ftr.addInterval as addInterval,aa.areaId as areaId";
			hql += ") from WlEsFeeTemp ft,WlEsTempSell ts,WlEsFeeTempRow ftr,WlEsArriveArea aa ";
			hql += " where ft.tempId=ts.tempId and ft.tempId =ftr.tempId and ftr.rowId=aa.rowId";
			if (!ValidateUtil.isEmpty(sellIds)) {
				hql += " and ts.sellId in" + sellIds;
			}
			if (!ValidateUtil.isEmpty(areaIds)) {
				hql += " and aa.areaId in" + areaIds;
			}
			if (!ValidateUtil.isEmpty(tempId)) {
				hql += " and ft.tempId !=" + FormatUtil.formatStrForDB(tempId);
			}
			// 此首件运费和续件运费排序不可更改，涉及到前端计算运费，按首件最高商品计算首件，首件最高有，按续件最高的商品算
			hql += " order by ftr.firstFee desc,ftr.addFee desc";
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取模板分页数据
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param paraMap 条件
	 * @return 模板分页数据
	 **/
	public Page search(int pageSize, int currPage, Map paraMap) {
		try {
			String hql = "from WlEsFeeTemp where 1=1 ";
			hql += "  order by createTime desc";
			return this.findPage(hql, currPage, pageSize);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}
