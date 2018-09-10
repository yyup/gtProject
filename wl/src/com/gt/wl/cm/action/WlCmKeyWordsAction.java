package com.gt.wl.cm.action;

import java.util.Date;
import java.util.Map;

import org.joyone.action.BaseAction;
import org.joyone.lang.BaseException;
import org.joyone.spring.Sc;
import org.joyone.sys.Lang;
import org.joyone.sys.Page;
import org.joyone.sys.User;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gt.wl.cm.model.WlCmKeywords;
import com.gt.wl.cm.service.WlCmKeywordsService;
import com.gt.wl.cm.service.WlCmSecurityService;
import com.gt.wl.cm.service.WlCmTypeService;

@Controller
@RequestMapping("/wl/cm/wlCmKeyWordsAction.do")
public class WlCmKeyWordsAction extends BaseAction {
	private WlCmKeywordsService wlCmKeywordsService = (WlCmKeywordsService) Sc.getBean("wl.cm.WlCmKeywordsService");
	private WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");
	private WlCmTypeService wlCmTypeService = (WlCmTypeService) Sc.getBean("wl.cm.WlCmTypeService"); 
	private final static String MODULE_CODE = "0301010";// 模块代码
	private final static String SYSTEM_NAME = Lang.getString("wl.cm.SYSTEM_NAME");
	private final static String MODULE_NAME = Lang.getString("wl.cm.wlCmKeyWordsAction.MODULE_NAME");

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
			return this.getJson(true, mapResult);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
	/**
	 * 获取数据列表
	 * @param pageSize 页记录数
	 * @param currPage 页码
	 * @return 数据列表
	 */
	@RequestMapping(params = "action=findDataList")
	@ResponseBody
	public String findDataList(int pageSize, int currPage) {

		try {
			Page page = wlCmKeywordsService.search(pageSize, currPage);
			return this.getJson(true, page);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}
	
	/**
	 * 通过 ID获取实体对象
	 * @param keywordsId 关键字id
	 * @return 实体对象
	 */
	@RequestMapping(params = "action=getData")
	@ResponseBody
	public String getData(String keywordsId) {
		try {
			WlCmKeywords wlCmKeywords = (WlCmKeywords) wlCmKeywordsService.getObject(keywordsId);
			return this.getJson(true,wlCmKeywords);

		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}
	/**
	 * 保存类型
	 * @param wlCmKeywords 关键字对象
	 * @param user 用户
	 * @return 结果
	 */
	@RequestMapping(params = "action=saveData")
	@ResponseBody
	public String saveData(WlCmKeywords wlCmKeywords, User user) {

		try {//新增
			if(ValidateUtil.isEmpty(wlCmKeywords.getKeywordsId())){
				
				wlCmKeywords.setCreateTime(new Date());
				wlCmKeywords.setCreator(user.getName());
				 this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.add"), "[" + wlCmKeywords.getKeywords() + "]");
				
			}
			else
			{//编辑
				wlCmKeywords.setModifyTime(new Date());
				wlCmKeywords.setModifier(user.getName());
			  
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.edit"), "[" + wlCmKeywords.getKeywords() + "]");
			}
			
			wlCmKeywordsService.saveObject(wlCmKeywords);
			
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
	
	/**
	 * 批量删除数据
	 * @param ids 关键字IDs
	 * @return 操作结果
	 * @param user 当前用户
	 */
	@RequestMapping(params = "action=deleteData")
	@ResponseBody
	public String deleteData(String ids[],User user) {
		try {
			String logs = "";
			for (String id : ids) {
				WlCmKeywords wlCmKeywords = (WlCmKeywords) wlCmKeywordsService.getObject(id);
				logs += wlCmKeywords.getKeywords() + ";";
			}
			this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("dw.delete"), "[" + logs + "]");
			wlCmKeywordsService.deleteObject(ids);
			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}
}