package com.gt.wl.cm.action;

import org.joyone.action.BaseAction;
import org.joyone.spring.Sc;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gt.wl.cm.service.WlCmUserGroupService;

@Controller
@RequestMapping("/wl/cm/wlCmUserGroupAction.do")
public class WlCmUserGroupAction extends BaseAction {
	private WlCmUserGroupService wlCmUserGroupService = (WlCmUserGroupService) Sc.getBean("wl.cm.WlCmUserGroupService");

}