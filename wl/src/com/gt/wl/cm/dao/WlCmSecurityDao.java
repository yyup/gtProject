package com.gt.wl.cm.dao;

import java.util.List;

import org.joyone.dao.BaseDao;
import org.joyone.lang.BaseException;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.cm.model.WlCmSecurity;

/**
 * 权限DAO层
 * @author liuyj
 * 
 */
@Repository("wl.cm.WlCmSecurityDao")
public class WlCmSecurityDao extends BaseDao {

	public WlCmSecurityDao() {
		modelClass = WlCmSecurity.class;
		defaultOrder = "";
	}

	/**
	 * 通过模块代码返回该模块拥有的所有权限
	 * @param moduleCode 模块代码
	 * @return 权限列表
	 */
	public List<WlCmSecurity> findNodeByCode(String moduleCode) {
		try {
			String hql = "from WlCmSecurity o where o.type=2 and o.state=1  and o.parentId in (select e.id from WlCmSecurity e where e.type=1 and e.value='" + moduleCode + "')";
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过网址查找功能节点
	 * @param url 网址
	 * @param query 请求参数
	 * @return List
	 */
	public List findNodeByUrl(String url, String query) {
		try {
			String s1 = "select * from wl_cm_security o where o.id in (select e.security_Id from wl_cm_security_url e where '" + url + "'  like ";
			String s2 = this.sqlUtil.concat("'%'", "e.url");
			String s3 = this.sqlUtil.concat(s2, "'%'");
			String s4 = " and ( '" + query + "' like ";
			String s5 = this.sqlUtil.concat("'%'", "e.query");
			String s6 = this.sqlUtil.concat(s5, "'%'");
			String s7 = " or e.query is null ))";
			String sql = s1 + s3 + s4 + s6 + s7;
			return this.findBySQL(sql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 通过ID数组查找功能节点
	 * @param idArray ID数组
	 * @return 权限列表
	 */
	public List findNodeByIdArray(String[] idArray) {
		try {
			String hql = "from WlCmSecurity o where o.state=1 and (1<>1";
			for (int i = 0; i < idArray.length; i++) {
				hql += " or o.id=" + idArray[i];
			}
			hql += ")";
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 是否拥有子权限
	 * @param id ID
	 * @return boolean
	 */
	public boolean isChildSecurity(long id) {
		try {
			String hql = "from WlCmSecurity o where o.type=2 and o.state=1 and o.parentId=" + id;

			return this.find(hql).size() != 0;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过parentId查找子权限
	 * @param id ID
	 * @return List
	 */
	public List findChildByParentId(String id) {
		try {
			String hql = "from WlCmSecurity o where o.type=2 and o.state=1 and o.parentId=? ";
			return this.find(hql, id);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 检测代码是否已存在--存在返回true
	 * @param code 代码
	 * @param parentId ID号
	 * @param type 类型
	 * @return boolean 是否存在
	 */
	public boolean checkHadCode(String code, String parentId, int type) {
		try {
			String hql = "from WlCmSecurity o where o.value= ? ";
			Object[] v;

			if (type == 1) {
				hql += " and o.type=1";
				v = new Object[1];
				v[0] = code;
			}
			else {
				hql += " and o.type=2 and o.parentId= ? ";
				v = new Object[2];
				v[0] = code;
				v[1] = parentId;
			}

			return this.find(hql, v).size() != 0;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据父ID获取子节点
	 * @param parentId 父ID
	 * @param state -1为所有、0为无效的、1为有效的
	 * @return 子节点
	 */
	public List findChild(long parentId, int state) {
		try {
			String hql = "from WlCmSecurity o where o.type=1 and o.securityLevel=0 and o.parentId=" + parentId;
			if (state == 0 || state == 1) {
				hql += " and o.state=" + state;
			}
			hql += " order by o.sequ desc";
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据state获取子节点
	 * @param state -1为所有、0为无效的、1为有效的
	 * @return 子节点
	 */
	public List findChild(int state) {
		try {
			String hql = "from WlCmSecurity o where 1=1 ";
			if (state == 0 || state == 1) {
				hql += " and o.state=" + state;
			}
			hql += " order by o.sequ desc";
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据state,Type获取子节点
	 * @param state -1为所有、0为无效的、1为有效的
	 * @param type 类型
	 * @return 子节点列表
	 */
	public List findChild(int state, int type) {
		try {
			String hql = "from WlCmSecurity o where  o.type=" + type;
			if (state == 0 || state == 1) {
				hql += " and o.state=" + state;
			}
			hql += " order by o.sequ desc";
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 
	 * @param ids 权限id，格式为('1','2','3')
	 * @return 权限列表
	 */
	public List findListByIds(String ids) {
		try {
			String hql = "from WlCmSecurity o where  1=1 ";
			if (!ValidateUtil.isEmpty(ids)) {
				hql += " and o.id in" + ids;
			}
			hql += " order by o.sequ desc";
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}
