package com.arrow.pegasus;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.session.data.redis.config.ConfigureNotifyKeyspaceEventsAction;

import com.arrow.acs.Loggable;

public class CustomRedisNotifyKeyspaceEvents extends ConfigureNotifyKeyspaceEventsAction {
    private Loggable logger = new Loggable(getClass().getName()) {
    };

    @Override
    public void configure(RedisConnection connection) {
        String method = "configure";
        try {
            super.configure(connection);
        } catch (IllegalStateException e) {
            logger.logError(method, "POSSIBLE SECURED REDIS CONFIG? - %s", e.getMessage());
        }
    }
}
