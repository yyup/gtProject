package com.gt.wl.es.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.joyone.lang.BaseException;
import org.joyone.service.BaseService;
import org.joyone.spring.Sc;
import org.joyone.sys.Config;
import org.joyone.sys.Lang;
import org.joyone.sys.Page;
import org.joyone.sys.User;
import org.joyone.util.DateUtil;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Service;

import com.gt.wl.cm.model.WlCmDocument;
import com.gt.wl.cm.model.WlCmDocumentPath;
import com.gt.wl.cm.service.WlCmDocumentPathService;
import com.gt.wl.cm.service.WlCmDocumentService;
import com.gt.wl.es.dao.WlEsCustRepairDao;
import com.gt.wl.es.dao.WlEsCustRepairPicDao;
import com.gt.wl.es.dao.WlEsCustRepairSugDao;
import com.gt.wl.es.dao.WlEsOrderGoodDao;
import com.gt.wl.es.model.WlEsCustRepair;
import com.gt.wl.es.model.WlEsCustRepairPic;
import com.gt.wl.es.model.WlEsCustRepairSug;
import com.gt.wl.es.model.WlEsOrder;
import com.gt.wl.es.model.WlEsOrderGood;

/**
 * 售后维修Service层
 * @author liuyj
 * 
 */
@Service("wl.es.WlEsCustRepairService")
public class WlEsCustRepairService extends BaseService {
	private WlEsCustRepairDao wlEsCustRepairDao = (WlEsCustRepairDao) Sc.getBean("wl.es.WlEsCustRepairDao");
	private WlEsCustRepairPicDao wlEsCustRepairPicDao = (WlEsCustRepairPicDao) Sc.getBean("wl.es.WlEsCustRepairPicDao");
	private WlEsCustRepairSugDao wlEsCustRepairSugDao = (WlEsCustRepairSugDao) Sc.getBean("wl.es.WlEsCustRepairSugDao");
	private WlCmDocumentService wlCmDocumentService = (WlCmDocumentService) Sc.getBean("wl.cm.WlCmDocumentService");
	private WlCmDocumentPathService pathService = (WlCmDocumentPathService) Sc.getBean("wl.cm.WlCmDocumentPathService");
	private WlEsOrderGoodDao wlEsOrderGoodDao = (WlEsOrderGoodDao) Sc.getBean("wl.es.WlEsOrderGoodDao");

	public WlEsCustRepairService() {
		baseDao = wlEsCustRepairDao;
	}

