package com.gt.wl.es.dao;

import java.util.List;
import java.util.Map;

import org.joyone.dao.BaseDao;
import org.joyone.dao.dialect.FormatUtil;
import org.joyone.lang.BaseException;
import org.joyone.sys.Page;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.es.model.WlEsSerialReg;

/**
 * 序列号登记管理DAO层
 * @author liuyj
 * 
 */
@Repository("wl.es.WlEsSerialRegDao")
public class WlEsSerialRegDao extends BaseDao {

	public WlEsSerialRegDao() {
		modelClass = WlEsSerialReg.class;
		defaultOrder = "";
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
			// String hql = "from WlEsSerialReg where isManualFlag='1' ";
			String hql = "from WlEsSerialReg where 1=1 ";
			if (!ValidateUtil.isEmpty(isManualFlag)) {
				hql += " and isManualFlag=" + FormatUtil.formatStrForDB(isManualFlag);
			}
			if (!ValidateUtil.isEmpty(keyId) && !ValidateUtil.isEmpty(keyValue)) {
				hql += " and " + keyId + " like '%" + keyValue + "%'";
			}
			// 审核标志
			if (!ValidateUtil.isEmpty(auditState)) {
				hql += " and auditState=" + FormatUtil.formatStrForDB(auditState);
			}
			// 登记日期开始
			if (!ValidateUtil.isEmpty(regDateStart)) {
				hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("regDate"));
				hql += " >= " + sqlUtil.string2ShortDate(FormatUtil.formatStrForDB(regDateStart));
			}
			// 登记日期结束
			if (!ValidateUtil.isEmpty(regDateEnd)) {
				hql += " and " + sqlUtil.string2ShortDate(sqlUtil.date2String("regDate"));
				hql += " <= " + sqlUtil.string2ShortDate(FormatUtil.formatStrForDB(regDateEnd));
			}
			// 会员账号
			if (!ValidateUtil.isEmpty(account)) {
				hql += " and account like " + FormatUtil.formatStrForDB("%" + account + "%");
			}
			hql += "  order by createTime desc";
			return this.findPage(hql, currPage, pageSize);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询设备编码
	 * @param memberId 当前用户id
	 * @return 数据列表
	 */
	public List<Map> findDeviceCdList(String memberId) {
		try {
			String hql = "select new map(m.deviceCd as deviceCd)  from WlEsSerialReg m where m.auditState='1' and m.memberId='" + memberId + "'";
			return this.find(hql);
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
			String hql = "select new map(s.frameExpDate as frameExpDate,s.accExpDate as accExpDate) from WlEsSerialReg s  where 1=1";
			// 序列号
			if (paraMap.containsKey("deviceCd")) {
				String deviceCd = (String) paraMap.get("deviceCd");
				if (!ValidateUtil.isEmpty(deviceCd)) {
					hql += " and s.deviceCd = " + FormatUtil.formatStrForDB(deviceCd);
				}
			}
			return this.find(hql);
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
			String hql = "from WlEsSerialReg t where t.deviceCd=? and t.auditState=?";
			List list = this.find(hql, new Object[] { deviceCd, auditState });
			if (!ValidateUtil.isEmpty(list)) {
				return (WlEsSerialReg) list.get(0);
			}
			return null;
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new BaseException(e, log);
		}
	}
}
