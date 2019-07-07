package com.arrow.pegasus.service;

import java.net.InetAddress;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;

public abstract class CacheServiceAbstract extends ServiceAbstract {

    public static final String ALL_KEYS = "*";

    @Autowired
    private CacheManager cacheManager;

    private String hostName = "UnknownHost";

    public CacheServiceAbstract() {
        super();
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
        }
    }

    protected CacheManager getCacheManager() {
        return cacheManager;
    }

    protected void notifyCacheUpdate(String cacheName, String cacheKey) {
        String method = "notifyRedis";
        if (StringUtils.isNotEmpty(cacheKey) && StringUtils.isNotEmpty(cacheKey)) {
            String key = String.format("%s/%s/%s", hostName, cacheName, cacheKey);
            logInfo(method, "key: %s", key);
            for (int i = 0; i < 10; i++) {
                try {
                    getRedis().opsForValue().increment(key, 1);
                    break;
                } catch (Exception e) {
                    logError(method, e);
                    try {
                        logInfo(method, "retrying in 3 seconds ...");
                        Thread.sleep(3000);
                    } catch (Exception x) {
                    }
                }
            }
        } else {
            logError(method, "cacheName or cacheKey is empty");
        }
    }
}
