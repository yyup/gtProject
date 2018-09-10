package com.gt.wl.es.dao;

import java.util.List;

import org.joyone.dao.BaseDao;
import org.joyone.dao.dialect.FormatUtil;
import org.joyone.lang.BaseException;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.es.model.WlEsSerialRegPic;

/**
 * 序列号登记图片DAO层
 * @author liuyj
 * 
 */
@Repository("wl.es.WlEsSerialRegPicDao")
public class WlEsSerialRegPicDao extends BaseDao {

	public WlEsSerialRegPicDao() {
		modelClass = WlEsSerialRegPic.class;
		defaultOrder = "";
	}

	/**
	 * 通过登记ID获取上传图片列表
	 * @param regId 登记ID
	 * @return 图片列表
	 */
	public List<WlEsSerialRegPic> findPicList(String regId) {
		try {
			String hql = "from WlEsSerialRegPic where regId=?";
			return this.find(hql, regId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 删除序列号注册下的图片
	 * @param regId 序列号注册id
	 */
	public void deleteData(String regId) {
		try {
			String hql = "delete from WlEsSerialRegPic where 1=1";
			if (!ValidateUtil.isEmpty(regId)) {
				hql += " and regId=" + FormatUtil.formatStrForDB(regId);
			}
			this.executeHql(hql);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}
