package com.gt.wl.cm.dao;

import java.util.List;

import org.joyone.dao.BaseDao;
import org.joyone.dao.dialect.FormatUtil;
import org.joyone.lang.BaseException;
import org.joyone.sys.Page;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.cm.model.WlCmMember;

/**
 * 注册会员DAO层
 * @author liuyj
 * 
 */
@Repository("wl.cm.WlCmMemberDao")
public class WlCmMemberDao extends BaseDao {

	public WlCmMemberDao() {
		modelClass = WlCmMember.class;
		defaultOrder = "";
	}

	/**
	 * 查询注册会员分页数据
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param levelId 等级ID
	 * @param name 用户名
	 * @param memberStateEk 会员状态
	 * @param isBuyFlag 是否有购买商品
	 * @param email 邮箱
	 * @param account 用户账号
	 * @return 分页结果
	 */
	public Page search(int pageSize, int currPage, String levelId, String name, String memberStateEk, String isBuyFlag, String email, String account) {
		try {
			String hql = "from WlCmMember  where 1=1  ";

			if (!ValidateUtil.isEmpty(memberStateEk)) {
				hql += " and memberStateEk = " + FormatUtil.formatStrForDB(memberStateEk);
			}

			if (!ValidateUtil.isEmpty(name)) {
				hql += " and name like " + "'%" + name + "%'";
			}

			if (!ValidateUtil.isEmpty(levelId)) {
				hql += " and levelId = " + FormatUtil.formatStrForDB(levelId);
			}
			if (!ValidateUtil.isEmpty(isBuyFlag)) {
				hql += " and isBuyFlag = " + FormatUtil.formatStrForDB(isBuyFlag);
			}
			if (!ValidateUtil.isEmpty(email)) {
				hql += " and email like " + "'%" + email + "%'";
			}
			if (!ValidateUtil.isEmpty(account)) {
				hql += " and account like " + "'%" + account + "%'";
			}
			hql += " order by regTime desc";

			return this.findPage(hql, currPage, pageSize);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 通过账号查询会员信息
	 * @param account 账号
	 * @param email 会员邮箱
	 * @param name 姓名
	 * @param qqAccount QQ账号
	 * @return 会员信息
	 */
	public WlCmMember getMemberByAccountOrEmail(String account, String email,String name,String qqAccount) {
		try {
			String hql = "from WlCmMember m where 1=1";
			if (!ValidateUtil.isEmpty(account)) {
				hql += " and m.account=" + FormatUtil.formatStrForDB(account);
			}
			if (!ValidateUtil.isEmpty(email)) {
				hql += " and m.email=" + FormatUtil.formatStrForDB(email);
			}
			if (!ValidateUtil.isEmpty(name)) {
				hql += " and m.name=" + FormatUtil.formatStrForDB(name);
			}
			if (!ValidateUtil.isEmpty(qqAccount)) {
				hql += " and m.qqAccount=" + FormatUtil.formatStrForDB(qqAccount);
			}
			List list = this.find(hql);
			if (!ValidateUtil.isEmpty(list)) {
				return (WlCmMember) list.get(0);
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
	 * 根据性别查询会员
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param sexEk 性别
	 * @return 结果
	 */
	public Page searchBySex(int pageSize, int currPage, String sexEk) {
		try {
			String hql = "select memberId from WlCmMember  where 1=1  ";
			if (!ValidateUtil.isEmpty(sexEk)) {
				hql += " and sexEk = " + FormatUtil.formatStrForDB(sexEk);
			}

			return this.findPage(hql, currPage, pageSize);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}

}
