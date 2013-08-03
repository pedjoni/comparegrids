package com.pjcode.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/jqgrid")
public class JQGridMediatorController {

	@RequestMapping
	public String getHomePage() {
		return "redirect:/jqgrid/main";
	}
	
	@RequestMapping(value="/main")
	public String getMainPage() {
		return "jqgrid/main";
	}
}