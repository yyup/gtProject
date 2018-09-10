package com.gt.wl.cm.action;

import java.util.List;
import java.util.Map;

import org.joyone.action.BaseAction;
import org.joyone.lang.BaseException;
import org.joyone.spring.Sc;
import org.joyone.sys.Lang;
import org.joyone.sys.User;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gt.wl.cm.model.WlCmProductTypeEn;
import com.gt.wl.cm.service.WlCmProductTypeEnService;
import com.gt.wl.cm.service.WlCmSecurityService;
import com.gt.wl.cm.service.WlCmTypeService;

@Controller
@RequestMapping({"/wl/cm/wlCmProductTypeEnAction.do","/wl/cm/wlCmProductTypeEnAction.dox"})
public class WlCmProductTypeEnAction extends BaseAction {
	private WlCmProductTypeEnService wlCmProductTypeService = (WlCmProductTypeEnService) Sc.getBean("wl.cm.WlCmProductTypeEnService");
	private final static String SYSTEM_NAME = Lang.getString("wl.cm.SYSTEM_NAME");
	private final static String MODULE_NAME = Lang.getString("wl.cm.WlCmProductTypeEnAction.MODULE_NAME");
	private final static String MODULE_CODE = "0307003";// 模块代码
	WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");
	WlCmTypeService wlCmTypeService = (WlCmTypeService) Sc.getBean("wl.cm.WlCmTypeService");

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
			mapResult.putAll(wlCmTypeService.getEnumType(enumTypes));// 到字典表获取枚举数据
			// 到商品种类的表获取枚举数据

			return this.getJson(true, mapResult);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取所有的商品类型树
	 * @param user 当前用户
	 * @return 商品类型树形json
	 */
	@RequestMapping(params = "action=getTree")
	@ResponseBody
	public String getTree(User user) {
		try {
			Map map = wlCmProductTypeService.getTree("0");

			return this.getJson(true, map);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取除了当前节点及子节点的树
	 * @param typeId 产品类型id
	 * @param user 当前用户
	 * @return 类型的树形json
	 */
	@RequestMapping(params = "action=getParentTree")
	@ResponseBody
	public String getParentTree(String typeId, User user) {
		try {

			Map map = wlCmProductTypeService.getParentTree(typeId, "0");

			return this.getJson(true, map);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 保存商品类型
	 * @param wlCmProductType 商品类型
	 * @param user 用户
	 * @return 结果
	 */
	@RequestMapping(params = "action=saveData")
	@ResponseBody
	public String saveData(@ModelAttribute("saveModel") WlCmProductTypeEn wlCmProductType, User user) {

		try {
			// 判断父ID是否为空，如为空则挂到根类型
			if (ValidateUtil.isEmpty(wlCmProductType.getParentTypeId())) {
				wlCmProductType.setParentTypeId("0");
			}
			if (wlCmProductType.getSequ() < 1) {
				wlCmProductType.setSequ(1);
			}
			if (ValidateUtil.isEmpty(wlCmProductType.getTypeId())) {
				// 新增日志
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.add"), wlCmProductType.getTypeName());
			}
			else {
				// 编辑日志
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.edit"), wlCmProductType.getTypeName());
			}
			// 保存商品类型
			wlCmProductTypeService.saveProductType(wlCmProductType);
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 删除选中数据
	 * @param ids 选中id数组
	 * @param user 用户
	 * @return 操作结果
	 */
	@RequestMapping(params = "action=deleteData")
	@ResponseBody
	public String deleteData(String ids[], User user) {
		String typeNames = "";
		try {
			for (int i = 0; i < ids.length; i++) {
				// 获取类型的名称
				WlCmProductTypeEn type = (WlCmProductTypeEn) wlCmProductTypeService.getObject(ids[i]);
				typeNames += type.getTypeName() + ",";
			}
			// 删除类型
			wlCmProductTypeService.deleteProductType(ids);
			// 设置日志
			if (!ValidateUtil.isEmpty(typeNames)) {
				typeNames = typeNames.substring(0, typeNames.length() - 1);
			}
			this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.delete"), typeNames);
			return this.getJson(true, "");

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 根据ID获取对象
	 * @param typeId 商品类型id
	 * @return 对象
	 */
	@RequestMapping(params = "action=getData")
	@ResponseBody
	public String getData(String typeId) {
		try {
			// 根据ID得到商品类型对象
			WlCmProductTypeEn wlCmProductType = (WlCmProductTypeEn) wlCmProductTypeService.getObject(typeId);
			// 获取类型ID对应的父类型名称
			WlCmProductTypeEn parentType = (WlCmProductTypeEn) wlCmProductTypeService.getObject(wlCmProductType.getParentTypeId());
			if (!ValidateUtil.isNull(parentType)) {
				wlCmProductType.setParentTypeName(parentType.getTypeName());
			}
			return this.getJson(true, wlCmProductType);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}
	

	/**
	 * 获取所有末级节点的产品类型
	 * @return 产品类型对象列表
	 */
	@RequestMapping(params = "action=findLastTypeList")
	@ResponseBody
	public String findLastTypeList() {
		try {
			List lastTypeList=wlCmProductTypeService.findAllLastType("1");
			return this.getJson(true, lastTypeList);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}

}