package com.gt.wl.cm.action;

import java.util.Map;

import org.joyone.action.BaseAction;
import org.joyone.lang.BaseException;
import org.joyone.spring.Sc;
import org.joyone.sys.Page;
import org.joyone.sys.User;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gt.wl.cm.model.WlCmDocumentPath;
import com.gt.wl.cm.service.WlCmDocumentPathService;
import com.gt.wl.cm.service.WlCmSecurityService;

/**
 * 文件路径action层
 * @author huangbj
 * 
 */
@Controller
@RequestMapping("/html/sys/wlCmDocumentPathAction.do")
public class WlCmDocumentPathAction extends BaseAction {
	private WlCmDocumentPathService wlCmDocumentPathService = (WlCmDocumentPathService) Sc.getBean("wl.cm.WlCmDocumentPathService");
	private final static String MODULE_CODE = "0303009";// 模块代码

	/**
	 * 界面初始化
	 * @param enumType 枚举类型
	 * @param user 当前登录用户
	 * @return 权限与枚举值
	 */
	@RequestMapping(params = "action=init")
	@ResponseBody
	public String init(String enumType, User user) {
		try {
			WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");
			Map mapResult = wlCmSecurityService.getSmcMap(user, MODULE_CODE);// 根据模块代码获取权限
			return this.getJson(true, mapResult);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 分页查询所有的文件路径配置
	 * @param pageSize 页记录数
	 * @param currPage 页码
	 * @return 所有的文件路径配置
	 */
	@RequestMapping(params = "action=findAllDocPath")
	@ResponseBody
	public String findAllDocPath(int pageSize, int currPage) {
		try {
			Page page = wlCmDocumentPathService.findAllDocPath(pageSize, currPage);
			return this.getJson(true, page);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据ID获取对象
	 * @param id id
	 * @return 对象
	 */
	@RequestMapping(params = "action=getData")
	@ResponseBody
	public String getData(String id) {
		try {
			WlCmDocumentPath docPath = (WlCmDocumentPath) wlCmDocumentPathService.getObject(id);
			return this.getJson(true, docPath);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 初始化对象属性
	 * @param action 操作名称
	 * @param id ID号
	 * @return 文件路径配置信息
	 */
	@ModelAttribute("saveModel")
	public WlCmDocumentPath initAttribute(String action, String id) {
		try {
			WlCmDocumentPath docPath = null;
			if ("saveData".equals(action)) {
				if (ValidateUtil.isEmpty(id))
					docPath = new WlCmDocumentPath();
				else
					docPath = (WlCmDocumentPath) wlCmDocumentPathService.getObject(id);
			}
			return docPath;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 保存文件路径信息
	 * @param docPath 对象
	 * @return 操作结果结果
	 */
	@RequestMapping(params = "action=saveData")
	@ResponseBody
	public String saveData(@ModelAttribute("saveModel") WlCmDocumentPath docPath) {
		try {
			wlCmDocumentPathService.saveObject(docPath);
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
			wlCmDocumentPathService.deleteObject(ids);
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}