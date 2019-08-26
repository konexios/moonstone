package com.arrow.pegasus;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.session.data.redis.config.ConfigureRedisAction;

import moonstone.acs.Loggable;

public class ConfigureAllRedisAction extends Loggable implements ConfigureRedisAction {
    static final String CONFIG_NOTIFY_KEYSPACE_EVENTS = "notify-keyspace-events";
    static final String ALL_EVENTS = "KEA";

    @Override
    public void configure(RedisConnection connection) {
        String method = "configure";
        try {
            logInfo(method, "setting redis config: %s to: %s", CONFIG_NOTIFY_KEYSPACE_EVENTS, ALL_EVENTS);
            connection.setConfig(CONFIG_NOTIFY_KEYSPACE_EVENTS, ALL_EVENTS);
        } catch (Exception e) {
            logError(method, e);
        }
    }
}
