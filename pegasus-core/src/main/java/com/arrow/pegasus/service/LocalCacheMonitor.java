package com.arrow.pegasus.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

@Service
public class LocalCacheMonitor extends ServiceAbstract implements MessageListener {
	private final static PatternTopic PATTERN_TOPIC = new PatternTopic("__keyevent@*__:incrby");

	private RedisMessageListenerContainer container;

	@Autowired
	private CacheManager cacheManager;

	@Override
	protected void postConstruct() {
		super.postConstruct();
		String method = "postContruct";
		container = new RedisMessageListenerContainer();
		container.setConnectionFactory(getRedis().getConnectionFactory());
		container.addMessageListener(this, PATTERN_TOPIC);
		container.setErrorHandler(e -> logError("There was an error in redis key expiration listener container", e));
		container.afterPropertiesSet();
		container.start();
		logInfo(method, "RedisMessageListenerContainer ready!");
	}

	@Override
	protected void preDestroy() {
		String method = "preDestroy";
		if (container != null) {
			try {
				logInfo(method, "destroying RedisMessageListenerContainer ...");
				container.destroy();
			} catch (Exception e) {
			}
		}
		super.preDestroy();
	}

	@Override
	public void onMessage(Message message, byte[] pattern) {
		String method = "onMessage";
		String key = new String(message.getBody());
		String tokens[] = key.split("/", -1);
		logDebug(method, "key: %s", key);
		try {
			if (tokens.length == 3) {
				String source = tokens[0];
				String name = tokens[1];
				String id = tokens[2];
				if (StringUtils.equals(name, CacheServiceAbstract.ALL_KEYS)) {
					for (String cacheName : cacheManager.getCacheNames()) {
						Cache cache = cacheManager.getCache(cacheName);
						if (cache != null) {
							logDebug(method, "[CLEAR ALL LOCAL CACHE] source: %s, clear all keys of cache: %s", source,
									cacheName);
							cache.clear();
						}
					}
				} else if (cacheManager.getCacheNames().contains(name)) {
					Cache cache = cacheManager.getCache(name);
					if (cache != null) {
						if (StringUtils.equals(id, CacheServiceAbstract.ALL_KEYS)) {
							logDebug(method, "source: %s, clear all keys of cache: %s", source, name);
							cache.clear();
						} else {
							logDebug(method, "source: %s, clear cache: %s, key: %s", source, name, id);
							cache.evict(id);
						}
					} else {
						logDebug(method, "source: %s, local cache not found: %s", source, name);
					}
				} else {
					logDebug(method, "source: %s, local cache not found: %s", source, name);
				}
			}
		} catch (Exception e) {
			logError(method, e);
		}
	}
}