	/**
	 * 查询售后维修信息(分页)
	 * @param map 参数
	 * @param currPage 当前页
	 * @param pageSize 页数
	 * @return 售后维修信息
	 */
	public Page search(Map map, int currPage, int pageSize) {
		try {
			return wlEsCustRepairDao.search(map, currPage, pageSize);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 修改处理信息
	 * @param repairId 维修ID
	 * @param isReturnFlag 是否退回维修配件
	 * @param upkeep 维修费
	 * @param freight 运费
	 * @param replyId 回复人ID
	 * @param replyName 回复人
	 * @param reply 回复意见
	 * @param isDelivery 是否发货给客户
	 */
	public void updateHandleData(String repairId, String isReturnFlag, String upkeep, String freight, String replyId, String replyName, String reply, String isDelivery, String langType) {
		try {
			WlEsCustRepair wlEsCustRepair = (WlEsCustRepair) wlEsCustRepairDao.getObject(repairId);
			wlEsCustRepair.setIsSendFlag(isDelivery);
			wlEsCustRepair.setIsReturnFlag(isReturnFlag);
			// 判断是否退回维修配件
			if ("1".equals(isReturnFlag)) {// 是
				wlEsCustRepair.setProcessStatusEk("1");// 处理状态设为待用户返回配件
			}
			else {// 否
					// 判断是否需要发货给客户
				if ("1".equals(isDelivery)) {// 是
					wlEsCustRepair.setProcessStatusEk("3-1");// 处理状态设为待发货
				}
				else {// 否
					wlEsCustRepair.setProcessStatusEk("6");// 处理状态设为完成
				}
			}
			if (!ValidateUtil.isEmpty(upkeep)) {
				wlEsCustRepair.setUpkeep(Double.parseDouble(upkeep));
			}
			if (!ValidateUtil.isEmpty(freight)) {
				wlEsCustRepair.setFreight(Double.parseDouble(freight));
			}

			// 保存维修主表
			wlEsCustRepairDao.updateObject(wlEsCustRepair);
			// 保存维修意见从表
			WlEsCustRepairSug sug = new WlEsCustRepairSug();
			sug.setProcessStatusEk(wlEsCustRepair.getProcessStatusEk());
			sug.setRepairId(wlEsCustRepair.getRepairId());
			// 设置回复人及内容
			sug.setReplyId(replyId);
			sug.setReplyName(replyName);
			if (!ValidateUtil.isEmpty(reply)) {
				reply = reply.replaceAll("\\t", "");
			}
			sug.setReply(reply);
			sug.setReplyTime(new Date());
			wlEsCustRepairSugDao.addObject(sug);
			// 判断是否退回维修配件,是退回，或者不需要退回单但需要发货给客户都生成订单
			if ("1".equals(isReturnFlag) || ("0".equals(isReturnFlag) && "1".equals(isDelivery))) {// 是退回或者不需要退回但需要发货
				double payAmt = wlEsCustRepair.getUpkeep() + wlEsCustRepair.getFreight();
				if (payAmt > 0) {
					WlEsOrderService wlEsOrderService = (WlEsOrderService) Sc.getBean("wl.es.WlEsOrderService");
					WlEsOrder wlEsOrder = new WlEsOrder();
					wlEsOrder.setOrderNo(wlEsOrderService.getNewOrderNO());
					wlEsOrder.setOrderTime(new Date());
					wlEsOrder.setMemberId(wlEsCustRepair.getMemberId());
					wlEsOrder.setReceiver(wlEsCustRepair.getReceiver());
					wlEsOrder.setAddr(wlEsCustRepair.getAddr());
					wlEsOrder.setMobile(wlEsCustRepair.getMobile());
					wlEsOrder.setOrderTime(new Date());
					wlEsOrder.setOrderStateEk("0");
					wlEsOrder.setGoodNum(1);
					wlEsOrder.setPayModeEk("1");// 在线支付

					wlEsOrder.setPayAmt(payAmt);
					wlEsOrder.setAmt(payAmt);

					wlEsOrderService.saveObject(wlEsOrder);// 保存订单
					wlEsCustRepair.setOrderNo(wlEsOrder.getOrderNo());
					// 保存维修主表
					wlEsCustRepairDao.updateObject(wlEsCustRepair);
					WlEsOrderGood good = new WlEsOrderGood();
					good.setOrderId(wlEsOrder.getOrderId());
					if ("EN".equals(langType)) {
						good.setProductName(Lang.getString("wl.es.wlEsCustRepairService.goodNameEN"));
						good.setUnitName(Lang.getString("wl.es.wlEsCustRepairService.unitNameEN"));
					} else {
						good.setProductName(Lang.getString("wl.es.wlEsCustRepairService.goodName"));
						good.setUnitName(Lang.getString("wl.es.wlEsCustRepairService.unitName"));
					}
					good.setNum(1);
					good.setPrice(wlEsOrder.getPayAmt());
					// 保存订单从表
					wlEsOrderGoodDao.saveObject(good);
				}
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 保存维修对象(含图片、视频)
	 * @param wlEsCustRepair 对象
	 * @param picPathList 图片地址
	 * @param videoPathList 图片地址
	 */
	public void saveCustRepair(WlEsCustRepair wlEsCustRepair, String[] picPathList, String[] videoPathList) {
		try {
			saveObject(wlEsCustRepair);
			// 删除图片和视频
			wlEsCustRepairPicDao.deleteData(wlEsCustRepair.getRepairId(), "");
			// 保存图片
			if (!ValidateUtil.isEmpty(picPathList)) {
				for (String picParh : picPathList) {
					WlEsCustRepairPic wlEsCustRepairPic = new WlEsCustRepairPic();
					wlEsCustRepairPic.setRepairId(wlEsCustRepair.getRepairId());
					wlEsCustRepairPic.setPath(picParh);
					wlEsCustRepairPic.setIsVideoFlag("0");
					wlEsCustRepairPicDao.saveObject(wlEsCustRepairPic);
				}
			}
			// 保存视频
			if (!ValidateUtil.isEmpty(videoPathList)) {
				for (String videoPath : videoPathList) {
					WlEsCustRepairPic wlEsCustRepairPic = new WlEsCustRepairPic();
					wlEsCustRepairPic.setRepairId(wlEsCustRepair.getRepairId());
					wlEsCustRepairPic.setPath(videoPath);
					wlEsCustRepairPic.setIsVideoFlag("1");
					wlEsCustRepairPicDao.saveObject(wlEsCustRepairPic);
				}
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 删除售后维修的图片和视频
	 * @param repairId 售后维修ID
	 * @param path 图片或视频路径
	 */
	public void deleteData(String repairId, String path) {
		try {
			wlEsCustRepairPicDao.deleteData(repairId, path);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取售后维修对象(含图片、视频、处理意见)
	 * @param repairId 售后维修ID
	 * @return 售后维修对象
	 */
	public WlEsCustRepair getCustRepair(String repairId) {
		try {
			// 获取售后维修信息
			WlEsCustRepair wlEsCustRepair = (WlEsCustRepair) wlEsCustRepairDao.getObject(repairId);
			// 获取上传的图片列表
			List<WlEsCustRepairPic> picList = wlEsCustRepairPicDao.findRepairPicList(repairId, "0");
			// 获取上传的视频列表
			List<WlEsCustRepairPic> videoList = wlEsCustRepairPicDao.findRepairPicList(repairId, "1");
			for (WlEsCustRepairPic video : videoList) {
				WlCmDocument doc = (WlCmDocument) wlCmDocumentService.getObject(video.getPath());
				WlCmDocumentPath docPath = (WlCmDocumentPath) pathService.findBySourceCode(doc.getSourceCode());
				String filePath = Config.uploadFilesPath + "/" + docPath.getPath() + "/" + doc.getFileName();
				String downLoadPath = "upload/" + docPath.getPath() + "/" + doc.getFileName();
				video.setFilePath(filePath);
				video.setRelativeFilePath(downLoadPath);
			}
			// 获取处理意见列表
			List<WlEsCustRepairSug> sugList = wlEsCustRepairSugDao.findRepairSugList(repairId);
			wlEsCustRepair.setPicList(picList);
			wlEsCustRepair.setVideoList(videoList);
			wlEsCustRepair.setSugList(sugList);
			return wlEsCustRepair;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 生成维修号
	 * @return 维修单号
	 */
	public String getNewCustRepairNO() {
		try {
			List<String> list = wlEsCustRepairDao.getNewrepairNoCode();
			Date nowDate = new Date();
			String newCode = "";
			if (!ValidateUtil.isEmpty(list)) {
				String nowStr = DateUtil.dateFormatFromDateToString(nowDate, "yyyy");
				String maxCodeString = list.get(0);
				if (ValidateUtil.isEmpty(maxCodeString)) {// 无数据的时候返回第一个维修编号
					newCode = nowStr + "00001";
				}
				else {
					if (maxCodeString.contains(nowStr)) {// 流水号添加1
						String maxStrCode = maxCodeString.replaceAll(nowStr, "");
						int maxCode = Integer.parseInt(maxStrCode);
						maxCode++;
						maxStrCode = "" + maxCode;
						int length = maxStrCode.length();
						for (int i = 0; i < 5 - length; i++) {
							maxStrCode = "0" + maxStrCode;
						}
						newCode = nowStr + maxStrCode;
					}
					else {// 否则直接生成这一分钟的第一个流水号
						newCode = nowStr + "00001";
					}
				}
			}
			else {
				String nowStr = DateUtil.dateFormatFromDateToString(nowDate, "yyyy");
				newCode = nowStr + "00001";
			}
			return newCode;
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 售后维修发货并将订单更改为已发货
	 * @param wlEsCustRepair 售后维修对象
	 * @param user 当前登录用户
	 */
	public void updateData(WlEsCustRepair wlEsCustRepair, User user) {
		try {
			this.updateObject(wlEsCustRepair);// 保存售后维修对象
			// 将订单更改为已发货
			WlEsOrderService wlEsOrderService = (WlEsOrderService) Sc.getBean("wl.es.WlEsOrderService");
			WlEsOrder order = wlEsOrderService.getWlEsOrderByOrderNo(wlEsCustRepair.getOrderNo());
			if (!ValidateUtil.isNull(order)) {
				order.setOrderStateEk("2");
				order.setCorpUserId(user.getId());
				order.setCorpUser(user.getName());
				order.setLogisticEk(wlEsCustRepair.getLogisticEk());
				order.setDeliveryNo(wlEsCustRepair.getDeliveryNo());
				order.setDeliveryTime(new Date());
				wlEsOrderService.saveObject(order);
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/***
	 * 根据订单号获取售后维修对象
	 * @param orderNo 订单号
	 * @return 售后维修对象
	 */
	public WlEsCustRepair getWlEsCustRepairByOrderNo(String orderNo) {
		try {
			return wlEsCustRepairDao.getWlEsCustRepairByOrderNo(orderNo);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 将维修单更新为未处理
	 * @param repairIds 维修id数组
	 */
	public void updateSubmit(String[] repairIds) {
		try {
			for (String repairId : repairIds) {
				WlEsCustRepair wlEsCustRepair = (WlEsCustRepair) this.getObject(repairId);
				if (!ValidateUtil.isEmpty(wlEsCustRepair.getProcessStatusEk())) {// 如果是已提交过
					throw new BaseException(Lang.getString("wl.es.wlEsCustRepairService.hasSubmit"));
				}
				wlEsCustRepair.setProcessStatusEk("0");
				this.saveObject(wlEsCustRepair);
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
	
	/**
	 * 将维修单更新为未提交
	 * @param repairIds 维修id数组
	 */
	public void updateRetract(String[] repairIds) {
		try {
			for (String repairId : repairIds) {
				WlEsCustRepair wlEsCustRepair = (WlEsCustRepair) this.getObject(repairId);
				if (ValidateUtil.isEmpty(wlEsCustRepair.getProcessStatusEk())) {// 如果是未提交的
					throw new BaseException(Lang.getString("wl.es.wlEsCustRepairService.hasNoSubmit"));
				}
				if (!"0".equals(wlEsCustRepair.getProcessStatusEk())) {// 如果是已处理的
					throw new BaseException(Lang.getString("wl.es.wlEsCustRepairService.hasHandle"));
				}
				wlEsCustRepair.setProcessStatusEk("");
				this.saveObject(wlEsCustRepair);
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}