package com.arrow.pegasus.dashboard.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.arrow.pegasus.service.CoreCacheHelper;
import com.arrow.pegasus.service.CoreCacheService;
import com.arrow.pegasus.service.ServiceAbstract;

public abstract class DashboardServiceAbstract extends ServiceAbstract {

	@Autowired
	private CoreCacheService coreCacheService;
	@Autowired
	private CoreCacheHelper coreCacheHelper;

	protected CoreCacheService getCoreCacheService() {
		return coreCacheService;
	}

	protected CoreCacheHelper getCoreCacheHelper() {
		return coreCacheHelper;
	}
}
