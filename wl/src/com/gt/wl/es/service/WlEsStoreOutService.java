package com.gt.wl.es.service;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.joyone.util.DateUtil;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Service;

import com.gt.wl.cm.model.WlCmDocument;
import com.gt.wl.cm.model.WlCmDocumentPath;
import com.gt.wl.cm.service.WlCmCommonSetService;
import com.gt.wl.cm.service.WlCmDocumentPathService;
import com.gt.wl.cm.service.WlCmDocumentService;
import com.gt.wl.es.dao.WlEsStoreOutDao;
import com.gt.wl.es.model.WlEsStoreOut;
import com.gt.wl.wm.model.WlWmStoreOutDetl;
import com.gt.wl.wm.service.WlWmStoreOutService;

/**
 * 出库Service层
 * @author liuyj
 * 
 */

@Service("wl.es.WlEsStoreOutService")
public class WlEsStoreOutService extends BaseService {
	private WlEsStoreOutDao wlEsStoreOutDao = (WlEsStoreOutDao) Sc.getBean("wl.es.WlEsStoreOutDao");
	private WlCmDocumentService wlCmDocumentService = (WlCmDocumentService) Sc.getBean("wl.cm.WlCmDocumentService");
	private WlCmDocumentPathService pathService = (WlCmDocumentPathService) Sc.getBean("wl.cm.WlCmDocumentPathService");
	private WlWmStoreOutService wlWmStoreOutService = (WlWmStoreOutService) Sc.getBean("wl.wm.WlWmStoreOutService");
	private WlCmCommonSetService wlCmCommonSetService = (WlCmCommonSetService) Sc.getBean("wl.cm.WlCmCommonSetService");

	public WlEsStoreOutService() {
		baseDao = wlEsStoreOutDao;
	}

