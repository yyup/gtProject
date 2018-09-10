package com.gt.wl.cm.action;

import org.joyone.action.BaseAction;
import org.joyone.spring.Sc;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gt.wl.cm.service.WlCmSecurityUrlService;

@Controller
@RequestMapping("/wl/wlCmSecurityUrlAction.do")
public class WlCmSecurityUrlAction extends BaseAction {
	private WlCmSecurityUrlService wlCmSecurityUrlService = (WlCmSecurityUrlService) Sc.getBean("wl.cm.WlCmSecurityUrlService");

}