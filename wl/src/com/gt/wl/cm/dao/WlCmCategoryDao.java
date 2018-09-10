package com.gt.wl.cm.dao;

import java.util.List;

import org.joyone.dao.BaseDao;
import org.joyone.dao.dialect.FormatUtil;
import org.joyone.lang.BaseException;
import org.joyone.util.ConvertUtil;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.cm.model.WlCmCategory;

@Repository("wl.cm.WlCmCategoryDao")
public class WlCmCategoryDao extends BaseDao {

	public WlCmCategoryDao() {
		modelClass = WlCmCategory.class;
		defaultOrder = "";
	}

	/**
	 * 查询所有数据状态为dataState的栏目对象
	 * @param isEnableFlag 栏目使用状态
	 * @param isLastFlag 是否末节点
	 * @param isShowAgency 是否显示给经销商
	 * @param parentCategoryId 父类别id
	 * @return 栏目对象列表
	 */
	public List<WlCmCategory> findAllColumnList(String isEnableFlag, String isLastFlag, String isShowAgency,String parentCategoryId) {
		try {

			String hql = "from WlCmCategory where 1=1  ";
			if (!ValidateUtil.isEmpty(isEnableFlag)) {
				hql += " and isEnableFlag ='" + isEnableFlag + "'";
			}
			if (!ValidateUtil.isEmpty(isLastFlag)) {
				hql += " and isLastFlag ='" + isLastFlag + "'";
			}
			if (!ValidateUtil.isEmpty(isShowAgency)) {
				hql += " and isShowAgency ='" + isShowAgency + "'";
			}
			if (!ValidateUtil.isEmpty(parentCategoryId)) {
				hql += " and parentCategoryId ="+FormatUtil.formatStrForDB(parentCategoryId);;
			}
			hql += " order by sequ";
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据父类型parentIds得到其直属子类型
	 * @param parentIds 父类型IDs
	 * @param isEnableFlag 是否启用
	 * @param isLastFlag 是否末节点
	 * @return 栏目列表
	 */
	public List<WlCmCategory> findChildList(String[] parentIds, String isEnableFlag, String isLastFlag) {
		try {
			String hql = "from WlCmCategory  where parentCategoryId in " + ConvertUtil.toDbString(parentIds);
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

}
