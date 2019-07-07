package com.arrow.selene.service;

import java.util.List;

import com.arrow.selene.dao.TelemetryDao;
import com.arrow.selene.data.Telemetry;

public class TelemetryService extends ServiceAbstract {

	private static class SingletonHolder {
		private static final TelemetryService SINGLETON = new TelemetryService();
	}

	public static TelemetryService getInstance() {
		return SingletonHolder.SINGLETON;
	}

	private final TelemetryDao telemetryDao;

	protected TelemetryService() {
		super();
		telemetryDao = TelemetryDao.getInstance();
	}

	protected TelemetryDao getTelemetryDao() {
		return telemetryDao;
	}

	public List<Telemetry> findTelemetryByDeviceId(int deviceId) {
		return telemetryDao.findByDeviceId(deviceId);
	}

	public List<Telemetry> findTelemetryBefore(long timestamp) {
		return telemetryDao.findByTimestampBefore(timestamp);
	}

	public Long findLastTimestamp(long deviceId) {
		Telemetry tel = telemetryDao.findLastTelemetryById(deviceId);
		if (tel != null) {
			return tel.getTimestamp();
		}
		return (long) 0;
	}

	public void deleteTelemetryBefore(long timestamp) {
		for (Telemetry telemetry : findTelemetryBefore(timestamp)) {
			telemetryDao.delete(telemetry.getId());
		}
	}
}
