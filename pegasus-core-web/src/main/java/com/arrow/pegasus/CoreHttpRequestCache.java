package com.arrow.pegasus;

import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.Company;
import com.arrow.pegasus.data.profile.Product;
import com.arrow.pegasus.data.profile.Subscription;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.data.profile.Zone;
import com.arrow.pegasus.data.security.Privilege;
import com.arrow.pegasus.data.security.Role;
import com.arrow.pegasus.service.CoreCacheHelper;
import com.arrow.pegasus.service.CoreCacheService;

public class CoreHttpRequestCache {
	@Autowired
	private CoreCacheService coreCacheService;
	@Autowired
	private CoreCacheHelper coreCacheHelper;

	public Company findCompanyById(String id, boolean details) {
		return doCache(id, s -> String.format("%s:%s", Company.class.getName(), s), s -> {
			Company company = coreCacheService.findCompanyById(s);
			if (details)
				coreCacheHelper.populateCompany(company);
			return company;
		});
	}

	public Subscription findSubscriptionById(String id, boolean details) {
		return doCache(id, s -> String.format("%s:%s", Subscription.class.getName(), s), s -> {
			Subscription subscription = coreCacheService.findSubscriptionById(s);
			if (details)
				coreCacheHelper.populateSubscription(subscription);
			return subscription;
		});
	}

	public Application findApplicationById(String id, boolean details) {
		return doCache(id, s -> String.format("%s:%s", Application.class.getName(), s), s -> {
			Application application = coreCacheService.findApplicationById(s);
			if (details)
				coreCacheHelper.populateApplication(application);
			return application;
		});
	}

	public Product findProductById(String id, boolean details) {
		return doCache(id, s -> String.format("%s:%s", Product.class.getName(), s), s -> {
			Product product = coreCacheService.findProductById(s);
			if (details)
				coreCacheHelper.populateProduct(product);
			return product;
		});
	}

	public Role findRoleById(String id, boolean details) {
		return doCache(id, s -> String.format("%s:%s", Role.class.getName(), s), s -> {
			Role role = coreCacheService.findRoleById(s);
			if (details)
				coreCacheHelper.populateRole(role);
			return role;
		});
	}

	public Privilege findPrivilegeById(String id) {
		return doCache(id, s -> String.format("%s:%s", Privilege.class.getName(), s),
		        s -> coreCacheService.findPrivilegeById(s));
	}

	public User findUserById(String id, boolean details) {
		return doCache(id, s -> String.format("%s:%s", User.class.getName(), s), s -> {
			User user = coreCacheService.findUserById(s);
			if (details)
				coreCacheHelper.populateUser(user);
			return user;
		});
	}

	public Zone findZoneById(String id, boolean details) {
		return doCache(id, s -> String.format("%s:%s", Zone.class.getName(), s), s -> {
			Zone zone = coreCacheService.findZoneById(s);
			if (details)
				coreCacheHelper.populateZone(zone);
			return zone;
		});
	}

	public CoreCacheHelper getCoreCacheHelper() {
		return coreCacheHelper;
	}

	public CoreCacheService getCoreCacheService() {
		return coreCacheService;
	}

	@SuppressWarnings("unchecked")
	protected <T> T doCache(String id, Function<String, String> keyFunction, Function<String, T> lookupFunction) {
		String key = keyFunction.apply(id);
		RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
		Assert.notNull(attrs, "No HttpRequestContext found!");
		T result = (T) attrs.getAttribute(key, RequestAttributes.SCOPE_REQUEST);
		if (result == null) {
			result = lookupFunction.apply(id);
			attrs.setAttribute(key, result, RequestAttributes.SCOPE_REQUEST);
		}
		return result;
	}
}
