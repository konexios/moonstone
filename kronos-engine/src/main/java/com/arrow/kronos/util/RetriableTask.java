package com.arrow.kronos.util;

import java.util.concurrent.Callable;

import com.arrow.acs.Loggable;

public abstract class RetriableTask<T> extends Loggable implements Callable<T> {

	private RetryStrategy retryStrategy;
	private boolean terminating = false;

	public RetriableTask(RetryStrategy retryStrategy) {
		this.retryStrategy = retryStrategy;
	}

	public T call() {
		String method = "call";
		T result = null;
		retryStrategy.start();
		while (!terminating) {
			try {
				result = execute();
				break;
			} catch (Throwable t) {
				onError();
				logError(method, t);
				try {
					if (retryStrategy.canRetry()) {
						logDebug(method, "retrying in %s ms ...", retryStrategy.getRetryIntervalMillis());
						Thread.sleep(retryStrategy.getRetryIntervalMillis());
					} else {
						logDebug(method, "stop retrying");
						break;
					}
				} catch (InterruptedException e) {
					logError(method, e);
				}
			}
		}
		retryStrategy.stop();
		return result;
	}

	public abstract T execute() throws Throwable;

	public void onError() {
	}

	public boolean isTerminating() {
		return terminating;
	}

	public void setTerminating(boolean terminating) {
		this.terminating = terminating;
	}
}
