package com.gt.wl.cm.dao;

import java.util.List;

import org.joyone.dao.BaseDao;
import org.joyone.dao.dialect.FormatUtil;
import org.joyone.lang.BaseException;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Repository;

import com.gt.wl.cm.model.WlCmSellPic;

/**
 * 上架产品相册dao层
 * @author huangbj
 * 
 */
@Repository("wl.cm.WlCmSellPicDao")
public class WlCmSellPicDao extends BaseDao {

	public WlCmSellPicDao() {
		modelClass = WlCmSellPic.class;
		defaultOrder = "";
	}

	/**
	 * 通过上架id删除上架商品的相册
	 * @param sellIds sellId 数组转化成dbString(格式'1','2','3')
	 */
	public void removeDataBySellId(String sellIds) {
		try {
			String hqlWhere = "  a.sellId in " + sellIds;
			String hql = "delete from WlCmSellPic  a where " + hqlWhere;
			this.executeHql(hql);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取上架商品的图片列表
	 * @param sellId 上架商品id
	 * @param isThumFlag 是否缩略图
	 * @param sellStateEk 商品上架状态
	 * @return 上架商品的图片列表
	 */
	public List findSellPic(String sellId, String isThumFlag, String sellStateEk) {
		try {
			String hqlWhere = "";
			String hql = "select new Map(s.picId as picId,s.sellId as sellId ,s.path as path,s.isThumFlag as isThumFlag ,w.productName as productName ) from WlCmSellPic as s,WlCmSell w where s.sellId=w.sellId ";
			if (!ValidateUtil.isEmpty(sellId)) {
				hqlWhere = " and s.sellId ='" + sellId + "'";
			}
			if (!ValidateUtil.isEmpty(isThumFlag)) {
				hqlWhere = " and s.isThumFlag ='" + isThumFlag + "'";
			}
			if (!ValidateUtil.isEmpty(sellStateEk)) {
				hqlWhere += " and w.sellStateEk=" + FormatUtil.formatStrForDB(sellStateEk);
			}
			hql = hql + hqlWhere + " order by s.sequ";
			List list = this.find(hql);
			return list;
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过 商品上架单Id 获取最大sequ的对象
	 * @param sellId 商品上架单Id
	 * @return 最大sequ的相册对象
	 */
	public Object getMaxObjectBySellId(String sellId) {
		try {
			String hqlWhere = "  a.sellId ='" + sellId + "' order by a.sequ desc";
			String hql = " from WlCmSellPic as a where " + hqlWhere;
			List list = this.find(hql);
			if (!ValidateUtil.isEmpty(list)) {
				return list.get(0);
			}
			return null;
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 获取相册对象
	 * @param sellId 上架商品id
	 * @param picId 图片id
	 * @return 相册对象
	 */
	public Object getObjectBySellId(String sellId, String picId) {
		try {
			String hqlWhere = "";
			if (!ValidateUtil.isEmpty(sellId)) {
				hqlWhere = " and a.sellId ='" + sellId + "'";
			}
			hqlWhere += "  and a.path ='" + picId + "'";
			String hql = " from WlCmSellPic  a where 1=1 " + hqlWhere;
			List list = this.find(hql);
			if (!ValidateUtil.isEmpty(list)) {
				return list.get(0);
			}
			return null;
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}
	/**
	 * 获取上架产品图片对象
	 * @param sellId  上架产品id
	 * @param isThumFlag 是否缩略图
	 * @return 上架产品图片对象
	 */
	public WlCmSellPic getSellPicByIsThumFlag(String sellId, String isThumFlag) {
		try {
				String hql = " from WlCmSellPic where 1=1 ";
				// 上架产品id
				if (!ValidateUtil.isEmpty(sellId)) {
					hql += " and sellId = " + FormatUtil.formatStrForDB(sellId);
				}
				//是否缩略图
				if (!ValidateUtil.isEmpty(isThumFlag)) {
					hql += " and isThumFlag = " + FormatUtil.formatStrForDB(isThumFlag);
				}
				List list = this.find(hql);
				WlCmSellPic wlCmSellPic = null;
				if(!ValidateUtil.isEmpty(list)){
					wlCmSellPic = (WlCmSellPic) list.get(0);
				}
				return  wlCmSellPic;
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}
