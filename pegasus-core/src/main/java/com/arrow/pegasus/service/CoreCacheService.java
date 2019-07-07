package com.arrow.pegasus.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.AccessPrivilege;
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

@Service
public class CoreCacheService extends CacheServiceAbstract {

	public static final String PEGASUS_SUBSCRIPTION_IDS = "pegasus_subscription_ids";
	public static final String PEGASUS_SUBSCRIPTION_HIDS = "pegasus_subscription_hids";

	public static final String PEGASUS_PRODUCT_SYSTEM_NAMES = "pegasus_product_system_names";
	public static final String PEGASUS_PRODUCT_HIDS = "pegasus_product_hids";
	public static final String PEGASUS_PRODUCT_IDS = "pegasus_product_ids";

	public static final String PEGASUS_COMPANY_HIDS = "pegasus_company_hids";
	public static final String PEGASUS_COMPANY_IDS = "pegasus_company_ids";

	public static final String PEGASUS_APPLICATION_CODES = "pegasus_application_codes";
	public static final String PEGASUS_APPLICATION_NAMES = "pegasus_application_names";
	public static final String PEGASUS_APPLICATION_HIDS = "pegasus_application_hids";
	public static final String PEGASUS_APPLICATION_IDS = "pegasus_application_ids";

	public static final String PEGASUS_ZONE_SYSTEM_NAMES = "pegasus_zone_system_names";
	public static final String PEGASUS_ZONE_HIDS = "pegasus_zone_hids";
	public static final String PEGASUS_ZONE_IDS = "pegasus_zone_ids";

	public static final String PEGASUS_REGION_NAMES = "pegasus_region_names";
	public static final String PEGASUS_REGION_HIDS = "pegasus_region_hids";
	public static final String PEGASUS_REGION_IDS = "pegasus_region_ids";

	public static final String PEGASUS_USER_IDS = "pegasus_user_ids";
	public static final String PEGASUS_USER_HIDS = "pegasus_user_hids";

	public static final String PEGASUS_AUTH_SAML_IDPS = "pegasus_auth_saml_idps";
	public static final String PEGASUS_AUTH_HIDS = "pegasus_auth_hids";
	public static final String PEGASUS_AUTH_IDS = "pegasus_auth_ids";

	public static final String PEGASUS_APPLICATION_ENGINE_HIDS = "pegasus_application_engine_hids";
	public static final String PEGASUS_APPLICATION_ENGINE_IDS = "pegasus_application_engine_ids";

	public static final String PEGASUS_ACCESS_KEY_HASHED_API_KEYS = "pegasus_accessKey_hashedApiKeys";
	public static final String PEGASUS_ACCESS_KEY_IDS = "pegasus_accessKey_ids";
	public static final String PEGASUS_OWNER_ACCESS_KEY_PRIS = "pegasus_owner_accessKey_pris";

	public static final String PEGASUS_ROLE_HIDS = "pegasus_role_hids";
	public static final String PEGASUS_ROLE_IDS = "pegasus_role_ids";

	public static final String PEGASUS_PRIVILEGE_HIDS = "pegasus_privilege_hids";
	public static final String PEGASUS_PRIVILEGE_IDS = "pegasus_privilege_ids";

	@Autowired
	private CoreCacheProxy proxy;

	@PostConstruct
	protected void preload() {
	}

	@Cacheable(PEGASUS_ACCESS_KEY_IDS)
	public AccessKey findAccessKeyById(String id) {
		String method = "findAccessKeyById";
		AccessKey result = proxy.findAccessKeyById(id);
		if (result == null) {
			logError(method, "id not found: %s", id);
		}
		return result;
	}

	@Cacheable(PEGASUS_ACCESS_KEY_HASHED_API_KEYS)
	public AccessKey findAccessKeyByHashedApiKey(String hashedApiKey) {
		String method = "findAccessKeyByHashedApiKey";
		AccessKey result = proxy.findAccessKeyByHashedApiKey(hashedApiKey);
		if (result == null) {
			logError(method, "hashedApiKey not found: %s", hashedApiKey);
		}
		return result;
	}

	@Cacheable(PEGASUS_OWNER_ACCESS_KEY_PRIS)
	public AccessKey findOwnerAccessKeyByPri(String pri) {
		String method = "findAccessKeyByPri";
		AccessKey result = proxy.findOwnerAccessKeyByPri(pri);
		if (result == null) {
			logError(method, "pri not found: %s", pri);
		}
		return result;
	}

