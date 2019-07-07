package com.arrow.pegasus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

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
import com.arrow.pegasus.repo.AccessKeyRepository;
import com.arrow.pegasus.repo.ApplicationEngineRepository;
import com.arrow.pegasus.repo.ApplicationRepository;
import com.arrow.pegasus.repo.AuthRepository;
import com.arrow.pegasus.repo.CompanyRepository;
import com.arrow.pegasus.repo.PrivilegeRepository;
import com.arrow.pegasus.repo.ProductRepository;
import com.arrow.pegasus.repo.RegionRepository;
import com.arrow.pegasus.repo.RoleRepository;
import com.arrow.pegasus.repo.SubscriptionRepository;
import com.arrow.pegasus.repo.UserRepository;
import com.arrow.pegasus.repo.ZoneRepository;

public class LocalCoreCacheProxy extends CoreCacheProxyAbstract implements CoreCacheProxy {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ApplicationRepository applicationRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private CompanyRepository companyRepository;
	@Autowired
	private SubscriptionRepository subscriptionRepository;
	@Autowired
	private ApplicationEngineRepository applicationEngineRepository;
	@Autowired
	private AuthRepository authRepository;
	@Autowired
	private PrivilegeRepository privilegeRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private AccessKeyRepository accessKeyRepository;
	@Autowired
	private RegionRepository regionRepository;
	@Autowired
	private ZoneRepository zoneRepository;

	public AccessKey findAccessKeyById(String id) {
		String method = "findAccessKeyById";
		logInfo(method, "looking up accessKey id: %s", id);
		return accessKeyRepository.findById(id).orElse(null);
	}

	public AccessKey findAccessKeyByHashedApiKey(String hashedApiKey) {
		String method = "findAccessKeyByHashedApiKey";
		logInfo(method, "looking up AccessKey hashedApiKey: %s", hashedApiKey);
		return accessKeyRepository.findByHashedApiKey(hashedApiKey);
	}

	@Override
	public AccessKey findOwnerAccessKeyByPri(String pri) {
		String method = "findOwnerAccessKeyByPri";
		logInfo(method, "looking up AccessKey pri: %s", pri);
		return accessKeyRepository.findOwnerByPri(pri);
	}

	public ApplicationEngine findApplicationEngineById(String id) {
		String method = "findApplicationEngineById";
		logInfo(method, "looking up applicationEngine id: %s", id);
		return applicationEngineRepository.findById(id).orElse(null);
	}

	public ApplicationEngine findApplicationEngineByHid(String hid) {
		String method = "findApplicationEngineByHid";
		logInfo(method, "looking up applicationEngine hid: %s", hid);
		return applicationEngineRepository.doFindByHid(hid);
	}

	public Auth findAuthById(String id) {
		String method = "findAuthById";
		logInfo(method, "looking up auth id: %s", id);
		return authRepository.findById(id).orElse(null);
	}

	public Auth findAuthByHid(String hid) {
		String method = "findAuthByHid";
		logInfo(method, "looking up auth hid: %s", hid);
		return authRepository.doFindByHid(hid);
	}

	public Auth findAuthBySamlIdp(String idp) {
		String method = "findAuthBySamlIdp";
		logInfo(method, "looking up auth idp: %s", idp);
		return authRepository.findBySamlIdp(idp);
	}

	public Role findRoleById(String id) {
		String method = "findRoleById";
		logInfo(method, "looking up role id: %s", id);
		return roleRepository.findById(id).orElse(null);
	}

	public Role findRoleByHid(String hid) {
		String method = "findRoleByHid";
		logInfo(method, "looking up role hid: %s", hid);
		return roleRepository.doFindByHid(hid);
	}

	public Privilege findPrivilegeById(String id) {
		String method = "findPrivilegeById";
		logInfo(method, "looking up privilege id: %s", id);
		return privilegeRepository.findById(id).orElse(null);
	}

	public Privilege findPrivilegeByHid(String hid) {
		String method = "findPrivilegeByHid";
		logInfo(method, "looking up privilege hid: %s", hid);
		return privilegeRepository.doFindByHid(hid);
	}

