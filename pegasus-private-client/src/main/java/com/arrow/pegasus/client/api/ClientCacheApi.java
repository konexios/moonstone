package com.arrow.pegasus.client.api;

import java.net.URI;
import java.util.Map;

import org.apache.http.client.methods.HttpGet;
import org.springframework.stereotype.Component;

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
import com.fasterxml.jackson.core.type.TypeReference;

@Component
public class ClientCacheApi extends ClientApiAbstract {
	private static final String CACHE_ROOT_URL = WEB_SERVICE_ROOT_URL + "/cache";

	public Map<String, Long> listAllCacheTimestamps() {
		try {
			return execute(new HttpGet(buildUri(CACHE_ROOT_URL + "/timestamps")),
			        new TypeReference<Map<String, Long>>() {
			        });
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public AccessKey findAccessKeyById(String id) {
		String method = "findAccessKeyById";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/access-keys/ids/" + id);
			AccessKey result = execute(new HttpGet(uri), AccessKey.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public AccessKey findAccessKeyByHashedApiKey(String hashedApiKey) {
		String method = "findAccessKeyByHashedApiKey";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/access-keys/hashed-api-keys/" + hashedApiKey);
			AccessKey result = execute(new HttpGet(uri), AccessKey.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}
	
	public AccessKey findOwnerAccessKeyByPri(String pri) {
		String method = "findOwnerAccessKeyByPri";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/access-keys/pris/" + pri);
			AccessKey result = execute(new HttpGet(uri), AccessKey.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public ApplicationEngine findApplicationEngineById(String id) {
		String method = "findApplicationEngineById";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/application-engines/ids/" + id);
			ApplicationEngine result = execute(new HttpGet(uri), ApplicationEngine.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public ApplicationEngine findApplicationEngineByHid(String hid) {
		String method = "findApplicationEngineByHid";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/application-engines/hids/" + hid);
			ApplicationEngine result = execute(new HttpGet(uri), ApplicationEngine.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public Auth findAuthById(String id) {
		String method = "findAuthById";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/auths/ids/" + id);
			Auth result = execute(new HttpGet(uri), Auth.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s", result.getId(), result.getHid());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public Auth findAuthByHid(String hid) {
		String method = "findAuthByHid";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/auths/hids/" + hid);
			Auth result = execute(new HttpGet(uri), Auth.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s", result.getId(), result.getHid());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public Auth findAuthBySamlIdp(String idp) {
		String method = "findAuthBySamlIdp";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/auths/saml/idps/" + idp);
			Auth result = execute(new HttpGet(uri), Auth.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s", result.getId(), result.getSaml().getIdp());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public Role findRoleById(String id) {
		String method = "findRoleById";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/roles/ids/" + id);
			Role result = execute(new HttpGet(uri), Role.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public Role findRoleByHid(String hid) {
		String method = "findRoleByHid";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/roles/hids/" + hid);
			Role result = execute(new HttpGet(uri), Role.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public Privilege findPrivilegeById(String id) {
		String method = "findPrivilegeById";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/privileges/ids/" + id);
			Privilege result = execute(new HttpGet(uri), Privilege.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public Privilege findPrivilegeByHid(String hid) {
		String method = "findPrivilegeByHid";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/privileges/hids/" + hid);
			Privilege result = execute(new HttpGet(uri), Privilege.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public User findUserByHid(String hid) {
		String method = "findUserByHid";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/users/hids/" + hid);
			User result = execute(new HttpGet(uri), User.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s", result.getId(), result.getHid());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public User findUserById(String id) {
		String method = "findUserById";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/users/ids/" + id);
			User result = execute(new HttpGet(uri), User.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s", result.getId(), result.getHid());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public Region findRegionById(String id) {
		String method = "findRegionById";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/regions/ids/" + id);
			Region result = execute(new HttpGet(uri), Region.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public Region findRegionByHid(String hid) {
		String method = "findRegionByHid";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/regions/hids/" + hid);
			Region result = execute(new HttpGet(uri), Region.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public Region findRegionByName(String name) {
		String method = "findRegionByName";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/regions/names/" + name);
			Region result = execute(new HttpGet(uri), Region.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public Zone findZoneById(String id) {
		String method = "findZoneById";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/zones/ids/" + id);
			Zone result = execute(new HttpGet(uri), Zone.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public Zone findZoneByHid(String hid) {
		String method = "findZoneByHid";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/zones/hids/" + hid);
			Zone result = execute(new HttpGet(uri), Zone.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public Zone findZoneBySystemName(String systemName) {
		String method = "findZoneBySystemName";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/zones/names/" + systemName);
			Zone result = execute(new HttpGet(uri), Zone.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, systemName: %s, name: %s", result.getId(), result.getHid(),
				        result.getSystemName(), result.getName());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public Application findApplicationById(String id) {
		String method = "findApplicationById";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/applications/ids/" + id);
			Application result = execute(new HttpGet(uri), Application.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public Application findApplicationByHid(String hid) {
		String method = "findApplicationByHid";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/applications/hids/" + hid);
			Application result = execute(new HttpGet(uri), Application.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public Application findApplicationByName(String name) {
		String method = "findApplicationByName";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/applications/names/" + name);
			Application result = execute(new HttpGet(uri), Application.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public Application findApplicationByCode(String code) {
		String method = "findApplicationByCode";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/applications/codes/" + code);
			Application result = execute(new HttpGet(uri), Application.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public Company findCompanyById(String id) {
		String method = "findCompanyById";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/companies/ids/" + id);
			Company result = execute(new HttpGet(uri), Company.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public Company findCompanyByHid(String hid) {
		String method = "findCompanyByHid";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/companies/hids/" + hid);
			Company result = execute(new HttpGet(uri), Company.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public Product findProductById(String id) {
		String method = "findProductById";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/products/ids/" + id);
			Product result = execute(new HttpGet(uri), Product.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public Product findProductByHid(String hid) {
		String method = "findProductByHid";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/products/hids/" + hid);
			Product result = execute(new HttpGet(uri), Product.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public Product findProductBySystemName(String systemName) {
		String method = "findProductBySystemName";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/products/system-names/" + systemName);
			Product result = execute(new HttpGet(uri), Product.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public Subscription findSubscriptionById(String id) {
		String method = "findSubscriptionById";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/subscriptions/ids/" + id);
			Subscription result = execute(new HttpGet(uri), Subscription.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public Subscription findSubscriptionByHid(String hid) {
		String method = "findSubscriptionByHid";
		try {
			URI uri = buildUri(CACHE_ROOT_URL + "/subscriptions/hids/" + hid);
			Subscription result = execute(new HttpGet(uri), Subscription.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}
}
