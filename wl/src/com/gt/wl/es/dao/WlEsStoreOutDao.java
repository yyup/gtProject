package com.gt.wl.es.dao;

import java.util.List;

import org.joyone.dao.BaseDao;
import org.joyone.dao.dialect.FormatUtil;
import org.joyone.lang.BaseException;
import org.joyone.sys.Page;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.es.model.WlEsStoreOut;

/**
 * 出库DAO层
 * @author liuyj
 * 
 */
@Repository("wl.es.WlEsStoreOutDao")
public class WlEsStoreOutDao extends BaseDao {

	public WlEsStoreOutDao() {
		modelClass = WlEsStoreOut.class;
		defaultOrder = "";
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

			String hql = "from WlEsStoreOut  where 1=1 ";

			if (!ValidateUtil.isEmpty(keyId) && !ValidateUtil.isEmpty(keyValue)) {
				hql += " and " + keyId + " like '%" + keyValue + "%'";
			}
			// 销售日期开始
			if (!ValidateUtil.isEmpty(saleDateStart)) {
				hql += " and saleDate >=" + FormatUtil.formatStrForDB(saleDateStart);
			}
			// 销售日期结束
			if (!ValidateUtil.isEmpty(saleDateEnd)) {
				hql += " and saleDate <=" + FormatUtil.formatStrForDB(saleDateEnd);
			}
			// 发货日期开始
			if (!ValidateUtil.isEmpty(deliveryDateStart)) {
				hql += " and deliveryDate >=" + FormatUtil.formatStrForDB(deliveryDateStart);
			}
			// 发货日期结束
			if (!ValidateUtil.isEmpty(deliveryDateEnd)) {
				hql += " and deliveryDate <=" + FormatUtil.formatStrForDB(deliveryDateEnd);
			}
			// 发货日期结束
			if (!ValidateUtil.isEmpty(isRegFlag)) {
				hql += " and isRegFlag =" + FormatUtil.formatStrForDB(isRegFlag);
			}
			// 收货人
			if (!ValidateUtil.isEmpty(receiver)) {
				hql += " and receiver like '%" + receiver + "%'";
			}
			// 联系方式
			if (!ValidateUtil.isEmpty(contact)) {
				hql += " and contact like '%" + contact + "%'";
			}
			// 型号
			if (!ValidateUtil.isEmpty(productName)) {
				hql += " and productName like '%" + productName + "%'";
			}
			return this.findPage(hql, currPage, pageSize);
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
			String hql = "from WlEsStoreOut  where 1=1 ";
			if (!ValidateUtil.isEmpty(keyId) && !ValidateUtil.isEmpty(keyValue)) {
				hql += " and " + keyId + " like '%" + keyValue + "%'";
			}
			// 销售日期开始
			if (!ValidateUtil.isEmpty(saleDateStart)) {
				hql += " and saleDate >=" + FormatUtil.formatStrForDB(saleDateStart);
			}
			// 销售日期结束
			if (!ValidateUtil.isEmpty(saleDateEnd)) {
				hql += " and saleDate <=" + FormatUtil.formatStrForDB(saleDateEnd);
			}
			// 发货日期开始
			if (!ValidateUtil.isEmpty(deliveryDateStart)) {
				hql += " and deliveryDate >=" + FormatUtil.formatStrForDB(deliveryDateStart);
			}
			// 发货日期结束
			if (!ValidateUtil.isEmpty(deliveryDateEnd)) {
				hql += " and deliveryDate <=" + FormatUtil.formatStrForDB(deliveryDateEnd);
			}
			// 收货人
			if (!ValidateUtil.isEmpty(receiver)) {
				hql += " and receiver like '%" + receiver + "%'";
			}
			// 联系方式
			if (!ValidateUtil.isEmpty(contact)) {
				hql += " and contact like '%" + contact + "%'";
			}
			// 联系方式
			if (!ValidateUtil.isEmpty(productName)) {
				hql += " and productName like '%" + productName + "%'";
			}
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过设备编码得到出库对象
	 * @param deviceCd 设备编码
	 * @param receiver 收货人
	 * @param contact 联系方式
	 * @param itemName 物料名称
	 * @return 出库对象
	 */
	public WlEsStoreOut getWlEsStoreOut(String deviceCd, String receiver, String contact, String itemName) {
		String hql = "from WlEsStoreOut where 1=1 ";
		try {
			if (!ValidateUtil.isEmpty(deviceCd)) {
				hql += " and deviceCd=" + FormatUtil.formatStrForDB(deviceCd);
			}
			if (!ValidateUtil.isEmpty(receiver)) {
				hql += " and receiver=" + FormatUtil.formatStrForDB(receiver);
			}
			if (!ValidateUtil.isEmpty(contact)) {
				hql += " and contact=" + FormatUtil.formatStrForDB(contact);
			}
			if (!ValidateUtil.isEmpty(itemName)) {
				hql += " and itemName=" + FormatUtil.formatStrForDB(itemName);
			}
			hql += " order by  deliveryDate desc";
			List list = this.find(hql);
			if (!ValidateUtil.isEmpty(list)) {
				return (WlEsStoreOut) list.get(0);
			}
			return null;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
}
