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
import org.joyone.sys.Page;
import org.joyone.sys.Session;
import org.joyone.sys.User;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gt.wl.cm.model.WlCmItem;
import com.gt.wl.cm.service.WlCmSecurityService;
import com.gt.wl.cm.service.WlCmTypeService;
import com.gt.wl.es.model.WlEsSerialReg;
import com.gt.wl.es.model.WlEsSerialRegPic;
import com.gt.wl.es.model.WlEsStoreOut;
import com.gt.wl.es.service.WlEsSerialRegService;
import com.gt.wl.es.service.WlEsStoreOutService;
import com.gt.wl.es.service.WlEsTodoMsgService;
import com.gt.wl.util.Type;

/**
 * 序列号登记管理Action层
 * @author liuyj
 * 
 */
@Controller
@RequestMapping({ "/wl/es/wlEsSerialRegAction.do", "/wl/es/wlEsSerialRegAction.web" })
public class WlEsSerialRegAction extends BaseAction {
	private WlEsSerialRegService wlEsSerialRegService = (WlEsSerialRegService) Sc.getBean("wl.es.WlEsSerialRegService");
	private WlCmTypeService wlCmTypeService = (WlCmTypeService) Sc.getBean("wl.cm.WlCmTypeService");
	private WlCmSecurityService wlCmSecurityService = (WlCmSecurityService) Sc.getBean("wl.cm.WlCmSecurityService");
	private WlEsTodoMsgService wlEsTodoMsgService = (WlEsTodoMsgService) Sc.getBean("wl.es.WlEsTodoMsgService");
	private WlEsStoreOutService wlEsStoreOutService = (WlEsStoreOutService) Sc.getBean("wl.es.WlEsStoreOutService");

