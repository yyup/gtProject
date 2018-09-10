package com.gt.wl.cm.service;

import org.joyone.service.BaseService;
import org.springframework.stereotype.Service;

import com.gt.wl.cm.dao.WlCmUnitDao;

/**
 * 单位service层
 * @author huangbj
 * 
 */
@Service("wl.cm.WlCmUnitService")
public class WlCmUnitService extends BaseService {
	private WlCmUnitDao wlCmUnitDao = new WlCmUnitDao();

	public WlCmUnitService() {
		baseDao = wlCmUnitDao;
	}

}