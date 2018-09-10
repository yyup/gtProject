package com.gt.wl.cm.dao;

import java.util.Map;

import org.joyone.dao.BaseDao;
import org.joyone.dao.dialect.FormatUtil;
import org.joyone.lang.BaseException;
import org.joyone.sys.Page;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.cm.model.WlCmKeywords;
import com.gt.wl.cm.model.WlCmUnit;

/**
 * 关键词Dao
 * @author Lizj
 *
 */
@Repository("wl.cm.WlCmKeywordsDao")
public class WlCmKeywordsDao extends BaseDao {

	public WlCmKeywordsDao() {
		modelClass = WlCmKeywords.class;
		defaultOrder = "";
	}
	

	/**
	 * 获取数据列表
	 * @param pageSize 页记录数
	 * @param currPage 页码
	 * @return 数据列表
	 */
	public Page search(int pageSize, int currPage) {

		try {
			String hql = "from WlCmKeywords f where 1=1 ";
			hql += " order by f.createTime desc";
			return this.findPage(hql, currPage, pageSize);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}
