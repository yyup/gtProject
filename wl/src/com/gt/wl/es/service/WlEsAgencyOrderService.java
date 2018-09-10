package com.gt.wl.es.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.joyone.lang.BaseException;
import org.joyone.service.BaseService;
import org.joyone.spring.Sc;
import org.joyone.sys.Config;
import org.joyone.sys.Lang;
import org.joyone.sys.Page;
import org.joyone.sys.User;
import org.joyone.util.ConvertUtil;
import org.joyone.util.DateUtil;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Service;

import com.gt.wl.cm.model.WlCmCategory;
import com.gt.wl.cm.model.WlCmDocument;
import com.gt.wl.cm.model.WlCmDocumentPath;
import com.gt.wl.cm.model.WlCmItem;
import com.gt.wl.cm.model.WlCmOrg;
import com.gt.wl.cm.model.WlCmOrgUser;
import com.gt.wl.cm.model.WlCmStorage;
import com.gt.wl.cm.service.WlCmCategoryService;
import com.gt.wl.cm.service.WlCmDocumentPathService;
import com.gt.wl.cm.service.WlCmDocumentService;
import com.gt.wl.cm.service.WlCmItemService;
import com.gt.wl.cm.service.WlCmOrgService;
import com.gt.wl.cm.service.WlCmOrgUserService;
import com.gt.wl.cm.service.WlCmStorageService;
import com.gt.wl.es.dao.WlEsAgencyOrderAuditDao;
import com.gt.wl.es.dao.WlEsAgencyOrderDao;
import com.gt.wl.es.dao.WlEsAgencyOrderDetlDao;
import com.gt.wl.es.model.WlEsAgencyOrder;
import com.gt.wl.es.model.WlEsAgencyOrderAudit;
import com.gt.wl.es.model.WlEsAgencyOrderDetl;
import com.gt.wl.util.CommonConf;
import com.gt.wl.util.MailUtil;
import com.gt.wl.wm.dao.WlWmNoticeOutDetlDao;
import com.gt.wl.wm.model.WlWmInventory;
import com.gt.wl.wm.model.WlWmNoticeOut;
import com.gt.wl.wm.model.WlWmNoticeOutDetl;
import com.gt.wl.wm.service.WlWmInventoryService;
import com.gt.wl.wm.service.WlWmNoticeOutService;

/**
 * 经销商订单Service层
 * @author liuyj
 * 
 */
@Service("wl.es.WlEsAgencyOrderService")
public class WlEsAgencyOrderService extends BaseService {
	private WlEsAgencyOrderDao wlEsAgencyOrderDao = (WlEsAgencyOrderDao) Sc.getBean("wl.es.WlEsAgencyOrderDao");;
	private WlEsAgencyOrderDetlDao wlEsAgencyOrderDetlDao = (WlEsAgencyOrderDetlDao) Sc.getBean("wl.es.WlEsAgencyOrderDetlDao");
	private WlCmOrgUserService wlCmOrgUserService = (WlCmOrgUserService) Sc.getBean("wl.cm.WlCmOrgUserService");
	private WlCmOrgService wlCmOrgService = (WlCmOrgService) Sc.getBean("wl.cm.WlCmOrgService");
	private WlEsAgencyOrderAuditDao wlEsAgencyOrderAuditDao = (WlEsAgencyOrderAuditDao) Sc.getBean("wl.es.WlEsAgencyOrderAuditDao");
	private WlCmDocumentService wlCmDocumentService = (WlCmDocumentService) Sc.getBean("wl.cm.WlCmDocumentService");
	private WlCmDocumentPathService pathService = (WlCmDocumentPathService) Sc.getBean("wl.cm.WlCmDocumentPathService");
	private WlCmItemService wlCmItemService = (WlCmItemService) Sc.getBean("wl.cm.WlCmItemService");
	private WlWmNoticeOutService wlWmNoticeOutService = (WlWmNoticeOutService) Sc.getBean("wl.wm.WlWmNoticeOutService");
	private WlCmStorageService wlCmStorageService = (WlCmStorageService) Sc.getBean("wl.cm.WlCmStorageService");
	private WlWmNoticeOutDetlDao wlWmNoticeOutDetlDao = (WlWmNoticeOutDetlDao) Sc.getBean("wl.wm.WlWmNoticeOutDetlDao");
	private WlCmCategoryService wlCmCategoryService = (WlCmCategoryService) Sc.getBean("wl.cm.WlCmCategoryService");
	private WlWmInventoryService wlWmInventoryService = (WlWmInventoryService) Sc.getBean("wl.wm.WlWmInventoryService");

