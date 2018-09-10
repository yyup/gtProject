package com.gt.wl.cm.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joyone.action.BaseAction;
import org.joyone.lang.BaseException;
import org.joyone.spring.Sc;
import org.joyone.sys.Lang;
import org.joyone.sys.Page;
import org.joyone.sys.User;
import org.joyone.util.JsonUtil;
import org.joyone.util.Md5Util;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gt.wl.cm.model.WlCmUser;
import com.gt.wl.cm.model.WlCmUserGroup;
import com.gt.wl.cm.service.WlCmDepartmentService;
import com.gt.wl.cm.service.WlCmDutyroleService;
import com.gt.wl.cm.service.WlCmSecurityService;
import com.gt.wl.cm.service.WlCmTypeService;
import com.gt.wl.cm.service.WlCmUserGroupService;
import com.gt.wl.cm.service.WlCmUserService;

/**
 * 用户Action层
 * @author liuyj
 * 
 */
@Controller
@RequestMapping({ "/html/sys/wlCmUserAction.do", "/html/sys/wlCmUserAction.dox" })
public class WlCmUserAction extends BaseAction {

	private final static String SYSTEM_NAME = Lang.getString("wl.cm.SYSTEM_NAME");
	private final static String MODULE_NAME = Lang.getString("wl.cm.WlCmUserAction.MODULE_NAME");
	private final static String MODULE_CODE = "0303001";// 模块代码
	private WlCmUserService wlCmUserService = (WlCmUserService) Sc.getBean("wl.cm.WlCmUserService");
	private WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");
	private WlCmDutyroleService wlCmDutyroleService = (WlCmDutyroleService) Sc.getBean("wl.cm.WlCmDutyroleService");
	private WlCmDepartmentService wlCmDepartmentService = (WlCmDepartmentService) Sc.getBean("wl.cm.WlCmDepartmentService");
	private WlCmUserGroupService wlCmUserGroupService = (WlCmUserGroupService) Sc.getBean("wl.cm.WlCmUserGroupService");

