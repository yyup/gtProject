package com.gt.wl.cm.action;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.joyone.action.BaseAction;
import org.joyone.lang.BaseException;
import org.joyone.spring.Sc;
import org.joyone.sys.Lang;
import org.joyone.sys.Page;
import org.joyone.sys.User;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gt.wl.cm.model.WlCmOrg;
import com.gt.wl.cm.service.WlCmOrgService;
import com.gt.wl.cm.service.WlCmSecurityService;
import com.gt.wl.cm.service.WlCmTypeService;
import com.gt.wl.util.Type;

@Controller
@RequestMapping("/wl/cm/wlCmOrgAction.do")
public class WlCmOrgAction extends BaseAction {
	private WlCmOrgService wlCmOrgService = (WlCmOrgService) Sc.getBean("wl.cm.WlCmOrgService");
	private WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");
	private WlCmTypeService wlCmTypeService = (WlCmTypeService) Sc.getBean("wl.cm.WlCmTypeService");

	private final static String MODULE_CODE = "0304001";// 模块代码
	private final static String SYSTEM_NAME = Lang.getString("wl.cm.SYSTEM_NAME");
	private final static String MODULE_NAME = Lang.getString("wl.cm.wlCmOrgAction.MODULE_NAME");

	/**
	 * 界面初始化
	 * @param enumTypes 枚举类型
	 * @param user 当前登录用户
	 * @return 权限与枚举值
	 */
	@RequestMapping(params = "action=init")
	@ResponseBody
	public String init(String[] enumTypes, User user) {
		try {
			Map mapResult = wlCmSecurityService.getSmcMap(user, MODULE_CODE);// 根据模块代码获取权限
			WlCmTypeService wlCmTypeService = (WlCmTypeService) Sc.getBean("wl.cm.WlCmTypeService");
			mapResult.putAll(wlCmTypeService.getEnumType(enumTypes));// 到字典表获取枚举数据
			return this.getJson(true, mapResult);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询往来单位，获取翻页数据
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param orgTypeEk 单位类型
	 * @param content 要查询的内容编号或者名称
	 * @return 结果
	 */
	@RequestMapping(params = "action=findDataList")
	@ResponseBody
	public String findDataList(int pageSize, int currPage, String orgTypeEk, String content) {

		try {
			Page page = wlCmOrgService.search(pageSize, currPage, orgTypeEk, content);
			List<WlCmOrg> list = page.getItems();
			for (WlCmOrg wlCmOrg : list) {
				// 将orgTypeEk转为对应的中文值
				wlCmOrg.setOrgTypeEkValue(wlCmOrgService.getOrgTypeValue(wlCmOrg.getOrgTypeEk()));
			}

			return this.getJson(true, page);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 增加数据
	 * @param wlCmOrg 往来单位对象
	 * @param user 当前登录用户
	 * @return 结果
	 */
	@RequestMapping(params = "action=saveData")
	@ResponseBody
	public String saveData(WlCmOrg wlCmOrg, User user) {

		try {
			if (ValidateUtil.isEmpty(wlCmOrg.getOrgId())) {// 新建
				wlCmOrg.setCreator(user.getName());
				wlCmOrg.setCreateTime(new Date());

				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.add"), wlCmOrg.getOrgName());
			}
			else {// 编辑
				wlCmOrg.setModifier(user.getName());
				wlCmOrg.setModifyTime(new Date());
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.edit"), wlCmOrg.getOrgName());
			}

			wlCmOrgService.saveObject(wlCmOrg);
			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 修改往来单位的状态
	 * @param orgId 单位id
	 * @param user 当前用户
	 * @return 操作结果
	 */
	@RequestMapping(params = "action=updateOrgState")
	@ResponseBody
	public String updateOrgState(String orgId, User user) {
		try {
			// 获取往来单位对象
			WlCmOrg wlCmOrg = (WlCmOrg) wlCmOrgService.getObject(orgId);
			// 修改使用状态（0-禁用,1-启用)
			if ("0".equals(wlCmOrg.getIsEnableFlag())) {
				wlCmOrg.setIsEnableFlag("1");
				wlCmOrgService.updateObject(wlCmOrg);
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.set"), wlCmOrg.getOrgCd() + Lang.getString("wl.cm.wlCmOrgAction.USE"));
			}
			else if ("1".equals(wlCmOrg.getIsEnableFlag())) {
				wlCmOrg.setIsEnableFlag("0");
				wlCmOrgService.updateObject(wlCmOrg);
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.set"), wlCmOrg.getOrgCd() + Lang.getString("wl.cm.wlCmOrgAction.LOCK"));
			}

			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据单位id获取往来单位对象
	 * @param orgId 单位id
	 * @return 对象
	 */
	@RequestMapping(params = "action=getData")
	@ResponseBody
	public String getData(String orgId) {
		try {
			// 获取往来单位对象
			WlCmOrg wlCmOrg = (WlCmOrg) wlCmOrgService.getObject(orgId);
			// 得到往来单位类型
			List<Type> orgTypeEkList = wlCmTypeService.findByCode("ORG_TYPE_EK");
			String orgTypeEk = wlCmOrg.getOrgTypeEk();
			String[] orgTypeEks = orgTypeEk.split(",");
			String result = "";
			for (String org : orgTypeEks) {
				for (Type type : orgTypeEkList) {
					if (type.getId().equals(org)) {
						result += type.getLable() + "|";
						break;
					}
				}
			}
			if (result.length() > 1) {
				result = result.substring(0, result.length() - 1);
			}
			wlCmOrg.setOrgTypeEkValue(result);
			return this.getJson(true, wlCmOrg);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取往来单位列表
	 * @param orgCd 单位编码
	 * @param orgId 单位id
	 * @return 往来单位列表
	 */
	@RequestMapping(params = "action=findWlCmOrgList")
	@ResponseBody
	public String findWlCmOrgList(String orgCd, String orgId) {
		try {
			List list = wlCmOrgService.findWlCmOrgList(orgCd, orgId, null, null);
			if (!ValidateUtil.isEmpty(list)) {
				return this.getJson(true, list);
			}
			else {
				return this.getJson(true, "");
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}