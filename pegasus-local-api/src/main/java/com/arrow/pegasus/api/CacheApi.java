package com.arrow.pegasus.api;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.ApplicationEngine;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.Company;
import com.arrow.pegasus.data.profile.Product;
import com.arrow.pegasus.data.profile.Region;
import com.arrow.pegasus.data.profile.Subscription;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.data.profile.Zone;
import com.arrow.pegasus.data.security.Auth;
import com.arrow.pegasus.data.security.Privilege;
import com.arrow.pegasus.data.security.Role;

@RestController(value = "localPegasusCacheApi")
@RequestMapping("/api/v1/local/pegasus/cache")
public class CacheApi extends BaseApiAbstract {

	@RequestMapping(path = "/applications/hids/{hid}", method = RequestMethod.GET)
	public Application findApplicationByHid(@PathVariable String hid) {
		return getCoreCacheService().findApplicationByHid(hid);
	}

	@RequestMapping(path = "/applications/ids/{id}", method = RequestMethod.GET)
	public Application findApplicationById(@PathVariable String id) {

		return getCoreCacheService().findApplicationById(id);
	}

	@RequestMapping(path = "/applications/names/{name}", method = RequestMethod.GET)
	public Application findApplicationByName(@PathVariable String name) {
		return getCoreCacheService().findApplicationByName(name);
	}

	@RequestMapping(path = "/applications/codes/{code}", method = RequestMethod.GET)
	public Application findApplicationByCode(@PathVariable String code) {
		return getCoreCacheService().findApplicationByCode(code);
	}

	@RequestMapping(path = "/application-engines/hids/{hid}", method = RequestMethod.GET)
	public ApplicationEngine findApplicationEngineByHid(@PathVariable String hid) {
		return getCoreCacheService().findApplicationEngineByHid(hid);
	}

	@RequestMapping(path = "/application-engines/ids/{id}", method = RequestMethod.GET)
	public ApplicationEngine findApplicationEngineById(@PathVariable String id) {
		return getCoreCacheService().findApplicationEngineById(id);
	}

	@RequestMapping(path = "/auths/hids/{hid}", method = RequestMethod.GET)
	public Auth findAuthByHid(@PathVariable String hid) {
		return getCoreCacheService().findAuthByHid(hid);
	}

	@RequestMapping(path = "/auths/ids/{id}", method = RequestMethod.GET)
	public Auth findAuthById(@PathVariable String id) {
		return getCoreCacheService().findAuthById(id);
	}

	@RequestMapping(path = "/auths/saml/idps/{idp}", method = RequestMethod.GET)
	public Auth findAuthBySamlIdp(@PathVariable String idp) {
		return getCoreCacheService().findAuthBySamlIdp(idp);
	}

	@RequestMapping(path = "/companies/hids/{hid}", method = RequestMethod.GET)
	public Company findCompanyByHid(@PathVariable String hid) {
		return getCoreCacheService().findCompanyByHid(hid);
	}

	@RequestMapping(path = "/companies/ids/{id}", method = RequestMethod.GET)
	public Company findCompanyById(@PathVariable String id) {
		return getCoreCacheService().findCompanyById(id);
	}

	@RequestMapping(path = "/products/hids/{hid}", method = RequestMethod.GET)
	public Product findProductByHid(@PathVariable String hid) {
		return getCoreCacheService().findProductByHid(hid);
	}

	@RequestMapping(path = "/products/ids/{id}", method = RequestMethod.GET)
	public Product findProductById(@PathVariable String id) {
		return getCoreCacheService().findProductById(id);
	}

	@RequestMapping(path = "/products/system-names/{systemName}", method = RequestMethod.GET)
	public Product findProductBySystemName(@PathVariable String systemName) {
		return getCoreCacheService().findProductBySystemName(systemName);
	}

	@RequestMapping(path = "/subscriptions/hids/{hid}", method = RequestMethod.GET)
	public Subscription findSubscriptionByHid(@PathVariable String hid) {
		return getCoreCacheService().findSubscriptionByHid(hid);
	}

	@RequestMapping(path = "/subscriptions/ids/{id}", method = RequestMethod.GET)
	public Subscription findSubscriptionById(@PathVariable String id) {
		return getCoreCacheService().findSubscriptionById(id);
	}

	@RequestMapping(path = "/access-keys/pris/{pri}", method = RequestMethod.GET)
	public AccessKey findOwnerAccessKeyByPri(@PathVariable String pri) {
		return getCoreCacheService().findOwnerAccessKeyByPri(pri);
	}

	@RequestMapping(path = "/access-keys/ids/{id}", method = RequestMethod.GET)
	public AccessKey findAccessKeyById(@PathVariable String id) {
		return getCoreCacheService().findAccessKeyById(id);
	}

	@RequestMapping(path = "/access-keys/hashed-api-keys/{hashedApiKey}", method = RequestMethod.GET)
	public AccessKey findAccessKeyByHashedApiKey(@PathVariable String hashedApiKey) {
		return getCoreCacheService().findAccessKeyByHashedApiKey(hashedApiKey);
	}

	@RequestMapping(path = "/roles/ids/{id}", method = RequestMethod.GET)
	public Role findRoleById(@PathVariable String id) {
		return getCoreCacheService().findRoleById(id);
	}

	@RequestMapping(path = "/roles/hids/{hid}", method = RequestMethod.GET)
	public Role findRoleByHid(@PathVariable String hid) {
		return getCoreCacheService().findRoleByHid(hid);
	}

	@RequestMapping(path = "/privileges/ids/{id}", method = RequestMethod.GET)
	public Privilege findPrivilegeById(@PathVariable String id) {
		return getCoreCacheService().findPrivilegeById(id);
	}

	@RequestMapping(path = "/privileges/hids/{hid}", method = RequestMethod.GET)
	public Privilege findPrivilegeByHid(@PathVariable String hid) {
		return getCoreCacheService().findPrivilegeByHid(hid);
	}

	@RequestMapping(path = "/users/hids/{hid}", method = RequestMethod.GET)
	public User findUserByHid(@PathVariable String hid) {
		return getCoreCacheService().findUserByHid(hid);
	}

	@RequestMapping(path = "/users/ids/{id}", method = RequestMethod.GET)
	public User findUserById(@PathVariable String id) {
		return getCoreCacheService().findUserById(id);
	}

	@RequestMapping(path = "/regions/ids/{id}", method = RequestMethod.GET)
	public Region findRegionById(@PathVariable String id) {
		return getCoreCacheService().findRegionById(id);
	}

	@RequestMapping(path = "/regions/hids/{hid}", method = RequestMethod.GET)
	public Region findRegionByHid(@PathVariable String hid) {
		return getCoreCacheService().findRegionByHid(hid);
	}

	@RequestMapping(path = "/regions/names/{name}", method = RequestMethod.GET)
	public Region findRegionByName(@PathVariable String name) {
		return getCoreCacheService().findRegionByName(name);
	}

	@RequestMapping(path = "/zones/ids/{id}", method = RequestMethod.GET)
	public Zone findZoneById(@PathVariable String id) {
		return getCoreCacheService().findZoneById(id);
	}

	@RequestMapping(path = "/zones/hids/{hid}", method = RequestMethod.GET)
	public Zone findZoneByHid(@PathVariable String hid) {
		return getCoreCacheService().findZoneByHid(hid);
	}

	@RequestMapping(path = "/zones/names/{name}", method = RequestMethod.GET)
	public Zone findZoneByName(@PathVariable String name) {
		return getCoreCacheService().findZoneBySystemName(name);
	}
}
