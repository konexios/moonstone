package com.arrow.pegasus.service;

import java.time.Instant;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arrow.pegasus.CoreAuditLog;
import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.data.AuditLogBuilder;
import com.arrow.pegasus.data.MigrationTask;
import com.arrow.pegasus.repo.MigrationTaskRepository;

@Service
public class MigrationTaskService extends BaseServiceAbstract {

	@Autowired
	private MigrationTaskRepository migrationTaskRepository;

	public MigrationTask createComplete(String name, String who) {
		if (StringUtils.isEmpty(who))
			who = CoreConstant.ADMIN_USER;

		MigrationTask task = new MigrationTask();
		task.setName(name);
		task.setError("");
		task.setComplete(true);
		task.setLastAttempt(Instant.now());

		migrationTaskRepository.doInsert(task, who);

		getAuditLogService().save(AuditLogBuilder.create().type(CoreAuditLog.MigrationTask.CREATE_COMPLETE)
		        .productName(ProductSystemNames.PEGASUS).objectId(task.getId()).by(who)
		        .parameter("name", task.getName()));

		return task;
	}

	public MigrationTask createError(String name, String error, String who) {
		if (StringUtils.isEmpty(who))
			who = CoreConstant.ADMIN_USER;

		MigrationTask task = new MigrationTask();
		task.setName(name);
		task.setError(error);
		task.setComplete(false);
		task.setLastAttempt(Instant.now());

		migrationTaskRepository.doInsert(task, who);

		getAuditLogService().save(AuditLogBuilder.create().type(CoreAuditLog.MigrationTask.CREATE_ERROR)
		        .productName(ProductSystemNames.PEGASUS).objectId(task.getId()).by(who)
		        .parameter("name", task.getName()).parameter("error", error));

		return task;
	}

	public MigrationTask updateComplete(MigrationTask task, String who) {
		if (StringUtils.isEmpty(who))
			who = CoreConstant.ADMIN_USER;

		task.setError("");
		task.setComplete(true);
		task.setLastAttempt(Instant.now());

		migrationTaskRepository.doSave(task, who);

		getAuditLogService().save(AuditLogBuilder.create().type(CoreAuditLog.MigrationTask.UPDATE_COMPLETE)
		        .productName(ProductSystemNames.PEGASUS).objectId(task.getId()).by(who)
		        .parameter("name", task.getName()));

		return task;
	}

	public MigrationTask updateError(MigrationTask task, String error, String who) {
		if (StringUtils.isEmpty(who))
			who = CoreConstant.ADMIN_USER;

		task.setError(error);
		task.setComplete(false);
		task.setLastAttempt(Instant.now());

		migrationTaskRepository.doSave(task, who);

		getAuditLogService().save(AuditLogBuilder.create().type(CoreAuditLog.MigrationTask.UPDATE_ERROR)
		        .productName(ProductSystemNames.PEGASUS).objectId(task.getId()).by(who)
		        .parameter("name", task.getName()).parameter("error", error));

		return task;
	}

	public MigrationTaskRepository getMigrationTaskRepository() {
		return migrationTaskRepository;
	}
}