	/**
	 * 查询设备序列号分页数据
	 * @param pageSize 页数
	 * @param currPage 当前页
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
	 * @return 分页结果
	 */
	public Page search(int currPage, int pageSize, String keyId, String keyValue, String saleDateStart, String saleDateEnd, String deliveryDateStart, String deliveryDateEnd, String isRegFlag, String receiver, String contact, String productName) {
		try {
			return wlEsStoreOutDao.search(currPage, pageSize, keyId, keyValue, saleDateStart, saleDateEnd, deliveryDateStart, deliveryDateEnd, isRegFlag, receiver, contact, productName);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询设备序列号数据
	 * @param keyId 字段名
	 * @param keyValue 字段值
	 * @param saleDateStart 销售日期开始
	 * @param saleDateEnd 销售日期结束
	 * @param deliveryDateStart 发货日期开始
	 * @param deliveryDateEnd 发货日期结束
	 * @param receiver 收货人
	 * @param contact 联系方式
	 * @param productName 型号
	 * @return 结果
	 */
	public List<WlEsStoreOut> findOutList(String keyId, String keyValue, String saleDateStart, String saleDateEnd, String deliveryDateStart, String deliveryDateEnd, String receiver, String contact, String productName) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			List<WlEsStoreOut> list = wlEsStoreOutDao.findOutList(keyId, keyValue, saleDateStart, saleDateEnd, deliveryDateStart, deliveryDateEnd, receiver, contact, productName);
			for (WlEsStoreOut wlEsStoreOut : list) {
				if (!ValidateUtil.isNull(wlEsStoreOut.getSaleDate())) {
					wlEsStoreOut.setSaleDates(sdf.format(wlEsStoreOut.getSaleDate()));
				}
				wlEsStoreOut.setDeliveryDates(sdf.format(wlEsStoreOut.getDeliveryDate()));
			}
			return list;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 保存上传数据
	 * @param path 上传文件ID
	 */
	public void saveData(String path) {
		try {
			WlCmDocument doc = (WlCmDocument) wlCmDocumentService.getObject(path);
			WlCmDocumentPath docPath = (WlCmDocumentPath) pathService.findBySourceCode(doc.getSourceCode());
			File file = new File(Config.uploadFilesPath + "//" + docPath.getPath(), doc.getFileName());
			// 转换为List
			List list = findExcelList(file);
			// 校验
			validateData(list);
			// 保存
			wlEsStoreOutDao.saveList(list);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * excel文件转换为出库对象列表
	 * @param file excel文件
	 * @return 出库对象列表
	 */
	private List<WlEsStoreOut> findExcelList(File file) {
		List<WlEsStoreOut> list = new ArrayList<WlEsStoreOut>();
		try {
			// 创建输入流
			FileInputStream fis = new FileInputStream(file);
			// 获取Excel文件对象
			jxl.Workbook rwb = Workbook.getWorkbook(fis);
			// 获取文件的第一个工作表
			Sheet sheet = rwb.getSheet(0);
			// 行数(表头的目录不需要，从1开始)
			for (int i = 1; i < sheet.getRows(); i++) {
				Cell[] cells = sheet.getRow(i);
				if (cells.length == 0) {
					break;
				}
				WlEsStoreOut wlEsStoreOut = new WlEsStoreOut();
				String content = cells[0].getContents();
				// 行记录序号如没有值，则结束返回
				if (ValidateUtil.isEmpty(content)) break;
				// 设置对象值
				String saleDate = cells[0].getContents();
				if (saleDate.length() <= 8) {
					saleDate = "20" + saleDate;
				}
				String deliveryDate = cells[1].getContents();
				if (deliveryDate.length() <= 8) {
					deliveryDate = "20" + deliveryDate;
				}
				wlEsStoreOut.setSaleDate(DateUtil.shortDateStrToDate(saleDate));
				wlEsStoreOut.setDeliveryDate(DateUtil.shortDateStrToDate(deliveryDate));
				wlEsStoreOut.setAgency(cells[2].getContents());
				wlEsStoreOut.setProductName(cells[3].getContents());
				wlEsStoreOut.setNum(Long.parseLong(cells[4].getContents()));
				wlEsStoreOut.setPrice(Double.parseDouble(cells[5].getContents()));
				wlEsStoreOut.setAccessories(cells[6].getContents());
				String accPrice = cells[7].getContents();
				if (ValidateUtil.isEmpty(accPrice)) {
					wlEsStoreOut.setAccPrice(0);
				}
				else {
					wlEsStoreOut.setAccPrice(Double.parseDouble(cells[7].getContents()));
				}
				wlEsStoreOut.setReceiver(cells[8].getContents());
				wlEsStoreOut.setContact(cells[9].getContents());
				wlEsStoreOut.setAddr(cells[10].getContents());
				wlEsStoreOut.setZipCd(cells[11].getContents());
				wlEsStoreOut.setDeviceCd(cells[12].getContents());
				if (cells.length > 13) {
					wlEsStoreOut.setProvince(cells[13].getContents());
					if (cells.length > 14) {
						if (!ValidateUtil.isEmpty(cells[14].getContents())) {
							wlEsStoreOut.setSalePrice(Double.parseDouble(cells[14].getContents()));
						}
					}
				}
				// 销售年月采用销售日期
				// String[] year = cells[15].getContents().split("-");
				String[] year = saleDate.split("-");
				wlEsStoreOut.setSaleYear(Integer.parseInt(year[0]));
				wlEsStoreOut.setSaleMonth(Integer.parseInt(year[1]));
				if (cells.length > 15) {
					wlEsStoreOut.setLogistic(cells[15].getContents());
					wlEsStoreOut.setTrackNo(cells[16].getContents());
				}
				// 加入list
				list.add(wlEsStoreOut);
			}
			fis.close();
			return list;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 校验设备序列是否已存在
	 * 如已存在，则抛出异常
	 * @param list 出库列表
	 */
	private void validateData(List<WlEsStoreOut> list) {
		try {
			// for (WlEsStoreOut wlEsStoreOut : list) {
			for (int i = 0; i < list.size(); i++) {
				WlEsStoreOut iWlEsStoreOut = list.get(i);
				// 通过设备编码判断
				WlEsStoreOut out = wlEsStoreOutDao.getWlEsStoreOut(iWlEsStoreOut.getDeviceCd(), "", "", "");
				if (!ValidateUtil.isNull(out)) {
					throw new BaseException("【" + out.getDeviceCd() + "】" + Lang.getString("wl.es.wlEsStoreOutAction.deviceNoExists"));
				}
				// 判断list数组里面是否有重复
				for (int j = i + 1; j < list.size(); j++) {
					WlEsStoreOut jWlEsStoreOut = list.get(j);
					if (iWlEsStoreOut.getDeviceCd().equals(jWlEsStoreOut.getDeviceCd())) {
						throw new BaseException("【" + iWlEsStoreOut.getDeviceCd() + "】" + Lang.getString("wl.es.wlEsStoreOutService.excelHasRepeatDevice"));
					}
				}
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 校验设备序列是否已存在
	 * 
	 * @param deviceCd 序列号
	 * @return 出库单
	 */
	public WlEsStoreOut validateDeviceCdData(String deviceCd) {
		try {

			WlEsStoreOut out = wlEsStoreOutDao.getWlEsStoreOut(deviceCd, "", "", "");
			return out;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/***
	 * 将出库单的序列号保存到序列号登记
	 */
	public synchronized void saveStoreOutData() {
		try {
			List<Map> list = wlWmStoreOutService.getTodayBill();
			for (Map resultMap : list) {
				String serialNo = resultMap.get("serialNo").toString().trim();
				WlEsStoreOut wlEsStoreOut = this.validateDeviceCdData(serialNo);
				if (ValidateUtil.isNull(wlEsStoreOut)) {// 如果序列号不存在，则保存
					wlEsStoreOut = new WlEsStoreOut();
					wlEsStoreOut.setCreateTime(new Date());
				}
				else {// 已存在则修改
					wlEsStoreOut.setModifyTime(new Date());
				}
				// if (ValidateUtil.isNull(wlEsStoreOut)) {// 如果序列号不存在，则保存

				String itemName = (String) resultMap.get("itemName");
				String outDate = resultMap.get("outDate").toString();
				String orgName = resultMap.get("orgName").toString();
				String spec = resultMap.get("spec").toString();
				String consignee = resultMap.get("consignee").toString();
				String contactWay = resultMap.get("contactWay").toString();
				String addr = resultMap.get("addr").toString();
				wlEsStoreOut.setItemName(itemName);
				// WlEsStoreOut wlEsStoreOut = new WlEsStoreOut();
				wlEsStoreOut.setDeliveryDate(DateUtil.shortDateStrToDate(outDate));
				// wlEsStoreOut.setSaleDate(DateUtil.shortDateStrToDate(outDate));
				// String[] year = outDate.split("-");
				// wlEsStoreOut.setSaleYear(Integer.parseInt(year[0]));
				// wlEsStoreOut.setSaleMonth(Integer.parseInt(year[1]));
				wlEsStoreOut.setAgency(orgName);
				wlEsStoreOut.setProductName(spec);
				wlEsStoreOut.setReceiver(consignee.trim());
				wlEsStoreOut.setContact(contactWay);
				wlEsStoreOut.setAddr(addr);
				wlEsStoreOut.setDeviceCd(serialNo);
				wlEsStoreOut.setNum(1);
				wlEsStoreOut.setIsRegFlag("0");
				// wlEsStoreOut.setCreateTime(new Date());
				wlEsStoreOutDao.saveObject(wlEsStoreOut);
				// }
			}
			// 查找蓝牙模块
			String itemName = wlCmCommonSetService.getWlCmCommonSetByKey("ITEM_NAME").getSetValue();
			String today = DateUtil.dateToShortDateStr(new Date());
			List<WlWmStoreOutDetl> blueToothList = wlWmStoreOutService.findWlWmStoreOutDetlList("", today, "WR800CN");
			for (WlWmStoreOutDetl wlWmStoreOutDetl : blueToothList) {
				WlEsStoreOut out = wlEsStoreOutDao.getWlEsStoreOut("", wlWmStoreOutDetl.getConsignee(), wlWmStoreOutDetl.getContactWay(), itemName);
				if (!ValidateUtil.isNull(out)) {
					if (ValidateUtil.isEmpty(out.getMemo())) {
						out.setMemo("购买蓝牙模块" + today);
					}
					else {
						out.setMemo(out.getMemo() + ",购买蓝牙模块" + today);
					}
					wlEsStoreOutDao.saveObject(out);
				}
			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}