package com.arrow.kronos.mig;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import com.arrow.acs.Loggable;
import com.arrow.pegasus.data.MigrationTask;
import com.arrow.pegasus.service.MigrationTaskService;

public abstract class MigrationTaskRunnerAbstract extends Loggable implements CommandLineRunner {

	@Autowired
	private MigrationTaskService migrationTaskService;

	private final String taskName;

	public MigrationTaskRunnerAbstract() {
		this.taskName = getClass().getName();
	}

	public MigrationTaskRunnerAbstract(String taskName) {
		this.taskName = taskName;
	}

	@Override
	public void run(String... args) throws Exception {
		String method = "run";

		MigrationTask task = migrationTaskService.getMigrationTaskRepository().findByName(taskName);
		if (task != null) {
			if (task.isComplete()) {
				logInfo(method, "task %s is already completed at %s", task.getName(), task.getLastAttempt());
			} else {
				logInfo(method, "task %s failed at %s, trying again ...", task.getName(), task.getLastAttempt());
				try {
					doTask();
					migrationTaskService.updateComplete(task, null);
					logInfo(method, "task %s is now complete at %s", task.getName(), task.getLastAttempt());
				} catch (Throwable t) {
					logError(method, t);
					migrationTaskService.updateError(task, ExceptionUtils.getStackTrace(t), null);
				}
			}
		} else {
			try {
				doTask();
				task = migrationTaskService.createComplete(taskName, null);
				logInfo(method, "task %s is now complete at %s", task.getName(), task.getLastAttempt());
			} catch (Throwable t) {
				logError(method, t);
				migrationTaskService.createError(taskName, ExceptionUtils.getStackTrace(t), null);
			}
		}
	}

	protected abstract void doTask();
}
