package com.gt.wl.cm.service;

import java.util.List;
import java.util.Map;

import org.joyone.lang.BaseException;
import org.joyone.service.BaseService;
import org.joyone.spring.Sc;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Service;

import com.gt.wl.cm.dao.WlCmWaterDataDao;
import com.gt.wl.cm.model.WlCmHydrant;
import com.gt.wl.cm.model.WlCmWaterData;

/**
 * 水压采集数据service层
 * @author liuyj
 * 
 */
@Service("wl.cm.WlCmWaterDataService")
public class WlCmWaterDataService extends BaseService {
	private WlCmWaterDataDao wlCmWaterDataDao = (WlCmWaterDataDao) Sc.getBean("wl.cm.WlCmWaterDataDao");
	private WlCmHydrantService wlCmHydrantService = (WlCmHydrantService) Sc.getBean("wl.cm.WlCmHydrantService");

	public WlCmWaterDataService() {
		baseDao = wlCmWaterDataDao;
	}

	/**
	 * 保存采集数据
	 * @param wlCmWaterData 采集数据对象
	 */
	public void saveData(WlCmWaterData wlCmWaterData) {
		try {
			// 陈该标签之前采集的数据状态置为“0”
			wlCmWaterDataDao.updateWaterState(wlCmWaterData.getHydrantId());
			this.saveObject(wlCmWaterData);

		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 查询有新上传水压数据的消防栓
	 * @return 消防栓序列号列表
	 */
	public List<Map> findNewWaterList() {
		try {
			return wlCmWaterDataDao.findNewWaterList();
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 更新消防栓数据状态为“0”
	 * @param list 消防栓序列号列表
	 */
	public void updateWaterState(List<Map> list) {
		try {
			for (Map map : list) {
				String dataId = (String) map.get("dataId");
				// 获得消防栓对象
				WlCmWaterData wlCmWaterData = (WlCmWaterData) wlCmWaterDataDao.getObject(dataId);
				wlCmWaterData.setDataState("0");
				wlCmWaterDataDao.updateObject(wlCmWaterData);

			}
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 更新所有采集数据状态为“0”
	 */
	public void updateWaterState() {
		try {
			wlCmWaterDataDao.updateWaterState();
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过消防栓ID查找最后上报的水压数据明细
	 * @param hydrantId 消防栓ID
	 * @return 水压采集数据
	 */
	public WlCmWaterData getWlCmWaterData(String hydrantId) {
		WlCmWaterData wlCmWaterData = null;
		try {
			WlCmHydrant wlCmHydrant = (WlCmHydrant) wlCmHydrantService.getObject(hydrantId);
			wlCmWaterData = wlCmWaterDataDao.getWlCmWaterData(hydrantId);
			if (ValidateUtil.isNull(wlCmWaterData)) {
				wlCmWaterData = new WlCmWaterData();
				wlCmWaterData.setCreateTime(null);
			}
			wlCmWaterData.setLabelNo(wlCmHydrant.getLabelNo());
			wlCmWaterData.setAddr(wlCmHydrant.getAddr());
			return wlCmWaterData;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}