package com.arrow.pegasus.service;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "com.arrow.pegasus.service.event")
public class EventServiceProperties {
    private final static long DEFAULT_CHECK_INTERVAL = 500;
    private final static long DEFAULT_EXPIRATION = 1 * 60 * 60 * 1000;

    private long checkInterval = DEFAULT_CHECK_INTERVAL;
    private long expiration = DEFAULT_EXPIRATION;

    public long getCheckInterval() {
        return checkInterval;
    }

    public void setCheckInterval(long checkInterval) {
        this.checkInterval = checkInterval;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }
}
