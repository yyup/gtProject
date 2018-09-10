package com.gt.wl.cm.action;

import java.util.Date;
import java.util.Map;

import org.joyone.action.BaseAction;
import org.joyone.lang.BaseException;
import org.joyone.spring.Sc;
import org.joyone.sys.Lang;
import org.joyone.sys.Page;
import org.joyone.sys.Session;
import org.joyone.sys.User;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gt.wl.cm.model.WlCmAgency;
import com.gt.wl.cm.service.WlCmAgencyService;
import com.gt.wl.cm.service.WlCmSecurityService;
import com.gt.wl.cm.service.WlCmTypeService;

/**
 * 经销商管理action层
 * @author chench
 * 
 */
@Controller
@RequestMapping("/wl/cm/wlCmAgencyAction.do")
public class WlCmAgencyAction extends BaseAction {
	private WlCmAgencyService wlCmAgencyService = (WlCmAgencyService) Sc.getBean("wl.cm.WlCmAgencyService");
	private WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");
	private WlCmTypeService wlCmTypeService = (WlCmTypeService) Sc.getBean("wl.cm.WlCmTypeService");
	private final static String MODULE_CODE = "0301008";// 模块代码
	private final static String SYSTEM_NAME = Lang.getString("wl.cm.SYSTEM_NAME");
	private final static String MODULE_NAME = Lang.getString("wl.cm.wlCmAgencyAction.MODULE_NAME");

	/**
	 * 界面初始化
	 * @param enumTypes 枚举类型
	 * @return 权限与枚举值
	 */
	@RequestMapping(params = "action=init")
	@ResponseBody
	public String init(String[] enumTypes) {
		try {
			// 根据模块代码获取权限
			Map mapResult = wlCmSecurityService.getSmcMap(Session.getCurrUser(), MODULE_CODE);
			// 到字典表获取枚举数据
			mapResult.putAll(wlCmTypeService.getEnumType(enumTypes));
			return this.getJson(true, mapResult);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 返回翻页对象
	 * @param pageSize 页大小
	 * @param currPage 当前页
	 * @param agencyName 经销商名称
	 * @return 翻页对象
	 */
	@RequestMapping(params = "action=findPage")
	@ResponseBody
	public String findPage(int pageSize, int currPage, String agencyName) {
		try {

			Page page = wlCmAgencyService.search(currPage, pageSize, agencyName);
			return this.getJson(true, page);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取详细数据
	 * @param agencyId 经销商ID
	 * @return 结果
	 */
	@RequestMapping(params = "action=getData")
	@ResponseBody
	public String getData(String agencyId) {
		try {
			WlCmAgency wlCmAgency = (WlCmAgency) wlCmAgencyService.getObject(agencyId);
			return this.getJson(true, wlCmAgency);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 保存数据
	 * @param wlCmAgency 经销商对象
	 * @param user 当前用户对象
	 * @return 结果
	 */
	@RequestMapping(params = "action=saveData")
	@ResponseBody
	public String saveData(WlCmAgency wlCmAgency, User user) {
		try {

			if (ValidateUtil.isEmpty(wlCmAgency.getAgencyId())) {// 新增
				wlCmAgency.setCreator(user.getName());
				wlCmAgency.setCreateTime(new Date());
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.add"), wlCmAgency.getAgencyName());
			}
			else {// 编辑
				wlCmAgency.setModifyTime(new Date());
				wlCmAgency.setModifier(user.getName());
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.edit"), wlCmAgency.getAgencyName());
			}

			wlCmAgencyService.saveObject(wlCmAgency);
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 删除选中数据
	 * @param ids 选中id数组
	 * @return json结果
	 */
	@RequestMapping(params = "action=deleteData")
	@ResponseBody
	public String deleteData(String ids[]) {
		String agencyNames = "";
		try {
			// 循环得到删除经销商名称
			for (int i = 0; i < ids.length; i++) {
				WlCmAgency agency = (WlCmAgency) wlCmAgencyService.getObject(ids[i]);
				agencyNames += agency.getAgencyName() + ",";
			}
			// 去掉最尾部逗号
			if (!ValidateUtil.isEmpty(agencyNames)) {
				agencyNames = agencyNames.substring(0, agencyNames.length() - 1);
			}
			// 删除
			wlCmAgencyService.deleteObject(ids);
			// 删除日志
			this.addLog(Session.getCurrUser(), SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.delete"), agencyNames);
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}