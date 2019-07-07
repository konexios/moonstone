package com.arrow.selene.engine.service;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.data.Telemetry;

public class TelemetryService extends com.arrow.selene.service.TelemetryService {

	private static class SingletonHolder {
		private static final TelemetryService SINGLETON = new TelemetryService();
	}

	public static TelemetryService getInstance() {
		return SingletonHolder.SINGLETON;
	}

	public Telemetry save(Telemetry telemetry) {
		Validate.notNull(telemetry, "telemetry is null");
		getTelemetryDao().insert(telemetry);
		return telemetry;
	}
}
