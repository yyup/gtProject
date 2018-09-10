package com.gt.wl.cm.action;

import org.joyone.action.BaseAction;
import org.joyone.spring.Sc;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gt.wl.cm.service.WlCmUserParameterService;

@Controller
@RequestMapping("/wl/wlCmUserParameterAction.do")
public class WlCmUserParameterAction extends BaseAction {
	private WlCmUserParameterService wlCmUserParameterService = (WlCmUserParameterService) Sc.getBean("wl.cm.WlCmUserParameterService");

}