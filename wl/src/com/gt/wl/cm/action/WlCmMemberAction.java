package com.gt.wl.cm.action;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.joyone.action.BaseAction;
import org.joyone.lang.BaseException;
import org.joyone.spring.Sc;
import org.joyone.sys.Lang;
import org.joyone.sys.Page;
import org.joyone.sys.Session;
import org.joyone.sys.User;
import org.joyone.util.DateUtil;
import org.joyone.util.Md5Util;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gt.wl.cm.model.WlCmMember;
import com.gt.wl.cm.service.WlCmMemberLevelService;
import com.gt.wl.cm.service.WlCmMemberService;
import com.gt.wl.cm.service.WlCmSecurityService;
import com.gt.wl.cm.service.WlCmTypeService;
import com.gt.wl.util.MD5Util;
import com.gt.wl.util.SendSms;

/**
 * 注册会员action层
 * @author liuyj
 * 
 */
@Controller
@RequestMapping({ "/wl/cm/wlCmMemberAction.do", "/wl/cm/wlCmMemberAction.web", "/wl/cm/wlCmMemberAction.dox" })
public class WlCmMemberAction extends BaseAction {
	private final static String MODULE_CODE = "0301005";// 模块代码
	private final static String SYSTEM_NAME = Lang.getString("wl.cm.SYSTEM_NAME");
	private final static String MODULE_NAME = Lang.getString("wl.cm.WlCmMemberAction.MODULE_NAME");
	private WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");
	private WlCmMemberService wlCmMemberService = (WlCmMemberService) Sc.getBean("wl.cm.WlCmMemberService");// 会员
	private WlCmMemberLevelService wlCmMemberLevelService = (WlCmMemberLevelService) Sc.getBean("wl.cm.WlCmMemberLevelService");// 会员等级
	private WlCmTypeService wlCmTypeService = (WlCmTypeService) Sc.getBean("wl.cm.WlCmTypeService");// 数据字典

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
			// 获取会员等级
			mapResult.put("levelEnum", wlCmMemberLevelService.getObjects());
			return this.getJson(true, mapResult);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询注册会员分页数据
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param levelId 等级ID
	 * @param name 用户名
	 * @param memberStateEk 会员状态
	 * @param isBuyFlag 是否有购买商品
	 * @param email 邮箱
	 * @param account 用户账号
	 * @return 分页结果
	 */

