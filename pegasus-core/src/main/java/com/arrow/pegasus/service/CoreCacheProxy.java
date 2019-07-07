package com.arrow.pegasus.service;

import java.util.List;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;

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

public interface CoreCacheProxy {

	List<Company> preloadCompanies();

	List<Application> preloadApplications();

	List<Privilege> preloadPrivileges();

	List<Product> preloadProducts();

	List<Role> preloadRoles();

	@Caching(put = {
			@CachePut(value = CoreCacheService.PEGASUS_ACCESS_KEY_HASHED_API_KEYS, key = "#result.hashedApiKey", condition = "#result != null") })
	AccessKey findAccessKeyById(String id);

	@Caching(put = {
			@CachePut(value = CoreCacheService.PEGASUS_ACCESS_KEY_IDS, key = "#result.id", condition = "#result != null") })
	AccessKey findAccessKeyByHashedApiKey(String hashedApiKey);

	AccessKey findOwnerAccessKeyByPri(String pri);

	@Caching(put = {
			@CachePut(value = CoreCacheService.PEGASUS_APPLICATION_ENGINE_HIDS, key = "#result.hid", condition = "#result != null") })
	ApplicationEngine findApplicationEngineById(String id);

	@Caching(put = {
			@CachePut(value = CoreCacheService.PEGASUS_APPLICATION_ENGINE_IDS, key = "#result.id", condition = "#result != null") })
	ApplicationEngine findApplicationEngineByHid(String hid);

	@Caching(put = {
			@CachePut(value = CoreCacheService.PEGASUS_AUTH_HIDS, key = "#result.hid", condition = "#result != null"),
			@CachePut(value = CoreCacheService.PEGASUS_AUTH_SAML_IDPS, key = "#result.saml.idp", condition = "#result != null and #result.saml != null") })
	Auth findAuthById(String id);

	@Caching(put = {
			@CachePut(value = CoreCacheService.PEGASUS_AUTH_SAML_IDPS, key = "#result.saml.idp", condition = "#result != null and #result.saml != null"),
			@CachePut(value = CoreCacheService.PEGASUS_AUTH_IDS, key = "#result.id", condition = "#result != null") })
	Auth findAuthByHid(String hid);

	@Caching(put = {
			@CachePut(value = CoreCacheService.PEGASUS_AUTH_HIDS, key = "#result.hid", condition = "#result != null"),
			@CachePut(value = CoreCacheService.PEGASUS_AUTH_IDS, key = "#result.id", condition = "#result != null") })
	Auth findAuthBySamlIdp(String idp);

	@Caching(put = {
			@CachePut(value = CoreCacheService.PEGASUS_ROLE_HIDS, key = "#result.hid", condition = "#result != null") })
	Role findRoleById(String id);

	@Caching(put = {
			@CachePut(value = CoreCacheService.PEGASUS_ROLE_IDS, key = "#result.id", condition = "#result != null") })
	Role findRoleByHid(String hid);

	@Caching(put = {
			@CachePut(value = CoreCacheService.PEGASUS_PRIVILEGE_HIDS, key = "#result.hid", condition = "#result != null") })
	Privilege findPrivilegeById(String id);

	@Caching(put = {
			@CachePut(value = CoreCacheService.PEGASUS_PRIVILEGE_IDS, key = "#result.id", condition = "#result != null") })
	Privilege findPrivilegeByHid(String hid);

	@Caching(put = {
			@CachePut(value = CoreCacheService.PEGASUS_USER_HIDS, key = "#result.hid", condition = "#result != null") })
	User findUserById(String id);

	@Caching(put = {
			@CachePut(value = CoreCacheService.PEGASUS_USER_IDS, key = "#result.id", condition = "#result != null") })
	User findUserByHid(String hid);

	@Caching(put = {
			@CachePut(value = CoreCacheService.PEGASUS_REGION_HIDS, key = "#result.hid", condition = "#result != null"),
			@CachePut(value = CoreCacheService.PEGASUS_REGION_NAMES, key = "#result.name", condition = "#result != null") })
	Region findRegionById(String id);

	@Caching(put = {
			@CachePut(value = CoreCacheService.PEGASUS_REGION_IDS, key = "#result.id", condition = "#result != null"),
			@CachePut(value = CoreCacheService.PEGASUS_REGION_NAMES, key = "#result.name", condition = "#result != null") })
	Region findRegionByHid(String hid);

	@Caching(put = {
			@CachePut(value = CoreCacheService.PEGASUS_REGION_HIDS, key = "#result.hid", condition = "#result != null"),
			@CachePut(value = CoreCacheService.PEGASUS_REGION_IDS, key = "#result.id", condition = "#result != null") })
	Region findRegionByName(String name);