	@Caching(evict = {
			@CacheEvict(value = PEGASUS_ACCESS_KEY_IDS, key = "#accessKey.id", condition = "#accessKey != null"),
			@CacheEvict(value = PEGASUS_ACCESS_KEY_HASHED_API_KEYS, key = "#accessKey.hashedApiKey", condition = "#accessKey != null") })
	public void clearAccessKey(AccessKey accessKey) {
		String method = "clearAccessKey";
		if (accessKey != null) {
			logInfo(method, "id: %s, hashedApiKey: %s", accessKey.getId(), accessKey.getHashedApiKey());
			notifyCacheUpdate(PEGASUS_ACCESS_KEY_IDS, accessKey.getId());
			notifyCacheUpdate(PEGASUS_ACCESS_KEY_HASHED_API_KEYS, accessKey.getHashedApiKey());

			// custom implementation to clear access key cached by
			// access privilege pri
			for (AccessPrivilege ap : accessKey.getPrivileges()) {
				getCacheManager().getCache(PEGASUS_OWNER_ACCESS_KEY_PRIS).evict(ap.getPri());
				notifyCacheUpdate(PEGASUS_OWNER_ACCESS_KEY_PRIS, ap.getPri());
			}
		}
	}

	@CacheEvict(cacheNames = { PEGASUS_ACCESS_KEY_IDS, PEGASUS_ACCESS_KEY_HASHED_API_KEYS,
			PEGASUS_OWNER_ACCESS_KEY_PRIS }, allEntries = true)
	public void clearAccessKeys() {
		String method = "clearAccessKeys";
		logInfo(method, "...");
		notifyCacheUpdate(PEGASUS_ACCESS_KEY_IDS, ALL_KEYS);
		notifyCacheUpdate(PEGASUS_ACCESS_KEY_HASHED_API_KEYS, ALL_KEYS);
		notifyCacheUpdate(PEGASUS_OWNER_ACCESS_KEY_PRIS, ALL_KEYS);
	}

	@Cacheable(PEGASUS_APPLICATION_ENGINE_IDS)
	public ApplicationEngine findApplicationEngineById(String id) {
		return proxy.findApplicationEngineById(id);
	}

	@Cacheable(PEGASUS_APPLICATION_ENGINE_HIDS)
	public ApplicationEngine findApplicationEngineByHid(String hid) {
		return proxy.findApplicationEngineByHid(hid);
	}

	@Caching(evict = {
			@CacheEvict(value = PEGASUS_APPLICATION_ENGINE_IDS, key = "#engine.id", condition = "#engine != null"),
			@CacheEvict(value = PEGASUS_APPLICATION_ENGINE_HIDS, key = "#engine.hid", condition = "#engine != null") })
	public void clearApplicationEngine(ApplicationEngine engine) {
		String method = "clearApplicationEngine";
		if (engine != null) {
			logInfo(method, "id: %s", engine.getId());
			notifyCacheUpdate(PEGASUS_APPLICATION_ENGINE_IDS, engine.getId());
			notifyCacheUpdate(PEGASUS_APPLICATION_ENGINE_HIDS, engine.getHid());
		}
	}

	@CacheEvict(cacheNames = { PEGASUS_APPLICATION_ENGINE_IDS, PEGASUS_APPLICATION_ENGINE_HIDS }, allEntries = true)
	public void clearApplicationEngines() {
		String method = "clearApplicationEngines";
		logInfo(method, "...");
		notifyCacheUpdate(PEGASUS_APPLICATION_ENGINE_IDS, ALL_KEYS);
		notifyCacheUpdate(PEGASUS_APPLICATION_ENGINE_HIDS, ALL_KEYS);
	}

	@Cacheable(PEGASUS_AUTH_IDS)
	public Auth findAuthById(String id) {
		return proxy.findAuthById(id);
	}

	@Cacheable(PEGASUS_AUTH_HIDS)
	public Auth findAuthByHid(String hid) {
		return proxy.findAuthByHid(hid);
	}

	@Cacheable(PEGASUS_AUTH_SAML_IDPS)
	public Auth findAuthBySamlIdp(String idp) {
		return proxy.findAuthBySamlIdp(idp);
	}

	@Caching(evict = { @CacheEvict(value = PEGASUS_AUTH_IDS, key = "#auth.id", condition = "#auth != null"),
			@CacheEvict(value = PEGASUS_AUTH_HIDS, key = "#auth.hid", condition = "#auth != null"),
			@CacheEvict(value = PEGASUS_AUTH_SAML_IDPS, key = "#auth.saml.idp", condition = "#auth != null") })
	public void clearAuth(Auth auth) {
		String method = "clearAuth";
		if (auth != null) {
			logInfo(method, "id: %s", auth.getId());
			notifyCacheUpdate(PEGASUS_AUTH_IDS, auth.getId());
			notifyCacheUpdate(PEGASUS_AUTH_HIDS, auth.getHid());
			if (auth.getSaml() != null)
				notifyCacheUpdate(PEGASUS_AUTH_SAML_IDPS, auth.getSaml().getIdp());
		}
	}

