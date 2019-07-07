package com.arrow.pegasus.core.api;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.api.ApiAbstract;

public abstract class UserApiAbstract extends ApiAbstract {

	// TODO temporary solution, need to implement batch management
	private ExecutorService service;

	// @Autowired
	// private ClientUserApi userApi;

	public UserApiAbstract() {
		service = Executors.newCachedThreadPool();
	}

	@Override
	protected void preDestroy() {
		String method = "preDestroy";
		super.preDestroy();
		if (service != null) {
			try {
				logInfo(method, "shutting down service ...");
				service.shutdown();
				service.awaitTermination(CoreConstant.DEFAULT_SHUTDOWN_WAIT_TIMEOUT_MS, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
			} finally {
				if (!service.isTerminated())
					service.shutdownNow();
			}
		}
	}

	// @RequestMapping(path = "/sync-saml-accounts", method =
	// RequestMethod.POST)
	// public StatusModel syncSamlAccounts(@RequestBody(required = false)
	// List<SamlAccountModel> body) {
	// String method = "syncSamlAccounts";
	// TypeReference<List<SamlAccountModel>> typeRef = new
	// TypeReference<List<SamlAccountModel>>() {
	// };
	// final List<SamlAccountModel> models = JsonUtils.fromJson(getApiPayload(),
	// typeRef);
	// final String applicationId = getAccessKey().getApplicationId();
	// logInfo(method, "applicationId: %s, models size: %d", applicationId,
	// models.size());
	// if (!models.isEmpty()) {
	// service.execute(new Runnable() {
	// @Override
	// public void run() {
	// try {
	// logInfo(method, "calling syncSamlAccounts ...");
	// userApi.syncSamlAccounts(applicationId, models);
	// } catch (Throwable t) {
	// logError(method, t);
	// }
	// }
	// });
	// }
	// return StatusModel.OK;
	// }
}
