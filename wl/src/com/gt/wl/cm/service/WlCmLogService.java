package com.gt.wl.cm.service;

import java.util.Date;

import org.joyone.lang.BaseException;
import org.joyone.service.BaseService;
import org.joyone.service.ILogService;
import org.joyone.spring.Sc;
import org.joyone.sys.Page;
import org.joyone.sys.User;
import org.springframework.stereotype.Service;

import com.gt.wl.cm.dao.WlCmLogDao;
import com.gt.wl.cm.model.WlCmLog;

/**
 * 日志管理Service层
 * @author liuyj
 * 
 */
@Service("wl.cm.WlCmLogService,org.joyone.LogSerivce")
public class WlCmLogService extends BaseService implements ILogService {
	private WlCmLogDao wlCmLogDao = (WlCmLogDao) Sc.getBean("wl.cm.WlCmLogDao");

	public WlCmLogService() {
		baseDao = wlCmLogDao;
	}

	/**
	 * 增加操作日志
	 * @param user 用户对象
	 * @param systemName 系统名称
	 * @param moduleName 模块名称
	 * @param content 内容
	 * @param memo 备注
	 */
	public void addLog(User user, String systemName, String moduleName, String content, String memo) {
		try {
			if ("sys".equals(user.getType())) {// 系统用户日志添加
				WlCmLog wlCmLog = new WlCmLog();
				wlCmLog.setContent(content);
				wlCmLog.setMemo(memo);
				wlCmLog.setUserId(user.getId());
				wlCmLog.setUserName(user.getName());
				wlCmLog.setSystemName(systemName);
				wlCmLog.setModuleName(moduleName);
				wlCmLog.setNodeTime(new Date());
				wlCmLog.setIp(user.getIp());
				wlCmLogDao.addObject(wlCmLog);
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);

		}
	}

	/**
	 * 分页查询日志/根据关键词查询
	 * @param baselog 日志
	 * @param fromDate 开始日期
	 * @param toDate 结束日期
	 * @param currPage 当前页
	 * @param pageSize 每页大小
	 * @return 分页日志信息
	 */
	public Page search(WlCmLog baselog, String fromDate, String toDate, int currPage, int pageSize) {
		try {
			return wlCmLogDao.search(baselog, fromDate, toDate, currPage, pageSize);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}