	@CacheEvict(cacheNames = { PEGASUS_AUTH_IDS, PEGASUS_AUTH_HIDS }, allEntries = true)
	public void clearAuths() {
		String method = "clearAuths";
		logInfo(method, "...");
		notifyCacheUpdate(PEGASUS_AUTH_IDS, ALL_KEYS);
		notifyCacheUpdate(PEGASUS_AUTH_HIDS, ALL_KEYS);
	}

	@Cacheable(PEGASUS_ROLE_IDS)
	public Role findRoleById(String id) {
		return proxy.findRoleById(id);
	}

	@Cacheable(PEGASUS_ROLE_HIDS)
	public Role findRoleByHid(String hid) {
		return proxy.findRoleByHid(hid);
	}

	@Cacheable(PEGASUS_PRIVILEGE_IDS)
	public Privilege findPrivilegeById(String id) {
		return proxy.findPrivilegeById(id);
	}

	@Cacheable(PEGASUS_PRIVILEGE_HIDS)
	public Privilege findPrivilegeByHid(String hid) {
		return proxy.findPrivilegeByHid(hid);
	}

	@CacheEvict(cacheNames = { PEGASUS_ROLE_IDS, PEGASUS_ROLE_HIDS, PEGASUS_PRIVILEGE_IDS,
			PEGASUS_PRIVILEGE_HIDS }, allEntries = true)
	public void clearRolesAndPrivileges() {
		String method = "clearRolesAndPrivileges";
		logInfo(method, "...");
		notifyCacheUpdate(PEGASUS_ROLE_IDS, ALL_KEYS);
		notifyCacheUpdate(PEGASUS_ROLE_HIDS, ALL_KEYS);
		notifyCacheUpdate(PEGASUS_PRIVILEGE_IDS, ALL_KEYS);
		notifyCacheUpdate(PEGASUS_PRIVILEGE_HIDS, ALL_KEYS);
	}

	@Cacheable(PEGASUS_USER_HIDS)
	public User findUserByHid(String hid) {
		return proxy.findUserByHid(hid);
	}

	@Cacheable(PEGASUS_USER_IDS)
	public User findUserById(String id) {
		return proxy.findUserById(id);
	}

	@Caching(evict = { @CacheEvict(value = PEGASUS_USER_IDS, key = "#user.id", condition = "#user != null"),
			@CacheEvict(value = PEGASUS_USER_HIDS, key = "#user.hid", condition = "#user != null") })
	public void clearUser(User user) {
		String method = "clearUser";
		if (user != null) {
			logInfo(method, "id: %s", user.getId());
			notifyCacheUpdate(PEGASUS_USER_IDS, user.getId());
			notifyCacheUpdate(PEGASUS_USER_HIDS, user.getHid());
		}
	}

	@CacheEvict(cacheNames = { PEGASUS_USER_IDS, PEGASUS_USER_HIDS }, allEntries = true)
	public void clearUsers() {
		String method = "clearUsers";
		logInfo(method, "...");
		notifyCacheUpdate(PEGASUS_USER_IDS, ALL_KEYS);
		notifyCacheUpdate(PEGASUS_USER_HIDS, ALL_KEYS);
	}

	@Cacheable(PEGASUS_REGION_IDS)
	public Region findRegionById(String id) {
		return proxy.findRegionById(id);
	}

	@Cacheable(PEGASUS_REGION_HIDS)
	public Region findRegionByHid(String hid) {
		return proxy.findRegionByHid(hid);
	}

	@Cacheable(PEGASUS_REGION_NAMES)
	public Region findRegionByName(String name) {
		return proxy.findRegionByName(name);
	}

	@Caching(evict = { @CacheEvict(value = PEGASUS_REGION_IDS, key = "#region.id", condition = "#region != null"),
			@CacheEvict(value = PEGASUS_REGION_HIDS, key = "#region.hid", condition = "#region != null") })
	public void clearRegion(Region region) {
		String method = "clearRegion";
		if (region != null) {
			logInfo(method, "id: %s", region.getId());
			notifyCacheUpdate(PEGASUS_REGION_IDS, region.getId());
			notifyCacheUpdate(PEGASUS_REGION_HIDS, region.getHid());
		}
	}

