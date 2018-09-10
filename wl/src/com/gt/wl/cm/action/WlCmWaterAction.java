package com.gt.wl.cm.action;

import org.joyone.action.BaseAction;
import org.joyone.lang.BaseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 接收水压上报action层
 * @author liuyj
 * 
 */
@Controller
@RequestMapping({ "/wl/cm/wlCmWaterAction.do", "/wl/cm/wlCmWaterAction.dox" })
public class WlCmWaterAction extends BaseAction {

	/**
	 * 保存上传数据
	 * @param dataJson 上传水压json数据
	 * @return 结果
	 */
	@RequestMapping(params = "action=saveData")
	@ResponseBody
	public String saveData(String dataJson) {
		try {
			System.out.println("dataJson=" + dataJson);
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}