	@Caching(put = {
			@CachePut(value = CoreCacheService.PEGASUS_ZONE_HIDS, key = "#result.hid", condition = "#result != null"),
			@CachePut(value = CoreCacheService.PEGASUS_ZONE_SYSTEM_NAMES, key = "#result.systemName", condition = "#result != null") })
	Zone findZoneById(String id);

	@Caching(put = {
			@CachePut(value = CoreCacheService.PEGASUS_ZONE_IDS, key = "#result.id", condition = "#result != null"),
			@CachePut(value = CoreCacheService.PEGASUS_ZONE_SYSTEM_NAMES, key = "#result.systemName", condition = "#result != null") })
	Zone findZoneByHid(String hid);

	@Caching(put = {
			@CachePut(value = CoreCacheService.PEGASUS_ZONE_HIDS, key = "#result.hid", condition = "#result != null"),
			@CachePut(value = CoreCacheService.PEGASUS_ZONE_IDS, key = "#result.id", condition = "#result != null") })
	Zone findZoneBySystemName(String systemName);

	@Caching(put = {
			@CachePut(value = CoreCacheService.PEGASUS_APPLICATION_HIDS, key = "#result.hid", condition = "#result != null"),
			@CachePut(value = CoreCacheService.PEGASUS_APPLICATION_NAMES, key = "#result.name", condition = "#result != null"),
			@CachePut(value = CoreCacheService.PEGASUS_APPLICATION_CODES, key = "#result.code", condition = "#result != null") })
	Application findApplicationById(String id);

	@Caching(put = {
			@CachePut(value = CoreCacheService.PEGASUS_APPLICATION_IDS, key = "#result.id", condition = "#result != null"),
			@CachePut(value = CoreCacheService.PEGASUS_APPLICATION_NAMES, key = "#result.name", condition = "#result != null"),
			@CachePut(value = CoreCacheService.PEGASUS_APPLICATION_CODES, key = "#result.code", condition = "#result != null") })
	Application findApplicationByHid(String hid);

	@Caching(put = {
			@CachePut(value = CoreCacheService.PEGASUS_APPLICATION_HIDS, key = "#result.hid", condition = "#result != null"),
			@CachePut(value = CoreCacheService.PEGASUS_APPLICATION_IDS, key = "#result.id", condition = "#result != null"),
			@CachePut(value = CoreCacheService.PEGASUS_APPLICATION_CODES, key = "#result.code", condition = "#result != null") })
	Application findApplicationByName(String name);

	@Caching(put = {
			@CachePut(value = CoreCacheService.PEGASUS_APPLICATION_HIDS, key = "#result.hid", condition = "#result != null"),
			@CachePut(value = CoreCacheService.PEGASUS_APPLICATION_IDS, key = "#result.id", condition = "#result != null"),
			@CachePut(value = CoreCacheService.PEGASUS_APPLICATION_NAMES, key = "#result.name", condition = "#result != null") })
	Application findApplicationByCode(String code);

	@Caching(put = {
			@CachePut(value = CoreCacheService.PEGASUS_COMPANY_HIDS, key = "#result.hid", condition = "#result != null") })
	Company findCompanyById(String id);

	@Caching(put = {
			@CachePut(value = CoreCacheService.PEGASUS_COMPANY_IDS, key = "#result.id", condition = "#result != null") })
	Company findCompanyByHid(String hid);

	@Caching(put = {
			@CachePut(value = CoreCacheService.PEGASUS_PRODUCT_HIDS, key = "#result.hid", condition = "#result != null"),
			@CachePut(value = CoreCacheService.PEGASUS_PRODUCT_SYSTEM_NAMES, key = "#result.systemName", condition = "#result != null") })
	Product findProductById(String id);

	@Caching(put = {
			@CachePut(value = CoreCacheService.PEGASUS_PRODUCT_SYSTEM_NAMES, key = "#result.systemName", condition = "#result != null"),
			@CachePut(value = CoreCacheService.PEGASUS_PRODUCT_IDS, key = "#result.id", condition = "#result != null") })
	Product findProductByHid(String hid);

	@Caching(put = {
			@CachePut(value = CoreCacheService.PEGASUS_PRODUCT_HIDS, key = "#result.hid", condition = "#result != null"),
			@CachePut(value = CoreCacheService.PEGASUS_PRODUCT_IDS, key = "#result.id", condition = "#result != null") })
	Product findProductBySystemName(String systemName);

	@Caching(put = {
			@CachePut(value = CoreCacheService.PEGASUS_SUBSCRIPTION_HIDS, key = "#result.hid", condition = "#result != null") })
	Subscription findSubscriptionById(String id);

	@Caching(put = {
			@CachePut(value = CoreCacheService.PEGASUS_SUBSCRIPTION_IDS, key = "#result.id", condition = "#result != null") })
	Subscription findSubscriptionByHid(String hid);
}
