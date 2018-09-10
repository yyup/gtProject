package com.gt.wl.cm.service;

import org.joyone.service.BaseService;
import org.springframework.stereotype.Service;

import com.gt.wl.cm.dao.WlCmDocumentTypeDao;

@Service("wl.WlCmDocumentTypeService")
public class WlCmDocumentTypeService extends BaseService {
	private WlCmDocumentTypeDao wlCmDocumentTypeDao = new WlCmDocumentTypeDao();

	public WlCmDocumentTypeService() {
		baseDao = wlCmDocumentTypeDao;
	}

}