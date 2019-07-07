package com.arrow.pegasus;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.arrow.acs.JsonUtils;
import com.arrow.acs.Loggable;
import com.arrow.pegasus.security.Crypto;
import com.arrow.pegasus.security.CryptoImpl;
import com.arrow.pegasus.util.SmtpEmailSender;
import com.arrow.pegasus.util.SmtpProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Caffeine;

public class CoreAppAbstract extends Loggable {

	@Value("${pegasus.local-cache-expires-hours:1}")
	private int localCacheExpiresHours;

	@Bean
	@Primary
	ObjectMapper objectMapper() {
		return JsonUtils.getObjectMapper();
	}

	@Bean
	Crypto crypto() {
		return new CryptoImpl();
	}

	@Bean
	SmtpEmailSender smtpEmailSender() {
		return new SmtpEmailSender();
	}

	@Bean
	SmtpProperties smtpProperties() {
		return new SmtpProperties();
	}

	@Bean
	CoreApplicationListener coreApplicationListener() {
		return new CoreApplicationListener();
	}

	@Bean
	CacheManager cacheManager() {
		String method = "cacheManager";
		logInfo(method, "initializing CaffeineCacheManager, localCacheExpiresHours: %d", localCacheExpiresHours);
		CaffeineCacheManager manager = new CaffeineCacheManager();
		manager.setCaffeine(Caffeine.newBuilder().expireAfterAccess(localCacheExpiresHours, TimeUnit.HOURS));
		return manager;
	}

	@Bean
	ConfigureRedisAction configureRedisAction(RedisConnectionFactory redisConnectionFactory) {
		String method = "configureRedisAction";
		ConfigureRedisAction result = new ConfigureAllRedisAction();
		RedisConnection connection = null;
		try {
			logInfo(method, "configuring redis action ...");
			result.configure(connection = redisConnectionFactory.getConnection());
		} catch (Exception e) {
			logError(method, "error configuring redis action", e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (Exception e) {
				}
			}
		}
		return result;
	}

	@Bean
	public ValidatingMongoEventListener validatingMongoEventListener() {
		return new ValidatingMongoEventListener(validator());
	}

	@Bean
	public LocalValidatorFactoryBean validator() {
		return new LocalValidatorFactoryBean();
	}
}
