package com.gt.wl.cm.dao;

import java.util.List;

import org.joyone.dao.BaseDao;
import org.joyone.dao.dialect.FormatUtil;
import org.joyone.lang.BaseException;
import org.joyone.util.ConvertUtil;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.cm.model.WlCmProductTypeEn;

@Repository("wl.cm.WlCmProductTypeEnDao")
public class WlCmProductTypeEnDao extends BaseDao {

	public WlCmProductTypeEnDao() {
		modelClass = WlCmProductTypeEn.class;
		defaultOrder = "";
	}

	/**
	 * 查询所有数据状态为“1”的商品类型对象
	 * @return 商品类型对象列表
	 */
	public List<WlCmProductTypeEn> findAllTypeList() {
		try {
			String hql = "from WlCmProductTypeEn  where dataState ='1' ";
			hql += "order by sequ";
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据父类型ID得到其直属子类型
	 * @param parentTypeId 父类型ID
	 * @return 类型列表
	 */
	public List<WlCmProductTypeEn> findChildTypeList(String parentTypeId) {
		try {
			String hql = "from WlCmProductTypeEn  where dataState ='1' and parentTypeId =? ";
			return this.find(hql, parentTypeId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 获取所有末级节点的产品类型
	 * @param dataState 数据状态
	 * @return 产品类型对象列表
	 */
	public List<WlCmProductTypeEn> findAllLastType(String dataState) {
		
		try {
			String hql = " from WlCmProductTypeEn m where m.typeId not in (select s.parentTypeId from WlCmProductTypeEn s)";
			if (!ValidateUtil.isEmpty(dataState)) {
				hql += " and dataState=" + FormatUtil.formatStrForDB(dataState);
			}
			hql += " order by sequ";
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 判断某类型是否有子节点
	 * @param typeId 类型ID
	 * @return true 有 false 无
	 */
	public boolean validateHasChild(String typeId) {
		String hql = "from WlCmProductTypeEn where dataState ='1'and parentTypeId =?";
		try {
			List list = this.find(hql, typeId);
			if (ValidateUtil.isEmpty(list)) {
				return false;
			}
			return true;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 删除商品类型
	 * @param ids 类型ID数组
	 */
	public void deleteProductType(String[] ids) {
		String hql = "update WlCmProductTypeEn set dataState ='0' where typeId in " + ConvertUtil.toDbString(ids);
		try {
			this.executeHql(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}
