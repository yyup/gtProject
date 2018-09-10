package com.gt.wl.cm.service;

import org.joyone.lang.BaseException;
import org.joyone.service.BaseService;
import org.joyone.spring.Sc;
import org.joyone.sys.Page;
import org.springframework.stereotype.Service;


import com.gt.wl.cm.dao.WlCmDocumentPathDao;
import com.gt.wl.cm.model.WlCmDocumentPath;
/**
 * 文件路径管理Service层
 * @author huangbj
 * 
 */
@Service("wl.cm.WlCmDocumentPathService")
public class WlCmDocumentPathService extends BaseService {
	private WlCmDocumentPathDao wlCmDocumentPathDao = (WlCmDocumentPathDao) Sc.getBean("wl.cm.WlCmDepartmentPathDao");

	public WlCmDocumentPathService() {
		baseDao = wlCmDocumentPathDao;
	}

	/**
	 * 分页查找所有的文件路径信息
	 * @param pageSize 页记录数
	 * @param currPage 页码
	 * @return 文件路径信息
	 */
	public Page findAllDocPath(int pageSize, int currPage) {
		try {
			return wlCmDocumentPathDao.findAllDocPath(pageSize, currPage);
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

			return wlCmDocumentPathDao.findBySourceCode(sourceCode);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}

}