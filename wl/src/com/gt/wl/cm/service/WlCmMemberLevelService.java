package com.gt.wl.cm.service;

import org.joyone.service.BaseService;
import org.joyone.spring.Sc;
import org.springframework.stereotype.Service;

import com.gt.wl.cm.dao.WlCmMemberLevelDao;

/**
 * 会员等级Service层
 * @author liuyj
 * 
 */
@Service("wl.cm.WlCmMemberLevelService")
public class WlCmMemberLevelService extends BaseService {
	private WlCmMemberLevelDao wlCmMemberLevelDao = (WlCmMemberLevelDao) Sc.getBean("wl.cm.WlCmMemberLevelDao");

	public WlCmMemberLevelService() {
		baseDao = wlCmMemberLevelDao;
	}

}