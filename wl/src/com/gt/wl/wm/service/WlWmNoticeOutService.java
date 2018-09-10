package com.gt.wl.wm.service;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
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

import com.gt.wl.cm.dao.WlCmOrgDao;
import com.gt.wl.cm.dao.WlCmStorageAreaDao;
import com.gt.wl.cm.dao.WlCmStorageDao;
import com.gt.wl.cm.model.WlCmCategory;
import com.gt.wl.cm.model.WlCmDocument;
import com.gt.wl.cm.model.WlCmDocumentPath;
import com.gt.wl.cm.model.WlCmItem;
import com.gt.wl.cm.model.WlCmOrg;
import com.gt.wl.cm.model.WlCmStorage;
import com.gt.wl.cm.model.WlCmStorageArea;
import com.gt.wl.cm.service.WlCmCategoryService;
import com.gt.wl.cm.service.WlCmDocumentPathService;
import com.gt.wl.cm.service.WlCmDocumentService;
import com.gt.wl.cm.service.WlCmItemService;
import com.gt.wl.wm.dao.WlWmNoticeOutDao;
import com.gt.wl.wm.dao.WlWmNoticeOutDetlDao;
import com.gt.wl.wm.dao.WlWmStoreOutDetlDao;
import com.gt.wl.wm.model.WlWmNoticeOut;
import com.gt.wl.wm.model.WlWmNoticeOutDetl;

/**
 * 出库通知Service层
 * @author liuyj
 * 
 */
@Service("wl.wm.WlWmNoticeOutService")
public class WlWmNoticeOutService extends BaseService {
	private WlWmNoticeOutDao wlWmNoticeOutDao = (WlWmNoticeOutDao) Sc.getBean("wl.wm.WlWmNoticeOutDao");
	private WlWmNoticeOutDetlDao wlWmNoticeOutDetlDao = (WlWmNoticeOutDetlDao) Sc.getBean("wl.wm.WlWmNoticeOutDetlDao");
	private WlCmOrgDao wlCmOrgDao = (WlCmOrgDao) Sc.getBean("wl.cm.WlCmOrgDao");
	private WlCmStorageDao wlCmStorageDao = (WlCmStorageDao) Sc.getBean("wl.cm.WlCmStorageDao");
	private WlCmStorageAreaDao wlCmStorageAreaDao = (WlCmStorageAreaDao) Sc.getBean("wl.cm.WlCmStorageAreaDao");
	private WlWmStoreOutDetlDao wlWmStoreOutDetlDao = (WlWmStoreOutDetlDao) Sc.getBean("wl.wm.WlWmStoreOutDetlDao");
	private WlCmDocumentService wlCmDocumentService = (WlCmDocumentService) Sc.getBean("wl.cm.WlCmDocumentService");
	private WlCmDocumentPathService pathService = (WlCmDocumentPathService) Sc.getBean("wl.cm.WlCmDocumentPathService");
	private WlCmItemService wlCmItemService = (WlCmItemService) Sc.getBean("wl.cm.WlCmItemService");
	private WlCmCategoryService wlCmCategoryService = (WlCmCategoryService) Sc.getBean("wl.cm.WlCmCategoryService");

	public WlWmNoticeOutService() {
		baseDao = wlWmNoticeOutDao;
	}

