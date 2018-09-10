package com.gt.wl.cm.action;

import org.joyone.action.BaseAction;
import org.joyone.spring.Sc;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gt.wl.cm.service.WlCmParameterService;

@Controller
@RequestMapping("/wl/wlCmParameterAction.do")
public class WlCmParameterAction extends BaseAction {
	private WlCmParameterService wlCmParameterService = (WlCmParameterService) Sc.getBean("wl.cm.WlCmParameterService");

}