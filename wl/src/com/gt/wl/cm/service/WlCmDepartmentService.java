package com.gt.wl.cm.service;

import java.util.List;
import java.util.Map;

import org.joyone.lang.BaseException;
import org.joyone.service.BaseService;
import org.joyone.spring.Sc;
import org.joyone.util.TreeUtil;
import org.springframework.stereotype.Service;

import com.gt.wl.cm.dao.WlCmDepartmentDao;
import com.gt.wl.cm.model.WlCmDepartment;

/**
 * 机构部门Service层
 * @author liuyj
 * 
 */
@Service("wl.cm.WlCmDepartmentService")
public class WlCmDepartmentService extends BaseService {

	private WlCmDepartmentDao wlCmDepartmentDao = (WlCmDepartmentDao) Sc.getBean("wl.cm.WlCmDepartmentDao");

	public WlCmDepartmentService() {
		baseDao = wlCmDepartmentDao;
	}

	/**
	 * 根据状态返回对象集
	 * @param state 状态
	 * @return 对象集
	 */
	public List findByState(int state) {
		try {
			return wlCmDepartmentDao.findByState(state);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据ID返回名称
	 * @param id 机构ID
	 * @return 名称
	 */
	public String getName(String id) {
		try {
			if (id == null) {
				return "";
			}
			WlCmDepartment wlCmDepartment = (WlCmDepartment) this.getObject(id);

			return wlCmDepartment == null ? "" : wlCmDepartment.getName();
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 返回部门Tree 对象
	 * @param state 状态
	 * @param rootId 根节点ID
	 * @return map树对象
	 */
	public Map getTree(int state, String rootId) {
		try {
			return TreeUtil.getPdTree(this.findByState(state), rootId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 返回部门Tree 对象
	 * @param state 状态
	 * @param id 过滤Id
	 * @param rootId 根节点ID
	 * @return map树对象
	 */
	public Map getParentTree(int state, String id, String rootId) {
		try {
			return TreeUtil.getPdTree(this.findByState(state), rootId, id);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}

}