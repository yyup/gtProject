package com.gt.wl.es.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.joyone.dao.BaseDao;
import org.joyone.dao.dialect.FormatUtil;
import org.joyone.lang.BaseException;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.es.model.WlEsFeeTempRow;

/**
 * 模板行DAO层
 * @author huangbj
 * 
 */
@Repository("wl.es.WlEsFeeTempRowDao")
public class WlEsFeeTempRowDao extends BaseDao {

	public WlEsFeeTempRowDao() {
		modelClass = WlEsFeeTempRow.class;
		defaultOrder = "";
	}

	/**
	 * 删除模板下运费模板行
	 * @param tempId 模板id
	 */
	public void removeData(String tempId) {
		try {
			String hql = "delete from WlEsFeeTempRow s where 1=1";
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
	 * 返回模板下运费模板行
	 * @param tempId 运费模板id
	 * @return 模板下运费模板行
	 */
	public List findFeeTempRowList(String tempId) {
		try {
			String hql = "from WlEsFeeTempRow s where 1=1";
			if (!ValidateUtil.isEmpty(tempId)) {
				hql += " and s.tempId=" + FormatUtil.formatStrForDB(tempId);
			}	
			return this.find(hql);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}
	
	/**
	 * 获取运费
	 * @param sellId 产品id
	 * @param sellIds 产品ids
	 * @param areaId 区域id
	 * @return 运费
	 */
	public double getFirstFee(String sellId,String sellIds,String areaId) {
		try {
			String sql = "SELECT MAX(r.FIRST_FEE) FROM `wl_es_fee_temp_row` r WHERE 1=1 ";
			if (!ValidateUtil.isEmpty(areaId)) {
				sql+=" AND r.`ROW_ID` IN (SELECT a.`ROW_ID` FROM `wl_es_arrive_area` a WHERE a.`AREA_ID`="+FormatUtil.formatStrForDB(areaId)+")";
			}
			if (!ValidateUtil.isEmpty(sellId)) {
				sql+=" AND r.`TEMP_ID` IN (SELECT s.`TEMP_ID` FROM `wl_es_temp_sell` s WHERE s.`SELL_ID` = "+FormatUtil.formatStrForDB(sellId)+")";
			}
			if (!ValidateUtil.isEmpty(sellIds)) {
				sql+=" AND r.`TEMP_ID` IN (SELECT s.`TEMP_ID` FROM `wl_es_temp_sell` s WHERE s.`SELL_ID` in "+sellIds+")";
			}
			List<BigDecimal> list=this.findByFreeSQL(sql);
			double firstFee=0;
			if(!ValidateUtil.isEmpty(list)){
				if(!ValidateUtil.isNull(list.get(0))){
					firstFee= list.get(0).doubleValue();
				}
			}
			return firstFee;
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}
	
	/**
	 * 获取运费
	 * @param sellId 产品id
	 * @param sellIds 产品ids
	 * @param areaId 区域id
	 * @return 运费
	 */
	public double getAddFee(String sellId,String sellIds,String areaId) {
		try {
			String sql = "SELECT MAX(r.ADD_FEE) FROM `wl_es_fee_temp_row` r WHERE 1=1 ";
			if (!ValidateUtil.isEmpty(areaId)) {
				sql+=" AND r.`ROW_ID` IN (SELECT a.`ROW_ID` FROM `wl_es_arrive_area` a WHERE a.`AREA_ID`="+FormatUtil.formatStrForDB(areaId)+")";
			}
			if (!ValidateUtil.isEmpty(sellId)) {
				sql+=" AND r.`TEMP_ID` IN (SELECT s.`TEMP_ID` FROM `wl_es_temp_sell` s WHERE s.`SELL_ID` = "+FormatUtil.formatStrForDB(sellId)+")";
			}
			if (!ValidateUtil.isEmpty(sellIds)) {
				sql+=" AND r.`TEMP_ID` IN (SELECT s.`TEMP_ID` FROM `wl_es_temp_sell` s WHERE s.`SELL_ID` in "+sellIds+")";
			}
			List<BigDecimal> list=this.findByFreeSQL(sql);
			double addFee=0;
			if(!ValidateUtil.isEmpty(list)){
				if(!ValidateUtil.isNull(list.get(0))){
					addFee= list.get(0).doubleValue();
				}				
			}
			return addFee;
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}
	
	
}
