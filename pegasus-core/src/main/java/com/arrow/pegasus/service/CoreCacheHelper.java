package com.arrow.pegasus.service;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.ApplicationEngine;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.Company;
import com.arrow.pegasus.data.profile.Product;
import com.arrow.pegasus.data.profile.Subscription;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.data.profile.Zone;
import com.arrow.pegasus.data.security.Privilege;
import com.arrow.pegasus.data.security.Role;

import moonstone.acs.Loggable;

@Component
public class CoreCacheHelper extends Loggable {
	@Autowired
	private CoreCacheService service;

	public User populateUser(User user) {
		String method = "populateUser";
		if (user != null) {
			if (!StringUtils.isEmpty(user.getCompanyId()) && user.getRefCompany() == null) {
				user.setRefCompany(service.findCompanyById(user.getCompanyId()));
			}
			if (user.getRefRoles().isEmpty() && !user.getRoleIds().isEmpty()) {
				// preload to speed up role populate method
				Map<String, Privilege> privilegeMap = service.getProxy().preloadPrivileges().stream()
				        .collect(Collectors.toMap(Privilege::getId, Function.identity()));
				for (String id : user.getRoleIds()) {
					Role role = populateRole(service.findRoleById(id), privilegeMap);
					if (role != null) {
						populateApplication(role.getRefApplication());
						populateProduct(role.getRefProduct());
						user.getRefRoles().add(role);
					} else {
						logError(method, "role not found for id: %s", id);
					}
				}
			}
		}
		return user;
	}

	public Application populateApplication(Application application) {
		String method = "populateApplication";
		if (application != null) {
			if (!StringUtils.isEmpty(application.getCompanyId()) && application.getRefCompany() == null)
				application.setRefCompany(service.findCompanyById(application.getCompanyId()));

			if (!StringUtils.isEmpty(application.getProductId()) && application.getRefProduct() == null)
				application.setRefProduct(service.findProductById(application.getProductId()));

			if (!StringUtils.isEmpty(application.getSubscriptionId()) && application.getRefSubscription() == null)
				application.setRefSubscription(service.findSubscriptionById(application.getSubscriptionId()));

			if (!StringUtils.isEmpty(application.getZoneId()) && application.getRefZone() == null)
				application.setRefZone(populateZone(service.findZoneById(application.getZoneId())));

			if (!StringUtils.isEmpty(application.getApplicationEngineId())
			        && application.getRefApplicationEngine() == null)
				application.setRefApplicationEngine(
				        service.findApplicationEngineById(application.getApplicationEngineId()));

			if (application.getRefProductExtensions().isEmpty() && !application.getProductExtensionIds().isEmpty()) {
				for (String id : application.getProductExtensionIds()) {
					Product product = service.findProductById(id);
					if (product != null) {
						application.getRefProductExtensions().add(product);
					} else {
						logError(method, "product not found for id: %s", id);
					}
				}
			}
		}
		return application;
	}

	public ApplicationEngine populateApplicationEngine(ApplicationEngine engine) {
		if (engine != null) {
			if (!StringUtils.isEmpty(engine.getProductId()) && engine.getRefProduct() == null)
				engine.setRefProduct(service.findProductById(engine.getProductId()));
			if (!StringUtils.isEmpty(engine.getZoneId()) && engine.getRefZone() == null)
				engine.setRefZone(service.findZoneById(engine.getZoneId()));
		}
		return engine;
	}

	public Role populateRole(Role role) {
		return populateRole(role, null);
	}

	public Role populateRole(Role role, Map<String, Privilege> privilegeMap) {
		String method = "populateRole";
		if (role != null) {
			if (!StringUtils.isEmpty(role.getApplicationId()) && role.getRefApplication() == null)
				role.setRefApplication(service.findApplicationById(role.getApplicationId()));

			if (role.getRefPrivileges().isEmpty() && !role.getPrivilegeIds().isEmpty()) {
				if (privilegeMap == null)
					privilegeMap = service.getProxy().preloadPrivileges().stream()
					        .collect(Collectors.toMap(Privilege::getId, Function.identity()));
				for (String privId : role.getPrivilegeIds()) {
					Privilege priv = privilegeMap.get(privId);
					if (priv != null) {
						role.getRefPrivileges().add(priv);
					} else {
						logError(method, "privilege not found for id: %s", privId);
					}
				}
			}
			if (!StringUtils.isEmpty(role.getProductId()) && role.getRefProduct() == null) {
				role.setRefProduct(service.findProductById(role.getProductId()));
			}
		}
		return role;
	}

	public Zone populateZone(Zone zone) {
		if (zone != null) {
			if (!StringUtils.isEmpty(zone.getRegionId()) && zone.getRefRegion() == null) {
				zone.setRefRegion(service.findRegionById(zone.getRegionId()));
			}
		}
		return zone;
	}

	public Subscription populateSubscription(Subscription subscription) {
		if (subscription != null) {
			if (!StringUtils.isEmpty(subscription.getCompanyId()) && subscription.getRefCompany() == null) {
				subscription.setRefCompany(service.findCompanyById(subscription.getCompanyId()));
			}
		}
		return subscription;
	}

	public AccessKey populateAccessKey(AccessKey accessKey) {
		if (accessKey != null) {
			if (!StringUtils.isEmpty(accessKey.getApplicationId()) && accessKey.getRefApplication() == null) {
				accessKey.setRefApplication(service.findApplicationById(accessKey.getApplicationId()));
			}
			if (!StringUtils.isEmpty(accessKey.getCompanyId()) && accessKey.getRefCompany() == null) {
				accessKey.setRefCompany(service.findCompanyById(accessKey.getCompanyId()));
			}
		}
		return accessKey;
	}

	public Company populateCompany(Company company) {
		if (company != null) {
			if (!StringUtils.isEmpty(company.getParentCompanyId()) && company.getRefParentCompany() == null) {
				company.setRefParentCompany(service.findCompanyById(company.getParentCompanyId()));
			}
		}
		return company;
	}

	public Product populateProduct(Product product) {
		if (product != null) {
			if (!StringUtils.isEmpty(product.getParentProductId()) && product.getRefParentProduct() == null) {
				product.setRefParentProduct(service.findProductById(product.getParentProductId()));
			}
		}
		return product;
	}
}
