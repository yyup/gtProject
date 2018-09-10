package com.gt.wl.cm.dao;

import java.util.List;

import org.joyone.dao.BaseDao;
import org.joyone.lang.BaseException;
import org.joyone.sys.Page;
import org.springframework.stereotype.Repository;
import com.gt.wl.cm.model.WlCmDocumentPath;

/**
 * 文件路径管理DAO层
 * @author huangbj
 * 
 */
@Repository("wl.cm.WlCmDepartmentPathDao")
public class WlCmDocumentPathDao extends BaseDao {

	public WlCmDocumentPathDao() {
		modelClass = WlCmDocumentPath.class;
		defaultOrder = "";
	}
	/**
	 * 分页查找所有的文件路径信息
	 * @param pageSize 页记录数
	 * @param currPage 页码
	 * @return 文件路径信息
	 */
	public Page findAllDocPath(int pageSize, int currPage) {
		try {
			String hql = "from WlCmDocumentPath p order by p.id";
			return this.findPage(hql, currPage, pageSize);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过 sourceCode 查找相应的路径
	 * @param sourceCode 来源代号
	 * @return 路径信息
	 */
	public WlCmDocumentPath findBySourceCode(String sourceCode) {
		try {
			String hql = "from WlCmDocumentPath p where sourceCode='" + sourceCode + "'";
			List<WlCmDocumentPath> pathList = this.find(hql);
			if (pathList.size() > 0 && pathList != null) {
				return pathList.get(0);
			}
			else {
				return null;
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}
