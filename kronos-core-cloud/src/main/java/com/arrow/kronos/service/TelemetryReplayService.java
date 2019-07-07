package com.arrow.kronos.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.acs.AcsLogicalException;
import com.arrow.kronos.KronosAuditLog;
import com.arrow.kronos.data.TelemetryReplay;
import com.arrow.kronos.repo.TelemetryReplayRepository;
import com.arrow.pegasus.data.AuditLogBuilder;

@Service
public class TelemetryReplayService extends KronosServiceAbstract {

	@Autowired
	private TelemetryReplayRepository telemetryReplayRepository;

	public TelemetryReplayRepository getTelemetryReplayRepository() {
		return telemetryReplayRepository;
	}

	public TelemetryReplay create(TelemetryReplay telemetryReplay, String who) {
		String method = "create";

		// logical checks
		if (telemetryReplay == null) {
			logInfo(method, "telemetryReplay is null");
			throw new AcsLogicalException("telemetryReplay is null");
		}

		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}

		// insert
		telemetryReplay = telemetryReplayRepository.doInsert(telemetryReplay, who);

		// write audit log
		getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.TelemetryReplay.CreateTelemetryReplay)
		        .applicationId(telemetryReplay.getApplicationId()).objectId(telemetryReplay.getId()).by(who)
		        .parameter("name", telemetryReplay.getName()));

		return telemetryReplay;
	}

	public TelemetryReplay update(TelemetryReplay telemetryReplay, String who) {
		String method = "update";

		// logical checks
		if (telemetryReplay == null) {
			logInfo(method, "telemetryReplay is null");
			throw new AcsLogicalException("telemetryReplay is null");
		}

		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}

		// update
		telemetryReplay = telemetryReplayRepository.doSave(telemetryReplay, who);

		// write audit log
		getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.TelemetryReplay.UpdateTelemetryReplay)
		        .applicationId(telemetryReplay.getApplicationId()).objectId(telemetryReplay.getId()).by(who)
		        .parameter("name", telemetryReplay.getName()));

		return telemetryReplay;
	}

	public Long deleteByApplicationId(String applicationId, String who) {
		String method = "deleteByApplicationId";
		Assert.hasText(applicationId, "applicationId is empty");
		Assert.hasText(who, "who is empty");
		logInfo(method, "applicationId: %s, who: %s", applicationId, who);
		return telemetryReplayRepository.deleteByApplicationId(applicationId);
	}
}
