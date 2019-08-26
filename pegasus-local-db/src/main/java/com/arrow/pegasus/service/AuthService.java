package com.arrow.pegasus.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arrow.pegasus.CoreAuditLog;
import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.data.AuditLogBuilder;
import com.arrow.pegasus.data.security.Auth;
import com.arrow.pegasus.repo.AuthRepository;

import moonstone.acs.AcsLogicalException;

@Service
public class AuthService extends BaseServiceAbstract {

	@Autowired
	private AuthRepository authRepository;

	public AuthRepository getAuthRepository() {
		return authRepository;
	}

	public Auth create(Auth auth, String who) {
		String method = "create";

		// logical checks
		if (auth == null) {
			logInfo(method, "auth is null");
			throw new AcsLogicalException("auth is null");
		}

		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}

		if (StringUtils.isEmpty(auth.getCompanyId())) {
			logInfo(method, "companyId is empty");
			throw new AcsLogicalException("companyId is empty");
		}

		if (auth.getLdap() == null && auth.getSaml() == null) {
			logInfo(method, "ldap and saml are null, one must be provided");
			throw new AcsLogicalException("ldap and saml are null, one must be provided");
		}

		// persist
		auth = authRepository.doInsert(auth, who);

		getAuditLogService().save(AuditLogBuilder.create().type(CoreAuditLog.Auth.CREATE_AUTH)
		        .productName(ProductSystemNames.PEGASUS).objectId(auth.getId()).by(who));

		return auth;
	}

	public Auth update(Auth auth, String who) {
		String method = "update";

		// logical checks
		if (auth == null) {
			logInfo(method, "auth is null");
			throw new AcsLogicalException("auth is null");
		}

		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}

		if (StringUtils.isEmpty(auth.getCompanyId())) {
			logInfo(method, "companyId is empty");
			throw new AcsLogicalException("companyId is empty");
		}

		if (auth.getLdap() == null && auth.getSaml() == null) {
			logInfo(method, "ldap and saml are null, one must be provided");
			throw new AcsLogicalException("ldap and saml are null, one must be provided");
		}

		// persist
		auth = authRepository.doSave(auth, who);

		getAuditLogService().save(AuditLogBuilder.create().type(CoreAuditLog.Auth.UPDATE_AUTH)
		        .productName(ProductSystemNames.PEGASUS).objectId(auth.getId()).by(who));

		// clear cache (condition for NPE inside CacheEvict annotation)
		getCoreCacheService().clearAuth(getCoreCacheService().findAuthById(auth.getId()));

		return auth;
	}

	public Long deleteByCompanyId(String companyId, String who) {
		String method = "deleteByCompanyId";
		logInfo(method, "companyId: %s, who: %s", companyId, who);
		return authRepository.deleteByCompanyId(companyId);
	}
}