	public WlEsAgencyOrderService() {
		baseDao = wlEsAgencyOrderDao;
	}

	/**
	 * 查询经销商订单从表的所有物料名称
	 * @param orderId 订单id
	 * @return 物料的名字，格式为"划船器等6种物料"
	 */
	public String getAllItemName(String orderId) {
		try {
			String result = "";
			List<WlEsAgencyOrderDetl> list = wlEsAgencyOrderDetlDao.findAgencyOrderDetlList(orderId);
			if (!ValidateUtil.isEmpty(list)) {
				if (list.size() == 1) {
					WlEsAgencyOrderDetl wlEsAgencyOrderDetl = list.get(0);
					result += wlEsAgencyOrderDetl.getItemName();
				}
				else if (list.size() > 1) {
					WlEsAgencyOrderDetl wlEsAgencyOrderDetl = list.get(0);
					result += wlEsAgencyOrderDetl.getItemName() + Lang.getString("wl.es.WlEsAgencyOrderService.kinds");
					result += list.size() + Lang.getString("wl.es.WlEsAgencyOrderService.itemNames");
				}
			}
			return result;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询经销商订单（分页）
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param paraMap 条件
	 * @return 分页结果
	 */
	public Page search(int currPage, int pageSize, Map paraMap) {
		try {
			return wlEsAgencyOrderDao.search(currPage, pageSize, paraMap);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/***
	 * 新增/编辑经销商订单
	 * @param wlEsAgencyOrder 经销商订单对象
	 * @param wlEsAgencyOrderDetlList 经销商订单从表对象数组
	 * @param user 当前登录用户
	 */
	public void saveData(WlEsAgencyOrder wlEsAgencyOrder, List<WlEsAgencyOrderDetl> wlEsAgencyOrderDetlList, User user) {
		try {
			double totalQty = 0;
			wlEsAgencyOrder.setOrderTime(new Date());
			wlEsAgencyOrder.setUserId(user.getId());
			wlEsAgencyOrder.setName(user.getName());
			if (ValidateUtil.isEmpty(wlEsAgencyOrder.getOrderId())) {// 新增
				wlEsAgencyOrder.setOrderNo(this.getNewOrderNoCode());
				wlEsAgencyOrder.setCreateTime(new Date());
				wlEsAgencyOrder.setCreator(user.getName());
				// 单位
				WlCmOrgUser wlCmOrgUser = (WlCmOrgUser) wlCmOrgUserService.getObject(user.getId());
				if (!ValidateUtil.isNull(wlCmOrgUser)) {
					WlCmOrg wlCmOrg = (WlCmOrg) wlCmOrgService.getObject(wlCmOrgUser.getOrgId());
					wlEsAgencyOrder.setOrgId(wlCmOrg.getOrgId());
					wlEsAgencyOrder.setOrgName(wlCmOrg.getOrgName());
				}

			}
			else {// 编辑
				wlEsAgencyOrder.setModifier(user.getName());
				wlEsAgencyOrder.setModifyTime(new Date());
			}
			this.saveObject(wlEsAgencyOrder);
			Map<String, String> itemMap = new HashMap<String, String>();// 用于判断是否存在姓名+手机号+物料编码一致的
			Map<String, Double> itemQtyMap = new HashMap<String, Double>();// key是itemId，value是此物料下单的的总数量
			if (!ValidateUtil.isEmpty(wlEsAgencyOrderDetlList)) {
				String[] orderIds = new String[1];
				orderIds[0] = wlEsAgencyOrder.getOrderId();
				wlEsAgencyOrderDetlDao.deleteOrderDetlByOrderId(ConvertUtil.toDbString(orderIds));
				for (WlEsAgencyOrderDetl wlEsAgencyOrderDetl : wlEsAgencyOrderDetlList) {
					String contactWay = wlEsAgencyOrderDetl.getConsignee().replaceAll("[^0-9,]", "");// 联系方式去除数字和逗号外的符号
					String consignee = wlEsAgencyOrderDetl.getConsignee().trim();
					if (itemMap.containsKey(consignee + contactWay + wlEsAgencyOrderDetl.getItemCd())) {
						String result = "";
						result += Lang.getString("wl.es.WlEsAgencyOrderService.consignee") + consignee;
						result += Lang.getString("wl.es.WlEsAgencyOrderService.contactWay") + contactWay + ",";
						result += Lang.getString("wl.es.WlEsAgencyOrderService.sameItem") + wlEsAgencyOrderDetl.getItemCd();
						throw new BaseException(result);
					}
					else {
						itemMap.put(consignee + contactWay + wlEsAgencyOrderDetl.getItemCd(), "");
						if (itemQtyMap.containsKey(wlEsAgencyOrderDetl.getItemId())) {
							itemQtyMap.put(wlEsAgencyOrderDetl.getItemId(), itemQtyMap.get(wlEsAgencyOrderDetl.getItemId()) + wlEsAgencyOrderDetl.getBaseUnitQty());
						}
						else {
							itemQtyMap.put(wlEsAgencyOrderDetl.getItemId(), wlEsAgencyOrderDetl.getBaseUnitQty());
						}
						double canUseQty = this.getCanUseInventoryQty(wlEsAgencyOrderDetl.getItemId());
						if (itemQtyMap.get(wlEsAgencyOrderDetl.getItemId()) > canUseQty) {// 下单数量超过可用数量
							String spec = ValidateUtil.isEmpty(wlEsAgencyOrderDetl.getSpec()) ? wlEsAgencyOrderDetl.getSpec() : "-" + wlEsAgencyOrderDetl.getSpec();
							throw new BaseException("订单保存失败(" + wlEsAgencyOrderDetl.getItemName() + spec + "库存不足）");
						}
						String addr = wlEsAgencyOrderDetl.getAddr();
						if (!ValidateUtil.isEmpty(addr)) {
							addr = addr.replaceAll("\\t", "");
						}
						WlEsAgencyOrderDetl newWlEsAgencyOrderDetl = new WlEsAgencyOrderDetl();
						newWlEsAgencyOrderDetl.setOrderId(wlEsAgencyOrder.getOrderId());
						newWlEsAgencyOrderDetl.setItemCd(wlEsAgencyOrderDetl.getItemCd());
						newWlEsAgencyOrderDetl.setItemId(wlEsAgencyOrderDetl.getItemId());
						newWlEsAgencyOrderDetl.setItemName(wlEsAgencyOrderDetl.getItemName());
						newWlEsAgencyOrderDetl.setCategoryId(wlEsAgencyOrderDetl.getCategoryId());
						newWlEsAgencyOrderDetl.setSpec(wlEsAgencyOrderDetl.getSpec());
						newWlEsAgencyOrderDetl.setBaseUnitId(wlEsAgencyOrderDetl.getBaseUnitId());
						newWlEsAgencyOrderDetl.setBaseUnitName(wlEsAgencyOrderDetl.getBaseUnitName());
						newWlEsAgencyOrderDetl.setBaseUnitQty(wlEsAgencyOrderDetl.getBaseUnitQty());
						totalQty += wlEsAgencyOrderDetl.getBaseUnitQty();
						newWlEsAgencyOrderDetl.setSequ(wlEsAgencyOrderDetl.getSequ());
						newWlEsAgencyOrderDetl.setMemo(wlEsAgencyOrderDetl.getMemo());
						newWlEsAgencyOrderDetl.setAddr(addr);
						newWlEsAgencyOrderDetl.setConsignee(consignee);
						newWlEsAgencyOrderDetl.setContactWay(contactWay);
						wlEsAgencyOrderDetlDao.saveObject(newWlEsAgencyOrderDetl);
					}
				}
			}
			wlEsAgencyOrder.setTotalQty(totalQty);
			this.saveObject(wlEsAgencyOrder);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询经销商订单
	 * @param orderIds 经销商id数组
	 * @param agencyOrderStateEk 订单状态(排除)
	 * @return 经销商订单列表
	 */
	public List findOrderList(String[] orderIds, String agencyOrderStateEk) {
		try {
			String result = "";
			if (!ValidateUtil.isEmpty(orderIds)) {
				result = ConvertUtil.toDbString(orderIds);
			}
			return wlEsAgencyOrderDao.findOrderList(result, agencyOrderStateEk);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询经销商订单从表列表
	 * @param orderId 订单id
	 * @return 经销商订单从表列表
	 */
	public List<WlEsAgencyOrderDetl> findAgencyOrderDetlList(String orderId) {
		try {
			return wlEsAgencyOrderDetlDao.findAgencyOrderDetlList(orderId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询经销商订单审核明细
	 * @param orderId 经销商订单id
	 * @return 经销商订单审核对象数组
	 */
	public List<WlEsAgencyOrderAudit> findAgencyOrderAuditList(String orderId) {
		try {
			return wlEsAgencyOrderAuditDao.findAgencyOrderAuditList(orderId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 删除经销商订单
	 * @param orderIds 经销商id数组
	 */
	public void deleteOrderByOrderId(String[] orderIds) {
		try {
			String result = "";
			if (!ValidateUtil.isEmpty(orderIds)) {
				result = ConvertUtil.toDbString(orderIds);
			}
			wlEsAgencyOrderDao.deleteOrderByOrderId(result);
			wlEsAgencyOrderDetlDao.deleteOrderDetlByOrderId(result);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取新的通知单流水编号
	 * @return 通知单号
	 * @author
	 */
	public String getNewOrderNoCode() {
		try {
			List<String> list = wlEsAgencyOrderDao.getNewOrderNoCode();
			Date nowDate = new Date();
			String newCode = "";
			if (!ValidateUtil.isEmpty(list)) {
				String nowStr = DateUtil.dateFormatFromDateToString(nowDate, "yyyyMM");
				nowStr = "A" + nowStr;
				String maxCodeString = list.get(0);
				if (ValidateUtil.isEmpty(maxCodeString)) {// 无数据的时候返回第一个订单编号
					newCode = nowStr + "001";
				}
				else {
					if (maxCodeString.contains(nowStr)) {// 如果存在同一天的编号，则为之后的流水号添加1
						String maxStrCode = maxCodeString.replaceAll(nowStr, "");
						int maxCode = Integer.parseInt(maxStrCode);
						maxCode++;
						maxStrCode = "" + maxCode;
						int maxStrCodeLength = maxStrCode.length();
						if (maxStrCode.length() < 3) {
							for (int i = 1; i <= 3 - maxStrCodeLength; i++) {
								maxStrCode = "0" + maxStrCode;
							}
						}
						newCode = nowStr + maxStrCode;
					}
					else {// 否则直接生成这一分钟的第一个流水号
						newCode = nowStr + "001";
					}
				}
			}
			else {
				String nowStr = DateUtil.dateFormatFromDateToString(nowDate, "yyyyMM");
				nowStr = "A" + nowStr;
				newCode = nowStr + "001";
			}

			return newCode;
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 保存上传数据
	 * @param path 上传文件ID
	 * @param user 当前登录用户
	 */
	public void saveExcelData(String path, User user) {
		try {
			WlCmDocument doc = (WlCmDocument) wlCmDocumentService.getObject(path);
			WlCmDocumentPath docPath = (WlCmDocumentPath) pathService.findBySourceCode(doc.getSourceCode());
			File file = new File(Config.uploadFilesPath + "//" + docPath.getPath(), doc.getFileName());
			WlEsAgencyOrder wlEsAgencyOrder = new WlEsAgencyOrder();
			wlEsAgencyOrder.setOrderNo(this.getNewOrderNoCode());
			wlEsAgencyOrder.setUserId(user.getId());
			wlEsAgencyOrder.setName(user.getName());
			wlEsAgencyOrder.setAgencyOrderStateEk("N");
			wlEsAgencyOrder.setCreator(user.getName());
			wlEsAgencyOrder.setCreateTime(new Date());
			wlEsAgencyOrder.setOrderTime(new Date());
			// 单位
			WlCmOrgUser wlCmOrgUser = (WlCmOrgUser) wlCmOrgUserService.getObject(user.getId());
			wlEsAgencyOrder.setContactWay(wlCmOrgUser.getMobile());
			wlEsAgencyOrder.setContact(wlCmOrgUser.getContact());
			wlEsAgencyOrder.setOrgId(wlCmOrgUser.getOrgId());
			if (!ValidateUtil.isNull(wlCmOrgUser)) {
				WlCmOrg wlCmOrg = (WlCmOrg) wlCmOrgService.getObject(wlCmOrgUser.getOrgId());
				wlEsAgencyOrder.setOrgName(wlCmOrg.getOrgName());
			}
			this.saveObject(wlEsAgencyOrder);
			// 转换为List并保存
			findExcelList(file, wlEsAgencyOrder);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * excel文件转换为出库对象列表
	 * @param file excel文件
	 * @param orderId 订单id
	 * @return 经销商订单明细和总数量
	 */
	private void findExcelList(File file, WlEsAgencyOrder wlEsAgencyOrder) {
		List<WlEsAgencyOrderDetl> list = new ArrayList<WlEsAgencyOrderDetl>();
		Map<String, Integer> itemMap = new HashMap<String, Integer>();// 用于判断是否存在姓名+手机号+物料编码一致的
		Map<String, Double> itemQtyMap = new HashMap<String, Double>();// key是itemId，value是此物料下单的的总数量
		try {
			// 创建输入流
			FileInputStream fis = new FileInputStream(file);
			// 获取Excel文件对象
			jxl.Workbook rwb = Workbook.getWorkbook(fis);
			// 获取文件的第一个工作表
			Sheet sheet = rwb.getSheet(0);
			// 行数(表头的目录不需要，从1开始)
			for (int i = 2; i < sheet.getRows(); i++) {
				Cell[] cells = sheet.getRow(i);
				if (cells.length == 0) {
					break;
				}
				WlEsAgencyOrderDetl wlEsAgencyOrderDetl = new WlEsAgencyOrderDetl();
				wlEsAgencyOrderDetl.setOrderId(wlEsAgencyOrder.getOrderId());
				String content = cells[0].getContents();
				// 行记录序号如没有值，则结束返回
				if (ValidateUtil.isEmpty(content)) break;
				// 设置对象值
				String itemCd = cells[5].getContents().trim();
				String consignee = cells[9].getContents().trim();
				String contactWay = cells[10].getContents().trim();
				if (ValidateUtil.isEmpty(itemCd)) {// 物料编码为空
					throw new BaseException((i + 1) + Lang.getString("wl.es.WlEsAgencyOrderService.col") + "物料编码为空，请检查！");
				}
				if (ValidateUtil.isEmpty(consignee)) {// 收件人不允许为空
					throw new BaseException((i + 1) + Lang.getString("wl.es.WlEsAgencyOrderService.col") + itemCd + Lang.getString("wl.es.WlEsAgencyOrderService.consigneeIsEmpty"));
				}
				if (ValidateUtil.isEmpty(contactWay)) {// 联系方式不允许为空
					throw new BaseException((i + 1) + Lang.getString("wl.es.WlEsAgencyOrderService.col") + itemCd + Lang.getString("wl.es.WlEsAgencyOrderService.contactWayIsEmpty"));
				}
				contactWay = contactWay.replaceAll("[^0-9,]", "");// 联系方式去除数字和逗号外的符号
				if (itemMap.containsKey(consignee + contactWay + itemCd)) {// 存在收货人+手机号+物料编码一致的收货人
					String result = "";
					result += Lang.getString("wl.es.WlEsAgencyOrderService.consignee") + consignee;
					result += Lang.getString("wl.es.WlEsAgencyOrderService.contactWay") + contactWay + ",";
					result += itemMap.get(consignee + contactWay + itemCd) + Lang.getString("wl.es.WlEsAgencyOrderService.col") + "与";
					result += (i + 1) + Lang.getString("wl.es.WlEsAgencyOrderService.col");
					result += Lang.getString("wl.es.WlEsAgencyOrderService.sameItem") + itemCd;
					throw new BaseException(result);
				}
				else {
					itemMap.put(consignee + contactWay + itemCd, i + 1);
				}
				// 构造从表的值
				wlEsAgencyOrderDetl.setContactWay(contactWay);
				wlEsAgencyOrderDetl.setConsignee(consignee);
				// 获取物料对象
				WlCmItem wlCmItem = wlCmItemService.getItemByItemCd(itemCd);
				if (!ValidateUtil.isNull(wlCmItem)) {
					// 维护物料数量，并判断是否超过可用库存
					String tempBaseUnitQty = cells[6].getContents().trim();
					if (!ValidateUtil.isEmpty(tempBaseUnitQty)) {
						double baseUnitQty = Double.parseDouble(tempBaseUnitQty);
						if (baseUnitQty > 0) {
							wlEsAgencyOrder.setTotalQty(wlEsAgencyOrder.getTotalQty() + baseUnitQty);
							wlEsAgencyOrderDetl.setBaseUnitQty(baseUnitQty);
							if (itemQtyMap.containsKey(wlCmItem.getItemId())) {// 已经存在
								itemQtyMap.put(wlCmItem.getItemId(), baseUnitQty + itemQtyMap.get(wlCmItem.getItemId()));
							}
							else {
								itemQtyMap.put(wlCmItem.getItemId(), baseUnitQty);
							}
						}
						else {// 数量小于0
							throw new BaseException((i + 1) + Lang.getString("wl.es.WlEsAgencyOrderService.col") + itemCd + Lang.getString("wl.es.WlEsAgencyOrderService.baseUnitQtyGtZero"));
						}
					}
					else {// 数量不允许为空
						throw new BaseException((i + 1) + Lang.getString("wl.es.WlEsAgencyOrderService.col") + itemCd + Lang.getString("wl.es.WlEsAgencyOrderService.baseUnitQtyIsEmpty"));
					}
					// 维护物料信息
					WlCmCategory wlCmCategory = (WlCmCategory) wlCmCategoryService.getObject(wlCmItem.getCategoryId());
					if ("0".equals(wlCmCategory.getIsShowAgency()) || "0".equals(wlCmItem.getIsShowAgency())) {// 如果整个物料类别或者本物料不显示给经销商，则不能保存
						String spec = ValidateUtil.isEmpty(wlCmItem.getSpec()) ? wlCmItem.getSpec() : "-" + wlCmItem.getSpec();
						throw new BaseException((i + 1) + Lang.getString("wl.es.WlEsAgencyOrderService.col") + wlCmItem.getItemName() + spec + "(" + itemCd + ")导入失败，请联系沃特罗伦");
					}
					// 判断需求数量是否超过安全库存
					double canUseQty = this.getCanUseInventoryQty(wlCmItem.getItemId());// 可用数量
					if (itemQtyMap.get(wlCmItem.getItemId()) > canUseQty) {
						String spec = ValidateUtil.isEmpty(wlCmItem.getSpec()) ? wlCmItem.getSpec() : "-" + wlCmItem.getSpec();
						throw new BaseException("订单保存失败(" + wlCmItem.getItemName() + spec + "库存不足）");
					}
					wlEsAgencyOrderDetl.setItemCd(wlCmItem.getItemCd());
					wlEsAgencyOrderDetl.setItemId(wlCmItem.getItemId());
					wlEsAgencyOrderDetl.setItemName(wlCmItem.getItemName());
					wlEsAgencyOrderDetl.setCategoryId(wlCmItem.getCategoryId());
					wlEsAgencyOrderDetl.setSpec(wlCmItem.getSpec());
					wlEsAgencyOrderDetl.setBaseUnitId(wlCmItem.getBaseUnitId());
					wlEsAgencyOrderDetl.setBaseUnitName(wlCmItem.getBaseUnitName());
				}
				else {// 物料编码不存在
					throw new BaseException((i + 1) + Lang.getString("wl.es.WlEsAgencyOrderService.col") + itemCd + Lang.getString("wl.es.WlEsAgencyOrderService.noExistItemCd"));
				}

				String addr = cells[11].getContents();
				if (!ValidateUtil.isEmpty(addr)) {
					addr = addr.replaceAll("\\t", "");
					wlEsAgencyOrderDetl.setAddr(addr);
				}
				else {// 地址不允许为空
					throw new BaseException((i + 1) + Lang.getString("wl.es.WlEsAgencyOrderService.col") + itemCd + Lang.getString("wl.es.WlEsAgencyOrderService.addrIsEmpty"));
				}
				list.add(wlEsAgencyOrderDetl);
			}
			this.saveObject(wlEsAgencyOrder);
			wlEsAgencyOrderDetlDao.saveList(list);
			fis.close();
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取可用库存
	 * @param itemId 物料id
	 * @return 可用库存
	 */
	public double getCanUseInventoryQty(String itemId) {
		try {
			// 公式：库存-物料最低下限-审核中和审核通过的数量+已经出库的数量（指定物料）
			WlWmInventory wlWmInventory = wlWmInventoryService.getData(CommonConf.storageId, itemId);// 翔安仓库指定仓库库存
			if (ValidateUtil.isNull(wlWmInventory)) {// 如果没有库存，当做0个处理
				return 0;
			}
			else {
				double qty = wlWmInventory.getBaseUnitQty();
				WlCmItem wlCmItem = (WlCmItem) wlCmItemService.getObject(itemId);
				qty = qty - wlCmItem.getLowerLimit();
				if (qty <= 0) {// 如果库存数-物料下限值小于0，就直接返回
					return 0;
				}
				else {
					qty = qty - wlEsAgencyOrderDao.getOrderItemCount(itemId, "0,1");// 安全库存-审核中和审核通过的数量
					qty = qty + wlEsAgencyOrderDao.getOrderOutCount(itemId, "1");
					return qty;
				}
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 更改订单状态为驳回
	 * @param orderId 经销商订单id
	 * @param auditState 审核状态
	 * @param auditResult 审核结论
	 * @param user 当前登录用户
	 */
	public void updateOrderToReJect(String orderId, String auditState, String auditResult, User user) {
		try {
			if (ValidateUtil.isEmpty(auditResult)) {
				auditResult = "";
			}
			WlEsAgencyOrder wlEsAgencyOrder = (WlEsAgencyOrder) this.getObject(orderId);
			wlEsAgencyOrder.setAgencyOrderStateEk(auditState);// 订单
			// 订单审核
			WlEsAgencyOrderAudit wlEsAgencyOrderAudit = new WlEsAgencyOrderAudit();
			wlEsAgencyOrderAudit.setAuditor(user.getName());
			wlEsAgencyOrderAudit.setAuditState(auditState);
			wlEsAgencyOrderAudit.setAuditTime(new Date());
			wlEsAgencyOrderAudit.setOrderId(orderId);
			wlEsAgencyOrderAudit.setAuditResult(auditResult);
			this.saveObject(wlEsAgencyOrder);
			wlEsAgencyOrderAuditDao.saveObject(wlEsAgencyOrderAudit);
			// 发邮件
			try {
				WlCmOrgUser wlCmOrgUser = (WlCmOrgUser) wlCmOrgUserService.getObject(wlEsAgencyOrder.getUserId());
				// WlCmOrg wlCmOrg = (WlCmOrg) wlCmOrgService.getObject(wlCmOrgUser.getOrgId());
				if (!ValidateUtil.isEmpty(wlCmOrgUser.getEmail())) {// 邮件有填写，则发送邮件
					// 邮件内容
					MailUtil mailUtil = new MailUtil();
					String orderTime = DateUtil.dateToShortDateStr(wlEsAgencyOrder.getOrderTime());
					String content = "<div>您于" + orderTime + "，提交的" + wlEsAgencyOrder.getOrderNo() + "订单申请被驳回，请尽快处理！</div>";
					content += "<div>处理人:" + user.getName() + "</div>";
					content += "<div>驳回原因:" + wlEsAgencyOrderAudit.getAuditResult() + "</div>";
					String title = "【沃特罗伦】订单号" + wlEsAgencyOrder.getOrderNo() + "申请被驳回";
					mailUtil.sendHtmlEmail(wlCmOrgUser.getEmail(), title, content);
				}
			}
			catch (Exception e) {}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 更改订单状态为审核通过
	 * @param orderId 经销商订单id
	 * @param auditState 审核状态
	 * @param storageId 仓库id
	 * @param expectOutDate 预计出库日期
	 * @param user 当前登录用户
	 */
	public void updateOrderToAudit(String orderId, String auditState, String storageId, Date expectOutDate, User user) {
		try {
			WlEsAgencyOrder wlEsAgencyOrder = (WlEsAgencyOrder) this.getObject(orderId);
			wlEsAgencyOrder.setAgencyOrderStateEk(auditState);// 订单
			WlCmStorage wlCmStorage = (WlCmStorage) wlCmStorageService.getObject(storageId);// 仓库
			WlCmOrg wlCmOrg = (WlCmOrg) wlCmOrgService.getObject(wlEsAgencyOrder.getOrgId());
			// 订单审核
			WlEsAgencyOrderAudit wlEsAgencyOrderAudit = new WlEsAgencyOrderAudit();
			wlEsAgencyOrderAudit.setAuditor(user.getName());
			wlEsAgencyOrderAudit.setAuditState(auditState);
			wlEsAgencyOrderAudit.setAuditTime(new Date());
			wlEsAgencyOrderAudit.setOrderId(orderId);
			// 生成出库通知单
			List<WlEsAgencyOrderDetl> list = this.findAgencyOrderDetlList(orderId);// 订单货品列表
			// 出库通知单主表
			WlWmNoticeOut wlWmNoticeOut = new WlWmNoticeOut();
			wlWmNoticeOut.setNoticeNo(wlWmNoticeOutService.getNewNoticeNoCode());
			wlWmNoticeOut.setStorageId(storageId);
			wlWmNoticeOut.setStorageName(wlCmStorage.getStorageName());
			wlWmNoticeOut.setWmOutTypeEk("OUT_SALES");
			wlWmNoticeOut.setOrgId(wlCmOrg.getOrgId());
			wlWmNoticeOut.setOrgName(wlCmOrg.getOrgName());
			wlWmNoticeOut.setObjectTypeEk("AGENT");
			wlWmNoticeOut.setExpectOutDate(expectOutDate);
			wlWmNoticeOut.setLinkedBillNo(wlEsAgencyOrder.getOrderNo());
			wlWmNoticeOut.setLinkedMasId(wlEsAgencyOrder.getOrderId());
			wlWmNoticeOut.setCreateOprId(user.getId());
			wlWmNoticeOut.setCreateor(user.getName());
			wlWmNoticeOut.setCreateTime(new Date());
			wlWmNoticeOut.setBillStateEk("ISSUE");
			wlWmNoticeOut.setResultEk("NOT_EXECUTE");
			wlWmNoticeOut.setIssuer(user.getName());
			wlWmNoticeOut.setIssueTime(new Date());
			wlWmNoticeOut.setMemo(wlEsAgencyOrder.getMemo());
			wlWmNoticeOutService.saveObject(wlWmNoticeOut);// 保存出库单主表
			// 订单货品列表转通知单物料列表
			List<WlWmNoticeOutDetl> noticeOutDetlList = new ArrayList();
			double totalQty = 0;
			for (WlEsAgencyOrderDetl wlEsAgencyOrderDetl : list) {
				WlWmNoticeOutDetl wlWmNoticeOutDetl = new WlWmNoticeOutDetl();
				wlWmNoticeOutDetl.setNoticeOutId(wlWmNoticeOut.getNoticeOutId());
				wlWmNoticeOutDetl.setStorageId(wlCmStorage.getStorageId());
				wlWmNoticeOutDetl.setStorageName(wlCmStorage.getStorageName());
				wlWmNoticeOutDetl.setItemCd(wlEsAgencyOrderDetl.getItemCd());
				wlWmNoticeOutDetl.setItemId(wlEsAgencyOrderDetl.getItemId());
				wlWmNoticeOutDetl.setItemName(wlEsAgencyOrderDetl.getItemName());
				wlWmNoticeOutDetl.setCategoryId(wlEsAgencyOrderDetl.getCategoryId());
				wlWmNoticeOutDetl.setSpec(wlEsAgencyOrderDetl.getSpec());
				wlWmNoticeOutDetl.setBaseUnitId(wlEsAgencyOrderDetl.getBaseUnitId());
				wlWmNoticeOutDetl.setBaseUnitName(wlEsAgencyOrderDetl.getBaseUnitName());
				wlWmNoticeOutDetl.setBaseUnitQty(wlEsAgencyOrderDetl.getBaseUnitQty());
				totalQty += wlEsAgencyOrderDetl.getBaseUnitQty();
				wlWmNoticeOutDetl.setConsignee(wlEsAgencyOrderDetl.getConsignee());
				wlWmNoticeOutDetl.setContactWay(wlEsAgencyOrderDetl.getContactWay());
				wlWmNoticeOutDetl.setAddr(wlEsAgencyOrderDetl.getAddr());
				noticeOutDetlList.add(wlWmNoticeOutDetl);
			}
			wlWmNoticeOut.setTotalQty(totalQty);
			this.saveObject(wlEsAgencyOrder);// 保存经销商订单
			wlWmNoticeOutService.saveObject(wlWmNoticeOut);// 保存出库单主表
			wlWmNoticeOutDetlDao.saveList(noticeOutDetlList);// 保存出库通知单从表
			wlEsAgencyOrderAuditDao.saveObject(wlEsAgencyOrderAudit);// 生成订单审核信息
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}