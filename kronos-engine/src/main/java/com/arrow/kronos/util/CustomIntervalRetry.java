package com.arrow.kronos.util;

import java.time.Duration;
import java.time.Instant;

public class CustomIntervalRetry implements RetryStrategy {

	private Duration intervals[];
	private int currIndex;
	private Instant startTime;

	public CustomIntervalRetry(Duration... intervals) {
		this.intervals = intervals;
	}

	public CustomIntervalRetry(String... text) {
		this.intervals = new Duration[text.length];
		for (int i = 0; i < text.length; i++) {
			intervals[i] = Duration.parse(text[i]);
		}
	}

	public long getRetryIntervalMillis() {
		return intervals[currIndex].toMillis();
	}

	public boolean canRetry() {
		boolean result = true;
		if (currIndex + 1 < intervals.length && startTime.plus(intervals[currIndex + 1]).isBefore(Instant.now())) {
			currIndex++;
		}
		if (currIndex + 1 >= intervals.length) {
			result = false;
		}
		return result;
	}

	public void start() {
		startTime = Instant.now();
		currIndex = 0;
	}

	public void stop() {
		startTime = null;
		currIndex = 0;
	}

	public Instant getStartTime() {
		return startTime;
	}
}
