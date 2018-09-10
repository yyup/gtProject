package com.gt.wl.es.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joyone.lang.BaseException;
import org.joyone.service.BaseService;
import org.joyone.spring.Sc;
import org.joyone.sys.Lang;
import org.joyone.util.ConvertUtil;
import org.joyone.util.DateUtil;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Service;

import com.gt.wl.cm.model.WlCmItem;
import com.gt.wl.cm.model.WlCmMember;
import com.gt.wl.cm.model.WlCmOrgUser;
import com.gt.wl.cm.model.WlCmUser;
import com.gt.wl.cm.service.WlCmCommonSetService;
import com.gt.wl.cm.service.WlCmItemService;
import com.gt.wl.cm.service.WlCmMemberService;
import com.gt.wl.cm.service.WlCmOrgUserService;
import com.gt.wl.cm.service.WlCmTypeService;
import com.gt.wl.cm.service.WlCmUserService;
import com.gt.wl.es.dao.WlEsTodoMsgDao;
import com.gt.wl.es.model.WlEsApply;
import com.gt.wl.es.model.WlEsCustRepair;
import com.gt.wl.es.model.WlEsOrder;
import com.gt.wl.es.model.WlEsOrderGood;
import com.gt.wl.es.model.WlEsSerialReg;
import com.gt.wl.es.model.WlEsTodoMsg;
import com.gt.wl.util.MailUtil;
import com.gt.wl.util.Type;
import com.gt.wl.wm.model.WlWmInventory;
import com.gt.wl.wm.model.WlWmNoticeCheck;
import com.gt.wl.wm.model.WlWmNoticeIn;
import com.gt.wl.wm.model.WlWmNoticeOut;
import com.gt.wl.wm.service.WlWmInventoryService;
import com.gt.wl.wm.service.WlWmNoticeCheckService;
import com.gt.wl.wm.service.WlWmNoticeInService;
import com.gt.wl.wm.service.WlWmNoticeOutService;

/**
 * 待办消息service层
 * @author liuyj
 * 
 */
@Service("wl.es.WlEsTodoMsgService")
public class WlEsTodoMsgService extends BaseService {
	private WlEsTodoMsgDao wlEsTodoMsgDao = (WlEsTodoMsgDao) Sc.getBean("wl.es.WlEsTodoMsgDao");
	private WlCmTypeService wlCmTypeService = (WlCmTypeService) Sc.getBean("wl.cm.WlCmTypeService");
	private WlCmMemberService wlCmMemberService = (WlCmMemberService) Sc.getBean("wl.cm.WlCmMemberService");
	private WlEsApplyService wlEsApplyService = (WlEsApplyService) Sc.getBean("wl.es.WlEsApplyService");
	private WlEsCustRepairService wlEsCustRepairService = (WlEsCustRepairService) Sc.getBean("wl.es.WlEsCustRepairService");
	private WlCmCommonSetService wlCmCommonSetService = (WlCmCommonSetService) Sc.getBean("wl.cm.WlCmCommonSetService");
	private WlCmUserService wlCmUserService = (WlCmUserService) Sc.getBean("wl.cm.WlCmUserService");
	private WlWmNoticeInService wlWmNoticeInService = (WlWmNoticeInService) Sc.getBean("wl.wm.WlWmNoticeInService");
	private WlWmNoticeOutService wlWmNoticeOutService = (WlWmNoticeOutService) Sc.getBean("wl.wm.WlWmNoticeOutService");
	private WlWmNoticeCheckService wlWmNoticeCheckService = (WlWmNoticeCheckService) Sc.getBean("wl.wm.WlWmNoticeCheckService");
	private WlCmOrgUserService wlCmOrgUserService = (WlCmOrgUserService) Sc.getBean("wl.cm.WlCmOrgUserService");
	private WlWmInventoryService wlWmInventoryService = (WlWmInventoryService) Sc.getBean("wl.wm.WlWmInventoryService");
	private WlCmItemService wlCmItemService = (WlCmItemService) Sc.getBean("wl.cm.WlCmItemService");
	private MailUtil mailUtil = new MailUtil();

	public WlEsTodoMsgService() {
		baseDao = wlEsTodoMsgDao;
	}