	private final static String SYSTEM_NAME = Lang.getString("wl.es.SYSTEM_NAME");
	private final static String MODULE_NAME = Lang.getString("wl.es.wlEsSerialRegAction.MODULE_NAME");
	private final static String MODULE_CODE = "0302004";// 模块代码

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
	 * 查询序列号登记审核信息（分页）
	 * @param currPage 当前页
	 * @param pageSize 页数
	 * @param keyId 字段名
	 * @param keyValue 字段值
	 * @param auditState 审核标志0-未审核,1-审核通过,2-驳回,3-终审通过
	 * @param regDateStart 登记日期开始
	 * @param regDateEnd 登记日期结束
	 * @param isManualFlag 是否人工审核
	 * @param account 会员账号
	 * @return 序列号登记审核信息
	 */
	@RequestMapping(params = "action=findDataList")
	@ResponseBody
	public String findDataList(int currPage, int pageSize, String keyId, String keyValue, String auditState, String regDateStart, String regDateEnd, String isManualFlag, String account) {
		try {
			if ("memberId".equals(keyId) && ValidateUtil.isNull(keyValue)) {
				keyValue = Session.getCurrUser().getId();
			}
			List<Type> auditStateList = wlCmTypeService.findByCode("AUDIT_STATE");
			List<Type> buyChannelEkList = wlCmTypeService.findByCode("BUY_CHANNEL_EK");
			Page page = wlEsSerialRegService.search(currPage, pageSize, keyId, keyValue, auditState, regDateStart, regDateEnd, isManualFlag, account);
			List<WlEsSerialReg> list = page.getItems();
			for (WlEsSerialReg wlEsSerialReg : list) {
				String tempAuditState = wlEsSerialReg.getAuditState();
				String tempBuyChannelEk = wlEsSerialReg.getBuyChannelEk();
				for (Type type : auditStateList) {
					if (type.getId().equals(tempAuditState)) {
						wlEsSerialReg.setAuditStateValue(type.getLable());
						break;
					}
				}
				for (Type type : buyChannelEkList) {
					if (type.getId().equals(tempBuyChannelEk)) {
						wlEsSerialReg.setBuyChannelEkValue(type.getLable());
						break;
					}
				}

			}
			return this.getJson(true, page);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询设备编码信息信息
	 * @return 设备编码信息
	 */
	@RequestMapping(params = "action=findDeviceCdList")
	@ResponseBody
	public String findDeviceCdList() {
		try {
			User user = Session.getCurrUser();
			List<Map> mapLst = wlEsSerialRegService.findDeviceCdList(user.getId());
			return this.getJson(true, mapLst);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 保存审核信息
	 * @param regId 登记ID
	 * @param auditState 审核状态
	 * @param deviceCd 设备编码(序列号)
	 * @param reason 拒绝原因
	 * @param deliveryDate 发货日期
	 * @return 结果
	 */
	@RequestMapping(params = "action=updateAuditData")
	@ResponseBody
	public String updateAuditData(String regId, String auditState, String deviceCd, String reason, String deliveryDate) {
		try {
			wlEsSerialRegService.updateAuditData(regId, auditState, deviceCd, reason, deliveryDate, Session.getCurrUser().getName());
			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 获取详细信息(含图片列表)
	 * @param regId 登记ID
	 * @return 结果
	 */
	@RequestMapping(params = "action=getData")
	@ResponseBody
	public String getData(String regId) {
		try {
			WlEsSerialReg wlEsSerialReg = (WlEsSerialReg) wlEsSerialRegService.getObject(regId);
			// 获取图片列表
			List<WlEsSerialRegPic> picList = wlEsSerialRegService.findPicList(regId);
			wlEsSerialReg.setPicList(picList);
			return this.getJson(true, wlEsSerialReg);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}

	}

	/**
	 * 获取序列号注册列表
	 * @param paraMap 登记ID
	 * @return 结果
	 */
	@RequestMapping(params = "action=findSerialDataList")
	@ResponseBody
	public String findSerialDataList(ParaMap paraMap) {
		try {
			Map map = paraMap.getMap();
			String addr = "";
			WlEsStoreOut wlEsStoreOut = wlEsStoreOutService.validateDeviceCdData((String) map.get("deviceCd"));
			if (!ValidateUtil.isNull(wlEsStoreOut)) {
				addr = wlEsStoreOut.getAddr();
			}
			Map resultMap = new HashMap();
			List<Map> list = wlEsSerialRegService.findSerialDataList(map);
			if (!ValidateUtil.isEmpty(list)) {
				Map regMap = list.get(0);
				resultMap.put("frameExpDate", regMap.get("frameExpDate"));
				resultMap.put("accExpDate", regMap.get("accExpDate"));
			}
			else {
				resultMap.put("frameExpDate", "");
				resultMap.put("accExpDate", "");
			}
			resultMap.put("addr", addr);
			return this.getJson(true, resultMap);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 保存数据
	 * @param wlEsSerialReg 对象
	 * @param picPathList 图片路径
	 * @return 结果
	 */
	@RequestMapping(params = "action=saveData")
	@ResponseBody
	public String saveData(WlEsSerialReg wlEsSerialReg, String[] picPathList,  @RequestParam(defaultValue="CN")String langType) {
		try {
			User user = Session.getCurrUser();
			Map<String, Object> mapResult = new HashMap<String, Object>();

			// 验证当前序列号是否已经被册注册
			WlEsSerialReg valWlEsSerialReg = wlEsSerialRegService.getWlEsSerialReg(wlEsSerialReg.getDeviceCd(), "1");
			if (!ValidateUtil.isNull(valWlEsSerialReg)) {
				this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.add"), wlEsSerialReg.getDeviceCd() + Lang.getString("wl.es.wlEsSerialRegAction.beRegistered"));
				if ("EN".equals(langType)) {
					mapResult.put("result", Lang.getString("wl.es.wlEsSerialRegAction.beRegisteredEN"));
				} else {
					mapResult.put("result", Lang.getString("wl.es.wlEsSerialRegAction.beRegistered"));
				}
				mapResult.put("type", "3");
				mapResult.put("isSuccess", false);
				return this.getJson(true, mapResult);
			}
			wlEsSerialReg.setRegDate(new Date());
			wlEsSerialReg.setMemberId(user.getId());
			wlEsSerialReg.setAccount(user.getLoginName());
			wlEsSerialReg.setCreateTime(new Date());
			wlEsSerialReg.setCreator(user.getName());
			mapResult = wlEsSerialRegService.saveSerialRegData(wlEsSerialReg, picPathList, langType);
			String result = (String) mapResult.get("result");
			boolean isSuccess = (Boolean) mapResult.get("isSuccess");
			if (isSuccess && ValidateUtil.isEmpty(wlEsSerialReg.getDeviceCd())) {// 如果是成功且未输入序列号，新建序列号登记待发送信息
				wlEsTodoMsgService.saveToDoMsg(wlEsSerialReg.getRegId(), "SERIALREG");
			}

			String tempResult = result;
			String deviceCd = wlEsSerialReg.getDeviceCd();
			if (ValidateUtil.isEmpty(result)) {// 如果返回结果为空是上传图片
				if ("EN".equals(langType)) {
					tempResult = Lang.getString("wl.es.wlEsSerialRegAction.uploadPicEN");
				} else {
					tempResult = Lang.getString("wl.es.wlEsSerialRegAction.uploadPic");
				}
				deviceCd = "";
			}
			// 添加操作日志
			this.addLog(user, SYSTEM_NAME, MODULE_NAME, Lang.getString("wl.add"), deviceCd + tempResult);

			return this.getJson(true, mapResult);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 保存编辑数据
	 * @param wlEsSerialReg 序列号注册对象
	 * @param deliveryDate 发货日期
	 * @param user 当前登录用户
	 * @return 结果
	 */
	@RequestMapping(params = "action=updateData")
	@ResponseBody
	public String updateData(WlEsSerialReg wlEsSerialReg, String deliveryDate, User user) {
		try {
			WlEsSerialReg oldWlEsSerialReg = (WlEsSerialReg) wlEsSerialRegService.getObject(wlEsSerialReg.getRegId());
			wlEsSerialReg.setModifier(user.getName());
			wlEsSerialReg.setModifyTime(new Date());
			wlEsSerialRegService.updateData(wlEsSerialReg, deliveryDate, oldWlEsSerialReg.getDeviceCd());
			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/***
	 * 校验序列号在序列号登记是否存在
	 * @param deviceCd 序列号
	 * @return 序列号登记
	 */
	@RequestMapping(params = "action=validateDeviceCdData")
	@ResponseBody
	public String validateDeviceCdData(String deviceCd) {
		try {
			String result = "";
			// add判断该设备编码在es_store_out中是否存在，如不存在，则插入一条记录
			WlEsStoreOut storeOut = wlEsStoreOutService.validateDeviceCdData(deviceCd);
			if (ValidateUtil.isNull(storeOut)) {// 不存在
				result = "0";
			}
			else {// 存在
				result = "1";
			}
			return this.getJson(true, result);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 删除序列号注册和序列号注册下的图片
	 * @param regId 序列号注册id
	 * @return 空
	 */
	@RequestMapping(params = "action=deleteData")
	@ResponseBody
	public String deleteData(String regId) {
		try {
			wlEsSerialRegService.deleteData(regId);
			return this.getJson(true, "");
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}
	/**
	 * 根据序列号查询物料信息
	 * @param serialNo
	 * @return
	 */
	@RequestMapping(params = "action=getproductName")
	@ResponseBody
	public String getproductName(String serialNo,@RequestParam(defaultValue="CN")String langType) {
		try {
			Map map = wlEsSerialRegService.getproductName(serialNo, langType);
			return this.getJson(true, map);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}
}