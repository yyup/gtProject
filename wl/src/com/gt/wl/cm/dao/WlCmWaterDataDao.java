package com.gt.wl.cm.dao;

import java.util.List;
import java.util.Map;

import org.joyone.dao.BaseDao;
import org.joyone.lang.BaseException;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.cm.model.WlCmWaterData;

/**
 * 水压采集数据DAO层
 * @author liuyj
 * 
 */
@Repository("wl.cm.WlCmWaterDataDao")
public class WlCmWaterDataDao extends BaseDao {

	public WlCmWaterDataDao() {
		modelClass = WlCmWaterData.class;
		defaultOrder = "";
	}

	/**
	 * 查询有新上传水压数据的消防栓
	 * @return 消防栓序列号列表
	 */
	public List<Map> findNewWaterList() {
		String hql = "select new map(m.dataId as dataId,m.water as water,m.creator as creator,m.createTime as createTime,s.labelNo as labelNo,s.addr as addr,s.position as position) "
				+ " from WlCmWaterData m,WlCmHydrant s where m.hydrantId=s.hydrantId and m.dataState='1'" + " and m.createTime =(select min(o.createTime) from WlCmWaterData o where m.hydrantId=o.hydrantId AND o.dataState='1') ";
		try {
			return this.find(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过消防栓ID查找最后上报的水压数据
	 * @param hydrantId 消防栓ID
	 * @return 水压采集数据
	 */
	public WlCmWaterData getWlCmWaterData(String hydrantId) {
		WlCmWaterData wlCmWaterData = null;
		String hql = " from WlCmWaterData where hydrantId=? order by createTime desc";
		try {
			List<WlCmWaterData> list = this.find(hql, hydrantId);
			if (!ValidateUtil.isEmpty(list)) {
				wlCmWaterData = list.get(0);
			}
			return wlCmWaterData;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 更新采集数据状态为“0”
	 * @param hydrantId 消防栓ID
	 */
	public void updateWaterState(String hydrantId) {
		String hql = "update WlCmWaterData set dataState='0' where dataState='1' and hydrantId='" + hydrantId + "'";
		try {
			this.executeHql(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 更新所有采集数据状态为“0”
	 */
	public void updateWaterState() {
		String hql = "update WlCmWaterData set dataState='0' where dataState='1'";
		try {
			this.executeHql(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}
