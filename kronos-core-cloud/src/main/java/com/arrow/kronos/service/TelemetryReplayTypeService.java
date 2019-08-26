package com.arrow.kronos.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arrow.kronos.KronosAuditLog;
import com.arrow.kronos.data.TelemetryReplayType;
import com.arrow.kronos.repo.TelemetryReplayTypeRepository;
import com.arrow.pegasus.data.AuditLogBuilder;

import moonstone.acs.AcsLogicalException;

@Service
public class TelemetryReplayTypeService extends KronosServiceAbstract {

	@Autowired
	private TelemetryReplayTypeRepository telemetryReplayTypeRepository;

	public TelemetryReplayTypeRepository getTelemetryReplayTypeRepository() {
		return telemetryReplayTypeRepository;
	}

	public TelemetryReplayType create(TelemetryReplayType telemetryReplayType, String who) {
		String method = "create";

		// logical checks
		if (telemetryReplayType == null) {
			logInfo(method, "telemetryReplayType is null");
			throw new AcsLogicalException("telemetryReplayType is null");
		}

		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}

		// insert
		telemetryReplayType = telemetryReplayTypeRepository.doInsert(telemetryReplayType, who);

		// write audit log
		getAuditLogService()
		        .save(AuditLogBuilder.create().type(KronosAuditLog.TelemetryReplayType.CreateTelemetryReplayType)
		                .applicationId(telemetryReplayType.getApplicationId()).objectId(telemetryReplayType.getId())
		                .by(who).parameter("name", telemetryReplayType.getName()));

		return telemetryReplayType;
	}

	public TelemetryReplayType update(TelemetryReplayType telemetryReplayType, String who) {
		String method = "update";

		// logical checks
		if (telemetryReplayType == null) {
			logInfo(method, "telemetryReplayType is null");
			throw new AcsLogicalException("telemetryReplayType is null");
		}

		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}

		// update
		telemetryReplayType = telemetryReplayTypeRepository.doSave(telemetryReplayType, who);

		// write audit log
		getAuditLogService()
		        .save(AuditLogBuilder.create().type(KronosAuditLog.TelemetryReplayType.UpdateTelemetryReplayType)
		                .applicationId(telemetryReplayType.getApplicationId()).objectId(telemetryReplayType.getId())
		                .by(who).parameter("name", telemetryReplayType.getName()));

		// clear cache
		TelemetryReplayType cachedTelemetryReplayType = getKronosCache()
		        .findTelemetryReplayTypeById(telemetryReplayType.getId());
		if (cachedTelemetryReplayType != null) {
			getKronosCache().clearTelemetryReplayType(cachedTelemetryReplayType);
		}

		return telemetryReplayType;
	}
}
