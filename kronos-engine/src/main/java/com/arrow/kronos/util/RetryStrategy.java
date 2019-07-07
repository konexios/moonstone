package com.arrow.kronos.util;

public interface RetryStrategy {

	public boolean canRetry();

	public long getRetryIntervalMillis();

	public void start();

	public void stop();
}
