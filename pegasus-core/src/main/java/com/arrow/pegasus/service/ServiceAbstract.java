package com.arrow.pegasus.service;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.Assert;

import com.arrow.pegasus.LifeCycleAbstract;
import com.arrow.pegasus.cron.CronLogger;
import com.arrow.pegasus.security.Crypto;

import moonstone.acs.AcsRuntimeException;
import moonstone.acs.AcsSystemException;

public abstract class ServiceAbstract extends LifeCycleAbstract {

	@Autowired
	private StringRedisTemplate redis;
	@Autowired
	private Crypto crypto;

	public ServiceAbstract() {
		logInfo(getClass().getSimpleName(), "...");
	}

	public String storeTemporaryContext(String json, long timeoutSeconds) {
		Assert.hasLength(json, "object is empty");
		Assert.isTrue(timeoutSeconds > 0, "timeoutSeconds is negative");
		String token = crypto.randomToken();
		redis.opsForValue().set(token, json, timeoutSeconds, TimeUnit.SECONDS);
		return token;
	}

	public String retrieveTemporaryContext(String token) {
		if (StringUtils.isNotEmpty(token) && redis.hasKey(token)) {
			return redis.opsForValue().get(token);
		} else {
			return null;
		}
	}

	protected StringRedisTemplate getRedis() {
		return redis;
	}

	protected AcsRuntimeException handleException(Exception e) {
		String method = "handleException";
		logError(method, e);
		if (e.getClass().isAssignableFrom(AcsRuntimeException.class)) {
			return (AcsRuntimeException) e;
		} else {
			return new AcsSystemException("Exception encountered: " + e.getClass().getName(), e);
		}
	}

	protected void addLog(CronLogger logger, String method, String message, Object... args) {
		logInfo(method, message, args);
		if (logger != null)
			logger.addLog(method, String.format(message, args));
	}

	protected void addError(CronLogger logger, String method, String message, Object... args) {
		logError(method, message, args);
		if (logger != null)
			logger.addError(method, String.format(message, args));
	}

	protected void addException(CronLogger logger, Throwable t, String method, String message, Object... args) {
		addError(logger, method, message, args);
		if (logger != null)
			logger.addException(method, t);
	}
}