	/**
	 * 界面初始化
	 * @param enumTypes 枚举类型，多个枚举项以“,”隔开，如“SEX,EFFECT”
	 * @param user 当前登录用户
	 * @return 权限与枚举值
	 */
	@RequestMapping(params = "action=init")
	@ResponseBody
	public String init(String[] enumTypes, User user) {
		try {

			WlCmTypeService wlCmTypeService = (WlCmTypeService) Sc.getBean("wl.cm.WlCmTypeService");
			Map mapResult = wlCmSecurityService.getSmcMap(user, MODULE_CODE);// 根据模块代码获取权限
			mapResult.putAll(wlCmTypeService.getEnumType(enumTypes));// 到字典表获取枚举数据
			mapResult.put("state", "1");

			mapResult.put("groupEnum", wlCmDutyroleService.findByCodeAndState("GROUP", 1));
			mapResult.put("groupAllEnum", wlCmDutyroleService.getObjects());

			mapResult.put("departmentEnum", wlCmDepartmentService.findByState(1));
			return this.getJson(true, mapResult);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 修改密码
	 * 
	 * @param oldpass 旧密码
	 * @param newpass 新密码
	 * @param user 当前登录用户
	 * @return true false
	 */
	@RequestMapping(params = "action=changePassword")
	@ResponseBody
	public String changePassword(String oldpass, String newpass, User user) {
		try {
			WlCmUser wlCmUser = (WlCmUser) wlCmUserService.getObject(user.getId());
			if (wlCmUser.getPassword().equals(Md5Util.encrypt(oldpass))) {
				wlCmUser.setPassword(Md5Util.encrypt(newpass));
				wlCmUserService.updateObject(wlCmUser);
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.cm.WlCmUserAction.changePasswordSuccess"), Lang.getString("wl.cm.WlCmUserAction.changePasswordSuccess"));
				return this.getJson(true, "success");
			}
			else {
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.cm.WlCmUserAction.changePasswordError"), Lang.getString("wl.cm.WlCmUserAction.changePasswordError"));
				return this.getJson(false, Lang.getString("wl.cm.WlCmUserAction.changePasswordError"));
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 增加数据
	 * @param wlCmUser 用户对象
	 * @param userGroupJson 用户所属用户组JSON
	 * @param user 当前用户
	 * @return 结果
	 */
	@RequestMapping(params = "action=saveData")
	@ResponseBody
	public String saveData(WlCmUser wlCmUser, String userGroupJson, User user) {
		try {

			if (ValidateUtil.isEmpty(wlCmUser.getId())) {// 新增
				// 检查用户名是否重复
				WlCmUser user2 = wlCmUserService.findByLoginName(wlCmUser.getLoginName());
				if (user2 != null) {
					throw new BaseException(Lang.getString("wl.cm.WlCmUserAction.loginNameDuplication"));
				}
				wlCmUser.setLoginDate(new Date());
				String pass = wlCmUser.getPassword();
				wlCmUser.setPassword(Md5Util.encrypt(pass));
				wlCmUser.setState(1);

			}
			else {// 更新
				WlCmUser oldUser = null;
				oldUser = (WlCmUser) wlCmUserService.getObject(wlCmUser.getId());
				wlCmUser.setLoginName(oldUser.getLoginName());
				// 如果用户密码有变动，则进行密码更新操作
				if (!oldUser.getPassword().equals(wlCmUser.getPassword())) {
					wlCmUser.setPassword(Md5Util.encrypt(wlCmUser.getPassword()));

				}
				wlCmUserGroupService.deleteByUserId(wlCmUser.getId());
			}
			wlCmUser.setDepartmentId("0");// 设置部门id为默认ID
			if (ValidateUtil.isEmpty(wlCmUser.getId())) {
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.add"), "[" + wlCmUser.getName() + "]");
			}
			else {
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.edit"), "[" + wlCmUser.getName() + "]");
			}
			wlCmUserService.saveObject(wlCmUser);

			List<WlCmUserGroup> userGroupList = JsonUtil.toList(userGroupJson, WlCmUserGroup.class);
			for (WlCmUserGroup group : userGroupList) {
				group.setUserId(wlCmUser.getId());
				wlCmUserGroupService.addObject(group);
			}

			return this.getJson(true, "");
		}
		catch (Exception e) {
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

			WlCmUser oldUser = (WlCmUser) wlCmUserService.getObject(user.getId());

			// 如果验证密码成功则进行密码修改操作
			if (oldUser.getPassword().equals(Md5Util.encrypt(oldPassword))) {

				oldUser.setPassword(Md5Util.encrypt(password));
				wlCmUserService.updateObject(oldUser);
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.cm.WlCmUserAction.updataOwner"), "[" + user.getName() + "]");
				return this.getJson(true, "");
			}
			else {
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.cm.WlCmUserAction.updataOwner"), Lang.getString("wl.cm.WlCmUserAction.changePasswordError"));

				return this.getJson(false, Lang.getString("wl.cm.WlCmUserAction.changePasswordError"));// 原始密码错误
			}

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 改变状态
	 * @param ids id
	 * @param state 状态
	 * @param user 当前用户
	 * @return true false
	 */
	@RequestMapping(params = "action=updateState")
	@ResponseBody
	public String updateState(String ids[], int state, User user) {
		try {
			String logs = "";
			for (String id : ids) {
				WlCmUser wlCmUser = (WlCmUser) wlCmUserService.getObject(id);
				if (user.getId().equals(wlCmUser.getId())) {
					continue;
				}
				else {
					wlCmUser.setState(state);
					wlCmUserService.updateObject(wlCmUser);
					logs += wlCmUser.getName() + ";";
				}
			}
			if (state == 0) {
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.cm.WlCmUserAction.delete"), "[" + logs + "]");
			}
			else {
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.cm.WlCmUserAction.recover"), "[" + logs + "]");

			}
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据用户ID返回用户对象
	 * @param id 用户ID
	 * @return 对象
	 */
	@RequestMapping(params = "action=getUser")
	@ResponseBody
	public String getUser(String id) {
		try {
			List list = wlCmUserGroupService.findByUserId(id, 3);// 返回用户组集合
			Map<String, Object> resultMap = new HashMap<String, Object>();
			WlCmUser baseuser = (WlCmUser) wlCmUserService.getObject(id);

			baseuser.setDepartmentName(wlCmDepartmentService.getName(baseuser.getDepartmentId()));
			resultMap.put("user", baseuser);
			resultMap.put("usergroup", list);
			return this.getJson(true, resultMap);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询用户列表，获取翻页数据
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param name 名称
	 * @param state 状态
	 * @return 结果
	 */
	@RequestMapping(params = "action=findDataList")
	@ResponseBody
	public String findUserPages(int pageSize, int currPage, String name, String state) {
		try {
			Page page = wlCmUserService.search(currPage, pageSize, name, state);
			return this.getJson(true, page);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询用户id列表
	 * @param user 当前登录用户
	 * @return loginName列表
	 */
	@RequestMapping(params = "action=findGroupUsers")
	@ResponseBody
	public String findGroupUsers(User user) {
		try {
			List<WlCmUser> userlist = wlCmUserService.findGroupUsers(user.getId());
			if (userlist == null) {
				List<String> idarr = new ArrayList<String>();
				idarr.add(user.getLoginName());
				return this.getJson(true, idarr);
			}
			List<String> idarr = new ArrayList<String>();
			for (WlCmUser baseuser : userlist) {
				idarr.add(baseuser.getLoginName());// 登录名
			}
			return this.getJson(true, idarr);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 修改用户的密码为初始值
	 * @param id 用户ID
	 * @param user 当前登录用户
	 * @return 操作结果
	 */
	@RequestMapping(params = "action=updateUserPwd")
	@ResponseBody
	public String updateUserPwd(String id, User user) {
		try {
			WlCmUser wlCmUser = (WlCmUser) wlCmUserService.getObject(id);
			// 重置密码，用Md5加密
			wlCmUser.setPassword(Md5Util.encrypt("123456"));
			wlCmUserService.updateObject(wlCmUser);
			this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.cm.WlCmUserAction.reset"), Lang.getString("wl.cm.WlCmUserAction.NAME") + wlCmUser.getName());
			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}
}