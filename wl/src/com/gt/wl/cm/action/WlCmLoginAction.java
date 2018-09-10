package com.gt.wl.cm.action;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.joyone.action.BaseAction;
import org.joyone.lang.BaseException;
import org.joyone.spring.Sc;
import org.joyone.sys.Lang;
import org.joyone.sys.User;
import org.joyone.util.DateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gt.wl.cm.model.WlCmOrgUser;
import com.gt.wl.cm.model.WlCmUser;
import com.gt.wl.cm.service.WlCmOrgUserService;
import com.gt.wl.cm.service.WlCmUserService;

/**
 * 登录Action
 * @author liuyj
 */
@Controller
@RequestMapping("/noSecurity/wlCmLoginAction.do")
public class WlCmLoginAction extends BaseAction {

	private final static String SYSTEM_NAME = Lang.getString("wl.cm.SYSTEM_NAME");
	private final static String MODULE_NAME = Lang.getString("wl.cm.WlCmUserAction.MODULE_NAME");
	private WlCmUserService wlCmUserService = (WlCmUserService) Sc.getBean("wl.cm.WlCmUserService");
	private WlCmOrgUserService wlCmOrgUserService = (WlCmOrgUserService) Sc.getBean("wl.cm.WlCmOrgUserService");

	/**
	 * 系统用户登录
	 * @param username 登录名
	 * @param pwd 密码
	 * @param request 请求
	 * @return 跳转的相应页面名
	 */
	@RequestMapping(params = "action=login")
	@ResponseBody
	public String login(String username, String pwd, HttpServletRequest request) {
		try {
			/**
			 * 获取用户的客户端信息
			 */

			WlCmUser user = wlCmUserService.findByLoginName(username);

			if (user == null) {
				// 用户不存在
				return this.getJson(false, Lang.getString("wl.cm.WlCmUserAction.noUser"));
			}
			else if (user.getState() != 1) {
				return this.getJson(false, Lang.getString("wl.cm.WlCmUserAction.deleted"));
			}
			else if (pwd.equals(user.getPassword())) {

				// 用户信息存入session
				User sessionUser = new User();
				// WlCmDepartmentService departmentService = (WlCmDepartmentService) Sc.getBean("wl.cm.WlCmDepartmentService");
				sessionUser.setIp(this.getIp(request));
				sessionUser.setAdmin(user.getIsAdmin() == 1);
				sessionUser.setProgrammer(user.getIsProgrammer() == 1);
				sessionUser.setDepartmentId(user.getDepartmentId());
				sessionUser.setName(user.getName());
				sessionUser.setLoginName(user.getLoginName());
				sessionUser.setId(user.getId());
				sessionUser.setType("sys");// 设置当前登录为系统用户
				// sessionUser.setDepartmentName(departmentService.getName(user.getDepartmentId()));// 设置所在部门名称
				sessionUser.setLoginTime(DateUtil.dateFormatFromDateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
				HttpSession session = request.getSession(true);
				session.removeAttribute("USER");
				session.setAttribute("USER", sessionUser);
				this.addLog(sessionUser, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.cm.WlCmUserAction.login"), Lang.getString("wl.cm.WlCmUserAction.success"));

				return this.getJson(true, sessionUser);

			}
			else {
				return this.getJson(false, Lang.getString("wl.cm.WlCmUserAction.passwordError"));
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 经销商用户登录
	 * @param username 登录名
	 * @param pwd 密码
	 * @param request 请求
	 * @return 跳转的相应页面名
	 */
	@RequestMapping(params = "action=agencyLogin")
	@ResponseBody
	public String agencyLogin(String username, String pwd, HttpServletRequest request) {
		try {
			/**
			 * 获取用户的客户端信息
			 */
			WlCmOrgUser user = wlCmOrgUserService.getWlCmOrgUser(username);

			if (user == null) {
				// 用户不存在
				return this.getJson(false, Lang.getString("wl.cm.WlCmUserAction.noUser"));
			}
			else if ("LOCK".equals(user.getMemberStateEk())) {
				return this.getJson(false, Lang.getString("wl.cm.WlCmUserAction.deleted"));
			}
			else if (pwd.equals(user.getPassword())) {
				// 用户信息存入session
				User sessionUser = new User();
				sessionUser.setIp(this.getIp(request));
				sessionUser.setName(user.getName());
				sessionUser.setLoginName(user.getAccount());
				sessionUser.setId(user.getUserId());
				sessionUser.setCorpId(user.getOrgId());// 设置用户所属公司ID
				sessionUser.setType("corp");// 设置当前登录为企业用户
				sessionUser.setLoginTime(DateUtil.dateFormatFromDateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
				HttpSession session = request.getSession(true);
				session.removeAttribute("USER");
				session.setAttribute("USER", sessionUser);
				this.addLog(sessionUser, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.cm.WlCmUserAction.login"), Lang.getString("wl.cm.WlCmUserAction.success"));
				return this.getJson(true, sessionUser);
			}
			else {
				return this.getJson(false, Lang.getString("wl.cm.WlCmUserAction.passwordError"));
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 退出系统，清除用户session
	 * @param request 请求
	 * @param response 响应
	 * @param user 当前登录用户
	 */
	@RequestMapping(params = "action=loginOut")
	@ResponseBody
	public void loginOut(HttpServletRequest request, HttpServletResponse response, User user) {
		try {
			HttpSession session = request.getSession();
			String sendUrl = "../html/base/login.html";
			if (user != null) {
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.cm.WlCmUserAction.loginOut"), Lang.getString("wl.cm.WlCmUserAction.loginOutSuccess"));
				if ("corp".equals(user.getType())) {
					sendUrl = "../html/base/login.html";
				}

			}

			session.removeAttribute("USER");
			response.sendRedirect(sendUrl);

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 经销商用户退出系统，清除用户session
	 * @param request 请求
	 * @param response 响应
	 * @param user 当前登录用户
	 */
	@RequestMapping(params = "action=agencyLoginOut")
	@ResponseBody
	public void agencyLoginOut(HttpServletRequest request, HttpServletResponse response, User user) {
		try {
			HttpSession session = request.getSession();
			String sendUrl = "../html/agency/login.html";
			if (user != null) {
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.cm.WlCmUserAction.loginOut"), Lang.getString("wl.cm.WlCmUserAction.loginOutSuccess"));
				if ("corp".equals(user.getType())) {
					sendUrl = "../html/agency/login.html";
				}

			}

			session.removeAttribute("USER");
			response.sendRedirect(sendUrl);

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 验证用户是否登录
	 * @param request 请求
	 * @param user 用户session信息
	 * @return 用户已登录则返回用户信息
	 */
	@RequestMapping(params = "action=checkLogin")
	@ResponseBody
	public String checkLogin(HttpServletRequest request, User user) {
		try {
			if (user != null) {
				return this.getJson(true, user);
			}
			else {
				return this.getJson(false, "noSecurity");
			}

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 验证经销商用户是否登录
	 * @param request 请求
	 * @param user 用户session信息
	 * @return 用户已登录则返回用户信息
	 */
	@RequestMapping(params = "action=checkAgencyLogin")
	@ResponseBody
	public String checkAgencyLogin(HttpServletRequest request, User user) {
		try {
			if (user != null) {
				return this.getJson(true, user);
			}
			else {
				return this.getJson(false, "agencyNoSecurity");
			}

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取IP
	 * @param request 请求对象
	 * @return ip
	 */
	public String getIp(HttpServletRequest request) {
		try {
			String ip = request.getHeader(" x-forwarded-for ");
			if (ip == null || ip.length() == 0 || " unknown ".equalsIgnoreCase(ip)) {
				ip = request.getHeader(" Proxy-Client-IP ");
			}
			if (ip == null || ip.length() == 0 || " unknown ".equalsIgnoreCase(ip)) {
				ip = request.getHeader(" WL-Proxy-Client-IP ");
			}
			if (ip == null || ip.length() == 0 || " unknown ".equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
			}
			return ip;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}
