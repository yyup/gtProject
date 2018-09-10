package com.gt.wl.cm.action;

import java.util.HashMap;
import java.util.Map;

import org.joyone.action.BaseAction;
import org.joyone.lang.BaseException;
import org.joyone.spring.Sc;
import org.joyone.sys.Lang;
import org.joyone.util.Md5Util;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gt.wl.cm.model.WlCmUser;
import com.gt.wl.cm.service.WlCmUserService;

/**
 * APP端登录校验Action
 * @author liuyj
 */
@Controller
@RequestMapping("/app/cm/wlCmAppLoginAction.do")
public class WlCmAppLoginAction extends BaseAction {

	private WlCmUserService wlCmUserService = (WlCmUserService) Sc.getBean("wl.cm.WlCmUserService");

	/**
	 * 用户登录校验
	 * @param account 登录名
	 * @param password 密码
	 * @return 结果
	 */
	@RequestMapping(params = "action=login")
	@ResponseBody
	public String login(String account, String password) {
		try {
			password = Md5Util.encrypt(password);
			WlCmUser user = wlCmUserService.findByLoginName(account);

			if (user == null) {
				// 用户不存在
				return this.getJson(false, Lang.getString("wl.cm.WlCmUserAction.noUser"));
			}
			else if (user.getState() != 1) {
				// 用户已删除
				return this.getJson(false, Lang.getString("wl.cm.WlCmUserAction.deleted"));
			}
			else if (password.equals(user.getPassword())) {
				// 用户密码正确
				Map resultMap = new HashMap();
				resultMap.put("userId", user.getId());
				resultMap.put("userName", user.getName());
				return this.getJson(true, resultMap);

			}
			else {
				// 密码错误
				return this.getJson(false, Lang.getString("wl.cm.WlCmUserAction.passwordError"));
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}
