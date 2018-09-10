package com.gt.wl.cm.service;


import org.joyone.lang.BaseException;
import org.joyone.service.BaseService;
import org.springframework.stereotype.Service;

import com.gt.wl.cm.dao.WlCmSellPicEnDao;
import com.gt.wl.cm.model.WlCmSellPicEn;
/**
 * 产品相册
 * @author huangbj
 *
 */
@Service("wl.cm.WlCmSellPicEnService")
public class WlCmSellPicEnService extends BaseService {
	private WlCmSellPicEnDao wlCmSellPicDao = new WlCmSellPicEnDao();

	public WlCmSellPicEnService() {
		baseDao = wlCmSellPicDao;
	}
	/**
	 * 获取上架商品的图片对象
	 * @param path 上架商品路径id
	 * @return 上架商品的图片对象
	 */
	public WlCmSellPicEn getWlCmSellPic(String path) {
		try {
			WlCmSellPicEn pic = (WlCmSellPicEn) wlCmSellPicDao.getObjectBySellId("", path);
			return pic;
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}
	

}