package com.gt.wl.es.action;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.joyone.action.BaseAction;
import org.joyone.excel.ExcelExportUtil;
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
import com.gt.wl.es.model.WlEsStoreOut;
import com.gt.wl.es.service.WlEsStoreOutService;

/**
 * 出库Action层
 * 设备序列号导入导出管理
 * @author liuyj
 * 
 */

@Controller
@RequestMapping("/wl/es/wlEsStoreOutAction.do")
public class WlEsStoreOutAction extends BaseAction {
	private WlEsStoreOutService wlEsStoreOutService = (WlEsStoreOutService) Sc.getBean("wl.es.WlEsStoreOutService");
	private WlCmTypeService wlCmTypeService = (WlCmTypeService) Sc.getBean("wl.cm.WlCmTypeService");
	private WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");
	private final static String SYSTEM_NAME = Lang.getString("wl.es.SYSTEM_NAME");
	private final static String MODULE_NAME = Lang.getString("wl.es.wlEsStoreOutAction.MODULE_NAME");
	private final static String MODULE_CODE = "0302003";// 模块代码

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
	 * 查询设备序列号信息
	 * @param currPage 当前页
	 * @param pageSize 页数
	 * @param keyId 字段名
	 * @param keyValue 字段值
	 * @param saleDateStart 销售日期开始
	 * @param saleDateEnd 销售日期结束
	 * @param deliveryDateStart 发货日期开始
	 * @param deliveryDateEnd 发货日期结束
	 * @param isRegFlag 是否已注册
	 * @param receiver 收货人
	 * @param contact 联系方式
	 * @param productName 型号
	 * @return 设备信息
	 */
	@RequestMapping(params = "action=findDataList")
	@ResponseBody
	public String findDataList(int currPage, int pageSize, String keyId, String keyValue, String saleDateStart, String saleDateEnd, String deliveryDateStart, String deliveryDateEnd, String isRegFlag, String receiver, String contact,
			String productName) {
		try {
			Page page = wlEsStoreOutService.search(currPage, pageSize, keyId, keyValue, saleDateStart, saleDateEnd, deliveryDateStart, deliveryDateEnd, isRegFlag, receiver, contact, productName);
			return this.getJson(true, page);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 获取详细信息
	 * @param outId 出库ID
	 * @return 结果
	 */
	@RequestMapping(params = "action=getData")
	@ResponseBody
	public String getData(String outId) {
		try {
			WlEsStoreOut storeOut = (WlEsStoreOut) wlEsStoreOutService.getObject(outId);
			return this.getJson(true, storeOut);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 更新设备序列号
	 * @param storeOut 设备序列号对象
	 * @param user 当前登录用户
	 * @return 空
	 */
	@RequestMapping(params = "action=updateData")
	@ResponseBody
	public String updateData(WlEsStoreOut storeOut, User user) {
		try {
			storeOut.setModifier(user.getName());
			storeOut.setModifyTime(new Date());
			wlEsStoreOutService.saveObject(storeOut);
			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 保存上传数据
	 * @param path 上传文件ID
	 * @return 结果
	 */
	@RequestMapping(params = "action=saveData")
	@ResponseBody
	public String saveData(String path) {
		try {
			wlEsStoreOutService.saveData(path);
			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 导出数据
	 * @param keyId 字段名
	 * @param keyValue 字段值
	 * @param saleDateStart 销售日期开始
	 * @param saleDateEnd 销售日期结束
	 * @param deliveryDateStart 发货日期开始
	 * @param deliveryDateEnd 发货日期结束
	 * @param receiver 收货人
	 * @param contact 联系方式
	 * @param productName 型号
	 * @param response 响应
	 */
	@RequestMapping(params = "action=exportDataList")
	public void exportDataList(String keyId, String keyValue, String saleDateStart, String saleDateEnd, String deliveryDateStart, String deliveryDateEnd, String receiver, String contact, String productName, HttpServletResponse response) {
		try {
			// 前端加码，后端解码
			if (!ValidateUtil.isEmpty(receiver)) {
				receiver = java.net.URLDecoder.decode(receiver, "utf-8");
			}
			if (!ValidateUtil.isEmpty(productName)) {
				productName = java.net.URLDecoder.decode(productName, "utf-8");
			}
			// 得到列表数据
			List<WlEsStoreOut> list = wlEsStoreOutService.findOutList(keyId, keyValue, saleDateStart, saleDateEnd, deliveryDateStart, deliveryDateEnd, receiver, contact, productName);
			// 得到表头数据
			List fieldList = ExcelExportUtil.getHeadFieldList("export/wlEsStoreOut.xml");
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