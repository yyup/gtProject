package com.gt.wl.cm.action;

import java.util.Map;

import org.joyone.action.BaseAction;
import org.joyone.lang.BaseException;
import org.joyone.spring.Sc;
import org.joyone.sys.Page;
import org.joyone.sys.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gt.wl.cm.model.WlCmLog;
import com.gt.wl.cm.service.WlCmLogService;
import com.gt.wl.cm.service.WlCmSecurityService;
import com.gt.wl.cm.service.WlCmTypeService;

/**
 * 日志Action层
 * @author liuyj
 * 
 */
@Controller
@RequestMapping("/html/sys/wlCmLogAction.do")
public class WlCmLogAction extends BaseAction {
	private WlCmLogService wlCmLogService = (WlCmLogService) Sc.getBean("wl.cm.WlCmLogService");
	private WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");
	private WlCmTypeService wlCmTypeService = (WlCmTypeService) Sc.getBean("wl.cm.WlCmTypeService");
	private final static String MODULE_CODE = "0303005";// 模块代码

	/**
	 * 界面初始化
	 * @param enumTypes 枚举类型，多个枚举项以“,”隔开，如“SEX,PARTY”
	 * @param user 当前用户
	 * @return 权限与枚举值
	 */
	@RequestMapping(params = "action=init")
	@ResponseBody
	public String init(String[] enumTypes, User user) {
		try {

			Map mapResult = wlCmSecurityService.getSmcMap(user, MODULE_CODE);// 根据模块代码获取权限
			mapResult.putAll(wlCmTypeService.getEnumType(enumTypes));// 到字典表获取枚举数据
			return this.getJson(true, mapResult);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询用户列表，获取翻页数据
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param fromTime 开始时间
	 * @param toTime 结束时间
	 * @param log 日志
	 * @return 结果
	 */
	@RequestMapping(params = "action=findPage")
	@ResponseBody
	public String findPage(int pageSize, int currPage, String fromTime, String toTime, WlCmLog log) {
		try {
			Page page = wlCmLogService.search(log, fromTime, toTime, currPage, pageSize);
			return this.getJson(true, page);
		}
		catch (Exception e) {
			throw new BaseException(e, this.log);
		}
	}

	/**
	 * 根据ID获取对象
	 * @param id id
	 * @return 对象
	 */
	@RequestMapping(params = "action=getData")
	@ResponseBody
	public String getData(String id) {
		try {
			return this.getJson(true, wlCmLogService.getObject(id));
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}