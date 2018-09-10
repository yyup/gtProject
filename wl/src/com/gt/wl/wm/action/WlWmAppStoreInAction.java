package com.gt.wl.wm.action;

import java.util.ArrayList;
import java.util.List;

import org.joyone.action.BaseAction;
import org.joyone.lang.BaseException;
import org.joyone.spring.Sc;
import org.joyone.util.JsonUtil;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gt.wl.wm.model.WlWmStoreIn;
import com.gt.wl.wm.model.WlWmStoreInDetl;
import com.gt.wl.wm.model.WlWmStoreInDetlInfo;
import com.gt.wl.wm.service.WlWmStoreInService;

/**
 * 入库单据APP端Action层
 * @author liuyj
 * 
 */
@Controller
@RequestMapping("/app/wm/wlWmAppStoreInAction.do")
public class WlWmAppStoreInAction extends BaseAction {
	private WlWmStoreInService wlWmStoreInService = (WlWmStoreInService) Sc.getBean("wl.wm.WlWmStoreInService");

	/**
	 * 新增数据
	 * @param noticeId 通知单ID
	 * @param userId 用户ID
	 * @param userName 用户名
	 * @param detlListJson 货品信息列表
	 * @param detlInfoListJson 序号信息列表
	 * @return 结果
	 */
	@RequestMapping(params = "action=saveData")
	@ResponseBody
	public String saveData(String noticeId, String userId, String userName, String detlListJson, String detlInfoListJson) {
		try {
			WlWmStoreIn wlWmStoreIn = new WlWmStoreIn();
			wlWmStoreIn.setNoticeNo(noticeId);// 通知单ID暂存noticeNo中
			wlWmStoreIn.setCreateOprId(userId);
			wlWmStoreIn.setCreateor(userName);

			// 把前台传来的Json转换成list
			List<WlWmStoreInDetl> detlList = JsonUtil.toList(detlListJson, WlWmStoreInDetl.class);
			List<WlWmStoreInDetlInfo> detlInfoList = new ArrayList<WlWmStoreInDetlInfo>();
			if (!ValidateUtil.isEmpty(detlInfoListJson)) {
				detlInfoList = JsonUtil.toList(detlInfoListJson, WlWmStoreInDetlInfo.class);
			}
			wlWmStoreInService.saveData(wlWmStoreIn, detlList, detlInfoList);
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 修改数据
	 * @param billId 单据ID
	 * @param userId 用户ID
	 * @param userName 用户名
	 * @param detlListJson 货品信息列表
	 * @param detlInfoListJson 序号信息列表
	 * @return 结果
	 */
	@RequestMapping(params = "action=updateData")
	@ResponseBody
	public String updateData(String billId, String userId, String userName, String detlListJson, String detlInfoListJson) {
		try {

			// 把前台传来的Json转换成list
			List<WlWmStoreInDetl> detlList = JsonUtil.toList(detlListJson, WlWmStoreInDetl.class);
			List<WlWmStoreInDetlInfo> detlInfoList = new ArrayList<WlWmStoreInDetlInfo>();
			if (!ValidateUtil.isEmpty(detlInfoListJson)) {
				detlInfoList = JsonUtil.toList(detlInfoListJson, WlWmStoreInDetlInfo.class);
			}
			wlWmStoreInService.updateData(billId, userName, detlList, detlInfoList);
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}