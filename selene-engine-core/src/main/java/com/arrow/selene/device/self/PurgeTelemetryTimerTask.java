package com.arrow.selene.device.self;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.Loggable;
import com.arrow.selene.data.Gateway;
import com.arrow.selene.service.TelemetryService;

class PurgeTelemetryTimerTask extends TimerTask {
	private static final Loggable LOGGER = new Loggable();
	private Gateway gateway;
	private AtomicBoolean running = new AtomicBoolean(false);

	public PurgeTelemetryTimerTask(Gateway gateway) {
		this.gateway = gateway;
	}

	@Override
	public void run() {
		String method = "PurgeTelemetryTimerTask.run";
		if (running.compareAndSet(false, true)) {
			Validate.notNull(gateway, "gateway is null");
			LOGGER.logInfo(method, "purging old telemetry ...");
			try {
				TelemetryService.getInstance().deleteTelemetryBefore(
						Instant.now().minus(gateway.getPurgeTelemetryIntervalDays(), ChronoUnit.DAYS).toEpochMilli());
				LOGGER.logInfo(method, "telemetry purged successfully");
			} catch (Throwable throwable) {
				LOGGER.logError(method, "failed to purge old telemetry", throwable);
			}
			running.set(false);
		}
	}
}
