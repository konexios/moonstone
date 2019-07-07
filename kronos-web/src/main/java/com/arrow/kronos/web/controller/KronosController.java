package com.arrow.kronos.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class KronosController extends ControllerAbstract {

	public KronosController() {
		logInfo(getClass().getSimpleName(), "...");
	}

	@RequestMapping("/")
	public String index() {
		return "index.html";
	}
}
