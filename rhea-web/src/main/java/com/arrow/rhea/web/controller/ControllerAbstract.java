package com.arrow.rhea.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.service.AuditLogService;
import com.arrow.pegasus.webapi.WebApiAbstract;
import com.arrow.rhea.service.RheaCacheService;

public abstract class ControllerAbstract extends WebApiAbstract {

	@Autowired
	private RheaCacheService rheaCacheService;

	public RheaCacheService getRheaCacheService() {
		return rheaCacheService;
	}

	@Autowired
	private AuditLogService auditLogService;

	protected AuditLogService getAuditLogService() {
		return auditLogService;
	}

	protected boolean isSystemAdmin(User user) {
		Assert.notNull(user, "user is null");
		return user.isAdmin();
	}

	protected boolean isEditableOrIsSystemAdmin(boolean editable, User user) {
		return editable || !editable && isSystemAdmin(user);
	}
}