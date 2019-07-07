package com.arrow.pegasus.client.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import com.arrow.pegasus.client.api.ClientApplicationApi;
import com.arrow.pegasus.client.api.ClientCacheApi;
import com.arrow.pegasus.client.api.ClientCompanyApi;
import com.arrow.pegasus.client.api.ClientPrivilegeApi;
import com.arrow.pegasus.client.api.ClientProductApi;
import com.arrow.pegasus.client.api.ClientRoleApi;
import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.ApplicationEngine;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.Company;
//import com.arrow.pegasus.data.profile.Gateway;
import com.arrow.pegasus.data.profile.Product;
import com.arrow.pegasus.data.profile.Region;
import com.arrow.pegasus.data.profile.Subscription;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.data.profile.Zone;
import com.arrow.pegasus.data.security.Auth;
import com.arrow.pegasus.data.security.Privilege;
import com.arrow.pegasus.data.security.Role;
import com.arrow.pegasus.service.CoreCacheProxyAbstract;

public class RemoteCoreCacheProxy extends CoreCacheProxyAbstract {

	@Autowired
	private ClientCacheApi clientCacheApi;
	@Autowired
	private ClientPrivilegeApi clientPrivilegeApi;
	@Autowired
	private ClientApplicationApi clientApplicationApi;
	@Autowired
	private ClientCompanyApi clientCompanyApi;
	@Autowired
	private ClientProductApi clientProductApi;
	@Autowired
	private ClientRoleApi clientRoleApi;

	public AccessKey findAccessKeyById(String id) {
		return clientCacheApi.findAccessKeyById(id);
	}

	public AccessKey findAccessKeyByHashedApiKey(String hashedApiKey) {
		return clientCacheApi.findAccessKeyByHashedApiKey(hashedApiKey);
	}

	@Override
	public AccessKey findOwnerAccessKeyByPri(String pri) {
		return clientCacheApi.findOwnerAccessKeyByPri(pri);
	}

	public ApplicationEngine findApplicationEngineById(String id) {
		return clientCacheApi.findApplicationEngineById(id);
	}

	public ApplicationEngine findApplicationEngineByHid(String hid) {
		return clientCacheApi.findApplicationEngineByHid(hid);
	}

	public Auth findAuthById(String id) {
		return clientCacheApi.findAuthById(id);
	}

	public Auth findAuthByHid(String hid) {
		return clientCacheApi.findAuthByHid(hid);
	}

	public Auth findAuthBySamlIdp(String idp) {
		return clientCacheApi.findAuthBySamlIdp(idp);
	}

	public Role findRoleById(String id) {
		return clientCacheApi.findRoleById(id);
	}

	public Role findRoleByHid(String hid) {
		return clientCacheApi.findRoleByHid(hid);
	}

	public Privilege findPrivilegeById(String id) {
		return clientCacheApi.findPrivilegeById(id);
	}

	public Privilege findPrivilegeByHid(String hid) {
		return clientCacheApi.findPrivilegeByHid(hid);
	}

	public User findUserById(String id) {
		return clientCacheApi.findUserById(id);
	}

	public User findUserByHid(String hid) {
		return clientCacheApi.findUserByHid(hid);
	}

	public Region findRegionById(String id) {
		return clientCacheApi.findRegionById(id);
	}

	public Region findRegionByHid(String hid) {
		return clientCacheApi.findRegionByHid(hid);
	}

	public Region findRegionByName(String name) {
		return clientCacheApi.findRegionByName(name);
	}

	public Zone findZoneById(String id) {
		return clientCacheApi.findZoneById(id);
	}

	public Zone findZoneByHid(String hid) {
		return clientCacheApi.findZoneByHid(hid);
	}

	public Zone findZoneBySystemName(String name) {
		return clientCacheApi.findZoneBySystemName(name);
	}

	public Application findApplicationById(String id) {
		return clientCacheApi.findApplicationById(id);
	}

	public Application findApplicationByHid(String hid) {
		return clientCacheApi.findApplicationByHid(hid);
	}

	public Application findApplicationByName(String name) {
		return clientCacheApi.findApplicationByName(name);
	}

	public Application findApplicationByCode(String code) {
		return clientCacheApi.findApplicationByCode(code);
	}

	public Company findCompanyById(String id) {
		return clientCacheApi.findCompanyById(id);
	}

	public Company findCompanyByHid(String hid) {
		return clientCacheApi.findCompanyByHid(hid);
	}

	public Product findProductById(String id) {
		return clientCacheApi.findProductById(id);
	}

	public Product findProductByHid(String hid) {
		return clientCacheApi.findProductByHid(hid);
	}

	public Product findProductBySystemName(String systemName) {
		return clientCacheApi.findProductBySystemName(systemName);
	}

	public Subscription findSubscriptionById(String id) {
		return clientCacheApi.findSubscriptionById(id);
	}

	public Subscription findSubscriptionByHid(String hid) {
		return clientCacheApi.findSubscriptionByHid(hid);
	}

	public List<Privilege> preloadPrivileges() {
		return clientPrivilegeApi.findAll(PageRequest.of(0, 100000)).getContent();
	}

	@Override
	public List<Application> preloadApplications() {
		return clientApplicationApi.findAll();
	}

	@Override
	public List<Company> preloadCompanies() {
		return clientCompanyApi.findAll();
	}

	@Override
	public List<Product> preloadProducts() {
		return clientProductApi.findAll();
	}

	@Override
	public List<Role> preloadRoles() {
		return clientRoleApi.findAll();
	}
}
