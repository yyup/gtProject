package com.gt.wl.es.action;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joyone.action.BaseAction;
import org.joyone.action.ParaMap;
import org.joyone.lang.BaseException;
import org.joyone.spring.Sc;
import org.joyone.sys.Lang;
import org.joyone.sys.Page;
import org.joyone.sys.User;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gt.wl.cm.service.WlCmSecurityService;
import com.gt.wl.cm.service.WlCmTypeService;
import com.gt.wl.es.model.WlEsCustInstall;
import com.gt.wl.es.model.WlEsStoreOut;
import com.gt.wl.es.service.WlEsCustInstallService;
import com.gt.wl.es.service.WlEsStoreOutService;


/**
 * 安装申请atcion
 * @author Lizj 
 *
 */
@Controller
@RequestMapping("/wl/es/wlEsCustInstallAction.do")
public class WlEsCustInstallAction extends BaseAction {
	private WlEsCustInstallService wlEsCustInstallService = (WlEsCustInstallService) Sc.getBean("wl.es.WlEsCustInstallService");
	private WlCmTypeService wlCmTypeService = (WlCmTypeService) Sc.getBean("wl.cm.WlCmTypeService");
	private WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");
	private WlEsStoreOutService wlEsStoreOutService = (WlEsStoreOutService) Sc.getBean("wl.es.WlEsStoreOutService");
	private final static String SYSTEM_NAME = Lang.getString("wl.es.SYSTEM_NAME");
	private final static String MODULE_NAME = Lang.getString("wl.es.wlEsCustInstallAction.MODULE_NAME");
	private final static String MODULE_CODE = "0302010";// 模块代码

	/**
	 * 界面初始化
	 * @param enumTypes 枚举
	 * @param user 用户
	 * @return 枚举数据
	 */
	@RequestMapping(params = "action=init")
	@ResponseBody
	public String init(String[] enumTypes, User user) {
		try {
			Map<String, Object> mapResult = wlCmSecurityService.getSmcMap(user, MODULE_CODE);
			mapResult.putAll(wlCmTypeService.getEnumType(enumTypes));// 到字典表获取枚举数据
			return this.getJson(true, mapResult);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}
	
	/**
	 * 查询安装申请
	 * @param currPage 当前页
	 * @param pageSize 页数
	 * @param paraMap 前端参数
	 * @return 配件申请信息
	 */
	@RequestMapping(params = "action=search")
	@ResponseBody
	public String search(int currPage, int pageSize,ParaMap paraMap) {
		try {
			Map pMap = paraMap.getMap();
			Page page = wlEsCustInstallService.search(currPage, pageSize,pMap);

			return this.getJson(true, page);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}

	}
	

	/**
	 * 获取详细信息
	 * @param installId 安装ID
	 * @return 结果
	 */
	@RequestMapping(params = "action=getData")
	@ResponseBody
	public String getData(String installId) {
		try {
			WlEsCustInstall wlEsCustInstall = (WlEsCustInstall) wlEsCustInstallService.getObject(installId);		
			return this.getJson(true, wlEsCustInstall);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}
	
	/**
	 * 保存安装申请
	 * @param wlEsCustInstall 安装对象
	 * @param user 当前登录用户
	 * @return 空
	 */
	@RequestMapping(params = "action=saveData")
	@ResponseBody
	public String saveData(WlEsCustInstall wlEsCustInstall, User user) {
		try {
			WlEsStoreOut wlEsStoreOut = wlEsStoreOutService.validateDeviceCdData(wlEsCustInstall.getDeviceCd());
			if (ValidateUtil.isNull(wlEsStoreOut)) {// 序列号登记表找不到记录
				return this.getJson(true, Lang.getString("wl.es.wlEsCustInstallAction.noDeviceCd"));
			}
			if (ValidateUtil.isEmpty(wlEsCustInstall.getInstallId())) {// 新增
				wlEsCustInstall.setCreator(user.getName());
				wlEsCustInstall.setCreateTime(new Date());
				wlEsCustInstall.setInstallNo(wlEsCustInstallService.getNewCustInstallNO());
			}
			else {// 编辑
				wlEsCustInstall.setModifier(user.getName());
				wlEsCustInstall.setModifyTime(new Date());
			}
			if (!ValidateUtil.isEmpty(wlEsCustInstall.getAddr())) {
				String addr = wlEsCustInstall.getAddr();
				addr = addr.replaceAll("\\t", "");// 去掉tab键
				wlEsCustInstall.setAddr(addr);
			}
			wlEsCustInstallService.saveObject(wlEsCustInstall);
			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}
	
	/**
	 * 序列号登记信息
	 * @param deviceCd 设备编码即序列号
	 * @return 序列号登记信息
	 */
	@RequestMapping(params = "action=getStoreOut")
	@ResponseBody
	public String getStoreOut(String deviceCd) {
		try {
			WlEsStoreOut wlEsStoreOut = wlEsStoreOutService.validateDeviceCdData(deviceCd);
			if (!ValidateUtil.isNull(wlEsStoreOut)) {
				Map map = new HashMap();
				map.put("addr", wlEsStoreOut.getAddr());
				map.put("spec", wlEsStoreOut.getProductName());
				map.put("mobile", wlEsStoreOut.getContact());
				map.put("receiver", wlEsStoreOut.getReceiver());
				return this.getJson(true, map);
			}
			else {
				return this.getJson(true, "");
			}
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 更新安装状态
	 * @param user 用户
	 * @param installIds 安装申请ids
	 * @param installState 安装状态(1,2,3)
	 * @param installMobile 安装员电话
	 * @return 操作结果
	 */
	@RequestMapping(params = "action=updateInstallState")
	@ResponseBody
	public String updateInstallState(User user, String[] installIds, String installState,String installMobile) {

		try {
			wlEsCustInstallService.getCheckInstallState(installIds,installState);
			String logs = "";
			List<WlEsCustInstall> list = new ArrayList();
			for (String installId : installIds) {
				WlEsCustInstall wlEsCustInstall = (WlEsCustInstall) wlEsCustInstallService.getObject(installId);
				wlEsCustInstall.setInstallState(installState);
				wlEsCustInstall.setInstallStateTime(new Date());
				wlEsCustInstall.setInstallStateMan(user.getName());
				if(!ValidateUtil.isEmpty(installMobile)){
					wlEsCustInstall.setInstallMobile(installMobile);
				}
				list.add(wlEsCustInstall);
				logs += wlEsCustInstall.getDeviceCd() + ";";
			}
			if ("1".equals(installState)) { // 通过时的日志
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.es.wlEsCustInstallAction.apply"), "[" + logs + "]");
			}
			else if ("2".equals(installState)) { // 驳回时的日志
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.es.wlEsCustInstallAction.dispatch"), "[" + logs + "]");
			}
			else if ("3".equals(installState)) { // 驳回时的日志
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.es.wlEsCustInstallAction.installSuccess"), "[" + logs + "]");
			}
			wlEsCustInstallService.saveList(list);
			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}
}