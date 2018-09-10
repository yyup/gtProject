package com.gt.wl.cm.service;

import java.util.List;

import org.joyone.lang.BaseException;
import org.joyone.service.BaseService;
import org.joyone.spring.Sc;
import org.springframework.stereotype.Service;

import com.gt.wl.cm.dao.WlCmUserGroupDao;

/**
 * 用户组管理Service层
 * @author liuyj
 * 
 */
@Service("wl.cm.WlCmUserGroupService")
public class WlCmUserGroupService extends BaseService {
	private WlCmUserGroupDao wlCmUserGroupDao = (WlCmUserGroupDao) Sc.getBean("wl.cm.WlCmUserGroupDao");

	public WlCmUserGroupService() {
		baseDao = wlCmUserGroupDao;
	}

	/**
	 * 用户是否属于该组（职务与角色）
	 * @param userId 用户ID
	 * @param groupId 组ID
	 * @param type 类型1表示职务，2表示角色，3表示用户组
	 * @return boolean
	 */
	public boolean checkIsBelongToGroup(String userId, String groupId, int type) {
		try {
			return wlCmUserGroupDao.isBelongToGroup(userId, groupId, type);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 用户是否属于该部门下的该组（职务与角色）
	 * @param userId 用户ID
	 * @param departmentId 部门ID
	 * @param groupId 组ID
	 * @param type 类型1表示职务，2表示角色，3表示用户组
	 * @return boolean
	 */
	public boolean checkIsBelongToGroup(String userId, String departmentId, String groupId, int type) {
		try {
			return wlCmUserGroupDao.isBelongToGroup(userId, departmentId, groupId, type);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 用户是否属于该部门
	 * @param userId 用户ID
	 * @param departmentId 部门ID
	 * @param type 类型1表示职务，2表示角色，3表示用户组
	 * @return boolean
	 */
	public boolean checkIsBelongToDepartment(String userId, String departmentId, int type) {
		try {
			return wlCmUserGroupDao.isBelongToDepartment(userId, departmentId, type);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过用户ID查找组集合
	 * @param userId 用户ID
	 * @param type 类型
	 * @return 用户组列表
	 */
	public List findByUserId(String userId, int type) {
		try {
			return wlCmUserGroupDao.findByUserId(userId, type);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过用户ID查找组集合
	 * @param userId 用户ID
	 * @return 用户组列表
	 */
	public List findByUserId(String userId) {
		try {
			return wlCmUserGroupDao.findByUserId(userId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据用户ID删除职务角色信息
	 * @param userId 用户ID
	 * @return 删除记录数
	 */
	public int deleteByUserId(String userId) {
		try {
			return wlCmUserGroupDao.deleteByUserId(userId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}