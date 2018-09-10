package com.gt.wl.cm.action;

import org.joyone.action.BaseAction;
import org.joyone.spring.Sc;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gt.wl.cm.service.WlCmSellPicService;

@Controller
@RequestMapping("/wl/wlCmSellPicAction.do")
public class WlCmSellPicAction extends BaseAction {
	private WlCmSellPicService wlCmSellPicService = (WlCmSellPicService) Sc.getBean("wl.cm.WlCmSellPicService");

}