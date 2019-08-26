package moonstone.acs.client.api;

import java.net.URI;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;

import moonstone.acs.JsonUtils;
import moonstone.acs.client.api.ApiConfig;
import moonstone.acs.client.model.HidModel;
import moonstone.acs.client.model.RoleModel;
import moonstone.acs.client.model.SubscriptionModel;

public final class RoleApi extends AcsApiAbstract {
	private static final String ROLES_ROOT_URL = WEB_SERVICE_ROOT_URL + "/roles";

	RoleApi(ApiConfig apiConfig) {
		super(apiConfig);
	}

	public RoleModel findByHid(String hid) {
		String method = "	public";
		try {
			URI uri = buildUri(ROLES_ROOT_URL + "/hids/" + hid);
			RoleModel result = execute(new HttpGet(uri), RoleModel.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "result: %s", JsonUtils.toJson(result));
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public HidModel createRole(SubscriptionModel model) {
		String method = "createRole";
		try {
			URI uri = buildUri(ROLES_ROOT_URL);
			HidModel result = execute(new HttpPost(uri), JsonUtils.toJson(model), HidModel.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "result: %s", JsonUtils.toJson(result));
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public HidModel updateRole(String hid, RoleModel model) {
		String method = "updateRole";
		try {
			URI uri = buildUri(ROLES_ROOT_URL + "/" + hid);
			HidModel result = execute(new HttpPut(uri), JsonUtils.toJson(model), HidModel.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "result: %s", JsonUtils.toJson(result));
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

}
