package com.gt.wl.cm.dao;

import java.util.List;

import org.joyone.dao.BaseDao;
import org.joyone.dao.dialect.FormatUtil;
import org.joyone.lang.BaseException;
import org.joyone.util.ConvertUtil;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.cm.model.WlCmColumnEn;

/**
 * 栏目管理(英文)DAO层
 * @author Lizj
 * 
 */

@Repository("wl.cm.WlCmColumnEnDao")
public class WlCmColumnEnDao extends BaseDao {

	public WlCmColumnEnDao() {
		modelClass = WlCmColumnEn.class;
		defaultOrder = "";
	}

	/**
	 * 查询所有数据状态为dataState的栏目对象
	 * @param isEnableFlag 栏目使用状态
	 * @param isLastFlag 是否末节点
	 * @param root 跟节点
	 * @return 栏目对象列表
	 */
	public List<WlCmColumnEn> findAllColumnList(String isEnableFlag, String isLastFlag,String root) {
		try {

			String hql = "from WlCmColumnEn where dataState='1'  ";
			if (!ValidateUtil.isEmpty(isEnableFlag)) {
				hql += " and isEnableFlag ='" + isEnableFlag + "'";
			}
			if (!ValidateUtil.isEmpty(isLastFlag)) {
				hql += " and isLastFlag ='" + isLastFlag + "'";
			}
			if (!ValidateUtil.isEmpty(root)) {
				hql += " and parentColumnId ='" + root + "'";
			}
			hql += " order by isEnableFlag desc,sequ";
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据父类型parentIds得到其直属子类型
	 * @param parentIds 父类型IDs,格式为('1','2','3')
	 * @param dataState 数据状态
	 * @param isEnableFlag 是否启用
	 * @return 栏目列表
	 */
	public List<WlCmColumnEn> findChildColumnList(String parentIds, String dataState, String isEnableFlag) {
		try {
			String hql = "from WlCmColumnEn  where 1=1";
			if (!ValidateUtil.isEmpty(dataState)) {
				hql += " and dataState=" + FormatUtil.formatStrForDB(dataState);
			}
			if (!ValidateUtil.isEmpty(isEnableFlag)) {
				hql += " and isEnableFlag=" + FormatUtil.formatStrForDB(isEnableFlag);

			}
			if (!ValidateUtil.isEmpty(parentIds)) {
				hql += " and  parentColumnId in " + parentIds;
			}
			hql += "  order by sequ ";
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 根据父类型parentIds得到其直属子类型
	 * @param parentIds 父类型IDs
	 * @param dataState 数据状态
	 * @param isEnableFlag 是否启用
	 * @param isLastFlag 是否末节点
	 * @return 栏目列表
	 */
	public List<WlCmColumnEn> findChildList(String[] parentIds, String dataState, String isEnableFlag, String isLastFlag) {
		try {
			String hql = "from WlCmColumnEn  where dataState='" + dataState + "' and  parentColumnId in " + ConvertUtil.toDbString(parentIds);
			if (!ValidateUtil.isEmpty(isEnableFlag)) {
				hql = hql + " and isEnableFlag='" + isEnableFlag + "'";
			}
			if (!ValidateUtil.isEmpty(isLastFlag)) {
				hql = hql + "and isLastFlag='" + isLastFlag + "'";
			}
			hql = hql + "order by sequ";
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 删除栏目
	 * @param ids 类型ID数组
	 * @param dataState 数据状态
	 */
	public void deleteColumns(String[] ids, String dataState) {
		String hql = "update WlCmColumnEn set dataState ='" + dataState + "' where columnId in " + ConvertUtil.toDbString(ids);
		try {
			this.executeHql(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}
