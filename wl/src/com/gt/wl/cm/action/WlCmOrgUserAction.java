package com.gt.wl.cm.action;

import java.util.Date;
import java.util.Map;

import org.joyone.action.BaseAction;
import org.joyone.action.ParaMap;
import org.joyone.lang.BaseException;
import org.joyone.spring.Sc;
import org.joyone.sys.Lang;
import org.joyone.sys.Session;
import org.joyone.sys.User;
import org.joyone.util.Md5Util;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gt.wl.cm.model.WlCmOrgUser;
import com.gt.wl.cm.service.WlCmOrgService;
import com.gt.wl.cm.service.WlCmOrgUserService;
import com.gt.wl.cm.service.WlCmSecurityService;
import com.gt.wl.cm.service.WlCmTypeService;

/**
 * 往来单位用户Action层
 * @author liuyj
 * 
 */
@Controller
@RequestMapping({ "/wl/cm/wlCmOrgUserAction.do", "/wl/cm/wlCmOrgUserAction.agency" })
public class WlCmOrgUserAction extends BaseAction {
	private WlCmOrgUserService wlCmOrgUserService = (WlCmOrgUserService) Sc.getBean("wl.cm.WlCmOrgUserService");
	private WlCmOrgService wlCmOrgService = (WlCmOrgService) Sc.getBean("wl.cm.WlCmOrgService");
	private WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");
	private WlCmTypeService wlCmTypeService = (WlCmTypeService) Sc.getBean("wl.cm.WlCmTypeService");// 数据字典
	private final static String MODULE_CODE = "0301009";// 模块代码
	private final static String SYSTEM_NAME = Lang.getString("wl.cm.SYSTEM_NAME");
	private final static String MODULE_NAME = Lang.getString("wl.cm.WlCmOrgUserAction.MODULE_NAME");

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
			mapResult.put("orgEnum", wlCmOrgService.findWlCmOrgList("", "", "1", "AGENT"));// 所有启用的经销商往来单位
			mapResult.put("allAgentOrgEnum", wlCmOrgService.findWlCmOrgList("", "", "", "AGENT"));// 所有的经销商往来单位

