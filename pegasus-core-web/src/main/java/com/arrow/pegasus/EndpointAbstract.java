package com.arrow.pegasus;

import org.springframework.beans.factory.annotation.Autowired;

import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.Product;
import com.arrow.pegasus.service.AuditLogService;
import com.arrow.pegasus.service.CoreCacheHelper;
import com.arrow.pegasus.service.CoreCacheService;
import com.arrow.pegasus.service.CryptoService;
import com.arrow.pegasus.service.EventService;

import moonstone.acs.AcsRuntimeException;
import moonstone.acs.AcsSystemException;

public abstract class EndpointAbstract extends LifeCycleAbstract {
	public final static int DEFAULT_PAGE_SIZE = 10;

	@Autowired
	private EventService eventService;
	@Autowired
	private CoreCacheService coreCacheService;
	@Autowired
	private CoreCacheHelper coreCacheHelper;
	@Autowired
	private AuditLogService auditLogService;
	@Autowired
	private CryptoService cryptoService;

	public EndpointAbstract() {
		logInfo(getClass().getSimpleName(), "...");
	}

	protected boolean validateProduct(String applicationId, String productSystemName) {
		String method = "validateProduct";

		Application application = getCoreCacheService().findApplicationById(applicationId);
		checkEnabled(application, "application");

		Product product = getCoreCacheService().findProductById(application.getProductId());
		checkEnabled(product, "product");

		String systemName = product.getSystemName();
		logDebug(method, "checking %s vs %s", systemName, productSystemName);

		return systemName.equals(productSystemName);
	}

	protected AcsRuntimeException handleException(Exception e) {
		String method = "handleException";
		logError(method, e);
		if (e.getClass().isAssignableFrom(AcsRuntimeException.class)) {
			return (AcsRuntimeException) e;
		} else {
			return new AcsSystemException("Exception encountered: " + e.getClass().getName(), e);
		}
	}

	protected EventService getEventService() {
		return eventService;
	}

	protected CoreCacheService getCoreCacheService() {
		return coreCacheService;
	}

	protected CoreCacheHelper getCoreCacheHelper() {
		return coreCacheHelper;
	}

	protected AuditLogService getAuditLogService() {
		return auditLogService;
	}

	public CryptoService getCryptoService() {
		return cryptoService;
	}
}
