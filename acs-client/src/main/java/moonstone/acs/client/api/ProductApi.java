package moonstone.acs.client.api;

import java.net.URI;

import org.apache.http.client.methods.HttpGet;

import com.fasterxml.jackson.core.type.TypeReference;

import moonstone.acs.JsonUtils;
import moonstone.acs.client.api.ApiConfig;
import moonstone.acs.client.model.ListResultModel;
import moonstone.acs.client.model.ProductModel;

public final class ProductApi extends AcsApiAbstract {
	private static final String PRODUCTS_ROOT_URL = WEB_SERVICE_ROOT_URL + "/products";

	// instantiation is expensive for these objects
	private TypeReference<ListResultModel<ProductModel>> productModelTypeRef;

	ProductApi(ApiConfig apiConfig) {
		super(apiConfig);
	}

	public ProductModel findByHid(String hid) {
		String method = "findByHid";
		try {
			URI uri = buildUri(String.format("%s/%s", PRODUCTS_ROOT_URL, hid));
			ProductModel result = execute(new HttpGet(uri), ProductModel.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "result: %s", JsonUtils.toJson(result));
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public ListResultModel<ProductModel> findAll() {
		String method = "findAll";
		try {
			URI uri = buildUri(PRODUCTS_ROOT_URL);
			ListResultModel<ProductModel> result = execute(new HttpGet(uri), getProductModelTypeRef());
			log(method, result);
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	synchronized TypeReference<ListResultModel<ProductModel>> getProductModelTypeRef() {
		return productModelTypeRef != null ? productModelTypeRef
				: (productModelTypeRef = new TypeReference<ListResultModel<ProductModel>>() {
				});
	}
}
