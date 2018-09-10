package com.gt.wl.cm.dao;

import java.util.List;

import org.joyone.dao.BaseDao;
import org.joyone.dao.dialect.FormatUtil;
import org.joyone.lang.BaseException;
import org.joyone.sys.Page;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.cm.model.WlCmOrg;

@Repository("wl.cm.WlCmOrgDao")
public class WlCmOrgDao extends BaseDao {

	public WlCmOrgDao() {
		modelClass = WlCmOrg.class;
		defaultOrder = "";
	}

	/**
	 * 查询往来单位，获取翻页数据
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param orgTypeEk 单位类型
	 * @param content 要查询的内容编号或者名称
	 * @return 结果
	 */
	public Page search(int pageSize, int currPage, String orgTypeEk, String content) {
		try {
			String hql = "from WlCmOrg w where 1=1 ";
			if (!ValidateUtil.isEmpty(orgTypeEk)) {
				hql += " and w.orgTypeEk like " + FormatUtil.formatStrForDB("%" + orgTypeEk + "%");
			}
			if (!ValidateUtil.isEmpty(content)) {
				hql += " and (";
				hql += " w.orgCd like '%" + content + "%' or w.orgName like '%" + content + "%' ";
				hql = hql + ") ";
			}
			hql += " order by w.createTime desc";
			return this.findPage(hql, currPage, pageSize);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取往来单位列表
	 * @param orgCd 单位编码
	 * @param orgId 单位id
	 * @param isEnableFlag 是否启用
	 * @param orgTypeEk 往来单位类型
	 * @return 往来单位列表
	 */
	public List findWlCmOrgList(String orgCd, String orgId, String isEnableFlag, String orgTypeEk) {
		try {
			String hql = "from WlCmOrg  where 1=1  ";
			if (!ValidateUtil.isEmpty(orgCd)) {
				hql += "  and orgCd=" + FormatUtil.formatStrForDB(orgCd);
			}
			if (!ValidateUtil.isEmpty(orgId)) {
				hql += "  and orgId!=" + FormatUtil.formatStrForDB(orgId);
			}
			if (!ValidateUtil.isEmpty(isEnableFlag)) {
				hql += "  and isEnableFlag =" + FormatUtil.formatStrForDB(isEnableFlag);
			}
			if (!ValidateUtil.isEmpty(orgTypeEk)) {
				hql += " and orgTypeEk like " + FormatUtil.formatStrForDB("%" + orgTypeEk + "%");

			}
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}
