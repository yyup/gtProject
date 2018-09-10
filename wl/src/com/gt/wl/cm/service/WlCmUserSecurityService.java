package com.gt.wl.cm.service;

import java.util.List;

import org.joyone.lang.BaseException;
import org.joyone.service.BaseService;
import org.joyone.spring.Sc;
import org.springframework.stereotype.Service;

import com.gt.wl.cm.dao.WlCmUserSecurityDao;
import com.gt.wl.cm.model.WlCmSecurity;
import com.gt.wl.cm.model.WlCmUserSecurity;

/**
 * 用户权限Service层
 * @author liuyj
 * 
 */
@Service("wl.cm.WlCmUserSecurityService")
public class WlCmUserSecurityService extends BaseService {
	private WlCmUserSecurityDao wlCmUserSecurityDao = (WlCmUserSecurityDao) Sc.getBean("wl.cm.WlCmUserSecurityDao");

	public WlCmUserSecurityService() {
		baseDao = wlCmUserSecurityDao;
	}

	/**
	 * 通过模块ID号和功能ID数组查找所有的权限定义节点
	 * @param securityId 模块ID号
	 * @param idArray 功能ID数组
	 * @return List
	 */
	public List findAllNode(String securityId, String[] idArray) {
		try {
			return wlCmUserSecurityDao.findAllNode(securityId, idArray);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过模块ID号或功能ID查找所有的权限定义节点
	 * @param idArray 功能ID或模块ID数组
	 * @return List
	 */
	public List findAllNode(String[] idArray) {
		try {
			return wlCmUserSecurityDao.findAllNode(idArray);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过模块ID号或功能ID查找所有的权限定义节点
	 * @param securityId 模块ID号或功能ID
	 * @return List
	 */
	public List findAllNode(String securityId) {
		try {
			return wlCmUserSecurityDao.findAllNode(securityId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过模块ID号查找所有的权限定义
	 * @param securityId 功能ID
	 * @return List
	 */
	public List findAllBySecurityId(String securityId) {
		try {
			return wlCmUserSecurityDao.findAllBySecurityId(securityId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 
	 * @param ownerId 根据拥有者查找权限
	 * @return 拥有者权限列表
	 */
	public List findListByOwnerId(String ownerId) {
		try {
			return wlCmUserSecurityDao.findListByOwnerId(ownerId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 保存权限
	 * @param securitys 权限,用逗号隔开
	 * @param ownerId 拥有者
	 * @param departmentId 组织机构ID
	 * @param type 类型
	 * @param user 当前用户
	 * @return 结果
	 */
	public void saveUserSecuriy(String securitys, String ownerId, String departmentId, int type) {
		try {
			wlCmUserSecurityDao.deleteData(ownerId);// 删除角色当前拥有的权限
			WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");
			String[] securityArray = securitys.split(",");
			for (String securityId : securityArray) {
				WlCmSecurity wlCmSecurity = (WlCmSecurity) wlCmSecurityService.getObject(securityId);
				WlCmUserSecurity wlCmUserSecurity = new WlCmUserSecurity();
				wlCmUserSecurity.setDepartmentId(departmentId);
				wlCmUserSecurity.setOwnerId(ownerId);
				wlCmUserSecurity.setSecurityArray(securityId);
				wlCmUserSecurity.setSecurityId(wlCmSecurity.getParentId());
				wlCmUserSecurity.setType(type);
				this.saveObject(wlCmUserSecurity);
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}