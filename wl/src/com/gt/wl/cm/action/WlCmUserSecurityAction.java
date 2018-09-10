package com.gt.wl.cm.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joyone.action.BaseAction;
import org.joyone.lang.BaseException;
import org.joyone.model.EnumModel;
import org.joyone.spring.Sc;
import org.joyone.sys.Lang;
import org.joyone.sys.User;
import org.joyone.util.TreeUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gt.wl.cm.model.WlCmDepartment;
import com.gt.wl.cm.model.WlCmDutyrole;
import com.gt.wl.cm.model.WlCmSecurity;
import com.gt.wl.cm.model.WlCmUser;
import com.gt.wl.cm.model.WlCmUserSecurity;
import com.gt.wl.cm.service.WlCmDepartmentService;
import com.gt.wl.cm.service.WlCmDutyroleService;
import com.gt.wl.cm.service.WlCmSecurityService;
import com.gt.wl.cm.service.WlCmTypeService;
import com.gt.wl.cm.service.WlCmUserSecurityService;
import com.gt.wl.cm.service.WlCmUserService;

@Controller
@RequestMapping("/html/sys/wlCmUserSecurityAction.do")
public class WlCmUserSecurityAction extends BaseAction {
	private WlCmUserSecurityService wlCmUserSecurityService = (WlCmUserSecurityService) Sc.getBean("wl.cm.WlCmUserSecurityService");
	private WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");
	private WlCmTypeService wlCmTypeService = (WlCmTypeService) Sc.getBean("wl.cm.WlCmTypeService");
	private WlCmUserService wlCmUserService = (WlCmUserService) Sc.getBean("wl.cm.WlCmUserService");
	private WlCmDutyroleService wlCmDutyroleService = (WlCmDutyroleService) Sc.getBean("wl.cm.WlCmDutyroleService");
	private WlCmDepartmentService departmentService = (WlCmDepartmentService) Sc.getBean("wl.cm.WlCmDepartmentService");
	private final static String SYSTEM_NAME = Lang.getString("wl.cm.SYSTEM_NAME");
	private final static String MODULE_NAME = Lang.getString("wl.cm.WlCmUserSecurityAction.MODULE_NAME");
	private final static String MODULE_CODE = "0303003";// 模块代码

	/**
	 * 界面初始化
	 * @param enumTypes 枚举类型，多个枚举项以“,”隔开，如“SEX,PARTY”
	 * @param user 当前登录用户
	 * @return 权限与枚举值
	 */
	@RequestMapping(params = "action=init")
	@ResponseBody
	public String init(String[] enumTypes, User user) {
		try {

			Map mapResult = wlCmSecurityService.getSmcMap(user, MODULE_CODE);// 根据模块代码获取权限
			mapResult.putAll(wlCmTypeService.getEnumType(enumTypes));// 到字典表获取枚举数据

			// 获取模块树
			List list = wlCmSecurityService.findByStateAndType(1, 1);
			Map securityTree = TreeUtil.getPdTree(list, "0");
			mapResult.put("securityTree", securityTree.get("items"));

			return this.getJson(true, mapResult);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 增加数据
	 * @param securityId 权限ID
	 * @param securityArray 权限数组
	 * @param ownerId 拥有者
	 * @param departmentId 组织机构ID
	 * @param type 类型
	 * @param user 当前用户
	 * @return 结果
	 */
	@RequestMapping(params = "action=addData")
	@ResponseBody
	public String addData(String securityId, String[] securityArray, String ownerId, String departmentId, int type, User user) {
		try {

			if ("1".equals(type) || departmentId == null) {
				departmentId = "-1";// 全部
			}
			if (ownerId == null || "".equals(ownerId)) {
				ownerId = "-1";// 全部
			}
			String security = "";
			String nut = "";
			for (int i = 0; i < securityArray.length; i++) {
				security += nut + securityArray[i];
				nut = ",";
			}
			WlCmUserSecurity wlCmUserSecurity = new WlCmUserSecurity();

			wlCmUserSecurity.setSecurityId(securityId);
			wlCmUserSecurity.setSecurityArray(security);
			wlCmUserSecurity.setType(type);

			wlCmUserSecurity.setDepartmentId(departmentId);
			wlCmUserSecurity.setOwnerId(ownerId);
			wlCmUserSecurity.setSequ(0);
			wlCmUserSecurityService.addObject(wlCmUserSecurity);

			this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.cm.WlCmUserSecurityAction.add"), "[" + wlCmUserSecurity.getSecurityId() + "]");
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 删除数据
	 * @param ids id
	 * @param user 当前登录用户
	 * @return true or false
	 */
	@RequestMapping(params = "action=deleteData")
	@ResponseBody
	public String deleteData(String ids[], User user) throws BaseException {
		try {
			wlCmUserSecurityService.deleteObject(ids);
			this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.cm.WlCmUserSecurityAction.delete"), "[" + ids + "]");
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据模块ID返回用户权限关系数组
	 * @param securityId 权限ID
	 * @return 数组
	 */
	@RequestMapping(params = "action=getList")
	@ResponseBody
	public String getList(String securityId) {
		try {
			List list = wlCmUserSecurityService.findAllBySecurityId(securityId);

			WlCmSecurity tpCmSecurity = (WlCmSecurity) wlCmSecurityService.getObject(securityId);
			List<WlCmSecurity> baseSecurityList = wlCmSecurityService.findChildByParentId(securityId);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("rightGridDataSource", list);
			resultMap.put("securityList", baseSecurityList);
			resultMap.put("security", tpCmSecurity);

			String enumTypes[] = new String[1];
			enumTypes[0] = "securityOwner";

			resultMap.putAll(wlCmTypeService.getEnumType(enumTypes));// 到字典表获取枚举数据

			List<WlCmUser> userList = wlCmUserService.getObjects();
			resultMap.put("userList", wlCmUserService.findAllUser());

			List<WlCmDutyrole> groupList = wlCmDutyroleService.findByCodeAndState("GROUP", 1);
			resultMap.put("groupList", groupList);
			List<WlCmDutyrole> groupAllList = wlCmDutyroleService.getObjects();
			List<EnumModel> ugList = new ArrayList();
			ugList.add(new EnumModel(Lang.getString("wl.cm.WlCmUserSecurityAction.all"), "-1"));
			for (WlCmUser u : userList) {
				ugList.add(new EnumModel(u.getName(), u.getId()));
			}
			for (WlCmDutyrole u : groupAllList) {
				ugList.add(new EnumModel(u.getName(), u.getId()));
			}
			resultMap.put("ownerList", ugList);

			List<WlCmDepartment> depList = departmentService.findByState(1);
			List<EnumModel> departList = new ArrayList();
			departList.add(new EnumModel(Lang.getString("wl.cm.WlCmUserSecurityAction.all"), "-1"));
			for (WlCmDepartment dep : depList) {
				departList.add(new EnumModel(dep.getName(), dep.getId()));
			}
			resultMap.put("departmentList", departList);
			return this.getJson(true, resultMap);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据模块ID返回模块拥有权限列表
	 * @param securityId 权限ID
	 * @return 数组
	 */
	@RequestMapping(params = "action=getSecurityList")
	@ResponseBody
	public String getSecurityList(String securityId) {
		try {
			List list = wlCmUserSecurityService.findAllBySecurityId(securityId);
			return this.getJson(true, list);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}