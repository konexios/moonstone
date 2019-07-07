package com.arrow.dashboard.runtime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.arrow.dashboard.DashboardEntityAbstract;
import com.arrow.pegasus.service.CryptoService;

public abstract class RuntimeManagerAbstract<T> extends DashboardEntityAbstract {

	@Autowired
	private CryptoService cryptoService;

	private Map<String, T> runtimeInstances = new HashMap<>();

	protected String createRuntimeInstanceId() {
		return cryptoService.getCrypto().randomToken();
	}

	protected void registerRuntimeInstance(String runtimeId, T runtimeInstance) {
		Assert.hasText(runtimeId, "runtimeId is empty");
		Assert.notNull(runtimeInstance, "runtimeInstance is null");

		runtimeInstances.put(runtimeId, runtimeInstance);
	}

	protected T getRuntimeInstance(String runtimeId) {
		Assert.hasText(runtimeId, "runtimeId is empty");

		return runtimeInstances.get(runtimeId);
	}

	protected List<T> getRuntimeInstances() {
		if (runtimeInstances.isEmpty())
			return Collections.emptyList();

		return new ArrayList<T>(runtimeInstances.values());
	}

	protected List<String> getRuntimeInstanceIds() {
		if (runtimeInstances.isEmpty())
			return Collections.emptyList();

		return new ArrayList<String>(runtimeInstances.keySet());
	}

	protected T unregisterRuntimeInstance(String runtimeId) {
		Assert.hasText(runtimeId, "runtimeId is empty");

		T runtimeInstance = getRuntimeInstance(runtimeId);

		runtimeInstances.remove(runtimeId);

		return runtimeInstance;
	}

	protected T updateRegisteredRuntimeInstance(String runtimeId, T runtimeInstance) {
		Assert.hasText(runtimeId, "runtimeId is empty");
		Assert.notNull(runtimeInstance, "runtimeInstance is null");
		Assert.isTrue(runtimeInstanceExists(runtimeId), "runtimeInstance does not exist! runtimeId=" + runtimeId);

		return runtimeInstances.put(runtimeId, runtimeInstance);
	}

	protected boolean runtimeInstanceExists(String runtimeId) {
		return getRuntimeInstance(runtimeId) != null;
	}

	public int getNumberOfRuntimeInstances() {
		return runtimeInstances.size();
	}
}
