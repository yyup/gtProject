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

import com.gt.wl.cm.model.WlCmFocusPic;
import com.gt.wl.cm.service.WlCmBizAuditLogService;
import com.gt.wl.cm.service.WlCmFocusPicService;
import com.gt.wl.cm.service.WlCmSecurityService;
import com.gt.wl.cm.service.WlCmTypeService;

/**
 * 焦点图片Action层
 * @author liuyj
 * 
 */
@Controller
@RequestMapping({ "/wl/cm/wlCmFocusPicAction.do", "/wl/cm/wlCmFocusPicAction.dox" })
public class WlCmFocusPicAction extends BaseAction {
	private final static String MODULE_CODE = "0301003";// 模块代码
	private final static String SYSTEM_NAME = Lang.getString("wl.cm.SYSTEM_NAME");
	private final static String MODULE_NAME = Lang.getString("wl.cm.WlCmFocusPicAction.MODULE_NAME");
	private WlCmFocusPicService wlCmFocusPicService = (WlCmFocusPicService) Sc.getBean("wl.cm.WlCmFocusPicService");
	private WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");
	private WlCmTypeService wlCmTypeService = (WlCmTypeService) Sc.getBean("wl.cm.WlCmTypeService");
	private WlCmBizAuditLogService wlCmBizAuditLogService = (WlCmBizAuditLogService) Sc.getBean("wl.cm.WlCmBizAuditLogService");

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
			return this.getJson(true, mapResult);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取焦点图片数据列表(分页)
	 * @param currPage 当前页号
	 * @param pageSize 页大小
	 * @return 数据分页对象
	 */
	@RequestMapping(params = "action=findFocusPicList")
	@ResponseBody
	public String findFocusPicList(int pageSize, int currPage) {
		try {
			// 获取登录用户
			User user = Session.getCurrUser();
			Page page = wlCmFocusPicService.findPage(currPage, pageSize);
			return this.getJson(true, page);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 保存焦点图片
	 * @param wlCmFocusPic 焦点图片
	 * @return 操作结果
	 */
	@RequestMapping(params = "action=saveData")
	@ResponseBody
	public String saveData(WlCmFocusPic wlCmFocusPic) {
		try {
			// 获取登录用户
			User user = Session.getCurrUser();
			// 新增操作
			if (ValidateUtil.isEmpty(wlCmFocusPic.getPicId())) {
				wlCmFocusPic.setIsEnableFlag("1");
				wlCmFocusPic.setCreateTime(new Date());
				wlCmFocusPic.setCreator(user.getName());
				// 新增日志
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.add"), wlCmFocusPic.getPicId());
			}
			else {// 编辑操作
				wlCmFocusPic.setModifyTime(new Date());
				wlCmFocusPic.setModifier(user.getName());
				// 编辑日志
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.edit"), wlCmFocusPic.getPicId());
			}
			wlCmFocusPicService.saveData(wlCmFocusPic, user);
			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据焦点图片ID获取焦点图片对象
	 * @param picId 焦点图片ID
	 * @return 对象
	 */
	@RequestMapping(params = "action=getData")
	@ResponseBody
	public String getData(String picId) {
		try {
			// 获取焦点图片
			WlCmFocusPic wlCmFocusPic = (WlCmFocusPic) wlCmFocusPicService.getObject(picId);
			wlCmFocusPic.setBizLogList(wlCmBizAuditLogService.findBizAuditList(picId));
			return this.getJson(true, wlCmFocusPic);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 批量删除焦点图片
	 * @param ids 焦点图片ID数组
	 * @return 操作结果
	 */
	@RequestMapping(params = "action=deleteData")
	@ResponseBody
	public String deleteData(String ids[]) {
		try {
			wlCmFocusPicService.deleteObject(ids);
			// 删除日志
			this.addLog(Session.getCurrUser(), SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.delete"), ids.toString());
			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 修改焦点图片的状态
	 * @param picId 焦点图片ID
	 * @param user 当前用户
	 * @return 操作结果
	 */
	@RequestMapping(params = "action=updateFocusPicState")
	@ResponseBody
	public String updateFocusPicState(String picId, User user) {
		try {
			// 获取焦点图片
			WlCmFocusPic wlCmFocusPic = (WlCmFocusPic) wlCmFocusPicService.getObject(picId);
			// 修改使用状态（0-禁用,1-启用)
			if ("0".equals(wlCmFocusPic.getIsEnableFlag())) {
				wlCmFocusPic.setIsEnableFlag("1");
				wlCmFocusPicService.updateObject(wlCmFocusPic);
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.set"), wlCmFocusPic.getPicId() + Lang.getString("wl.cm.WlCmFocusPicAction.USE"));
			}
			else if ("1".equals(wlCmFocusPic.getIsEnableFlag())) {
				wlCmFocusPic.setIsEnableFlag("0");
				wlCmFocusPicService.updateObject(wlCmFocusPic);
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.set"), wlCmFocusPic.getPicId() + Lang.getString("wl.cm.WlCmFocusPicAction.LOCK"));
			}

			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 修改焦点图片的状态
	 * @param picId 焦点图片ID
	 * @param isEnableFlag 是否启用
	 * @param user 当前登录用户
	 * @return 操作结果
	 */
	@RequestMapping(params = "action=updateState")
	@ResponseBody
	public String updateState(String[] picIds, String isEnableFlag, User user) {
		try {
			if (!ValidateUtil.isEmpty(picIds)) {
				wlCmFocusPicService.updateState(picIds, isEnableFlag, user);
			}
			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取焦点图片
	 * @param isEnableFlag 是否启用
	 * @return 焦点图片列表
	 */
	@RequestMapping(params = "action=findFocusPic")
	@ResponseBody
	public String findFocusPic(String isEnableFlag) {
		try {
			return this.getJson(true, wlCmFocusPicService.findFocusPic(isEnableFlag, "", ""));
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/***
	 * 终审（通过或驳回）
	 * @param picIds 焦点图片ids
	 * @param auditNodeEk 审核环节
	 * @param auditState 审核状态
	 * @param auditRemark审核意见
	 * @return 空
	 */
	@RequestMapping(params = "action=updateFinalAudit")
	@ResponseBody
	public String updateFinalAudit(String picIds[], String auditNodeEk, String auditState, String auditRemark, User user) {
		try {
			wlCmFocusPicService.updateFinalAudit(picIds, auditNodeEk, auditState, auditRemark, user);
			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/***
	 * 初审（通过或驳回）
	 * @param picIds 焦点图片ids
	 * @param auditNodeEk 审核环节
	 * @param auditState 审核状态
	 * @param auditRemark审核意见
	 * @return 空
	 */
	@RequestMapping(params = "action=updateFirstAudit")
	@ResponseBody
	public String updateFirstAudit(String picIds[], String auditNodeEk, String auditState, String auditRemark, User user) {
		try {
			wlCmFocusPicService.updateFirstAudit(picIds, auditNodeEk, auditState, auditRemark, user);
			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}
}