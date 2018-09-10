package com.gt.wl.cm.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.joyone.action.BaseAction;
import org.joyone.lang.BaseException;
import org.joyone.model.EnumModel;
import org.joyone.spring.Sc;
import org.joyone.sys.Lang;
import org.joyone.sys.Page;
import org.joyone.sys.User;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gt.wl.cm.model.WlCmColumnEn;
import com.gt.wl.cm.model.WlCmInfoEn;
import com.gt.wl.cm.service.WlCmBizAuditLogService;
import com.gt.wl.cm.service.WlCmColumnEnService;
import com.gt.wl.cm.service.WlCmInfoEnService;
import com.gt.wl.cm.service.WlCmKeywordsService;
import com.gt.wl.cm.service.WlCmSecurityService;
import com.gt.wl.cm.service.WlCmTypeService;

/**
 * 信息发布action层
 * @author huangbj
 * 
 */
@Controller
@RequestMapping({ "/wl/cm/wlCmInfoEnAction.do", "/wl/cm/wlCmInfoEnAction.dox" })
public class WlCmInfoEnAction extends BaseAction {
	private WlCmInfoEnService wlCmInfoService = (WlCmInfoEnService) Sc.getBean("wl.cm.WlCmInfoEnService");
	private WlCmColumnEnService wlCmColumnService = (WlCmColumnEnService) Sc.getBean("wl.cm.WlCmColumnEnService");
	private WlCmBizAuditLogService wlCmBizAuditLogService = (WlCmBizAuditLogService) Sc.getBean("wl.cm.WlCmBizAuditLogService");
	private WlCmKeywordsService wlCmKeywordsService = (WlCmKeywordsService) Sc.getBean("wl.cm.WlCmKeywordsService");
	private final static String SYSTEM_NAME = Lang.getString("wl.cm.SYSTEM_NAME");
	private final static String MODULE_NAME = Lang.getString("wl.cm.WlCmInfoEnAction.MODULE_NAME");
	private final static String MODULE_CODE = "0307002";// 模块代码

