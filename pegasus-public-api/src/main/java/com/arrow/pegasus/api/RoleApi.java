package com.arrow.pegasus.api;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.profile.Product;
import com.arrow.pegasus.data.security.Role;

import moonstone.acs.JsonUtils;
import moonstone.acs.client.model.HidModel;
import moonstone.acs.client.model.ListResultModel;
import moonstone.acs.client.model.RoleModel;

@RestController(value = "pegasusRoleApi")
@RequestMapping("/api/v1/pegasus/roles")
public class RoleApi extends BaseApiAbstract {

	@RequestMapping(path = "", method = RequestMethod.GET)
	public ListResultModel<RoleModel> findRolesByApplication() {
		AccessKey accessKey = validateApplicationOwner();
		List<Role> roles = getRoleService().getRoleRepository().findByApplicationId(accessKey.getApplicationId());
		List<RoleModel> data = roles.stream().map(role -> toRoleModel(role)).collect(Collectors.toCollection(ArrayList::new));
		return new ListResultModel<RoleModel>().withData(data).withSize(data.size());
	}

	@RequestMapping(path = "/{hid}", method = RequestMethod.GET)
	public RoleModel findByHid(@PathVariable(name = "hid") String hid) {
		AccessKey accessKey = validateApplicationOwner();
		Role role = getRoleService().getRoleRepository().doFindByHid(hid);
		Assert.notNull(role, "role is not found");
		Assert.isTrue(StringUtils.equals(accessKey.getApplicationId(), role.getApplicationId()), "application does not match");
		return toRoleModel(role);
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public HidModel createRole(@RequestBody(required = false) RoleModel body) {
		AccessKey accessKey = validateApplicationOwner();

		RoleModel model = JsonUtils.fromJson(getApiPayload(), RoleModel.class);
		Assert.notNull(model, "model is null");
		Assert.hasText(model.getName(), "name is empty");
		Assert.hasText(model.getDescription(), "description is empty");
		Assert.notNull(model.getProductHid(), "productHid is null");
		Assert.isTrue(StringUtils.equals(accessKey.getRefApplication().getHid(), model.getApplicationHid()), 
				"application does not match");

		Role role = getRoleService().getRoleRepository().findFirstByNameAndApplicationId(model.getName(), accessKey.getApplicationId());
		if (role != null) {
			return new HidModel().withHid(role.getHid()).withMessage("role already exists");
		} else {
			role = fromRoleModel(model);
			Role result = getRoleService().create(role, accessKey.getId());
			return new HidModel().withHid(result.getHid()).withMessage("OK");
		}
	}

	@RequestMapping(path = "/{hid}", method = RequestMethod.PUT)
	public HidModel updateRole(@PathVariable(name = "hid") String hid, @RequestBody(required = false) RoleModel body) {
		AccessKey accessKey = validateApplicationOwner();

		RoleModel model = JsonUtils.fromJson(getApiPayload(), RoleModel.class);
		Assert.notNull(model, "model is null");
		model.setHid(hid);

		Role role = getCoreCacheService().findRoleByHid(hid);
		Assert.notNull(role, "role is not found");
		Assert.isTrue(role.isEditable(), "role is not editable"); // TODO to be revised later

		role.setEnabled(model.isEnabled());
		if (StringUtils.isNotBlank(model.getApplicationHid())) {
			Assert.isTrue(StringUtils.equals(accessKey.getRefApplication().getHid(), model.getApplicationHid()), 
					"application does not match");
		}
		if (StringUtils.isNotBlank(model.getProductHid())) {
			Product product = getCoreCacheService().findProductByHid(model.getProductHid());
			Assert.notNull(product, "product is not found");
			role.setProductId(product.getId());
		}
		if (StringUtils.isNotBlank(model.getDescription())) {
			role.setDescription(model.getDescription());
		}
		if (StringUtils.isNotBlank(model.getName()) && !StringUtils.equals(model.getName(), role.getName())) {
			Role tempRole = getRoleService().getRoleRepository().findFirstByNameAndApplicationId(model.getName(), 
					role.getApplicationId());
			Assert.isNull(tempRole, "duplicate name");
			role.setName(model.getName());
		}
		if (model.getPrivilegeHids() != null && !model.getPrivilegeHids().isEmpty()) {
			role.setPrivilegeIds(populateIds(model.getPrivilegeHids().stream().map(this::getValidatedPrivilege)));
		}
		Role result = getRoleService().update(role, accessKey.getId());
		return new HidModel().withHid(result.getHid()).withMessage("OK");
	}
}
