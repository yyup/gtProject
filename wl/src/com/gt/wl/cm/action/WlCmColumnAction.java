package com.gt.wl.cm.action;

import java.util.Date;
import java.util.HashMap;
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

import com.gt.wl.cm.model.WlCmColumn;
import com.gt.wl.cm.model.WlCmInfo;
import com.gt.wl.cm.service.WlCmBizAuditLogService;
import com.gt.wl.cm.service.WlCmColumnService;
import com.gt.wl.cm.service.WlCmInfoService;
import com.gt.wl.cm.service.WlCmSecurityService;
import com.gt.wl.cm.service.WlCmTypeService;

/**
 * 栏目管理action层
 * @author huangbj
 * 
 */
@Controller
@RequestMapping({ "/wl/cm/wlCmColumnAction.do", "/wl/cm/wlCmColumnAction.dox" })
public class WlCmColumnAction extends BaseAction {
	private final static String SYSTEM_NAME = Lang.getString("wl.cm.SYSTEM_NAME");
	private final static String MODULE_NAME = Lang.getString("wl.cm.WlCmColumnAction.MODULE_NAME");
	private WlCmBizAuditLogService wlCmBizAuditLogService = (WlCmBizAuditLogService) Sc.getBean("wl.cm.WlCmBizAuditLogService");
	private WlCmInfoService wlCmInfoService = (WlCmInfoService) Sc.getBean("wl.cm.WlCmInfoService");
	private WlCmColumnService wlCmColumnService = (WlCmColumnService) Sc.getBean("wl.cm.WlCmColumnService");
	private WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");
	private final static String MODULE_CODE = "0301001";// 模块代码

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
	 * @param isLastFlag 是否末节点
	 * @param rootId 跟节点
	 * @return 商品类型树形json
	 */
	@RequestMapping(params = "action=getTree")
	@ResponseBody
	public String getTree(User user, String isEnableFlag, String isLastFlag, String rootId) {
		try {

			Map map = wlCmColumnService.getTree(rootId, isEnableFlag, isLastFlag);

			return this.getJson(true, map);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
	/**
	 * 获取客户服务中心的页面数据
	 * @param user 当前用户
	 * @param isEnableFlag 栏目的使用状态
	 * @param isLastFlag 是否末节点
	 * @param rootId 跟节点
	 * @return 客户服务中心的页面数据
	 */
	@RequestMapping(params = "action=getCenterServiceContent")
	@ResponseBody 
	public String getCenterServiceContent(User user, String isEnableFlag, String isLastFlag, String rootId) {
		try {
			Map map = new HashMap();
			//获取上级的列表
			List list = wlCmColumnService.findAllColumnList(isEnableFlag, isLastFlag,rootId);
			List<WlCmColumn> childList;//获取下级列表
			if(!ValidateUtil.isEmpty(list)){//部位空获取第一个
				WlCmColumn parentWlCmColumn = (WlCmColumn) list.get(0);
				childList = wlCmColumnService.findAllColumnList(isEnableFlag, isLastFlag,parentWlCmColumn.getColumnId());
				map.put("titleList", childList);
				for (WlCmColumn child : childList) {
					List<WlCmInfo> infoList = wlCmInfoService.findInfoList("",child.getColumnId(),"2","","","1","");
					child.setChildColumnList(infoList);
					for (WlCmInfo wlCmInfo : infoList) {
						List bizLogList = wlCmBizAuditLogService.findBizAuditList(wlCmInfo.getInfoId());
						if (!ValidateUtil.isEmpty(bizLogList)) {
							wlCmInfo.setBizLogList(bizLogList);
						}
					}
				}
			}
			return this.getJson(true, map);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
	/**
	 * 获取除了当前节点及子节点的树
	 * @param columnId 栏目id
	 * @param isEnableFlag 栏目的使用状态
	 * @param isLastFlag 是否末节点
	 * @param user 当前用户
	 * @return 类型的树形json
	 */
	@RequestMapping(params = "action=getParentTree")
	@ResponseBody
	public String getParentTree(String columnId, String isEnableFlag, String isLastFlag, User user) {
		try {

			Map map = wlCmColumnService.getParentTree(columnId, isEnableFlag, "0", isLastFlag);

			return this.getJson(true, map);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 保存栏目
	 * @param wlCmColumn 栏目对象
	 * @param user 用户
	 * @return 结果
	 */
	@RequestMapping(params = "action=saveData")
	@ResponseBody
	public String saveData(WlCmColumn wlCmColumn, User user) {

		try {
			// 判断父ID是否为空，如为空则挂到根类型
			if (ValidateUtil.isEmpty(wlCmColumn.getParentColumnId())) {
				wlCmColumn.setParentColumnId("0");

			}
			if (wlCmColumn.getSequ() == 0) {// 如果序号没填，默认为1
				wlCmColumn.setSequ(1);
			}

			if (ValidateUtil.isEmpty(wlCmColumn.getColumnId())) {
				// 新增日志
				wlCmColumn.setDataState("1");
				wlCmColumn.setIsEnableFlag("1");
				wlCmColumn.setCreateTime(new Date());
				wlCmColumn.setCreator(user.getName());

				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.add"), wlCmColumn.getColumnName());
			}
			else {
				// 编辑日志
				wlCmColumn.setModifier(user.getName());
				wlCmColumn.setModifyTime(new Date());
				if ("1".equals(wlCmColumn.getIsLastFlag())) {
					String parentColumnIds[] = new String[1];
					parentColumnIds[0] = wlCmColumn.getColumnId();
					List list = wlCmColumnService.findChildList(parentColumnIds, "1", "1", "1");
					if (!ValidateUtil.isEmpty(list)) {
						return this.getJson(true, Lang.getString("wl.cm.WlCmColumnService.existChildNode"));
					}
				}

				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.edit"), wlCmColumn.getColumnName());
			}
			// 保存栏目
			wlCmColumnService.saveObject(wlCmColumn);
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 根据ID获取对象
	 * @param columnId 栏目的id
	 * @return 对象
	 */
	@RequestMapping(params = "action=getData")
	@ResponseBody
	public String getData(String columnId) {
		try {
			// 根据ID得到栏目类型对象
			WlCmColumn wlCmColumn = (WlCmColumn) wlCmColumnService.getObject(columnId);
			// 获取类型ID对应的父类型名称
			WlCmColumn parentColumn = (WlCmColumn) wlCmColumnService.getObject(wlCmColumn.getParentColumnId());
			if (!ValidateUtil.isNull(parentColumn)) {
				wlCmColumn.setParentColumnName(parentColumn.getColumnName());
			}
			return this.getJson(true, wlCmColumn);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 根据父ID获取相对应的子对象
	 * @param columnId 栏目的id
	 * @return 对象
	 */

	@RequestMapping(params = "action=getChildData")
	@ResponseBody
	public String getChildData(String columnId) {
		try {
			String[] columnIds = new String[1];
			columnIds[0] = columnId;
			List<WlCmColumn> childTypeList = wlCmColumnService.findChildColumnList(columnIds, "1", "1");
			for (WlCmColumn wlCmColumn : childTypeList) {//栏目下面的信息
				List<WlCmInfo> infoList = wlCmInfoService.findInfoList("",wlCmColumn.getColumnId(),"2","","","1","");
				wlCmColumn.setInfoList(infoList);
			}
			return this.getJson(true, childTypeList);
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
		String columnNames = "";
		try {
			for (int i = 0; i < ids.length; i++) {
				// 获取类型的名称
				WlCmColumn wlCmColumn = (WlCmColumn) wlCmColumnService.getObject(ids[i]);
				columnNames += wlCmColumn.getColumnName() + ",";
			}
			// 删除类型
			wlCmColumnService.deleteColumns(ids);
			// 设置日志
			if (!ValidateUtil.isEmpty(columnNames)) {
				columnNames = columnNames.substring(0, columnNames.length() - 1);
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.delete"), columnNames);
			}

			return this.getJson(true, "");

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 修改栏目的使用状态
	 * @param ids 信息的id数组
	 * @param isEnableFlag 要修改成的信息使用状态
	 * @param user 当前用户
	 * @return 操作结果
	 */
	@RequestMapping(params = "action=updateWlCmColumnState")
	@ResponseBody
	public String updateWlCmColumnState(String[] ids, String isEnableFlag, User user) {
		try {
			wlCmColumnService.updateWlCmColumnState(ids, isEnableFlag);

			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}