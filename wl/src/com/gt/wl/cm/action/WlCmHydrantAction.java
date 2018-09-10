package com.gt.wl.cm.action;

import org.joyone.action.BaseAction;
import org.joyone.spring.Sc;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gt.wl.cm.service.WlCmHydrantService;

@Controller
@RequestMapping("/wl/wlCmHydrantAction.do")
public class WlCmHydrantAction extends BaseAction {
	private WlCmHydrantService wlCmHydrantService = (WlCmHydrantService) Sc.getBean("wl.cm.WlCmHydrantService");

}