package com.arrow.pegasus.client.api;

import java.net.URI;
import java.util.List;

import org.apache.http.client.methods.HttpGet;
import org.springframework.stereotype.Component;

import com.arrow.pegasus.data.profile.Product;
import com.fasterxml.jackson.core.type.TypeReference;

@Component
public class ClientProductApi extends ClientApiAbstract {
	private static final String PRODUCTS_ROOT_URL = WEB_SERVICE_ROOT_URL + "/products";

	public List<Product> findAll() {
		String method = "findAll";
		try {
			URI uri = buildUri(PRODUCTS_ROOT_URL);
			List<Product> result = execute(new HttpGet(uri), new TypeReference<List<Product>>() {
			});
			logDebug(method, "found %d companies", result.size());
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public Product findById(String id) {
		String method = "findById";
		try {
			URI uri = buildUri(PRODUCTS_ROOT_URL + "/ids/" + id);
			Product result = execute(new HttpGet(uri), Product.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}
}
