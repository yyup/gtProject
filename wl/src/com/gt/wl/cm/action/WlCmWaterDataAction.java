package com.gt.wl.cm.action;

import java.util.List;
import java.util.Map;

import org.joyone.action.BaseAction;
import org.joyone.lang.BaseException;
import org.joyone.spring.Sc;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gt.wl.cm.model.WlCmHydrant;
import com.gt.wl.cm.model.WlCmWaterData;
import com.gt.wl.cm.service.WlCmHydrantService;
import com.gt.wl.cm.service.WlCmWaterDataService;

/**
 * 接收水压上报action层
 * @author liuyj
 * 
 */
@Controller
@RequestMapping({ "/wl/cm/wlCmWaterDataAction.do", "/wl/cm/wlCmWaterDataAction.dox" })
public class WlCmWaterDataAction extends BaseAction {
	private WlCmWaterDataService wlCmWaterDataService = (WlCmWaterDataService) Sc.getBean("wl.cm.WlCmWaterDataService");
	private WlCmHydrantService wlCmHydrantService = (WlCmHydrantService) Sc.getBean("wl.cm.WlCmHydrantService");

	/**
	 * 初始化-所有数据状态设为“0”
	 * @return 结果
	 */
	@RequestMapping(params = "action=init")
	@ResponseBody
	public String init() {
		try {
			wlCmWaterDataService.updateWaterState();
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 保存初始化消防栓数据
	 * @param labelNo 序列号
	 * @param customNo 自编号
	 * @param longitude 经度
	 * @param latitude 纬度
	 * @param creator 用户
	 * 
	 * @return 结果
	 */
	@RequestMapping(params = "action=initData")
	@ResponseBody
	public String initData(String labelNo, String customNo, String longitude, String latitude, String creator) {
		try {
			// System.out.println("dataJson=" + dataJson);
			// WlCmHydrant wlCmHydrant = (WlCmHydrant) JsonUtil.toObject(dataJson, WlCmHydrant.class);
			System.out.println("labelNo=" + labelNo);
			System.out.println("customNo=" + customNo);
			System.out.println("longitude=" + longitude);
			System.out.println("latitude=" + latitude);
			// 通过标签序列号获取消防栓对象
			WlCmHydrant wlCmHydrant2 = wlCmHydrantService.getWlCmHydrant(labelNo);
			// 不存在，则保存
			if (ValidateUtil.isNull(wlCmHydrant2)) {
				WlCmHydrant wlCmHydrant = new WlCmHydrant();
				wlCmHydrant.setLabelNo(labelNo);
				wlCmHydrant.setCustomNo(customNo);
				wlCmHydrant.setPosition(longitude + "," + latitude);
				wlCmHydrantService.saveObject(wlCmHydrant);
			}
			else {
				// 存在，则更新
				wlCmHydrant2.setCustomNo(customNo);
				wlCmHydrant2.setPosition(longitude + "," + latitude);
				wlCmHydrantService.updateObject(wlCmHydrant2);
			}
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 保存上传数据
	 * @param labelNo 序列号
	 * @param water 水压
	 * @param creator 用户
	 * @param remark 备注
	 * @return 结果
	 */
	@RequestMapping(params = "action=saveData")
	@ResponseBody
	public String saveData(String labelNo, String water, String creator, String remark) {
		try {
			String hydrantId = "";
			// System.out.println("dataJson=" + dataJson);
			// WlCmWaterData wlCmWaterData = (WlCmWaterData) JsonUtil.toObject(dataJson, WlCmWaterData.class);
			System.out.println("labelNo=" + labelNo);
			System.out.println("water=" + water);
			System.out.println("creator=" + creator);
			// System.out.println("remark=" + datetime);
			System.out.println("remark=" + remark);
			// 通过序列号找到id
			WlCmHydrant wlCmHydrant = wlCmHydrantService.getWlCmHydrant(labelNo);
			if (!ValidateUtil.isNull(wlCmHydrant)) {
				hydrantId = wlCmHydrant.getHydrantId();
			}
			WlCmWaterData wlCmWaterData = new WlCmWaterData();
			wlCmWaterData.setWater(Double.parseDouble(water));
			wlCmWaterData.setCreator(creator);
			wlCmWaterData.setHydrantId(hydrantId);
			// 保存采集数据
			wlCmWaterDataService.saveData(wlCmWaterData);
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询有新上传水压数据的消防栓
	 * @return 结果
	 */
	@RequestMapping(params = "action=findNewWaterList")
	@ResponseBody
	public String findNewWaterList() {
		try {
			List<Map> result = wlCmWaterDataService.findNewWaterList();
			// 更新查询到的数据状态为“1”
			wlCmWaterDataService.updateWaterState(result);
			return this.getJson(true, result);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询所有消防栓
	 * @return 结果
	 */
	@RequestMapping(params = "action=findHydrantList")
	@ResponseBody
	public String findHydrantList() {
		try {
			List<WlCmHydrant> list = wlCmHydrantService.getObjects();
			return this.getJson(true, list);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过消防栓ID查找最后上报的水压数据
	 * @param hydrantId 消防栓ID
	 * @return 水压采集数据
	 */
	@RequestMapping(params = "action=getWlCmWaterData")
	@ResponseBody
	public String getWlCmWaterData(String hydrantId) {
		try {
			WlCmWaterData wlCmWaterData = wlCmWaterDataService.getWlCmWaterData(hydrantId);
			return this.getJson(true, wlCmWaterData);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}