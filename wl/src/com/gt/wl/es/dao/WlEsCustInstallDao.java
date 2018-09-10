package com.gt.wl.es.dao;


import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.joyone.dao.BaseDao;
import org.joyone.dao.dialect.FormatUtil;
import org.joyone.lang.BaseException;
import org.joyone.sys.Page;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.es.model.WlEsCustInstall;

/**
 * 安装服务
 * @author Lizj
 *
 */
@Repository("wl.es.WlEsCustInstallDao")
public class WlEsCustInstallDao extends BaseDao {

	public WlEsCustInstallDao() {
		modelClass = WlEsCustInstall.class;
		defaultOrder = "";
	}
	
	/**
	 * 查询安装申请
	 * @param currPage 当前页
	 * @param pageSize 页数
	 * @param map 参数
	 * @return 分页对象
	 */
	public Page search(int currPage, int pageSize,Map map) {
		try {
			String hql = "from WlEsCustInstall where 1=1 ";
			// 字段名、字段值
			if (map.containsKey("keyId") && map.containsKey("keyValue")) {
				String keyId = (String) map.get("keyId");
				String keyValue = (String) map.get("keyValue");
				if (!ValidateUtil.isEmpty(keyId) && !ValidateUtil.isEmpty(keyValue)) {
					hql += " and " + keyId + " like " + FormatUtil.formatStrForDB("%" + keyValue + "%");
				}
			}
			// 收货人
			if (map.containsKey("receiver")) {
				String receiver = (String) map.get("receiver");
				if (!ValidateUtil.isEmpty(receiver)) {
					hql += " and receiver like" + FormatUtil.formatStrForDB("%" + receiver + "%");
				}
			}
			// 手机号码
			if (map.containsKey("mobile")) {
				String mobile = (String) map.get("mobile");
				if (!ValidateUtil.isEmpty(mobile)) {
					hql += " and mobile like" + FormatUtil.formatStrForDB("%" + mobile + "%");
				}
			}
			hql += "  order by createTime desc";
			return this.findPage(hql, currPage, pageSize);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
	
	/**
	 * 获取安装申请列表
	 * @param installState 安装状态
	 * @param filInstallState 用于过滤的安装状态
	 * @param installIds 选中的安装申请ids，格式为('','')
	 * @return 安装申请列表
	 */

	public List findCustInstallList(String installState, String filInstallState, String installIds) {
		try {
			String hql = "from WlEsCustInstall   where  1=1 ";
			if (!ValidateUtil.isEmpty(installState)) {
				hql += " and installState=" + FormatUtil.formatStrForDB(installState);
			}
			if (!ValidateUtil.isEmpty(filInstallState)) {
				hql += " and installState!=" + FormatUtil.formatStrForDB(filInstallState);
			}
			if (!ValidateUtil.isEmpty(installIds)) {
				hql += " and installId in" + installIds;
			}
			//hql += " order by sequ,issueTime desc ";
			return this.find(hql);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}
	
	/**
	 * 获取新的安装单号
	 * @return 新的安装单号
	 */
	public List getNewInstallNoCode() {
		try {
			String hql = "select max(installNo) from WlEsCustInstall";
			Session session = getSession();
			Query query = session.createQuery(hql);
			return query.list();
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}
