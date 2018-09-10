package com.gt.wl.cm.action;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.joyone.action.BaseAction;
import org.joyone.lang.BaseException;
import org.joyone.spring.Sc;
import org.joyone.sys.Lang;
import org.joyone.sys.User;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gt.wl.cm.model.WlCmCategory;
import com.gt.wl.cm.service.WlCmCategoryService;
import com.gt.wl.cm.service.WlCmSecurityService;
import com.gt.wl.cm.service.WlCmTypeService;

@Controller
@RequestMapping("/wl/cm/wlCmCategoryAction.do")
public class WlCmCategoryAction extends BaseAction {
	private WlCmCategoryService wlCmCategoryService = (WlCmCategoryService) Sc.getBean("wl.cm.WlCmCategoryService");
	private final static String SYSTEM_NAME = Lang.getString("wl.cm.SYSTEM_NAME");
	private final static String MODULE_NAME = Lang.getString("wl.cm.wlCmCategoryAction.MODULE_NAME");
	private WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");
	private final static String MODULE_CODE = "0304003";// 模块代码

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
	 * 获取所有的栏目类型树
	 * @param user 当前用户
	 * @param isEnableFlag 栏目的使用状态
	 * @param rootId 跟节点
	 * @param isLastFlag 是否末级节点
	 * @return 商品类型树形json
	 */
	@RequestMapping(params = "action=getTree")
	@ResponseBody
	public String getTree(User user, String isEnableFlag, String rootId, String isLastFlag) {
		try {

			Map map = wlCmCategoryService.getTree(rootId, isEnableFlag, isLastFlag, "","");

			return this.getJson(true, map);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取除了当前节点及子节点的树
	 * @param categoryId 物料类别id
	 * @param isEnableFlag 栏目的使用状态
	 * @param user 当前用户
	 * @param isLastFlag 是否末节点
	 * @return 类型的树形json
	 */
	@RequestMapping(params = "action=getParentTree")
	@ResponseBody
	public String getParentTree(String categoryId, String isEnableFlag, User user, String isLastFlag) {
		try {

			Map map = wlCmCategoryService.getParentTree(categoryId, isEnableFlag, "0", isLastFlag, "");

			return this.getJson(true, map);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 保存栏目
	 * @param wlCmCategory 物料类别对象
	 * @param user 用户
	 * @return 结果
	 */
	@RequestMapping(params = "action=saveData")
	@ResponseBody
	public String saveData(WlCmCategory wlCmCategory, User user) {

		try {
			// 判断父ID是否为空，如为空则挂到根类型
			if (ValidateUtil.isEmpty(wlCmCategory.getParentCategoryId())) {
				wlCmCategory.setParentCategoryId("0");

			}
			if (wlCmCategory.getSequ() == 0) {// 如果序号没填，默认为1
				wlCmCategory.setSequ(1);
			}
			/*
			 * if (!"0".equals(wlCmCategory.getParentCategoryId())) {// 第二级节点开始都默认显示给经销商
			 * wlCmCategory.setIsShowAgency("1");
			 * }
			 */

			if (ValidateUtil.isEmpty(wlCmCategory.getCategoryId())) {
				// 新增日志
				wlCmCategory.setIsEnableFlag("1");
				wlCmCategory.setCreateTime(new Date());
				wlCmCategory.setCreator(user.getName());

				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.add"), wlCmCategory.getName());
			}
			else {
				// 编辑日志
				wlCmCategory.setModifier(user.getName());
				wlCmCategory.setModifyTime(new Date());
				if ("1".equals(wlCmCategory.getIsLastFlag())) {
					String parentIds[] = new String[1];
					parentIds[0] = wlCmCategory.getCategoryId();
					List list = wlCmCategoryService.findChildList(parentIds, "1", "1");
					if (!ValidateUtil.isEmpty(list)) {
						return this.getJson(true, Lang.getString("wl.cm.wlCmItemAction.existChildNode"));
					}
				}

				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.edit"), wlCmCategory.getName());
			}
			// 保存栏目
			wlCmCategoryService.saveObject(wlCmCategory);
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 修改物料类别对象的状态
	 * @param categoryId 物料类别id
	 * @param user 当前登录用户
	 * @return 空
	 */
	@RequestMapping(params = "action=updateOrgState")
	@ResponseBody
	public String updateOrgState(String categoryId, User user) {
		try {
			// 获取仓库信息对象
			WlCmCategory wlCmCategory = (WlCmCategory) wlCmCategoryService.getObject(categoryId);
			// 修改使用状态（0-禁用,1-启用)
			if ("0".equals(wlCmCategory.getIsEnableFlag())) {
				wlCmCategory.setIsEnableFlag("1");
				wlCmCategoryService.updateObject(wlCmCategory);
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.set"), wlCmCategory.getCategoryCd() + Lang.getString("wl.cm.wlCmCategoryAction.USE"));
			}
			else if ("1".equals(wlCmCategory.getIsEnableFlag())) {
				wlCmCategory.setIsEnableFlag("0");
				wlCmCategoryService.updateObject(wlCmCategory);
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.set"), wlCmCategory.getCategoryCd() + Lang.getString("wl.cm.wlCmCategoryAction.LOCK"));
			}

			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据物料类别idid获取物料类别对象
	 * @param categoryId 单位id
	 * @return 对象
	 */
	@RequestMapping(params = "action=getData")
	@ResponseBody
	public String getData(String categoryId) {
		try {
			// 获取仓库信息对象
			WlCmCategory wlCmCategory = (WlCmCategory) wlCmCategoryService.getObject(categoryId);
			// 获取类型ID对应的父类型名称
			WlCmCategory parentWlCmCategory = (WlCmCategory) wlCmCategoryService.getObject(wlCmCategory.getParentCategoryId());
			if (!ValidateUtil.isNull(parentWlCmCategory)) {
				wlCmCategory.setParentName(parentWlCmCategory.getName());
			}
			return this.getJson(true, wlCmCategory);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}