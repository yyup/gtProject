package com.gt.wl.cm.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joyone.action.BaseAction;
import org.joyone.lang.BaseException;
import org.joyone.spring.Sc;
import org.joyone.sys.Page;
import org.joyone.sys.Session;
import org.joyone.sys.User;
import org.joyone.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gt.wl.cm.model.WlCmInfo;
import com.gt.wl.cm.service.WlCmFocusPicService;
import com.gt.wl.cm.service.WlCmInfoService;
import com.gt.wl.cm.service.WlCmSellService;
import com.gt.wl.es.service.WlEsMyShoppingCartService;

@Controller
@RequestMapping("/wl/cm/WlCmIndexAction.dox")
public class WlCmIndexAction extends BaseAction {
	private WlCmFocusPicService wlCmFocusPicService = (WlCmFocusPicService) Sc.getBean("wl.cm.WlCmFocusPicService");
	private WlEsMyShoppingCartService wlEsMyShoppingCartService = (WlEsMyShoppingCartService) Sc.getBean("wl.es.WlEsMyShoppingCartService");
	private WlCmSellService wlCmSellService = (WlCmSellService) Sc.getBean("wl.cm.WlCmSellService");
	private WlCmInfoService wlCmInfoService = (WlCmInfoService) Sc.getBean("wl.cm.WlCmInfoService");

	/***
	 * 返回官网首页信息，焦点图片， 上架商品缩略图，视频，新闻
	 * @return 官网首页信息（焦点图片， 上架商品缩略图，视频，新闻）
	 */
	@RequestMapping(params = "action=init")
	@ResponseBody
	public String init() {
		try {
			List focusPicList = wlCmFocusPicService.findFocusPic("1", "", "");// 焦点图片
			List sellPicList = wlCmSellService.findSellPic(null, "1", "HAS_SHELVE");// 上架商品缩略图
			Page page = wlCmInfoService.search(7, 1, "0106", null, "2", "1", "", "","");// 新闻
			List<WlCmInfo> wlCmInfoNewList = page.getItems();
			List newList = new ArrayList();
			for (WlCmInfo wlCmInfo : wlCmInfoNewList) {
				// String content = wlCmInfo.getContent();
				// wlCmInfo.setContent(SubStringHtmlUtil.subStringHTML(content, 200, "..."));
				Map map = new HashMap();
				map.put("infoId", wlCmInfo.getInfoId());
				map.put("title", wlCmInfo.getTitle());
				newList.add(map);
			}
			page = wlCmInfoService.search(2, 1, "0107", null, "2", "1", "", "","");// 视频
			List<WlCmInfo> wlCmInfoVideoList = page.getItems();
			List videoList = new ArrayList();
			for (WlCmInfo wlCmInfo : wlCmInfoVideoList) {
				Map map = new HashMap();
				String content = wlCmInfo.getContent();
				int sourceIndex = content.indexOf("source");
				int sourceEndIndex = content.indexOf("source", sourceIndex+1);
				
				content = content.substring(sourceIndex, sourceEndIndex);
				int startIndex = content.indexOf("\"");
				int endIndex = content.indexOf("\"",startIndex+1);
				
				content = content.substring(startIndex+1, endIndex);
//				int embedIndex = content.indexOf("embed");
//				// 替换宽
//				int widthIndex = content.indexOf("width", embedIndex);
//				int startIndex = content.indexOf("\"", widthIndex);
//				int endIndex = content.indexOf("\"", startIndex + 1);
//				content = content.substring(0, startIndex + 1) + "490" + content.substring(endIndex, content.length());
//				// 替换高
//				int heightIndex = content.indexOf("height", embedIndex);
//				startIndex = content.indexOf("\"", heightIndex);
//				endIndex = content.indexOf("\"", startIndex + 1);
//				content = content.substring(0, startIndex + 1) + "240" + content.substring(endIndex, content.length());
//				// wlCmInfo.setContent(content);
				map.put("content", content);
				videoList.add(map);
			}

			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("focusPicList", focusPicList);
			resultMap.put("sellPicList", sellPicList);
			resultMap.put("wlCmInfoNewList", newList);
			resultMap.put("wlCmInfoVideoList", videoList);
			return this.getJson(true, resultMap);
		}
		catch (BaseException e) {
			throw new BaseException(e, log);
		}
	}
	/**
	 * /**
	 * 查询我的购物车列表
	 * @param pageSize 页数
	 * @param currPage 当前页
	 * @param isThumFlag 是否缩略图
	 * @param sellIds sellIds数组
	 * @return 我的购物车列表
	 */
	@RequestMapping(params = "action=findMyShoppingCartPage")
	@ResponseBody
	public String findMyShoppingCartPage(int pageSize, int currPage, String isThumFlag, String[] sellIds) {
		try {
			User user = Session.getCurrUser();
			Page page = wlEsMyShoppingCartService.findMyShoppingCartPage(pageSize, currPage, user.getId(), isThumFlag, sellIds,"");
			return this.getJson(true, page);
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}
	
	/**
	 * 删除购物车商品
	 * @param shoppingCartId 购物车id
	 * @param ids 购物车ids数组
	 * @return 空
	 */
	@RequestMapping(params = "action=delCarGoods")
	@ResponseBody
	public String delCarGoods(String shoppingCartId,String[] ids) {
		try {
			//单个删除
			if(!ValidateUtil.isEmpty(shoppingCartId)){
				wlEsMyShoppingCartService.deleteObject(shoppingCartId);
			}
			//批量删除
			if(!ValidateUtil.isNull(ids)){
				wlEsMyShoppingCartService.deleteObject(ids);
			}
			return this.getJson(true, "");
		}
		catch (Exception e) {
			throw new BaseException(e, log);
		}
	}

}