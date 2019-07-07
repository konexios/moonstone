package com.arrow.pegasus.cron;

public interface CronLogger {
	void addLog(String method, String log);

	void addError(String method, String error);

	void addException(String method, Throwable t);
}