	@Cacheable(PEGASUS_ZONE_IDS)
	public Zone findZoneById(String id) {
		return proxy.findZoneById(id);
	}

	@Cacheable(PEGASUS_ZONE_HIDS)
	public Zone findZoneByHid(String hid) {
		return proxy.findZoneByHid(hid);
	}

	@Cacheable(PEGASUS_ZONE_SYSTEM_NAMES)
	public Zone findZoneBySystemName(String systemName) {
		return proxy.findZoneBySystemName(systemName);
	}

	@Caching(evict = { @CacheEvict(value = PEGASUS_ZONE_IDS, key = "#zone.id", condition = "#zone != null"),
			@CacheEvict(value = PEGASUS_ZONE_HIDS, key = "#zone.hid", condition = "#zone != null"),
			@CacheEvict(value = PEGASUS_ZONE_SYSTEM_NAMES, key = "#zone.systemName", condition = "#zone != null") })
	public void clearZone(Zone zone) {
		String method = "clearZone";
		if (zone != null) {
			logInfo(method, "id: %s", zone.getId());
			notifyCacheUpdate(PEGASUS_ZONE_IDS, zone.getId());
			notifyCacheUpdate(PEGASUS_ZONE_HIDS, zone.getHid());
			notifyCacheUpdate(PEGASUS_ZONE_SYSTEM_NAMES, zone.getSystemName());
		}
	}

	@CacheEvict(cacheNames = { PEGASUS_ZONE_IDS, PEGASUS_ZONE_HIDS, PEGASUS_ZONE_SYSTEM_NAMES }, allEntries = true)
	public void clearZones() {
		String method = "clearZones";
		logInfo(method, "...");
		notifyCacheUpdate(PEGASUS_ZONE_IDS, ALL_KEYS);
		notifyCacheUpdate(PEGASUS_ZONE_HIDS, ALL_KEYS);
		notifyCacheUpdate(PEGASUS_ZONE_SYSTEM_NAMES, ALL_KEYS);
	}

	@Cacheable(PEGASUS_APPLICATION_IDS)
	public Application findApplicationById(String id) {
		return proxy.findApplicationById(id);
	}

	@Cacheable(PEGASUS_APPLICATION_HIDS)
	public Application findApplicationByHid(String hid) {
		return proxy.findApplicationByHid(hid);
	}

	@Cacheable(PEGASUS_APPLICATION_NAMES)
	public Application findApplicationByName(String name) {
		return proxy.findApplicationByName(name);
	}

	@Cacheable(PEGASUS_APPLICATION_CODES)
	public Application findApplicationByCode(String code) {
		return proxy.findApplicationByCode(code);
	}

	@Caching(evict = {
			@CacheEvict(value = PEGASUS_APPLICATION_IDS, key = "#application.id", condition = "#application != null"),
			@CacheEvict(value = PEGASUS_APPLICATION_HIDS, key = "#application.hid", condition = "#application != null"),
			@CacheEvict(value = PEGASUS_APPLICATION_NAMES, key = "#application.name", condition = "#application != null"),
			@CacheEvict(value = PEGASUS_APPLICATION_CODES, key = "#application.code", condition = "#application != null") })
	public void clearApplication(Application application) {
		String method = "clearApplication";
		if (application != null) {
			logInfo(method, "id: %s", application.getId());
			notifyCacheUpdate(PEGASUS_APPLICATION_IDS, application.getId());
			notifyCacheUpdate(PEGASUS_APPLICATION_HIDS, application.getHid());
			notifyCacheUpdate(PEGASUS_APPLICATION_NAMES, application.getName());
			notifyCacheUpdate(PEGASUS_APPLICATION_CODES, application.getCode());
		}
	}

	@CacheEvict(cacheNames = { PEGASUS_APPLICATION_IDS, PEGASUS_APPLICATION_HIDS,
			PEGASUS_APPLICATION_NAMES }, allEntries = true)
	public void clearApplications() {
		String method = "clearApplications";
		logInfo(method, "...");
		notifyCacheUpdate(PEGASUS_APPLICATION_IDS, ALL_KEYS);
		notifyCacheUpdate(PEGASUS_APPLICATION_HIDS, ALL_KEYS);
	}

	@Cacheable(PEGASUS_COMPANY_IDS)
	public Company findCompanyById(String id) {
		return proxy.findCompanyById(id);
	}

	@Cacheable(PEGASUS_COMPANY_IDS)
	public Company findCompanyByHid(String hid) {
		return proxy.findCompanyByHid(hid);
	}

