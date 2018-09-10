package com.gt.wl.cm.service;

import java.util.List;

import org.joyone.lang.BaseException;
import org.joyone.service.BaseService;
import org.joyone.spring.Sc;
import org.springframework.stereotype.Service;

import com.gt.wl.cm.dao.WlCmSecurityUrlDao;
import com.gt.wl.cm.model.WlCmSecurityUrl;

/**
 * 网址Service层
 * @author liuyj
 * 
 */
@Service("wl.cm.WlCmSecurityUrlService")
public class WlCmSecurityUrlService extends BaseService {
	private WlCmSecurityUrlDao wlCmSecurityUrlDao = (WlCmSecurityUrlDao) Sc.getBean("wl.cm.WlCmSecurityUrlDao");

	public WlCmSecurityUrlService() {
		baseDao = wlCmSecurityUrlDao;
	}

	/**
	 * 通过权限ID号查找相关网址
	 * @param securityId 权限ID号
	 * @return List
	 */
	public List findBySecurityId(String securityId) {
		try {
			return wlCmSecurityUrlDao.findBySecurityId(securityId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据权限ID 删除权限路径数据
	 * @param securityId 权限ID
	 * @return delete count
	 */
	public int deleteBySecurityId(String securityId) {
		try {
			return wlCmSecurityUrlDao.deleteBySecurityId(securityId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 保存 url地址
	 * @param securityId 权限对应ID
	 * @param list 数组
	 */
	public void saveSecurityUrlList(String securityId, List<WlCmSecurityUrl> list) {
		try {

			this.deleteBySecurityId(securityId);
			for (WlCmSecurityUrl url : list) {
				WlCmSecurityUrl newUrl = new WlCmSecurityUrl();
				newUrl.setQuery(url.getQuery());
				newUrl.setSecurityId(securityId);
				newUrl.setUrl(url.getUrl());
				this.addObject(newUrl);
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}