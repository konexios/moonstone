package com.arrow.rhea.client.api;

import java.net.URI;
import java.util.List;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.springframework.stereotype.Component;

import com.arrow.rhea.client.model.SoftwareVendorModel;
import com.arrow.rhea.data.SoftwareVendor;
import com.fasterxml.jackson.core.type.TypeReference;

import moonstone.acs.JsonUtils;

@Component("RheaClientSoftwareVendorApi")
public class ClientSoftwareVendorApi extends ClientApiAbstract {
	private static final String SOFTWARE_VENDORS_ROOT_URL = WEB_SERVICE_ROOT_URL + "/software-vendors";

	public List<SoftwareVendor> findAll() {
		try {
			URI uri = buildUri(SOFTWARE_VENDORS_ROOT_URL);
			List<SoftwareVendor> result = execute(new HttpGet(uri), new TypeReference<List<SoftwareVendor>>() {
			});
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public SoftwareVendor create(SoftwareVendor softwareVendor, String who) {
		try {
			URI uri = buildUri(SOFTWARE_VENDORS_ROOT_URL);
			SoftwareVendorModel model = new SoftwareVendorModel().withModel(softwareVendor).withWho(who);
			SoftwareVendor result = execute(new HttpPost(uri), JsonUtils.toJson(model), SoftwareVendor.class);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public SoftwareVendor update(SoftwareVendor softwareVendor, String who) {
		try {
			URI uri = buildUri(SOFTWARE_VENDORS_ROOT_URL);
			SoftwareVendorModel model = new SoftwareVendorModel().withModel(softwareVendor).withWho(who);
			SoftwareVendor result = execute(new HttpPut(uri), JsonUtils.toJson(model), SoftwareVendor.class);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public List<SoftwareVendor> findAllByCompanyIdAndEnabled(String companyId, boolean enabled) {
		try {
			URI uri = buildUri(
			        SOFTWARE_VENDORS_ROOT_URL + "/companies/" + companyId + "/enabled/" + String.valueOf(enabled));
			List<SoftwareVendor> result = execute(new HttpGet(uri), new TypeReference<List<SoftwareVendor>>() {
			});
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}
	
	public SoftwareVendor findById(String id) {
		try {
			URI uri = buildUri(SOFTWARE_VENDORS_ROOT_URL + "/ids/" + id);
			SoftwareVendor result = execute(new HttpGet(uri), SoftwareVendor.class);
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}
}