	/**
	 * 获取页面初始化权限和返回枚举数据
	 * @param enumTypes 枚举类型
	 * @param user 用户
	 * @return 返回枚举数据
	 */
	@RequestMapping(params = "action=init")
	@ResponseBody
	public String init(String[] enumTypes, User user) {
		try {
			WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");
			Map mapResult = wlCmSecurityService.getSmcMap(user, MODULE_CODE);// 根据模块代码获取权限
			WlCmTypeService wlCmTypeService = (WlCmTypeService) Sc.getBean("wl.cm.WlCmTypeService");
			Map map = wlCmTypeService.getEnumType(enumTypes);
			// 去掉取消发布
			List<EnumModel> list = (List<EnumModel>) map.get("ISSUE_STATE_EKEnum");
			List<EnumModel> tempList = new ArrayList();
			for (EnumModel enumModel : list) {
				if (!"0".equals(enumModel.getValue())) {
					tempList.add(enumModel);
				}
			}
			map.put("ISSUE_STATE_EKEnum", tempList);
			mapResult.putAll(map);// 到字典表获取枚举数据
			return this.getJson(true, mapResult);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 返回Tree 对象
	 * @param user 用户
	 * @param isFirst 是否首次点开信息管理，空首次点开，非空不是
	 * @param smc 当前用户拥有的权限，0代表没有权限，1代表初审，2，代表终审
	 * @return map树对象
	 */
	@RequestMapping(params = "action=getTree")
	@ResponseBody
	public String getTree(User user, String isFirst, String smc) {
		try {
			Map map = wlCmInfoService.getTree("0", isFirst, smc);
			return this.getJson(true, map);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 增加数据
	 * @param wlCmInfo 发布信息对象
	 * @param user 当前登录用户
	 * @return 结果
	 */
	@RequestMapping(params = "action=saveData")
	@ResponseBody
	public String saveData(@ModelAttribute("saveModel") WlCmInfoEn wlCmInfo, User user) {
		try {
			String content = wlCmInfo.getContent();
			if (!ValidateUtil.isEmpty(content)) {
				content = content.replaceAll("\\t", "");
				wlCmInfo.setContent(content);
			}
			if (ValidateUtil.isEmpty(wlCmInfo.getInfoId())) {// 新增
				wlCmInfo.setCreator(user.getName());
				wlCmInfo.setCreateTime(new Date());
				wlCmInfo.setPreIssueTime(new Date());
				wlCmInfo.setIssueTime(new Date());
				wlCmInfo.setModifyTime(new Date());
				wlCmInfo.setIssueStateEk("1");
				wlCmInfo.setDataState("1");
				if (wlCmInfo.getSequ() == 0) {
					wlCmInfo.setSequ(1);
				}
				wlCmInfo.setAuditTime(null);
				// 把栏目的名字当作单篇或文章发布的标题
				WlCmColumnEn wlCmColumn = (WlCmColumnEn) wlCmColumnService.getObject(wlCmInfo.getColumnId());
				if ("SINGLE".equals(wlCmColumn.getColTypeEk()) || "ARTICLE".equals(wlCmColumn.getColTypeEk())) {
					wlCmInfo.setTitle(wlCmColumn.getColumnName());
				}
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.add"), wlCmInfo.getTitle());
			}
			else {// 编辑
				wlCmInfo.setModifyTime(new Date());
				wlCmInfo.setModifier(user.getName());
				// wlCmInfo.setIssueStateEk("1");// 切换成未发布
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.edit"), wlCmInfo.getTitle());
			}

			wlCmInfoService.saveData(wlCmInfo, user);
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据ID获取对象
	 * @param key 发布信息的字段
	 * @param value key对应的字段的值
	 * @return 对象
	 */
	@RequestMapping(params = "action=getData")
	@ResponseBody
	public String getData(String key, String value) {
		try {

			List list = wlCmInfoService.getWlCmInfoList(key, value);
			WlCmInfoEn wlCmInfo = null;
			if (!ValidateUtil.isEmpty(list)) {
				wlCmInfo = (WlCmInfoEn) list.get(0);
				List bizLogList = wlCmBizAuditLogService.findBizAuditList(value);
				if (!ValidateUtil.isEmpty(bizLogList)) {
					wlCmInfo.setBizLogList(bizLogList);
				}
			}
			return this.getJson(true, wlCmInfo);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 查询信息发布管理，获取翻页数据
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param columnId 信息栏目id
	 * @param title 标题
	 * @param issueStateEk 发布状态
	 * @param auditNodeEk 审核环节
	 * @param auditState 审核状态
	 * @param titleAndContent 标题和内容
	 * @return 结果
	 */
	@RequestMapping(params = "action=findDataList")
	@ResponseBody
	public String findDataList(int pageSize, int currPage, String columnId, String title, String issueStateEk, String auditNodeEk, String auditState,String titleAndContent) {
		try {
			Page page = wlCmInfoService.search(pageSize, currPage, columnId, title, issueStateEk, "1", auditNodeEk, auditState,titleAndContent);
			return this.getJson(true, page);
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
		String wlCmInfoNames = "";
		try {
			for (int i = 0; i < ids.length; i++) {
				// 获取类型的名称
				WlCmInfoEn wlCmInfo = (WlCmInfoEn) wlCmInfoService.getObject(ids[i]);
				wlCmInfoNames += wlCmInfo.getTitle() + ",";
			}
			// 删除类型
			wlCmInfoService.updateWlCmInfoEnsDataState(ids, "0");
			// 设置日志
			if (!ValidateUtil.isEmpty(wlCmInfoNames)) {
				wlCmInfoNames = wlCmInfoNames.substring(0, wlCmInfoNames.length() - 1);
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.delete"), wlCmInfoNames);
			}

			return this.getJson(true, "");

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 修改信息发布的使用状态
	 * @param infoIds 信息的id数组
	 * @param issueStateEk 要修改成的信息使用状态
	 * @param user 当前用户
	 * @return 操作结果
	 */
	@RequestMapping(params = "action=updateWlCmInfoState")
	@ResponseBody
	public String updateWlCmInfoEnState(String[] infoIds, String issueStateEk, User user) {
		try {
			wlCmInfoService.updateWlCmInfoState(infoIds, issueStateEk);
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 信息发布初始化页面的对象
	 * @return 栏目对象
	 */
	@RequestMapping(params = "action=getInitializePage")
	@ResponseBody
	public String getInitializePage() {
		try {
			// List list = wlCmColumnService.findAllColumnList("1", "1");
			List list = new ArrayList();
			wlCmColumnService.getInitializePage("0", "1", "1", list);
			WlCmColumnEn wlCmColumn = null;
			if (!ValidateUtil.isEmpty(list)) {
				wlCmColumn = (WlCmColumnEn) list.get(0);
			}
			return this.getJson(true, wlCmColumn);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取链接地址
	 * @param request 请求
	 * @param infoId 信息id
	 * @return 链接地址
	 */
	@RequestMapping(params = "action=getLinkAddr")
	@ResponseBody
	public String getLinkAddr(String infoId, HttpServletRequest request) {
		try {
			WlCmInfoEn wlCmInfo = (WlCmInfoEn) wlCmInfoService.getObject(infoId);
			if (!"3".equals(wlCmInfo.getAuditState())) {// 不是终审通过
				return this.getJson(true, Lang.getString("wl.cm.WlCmInfoEnService.nofinalAuditPass"));
			}
			else {
				String path = request.getContextPath();
				String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
				return this.getJson(true, basePath + "html/site/wlCmArticle.html?infoId=" + infoId);
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 更改为初审通过或初审驳回
	 * @param infoIds 信息id数组
	 * @param auditRemark 审核意见
	 * @param auditNodeEk 审核环节
	 * @param auditState 审核状态
	 * @param smc 当前用户拥有的权限，0代表没有权限，1代表初审，2，代表终审
	 * @param user 当前登录用户
	 * @return 栏目树
	 */
	@RequestMapping(params = "action=updateFirstAudit")
	@ResponseBody
	public String updateFirstAudit(String[] infoIds, String auditRemark, String auditNodeEk, String auditState, String smc, User user) {
		try {
			wlCmInfoService.updateFirstAudit(infoIds, auditRemark, auditNodeEk, auditState, user);
			return this.getJson(true, wlCmInfoService.getTree("0", "0", smc));
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 更改为终审通过或终审驳回
	 * @param infoIds 信息id数组
	 * @param auditRemark 审核意见
	 * @param auditNodeEk 审核环节
	 * @param auditState 审核状态
	 * @param smc 当前用户拥有的权限，0代表没有权限，1代表初审，2，代表终审
	 * @param columnType 栏目类型，single 代表单边或文章，list代表列表
	 * @param user 当前登录用户
	 * @return 栏目树
	 */
	@RequestMapping(params = "action=updateFinalAudit")
	@ResponseBody
	public String updateFinalAudit(String[] infoIds, String auditRemark, String auditNodeEk, String smc, String auditState, String columnType, User user) {
		try {
			wlCmInfoService.updateFinalAudit(infoIds, auditRemark, auditNodeEk, auditState, columnType, user);
			return this.getJson(true, wlCmInfoService.getTree("0", "0", smc));
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/***
	 * 
	 * @param infoId 信息id
	 * @return 信息操作日志列表
	 */
	@RequestMapping(params = "action=findBizAuditList")
	@ResponseBody
	public String findBizAuditList(String infoId) {
		try {
			return this.getJson(true, wlCmBizAuditLogService.findBizAuditList(infoId));
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 将编辑预览的信息缓存在session
	 * @param type set表示设置值到session，get表示从session取值出来
	 * @param content 预览信息对象内容
	 * @param request 请求
	 * @return 预览信息或空
	 */
	@RequestMapping(params = "action=getOrSetInfo")
	@ResponseBody
	public String getOrSetInfo(String type, String content, String title, HttpServletRequest request) {
		try {
			HttpSession session = request.getSession(true);
			if ("set".equals(type)) {// 设置
				session.setAttribute("content", content);
				session.setAttribute("title", title);
			}
			else if ("get".equals(type)) {// 取值
				Map map = new HashMap();
				map.put("content", session.getAttribute("content"));
				map.put("title", session.getAttribute("title"));
				return this.getJson(true, map);
			}
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
	/**
	 * 查询信息列表
	 * @param columnId 栏目ID
	 * @param title 标题
	 * @return 信息列表
	 */
	@RequestMapping(params = "action=findInfoList")
	@ResponseBody
	public String findInfoList(String columnId,String title) {
		try {
			List list=wlCmInfoService.findInfoList("", columnId, "2", "", "", "1",title);
			return this.getJson(true, list);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
	
	/**
	 * 查询关键字列表
	 * @return 关键字列表
	 */
	@RequestMapping(params = "action=findKeywordsList")
	@ResponseBody
	public String findKeywordsList() {
		try {
			List list=wlCmKeywordsService.getObjects();
			return this.getJson(true, list);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}