package com.arrow.rhea.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.rhea.web.RheaWebVersion;

import moonstone.acs.client.model.VersionModel;

@RestController
@RequestMapping("/api/rhea/webapp")
public class RheaWebAppController extends ControllerAbstract {

	@RequestMapping("/version")
	public VersionModel version() {
		return RheaWebVersion.get();
	}
}
