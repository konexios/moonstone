package com.arrow.pegasus.service;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseServiceAbstract extends ServiceAbstract {

	@Autowired
	private CoreCacheService coreCacheService;
	@Autowired
	private CoreCacheHelper coreCacheHelper;
	@Autowired
	private AuditLogService auditLogService;
	@Autowired
	private CryptoService cryptoService;

	protected CoreCacheService getCoreCacheService() {
		return coreCacheService;
	}

	protected CoreCacheHelper getCoreCacheHelper() {
		return coreCacheHelper;
	}

	protected AuditLogService getAuditLogService() {
		return auditLogService;
	}

	protected CryptoService getCryptoService() {
		return cryptoService;
	}
}