	/**
	 * 获取待办单数
	 * @param storageId 仓库ID（为空或null则查询所有仓库）
	 * @return 待办单数
	 */
	public long getTodoCount(String storageId) {
		try {
			return wlWmNoticeOutDao.getTodoCount(storageId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询出库通知单（分页）
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param paraMap 条件
	 * @return 分页结果
	 */
	public Page search(int currPage, int pageSize, Map paraMap) {
		try {
			return wlWmNoticeOutDao.search(currPage, pageSize, paraMap);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询出库通知单（分页）按型号分组
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param paraMap 条件
	 * @param categoryId 父节点类别id，多个用逗号隔开
	 * @return 分页结果
	 */
	public Page searchSpec(int currPage, int pageSize, Map paraMap, String categoryId) {
		try {
			//
			String result = "";
			if (!ValidateUtil.isEmpty(categoryId)) {
				String[] categoryIds = categoryId.split(",");
				List<WlCmCategory> list = wlCmCategoryService.findChildList(categoryIds, "1", "");
				String[] childCategoryIds = new String[list.size() + categoryIds.length];
				int i = 0;
				for (WlCmCategory wlCmCategory : list) {
					childCategoryIds[i] = wlCmCategory.getCategoryId();
					i++;
				}
				for (String str : categoryIds) {
					childCategoryIds[i] = str;
					i++;
				}
				result = ConvertUtil.toDbString(childCategoryIds);
			}
			paraMap.put("categoryId", result);
			return wlWmNoticeOutDao.searchSpec(currPage, pageSize, paraMap);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过出库通知单ID获取明细
	 * @param noticeOutId 出库通知单ID
	 * @return 明细列表
	 */
	public List<WlWmNoticeOutDetl> findNoticeOutList(String noticeOutId) {
		try {
			return wlWmNoticeOutDetlDao.findNoticeOutDetlList(noticeOutId);
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
	public String getNewNoticeNoCode() {
		try {
			List<String> list = wlWmNoticeOutDao.getNewNoticeNoCode();
			Date nowDate = new Date();
			String newCode = "";
			if (!ValidateUtil.isEmpty(list)) {
				String nowStr = DateUtil.dateFormatFromDateToString(nowDate, "yyyyMM");
				nowStr = "NO" + nowStr;
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
				nowStr = "NO" + nowStr;
				newCode = nowStr + "001";
			}

			return newCode;
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 新增/编辑数据
	 * @param wlWmNoticeOut 出库通知单对象
	 * @param wlWmNoticeOutDetlList 出库通知单从表对象数组
	 * @param user 当前登录用户
	 * @return
	 */
	public void saveData(WlWmNoticeOut wlWmNoticeOut, List<WlWmNoticeOutDetl> wlWmNoticeOutDetlList, User user) {
		try {
			double totalQty = 0;
			WlCmStorage wlCmStorage = (WlCmStorage) wlCmStorageDao.getObject(wlWmNoticeOut.getStorageId());
			wlWmNoticeOut.setStorageName(wlCmStorage.getStorageName());
			if (!ValidateUtil.isEmpty(wlWmNoticeOut.getStorageAreaId())) {
				WlCmStorageArea wlCmStorageArea = (WlCmStorageArea) wlCmStorageAreaDao.getObject(wlWmNoticeOut.getStorageAreaId());
				wlWmNoticeOut.setStorageAreaName(wlCmStorageArea.getStorageAreaName());
			}
			if (!ValidateUtil.isEmpty(wlWmNoticeOut.getOrgId())) {
				WlCmOrg wlCmOrg = (WlCmOrg) wlCmOrgDao.getObject(wlWmNoticeOut.getOrgId());
				wlWmNoticeOut.setOrgName(wlCmOrg.getOrgName());
			}
			if (ValidateUtil.isEmpty(wlWmNoticeOut.getNoticeOutId())) {// 新增
				wlWmNoticeOut.setBillStateEk("NO_ISSUE");
				wlWmNoticeOut.setResultEk("NOT_EXECUTE");
				wlWmNoticeOut.setCreateor(user.getName());
				wlWmNoticeOut.setCreateTime(new Date());
				wlWmNoticeOut.setNoticeNo(this.getNewNoticeNoCode());
			}
			this.saveObject(wlWmNoticeOut);
			if (!ValidateUtil.isEmpty(wlWmNoticeOutDetlList)) {
				String[] noticeOutIds = new String[1];
				noticeOutIds[0] = wlWmNoticeOut.getNoticeOutId();
				wlWmNoticeOutDetlDao.deleteByNoticeInId(ConvertUtil.toDbString(noticeOutIds));
				for (WlWmNoticeOutDetl wlWmNoticeOutDetl : wlWmNoticeOutDetlList) {
					WlWmNoticeOutDetl newWlWmNoticeOutDetl = new WlWmNoticeOutDetl();
					newWlWmNoticeOutDetl.setNoticeOutId(wlWmNoticeOut.getNoticeOutId());
					newWlWmNoticeOutDetl.setStorageId(wlWmNoticeOut.getStorageId());
					newWlWmNoticeOutDetl.setStorageName(wlCmStorage.getStorageName());
					newWlWmNoticeOutDetl.setItemId(wlWmNoticeOutDetl.getItemId());
					newWlWmNoticeOutDetl.setItemName(wlWmNoticeOutDetl.getItemName());
					newWlWmNoticeOutDetl.setItemCd(wlWmNoticeOutDetl.getItemCd());
					newWlWmNoticeOutDetl.setCategoryId(wlWmNoticeOutDetl.getCategoryId());
					newWlWmNoticeOutDetl.setSpec(wlWmNoticeOutDetl.getSpec());
					newWlWmNoticeOutDetl.setBaseUnitId(wlWmNoticeOutDetl.getBaseUnitId());
					newWlWmNoticeOutDetl.setBaseUnitName(wlWmNoticeOutDetl.getBaseUnitName());
					newWlWmNoticeOutDetl.setBaseUnitQty(wlWmNoticeOutDetl.getBaseUnitQty());
					newWlWmNoticeOutDetl.setConsignee(wlWmNoticeOut.getConsignee());
					newWlWmNoticeOutDetl.setContactWay(wlWmNoticeOut.getContactWay());
					newWlWmNoticeOutDetl.setAddr(wlWmNoticeOut.getAddr());
					totalQty += wlWmNoticeOutDetl.getBaseUnitQty();
					newWlWmNoticeOutDetl.setModifier(user.getName());
					newWlWmNoticeOutDetl.setModifyTime(new Date());
					wlWmNoticeOutDetlDao.saveObject(newWlWmNoticeOutDetl);
				}
				wlWmNoticeOut.setTotalQty(totalQty);
				this.saveObject(wlWmNoticeOut);
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询出库通知单从表的所有物料名称
	 * @param noticeOutId 出库通知单id
	 * @param flag 调用标志（web端调用为0，APP端调用为1）
	 * @return 物料的名字，格式为"划船器等6种物料"
	 */
	public String getAllItemName(String noticeOutId, int flag) {
		try {
			String result = "";
			List<WlWmNoticeOutDetl> list = wlWmNoticeOutDetlDao.findNoticeOutDetlList(noticeOutId);
			if (!ValidateUtil.isEmpty(list)) {
				if (list.size() == 1) {
					WlWmNoticeOutDetl wlWmNoticeOutDetl = list.get(0);
					result += wlWmNoticeOutDetl.getItemName() + "(" + wlWmNoticeOutDetl.getSpec() + ")";
				}
				else if (list.size() > 1) {
					WlWmNoticeOutDetl wlWmNoticeOutDetl = list.get(0);
					result += wlWmNoticeOutDetl.getItemName() + "(" + wlWmNoticeOutDetl.getSpec() + ")" + Lang.getString("wl.wm.WlWmNoticeOutDetlService.kinds");
					if (flag == 0) {
						result += list.size() + Lang.getString("wl.wm.WlWmNoticeOutDetlService.items");
					}
					else {
						result += list.size() + Lang.getString("wl.wm.WlWmNoticeOutDetlService.itemName");
					}
				}
			}
			return result;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询出库通知单对象数组
	 * @param noticeInOutIds 出库通知单ids数组
	 * @param billStateEk 单据状态
	 * @param resultEk 结果状态
	 * @return 通知单对象列表
	 */
	public List findNoticeOutList(String[] noticeInOutIds, String billStateEk, String resultEk) {
		try {
			String result = "";
			if (!ValidateUtil.isEmpty(noticeInOutIds)) {
				result = ConvertUtil.toDbString(noticeInOutIds);
			}
			return wlWmNoticeOutDao.findNoticeOutList(result, billStateEk, resultEk);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取出库通知单的通知单单号，用,隔开
	 * @param noticeInOutIds 入库通知单ids数组
	 * @return 通知单号，单号之间用，隔开，格式为1,2,3
	 */
	public String getNoticeNo(String[] noticeInOutIds) {
		try {
			String result = "";
			for (String noticeOutId : noticeInOutIds) {
				WlWmNoticeOut wlWmNoticeOut = (WlWmNoticeOut) this.getObject(noticeOutId);
				result += wlWmNoticeOut.getNoticeNo() + ",";
			}
			if (result.length() > 0) {
				result = result.substring(0, result.length() - 1);
			}
			return result;
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 更改出库通知单的状态
	 * @param noticeOutIds 出库通知单ids数组
	 * @param billStateEk 单据状态
	 * @param user 当前登录用户
	 */
	public void updateNoticeOutState(String[] noticeOutIds, String billStateEk, User user) {
		try {
			String result = "";
			if (!ValidateUtil.isEmpty(noticeOutIds)) {
				result = ConvertUtil.toDbString(noticeOutIds);
			}
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			wlWmNoticeOutDao.updateNoticeOutState(result, billStateEk, user.getName(), sdf.format(date));
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 删除出库通知单
	 * @param noticeOutIds 出库单id数组
	 */
	public void removeData(String[] noticeOutIds) {
		try {
			wlWmNoticeOutDetlDao.deleteByNoticeInId(ConvertUtil.toDbString(noticeOutIds));
			wlWmNoticeOutDao.removeData(ConvertUtil.toDbString(noticeOutIds));
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过通知单号获取出库通知单
	 * @param noticeNo 通知单号
	 * @return 出库通知单对象
	 */
	public WlWmNoticeOut getData(String noticeNo) {
		try {
			return wlWmNoticeOutDao.getData(noticeNo);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查找出库发货的相关信息
	 * @param noticeOutIds id数组转化成dbString，格式为('1','2','3')
	 * @return 出库发货的相关信息
	 */
	public List findNoticeOutAndDetlList(String[] noticeOutIds) {
		try {
			String result = "";
			if (!ValidateUtil.isEmpty(noticeOutIds)) {
				result = ConvertUtil.toDbString(noticeOutIds);
			}
			return wlWmNoticeOutDao.findNoticeOutAndDetlList(result);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 备货数据
	 * @param paraMap 条件
	 * @return 备货数据
	 */
	public List findStoreOutStockList(Map paraMap) {
		try {
			return wlWmNoticeOutDao.findStoreOutStockList(paraMap);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 保存上传数据
	 * @param path 上传文件ID
	 * @param wlCmOrg 经销商对象
	 * @param wlCmStorage 仓库对象
	 * @param expectOutDate 预计出库日期
	 * @param wmOutTypeEk 出库类型
	 * @param user 当前登录用户
	 */
	public synchronized void saveExcelData(String path, WlCmOrg wlCmOrg, WlCmStorage wlCmStorage, Date expectOutDate, String wmOutTypeEk, User user) {
		try {
			WlCmDocument doc = (WlCmDocument) wlCmDocumentService.getObject(path);
			WlCmDocumentPath docPath = (WlCmDocumentPath) pathService.findBySourceCode(doc.getSourceCode());
			File file = new File(Config.uploadFilesPath + "//" + docPath.getPath(), doc.getFileName());
			this.findExcelList(file, wlCmOrg, wlCmStorage, expectOutDate, wmOutTypeEk, user);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * excel文件转换为出库对象列表
	 * @param file excel文件
	 * @param noticeOutId 通知单id
	 * @param user 当前登录用户
	 * @return 经销商订单明细和总数量
	 */
	private void findExcelList(File file, WlCmOrg wlCmOrg, WlCmStorage wlCmStorage, Date expectOutDate, String wmOutTypeEk, User user) {
		try {
			List<WlWmNoticeOutDetl> outDetllist = new ArrayList<WlWmNoticeOutDetl>();
			Map<String, Integer> noticeMap = new HashMap<String, Integer>();// 出库单主表对应的列表索引，key为姓名+手机号
			List<WlWmNoticeOut> noticeList = new ArrayList<WlWmNoticeOut>();// 出库单列表
			Map<String, Integer> itemMap = new HashMap<String, Integer>();// 用于判断是否存在姓名+手机号+物料编码一致的
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
				String content = cells[0].getContents();
				// 行记录序号如没有值，则结束返回
				if (ValidateUtil.isEmpty(content)) break;
				String itemCd = cells[5].getContents().trim();// 物料编码
				String consignee = cells[10].getContents().trim();// 收货人
				String contactWay = cells[11].getContents().trim();// 联系方式（手机号码）
				if (ValidateUtil.isEmpty(itemCd)) {// 物料编码为空
					throw new BaseException((i + 1) + Lang.getString("wl.es.wlWmNoticeOutService.col") + "物料编码为空，请检查！");
				}
				if (ValidateUtil.isEmpty(consignee)) {// 收件人不允许为空
					throw new BaseException((i + 1) + Lang.getString("wl.es.wlWmNoticeOutService.col") + itemCd + Lang.getString("wl.es.wlWmNoticeOutService.consigneeIsEmpty"));
				}
				if (ValidateUtil.isEmpty(contactWay)) {// 联系方式不允许为空
					throw new BaseException((i + 1) + Lang.getString("wl.es.wlWmNoticeOutService.col") + itemCd + Lang.getString("wl.es.wlWmNoticeOutService.contactWayIsEmpty"));
				}
				contactWay = contactWay.replaceAll("[^0-9,]", "");// 联系方式去除数字和逗号外的符号
				if (itemMap.containsKey(consignee + contactWay + itemCd)) {// 存在收货人+手机号+物料编码一致的收货人
					String result = "";
					result += Lang.getString("wl.es.wlWmNoticeOutService.consignee") + consignee;
					result += Lang.getString("wl.es.wlWmNoticeOutService.contactWay") + contactWay + ",";
					result += itemMap.get(consignee + contactWay + itemCd) + Lang.getString("wl.es.wlWmNoticeOutService.col") + "与";
					result += (i + 1) + Lang.getString("wl.es.wlWmNoticeOutService.col");
					result += Lang.getString("wl.es.wlWmNoticeOutService.sameItem") + itemCd;
					throw new BaseException(result);
				}
				else {
					itemMap.put(consignee + contactWay + itemCd, i + 1);
				}
				// 获取出库单主表
				WlWmNoticeOut wlWmNoticeOut = null;
				if (noticeMap.containsKey(consignee + contactWay)) {// 此收货人对应的主表已经存在
					int index = noticeMap.get(consignee + contactWay);
					wlWmNoticeOut = noticeList.get(index);
				}
				else {// 主表不存在，马上生成
					wlWmNoticeOut = this.getNoticeOut(wlCmOrg, wlCmStorage, expectOutDate, wmOutTypeEk, user);
					wlWmNoticeOut.setConsignee(consignee);
					wlWmNoticeOut.setContactWay(contactWay);
					noticeMap.put(consignee + contactWay, noticeList.size());
					noticeList.add(wlWmNoticeOut);
				}
				// 构造从表
				WlWmNoticeOutDetl wlWmNoticeOutDetl = new WlWmNoticeOutDetl();// 出库单从表
				wlWmNoticeOutDetl.setNoticeOutId(wlWmNoticeOut.getNoticeOutId());
				wlWmNoticeOutDetl.setStorageId(wlCmStorage.getStorageId());
				wlWmNoticeOutDetl.setStorageName(wlCmStorage.getStorageName());
				wlWmNoticeOutDetl.setModifyTime(new Date());
				wlWmNoticeOutDetl.setModifier(user.getName());
				wlWmNoticeOutDetl.setConsignee(consignee);
				wlWmNoticeOutDetl.setContactWay(contactWay);
				// 设置对象值
				WlCmItem wlCmItem = wlCmItemService.getItemByItemCd(itemCd);
				if (!ValidateUtil.isNull(wlCmItem)) {
					wlWmNoticeOutDetl.setItemCd(wlCmItem.getItemCd());
					wlWmNoticeOutDetl.setItemId(wlCmItem.getItemId());
					wlWmNoticeOutDetl.setItemName(wlCmItem.getItemName());
					wlWmNoticeOutDetl.setCategoryId(wlCmItem.getCategoryId());
					wlWmNoticeOutDetl.setSpec(wlCmItem.getSpec());
					wlWmNoticeOutDetl.setBaseUnitId(wlCmItem.getBaseUnitId());
					wlWmNoticeOutDetl.setBaseUnitName(wlCmItem.getBaseUnitName());
				}
				else {// 物料编码不存在
					throw new BaseException((i + 1) + Lang.getString("wl.es.wlWmNoticeOutService.col") + itemCd + Lang.getString("wl.es.wlWmNoticeOutService.noExistItemCd"));
				}
				String tempBaseUnitQty = cells[6].getContents().trim();
				if (!ValidateUtil.isEmpty(tempBaseUnitQty)) {
					double baseUnitQty = Double.parseDouble(tempBaseUnitQty);
					if (baseUnitQty > 0) {
						// total += baseUnitQty;
						wlWmNoticeOut.setTotalQty(wlWmNoticeOut.getTotalQty() + baseUnitQty);
						wlWmNoticeOutDetl.setBaseUnitQty(baseUnitQty);
					}
					else {// 数量小于0
						throw new BaseException((i + 1) + Lang.getString("wl.es.wlWmNoticeOutService.col") + itemCd + Lang.getString("wl.es.wlWmNoticeOutService.baseUnitQtyGtZero"));

					}
				}
				else {// 数量不允许为空
					throw new BaseException((i + 1) + Lang.getString("wl.es.wlWmNoticeOutService.col") + itemCd + Lang.getString("wl.es.wlWmNoticeOutService.baseUnitQtyIsEmpty"));
				}
				String addr = cells[12].getContents();
				if (!ValidateUtil.isEmpty(addr)) {
					addr = addr.replaceAll("\\t", "");
					wlWmNoticeOutDetl.setAddr(addr);
				}
				else {// 地址不允许为空
					throw new BaseException((i + 1) + Lang.getString("wl.es.wlWmNoticeOutService.col") + itemCd + Lang.getString("wl.es.wlWmNoticeOutService.addrIsEmpty"));
				}
				outDetllist.add(wlWmNoticeOutDetl);
			}
			// resultList.add(outDetllist);// noticeList
			wlWmNoticeOutDetlDao.saveList(outDetllist);// 保存主表
			wlWmNoticeOutDao.saveList(noticeList);// 保存从表
			fis.close();
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 保存并返回出库单主表
	 * @param wlCmOrg 经销商对象
	 * @param wlCmStorage 仓库对象
	 * @param expectOutDate 预计出库日期
	 * @param wmOutTypeEk 出库类型
	 * @param user 当前登录用户
	 * @return 出库单主表
	 */
	public WlWmNoticeOut getNoticeOut(WlCmOrg wlCmOrg, WlCmStorage wlCmStorage, Date expectOutDate, String wmOutTypeEk, User user) {
		try {
			// 出库通知单主表
			WlWmNoticeOut wlWmNoticeOut = new WlWmNoticeOut();
			wlWmNoticeOut.setNoticeNo(this.getNewNoticeNoCode());
			wlWmNoticeOut.setStorageId(wlCmStorage.getStorageId());
			wlWmNoticeOut.setStorageName(wlCmStorage.getStorageName());
			wlWmNoticeOut.setWmOutTypeEk(wmOutTypeEk);
			wlWmNoticeOut.setOrgId(wlCmOrg.getOrgId());
			wlWmNoticeOut.setOrgName(wlCmOrg.getOrgName());
			wlWmNoticeOut.setObjectTypeEk("AGENT");
			wlWmNoticeOut.setExpectOutDate(expectOutDate);
			wlWmNoticeOut.setCreateOprId(user.getId());
			wlWmNoticeOut.setCreateor(user.getName());
			wlWmNoticeOut.setCreateTime(new Date());
			wlWmNoticeOut.setBillStateEk("NO_ISSUE");
			wlWmNoticeOut.setResultEk("NOT_EXECUTE");
			wlWmNoticeOut.setIssuer(user.getName());
			wlWmNoticeOut.setIssueTime(new Date());
			this.saveObject(wlWmNoticeOut);// 保存出库单主表
			return wlWmNoticeOut;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}