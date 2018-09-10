package com.gt.wl.cm.action;

import java.util.List;

import org.joyone.action.BaseAction;
import org.joyone.lang.BaseException;
import org.joyone.spring.Sc;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gt.wl.cm.service.WlCmAreaService;

@Controller
@RequestMapping({ "/wl/cm/wlCmAreaAction.do", "/wl/cm/wlCmAreaAction.web" })
public class WlCmAreaAction extends BaseAction {
	private WlCmAreaService wlCmAreaService = (WlCmAreaService) Sc.getBean("wl.cm.WlCmAreaService");

	/**
	 * 获取某区域下所有子区域数据列表
	 * @param parentId 当前区域ID
	 * @return 区域数据列表对象
	 */
	@RequestMapping(params = "action=findAreaByParentId")
	@ResponseBody
	public String findAreaByParentId(String parentId) {
		try {
			// 获取数据
			List areaList = wlCmAreaService.findAreByParentId(parentId);
			return this.getJson(true, areaList);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}
}