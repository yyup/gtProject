package com.gt.wl.cm.dao;

import java.util.List;
import java.util.Map;

import org.joyone.dao.BaseDao;
import org.joyone.dao.dialect.FormatUtil;
import org.joyone.lang.BaseException;
import org.joyone.sys.Page;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.cm.model.WlCmOrgUser;

/**
 * 往来单位用户DAO层
 * @author liuyj
 * 
 */
@Repository("wl.cm.WlCmOrgUserDao")
public class WlCmOrgUserDao extends BaseDao {

	public WlCmOrgUserDao() {
		modelClass = WlCmOrgUser.class;
		defaultOrder = "";
	}

	/**
	 * 通过经销商用户信息
	 * @param account 账号
	 * @return 经销商用户信息
	 */
	public WlCmOrgUser getWlCmOrgUser(String account) {
		try {
			String hql = "from WlCmOrgUser  where 1=1 ";
			if (!ValidateUtil.isEmpty(account)) {
				hql += " and account=" + FormatUtil.formatStrForDB(account);
			}
			List list = this.find(hql);
			if (!ValidateUtil.isEmpty(list)) {
				return (WlCmOrgUser) list.get(0);
			}
			else {
				return null;
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询经销商用户分页数据
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param paraMap 条件
	 * @return 分页结果
	 */
	public Page findDataList(int pageSize, int currPage, Map paraMap) {
		try {
			String hql = "from WlCmOrgUser  where 1=1 ";
			// 单据编号
			if (paraMap.containsKey("memberStateEk")) {
				String memberStateEk = paraMap.get("memberStateEk").toString();
				if (!ValidateUtil.isEmpty(memberStateEk)) {
					hql += " and memberStateEk=" + FormatUtil.formatStrForDB(memberStateEk);
				}
			}
			if (paraMap.containsKey("name")) {
				String name = paraMap.get("name").toString();
				if (!ValidateUtil.isEmpty(name)) {
					hql += " and name like " + FormatUtil.formatStrForDB("%" + name + "%");
				}
			}
			return this.findPage(hql, currPage, pageSize);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}
