package com.arrow.rhea.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.arrow.pegasus.service.BaseServiceAbstract;

public abstract class RheaServiceAbstract extends BaseServiceAbstract {

	@Autowired
	private RheaCacheService rheaCacheService;

	protected RheaCacheService getRheaCacheService() {
		return rheaCacheService;
	}
}