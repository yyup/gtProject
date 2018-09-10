package com.gt.wl.cm.service;


import org.joyone.lang.BaseException;
import org.joyone.service.BaseService;
import org.springframework.stereotype.Service;

import com.gt.wl.cm.dao.WlCmSellPicDao;
import com.gt.wl.cm.model.WlCmSellPic;
/**
 * 产品相册
 * @author huangbj
 *
 */
@Service("wl.cm.WlCmSellPicService")
public class WlCmSellPicService extends BaseService {
	private WlCmSellPicDao wlCmSellPicDao = new WlCmSellPicDao();

	public WlCmSellPicService() {
		baseDao = wlCmSellPicDao;
	}
	/**
	 * 获取上架商品的图片对象
	 * @param path 上架商品路径id
	 * @return 上架商品的图片对象
	 */
	public WlCmSellPic getWlCmSellPic(String path) {
		try {
			WlCmSellPic pic = (WlCmSellPic) wlCmSellPicDao.getObjectBySellId("", path);
			return pic;
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}
	

}