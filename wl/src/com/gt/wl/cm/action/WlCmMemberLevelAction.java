package com.gt.wl.cm.action;

import org.joyone.action.BaseAction;
import org.joyone.spring.Sc;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gt.wl.cm.service.WlCmMemberLevelService;

@Controller
@RequestMapping("/wl/cm/wlCmMemberLevelAction.do")
public class WlCmMemberLevelAction extends BaseAction {
	private WlCmMemberLevelService wlCmMemberLevelService = (WlCmMemberLevelService) Sc.getBean("wl.cm.WlCmMemberLevelService");

}