	@RequestMapping(params = "action=findDataList")
	@ResponseBody
	public String findDataList(int pageSize, int currPage, String levelId, String name, String memberStateEk, String isBuyFlag, String email, String account) {
		try {
			Page page = wlCmMemberService.search(currPage, pageSize, levelId, name, memberStateEk, isBuyFlag, email, account);
			return this.getJson(true, page);
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
		String memberNames = "";
		try {
			// 循环得到删除会员名称
			for (int i = 0; i < ids.length; i++) {
				WlCmMember member = (WlCmMember) wlCmMemberService.getObject(ids[i]);
				memberNames += member.getName() + ",";
			}
			// 去掉最尾部逗号
			if (!ValidateUtil.isEmpty(memberNames)) {
				memberNames = memberNames.substring(0, memberNames.length() - 1);
			}
			// 删除
			wlCmMemberService.deleteObject(ids);
			// 删除日志
			this.addLog(Session.getCurrUser(), SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.delete"), memberNames);
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 保存数据
	 * @param wlCmMember 会员对象
	 * @param language 语言类型
	 * @return 结果
	 */
	@RequestMapping(params = "action=saveData")
	@ResponseBody
	public String saveData(WlCmMember wlCmMember,String language) {
		try {
			User user = Session.getCurrUser();
			if (ValidateUtil.isEmpty(wlCmMember.getMemberId())) {// 新增
				// 如果账号为空
				if (ValidateUtil.isEmpty(wlCmMember.getAccount())) {
					throw new BaseException(Lang.getString("wl.cm.WlCmMemberAction.accountIsEmpty"));
				}
				// 如果密码为空
				if (ValidateUtil.isEmpty(wlCmMember.getPassword())) {
					throw new BaseException(Lang.getString("wl.cm.WlCmMemberAction.passwordIsEmpty"));
				}
				// 检查帐号是否重复
				WlCmMember member = wlCmMemberService.getMemberByAccountOrEmail(wlCmMember.getAccount(), null,"","");
				if (!ValidateUtil.isNull(member)) {
					if("english".equals(language)){
						throw new BaseException(Lang.getString("wl.cm.WlCmMemberAction.mobileExistEn"));						
					}else{
						throw new BaseException(Lang.getString("wl.cm.WlCmMemberAction.accountDuplication"));
					}
					
				}
				wlCmMember.setMobile(wlCmMember.getAccount());
				wlCmMember.setRegTime(new Date());
				String pass = wlCmMember.getPassword();
				wlCmMember.setPassword(Md5Util.encrypt(pass));
				wlCmMember.setMemberStateEk("NORMAL");// 正常状态

			}
			else {// 更新
				if (!ValidateUtil.isNull(user)) {// 将session中的user的名字更新
					user.setName(wlCmMember.getName());
				}
				WlCmMember oldMember = null;
				oldMember = (WlCmMember) wlCmMemberService.getObject(wlCmMember.getMemberId());
				wlCmMember.setAccount(oldMember.getAccount());
				// 如果用户密码有变动，则进行密码更新操作
				if (!oldMember.getPassword().equals(wlCmMember.getPassword())) {
					wlCmMember.setPassword(Md5Util.encrypt(wlCmMember.getPassword()));

				}
			}
			if (!ValidateUtil.isNull(user)) {
				if (ValidateUtil.isEmpty(wlCmMember.getMemberId())) {
					this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.add"), "[" + wlCmMember.getName() + "]");
				}
				else {
					this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.edit"), "[" + wlCmMember.getName() + "]");
				}
			}
			wlCmMemberService.saveObject(wlCmMember);

			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取详细数据
	 * @param memberId 会员ID
	 * @return 结果
	 */
	@RequestMapping(params = "action=getData")
	@ResponseBody
	public String getData(String memberId) {
		try {
			WlCmMember wlCmMember = (WlCmMember) wlCmMemberService.getObject(memberId);
			return this.getJson(true, wlCmMember);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取表格详细数据
	 * @return 结果
	 */
	@RequestMapping(params = "action=getTableData")
	@ResponseBody
	public String getTableData() {
		try {

			User user = Session.getCurrUser();
			String memberId = user.getId();
			WlCmMember wlCmMember = (WlCmMember) wlCmMemberService.getObject(memberId);
			return this.getJson(true, wlCmMember);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 修改会员的密码为初始值
	 * @param memberId 会员ID
	 * @param user 当前用户
	 * @return 操作结果
	 */
	@RequestMapping(params = "action=updateMemberPwd")
	@ResponseBody
	public String updateMemberPwd(String memberId, User user) {
		try {
			WlCmMember wlCmMember = (WlCmMember) wlCmMemberService.getObject(memberId);
			// 重置密码，用Md5加密
			wlCmMember.setPassword(Md5Util.encrypt("123456"));
			wlCmMemberService.updateObject(wlCmMember);
			this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.cm.WlCmMemberAction.reset"), Lang.getString("wl.cm.WlCmMemberAction.NAME") + wlCmMember.getName());
			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 修改会员的密码
	 * @param oldPwd 会员旧密码
	 * @param newPwd 会员新密码
	 * @param user 当前用户
	 * @param language 语言类型
	 * @return 操作结果
	 */
	@RequestMapping(params = "action=updatePwd")
	@ResponseBody
	public String updatePwd(String oldPwd, String newPwd, User user,String language) {
		try {
			WlCmMember wlCmMember = (WlCmMember) wlCmMemberService.getObject(user.getId());

			String enOldPwd = Md5Util.encrypt(oldPwd);
			
			if (!wlCmMember.getPassword().equals(enOldPwd)) {
				if("english".equals(language)){
					return this.getJson(false, Lang.getString("wl.cm.WlCmMemberAction.pwdWrongEn"));
				}else{
					return this.getJson(false, Lang.getString("wl.cm.WlCmMemberAction.pwdWrong"));
				}
			}

			// 修改密码，用Md5加密
			wlCmMember.setPassword(Md5Util.encrypt(newPwd));
			wlCmMemberService.updateObject(wlCmMember);
			this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.cm.WlCmMemberAction.pwdUpdate"), Lang.getString("wl.cm.WlCmMemberAction.NAME") + wlCmMember.getName());
			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 修改会员的使用状态
	 * @param memberId 会员ID
	 * @param user 当前用户
	 * @return 操作结果
	 */
	@RequestMapping(params = "action=updateMemberState")
	@ResponseBody
	public String updateMemberState(String memberId, User user) {
		try {
			WlCmMember wlCmMember = (WlCmMember) wlCmMemberService.getObject(memberId);
			// 修改用户使用状态（LOCK-锁定,NORMAL-正常)
			if ("LOCK".equals(wlCmMember.getMemberStateEk())) {
				wlCmMember.setMemberStateEk("NORMAL");
				wlCmMemberService.updateObject(wlCmMember);
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.set"), Lang.getString("wl.cm.WlCmMemberAction.NAME") + wlCmMember.getName() + "," + Lang.getString("wl.cm.WlCmMemberAction.USE"));
			}
			else if ("NORMAL".equals(wlCmMember.getMemberStateEk())) {
				wlCmMember.setMemberStateEk("LOCK");
				wlCmMemberService.updateObject(wlCmMember);
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.set"), Lang.getString("wl.cm.WlCmMemberAction.NAME") + wlCmMember.getName() + "," + Lang.getString("wl.cm.WlCmMemberAction.LOCK"));
			}

			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 会员用户登录用户登录(非手机号)
	 * @param username 登录名
	 * @param pwd 密码
	 * @param request 请求
	 * @param language 语言类型
	 * @return 跳转的相应页面名
	 */
	@RequestMapping(params = "action=loginWithOldAccount")
	@ResponseBody
	public String loginWithOldAccount(String username, String pwd, HttpServletRequest request,String language) {
		try {
			/**
			 * 获取用户的客户端信息
			 */
			WlCmMember oldUser = wlCmMemberService.getMemberByAccountOrEmail("", null,username,"");
			WlCmMember user = wlCmMemberService.getMemberByAccountOrEmail(username, null,"","");
			if (oldUser != null&&user == null) {
				// 用户不存在
				if("english".equals(language)){
					return this.getJson(false, Lang.getString("tp.cm.WlCmMemberAction.oldUserEn"));
				}else{
					return this.getJson(false, Lang.getString("tp.cm.WlCmMemberAction.oldUser"));
				}
				
			}
			if (user == null) {
				// 用户不存在
				if("english".equals(language)){
					return this.getJson(false, Lang.getString("wl.cm.WlCmMemberAction.noUserEn"));
				}else{
					return this.getJson(false, Lang.getString("tp.cm.WlCmMemberAction.noUser"));
				}
			}
			else if ("LOCK".equals(user.getMemberStateEk())) {
				return this.getJson(false, Lang.getString("tp.cm.WlCmMemberAction.deleted"));
			}
			else if (pwd.equals(user.getPassword())) {
				if("english".equals(language)){
					return this.getJson(true, Lang.getString("tp.cm.WlCmMemberAction.toBindPhoneEn"));
				}else{
					return this.getJson(true, Lang.getString("tp.cm.WlCmMemberAction.toBindPhone"));
				}
				
			}
			else {
				if("english".equals(language)){
					return this.getJson(false, Lang.getString("wl.cm.WlCmMemberAction.passwordErrorEn"));
				}else{
					return this.getJson(false, Lang.getString("tp.cm.WlCmMemberAction.passwordError"));
				}
				
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
	
	/**
	 * 会员用户登录用户登录(手机号)
	 * @param username 登录名
	 * @param pwd 密码
	 * @param request 请求
	 * @param language 语言类型
	 * @return 跳转的相应页面名
	 */
	@RequestMapping(params = "action=login")
	@ResponseBody
	public String login(String username, String pwd, HttpServletRequest request,String language) {
		try {
			/**
			 * 获取用户的客户端信息
			 */
			WlCmMember user = wlCmMemberService.getMemberByAccountOrEmail(username, null,"","");

	
			if (user == null) {
				// 用户不存在
				if("english".equals(language)){
					return this.getJson(false, Lang.getString("wl.cm.WlCmMemberAction.noUserEn"));
				}else{
					return this.getJson(false, Lang.getString("tp.cm.WlCmMemberAction.noUser"));
				}
			}
			else if ("LOCK".equals(user.getMemberStateEk())) {
				return this.getJson(false, Lang.getString("tp.cm.WlCmMemberAction.deleted"));
			}
			else if (pwd.equals(user.getPassword())) {
				user.setLastLoginTime(new Date());
				user.setIp(this.getIp(request));
				wlCmMemberService.saveObject(user);
				// 用户信息存入session
				User sessionUser = new User();
				// TpCmDepartmentService departmentService = (TpCmDepartmentService) Sc.getBean("tp.cm.TpCmDepartmentService");
				sessionUser.setIp(this.getIp(request));
				// sessionUser.setAdmin(user.getIsAdmin() == 1);
				// sessionUser.setProgrammer(user.getIsProgrammer() == 1);
				// sessionUser.setDepartmentId(user.getDepartmentId());
				sessionUser.setName(user.getName());
				sessionUser.setLoginName(user.getAccount());
				sessionUser.setId(user.getMemberId());
				sessionUser.setType("member");// 设置当前登录为会员用户
				// sessionUser.setDepartmentName(departmentService.getName(user.getDepartmentId()));// 设置所在部门名称
				sessionUser.setLoginTime(DateUtil.dateFormatFromDateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
				HttpSession session = request.getSession(true);
				session.removeAttribute("USER");
				session.setAttribute("USER", sessionUser);
				this.addLog(sessionUser, SYSTEM_NAME, MODULE_NAME, Lang.getString("tp.cm.WlCmMemberAction.login"), Lang.getString("tp.cm.WlCmMemberAction.success"));

				return this.getJson(true, sessionUser);

			}
			else {
				if("english".equals(language)){
					return this.getJson(false, Lang.getString("wl.cm.WlCmMemberAction.passwordErrorEn"));
				}else{
					return this.getJson(false, Lang.getString("tp.cm.WlCmMemberAction.passwordError"));
				}
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
	public void loginOut(HttpServletRequest request, HttpServletResponse response, User user,String language) {
		try {
			HttpSession session = request.getSession();
			String sendUrl = "";
			if("english".equals(language)){
				sendUrl = "../../html/engsite/login.html";
			}else{
				sendUrl = "../../html/newsite/login.html";
			}
			if (user != null) {
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.cm.WlCmMemberAction.loginOut"), Lang.getString("wl.cm.WlCmMemberAction.loginOutSuccess"));
				if ("corp".equals(user.getType())) {//是之前旧项目有一个类用户是企业,相当于两类用户，跳转不一样的界面，调用同一个接口
					sendUrl = "../../html/newsite/login.html";
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
	 * 获取用户是否登录
	 * @param user 登录用户
	 * @return 用户名
	 */
	@RequestMapping(params = "action=getIsLogin")
	@ResponseBody
	public String getIsLogin(User user) {
		try {
			if (ValidateUtil.isNull(user)) {
				return this.getJson(true, "");
			}
			else {
				WlCmMember wlCmMember=(WlCmMember) wlCmMemberService.getObject(user.getId());
				if(ValidateUtil.isNull(wlCmMember)){
					return this.getJson(true, user.getName());
				}else{
					if(ValidateUtil.isEmpty(wlCmMember.getAccount())){
						return this.getJson(true, wlCmMember.getName());
					}else{
						return this.getJson(true, wlCmMember.getAccount());
					}				
				}
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

	/**
	 * 更新新密码发送到指定邮箱
	 * @param account 账号
	 * @param email 邮箱
	 * @return 空
	 */
	@RequestMapping(params = "action=updatePasswordSendToEmail")
	@ResponseBody
	public String updatePasswordSendToEmail(String account, String email) {
		try {
			WlCmMember wlCmMember = null;
			wlCmMember = wlCmMemberService.getMemberByAccountOrEmail(account, null,"","");
			if (ValidateUtil.isNull(wlCmMember)) {// 账号不存在
				return this.getJson(true, Lang.getString("wl.cm.WlCmMemberAction.noAccount"));
			}
			else {
				wlCmMember = wlCmMemberService.getMemberByAccountOrEmail(account, email,"","");
				if (ValidateUtil.isNull(wlCmMember)) {// 邮箱跟账号不匹配
					return this.getJson(true, Lang.getString("wl.cm.WlCmMemberAction.noEmail"));
				}
				else {// 邮箱和账号匹配,发送密码到邮箱
					wlCmMemberService.updatePasswordSendToEmail(wlCmMember);
					return this.getJson(true, "");
				}

			}

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
	
	/**
	 * 获取短信验证码（绑定手机和注册会员使用）
	 * @param mobile 手机号码
	 * @param language 语言
	 * @return 验证码
	 */
	@RequestMapping(params = "action=getMsgCode")
	@ResponseBody
	public String getMsgCode(String mobile,String language) {
		try {
			WlCmMember wlCmMember = wlCmMemberService.getMemberByAccountOrEmail(mobile, "","","");
			if (ValidateUtil.isNull(wlCmMember)) {
				int msgCode = (int) ((Math.random() * 9 + 1) * 100000);
				String content = "";
				if("english".equals(language)){
					content = Lang.getString("wl.cm.WlCmMemberAction.yourCodeEn") + msgCode + Lang.getString("wl.cm.WlCmMemberAction.noTellOtherEn");
				}else{
					content = Lang.getString("wl.cm.WlCmMemberAction.yourCode") + msgCode + Lang.getString("wl.cm.WlCmMemberAction.noTellOther");
				}
				
				boolean mobileState = SendSms.sendMsg(mobile, msgCode, content);
				if (mobileState) {
					return this.getJson(true, msgCode + "");
				}
				else {
					if("english".equals(language)){
						return this.getJson(false, Lang.getString("wl.cm.WlCmMemberAction.noGetMsgCodeEn"));
					}else{
						return this.getJson(false, Lang.getString("wl.cm.WlCmMemberAction.noGetMsgCode"));
					}
				}
			}
			else {
				if("english".equals(language)){
					return this.getJson(false, Lang.getString("wl.cm.WlCmMemberAction.mobileExistEn"));
				}else{
					return this.getJson(false, Lang.getString("wl.cm.WlCmMemberAction.mobileExist"));
				}
				
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
	
	/**
	 * 获取忘记密码短信验证码（忘记密码使用）
	 * @param request 请求
	 * @param mobile 手机号码
	 * @param language 语言
	 * @return 验证码
	 */
	@RequestMapping(params = "action=getForgetPasswordMsgCode")
	@ResponseBody
	public String getForgetPasswordMsgCode(HttpServletRequest request,String mobile,String language) {
		try {
			WlCmMember wlCmMember = wlCmMemberService.getMemberByAccountOrEmail(mobile, "","","");// 获取手机号为mobile的会员
			if (!ValidateUtil.isNull(wlCmMember)) {
				int msgCode = (int) ((Math.random() * 9 + 1) * 100000);
				String content = "";
				if("english".equals(language)){
					content = Lang.getString("wl.cm.WlCmMemberAction.yourCodeEn") + msgCode + Lang.getString("wl.cm.WlCmMemberAction.noTellOtherEn");
				}else{
					content = Lang.getString("wl.cm.WlCmMemberAction.yourCode") + msgCode + Lang.getString("wl.cm.WlCmMemberAction.noTellOther");
				}
				boolean mobileState = SendSms.sendMsg(mobile, msgCode, content);				
				if (mobileState) {
					Map map=new HashMap();
					map.put("mobile", mobile);
					map.put("msgCode", String.valueOf(msgCode));
					HttpSession session = request.getSession(true);
					session.removeAttribute("FORGET");
					session.setAttribute("FORGET", map);
					return this.getJson(true, msgCode + "");
				}
				else {
					if("english".equals(language)){
						return this.getJson(false, Lang.getString("wl.cm.WlCmMemberAction.noGetMsgCodeEn"));
					}else{
						return this.getJson(false, Lang.getString("wl.cm.WlCmMemberAction.noGetMsgCode"));
					}
				}
			
			}
			else {
				if("english".equals(language)){
					return this.getJson(false, Lang.getString("wl.cm.WlCmMemberAction.mobileNonExistEn"));
				}else{
					return this.getJson(false, Lang.getString("wl.cm.WlCmMemberAction.mobileNonExist"));
				}
			}
			
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 修改密码(忘记密码使用)
	 * @param mobile 手机号码
	 * @param password 密码
	 * @return 空
	 */
	@RequestMapping(params = "action=updatePassword")
	@ResponseBody
	public String updatePassword(HttpServletRequest request,String mobile, String password,String code) {
		try {
			Map sessionMap=new HashMap();
	
			HttpSession session = request.getSession(true);
			sessionMap=(Map) session.getAttribute("FORGET");
			String sessionMobile=(String) sessionMap.get("mobile");
			String sessionCode=(String) sessionMap.get("msgCode");
			if(mobile.equals(sessionMobile)&&code.equals(sessionCode)){
				WlCmMember wlCmMember = wlCmMemberService.getMemberByAccountOrEmail(mobile, "","","");
				wlCmMember.setPassword(password);
				wlCmMemberService.saveObject(wlCmMember);// 保存会员
				return this.getJson(true, "");
			}else{
				throw new BaseException(Lang.getString("wl.cm.WlCmLoginAction.noUser"));
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
	
	/**
	 * 绑定手机(绑定手机使用)
	 * @param account 账号 
	 * @param mobile 手机
	 * @param qqAccount qq账号
	 * @param password 密码
	 * @return 空
	 */
	@RequestMapping(params = "action=bindMobile")
	@ResponseBody
	public String bindMobile(String account,String mobile,String qqAccount,String password) {
		try {
			WlCmMember wlCmMember=null;
			if(!ValidateUtil.isEmpty(account)){
				wlCmMember = wlCmMemberService.getMemberByAccountOrEmail(account, "","","");
			}else{
				wlCmMember = wlCmMemberService.getMemberByAccountOrEmail("", "","",qqAccount);
				wlCmMember.setPassword(Md5Util.encrypt(password));
			}		
			wlCmMember.setMobile(mobile);
			wlCmMember.setAccount(mobile);
			wlCmMemberService.saveObject(wlCmMember);// 保存会员
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
	/**
	 * QQ登录
	 * @param name 名称
	 * @param openId uid
	 * @param request 请求
	 * @return 空
	 */
	@RequestMapping(params = "action=qqLogin")
	@ResponseBody
	public String qqLogin(String name,String openId,HttpServletRequest request) {
		try {
			WlCmMember user=wlCmMemberService.getMemberByAccountOrEmail("", "", "", openId);
			if(ValidateUtil.isNull(user)){
				user =new WlCmMember();
				user.setName(name);
				user.setRegTime(new Date());
				user.setQqAccount(openId);
				//wlCmMemberService.saveObject(user);
			}
			user.setLastLoginTime(new Date());
			user.setIp(this.getIp(request));
			wlCmMemberService.saveObject(user);
			if(!ValidateUtil.isEmpty(user.getAccount())){
				// 用户信息存入session
				User sessionUser = new User();
				sessionUser.setIp(this.getIp(request));
				sessionUser.setName(user.getName());
				sessionUser.setLoginName(user.getAccount());
				sessionUser.setId(user.getMemberId());
				sessionUser.setType("member");// 设置当前登录为会员用户
				sessionUser.setLoginTime(DateUtil.dateFormatFromDateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
				HttpSession session = request.getSession(true);
				session.removeAttribute("USER");
				session.setAttribute("USER", sessionUser);
				this.addLog(sessionUser, SYSTEM_NAME, MODULE_NAME, Lang.getString("tp.cm.WlCmMemberAction.login"), Lang.getString("tp.cm.WlCmMemberAction.success"));	
			}
			return this.getJson(true, user);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
	

}