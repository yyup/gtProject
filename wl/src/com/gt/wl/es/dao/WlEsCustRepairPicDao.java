package com.gt.wl.es.dao;

import java.util.List;

import org.joyone.dao.BaseDao;
import org.joyone.dao.dialect.FormatUtil;
import org.joyone.lang.BaseException;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.es.model.WlEsCustRepairPic;

/**
 * 售后维修上传图片视频DAO层
 * @author liuyj
 * 
 */
@Repository("wl.es.WlEsCustRepairPicDao")
public class WlEsCustRepairPicDao extends BaseDao {

	public WlEsCustRepairPicDao() {
		modelClass = WlEsCustRepairPic.class;
		defaultOrder = "";
	}

	/**
	 * 查询售后维修上传的图片或视频列表
	 * @param repairId 售后维修ID
	 * @param isVideoFlag 是否视频标志（0-否 1-是）
	 * @return 图片或视频列表
	 */
	public List<WlEsCustRepairPic> findRepairPicList(String repairId, String isVideoFlag) {
		try {
			String hql = "from WlEsCustRepairPic where repairId=? and isVideoFlag=?";
			return this.find(hql, new Object[] { repairId, isVideoFlag });
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
			String hql = "delete from WlEsCustRepairPic where 1=1";
			if (!ValidateUtil.isEmpty(repairId)) {
				hql += " and repairId=" + FormatUtil.formatStrForDB(repairId);
			}
			if (!ValidateUtil.isEmpty(path)) {
				path += " and path=" + FormatUtil.formatStrForDB(path);
			}

			this.executeHql(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}