	@Caching(evict = { @CacheEvict(value = PEGASUS_COMPANY_IDS, key = "#company.id", condition = "#company != null"),
			@CacheEvict(value = PEGASUS_COMPANY_HIDS, key = "#company.hid", condition = "#company != null") })
	public void clearCompany(Company company) {
		String method = "clearCompany";
		if (company != null) {
			logInfo(method, "id: %s", company.getId());
			notifyCacheUpdate(PEGASUS_COMPANY_IDS, company.getId());
			notifyCacheUpdate(PEGASUS_COMPANY_HIDS, company.getHid());
		}
	}

	@CacheEvict(cacheNames = { PEGASUS_COMPANY_IDS, PEGASUS_COMPANY_HIDS }, allEntries = true)
	public void clearCompanies() {
		String method = "clearCompanies";
		logInfo(method, "...");
		notifyCacheUpdate(PEGASUS_COMPANY_IDS, ALL_KEYS);
		notifyCacheUpdate(PEGASUS_COMPANY_HIDS, ALL_KEYS);
	}

	@Cacheable(PEGASUS_PRODUCT_IDS)
	public Product findProductById(String id) {
		return proxy.findProductById(id);
	}

	@Cacheable(PEGASUS_PRODUCT_HIDS)
	public Product findProductByHid(String hid) {
		return proxy.findProductByHid(hid);
	}

	@Cacheable(PEGASUS_PRODUCT_SYSTEM_NAMES)
	public Product findProductBySystemName(String systemName) {
		return proxy.findProductBySystemName(systemName);
	}

	@Caching(evict = { @CacheEvict(value = PEGASUS_PRODUCT_IDS, key = "#product.id", condition = "#product != null"),
			@CacheEvict(value = PEGASUS_PRODUCT_HIDS, key = "#product.hid", condition = "#product != null"),
			@CacheEvict(value = PEGASUS_PRODUCT_SYSTEM_NAMES, key = "#product.systemName", condition = "#product != null") })
	public void clearProduct(Product product) {
		String method = "clearProduct";
		if (product != null) {
			logInfo(method, "id: %s", product.getId());
			notifyCacheUpdate(PEGASUS_PRODUCT_IDS, product.getId());
			notifyCacheUpdate(PEGASUS_PRODUCT_HIDS, product.getHid());
			notifyCacheUpdate(PEGASUS_PRODUCT_SYSTEM_NAMES, product.getSystemName());
		}
	}

	@CacheEvict(cacheNames = { PEGASUS_PRODUCT_IDS, PEGASUS_PRODUCT_HIDS,
			PEGASUS_PRODUCT_SYSTEM_NAMES }, allEntries = true)
	public void clearProducts() {
		String method = "clearProducts";
		logInfo(method, "...");
		notifyCacheUpdate(PEGASUS_PRODUCT_IDS, ALL_KEYS);
		notifyCacheUpdate(PEGASUS_PRODUCT_HIDS, ALL_KEYS);
		notifyCacheUpdate(PEGASUS_PRODUCT_SYSTEM_NAMES, ALL_KEYS);
	}

	@Cacheable(PEGASUS_SUBSCRIPTION_IDS)
	public Subscription findSubscriptionById(String id) {
		return proxy.findSubscriptionById(id);
	}

	@Cacheable(PEGASUS_SUBSCRIPTION_HIDS)
	public Subscription findSubscriptionByHid(String hid) {
		return proxy.findSubscriptionByHid(hid);
	}

	@Caching(evict = {
			@CacheEvict(value = PEGASUS_SUBSCRIPTION_IDS, key = "#subscription.id", condition = "#subscription != null"),
			@CacheEvict(value = PEGASUS_SUBSCRIPTION_HIDS, key = "#subscription.hid", condition = "#subscription != null") })
	public void clearSubscription(Subscription subscription) {
		String method = "clearSubscription";
		if (subscription != null) {
			logInfo(method, "id: %s", subscription.getId());
			notifyCacheUpdate(PEGASUS_SUBSCRIPTION_IDS, subscription.getId());
			notifyCacheUpdate(PEGASUS_SUBSCRIPTION_HIDS, subscription.getHid());
		}
	}

	@CacheEvict(cacheNames = { PEGASUS_SUBSCRIPTION_IDS, PEGASUS_SUBSCRIPTION_HIDS }, allEntries = true)
	public void clearSubscriptions() {
		String method = "clearSubscriptions";
		logInfo(method, "...");
		notifyCacheUpdate(PEGASUS_SUBSCRIPTION_IDS, ALL_KEYS);
		notifyCacheUpdate(PEGASUS_SUBSCRIPTION_HIDS, ALL_KEYS);
	}

	public CoreCacheProxy getProxy() {
		return proxy;
	}
}
