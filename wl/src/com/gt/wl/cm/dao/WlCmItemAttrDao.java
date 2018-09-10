package com.gt.wl.cm.dao;

import java.util.List;

import org.joyone.dao.BaseDao;
import org.joyone.dao.dialect.FormatUtil;
import org.joyone.lang.BaseException;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.cm.model.WlCmItemAttr;

/**
 * 物料属性
 * @author Lizj
 *
 */
@Repository("wl.cm.WlCmItemAttrDao")
public class WlCmItemAttrDao extends BaseDao {

	public WlCmItemAttrDao() {
		modelClass = WlCmItemAttr.class;
		defaultOrder = "";
	}
	/**
	 * 查询物料自定义属性表
	 * @param itemId 物料ID
	 * @return 物料自定义属性表
	 */
	public List<WlCmItemAttr> findItemAttrList(String itemId, String languageType) {
		try {
			String hql = "from WlCmItemAttr where 1=1";
			//if (!ValidateUtil.isEmpty(itemId)) {
				hql += " and itemId =" + FormatUtil.formatStrForDB(itemId);
			//}
				
			if (!ValidateUtil.isEmpty(languageType)) {
				if (languageType.equals("EN")) {
					hql += " AND languageTypeEk =" + FormatUtil.formatStrForDB(languageType);
				} else {
					hql += " AND (languageTypeEk IS NULL OR languageTypeEk='CN') ";
				}
				
			}
			hql += " order by sequ";
			return this.find(hql);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

}
