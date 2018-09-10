package com.gt.wl.es.action;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.joyone.action.BaseAction;
import org.joyone.action.ParaMap;
import org.joyone.excel.ExcelExportUtil;
import org.joyone.lang.BaseException;
import org.joyone.spring.Sc;
import org.joyone.sys.Lang;
import org.joyone.sys.Page;
import org.joyone.sys.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gt.wl.cm.service.WlCmSecurityService;
import com.gt.wl.cm.service.WlCmTypeService;
import com.gt.wl.es.service.WlEsOrderService;
import com.gt.wl.util.Type;

/**
 * 订单查询统计Action层
 * @author dongyl
 */

@Controller
@RequestMapping("/wl/es/WlEsOrderQueryAction.do")
public class WlEsOrderQueryAction extends BaseAction {
	private WlEsOrderService wlEsOrderService = (WlEsOrderService) Sc.getBean("wl.es.WlEsOrderService");
	private WlCmTypeService wlCmTypeService = (WlCmTypeService) Sc.getBean("wl.cm.WlCmTypeService");
	private WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");
	private final static String SYSTEM_NAME = Lang.getString("wl.es.SYSTEM_NAME");
	private final static String MODULE_NAME = Lang.getString("wl.es.wlEsOrderQueryAction.MODULE_NAME");
	private final static String MODULE_CODE = "0302006";// 模块代码

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
	 * 查询订单信息(分页)
	 * @param currPage 当前页
	 * @param pageSize 页数
	 * @param paraMap 前端参数
	 * @return 订单信息
	 */
	@RequestMapping(params = "action=findOrderList")
	@ResponseBody
	public String findOrderList(int pageSize, int currPage, ParaMap paraMap) {
		try {
			Map map = paraMap.getMap();
			Page page = wlEsOrderService.search(pageSize, currPage, map);
			return this.getJson(true, page);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询订单信息
	 * @param orderStateEk 订单状态
	 * @param fromTime 开始时间
	 * @param toTime 结束时间
	 * @param response 响应
	 */
	@RequestMapping(params = "action=exportDataList")
	@ResponseBody
	public void exportDataList(String orderStateEk, String fromTime, String toTime, HttpServletResponse response) {
		try {
			// 得到列表数据
			List<Map> list = wlEsOrderService.findOrderList(orderStateEk, fromTime, toTime);
			// 得到订单状态枚举
			List<Type> orderStatusList = wlCmTypeService.findByCode("ORDER_STATE_EK");
			// 得到支付方式枚举
			List<Type> payModeList = wlCmTypeService.findByCode("PAY_MODE_EK");
			// 循环列表，设置订单状态与支付方式
			for (Map map : list) {
				String stateEk = (String) map.get("orderStateEk");
				String payModeEk = (String) map.get("payModeEk");
				for (Type type : orderStatusList) {
					if (type.getId().equals(stateEk)) {
						map.put("orderStateEkName", type.getLable());
						break;
					}
				}
				for (Type type : payModeList) {
					if (type.getId().equals(payModeEk)) {
						map.put("payModeEkName", type.getLable());
						break;
					}
				}
			}
			// 得到表头数据
			List fieldList = ExcelExportUtil.getHeadFieldList("export/WlEsOrderOut.xml");
			// 清空response
			response.reset();
			// 设置response的Header
			// 相应消息不缓存
			response.setHeader("Cache-Control", "no-cache");
			response.setContentType("application/x-msdownload");
			// 下载格式设置
			response.setHeader("Content-disposition", "attachment;filename=data.xls"); // 文件名

			ServletOutputStream out = response.getOutputStream();
			ExcelExportUtil.export(out, fieldList, list);
			out.flush();
			out.close();
			response.flushBuffer();
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}
