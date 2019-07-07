package com.arrow.pegasus.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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

import com.arrow.acs.JsonUtils;
import com.arrow.acs.client.model.PagingResultModel;
import com.arrow.pegasus.client.model.AccessKeyCreateModel;
import com.arrow.pegasus.client.model.CreateUpdateModel;
import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.repo.params.AccessKeySearchParams;
import com.fasterxml.jackson.core.type.TypeReference;

@RestController(value = "localPegasusAccessKeyApi")
@RequestMapping("/api/v1/local/pegasus/accessKeys")
public class AccessKeyApi extends BaseApiAbstract {
	@RequestMapping(path = "", method = RequestMethod.POST)
	public AccessKey create(@RequestBody(required = false) CreateUpdateModel<AccessKeyCreateModel> payload) {
		if (payload == null) {
			payload = JsonUtils.fromJson(getApiPayload(), new TypeReference<CreateUpdateModel<AccessKeyCreateModel>>() {
			});
		}
		AccessKeyCreateModel model = payload.getModel();
		Assert.notNull(model, "model is null");
		Assert.hasText(model.getCompanyId(), "companyId is empty");
		checkCompany(model.getCompanyId());
		if (StringUtils.isNotEmpty(model.getApplicationId())) {
			checkApplication(payload.getModel().getApplicationId());
		}
		AccessKey accessKey;
		if (StringUtils.isBlank(model.getApiKey()) || StringUtils.isBlank(model.getSecretKey())) {
			if (StringUtils.isBlank(model.getName()) && model.getExpiration() == null) {
				accessKey = getAccessKeyService().create(model.getCompanyId(), model.getSubscriptionId(),
						model.getApplicationId(), model.getPrivileges(), false, payload.getWho());
			} else {
				accessKey = getAccessKeyService().create(model.getCompanyId(), model.getSubscriptionId(),
						model.getApplicationId(), model.getName(), model.getExpiration(), model.getPrivileges(), false,
						payload.getWho());
			}
		} else {
			if (StringUtils.isBlank(model.getName())) {
				accessKey = getAccessKeyService().create(model.getCompanyId(), model.getSubscriptionId(),
						model.getApplicationId(), model.getApiKey(), model.getSecretKey(), model.getExpiration(),
						model.getPrivileges(), false, payload.getWho());
			} else {
				accessKey = getAccessKeyService().create(model.getCompanyId(), model.getSubscriptionId(),
						model.getApplicationId(), model.getApiKey(), model.getSecretKey(), model.getName(),
						model.getExpiration(), model.getPrivileges(), false, payload.getWho());
			}
		}
		Assert.notNull(accessKey, "accessKey is null");
		return accessKey;
	}

	@RequestMapping(path = "/{accessKeyId}", method = RequestMethod.PUT)
	public AccessKey update(@PathVariable String accessKeyId,
			@RequestBody(required = false) CreateUpdateModel<AccessKey> payload) {
		Assert.hasText(accessKeyId, "accessKeyId is empty");
		if (payload == null) {
			payload = JsonUtils.fromJson(getApiPayload(), new TypeReference<CreateUpdateModel<AccessKey>>() {
			});
		}

		AccessKey accessKeyModel = payload.getModel();
		Assert.notNull(accessKeyModel, "model is null");
		Assert.isTrue(StringUtils.equals(accessKeyId, accessKeyModel.getId()), "model doesn't match the id");
		AccessKey key = getAccessKeyService().getAccessKeyRepository().findById(accessKeyId).orElse(null);
		Assert.notNull(key, "accessKey not found");

		key = buildAccessKey(accessKeyModel, key);
		key = getAccessKeyService().update(key, payload.getWho());
		Assert.notNull(key, "accessKey is null");
		return key;
	}

	private AccessKey buildAccessKey(AccessKey model, AccessKey accessKey) {
		if (accessKey == null) {
			accessKey = new AccessKey();
		}

		if (model.getName() != null) {
			accessKey.setName(model.getName());
		}
		if (StringUtils.isNotEmpty(model.getCompanyId())) {
			checkCompany(model.getCompanyId());
			accessKey.setCompanyId(model.getCompanyId());
		}
		if (model.getApplicationId() != null) {
			checkApplication(model.getApplicationId());
			accessKey.setApplicationId(model.getApplicationId());
		}
		if (model.getExpiration() != null) {
			accessKey.setExpiration(model.getExpiration());
		}
		if (model.getPrivileges() != null) {
			accessKey.setPrivileges(model.getPrivileges());
		}

		return accessKey;
	}

	@RequestMapping(path = "/{accessKeyId}", method = RequestMethod.GET)
	public AccessKey findById(@PathVariable String accessKeyId) {
		Assert.hasText(accessKeyId, "accessKeyId is empty");
		AccessKey accessKey = getAccessKeyService().getAccessKeyRepository().findById(accessKeyId).orElse(null);
		Assert.notNull(accessKey, "accessKey not found, accessKeyId=" + accessKeyId);
		return accessKey;
	}

	@RequestMapping(path = "/owner/{pri}", method = RequestMethod.GET)
	public AccessKey findOwnerKey(@PathVariable String pri) {
		Assert.hasText(pri, "pri is empty");
		return getAccessKeyService().findOwnerKey(pri);
	}

	@RequestMapping(path = "/find", method = RequestMethod.POST)
	public PagingResultModel<AccessKey> findAccessKeys(@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "" + DEFAULT_PAGE_SIZE) int size,
			@RequestParam(name = "sortDirection", required = false) String[] sortDirection,
			@RequestParam(name = "sortProperty", required = false) String[] sortProperty,
			@RequestBody(required = false) AccessKeySearchParams params) {
		PageRequest pageRequest;
		if (sortDirection != null && sortProperty != null) {
			List<Order> orders = new ArrayList<>();
			for (int i = 0; i < sortDirection.length && i < sortProperty.length; i++) {
				orders.add(new Order(Direction.fromString(sortDirection[i]), sortProperty[i]));
			}
			pageRequest = PageRequest.of(page, size, Sort.by(orders));
		} else {
			pageRequest = PageRequest.of(page, size);
		}
		if (params == null) {
			params = JsonUtils.fromJson(getApiPayload(), AccessKeySearchParams.class);
		}
		Page<AccessKey> accessKeys = getAccessKeyService().getAccessKeyRepository().findAccessKeys(pageRequest, params);
		PagingResultModel<AccessKey> result = new PagingResultModel<AccessKey>().withPage(accessKeys.getNumber())
				.withTotalPages(accessKeys.getTotalPages()).withTotalSize(accessKeys.getTotalElements());
		result.withData(accessKeys.getContent()).withSize(accessKeys.getSize());

		return result;
	}

	@RequestMapping(path = "/pri/{pri}", method = RequestMethod.DELETE)
	public void removePriFromAccessKeys(@PathVariable String pri,
			@RequestParam(name = "who", required = true) String who) {
		Assert.hasText(pri, "pri is empty");
		List<AccessKey> accessKeys = getAccessKeyService().getAccessKeyRepository().findByPri(pri);
		for (AccessKey accessKey : accessKeys) {
			accessKey.getPrivileges().removeIf((privilege) -> StringUtils.equals(privilege.getPri(), pri));
			if (accessKey.getPrivileges().size() == 0) {
				// no more privileges in access key - delete it
				getAccessKeyService().delete(accessKey, who);
			} else {
				// there are still some privileges - update it
				accessKey = getAccessKeyService().update(accessKey, who);
			}
		}
	}
}