			return this.getJson(true, mapResult);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 保存数据
	 * @param wlCmOrgUser 经销商用户对象
	 * @return 结果
	 */
	@RequestMapping(params = "action=saveData")
	@ResponseBody
	public String saveData(WlCmOrgUser wlCmOrgUser) {
		try {
			User user = Session.getCurrUser();
			if (ValidateUtil.isEmpty(wlCmOrgUser.getUserId())) {// 新增
				// 检查帐号是否重复
				WlCmOrgUser orgUser = wlCmOrgUserService.getWlCmOrgUser(wlCmOrgUser.getAccount());
				if (!ValidateUtil.isNull(orgUser)) {
					throw new BaseException(Lang.getString("wl.cm.WlCmOrgUserAction.accountDuplication"));
				}
				wlCmOrgUser.setCreator(user.getName());
				wlCmOrgUser.setCreateTime(new Date());
				String pass = wlCmOrgUser.getPassword();
				wlCmOrgUser.setPassword(Md5Util.encrypt(pass));
				wlCmOrgUser.setMemberStateEk("NORMAL");// 正常状态
			}
			else {// 更新
				WlCmOrgUser oldWlCmOrgUser = (WlCmOrgUser) wlCmOrgUserService.getObject(wlCmOrgUser.getUserId());
				// 如果用户密码有变动，则进行密码更新操作
				if (!oldWlCmOrgUser.getPassword().equals(wlCmOrgUser.getPassword())) {
					wlCmOrgUser.setPassword(Md5Util.encrypt(wlCmOrgUser.getPassword()));
				}
				wlCmOrgUser.setModifier(user.getName());
				wlCmOrgUser.setModifyTime(new Date());
			}
			if (!ValidateUtil.isNull(user)) {
				if (ValidateUtil.isEmpty(wlCmOrgUser.getUserId())) {// 新增
					this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.add"), "[" + wlCmOrgUser.getName() + "]");
				}
				else {// 编辑
					this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.edit"), "[" + wlCmOrgUser.getName() + "]");
				}
			}
			wlCmOrgUserService.saveObject(wlCmOrgUser);

			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询经销商用户分页数据
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param paraMap 条件
	 * @return 分页结果
	 */

	@RequestMapping(params = "action=findDataList")
	@ResponseBody
	public String findDataList(int pageSize, int currPage, ParaMap paraMap) {
		try {
			Map map = paraMap.getMap();
			return this.getJson(true, wlCmOrgUserService.findDataList(pageSize, currPage, map));
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取经销商用户
	 * @param userId 经销商用户id
	 * @return 经销商用户
	 */
	@RequestMapping(params = "action=getData")
	@ResponseBody
	public String getData(String userId) {
		try {
			return this.getJson(true, wlCmOrgUserService.getObject(userId));
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 修改经销商用户的密码为初始值
	 * @param userId 经销商用户id
	 * @param user 当前用户
	 * @return 操作结果
	 */
	@RequestMapping(params = "action=updateOrgUserPwd")
	@ResponseBody
	public String updateOrgUserPwd(String userId, User user) {
		try {
			WlCmOrgUser wlCmOrgUser = (WlCmOrgUser) wlCmOrgUserService.getObject(userId);
			// 重置密码，用Md5加密
			wlCmOrgUser.setPassword(Md5Util.encrypt("123456"));
			wlCmOrgUserService.updateObject(wlCmOrgUser);
			this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.cm.WlCmOrgUserAction.reset"), Lang.getString("wl.cm.WlCmOrgUserAction.account") + wlCmOrgUser.getAccount());
			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 修改经销商用户的使用状态
	 * @param userId 经销商用户ID
	 * @param user 当前用户
	 * @return 操作结果
	 */
	@RequestMapping(params = "action=updateOrgUserState")
	@ResponseBody
	public String updateOrgUserState(String userId, User user) {
		try {
			WlCmOrgUser wlCmOrgUser = (WlCmOrgUser) wlCmOrgUserService.getObject(userId);
			// 修改用户使用状态（LOCK-锁定,NORMAL-正常)
			if ("LOCK".equals(wlCmOrgUser.getMemberStateEk())) {
				wlCmOrgUser.setMemberStateEk("NORMAL");
				wlCmOrgUserService.updateObject(wlCmOrgUser);
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.set"), Lang.getString("wl.cm.WlCmOrgUserAction.account") + wlCmOrgUser.getAccount() + "," + Lang.getString("wl.cm.WlCmOrgUserAction.USE"));
			}
			else if ("NORMAL".equals(wlCmOrgUser.getMemberStateEk())) {
				wlCmOrgUser.setMemberStateEk("LOCK");
				wlCmOrgUserService.updateObject(wlCmOrgUser);
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.set"), Lang.getString("wl.cm.WlCmOrgUserAction.account") + wlCmOrgUser.getAccount() + "," + Lang.getString("wl.cm.WlCmOrgUserAction.LOCK"));
			}

			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 修改密码
	 * @param oldPassword 旧密码
	 * @param password 新密码
	 * @param user 当前用户
	 * @return 脚本
	 */
	@RequestMapping(params = "action=updateSelf")
	@ResponseBody
	public String updateSelf(String oldPassword, String password, User user) {
		try {

			WlCmOrgUser oldWlCmOrgUser = (WlCmOrgUser) wlCmOrgUserService.getObject(user.getId());

			// 如果验证密码成功则进行密码修改操作
			if (oldWlCmOrgUser.getPassword().equals(Md5Util.encrypt(oldPassword))) {

				oldWlCmOrgUser.setPassword(Md5Util.encrypt(password));
				wlCmOrgUserService.updateObject(oldWlCmOrgUser);
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.set"), Lang.getString("wl.cm.WlCmOrgUserAction.account") + oldWlCmOrgUser.getName() + "," + Lang.getString("wl.cm.WlCmOrgUserAction.changePassword"));
				return this.getJson(true, "");
			}
			else {
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.set"), Lang.getString("wl.cm.WlCmOrgUserAction.changePasswordError"));

				return this.getJson(false, Lang.getString("wl.cm.WlCmOrgUserAction.changePasswordError"));// 原始密码错误
			}

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}
