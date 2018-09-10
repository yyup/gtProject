package com.gt.wl.es.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joyone.lang.BaseException;
import org.joyone.service.BaseService;
import org.joyone.spring.Sc;
import org.joyone.sys.Lang;
import org.joyone.sys.Page;
import org.joyone.util.DateUtil;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Service;

import com.gt.wl.cm.dao.WlCmSellDao;
import com.gt.wl.cm.dao.WlCmSellEnDao;
import com.gt.wl.cm.model.WlCmMember;
import com.gt.wl.cm.model.WlCmSell;
import com.gt.wl.cm.model.WlCmSellEn;
import com.gt.wl.cm.service.WlCmMemberService;
import com.gt.wl.es.dao.WlEsSerialRegDao;
import com.gt.wl.es.dao.WlEsSerialRegPicDao;
import com.gt.wl.es.dao.WlEsStoreOutDao;
import com.gt.wl.es.model.WlEsSerialReg;
import com.gt.wl.es.model.WlEsSerialRegPic;
import com.gt.wl.es.model.WlEsStoreOut;
import com.gt.wl.wm.dao.WlWmInventoryDao;
import com.gt.wl.wm.dao.WlWmStoreOutDetlDao;

/**
 * 序列号登记管理Service层
 * @author liuyj
 * 
 */
@Service("wl.es.WlEsSerialRegService")
public class WlEsSerialRegService extends BaseService {
	private WlEsSerialRegDao wlEsSerialRegDao = (WlEsSerialRegDao) Sc.getBean("wl.es.WlEsSerialRegDao");
	private WlEsSerialRegPicDao wlEsSerialRegPicDao = (WlEsSerialRegPicDao) Sc.getBean("wl.es.WlEsSerialRegPicDao");
	private WlEsStoreOutDao wlEsStoreOutDao = (WlEsStoreOutDao) Sc.getBean("wl.es.WlEsStoreOutDao");
	private WlEsStoreOutService wlEsStoreOutService = (WlEsStoreOutService) Sc.getBean("wl.es.WlEsStoreOutService");
	private WlCmMemberService wlCmMemberService = (WlCmMemberService) Sc.getBean("wl.cm.WlCmMemberService");
	private WlEsTodoMsgService wlEsTodoMsgService = (WlEsTodoMsgService) Sc.getBean("wl.es.WlEsTodoMsgService");
	private WlWmStoreOutDetlDao wlWmStoreOutDetlDao = (WlWmStoreOutDetlDao) Sc.getBean("wl.wm.WlWmStoreOutDetlDao");
	private WlWmInventoryDao wlWmInventoryDao = (WlWmInventoryDao) Sc.getBean("wl.wm.WlWmInventoryDao");
	private WlCmSellDao wlCmSellDao = (WlCmSellDao) Sc.getBean("wl.cm.WlCmSellDao");
	private WlCmSellEnDao wlCmSellEnDao = (WlCmSellEnDao) Sc.getBean("wl.cm.WlCmSellEnDao");

