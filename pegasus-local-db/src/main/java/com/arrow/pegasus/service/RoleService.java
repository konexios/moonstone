package com.arrow.pegasus.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arrow.acs.AcsLogicalException;
import com.arrow.pegasus.CoreAuditLog;
import com.arrow.pegasus.data.AuditLogBuilder;
import com.arrow.pegasus.data.security.Privilege;
import com.arrow.pegasus.data.security.Role;
import com.arrow.pegasus.repo.RoleRepository;

@Service
public class RoleService extends BaseServiceAbstract {

	@Autowired
	private RoleRepository roleRepository;

	public RoleRepository getRoleRepository() {
		return roleRepository;
	}

	public Role create(Role role, String who) {
		String method = "create";

		// logical checks
		if (role == null) {
			logInfo(method, "role is null");
			throw new AcsLogicalException("role is null");
		}

		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}

		role = roleRepository.doInsert(role, who);

		// write audit log
		getAuditLogService().save(
		        AuditLogBuilder.create().type(CoreAuditLog.Role.CREATE_ROLE).applicationId(role.getApplicationId())
		                .objectId(role.getId()).by(who).parameter("name", role.getName()));

		// re-cache
		getCoreCacheService().clearRolesAndPrivileges();

		return role;
	}

	public Role update(Role role, String who) {
		String method = "update";

		// logical checks
		if (role == null) {
			logInfo(method, "role is null");
			throw new AcsLogicalException("role is null");
		}

		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}

		role = roleRepository.doSave(role, who);

		// TODO check property changes?
		// write audit log
		getAuditLogService().save(
		        AuditLogBuilder.create().type(CoreAuditLog.Role.UPDATE_ROLE).applicationId(role.getApplicationId())
		                .objectId(role.getId()).by(who).parameter("name", role.getName()));

		// re-cache
		getCoreCacheService().clearRolesAndPrivileges();

		return role;
	}

	public Long deleteByApplicationId(String applicationId, String who) {
		String method = "deleteByApplicationId";
		logInfo(method, "applicationId: %s, who: %s", applicationId, who);
		return roleRepository.deleteByApplicationId(applicationId);
	}

	public Role populate(Role role) {

		if (role != null) {
			if (role.getRefProduct() == null && !StringUtils.isEmpty(role.getProductId()))
				role.setRefProduct(getCoreCacheService().findProductById(role.getProductId()));

			if (role.getRefApplication() == null && !StringUtils.isEmpty(role.getApplicationId()))
				role.setRefApplication(getCoreCacheService().findApplicationById(role.getApplicationId()));

			if ((role.getRefPrivileges() == null || role.getRefPrivileges().isEmpty())
			        && !role.getPrivilegeIds().isEmpty()) {

				List<Privilege> refPrivileges = new ArrayList<>();
				for (String privilegeId : role.getPrivilegeIds())
					refPrivileges.add(getCoreCacheService().findPrivilegeById(privilegeId));

				role.setRefPrivileges(refPrivileges);
			}
		}

		return role;
	}
}
