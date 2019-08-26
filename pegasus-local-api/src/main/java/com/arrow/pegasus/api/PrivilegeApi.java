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

import com.arrow.pegasus.client.model.PrivilegeChangeModel;
import com.arrow.pegasus.data.security.Privilege;

import moonstone.acs.JsonUtils;
import moonstone.acs.client.model.PagingResultModel;

@RestController(value = "localPegasusPrivilegeApi")
@RequestMapping("/api/v1/local/pegasus/privileges")
public class PrivilegeApi extends BaseApiAbstract {

	@RequestMapping(path = "/products/{productId}/enabled/{enabled}", method = RequestMethod.GET)
	public List<Privilege> findByProductIdAndEnabled(
			@PathVariable(name = "productId", required = true) String productId,
			@PathVariable(name = "enabled", required = true) boolean enabled) {
		return getPrivilegeService().getPrivilegeRepository().findByProductIdAndEnabled(productId, enabled);
	}

	@RequestMapping(path = "", method = RequestMethod.GET)
	public PagingResultModel<Privilege> findAll(@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "" + DEFAULT_PAGE_SIZE) int size,
			@RequestParam(name = "sortDirection", required = false) String[] sortDirection,
			@RequestParam(name = "sortProperty", required = false) String[] sortProperty) {
		Assert.isTrue(
				sortDirection == null && sortProperty == null
						|| sortDirection != null && sortProperty != null && sortDirection.length == sortProperty.length,
				"invalid sort options");
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
		Page<Privilege> privileges = getPrivilegeService().getPrivilegeRepository().findAll(pageRequest);
		PagingResultModel<Privilege> result = new PagingResultModel<Privilege>().withPage(privileges.getNumber())
				.withTotalPages(privileges.getTotalPages()).withTotalSize(privileges.getTotalElements());
		result.withData(privileges.getContent()).withSize(privileges.getSize());
		return result;
	}

	@RequestMapping(path = "/ids/{id}", method = RequestMethod.GET)
	public Privilege findById(@PathVariable(name = "id", required = true) String id) {
		Privilege privilege = getPrivilegeService().getPrivilegeRepository().findById(id).orElse(null);
		Assert.notNull(privilege, "privilege not found");
		return privilege;
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public Privilege create(@RequestBody(required = false) PrivilegeChangeModel body) {
		PrivilegeChangeModel model = JsonUtils.fromJson(getApiPayload(), PrivilegeChangeModel.class);
		Assert.notNull(model, "model is null");
		Assert.notNull(model.getPrivilege(), "privilege is null");
		Assert.hasText(model.getPrivilege().getSystemName(), "systemName is empty");
		Assert.hasText(model.getPrivilege().getDescription(), "description is empty");
		Assert.hasText(model.getPrivilege().getName(), "name is empty");
		checkProduct(model.getPrivilege().getProductId());
		return getPrivilegeService().create(model.getPrivilege(), model.getWho());
	}

	@RequestMapping(path = "", method = RequestMethod.PUT)
	public Privilege update(@RequestBody(required = false) PrivilegeChangeModel body) {
		PrivilegeChangeModel model = JsonUtils.fromJson(getApiPayload(), PrivilegeChangeModel.class);
		Assert.notNull(model, "model is null");
		Assert.notNull(model.getPrivilege(), "privilege is null");
		Assert.hasText(model.getPrivilege().getSystemName(), "systemName is empty");
		Assert.hasText(model.getPrivilege().getDescription(), "description is empty");
		Assert.hasText(model.getPrivilege().getName(), "name is empty");
		Privilege privilege = getCoreCacheService().findPrivilegeById(model.getPrivilege().getId());
		Assert.notNull(privilege, "privilege is not found");
		checkProduct(model.getPrivilege().getProductId());
		return getPrivilegeService().update(model.getPrivilege(), model.getWho());
	}
}
