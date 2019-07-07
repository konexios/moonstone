package com.arrow.pegasus.cron;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.util.Assert;

public class CronReport {
	private final static String LINE_DELIMITER = "<br>\n";

	private final String name;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private List<String> logs = new ArrayList<>();
	private List<String> errors = new ArrayList<>();
	private List<String> exceptions = new ArrayList<>();

	public CronReport(String name) {
		this.name = name;
	}

	public void startCron() {
		Assert.isNull(startTime, "cron already started");
		startTime = LocalDateTime.now();
	}

	public void endCron() {
		Assert.isNull(endTime, "cron already ended");
		endTime = LocalDateTime.now();
	}

	public void addLog(String log) {
		if (log != null)
			logs.add(log);
	}

	public void addError(String error) {
		if (error != null)
			errors.add(error);
	}

	public void addException(Throwable t) {
		if (t != null)
			exceptions.add(ExceptionUtils.getStackTrace(t));
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public String getErrors() {
		return errors.stream().collect(Collectors.joining(LINE_DELIMITER));
	}

	public String getExceptions() {
		return exceptions.stream().collect(Collectors.joining(LINE_DELIMITER));
	}

	public String getLogs() {
		return logs.stream().collect(Collectors.joining(LINE_DELIMITER));
	}

	public String getName() {
		return name;
	}

	public String getStatus() {
		return errors.isEmpty() && exceptions.isEmpty() ? "Success" : "Failed";
	}
}
