package com.pjcode.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/demo1")
public class Demo1MediatorController {

	@RequestMapping
	public String getHomePage() {
		return "redirect:/demo1/main";
	}
	
	@RequestMapping(value="/main")
	public String getMainPage() {
		return "demo1/main";
	}
}