	public User findUserById(String id) {
		String method = "findUserById";
		logInfo(method, "looking up user id: %s", id);
		return userRepository.findById(id).orElse(null);
	}

	public User findUserByHid(String hid) {
		String method = "findUserByHid";
		logInfo(method, "looking up user hid: %s", hid);
		return userRepository.doFindByHid(hid);
	}

	public Region findRegionById(String id) {
		String method = "findRegionById";
		logInfo(method, "looking up region id: %s", id);
		return regionRepository.findById(id).orElse(null);
	}

	public Region findRegionByHid(String hid) {
		String method = "findRegionByHId";
		logInfo(method, "looking up region hid: %s", hid);
		return regionRepository.doFindByHid(hid);
	}

	public Region findRegionByName(String name) {
		String method = "findRegionByName";
		logInfo(method, "looking up region name: %s", name);
		return regionRepository.findByName(name);
	}

	public Zone findZoneById(String id) {
		String method = "findZoneById";
		logInfo(method, "looking up zone id: %s", id);
		return zoneRepository.findById(id).orElse(null);
	}

	public Zone findZoneByHid(String hid) {
		String method = "findZoneByHId";
		logInfo(method, "looking up zone hid: %s", hid);
		return zoneRepository.doFindByHid(hid);
	}

	public Zone findZoneBySystemName(String systemName) {
		String method = "findZoneBySystemName";
		logInfo(method, "looking up zone systemName: %s", systemName);
		return zoneRepository.findBySystemName(systemName);
	}

	public Application findApplicationById(String id) {
		String method = "findApplicationById";
		logInfo(method, "looking up application id: %s", id);
		return applicationRepository.findById(id).orElse(null);
	}

	public Application findApplicationByHid(String hid) {
		String method = "findApplicationByHid";
		logInfo(method, "looking up application hid: %s", hid);
		return applicationRepository.doFindByHid(hid);
	}

	public Application findApplicationByName(String name) {
		String method = "findApplicationByName";
		logInfo(method, "looking up application name: %s", name);
		return applicationRepository.findByName(name);
	}

	public Application findApplicationByCode(String code) {
		String method = "findApplicationByCode";
		logInfo(method, "looking up application code: %s", code);
		return applicationRepository.findByCode(code);
	}

	public Company findCompanyById(String id) {
		String method = "findCompanyById";
		logInfo(method, "looking up company id: %s", id);
		return companyRepository.findById(id).orElse(null);
	}

	public Company findCompanyByHid(String hid) {
		String method = "findProductByHid";
		logInfo(method, "looking up product hid: %s", hid);
		return companyRepository.doFindByHid(hid);
	}

	public Product findProductById(String id) {
		String method = "findProductById";
		logInfo(method, "looking up product id: %s", id);
		return productRepository.findById(id).orElse(null);
	}

	public Product findProductByHid(String hid) {
		String method = "findProductByHid";
		logInfo(method, "looking up product hid: %s", hid);
		return productRepository.doFindByHid(hid);
	}

	public Product findProductBySystemName(String systemName) {
		String method = "findProductBySystemName";
		logInfo(method, "looking up product systemName: %s", systemName);
		return productRepository.findBySystemName(systemName);
	}

	public Subscription findSubscriptionById(String id) {
		String method = "findSubscriptionById";
		logInfo(method, "looking up subscription id: %s", id);
		return subscriptionRepository.findById(id).orElse(null);
	}

	public Subscription findSubscriptionByHid(String hid) {
		String method = "findSubscriptionByHid";
		logInfo(method, "looking up subscription hid: %s", hid);
		return subscriptionRepository.doFindByHid(hid);
	}

	public List<Privilege> preloadPrivileges() {
		return privilegeRepository.findAll();
	}

	public List<Application> preloadApplications() {
		return applicationRepository.findAll();
	}

	public List<Company> preloadCompanies() {
		return companyRepository.findAll();
	}

	@Override
	public List<Product> preloadProducts() {
		return productRepository.findAll();
	}

	@Override
	public List<Role> preloadRoles() {
		return roleRepository.findAll();
	}
}