	/**
	 * 
	 * @param objectId 对象ID
	 * @param objectTypeEk 对象类型 ORDER-订单, APPLY-配件申请REPAIR-返修,SERIAL-序列号登记（有审核）,NOTICEIN-入库通知单,NOTICEOUT-出库通知单,NOTICECHECK盘点通知单SERIALREG(沃特)
	 *            INVENTORYNOTICE- 库存不足通知邮箱
	 */
	public void saveToDoMsg(String objectId, String objectTypeEk) {
		try {
			WlEsTodoMsg wlEsTodoMsg = new WlEsTodoMsg();
			wlEsTodoMsg.setObjectId(objectId);
			wlEsTodoMsg.setObjectTypeEk(objectTypeEk);
			wlEsTodoMsg.setMailSend("0");
			wlEsTodoMsg.setSmsSend("0");
			wlEsTodoMsg.setCreator(Lang.getString("wl.es.WlEsTodoMsgService.systemor"));
			this.saveObject(wlEsTodoMsg);

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据对象id查找待发送消息
	 * @param objectId 对象id
	 * @return 待发送消息对象
	 */
	public WlEsTodoMsg getWlEsTodoMsg(String objectId) {
		try {
			return wlEsTodoMsgDao.getWlEsTodoMsg(objectId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/***
	 * 将待发送的信息发送到指定邮箱并且更新为已发
	 */
	public void updateSendToDoMsg() {
		try {
			Map inventoryMap = new HashMap();// 用于暂存库存不足待发送的信息
			List<String> inventoryTodoList = new ArrayList();// 用于存放库存不足的待发送消息的主键id
			String content = "";
			String[] orderNoticeMails = wlCmCommonSetService.getWlCmCommonSetByKey("ORDER_NOTICE_MAIL").getSetValue().split(",");
			String[] applyNoticeMails = wlCmCommonSetService.getWlCmCommonSetByKey("APPLY_NOTICE_MAIL").getSetValue().split(",");
			String[] repairNoticeMails = wlCmCommonSetService.getWlCmCommonSetByKey("REPAIR_NOTICE_MAIL").getSetValue().split(",");
			String[] noticeCreateMails = wlCmCommonSetService.getWlCmCommonSetByKey("NOTICE_CREATE_MAIL").getSetValue().split(",");
			String[] serialNoticeMails = wlCmCommonSetService.getWlCmCommonSetByKey("SERIAL_NOTICE_MAIL").getSetValue().split(",");
			String[] inventoryLockNoticeMails = wlCmCommonSetService.getWlCmCommonSetByKey("INVENTORY_LACK_NOTICE_MAIL").getSetValue().split(",");

			List<WlEsTodoMsg> list = wlEsTodoMsgDao.findDataList();
			for (WlEsTodoMsg wlEsTodoMsg : list) {
				if (ValidateUtil.isEmpty(wlEsTodoMsg.getObjectId())) {// 如果objectId为空，跳过
					continue;
				}
				if ("ORDER".equals(wlEsTodoMsg.getObjectTypeEk())) {// 订单
					content = this.getOrderMsg(wlEsTodoMsg.getObjectId());
					for (String orderNoticeMail : orderNoticeMails) {
						if (!ValidateUtil.isEmpty(orderNoticeMail)) {
							mailUtil.sendHtmlEmail(orderNoticeMail, Lang.getString("wl.es.WlEsTodoMsgService.ordersubJect"), content);
							// 修改邮箱为已发送
							wlEsTodoMsg.setMailSend("1");
						}
					}
					this.updateObject(wlEsTodoMsg);
				}
				else if ("APPLY".equals(wlEsTodoMsg.getObjectTypeEk())) {// 配件申请
					content = this.getApplyMsg(wlEsTodoMsg.getObjectId());
					for (String applyNoticeMail : applyNoticeMails) {
						if (!ValidateUtil.isEmpty(applyNoticeMail) && !ValidateUtil.isEmpty(content)) {
							mailUtil.sendHtmlEmail(applyNoticeMail, Lang.getString("wl.es.WlEsTodoMsgService.applySubJect"), content);
							// 修改邮箱为已发送
							wlEsTodoMsg.setMailSend("1");
						}
					}
					this.updateObject(wlEsTodoMsg);
				}
				else if ("REPAIR".equals(wlEsTodoMsg.getObjectTypeEk())) {// 返修
					content = this.getRepairMsg(wlEsTodoMsg.getObjectId());//
					for (String repairNoticeMail : repairNoticeMails) {
						if (!ValidateUtil.isEmpty(repairNoticeMail)) {
							mailUtil.sendHtmlEmail(repairNoticeMail, Lang.getString("wl.es.WlEsTodoMsgService.repairSubJect"), content);
							// 修改邮箱为已发送
							wlEsTodoMsg.setMailSend("1");
						}
					}
					this.updateObject(wlEsTodoMsg);
				}
				else if ("SERIAL".equals(wlEsTodoMsg.getObjectTypeEk())) {// 序列号登记
					WlEsSerialRegService wlEsSerialRegService = (WlEsSerialRegService) Sc.getBean("wl.es.WlEsSerialRegService");
					WlEsSerialReg wlEsSerialReg = (WlEsSerialReg) wlEsSerialRegService.getObject(wlEsTodoMsg.getObjectId());
					WlCmMember wlCmMember = (WlCmMember) wlCmMemberService.getObject(wlEsSerialReg.getMemberId());
					content = this.getSerialMsg(wlEsSerialReg.getCreator(), DateUtil.dateToShortDateStr(wlEsSerialReg.getRegDate()));//
					mailUtil.sendHtmlEmail(wlCmMember.getEmail(), Lang.getString("wl.es.WlEsTodoMsgService.serialSubJect"), content);
					// 修改邮箱为已发送
					wlEsTodoMsg.setMailSend("1");
					this.updateObject(wlEsTodoMsg);
				}
				else if ("SERIALREG".equals(wlEsTodoMsg.getObjectTypeEk())) {// 发送给沃特
					if (!ValidateUtil.isEmpty(wlEsTodoMsg.getObjectId())) {
						content = this.getSerialRegMsg(wlEsTodoMsg.getObjectId());
						for (String serialNoticeMail : serialNoticeMails) {
							if (!ValidateUtil.isEmpty(serialNoticeMail)) {
								mailUtil.sendHtmlEmail(serialNoticeMail, Lang.getString("wl.es.WlEsTodoMsgService.serialSubJect"), content);
								// 修改邮箱为已发送
								wlEsTodoMsg.setMailSend("1");
							}
						}
						this.updateObject(wlEsTodoMsg);
					}
				}
				// NOTICEIN-入库通知单,NOTICEOUT-出库通知单,NOTICECHECK盘点通知单
				else if ("NOTICEIN".equals(wlEsTodoMsg.getObjectTypeEk())) {// 入库通知单
					String subJect = this.getNoticeInMsg(wlEsTodoMsg.getObjectId());
					for (String noticeCreateMail : noticeCreateMails) {
						if (!ValidateUtil.isEmpty(noticeCreateMail) && !ValidateUtil.isEmpty(subJect)) {
							mailUtil.sendSimpleEmail(noticeCreateMail, subJect, Lang.getString("wl.es.WlEsTodoMsgService.comeFrom"));
							// 修改邮箱为已发送
							wlEsTodoMsg.setMailSend("1");
						}
					}
					this.updateObject(wlEsTodoMsg);
				}
				else if ("NOTICEOUT".equals(wlEsTodoMsg.getObjectTypeEk())) {// 出库通知单
					String subJect = this.getNoticeOutMsg(wlEsTodoMsg.getObjectId());
					for (String noticeCreateMail : noticeCreateMails) {
						if (!ValidateUtil.isEmpty(noticeCreateMail) && !ValidateUtil.isEmpty(subJect)) {
							mailUtil.sendSimpleEmail(noticeCreateMail, subJect, Lang.getString("wl.es.WlEsTodoMsgService.comeFrom"));
							// 修改邮箱为已发送
							wlEsTodoMsg.setMailSend("1");
						}
					}
					this.updateObject(wlEsTodoMsg);
				}
				else if ("NOTICECHECK".equals(wlEsTodoMsg.getObjectTypeEk())) {// 盘点通知单
					String subJect = this.getNoticeCheckMsg(wlEsTodoMsg.getObjectId());
					for (String noticeCreateMail : noticeCreateMails) {
						if (!ValidateUtil.isEmpty(noticeCreateMail) && !ValidateUtil.isEmpty(subJect)) {
							mailUtil.sendSimpleEmail(noticeCreateMail, subJect, Lang.getString("wl.es.WlEsTodoMsgService.comeFrom"));
							// 修改邮箱为已发送
							wlEsTodoMsg.setMailSend("1");
						}
					}
					this.updateObject(wlEsTodoMsg);
				}
				else if ("INVENTORYNOTICE".equals(wlEsTodoMsg.getObjectTypeEk())) {// 库存不足(待发送消息保存的时候已经过滤掉非沃特仓库)
					inventoryTodoList.add(wlEsTodoMsg.getMsgId());// 添加待发送消息主键id
					WlWmInventory wlWmInventory = (WlWmInventory) wlWmInventoryService.getObject(wlEsTodoMsg.getObjectId());
					if (inventoryMap.containsKey(wlWmInventory.getStorageName())) {// map里面已存在该仓库记录
						List<String> invenList = (List<String>) inventoryMap.get(wlWmInventory.getStorageName());
						invenList.add(wlWmInventory.getInventoryId());
						inventoryMap.put(wlWmInventory.getStorageName(), invenList);
					}
					else {// map里面还不存在该仓库记录
						List<String> invenList = new ArrayList();
						invenList.add(wlWmInventory.getInventoryId());
						inventoryMap.put(wlWmInventory.getStorageName(), invenList);
					}
				}
				// 修改邮箱为已发送
				// wlEsTodoMsg.setMailSend("1");
				// this.updateObject(wlEsTodoMsg);
			}
			// 发送库存不足邮件信息
			if (!ValidateUtil.isEmpty(inventoryMap)) {
				content = this.getInventoryLockNoticeMail(inventoryMap);
				for (String inventoryLockNoticeMail : inventoryLockNoticeMails) {
					if (!ValidateUtil.isEmpty(inventoryLockNoticeMail)) {// 发送邮件
						mailUtil.sendHtmlEmail(inventoryLockNoticeMail, Lang.getString("wl.es.WlEsTodoMsgService.inventorySubJect"), content);
					}
				}
				String[] array = (String[]) inventoryTodoList.toArray(new String[inventoryTodoList.size()]);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				// 取得当前时间
				Date curuentDate = new Date();
				String curDate = sdf.format(curuentDate);
				String msgIds = ConvertUtil.toDbString(array);
				wlEsTodoMsgDao.updateTodoMsgState("1", curDate, msgIds);
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 发送待发送订单信息
	 * @param orderId 订单id
	 * @return 返回订单界面模板
	 */
	public String getOrderMsg(String orderId) {
		try {
			WlEsOrderService wlEsOrderService = (WlEsOrderService) Sc.getBean("wl.es.WlEsOrderService");
			// 准备数据
			WlEsOrder wlEsOrder = (WlEsOrder) wlEsOrderService.getObject(orderId);// 订单
			List<WlEsOrderGood> wlpEsOrderGoodList = wlEsOrderService.findOrderGoodList(orderId);// 商品列表
			// wlEsOrder.setGoodList(wlpEsOrderGoodList);
			List<Type> orderStateEkList = wlCmTypeService.findByCode("ORDER_STATE_EK");
			for (Type type : orderStateEkList) {
				if (type.getId().equals(wlEsOrder.getOrderStateEk())) {
					wlEsOrder.setOrderStateEkValue(type.getLable());
					break;
				}
			}
			String payModeEValue = "";
			String account = "";
			String name = "";
			if ("3".equals(wlEsOrder.getBuyChannelEk())) {// 如果是官网购买
				List<Type> payModeEkList = wlCmTypeService.findByCode("PAY_MODE_EK");
				for (Type type : payModeEkList) {
					if (type.getId().equals(wlEsOrder.getPayModeEk())) {
						payModeEValue = type.getLable();
						break;
					}
				}
				WlCmMember wlCmMember = (WlCmMember) wlCmMemberService.getObject(wlEsOrder.getMemberId());
				account = wlCmMember.getAccount();
				name = wlCmMember.getName();
			}
			// 构造订单界面开始
			String content = "";
			content += "<div align='left'>";
			content += "<table width='60%' cellspacing='0' cellpadding='0' border='0' >";
			content += "<tbody>";
			content += "<tr height='25'>";
			content += "<td  nowrap>" + Lang.getString("wl.es.WlEsTodoMsgService.orderNo") + "</td>";
			content += "<td width='230'>" + wlEsOrder.getOrderNo() + "</td>";
			content += "</tr>";
			content += "<tr height='25'>";
			content += "<td nowrap>" + Lang.getString("wl.es.WlEsTodoMsgService.orderTime") + "</td>";
			content += "<td>" + wlEsOrder.getOrderTime() + "</td>";
			content += "</tr>";
			content += "<tr height='25'>";
			content += "<td nowrap>" + Lang.getString("wl.es.WlEsTodoMsgService.account") + "</td>";
			content += "<td>" + account + "</td>";
			content += "</tr>";
			content += "<tr height='25'>";
			content += "<td nowrap>" + Lang.getString("wl.es.WlEsTodoMsgService.name") + "</td>";
			content += "<td>" + name + "</td>";
			content += "</tr>";
			content += "<tr height='25'>";
			content += "<td nowrap>" + Lang.getString("wl.es.WlEsTodoMsgService.orderStateEkValue") + "</td>";
			content += "<td>" + wlEsOrder.getOrderStateEkValue() + "</td>";
			content += "</tr>";
			content += "<tr height='25' >";
			content += "<td style='border-bottom:1px solid #000;' nowrap>" + Lang.getString("wl.es.WlEsTodoMsgService.payModeEK") + "</td>";
			content += "<td style='border-bottom:1px solid #000;'>" + payModeEValue + "</td>";
			content += "</tr>";
			content += "<tr >";
			content += "<td  style='border-bottom:1px solid #000;' nowrap>" + Lang.getString("wl.es.WlEsTodoMsgService.orderInfo") + "</td>";
			content += "<td width='640px' style='padding:5px 5px;border-bottom:1px solid #000;' colspan='3'>";
			content += " <div >";
			content += "<table  >";
			content += " <colgroup>";
			content += "<col style='width:25%;' ><col style='width:25%;'>";
			content += " <col style='width:25%;'><col style='width:25%;'>";
			content += "  </colgroup>";
			content += "<tbody >";
			content += "<tr align='center'>";
			content += "<th >" + Lang.getString("wl.es.WlEsTodoMsgService.productName") + "</th>";
			content += " <th >" + Lang.getString("wl.es.WlEsTodoMsgService.price") + "</th>";
			content += " <th>" + Lang.getString("wl.es.WlEsTodoMsgService.num") + "</th>";
			content += " <th>" + Lang.getString("wl.es.WlEsTodoMsgService.unitName") + "</th>";
			content += "</tr>";
			for (WlEsOrderGood wlEsOrderGood : wlpEsOrderGoodList) {
				content += "<tr align='center'  >";
				content += "<td>" + wlEsOrderGood.getProductName() + "</td>";
				content += "<td>" + wlEsOrderGood.getPrice() + "</td>";
				content += "<td >" + wlEsOrderGood.getNum() + "</td>";
				content += "<td>" + wlEsOrderGood.getUnitName() + "</td>";
				content += "</tr>";
			}
			content += "</tbody>";
			content += "</table>";
			content += "</div>";
			content += "</td>";
			content += "</tr>";
			content += "<tr height='40'>";
			content += "  <td valign='bottom' nowrap>" + Lang.getString("wl.es.WlEsTodoMsgService.receiver") + "</td>";
			content += "<td valign='bottom'>" + wlEsOrder.getReceiver() + "</td>";
			content += "</tr>";
			content += "<tr height='25'>";
			content += "  <td nowrap>" + Lang.getString("wl.es.WlEsTodoMsgService.mobiel") + "</td>";
			content += "<td>" + wlEsOrder.getMobile() + "</td>";
			content += "</tr>";
			content += "<tr height='40'>";
			content += "   <td style='border-bottom:1px solid #000;'valign='top' nowrap>" + Lang.getString("wl.es.WlEsTodoMsgService.addr") + "</td>";
			content += "<td style='border-bottom:1px solid #000;' valign='top'>" + wlEsOrder.getProvince() + " " + wlEsOrder.getCity() + " " + wlEsOrder.getAddr() + "</td>";
			content += "</tr>";
			content += "<tr height='50' >";
			content += "<td width='100px'  nowrap>" + Lang.getString("wl.es.WlEsTodoMsgService.buyMsg") + "</td>";
			content += "<td width='560px' colspan='3'  >" + wlEsOrder.getBuyMsg() + "</td>";
			content += "</tr>";
			content += "</tbody>";
			content += "</table>";
			content += "</div>";
			// 构造订单界面结束
			return content;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/***
	 * 待发送净水片申请信息
	 * @param applyId 净水片申请id
	 * @return 净水片申请界面模板
	 */
	public String getApplyMsg(String applyId) {
		try {
			// 准备数据
			boolean isSend = false;
			String content = "";
			WlEsOrderService wlEsOrderService = (WlEsOrderService) Sc.getBean("wl.es.WlEsOrderService");
			WlEsApply wlEsApply = (WlEsApply) wlEsApplyService.getObject(applyId);// 获取配件申请对象
			WlEsOrder wlEsOrder = wlEsOrderService.getWlEsOrderByOrderNo(wlEsApply.getOrderNo());// 获取订单对象订单
			if ("0".equals(wlEsApply.getPayModeEk())) {// 如果货到付款
				isSend = true;
			}
			else if ("1".equals(wlEsApply.getPayModeEk())) {// 如果在线付款
				if (!ValidateUtil.isNull(wlEsOrder)) {
					if ("1".equals(wlEsOrder.getOrderStateEk()) || "2".equals(wlEsOrder.getOrderStateEk())) {// 如果是已支付或已发货,则发送
						isSend = true;
					}
				}
			}
			if (isSend) {
				List<Type> machineTypeEkList = wlCmTypeService.findByCode("MACHINE_TYPE_EK");
				for (Type type : machineTypeEkList) {
					if (type.getId().equals(wlEsApply.getMachineTypeEk())) {
						wlEsApply.setMachineTypeEkValue(type.getLable());
						break;
					}
				}
				List<Type> payModeEkList = wlCmTypeService.findByCode("PAY_MODE_EK");
				for (Type type : payModeEkList) {
					if (type.getId().equals(wlEsApply.getPayModeEk())) {
						wlEsApply.setPayMoreEkValue(type.getLable());
						break;
					}
				}
				// 构造净水片界面
				content += "<div align='left'>";
				content += "<table width='60%' cellspacing='0' cellpadding='0' border='0' >";
				content += "<tbody>";
				content += "<tr height='25'>";
				content += "<td nowrap>" + Lang.getString("wl.es.WlEsTodoMsgService.DeviceCd") + "</td>";
				content += "<td width='230'>" + wlEsApply.getDeviceCd() + "</td>	";
				content += "</tr> ";
				content += "<tr height='25'>";
				content += "<td nowrap>" + Lang.getString("wl.es.WlEsTodoMsgService.MachineTypeEkValue") + "</td>";
				content += "<td>" + wlEsApply.getMachineTypeEkValue() + "</td>";
				content += "</tr> 	";
				content += "<tr height='25'>";
				content += "<td nowrap>" + Lang.getString("wl.es.WlEsTodoMsgService.account") + "</td>";
				content += "<td>" + wlEsApply.getAccount() + "</td>";
				content += "</tr>";
				content += "<tr height='25'>";
				content += "<td nowrap>" + Lang.getString("wl.es.WlEsTodoMsgService.applyName") + "</td>";
				content += "<td>" + wlEsApply.getName() + "</td>";
				content += "</tr> ";
				content += "<tr height='25'>";
				content += "<td nowrap>" + Lang.getString("wl.es.WlEsTodoMsgService.payModeEkValue") + "</td>";
				content += "<td>" + wlEsApply.getPayModeEkValue() + "</td>";
				content += "</tr>";
				content += "<tr height='25' >";
				content += "<td style='border-bottom:1px solid #000;' nowrap>" + Lang.getString("wl.es.WlEsTodoMsgService.applyTime") + "</td>";
				content += "<td style='border-bottom:1px solid #000;'>" + wlEsApply.getApplyTime() + "</td>";
				content += "</tr>";
				content += "<tr height='40'>";
				content += "<td  valign='bottom'  nowrap>" + Lang.getString("wl.es.WlEsTodoMsgService.receiver") + "</td>";
				content += "<td valign='bottom'>" + wlEsApply.getReceiver() + "</td>";
				content += "</tr>";
				content += "<tr height='25' nowrap>";
				content += "  <td>" + Lang.getString("wl.es.WlEsTodoMsgService.mobiel") + "</td>";
				content += "<td>" + wlEsApply.getMobile() + "</td>";
				content += "</tr>";
				content += "<tr height='25' nowrap>";
				content += "<td >" + Lang.getString("wl.es.WlEsTodoMsgService.addr") + "</td>";
				content += "<td>" + wlEsApply.getProvince() + " " + wlEsApply.getCity() + " " + wlEsApply.getAddr() + "</td>";
				content += "</tr>";
				content += "</tbody>";
				content += "</table>";
				content += "</div>";
			}
			return content;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 待发送售后维修信息
	 * @param repairId 售后维修id
	 * @return 售后维修信息模板
	 */
	public String getRepairMsg(String repairId) {
		try {
			// 准备数据
			WlEsCustRepair wlEsCustRepair = (WlEsCustRepair) wlEsCustRepairService.getObject(repairId);
			WlCmMember wlCmMember = (WlCmMember) wlCmMemberService.getObject(wlEsCustRepair.getMemberId());
			WlCmUser wlCmUser = (WlCmUser) wlCmUserService.getObject(wlEsCustRepair.getMemberId());
			WlCmOrgUser wlCmOrgUser = (WlCmOrgUser) wlCmOrgUserService.getObject(wlEsCustRepair.getMemberId());

			List<Type> moduleEkList = wlCmTypeService.findByCode("MODULE_EK");
			List<Type> serialVerfEkList = wlCmTypeService.findByCode("SERIAL_VERF_EK");
			List<Type> processStatusEkList = wlCmTypeService.findByCode("PROCESS_STATUS_EK");

			for (Type type : moduleEkList) {
				if (type.getId().equals(wlEsCustRepair.getModuleEk())) {
					wlEsCustRepair.setModuleEkValue(type.getLable());
					break;
				}
			}
			for (Type type : serialVerfEkList) {
				if (type.getId().equals(wlEsCustRepair.getSerialVerfEk())) {
					wlEsCustRepair.setSerialVerfEkValue(type.getLable());
					break;
				}
			}
			String processStatusEk = "";
			for (Type type : processStatusEkList) {
				if (type.getId().equals(wlEsCustRepair.getProcessStatusEk())) {
					processStatusEk = type.getLable();
					break;
				}
			}
			String account = "";
			String name = "";
			if (!ValidateUtil.isNull(wlCmMember)) {
				account = wlCmMember.getAccount();
				name = wlCmMember.getName();
			}
			else if (!ValidateUtil.isNull(wlCmUser)) {
				account = wlCmUser.getLoginName();
				name = wlCmUser.getName();
			}
			else if (!ValidateUtil.isNull(wlCmOrgUser)) {
				account = wlCmOrgUser.getAccount();
				name = wlCmOrgUser.getName();
			}
			// 构造售后维修界面
			String content = "";
			content += "<div align='left'>";
			content += "<table width='60%' cellspacing='0' cellpadding='0' border='0' >";
			content += "<tbody>";
			content += "<tr height='25'>";
			content += "<td  nowrap>" + Lang.getString("wl.es.WlEsTodoMsgService.repairNo") + "</td>";
			content += "<td width='230'>" + wlEsCustRepair.getRepairNo() + "</td>";
			content += "</tr>";
			content += "<tr height='25'>";
			content += "<td nowrap>" + Lang.getString("wl.es.WlEsTodoMsgService.applyTime") + "</td>";
			content += "<td>" + wlEsCustRepair.getApplyTime() + "</td>";
			content += "</tr>";
			content += "<tr height='25'>	";
			content += "<td nowrap>" + Lang.getString("wl.es.WlEsTodoMsgService.account") + "</td>";
			content += "<td>" + account + "</td>";
			content += "</tr>";
			content += "<tr height='25'>";
			content += "<td nowrap>" + Lang.getString("wl.es.WlEsTodoMsgService.name") + "</td>";
			content += "<td>" + name + "</td>";
			content += "</tr> ";
			content += "<tr height='25'>	";
			content += "<td nowrap>" + Lang.getString("wl.es.WlEsTodoMsgService.moduleEk") + "</td>";
			content += "<td>" + wlEsCustRepair.getModuleEkValue() + "</td>";
			content += "</tr>";
			content += "<tr height='25'>";
			content += " <td nowrap>" + Lang.getString("wl.es.WlEsTodoMsgService.deviceCd") + "</td>";
			content += "<td>" + wlEsCustRepair.getDeviceCd() + "</td>";
			content += "</tr>";
			content += "<tr height='25' >";
			content += "<td nowrap>" + Lang.getString("wl.es.WlEsTodoMsgService.contactPerson") + "</td>";
			content += "<td>" + wlEsCustRepair.getReceiver() + "</td>";
			content += "</tr>";
			content += "<tr height='25' >";
			content += "<td >" + Lang.getString("wl.es.WlEsTodoMsgService.contact") + "</td>";
			content += "<td>" + wlEsCustRepair.getMobile() + "</td>";
			content += "</tr>";
			content += "<tr height='25' >";
			content += "<td nowrap>" + Lang.getString("wl.es.WlEsTodoMsgService.serialVerfEk") + "</td>";
			content += "<td>" + wlEsCustRepair.getSerialVerfEkValue() + "</td>";
			content += "</tr>";
			content += "<tr height='25' >";
			content += "<td nowrap>" + Lang.getString("wl.es.WlEsTodoMsgService.processStatusEk") + "</td>";
			content += "<td>" + processStatusEk + "</td>";
			content += "</tr>";
			content += "<tr height='50' >";
			content += "<td width='100' style='border-top:1px solid #000;' nowrap>问题描述</td>";
			content += "<td width='560' colspan='3' style='border-top:1px solid #000;' >" + wlEsCustRepair.getDescription() + "</td>";
			content += "</tr>";
			content += "</tbody>";
			content += "</table>";
			content += "</div>";
			return content;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 待发送序列号注册成功信息
	 * @param account 帐户
	 * @param regDate 登记日期
	 * @return 审核通过信息模板
	 */
	public String getSerialMsg(String account, String regDate) {
		try {
			// 构造序列号注册成功界面
			String content = "";
			content += "<div align='left'>";
			content += "<table width='60%' cellspacing='0' cellpadding='0' border='0' >";
			content += "<tbody>";
			content += "<tr height='25'>";
			content += "<td  nowrap>尊敬的" + account + "，您好！</td>";
			content += "</tr>";
			content += "<tr height='25'>";
			content += "<td width='230'>感谢您对沃特罗伦的支持，您于" + regDate + "在官网</td>";
			content += "</tr>";
			content += "<tr height='25'>";
			content += "<td nowrap><a href='http://www.waterrower.com.cn'>http://www.waterrower.com.cn</a>申请的序列号注册已成功通过审核，</td>";
			content += "</tr>";
			content += "<tr height='25'>	";
			content += "<td nowrap>更多功能期待您的探索，如有疑问请致电：400-600-4546</td>";
			content += "</tr>";
			content += "<tr height='50'>";
			content += "<td nowrap>&nbsp;</td>";
			content += "</tr> ";
			content += "<tr height='25'>";
			content += "<td nowrap align='right'>沃特罗伦中国</td>";
			content += "</tr> ";
			content += "</tbody>";
			content += "</table>";
			content += "</div>";
			return content;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/***
	 * 获取待发送序列号信息模板（沃特）
	 * @param regId 序列号注册id
	 * @return 待发送序列号信息模板（沃特）
	 */
	public String getSerialRegMsg(String regId) {
		try {
			String content = "";
			WlEsSerialRegService wlEsSerialRegService = (WlEsSerialRegService) Sc.getBean("wl.es.WlEsSerialRegService");
			WlEsSerialReg wlEsSerialReg = (WlEsSerialReg) wlEsSerialRegService.getObject(regId);
			List<Type> buyChannelEkList = wlCmTypeService.findByCode("BUY_CHANNEL_EK");
			List<Type> auditStateList = wlCmTypeService.findByCode("AUDIT_STATE");
			for (Type type : buyChannelEkList) {
				if (type.getId().equals(wlEsSerialReg.getBuyChannelEk())) {
					wlEsSerialReg.setBuyChannelEkValue(type.getLable());
					break;
				}
			}
			for (Type type : auditStateList) {
				if (type.getId().equals(wlEsSerialReg.getAuditState())) {
					wlEsSerialReg.setAuditStateValue(type.getLable());
					break;
				}
			}
			// 构造序列号登记界面
			content += "<div align='left'>";
			content += "<table width='60%' cellspacing='0' cellpadding='0' border='0' >";
			content += "<tbody>";
			content += "<tr height='25'>";
			content += "<td nowrap>" + Lang.getString("wl.es.WlEsTodoMsgService.model") + "</td>";
			content += "<td width='230'>" + wlEsSerialReg.getProductName() + "</td>	";
			content += "</tr> ";
			content += "<tr height='25'>";
			content += "<td nowrap>" + Lang.getString("wl.es.WlEsTodoMsgService.buyChannelEk") + "</td>";
			content += "<td>" + wlEsSerialReg.getBuyChannelEkValue() + "</td>";
			content += "</tr> 	";
			content += "<tr height='25'>";
			content += "<td nowrap>" + Lang.getString("wl.es.WlEsTodoMsgService.agency") + "</td>";
			content += "<td>" + wlEsSerialReg.getAgency() + "</td>";
			content += "</tr>";
			content += "<tr height='25'>";
			content += "<td nowrap>" + Lang.getString("wl.es.WlEsTodoMsgService.buyDate") + "</td>";
			content += "<td>" + wlEsSerialReg.getBuyDate() + "</td>";
			content += "</tr> ";
			content += "<tr height='25'>";
			content += "<td nowrap>" + Lang.getString("wl.es.WlEsTodoMsgService.buyPeople") + "</td>";
			content += "<td>" + wlEsSerialReg.getReceiver() + "</td>";
			content += "</tr>";
			content += "<tr height='25' >";
			content += "<td style='border-bottom:1px solid #000;' nowrap>" + Lang.getString("wl.es.WlEsTodoMsgService.contact") + "</td>";
			content += "<td style='border-bottom:1px solid #000;'>" + wlEsSerialReg.getContact() + "</td>";
			content += "</tr>";
			content += "<tr height='25' >";
			content += "<td style='border-bottom:1px solid #000;' nowrap>" + Lang.getString("wl.es.WlEsTodoMsgService.description") + "</td>";
			content += "<td style='border-bottom:1px solid #000;'>" + wlEsSerialReg.getDescription() + "</td>";
			content += "</tr>";
			content += "<tr height='40'>";
			content += "<td nowrap>" + Lang.getString("wl.es.WlEsTodoMsgService.auditState") + "</td>";
			content += "<td >" + wlEsSerialReg.getAuditStateValue() + "</td>";
			content += "</tr>";
			content += "</tbody>";
			content += "</table>";
			content += "</div>";
			return content;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 待发送入库通知单信息
	 * @param noticeInId 入库通知单id
	 * @return 入库通知单主题
	 */
	public String getNoticeInMsg(String noticeInId) {
		try {
			// 准备数据
			WlWmNoticeIn wlWmNoticeIn = (WlWmNoticeIn) wlWmNoticeInService.getObject(noticeInId);
			// 构造入库通知单待发送内容
			String subJect = "";
			if (!ValidateUtil.isNull(wlWmNoticeIn)) {
				if ("NO_ISSUE".equals(wlWmNoticeIn.getBillStateEk())) {
					subJect += Lang.getString("wl.es.WlEsTodoMsgService.newNoticeInJob") + wlWmNoticeIn.getNoticeNo();
				}
			}
			return subJect;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 待发送出库通知单信息
	 * @param noticeOutId 出库通知单id
	 * @return 出库通知单主题
	 */
	public String getNoticeOutMsg(String noticeOutId) {
		try {
			// 准备数据
			WlWmNoticeOut wlWmNoticeOut = (WlWmNoticeOut) wlWmNoticeOutService.getObject(noticeOutId);
			// 构造入库通知单待发送内容
			String subJect = "";
			if (!ValidateUtil.isNull(wlWmNoticeOut)) {
				if ("NO_ISSUE".equals(wlWmNoticeOut.getBillStateEk())) {
					subJect += Lang.getString("wl.es.WlEsTodoMsgService.newNoticeOutJob") + wlWmNoticeOut.getNoticeNo();
				}
			}
			return subJect;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 待发送盘点通知单信息
	 * @param noticeCheckId 入库通知单id
	 * @return 盘点通知单主题
	 */
	public String getNoticeCheckMsg(String noticeCheckId) {
		try {
			// 准备数据
			WlWmNoticeCheck wlWmNoticeCheck = (WlWmNoticeCheck) wlWmNoticeCheckService.getObject(noticeCheckId);
			// 构造入库通知单待发送内容
			String subJect = "";
			if (!ValidateUtil.isNull(wlWmNoticeCheck)) {
				if ("NO_ISSUE".equals(wlWmNoticeCheck.getBillStateEk())) {
					subJect += Lang.getString("wl.es.WlEsTodoMsgService.newNoticeCheckJob") + wlWmNoticeCheck.getNoticeNo();
				}
			}
			return subJect;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/***
	 * 待发送的库存不足
	 * @param map key是仓库名字，value是此仓库对应的库存id列表
	 * @return 待发送库存不足内容
	 */
	public String getInventoryLockNoticeMail(Map map) {
		try {
			String content = "";
			content += "<div align='left'>";
			content += "<table width='60%' cellspacing='0' cellpadding='0' border='0' >";
			content += "<tbody>";
			content += "<tr height='25'>";
			content += "<td colspan='5'nowrap>库存不足列表如下</td>";
			content += "</tr>";
			for (Object key : map.keySet()) {// 循环仓库
				String storageName = key.toString();
				content += "<tr height='25'>";
				content += "<td style='font-weight:bold;' colspan='5' nowrap>" + storageName + ":</td>";
				content += "</tr>";
				content += "<tr height='25'>";
				content += "<td nowrap>物料编码</td>";
				content += "<td nowrap>型号</td>";
				content += "<td nowrap>当前库存</td>";
				content += "<td nowrap>安全数</td>";
				content += "</tr>";
				List<String> invenList = (List) map.get(key);// 此仓库下的库存id列表
				for (String inventoryId : invenList) {// 循环库存id
					WlWmInventory wlWmInventory = (WlWmInventory) wlWmInventoryService.getObject(inventoryId);
					WlCmItem wlCmItem = (WlCmItem) wlCmItemService.getObject(wlWmInventory.getItemId());
					content += "<tr height='25'>";
					content += "<td >" + wlWmInventory.getItemCd() + "</td>";
					content += "<td>" + wlWmInventory.getSpec() + "</td>";
					content += "<td  style='color:red;' >" + wlWmInventory.getBaseUnitQty() + "</td>";
					content += "<td>" + wlCmItem.getLowerLimit() + "</td>";
					content += "</tr>";
				}
				content += "<tr height='25'>";
				content += "<td colspan='4'>&nbsp;</td>";
				content += "</tr>";
			}
			content += "</tbody>";
			content += "</table>";
			content += "</div>";
			return content;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}
