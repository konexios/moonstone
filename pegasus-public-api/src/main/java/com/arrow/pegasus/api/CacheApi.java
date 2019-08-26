package com.arrow.pegasus.api;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import moonstone.acs.client.model.AccessKeyModel;
import moonstone.acs.client.model.ApplicationModel;
import moonstone.acs.client.model.AuthModel;
import moonstone.acs.client.model.CompanyModel;
import moonstone.acs.client.model.PrivilegeModel;
import moonstone.acs.client.model.ProductModel;
import moonstone.acs.client.model.RegionModel;
import moonstone.acs.client.model.RoleModel;
import moonstone.acs.client.model.SubscriptionModel;
import moonstone.acs.client.model.UserModel;
import moonstone.acs.client.model.ZoneModel;

@RestController(value = "pegasusCacheApi")
@RequestMapping("/api/v1/pegasus/cache")
public class CacheApi extends BaseApiAbstract {

	@RequestMapping(path = "/applications/hids/{hid}", method = RequestMethod.GET)
	public ApplicationModel findApplicationByHid(@PathVariable String hid) {
		validateRootAccess();
		return toApplicationModel(getCoreCacheService().findApplicationByHid(hid));
	}

	@RequestMapping(path = "/applications/names/{name}", method = RequestMethod.GET)
	public ApplicationModel findApplicationByName(@PathVariable String name) {
		validateRootAccess();
		return toApplicationModel(getCoreCacheService().findApplicationByName(name));
	}

	@RequestMapping(path = "/companies/hids/{hid}", method = RequestMethod.GET)
	public CompanyModel findCompanyByHid(@PathVariable String hid) {
		validateRootAccess();
		return toCompanyModel(getCoreCacheService().findCompanyByHid(hid));
	}

	@RequestMapping(path = "/products/hids/{hid}", method = RequestMethod.GET)
	public ProductModel findProductByHid(@PathVariable String hid) {
		validateRootAccess();
		return toProductModel(getCoreCacheService().findProductByHid(hid));
	}

	@RequestMapping(path = "/products/system-names/{systemName}", method = RequestMethod.GET)
	public ProductModel findProductBySystemName(@PathVariable String systemName) {
		validateRootAccess();
		return toProductModel(getCoreCacheService().findProductBySystemName(systemName));
	}

	@RequestMapping(path = "/subscriptions/hids/{hid}", method = RequestMethod.GET)
	public SubscriptionModel findSubscriptionByHid(@PathVariable String hid) {
		validateRootAccess();
		return toSubscriptionModel(getCoreCacheService().findSubscriptionByHid(hid));
	}

	@RequestMapping(path = "/access-keys/ids/{id}", method = RequestMethod.GET)
	public AccessKeyModel findAccessKeyById(@PathVariable String id) {
		validateRootAccess();
		return toAccessKeyModel(getCoreCacheService().findAccessKeyById(id));
	}

	@RequestMapping(path = "/access-keys/hashed-api-keys/{hashedApiKey}", method = RequestMethod.GET)
	public AccessKeyModel findAccessKeyByHashedApiKey(@PathVariable String hashedApiKey) {
		validateRootAccess();
		return toAccessKeyModel(getCoreCacheService().findAccessKeyByHashedApiKey(hashedApiKey));
	}

	@RequestMapping(path = "/roles/hids/{hid}", method = RequestMethod.GET)
	public RoleModel findRoleByHid(@PathVariable String hid) {
		validateRootAccess();
		return toRoleModel(getCoreCacheService().findRoleByHid(hid));
	}

	@RequestMapping(path = "/privileges/hids/{hid}", method = RequestMethod.GET)
	public PrivilegeModel findPrivilegeByHid(@PathVariable String hid) {
		validateRootAccess();
		return toPrivilegeModel(getCoreCacheService().findPrivilegeByHid(hid));
	}

	@RequestMapping(path = "/users/hids/{hid}", method = RequestMethod.GET)
	public UserModel findUserByHid(@PathVariable String hid) {
		validateRootAccess();
		return toUserModel(getCoreCacheService().findUserByHid(hid));
	}
	
	@RequestMapping(path = "/zones/hids/{hid}", method = RequestMethod.GET)
	public ZoneModel findZoneByHid(@PathVariable String hid) {
		validateRootAccess();
		return toZoneModel(getCoreCacheService().findZoneByHid(hid));
	}

	@RequestMapping(path = "/zones/system-names/{systemName}", method = RequestMethod.GET)
	public ZoneModel findZoneByName(@PathVariable String systemName) {
		validateRootAccess();
		return toZoneModel(getCoreCacheService().findZoneBySystemName(systemName));
	}
	
