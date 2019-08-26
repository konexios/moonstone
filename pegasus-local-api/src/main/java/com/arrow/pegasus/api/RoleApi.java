package com.arrow.pegasus.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.pegasus.client.model.RoleChangeModel;
import com.arrow.pegasus.data.security.Role;

import moonstone.acs.JsonUtils;
import moonstone.acs.client.model.PagingResultModel;

@RestController(value = "localPegasusRoleApi")
@RequestMapping("/api/v1/local/pegasus/roles")
public class RoleApi extends BaseApiAbstract {

	@RequestMapping(path = "", method = RequestMethod.GET)
	public List<Role> findAll() {
		return getRoleService().getRoleRepository().findAll();
	}

	@RequestMapping(path = "/{roleId}", method = RequestMethod.GET)
	public Role findById(@PathVariable(name = "roleId", required = true) String roleId) {
		Role role = getRoleService().getRoleRepository().findById(roleId).orElse(null);
		Assert.notNull(role, "role not found");
		return role;
	}

	@RequestMapping(path = "/applications/{applicationId}/names", method = RequestMethod.GET)
	public Role findByNameAndApplicationId(@RequestParam(name = "name", required = true) String name,
			@PathVariable(name = "applicationId", required = true) String applicationId) {
		Assert.hasText(name, "name is empty");
		Role role = getRoleService().getRoleRepository().findFirstByNameAndApplicationId(name, applicationId);
		Assert.notNull(role, "role not found");
		return role;
	}

	@RequestMapping(path = "/applications/{applicationId}/enabled/{enabled}", method = RequestMethod.GET)
	public List<Role> findByApplicationIdAndEnabled(
			@PathVariable(name = "applicationId", required = true) String applicationId,
			@PathVariable(name = "enabled", required = true) boolean enabled) {
		return getRoleService().getRoleRepository().findByApplicationIdAndEnabled(applicationId, enabled);
	}

	@RequestMapping(path = "/enabled/{enabled}", method = RequestMethod.GET)
	public List<Role> findByEnabled(@PathVariable(name = "enabled", required = true) boolean enabled) {
		return getRoleService().getRoleRepository().findByEnabled(enabled);
	}

	@RequestMapping(path = "/applications", method = RequestMethod.GET)
	public PagingResultModel<Role> findAllByApplicationId(@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "" + DEFAULT_PAGE_SIZE) int size,
			@RequestParam(name = "sortDirection", required = false) String[] sortDirection,
			@RequestParam(name = "sortProperty", required = false) String[] sortProperty,
			@RequestParam(name = "applicationId", required = false) String applicationId) {
		PageRequest pageRequest = null;
		if (sortDirection != null) {
			List<Order> orders = new ArrayList<>();
			for (int i = 0; i < sortDirection.length; i++) {
				orders.add(new Order(Direction.fromString(sortDirection[i]), sortProperty[i]));
			}
			pageRequest = PageRequest.of(page, size, Sort.by(orders));
		} else {
			pageRequest = PageRequest.of(page, size);
		}
		Page<Role> roles = getRoleService().getRoleRepository().findAllByApplicationId(pageRequest, applicationId);
		PagingResultModel<Role> result = new PagingResultModel<Role>().withPage(roles.getNumber())
				.withTotalPages(roles.getTotalPages()).withTotalSize(roles.getTotalElements());
		result.withData(roles.getContent()).withSize(roles.getSize());
		return result;
	}

	@RequestMapping(path = "/applications/{applicationId}", method = RequestMethod.GET)
	public List<Role> findByApplicationId(@PathVariable(name = "applicationId", required = true) String applicationId) {
		return getRoleService().getRoleRepository().findByApplicationId(applicationId);
	}

	@RequestMapping(path = "/{roleId}/applications/{applicationId}", method = RequestMethod.GET)
	public Role findByIdAndApplicationId(@PathVariable(name = "roleId", required = true) String roleId,
			@PathVariable(name = "applicationId", required = true) String applicationId) {
		Role role = getRoleService().getRoleRepository().findByIdAndApplicationId(roleId, applicationId);
		Assert.notNull(role, "role not found");
		return role;
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public Role create(@RequestBody(required = false) RoleChangeModel body) {
		RoleChangeModel model = JsonUtils.fromJson(getApiPayload(), RoleChangeModel.class);
		Assert.notNull(model, "model is null");
		Assert.notNull(model.getRole(), "role is null");
		Assert.hasText(model.getRole().getName(), "name is empty");
		Assert.hasText(model.getRole().getDescription(), "description is empty");
		checkApplication(model.getRole().getApplicationId());
		checkProduct(model.getRole().getProductId());
		checkPrivileges(model.getRole().getPrivilegeIds());
		return getRoleService().create(model.getRole(), model.getWho());
	}

	@RequestMapping(path = "", method = RequestMethod.PUT)
	public Role update(@RequestBody(required = false) RoleChangeModel body) {
		RoleChangeModel model = JsonUtils.fromJson(getApiPayload(), RoleChangeModel.class);
		Assert.notNull(model, "model is null");
		Assert.notNull(model.getRole(), "role is null");
		Assert.hasText(model.getRole().getName(), "name is empty");
		Assert.hasText(model.getRole().getDescription(), "description is empty");
		checkApplication(model.getRole().getApplicationId());
		checkProduct(model.getRole().getProductId());
		checkPrivileges(model.getRole().getPrivilegeIds());
		Role role = getCoreCacheService().findRoleById(model.getRole().getId());
		Assert.notNull(role, "role is not found");
		Assert.isTrue(role.isEditable(), "role is not editable");
		return getRoleService().update(model.getRole(), model.getWho());
	}
}
