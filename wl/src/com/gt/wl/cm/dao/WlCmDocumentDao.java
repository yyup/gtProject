package com.gt.wl.cm.dao;

import java.util.List;

import org.joyone.dao.BaseDao;
import org.joyone.lang.BaseException;
import org.springframework.stereotype.Repository;
import com.gt.wl.cm.model.WlCmDocument;
/**
 * 文件管理DAO层
 * @author huangbj
 * 
 */
@Repository("wl.cm.WlCmDocumentDao")
public class WlCmDocumentDao extends BaseDao {

	public WlCmDocumentDao() {
		modelClass = WlCmDocument.class;
		defaultOrder = "";
	}
	/**
	 * 根据名称原名称和源文件夹返回对象集
	 * @param source :文件夹
	 * @param name 名称
	 * @return 对象集
	 */
	public List findByName(String source, String name) {
		try {
			String hql = "from WlCmDocument o where o.name like '%" + name + "%'";
			if (!("".equals(source))) {
				hql += " and o.sourceCode ='" + source + "'";
			}
			hql += " order by o.sequ desc";
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据名称原名称和源文件夹返回用户对象集
	 * @param source :文件夹
	 * @param name 名称
	 * @param userId 用户ID
	 * @return 对象集
	 */
	public List findByName(String source, String name, String userId) {
		try {
			String hql = "from WlCmDocument o where o.name like '%" + name + "%' and o.userId = '" + userId + "' ";
			if (!("".equals(source))) {
				hql += " and o.sourceCode ='" + source + "'";
			}
			hql += " order by o.sequ desc";
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过sourceId查找文件信息
	 * @param sourceId 文件的sourceId
	 * @return 文件信息
	 */
	public WlCmDocument findBySourceId(String sourceId) {
		try {
			String hql = "from WlCmDocument o where o.sourceId = '" + sourceId + "' order by o.sequ desc";
			List<WlCmDocument> docList = this.find(hql);
			if (docList.size() > 0 && docList != null) {
				return docList.get(0);
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
	 * 通过id和workUploadFile表相同查询文件信息
	 * @return 文件信息
	 */
	public List findByUploadId() {
		try {
			String hql = "from WlCmDocument o where o.sourceId in(select w.id from WorkUpLoadFile w) order by o.id desc";
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过id和FormerUploadFile表相同查询文件信息
	 * @return 文件信息
	 */
	public List findByFoemerUploadId() {
		try {
			String hql = "from WlCmDocument o where o.sourceId in(select w.id from FormerUploadfile w) order by o.id desc";
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}
