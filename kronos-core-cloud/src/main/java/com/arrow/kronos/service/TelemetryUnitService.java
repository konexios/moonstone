package com.arrow.kronos.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arrow.kronos.KronosAuditLog;
import com.arrow.kronos.data.TelemetryUnit;
import com.arrow.kronos.repo.TelemetryUnitRepository;
import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.data.AuditLogBuilder;

import moonstone.acs.AcsLogicalException;

@Service
public class TelemetryUnitService extends KronosServiceAbstract {

	@Autowired
	private TelemetryUnitRepository telemetryUnitRepository;

	public TelemetryUnitRepository getTelemetryUnitRepository() {
		return telemetryUnitRepository;
	}

	public TelemetryUnit create(TelemetryUnit telemetryUnit, String who) {
		String method = "create";

		// logical checks
		if (telemetryUnit == null) {
			logInfo(method, "telemetryUnit is null");
			throw new AcsLogicalException("telemetryUnit is null");
		}

		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}

		// insert
		telemetryUnit = telemetryUnitRepository.doInsert(telemetryUnit, who);

		// write audit log
		getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.TelemetryUnit.CreateTelemetryUnit)
		        .productName(ProductSystemNames.KRONOS).objectId(telemetryUnit.getId()).by(who)
		        .parameter("name", telemetryUnit.getName()));

		return telemetryUnit;
	}

	public TelemetryUnit update(TelemetryUnit telemetryUnit, String who) {
		String method = "update";

		// logical checks
		if (telemetryUnit == null) {
			logInfo(method, "telemetryUnit is null");
			throw new AcsLogicalException("telemetryUnit is null");
		}

		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}

		// update
		telemetryUnit = telemetryUnitRepository.doSave(telemetryUnit, who);

		// write audit log
		getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.DeviceType.UpdateDeviceType)
		        .productName(ProductSystemNames.KRONOS).objectId(telemetryUnit.getId()).by(who)
		        .parameter("name", telemetryUnit.getName()));

		// clear cache
		TelemetryUnit cachedTelemetryUnit = getKronosCache().findTelemetryUnitById(telemetryUnit.getId());
		if (cachedTelemetryUnit != null) {
			getKronosCache().clearTelemetryUnit(cachedTelemetryUnit);
		}

		return telemetryUnit;
	}
}
