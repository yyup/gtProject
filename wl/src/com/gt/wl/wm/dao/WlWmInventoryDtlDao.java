package com.gt.wl.wm.dao;

import java.util.List;

import org.joyone.dao.BaseDao;
import org.joyone.dao.dialect.FormatUtil;
import org.joyone.lang.BaseException;
import org.joyone.util.ConvertUtil;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.wm.model.WlWmInventoryDtl;

/**
 * 库存序号明细DAO层
 * @author liuyj
 * 
 */
@Repository("wl.wm.WlWmInventoryDtlDao")
public class WlWmInventoryDtlDao extends BaseDao {

	public WlWmInventoryDtlDao() {
		modelClass = WlWmInventoryDtl.class;
		defaultOrder = "";
	}

	/**
	 * 获取库存明细(通过序列号)
	 * @param serialNo 序列号
	 * @return 序号明细对象，无则返回null
	 */
	public WlWmInventoryDtl getDataBySerialNo(String serialNo) {
		WlWmInventoryDtl wlWmInventoryDtl = null;
		try {
			String hql = "from WlWmInventoryDtl where isStore='1' and serialNo=?";
			List list = this.find(hql, serialNo);
			if (!ValidateUtil.isEmpty(list)) {
				wlWmInventoryDtl = (WlWmInventoryDtl) list.get(0);
			}
			return wlWmInventoryDtl;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取库存明细(通过库存ID、序列号)
	 * @param inventoryId 库存ID
	 * @param serialNo 序列号
	 * @return 序号明细对象，无则返回null
	 */
	public WlWmInventoryDtl getData(String inventoryId, String serialNo) {
		WlWmInventoryDtl wlWmInventoryDtl = null;
		try {
			String hql = "from WlWmInventoryDtl where inventoryId=? and serialNo=?";
			List list = this.find(hql, new Object[] { inventoryId, serialNo });
			if (!ValidateUtil.isEmpty(list)) {
				wlWmInventoryDtl = (WlWmInventoryDtl) list.get(0);
			}
			return wlWmInventoryDtl;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取库存明细(通过仓库ID、物料CD、序列号)
	 * @param storageId 仓库ID
	 * @param itemCd 物料代码
	 * @param serialNo 序列号
	 * @return 序号明细对象，无则返回null
	 */
	public WlWmInventoryDtl getDataBySerialNo(String storageId, String itemCd, String serialNo) {
		WlWmInventoryDtl wlWmInventoryDtl = null;
		try {
			String hql = "select m from WlWmInventoryDtl m,WlWmInventory s where m.inventoryId=s.inventoryId and s.storageId=? and s.itemCd=? and m.serialNo=?";
			List list = this.find(hql, new Object[] { storageId, itemCd, serialNo });
			if (!ValidateUtil.isEmpty(list)) {
				wlWmInventoryDtl = (WlWmInventoryDtl) list.get(0);
			}
			return wlWmInventoryDtl;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 更改设备序号状态
	 * @param serialNoArray 序列号数组
	 */
	public void updateData(String[] serialNoArray) {
		try {
			String hql = "update WlWmInventoryDtl set isStore='0' where serialNo in " + ConvertUtil.toDbString(serialNoArray);
			this.executeHql(hql);

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查找序列号列表
	 * @param inventoryId 库存id
	 * @param serialNo 序列号
	 * @param isStore 是否在库
	 * @return 序列号列表
	 */
	public List<WlWmInventoryDtl> findSerialNoList(String inventoryId, String serialNo, String isStore) {
		try {
			String hql = "from WlWmInventoryDtl where 1=1";
			if (!ValidateUtil.isEmpty(inventoryId)) {
				hql += " and inventoryId=" + FormatUtil.formatStrForDB(inventoryId);
			}
			if (!ValidateUtil.isEmpty(serialNo)) {
				hql += " and serialNo like " + FormatUtil.formatStrForDB("%" + serialNo + "%");
			}
			if (!ValidateUtil.isEmpty(isStore)) {
				hql += " and isStore=" + FormatUtil.formatStrForDB(isStore);
			}
			hql += " order by serialNo";
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}
