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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.profile.Product;
import com.arrow.pegasus.data.security.Privilege;
import com.arrow.pegasus.repo.params.PrivilegeSearchParams;

import moonstone.acs.JsonUtils;
import moonstone.acs.client.model.HidModel;
import moonstone.acs.client.model.ListResultModel;
import moonstone.acs.client.model.PrivilegeModel;

@RestController(value = "pegasusPrivilegeApi")
@RequestMapping("/api/v1/pegasus/privileges")
public class PrivilegeApi extends BaseApiAbstract {

	@RequestMapping(path = "", method = RequestMethod.GET)
	public ListResultModel<PrivilegeModel> findBy(
			// @formatter:off
			@RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "systemName", required = false) String systemName,
			@RequestParam(name = "productHid", required = false) String productHid,
			@RequestParam(name = "enabled", required = false) String enabled) {
			// @formatter:on

		validateRootAccess(); // TODO validate accessKey

		ListResultModel<PrivilegeModel> result = new ListResultModel<>();
		PrivilegeSearchParams params = new PrivilegeSearchParams();
		if (StringUtils.isNotBlank(productHid)) {
			Product product = getCoreCacheService().findProductByHid(productHid);
			Assert.notNull(product, "product not found");
			params.addProductIds(product.getId());
		}
		params.setName(name);
		params.setSystemName(systemName);
		if (StringUtils.isNotBlank(enabled)) {
			params.setEnabled(Boolean.parseBoolean(enabled));
		}
		List<Privilege> privileges = getPrivilegeService().getPrivilegeRepository().findPrivileges(params);
		List<PrivilegeModel> data = privileges.stream().map(privilege -> toPrivilegeModel(privilege))
		        .collect(Collectors.toCollection(ArrayList::new));
		return result.withData(data).withSize(data.size());
	}

	@RequestMapping(path = "/{hid}", method = RequestMethod.GET)
	public PrivilegeModel findByHid(@PathVariable(name = "hid") String hid) {

		validateRootAccess(); // TODO validate accessKey

		Privilege privilege = getCoreCacheService().findPrivilegeByHid(hid);
		Assert.notNull(privilege, "privilege is not found");
		return toPrivilegeModel(privilege);
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public HidModel createPrivilege(@RequestBody(required = false) PrivilegeModel body) {

		AccessKey accessKey = validateRootAccess(); // TODO validate accessKey

		PrivilegeModel model = JsonUtils.fromJson(getApiPayload(), PrivilegeModel.class);
		Assert.notNull(model, "model is null");
		Assert.hasText(model.getSystemName(), "systemName is empty");
		Privilege privilege = getPrivilegeService().getPrivilegeRepository().findBySystemName(model.getSystemName());
		if (privilege != null) {
			return new HidModel().withHid(privilege.getHid()).withMessage("Privilege already exists");
		} else {
			Privilege result = getPrivilegeService().create(fromPrivilegeModel(model), accessKey.getId());
			return new HidModel().withHid(result.getHid()).withMessage("OK");
		}
	}

	@RequestMapping(path = "/{hid}", method = RequestMethod.PUT)
	public HidModel updatePrivilege(@PathVariable(name = "hid") String hid,
	        @RequestBody(required = false) PrivilegeModel body) {

		AccessKey accessKey = validateRootAccess(); // TODO validate accessKey

		PrivilegeModel model = JsonUtils.fromJson(getApiPayload(), PrivilegeModel.class);
		Assert.notNull(model, "model is null");
		model.setHid(hid);

		Privilege privilege = getCoreCacheService().findPrivilegeByHid(hid);
		Assert.notNull(privilege, "privilege is not found");
		if (StringUtils.isNotBlank(model.getSystemName())
		        && !StringUtils.equals(privilege.getSystemName(), model.getSystemName())) {
			Privilege tempPrivilege = getPrivilegeService().getPrivilegeRepository()
			        .findBySystemName(model.getSystemName());
			Assert.isNull(tempPrivilege, "duplicate systemName");
		}
		privilege = populatePrivilege(privilege, model);
		Privilege result = getPrivilegeService().update(privilege, accessKey.getId());
		return new HidModel().withHid(result.getHid()).withMessage("OK");
	}
}