	@RequestMapping(path = "/regions/hids/{hid}", method = RequestMethod.GET)
	public RegionModel findRegionByHid(@PathVariable String hid) {
		validateRootAccess();
		return toRegionModel(getCoreCacheService().findRegionByHid(hid));
	}

	@RequestMapping(path = "/regions/names/{name}", method = RequestMethod.GET)
	public RegionModel findRegionByName(@PathVariable String name) {
		validateRootAccess();
		return toRegionModel(getCoreCacheService().findRegionByName(name));
	}
	
	@RequestMapping(path = "/auths/hids/{hid}", method = RequestMethod.GET)
	public AuthModel findAuthByHid(@PathVariable String hid) {
		validateRootAccess();
		return toAuthModel(getCoreCacheService().findAuthByHid(hid));
	}

	@RequestMapping(path = "/auths/saml-idps/{samlIdp}", method = RequestMethod.GET)
	public AuthModel findAuthBySamlIdp(@PathVariable String samlIdp) {
		validateRootAccess();
		return toAuthModel(getCoreCacheService().findAuthBySamlIdp(samlIdp));
	}

	// @RequestMapping(path = "/regions/ids/{id}", method = RequestMethod.GET)
	// public Region findRegionById(@PathVariable String id) {
	// validateRootAccess();
	// return getCoreCacheService().findRegionById(id);
	// }
	//
	// @RequestMapping(path = "/regions/hids/{hid}", method = RequestMethod.GET)
	// public Region findRegionByHid(@PathVariable String hid) {
	// validateRootAccess();
	// return getCoreCacheService().findRegionByHid(hid);
	// }
	//
	// @RequestMapping(path = "/regions/names/{name}", method =
	// RequestMethod.GET)
	// public Region findRegionByName(@PathVariable String name) {
	// validateRootAccess();
	// return getCoreCacheService().findRegionByName(name);
	// }
	//
	// @RequestMapping(path = "/zones/ids/{id}", method = RequestMethod.GET)
	// public Zone findZoneById(@PathVariable String id) {
	// validateRootAccess();
	// return getCoreCacheService().findZoneById(id);
	// }
	//
	// @RequestMapping(path = "/zones/hids/{hid}", method = RequestMethod.GET)
	// public Zone findZoneByHid(@PathVariable String hid) {
	// validateRootAccess();
	// return getCoreCacheService().findZoneByHid(hid);
	// }
	//
	// @RequestMapping(path = "/zones/names/{name}", method = RequestMethod.GET)
	// public Zone findZoneByName(@PathVariable String name) {
	// validateRootAccess();
	// return getCoreCacheService().findZoneByName(name);
	// }

	// @RequestMapping(path = "/dashboards/hids/{hid}", method =
	// RequestMethod.GET)
	// public Dashboard findDashboardByHid(@PathVariable String hid) {
	// validateRootAccess();
	// return getCoreCacheService().findDashboardByHid(hid);
	// }
	//
	// @RequestMapping(path = "/dashboards/ids/{id}", method =
	// RequestMethod.GET)
	// public Dashboard findDashboardById(@PathVariable String id) {
	// validateRootAccess();
	// return getCoreCacheService().findDashboardById(id);
	// }
	//
	// @RequestMapping(path = "/widgets/hids/{hid}", method = RequestMethod.GET)
	// public Widget findWidgetByHid(@PathVariable String hid) {
	// validateRootAccess();
	// return getCoreCacheService().findWidgetByHid(hid);
	// }
	//
	// @RequestMapping(path = "/widgets/ids/{id}", method = RequestMethod.GET)
	// public Widget findWidgetById(@PathVariable String id) {
	// validateRootAccess();
	// return getCoreCacheService().findWidgetById(id);
	// }
	//
	// @RequestMapping(path = "/widget-types/ids/{id}", method =
	// RequestMethod.GET)
	// public WidgetType findWidgetTypeById(@PathVariable String id) {
	// validateRootAccess();
	// return getCoreCacheService().findWidgetTypeById(id);
	// }
	//
	// @RequestMapping(path = "/widget-types/names/{name}", method =
	// RequestMethod.GET)
	// public WidgetType findWidgetTypeByName(@RequestParam(required = true)
	// String applicationId,
	// @PathVariable String name) {
	// validateRootAccess();
	// Assert.notNull(applicationId, "applicationId is null");
	// return getCoreCacheService().findWidgetTypeByName(applicationId, name);
	// }
}
