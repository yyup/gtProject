package com.gt.wl.es.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joyone.action.BaseAction;
import org.joyone.action.ParaMap;
import org.joyone.lang.BaseException;
import org.joyone.spring.Sc;
import org.joyone.sys.Lang;
import org.joyone.sys.User;
import org.joyone.util.JsonUtil;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gt.wl.cm.service.WlCmAreaService;
import com.gt.wl.cm.service.WlCmSecurityService;
import com.gt.wl.cm.service.WlCmSellService;
import com.gt.wl.cm.service.WlCmTypeService;
import com.gt.wl.es.model.WlEsFeeTemp;
import com.gt.wl.es.model.WlEsFeeTempRow;
import com.gt.wl.es.service.WlEsFeeTempService;

/**
 * 运费模板action层
 * @author huangbj
 * 
 */
@Controller
@RequestMapping({"/wl/es/WlEsFeeTempAction.do","/wl/es/WlEsFeeTempAction.web"})
public class WlEsFeeTempAction extends BaseAction {
	private WlEsFeeTempService wlEsFeeTempService = (WlEsFeeTempService) Sc.getBean("wl.es.WlEsFeeTempService");
	private WlCmTypeService wlCmTypeService = (WlCmTypeService) Sc.getBean("wl.cm.WlCmTypeService");
	private WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");
	private WlCmAreaService wlCmAreaService = (WlCmAreaService) Sc.getBean("wl.cm.WlCmAreaService");
	private WlCmSellService wlCmSellService = (WlCmSellService) Sc.getBean("wl.cm.WlCmSellService");

	private final static String SYSTEM_NAME = Lang.getString("wl.es.SYSTEM_NAME");
	private final static String MODULE_NAME = Lang.getString("wl.es.WlEsFeeTempAction.MODULE_NAME");
	private final static String MODULE_CODE = "0302008";// 模块代码

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
			Map paraMap = new HashMap();
			paraMap.put("sellStateEk", "HAS_SHELVE");
			mapResult.put("sellEnum", wlCmSellService.findSellListByMap(paraMap));
			return this.getJson(true, mapResult);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取地区树
	 * @return 地区树
	 */
	@RequestMapping(params = "action=getAreaTree")
	@ResponseBody
	public String getAreaTree() {
		try {
			return this.getJson(true, wlCmAreaService.getAreaTree("2"));
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 保存数据
	 * @param wlEsFeeTemp 运费模板
	 * @param feeTempRow 模板行列表且含有到底地区（json格式）
	 * @param user 登陆用户
	 * @return 空
	 */
	@RequestMapping(params = "action=saveData")
	@ResponseBody
	public String saveData(WlEsFeeTemp wlEsFeeTemp, String feeTempRow, User user) {
		try {
			if (ValidateUtil.isEmpty(wlEsFeeTemp.getTempId())) {// 新增
				wlEsFeeTemp.setCreateTime(new Date());
				wlEsFeeTemp.setCreator(user.getName());
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.add"), Lang.getString("wl.es.WlEsAgencyOrderAction.orderNo") + wlEsFeeTemp.getTempName());
			}
			else {// 编辑
				wlEsFeeTemp.setModifyTime(new Date());
				wlEsFeeTemp.setModifier(user.getName());
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.edit"), Lang.getString("wl.es.WlEsAgencyOrderAction.orderNo") + wlEsFeeTemp.getTempName());
			}
			List<WlEsFeeTempRow> wlEsFeeTempRowList = JsonUtil.toList(feeTempRow, WlEsFeeTempRow.class);
			wlEsFeeTempService.saveData(wlEsFeeTemp, wlEsFeeTempRowList);
			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取模板分页数据
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param paraMap result
	 * @return 模板分页数据
	 */
	@RequestMapping(params = "action=search")
	@ResponseBody
	public String search(int pageSize, int currPage, ParaMap paraMap) {
		try {
			Map map = paraMap.getMap();
			return this.getJson(true, wlEsFeeTempService.search(pageSize, currPage, map));
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 运费详细信息
	 * @param tempId 运费模板id
	 * @return 运费详细信息
	 */
	@RequestMapping(params = "action=getData")
	@ResponseBody
	public String getData(String tempId) {
		try {
			return this.getJson(true, wlEsFeeTempService.getData(tempId));
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 删除模板
	 * @param tempId 模板id
	 * @return 空
	 */
	@RequestMapping(params = "action=deleteData")
	@ResponseBody
	public String deleteData(String tempId) {
		try {
			wlEsFeeTempService.deleteData(tempId);
			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}
	
	/**
	 * 获取运费
	 * @param sellId 产品id
	 * @param sellIds 产品ids
	 * @param areaId 区域id
	 * @return 运费
	 */
	@RequestMapping(params = "action=getFreight")
	@ResponseBody
	public String getFreight(String sellId,String[] sellIds,String areaId) {
		try {			
			return this.getJson(true, wlEsFeeTempService.getFreight(sellId, sellIds, areaId));
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

}