	public WlEsSerialRegService() {
		baseDao = wlEsSerialRegDao;
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
	public Page search(int currPage, int pageSize, String keyId, String keyValue, String auditState, String regDateStart, String regDateEnd, String isManualFlag, String account) {
		try {
			return wlEsSerialRegDao.search(currPage, pageSize, keyId, keyValue, auditState, regDateStart, regDateEnd, isManualFlag, account);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询设备编码信息信息
	 * @param memberId 会员id
	 * @return 设备编码信息
	 */

	public List<Map> findDeviceCdList(String memberId) {
		try {
			List<Map> mapLst = wlEsSerialRegDao.findDeviceCdList(memberId);
			return mapLst;
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
	 * @param userName 用户名
	 */
	public void updateAuditData(String regId, String auditState, String deviceCd, String reason, String deliveryDate, String userName) {
		try {
			WlEsSerialReg wlEsSerialReg = (WlEsSerialReg) this.getObject(regId);
			wlEsSerialReg.setAuditState(auditState);
			wlEsSerialReg.setDeviceCd(deviceCd);
			wlEsSerialReg.setReason(reason);
			wlEsSerialReg.setModifier(userName);
			wlEsSerialReg.setModifyTime(new Date());
			// 如果审核状态为通过，则从发货日起+3天开始算保修期，登记完成后框架升级质保5年，配件升级质保3年
			if ("1".equals(auditState)) {
				// 发货日期字符串转为日期类型
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date date = sdf.parse(deliveryDate);
				// 取得发货日期3天后日期
				// Date threeDate = DateUtil.dateAdd(Calendar.DAY_OF_MONTH, 3, date);
				// 取得框架质保5年后日期
				Date fiveYearDate = DateUtil.dateAdd(Calendar.YEAR, 5, date);
				// 取得配件质保3年后日期
				Date threeYearDate = DateUtil.dateAdd(Calendar.YEAR, 3, date);
				wlEsSerialReg.setFrameExpDate(fiveYearDate);
				wlEsSerialReg.setAccExpDate(threeYearDate);
				// 会员购买过商品
				WlCmMember wlCmMember = (WlCmMember) wlCmMemberService.getObject(wlEsSerialReg.getMemberId());
				if (!ValidateUtil.isNull(wlCmMember)) {
					wlCmMember.setIsBuyFlag("1");// 会员购买过商品
					wlCmMemberService.saveObject(wlCmMember);
				}
				// liuyj 2016-1-8 add判断该设备编码在es_store_out中是否存在，如不存在，则插入一条记录
				WlEsStoreOut storeOut = wlEsStoreOutService.validateDeviceCdData(deviceCd);
				if (ValidateUtil.isNull(storeOut)) {// 不存在，新增
					WlEsStoreOut wlEsStoreOut = new WlEsStoreOut();
					wlEsStoreOut.setSaleDate(wlEsSerialReg.getBuyDate());
					wlEsStoreOut.setDeliveryDate(DateUtil.shortDateStrToDate(deliveryDate));
					wlEsStoreOut.setAgency(wlEsSerialReg.getAgency());
					wlEsStoreOut.setProductName(wlEsSerialReg.getProductName());
					wlEsStoreOut.setNum(1);
					wlEsStoreOut.setPrice(0);
					wlEsStoreOut.setAccessories("");
					wlEsStoreOut.setAccPrice(0);
					wlEsStoreOut.setReceiver(wlEsSerialReg.getReceiver());
					wlEsStoreOut.setContact(wlEsSerialReg.getContact());
					wlEsStoreOut.setAddr("");
					wlEsStoreOut.setZipCd("");
					wlEsStoreOut.setDeviceCd(deviceCd);
					wlEsStoreOut.setIsRegFlag("1");
					wlEsStoreOutService.saveObject(wlEsStoreOut);
				}
				else {// 存在，是否已注册设为是
						// 判断该设备编码是否已注册，如是则提示
					if ("1".equals(storeOut.getIsRegFlag())) {
						throw new BaseException("【" + deviceCd + "】" + Lang.getString("wl.es.wlEsStoreOutAction.deviceNoHasReg"));
					}
					storeOut.setIsRegFlag("1");
					wlEsStoreOutService.saveObject(storeOut);
				}
				// 增加到待发送消息
				wlEsTodoMsgService.saveToDoMsg(wlEsSerialReg.getRegId(), "SERIAL");
				// update finish!
			}
			wlEsSerialRegDao.updateObject(wlEsSerialReg);

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过登记ID获得图片列表
	 * @param regId 登记ID
	 * @return 图片列表
	 */
	public List<WlEsSerialRegPic> findPicList(String regId) {
		try {
			return wlEsSerialRegPicDao.findPicList(regId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询序列号列表
	 * @param paraMap 参数Map对象
	 * @return 数据列表
	 */
	public List<Map> findSerialDataList(Map paraMap) {
		try {
			return wlEsSerialRegDao.findSerialDataList(paraMap);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据设备编号和审核状态获取序列号注册对象
	 * @param deviceCd 设备编号
	 * @param auditState 审核状态
	 * @return 序列号注册对象
	 */
	public WlEsSerialReg getWlEsSerialReg(String deviceCd, String auditState) {
		try {
			return wlEsSerialRegDao.getWlEsSerialReg(deviceCd, auditState);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 保存序列号
	 * @param wlEsSerialReg 序列号对象
	 * @param picPathList 图片编号列表
	 */
	public void saveSerialReg(WlEsSerialReg wlEsSerialReg, String[] picPathList) {
		try {
			if (!ValidateUtil.isEmpty(picPathList)) {
				wlEsSerialReg.setIsManualFlag("1");
				wlEsSerialReg.setAuditState("0");
				wlEsSerialRegDao.saveObject(wlEsSerialReg);

				for (String picParh : picPathList) {
					WlEsSerialRegPic wlEsSerialRegPic = new WlEsSerialRegPic();
					wlEsSerialRegPic.setRegId(wlEsSerialReg.getRegId());
					wlEsSerialRegPic.setPath(picParh);
					wlEsSerialRegPicDao.saveObject(wlEsSerialRegPic);
				}
			}
			else {
				List<WlEsStoreOut> wlEsStoreOutList = wlEsStoreOutDao.findOutList("", "", "", "", "", "", "", "", "");

				WlEsStoreOut matWlEsStoreOut = null;
				for (WlEsStoreOut wlEsStoreOut : wlEsStoreOutList) {
					if (wlEsStoreOut.getDeviceCd().equals(wlEsSerialReg.getDeviceCd())) {
						matWlEsStoreOut = wlEsStoreOut;
						break;
					}
				}

				if (!ValidateUtil.isNull(matWlEsStoreOut)) {
					wlEsSerialReg.setIsManualFlag("1");
					wlEsSerialReg.setAuditState("0");
				}
				else {
					wlEsSerialReg.setIsManualFlag("0");
					wlEsSerialReg.setAuditState("1");
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

					// 发货日期字符串转为日期类型
					Date date = sdf.parse(matWlEsStoreOut.getDeliveryDate().toString());
					// 取得发货日期3天后日期
					// Date threeDate = DateUtil.dateAdd(Calendar.DAY_OF_MONTH, 3, date);
					// 取得框架质保5年后日期
					Date fiveYearDate = DateUtil.dateAdd(Calendar.YEAR, 5, date);
					// 取得配件质保3年后日期
					Date threeYearDate = DateUtil.dateAdd(Calendar.YEAR, 3, date);
					wlEsSerialReg.setFrameExpDate(fiveYearDate);
					wlEsSerialReg.setAccExpDate(threeYearDate);
				}

				wlEsSerialRegDao.saveObject(wlEsSerialReg);
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 保存序列号
	 * @param wlEsSerialReg 序列号对象
	 * @param picPathList 图片编号列表
	 * @return 异常信息提示或为空
	 */
	public Map<String, Object> saveSerialRegData(WlEsSerialReg wlEsSerialReg, String[] picPathList, String langType) {
		try {
			Map<String, Object> mapResult = new HashMap<String, Object>();
			WlCmMember wlCmMember = (WlCmMember) wlCmMemberService.getObject(wlEsSerialReg.getMemberId());
			if (!ValidateUtil.isEmpty(wlEsSerialReg.getDeviceCd())) {// 有输入序列号
				WlEsStoreOut wlEsStoreOut = wlEsStoreOutService.validateDeviceCdData(wlEsSerialReg.getDeviceCd());
				if (null != wlEsStoreOut && !ValidateUtil.isNull(wlEsStoreOut)) {// 在出库表查到序列号
					if (wlEsStoreOut.getReceiver().equals(wlEsSerialReg.getReceiver()) && wlEsStoreOut.getContact().equals(wlEsSerialReg.getContact())) {// 如果姓名和手机号码一致，允许注册
						//验证购买型号
						Map productNameMap = getproductName(wlEsSerialReg.getDeviceCd(), langType);
						if (!ValidateUtil.isEmpty(productNameMap)) {//购买型号不匹配
							wlEsSerialReg.setProductName(productNameMap.get("productName").toString());
							wlEsSerialReg.setAuditState("1");
							wlEsSerialReg.setIsManualFlag("0");
							wlEsSerialReg.setBuyDate(wlEsStoreOut.getDeliveryDate());
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							// 发货日期字符串转为日期类型
							Date date = sdf.parse(wlEsStoreOut.getDeliveryDate().toString());
							// 取得发货日期3天后日期
							// Date threeDate = DateUtil.dateAdd(Calendar.DAY_OF_MONTH, 3, date);
							// 取得框架质保5年后日期
							Date fiveYearDate = DateUtil.dateAdd(Calendar.YEAR, 5, date);
							// 取得配件质保3年后日期
							Date threeYearDate = DateUtil.dateAdd(Calendar.YEAR, 3, date);
							wlEsSerialReg.setFrameExpDate(fiveYearDate);
							wlEsSerialReg.setAccExpDate(threeYearDate);
							this.saveObject(wlEsSerialReg);
							String result = "";
							if ("EN".equals(langType)) {//英文
								result = Lang.getString("wl.es.wlEsSerialRegAction.frameExpDateEN") + sdf.format(fiveYearDate) + ",";
								result += Lang.getString("wl.es.wlEsSerialRegAction.accExpDateEN") + sdf.format(threeYearDate);
							} else {
								result = Lang.getString("wl.es.wlEsSerialRegAction.frameExpDate") + sdf.format(fiveYearDate) + ",";
								result += Lang.getString("wl.es.wlEsSerialRegAction.accExpDate") + sdf.format(threeYearDate);
							}
							if (!ValidateUtil.isNull(wlCmMember)) {
								wlCmMember.setIsBuyFlag("1");// 会员已购买过商品
								wlCmMemberService.saveObject(wlCmMember);
							}
							wlEsStoreOut.setIsRegFlag("1");
							wlEsStoreOutService.saveObject(wlEsStoreOut);// 当前序列号已注册
							mapResult.put("result", result);
							mapResult.put("isSuccess", true);
						} else {
							mapResult.put("productNameMap", productNameMap);
							mapResult.put("type", "2");
							if ("EN".equals(langType)) {//英文
								mapResult.put("result", Lang.getString("wl.es.wlEsSerialRegService.noProductNameEN"));
							} else {
								mapResult.put("result", Lang.getString("wl.es.wlEsSerialRegService.noProductName"));
							}

							mapResult.put("isSuccess", false);
						}
					}
					else {// 注册他人序列号
						if ("EN".equals(langType)) {//英文
							mapResult.put("result", Lang.getString("wl.es.wlEsSerialRegService.otherRegisterEN"));
						} else {
							mapResult.put("result", Lang.getString("wl.es.wlEsSerialRegService.otherRegister"));
						}

						mapResult.put("contact", wlEsStoreOut.getContact());
						mapResult.put("receiver", wlEsStoreOut.getReceiver());
						mapResult.put("deviceCd", wlEsStoreOut.getDeviceCd());
						mapResult.put("isSuccess", false);
					}
					// return mapResult;
				} else {// 在出库表查不到序列号
					if ("EN".equals(langType)) {//英文
						mapResult.put("result", wlEsSerialReg.getDeviceCd() + Lang.getString("wl.es.wlEsSerialRegAction.noSerialRecordEN"));
					} else {
						mapResult.put("result", wlEsSerialReg.getDeviceCd() + Lang.getString("wl.es.wlEsSerialRegAction.noSerialRecord"));
					}
					mapResult.put("isSuccess", false);
					mapResult.put("type", "3");
					// return mapResult;
				}

			} else {// 没有输入序列号
				if (!ValidateUtil.isNull(picPathList)) {
					wlEsSerialReg.setAuditState("0");
					wlEsSerialReg.setIsManualFlag("1");
					this.saveObject(wlEsSerialReg);
					int i = 1;
					for (String picPath : picPathList) {
						WlEsSerialRegPic wlEsSerialRegPic = new WlEsSerialRegPic();
						wlEsSerialRegPic.setPath(picPath);
						wlEsSerialRegPic.setRegId(wlEsSerialReg.getRegId());
						wlEsSerialRegPic.setSequ(i++);
						wlEsSerialRegPicDao.saveObject(wlEsSerialRegPic);
					}
				}
				mapResult.put("type", "3");
				mapResult.put("result", "");
				mapResult.put("isSuccess", true);
				// return mapResult;
			}
			return mapResult;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 保存编辑数据
	 * @param wlEsSerialReg 序列号注册对象
	 * @param deliveryDate 发货日期
	 * @param oldDeviceCd 修改前的序列号
	 */
	public void updateData(WlEsSerialReg wlEsSerialReg, String deliveryDate, String oldDeviceCd) {
		try {
			if ("0".equals(wlEsSerialReg.getAuditState())) {// 审核中
				this.updateObject(wlEsSerialReg);
			}
			else if ("1".equals(wlEsSerialReg.getAuditState())) {// 审核通过
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				// 发货日期字符串转为日期类型
				Date date = sdf.parse(deliveryDate);
				// 取得发货日期3天后日期
				// Date threeDate = DateUtil.dateAdd(Calendar.DAY_OF_MONTH, 3, date);
				// 取得框架质保5年后日期
				Date fiveYearDate = DateUtil.dateAdd(Calendar.YEAR, 5, date);
				// 取得配件质保3年后日期
				Date threeYearDate = DateUtil.dateAdd(Calendar.YEAR, 3, date);
				wlEsSerialReg.setFrameExpDate(fiveYearDate);
				wlEsSerialReg.setAccExpDate(threeYearDate);
				// add判断该设备编码在es_store_out中是否存在，如不存在，则插入一条记录
				WlEsStoreOut storeOut = wlEsStoreOutService.validateDeviceCdData(wlEsSerialReg.getDeviceCd());
				if (ValidateUtil.isNull(storeOut)) {// 不存在，新增
					WlEsStoreOut wlEsStoreOut = new WlEsStoreOut();
					wlEsStoreOut.setSaleDate(wlEsSerialReg.getBuyDate());
					wlEsStoreOut.setDeliveryDate(DateUtil.shortDateStrToDate(deliveryDate));
					wlEsStoreOut.setAgency(wlEsSerialReg.getAgency());
					wlEsStoreOut.setProductName(wlEsSerialReg.getProductName());
					wlEsStoreOut.setNum(1);
					wlEsStoreOut.setPrice(0);
					wlEsStoreOut.setAccessories("");
					wlEsStoreOut.setAccPrice(0);
					wlEsStoreOut.setReceiver(wlEsSerialReg.getReceiver());
					wlEsStoreOut.setContact(wlEsSerialReg.getContact());
					wlEsStoreOut.setAddr("");
					wlEsStoreOut.setZipCd("");
					wlEsStoreOut.setDeviceCd(wlEsSerialReg.getDeviceCd());
					wlEsStoreOut.setIsRegFlag("1");
					wlEsStoreOutService.saveObject(wlEsStoreOut);
				}
				else {// 在es_store_out中已存在此序列号
					/*
					 * if (!wlEsSerialReg.getProductName().equals(storeOut.getProductName())) {// 型号与序列号登记型号不一致
					 * throw new BaseException(Lang.getString("wl.es.wlEsSerialRegService.productNameNoSame"));
					 * }
					 */
					if (!wlEsSerialReg.getAgency().equals(storeOut.getAgency())) {// 经销商与序列号登记经销商不一致
						throw new BaseException(Lang.getString("wl.es.wlEsSerialRegService.agencyNoSame"));
					}
					if (!wlEsSerialReg.getReceiver().equals(storeOut.getReceiver())) {// 购买人与序列号登记收货人不一致
						throw new BaseException(Lang.getString("wl.es.wlEsSerialRegService.receiverNoSame"));
					}
					if (!storeOut.getDeviceCd().equals(oldDeviceCd)) {// 如果序列号有做修改，将修改前序列号对象设置为未注册
						WlEsStoreOut oldStoreOut = wlEsStoreOutService.validateDeviceCdData(oldDeviceCd);// 修改前序列号对应的序列号登记对象
						oldStoreOut.setIsRegFlag("0");
						wlEsStoreOutService.updateObject(oldStoreOut);
					}
					storeOut.setIsRegFlag("1");
					wlEsStoreOutService.updateObject(storeOut);
				}
				this.updateObject(wlEsSerialReg);
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 删除序列号注册和序列号注册下的图片
	 * @param regId 序列号注册id
	 */
	public void deleteData(String regId) {
		try {
			// 删除序列号登记
			this.deleteObject(regId);
			// 删除图片
			wlEsSerialRegPicDao.deleteData(regId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 根据序列号查询物料信息
	 * @param serialNo
	 * @return
	 */
	public Map getproductName(String serialNo, String langType) {
		try {
			Map map = new HashMap();
			String productName = "";
			Map inventoryMap = wlWmInventoryDao.getInventoryBySerialNo(serialNo);//根据序列号查询库存
			if ("CN".equals(langType)) {//中文
				if (!inventoryMap.isEmpty() && !ValidateUtil.isNull(inventoryMap)) {
					WlCmSell wlCmSell = wlCmSellDao.getWlCmSellByItemCd(inventoryMap.get("itemCd").toString());
					if (!ValidateUtil.isNull(wlCmSell)) {
						map.put("productName", wlCmSell.getProductName());
					}
				}
			} else {
				if (!ValidateUtil.isEmpty(inventoryMap)) {
					WlCmSellEn wlCmSellEn = wlCmSellEnDao.getWlCmSellByItemCd(inventoryMap.get("itemCd").toString());
					if (!ValidateUtil.isNull(wlCmSellEn)) {
						map.put("productName", wlCmSellEn.getProductName());
					}
				}
			}
			return map;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}