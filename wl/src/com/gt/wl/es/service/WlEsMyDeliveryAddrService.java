package com.gt.wl.es.service;

import java.util.List;

import org.joyone.lang.BaseException;
import org.joyone.service.BaseService;
import org.joyone.spring.Sc;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Service;

import com.gt.wl.es.dao.WlEsMyDeliveryAddrDao;
import com.gt.wl.es.model.WlEsMyDeliveryAddr;

/**
 * 我的收货地址Service层
 * @author liuyj
 * 
 */

@Service("wl.es.WlEsMyDeliveryAddrService")
public class WlEsMyDeliveryAddrService extends BaseService {
	private WlEsMyDeliveryAddrDao wlEsMyDeliveryAddrDao = (WlEsMyDeliveryAddrDao) Sc.getBean("wl.es.WlEsMyDeliveryAddrDao");

	public WlEsMyDeliveryAddrService() {
		baseDao = wlEsMyDeliveryAddrDao;
	}

	/**
	 * 通过会员ID得到收货地址
	 * @param memberId 会员ID
	 * @return 收货地址
	 */
	public String getDeliverAddrByMemId(String memberId) {
		try {
			return wlEsMyDeliveryAddrDao.getDeliverAddrByMemId(memberId);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过会员ID得到收货地址列表
	 * @param memberId 会员id
	 * @return 地址列表
	 */
	public List<WlEsMyDeliveryAddr> getMyDeliveryAddrList(String memberId) {
		try {
			return wlEsMyDeliveryAddrDao.findMyDeliveryAddrList(memberId);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 通过会员ID得到默认收货地址
	 * @param memberId 会员id
	 * @param isDefaultFlag 是否默认地址
	 * @return 地址列表
	 */
	public WlEsMyDeliveryAddr getMyDeliveryAddrData(String memberId, String isDefaultFlag) {
		try {
			return wlEsMyDeliveryAddrDao.getMyDeliveryAddrData(memberId, isDefaultFlag);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 保存收货地址
	 * @param myDeliveryAddr 收货地址
	 * @param memberId 会员ID
	 */
	public void saveData(WlEsMyDeliveryAddr myDeliveryAddr, String memberId) {
		try {
			if ("1".equals(myDeliveryAddr.getIsDefaultFlag())) {
				WlEsMyDeliveryAddr myDefalutDeliveryAddr = wlEsMyDeliveryAddrDao.getMyDeliveryAddrData(memberId, "1");

				if (!ValidateUtil.isNull(myDefalutDeliveryAddr) && myDefalutDeliveryAddr.getDeliveryAddrId().equals(myDeliveryAddr.getDeliveryAddrId())) {
					myDefalutDeliveryAddr.setZipCd(myDeliveryAddr.getZipCd());
					myDefalutDeliveryAddr.setAccount(myDeliveryAddr.getAccount());
					myDefalutDeliveryAddr.setAddr(myDeliveryAddr.getAddr());
					myDefalutDeliveryAddr.setArea(myDeliveryAddr.getArea());
					myDefalutDeliveryAddr.setAreaId(myDeliveryAddr.getAreaId());
					myDefalutDeliveryAddr.setCity(myDeliveryAddr.getCity());
					myDefalutDeliveryAddr.setCityId(myDeliveryAddr.getCityId());
					myDefalutDeliveryAddr.setCreateTime(myDeliveryAddr.getCreateTime());
					myDefalutDeliveryAddr.setCreator(myDeliveryAddr.getCreator());
					myDefalutDeliveryAddr.setIsDefaultFlag(myDeliveryAddr.getIsDefaultFlag());
					myDefalutDeliveryAddr.setMemberId(myDeliveryAddr.getMemberId());
					myDefalutDeliveryAddr.setMobile(myDeliveryAddr.getMobile());
					myDefalutDeliveryAddr.setModifier(myDeliveryAddr.getModifier());
					myDefalutDeliveryAddr.setModifyTime(myDeliveryAddr.getModifyTime());
					myDefalutDeliveryAddr.setProvince(myDeliveryAddr.getProvince());
					myDefalutDeliveryAddr.setProvinceId(myDeliveryAddr.getProvinceId());
					myDefalutDeliveryAddr.setReceiver(myDeliveryAddr.getReceiver());

					wlEsMyDeliveryAddrDao.saveObject(myDefalutDeliveryAddr);
				}
				else {
					if (!ValidateUtil.isNull(myDefalutDeliveryAddr)) {
						myDefalutDeliveryAddr.setIsDefaultFlag("0");
						wlEsMyDeliveryAddrDao.saveObject(myDefalutDeliveryAddr);
					}
					wlEsMyDeliveryAddrDao.saveObject(myDeliveryAddr);
				}
			}
			else {
				wlEsMyDeliveryAddrDao.saveObject(myDeliveryAddr);
			}
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}

	/**
	 * 设置为默认的收货地址
	 * @param deliveryAddrId 要设置的收货地址ID
	 * @param memberId 会员ID
	 */
	public void setAsDefaultAddr(String deliveryAddrId, String memberId) {
		try {
			WlEsMyDeliveryAddr myDefalutDeliveryAddr = wlEsMyDeliveryAddrDao.getMyDeliveryAddrData(memberId, "1");
			if (!ValidateUtil.isNull(myDefalutDeliveryAddr)) {
				myDefalutDeliveryAddr.setIsDefaultFlag("0");
				wlEsMyDeliveryAddrDao.saveObject(myDefalutDeliveryAddr);
			}

			WlEsMyDeliveryAddr myDeliveryAddr = (WlEsMyDeliveryAddr) wlEsMyDeliveryAddrDao.getObject(deliveryAddrId);
			myDeliveryAddr.setIsDefaultFlag("1");
			wlEsMyDeliveryAddrDao.saveObject(myDeliveryAddr);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}
}