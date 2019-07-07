package com.arrow.pegasus.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.acs.AcsLogicalException;
import com.arrow.pegasus.data.security.Privilege;
import com.arrow.pegasus.repo.PrivilegeRepository;

@Service
public class PrivilegeService extends BaseServiceAbstract {

	@Autowired
	private PrivilegeRepository privilegeRepository;

	public PrivilegeRepository getPrivilegeRepository() {
		return privilegeRepository;
	}

	public Privilege populateRefs(Privilege privilege) {

		if (privilege == null)
			return privilege;

		if (!StringUtils.isEmpty(privilege.getProductId()))
			privilege.setRefProduct(getCoreCacheService().findProductById(privilege.getProductId()));
		Assert.notNull(privilege.getRefProduct(), "refProduct is null");

		return privilege;
	}

	public Privilege create(Privilege privilege, String who) {
		String method = "create";

		// logical checks
		if (privilege == null) {
			logInfo(method, "privilege is null");
			throw new AcsLogicalException("privilege is null");
		}

		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}

		privilege = privilegeRepository.doInsert(privilege, who);

		// re-cache
		getCoreCacheService().clearRolesAndPrivileges();

		return privilege;
	}

	public Privilege update(Privilege privilege, String who) {
		String method = "update";

		// logical checks
		if (privilege == null) {
			logInfo(method, "privilege is null");
			throw new AcsLogicalException("privilege is null");
		}

		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}

		privilege = privilegeRepository.doSave(privilege, who);

		// re-cache
		getCoreCacheService().clearRolesAndPrivileges();

		return privilege;
	}
}
