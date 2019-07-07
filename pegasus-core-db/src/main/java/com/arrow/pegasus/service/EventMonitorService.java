package com.arrow.pegasus.service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.acs.JsonUtils;
import com.arrow.acs.Loggable;
import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.data.event.Event;

@Service
class EventMonitorService extends BaseServiceAbstract {

    @Autowired
    private PlatformConfigService platformConfigService;

    private Timer redisTimer;
    private AtomicBoolean redisTimerRunning;
    private ConcurrentHashMap<String, EventWrapper> eventMap;

    EventMonitorService() {
        eventMap = new ConcurrentHashMap<>();
        redisTimerRunning = new AtomicBoolean(false);
        redisTimer = new Timer(true);
        redisTimer.schedule(new RedisTimerTask(), 0, CoreConstant.DEFAULT_EVENT_SERVICE_TIMER__MS);
    }

    Event waitForResult(Event event, long timeoutMs) {
        Assert.notNull(event, "null event");
        EventWrapper wrapper = new EventWrapper();
        String key = event.buildRedisKey(platformConfigService.getConfig().getZoneSystemName());
        eventMap.put(key, wrapper);
        Event result = wrapper.waitForResult(timeoutMs);
        if (result == null) {
            // if timeout, remove it from eventMap
            eventMap.remove(key);
        }
        return result;
    }

    class RedisTimerTask extends TimerTask {
        private final static long LOG_INTERVAL = 10;
        private Instant lastLog = null;

        @Override
        public void run() {
            String method = "RedisPollingTask";
            if (redisTimerRunning.compareAndSet(false, true)) {
                if (!eventMap.isEmpty()) {
                    List<String> keys = new ArrayList<>(eventMap.keySet());
                    if (lastLog == null
                            || Math.abs(Duration.between(lastLog, Instant.now()).getSeconds()) > LOG_INTERVAL) {
                        lastLog = Instant.now();
                        logDebug(method, "eventMap size: %d", keys.size());
                    }
                    for (String key : keys) {
                        if (getRedis().hasKey(key)) {
                            String json = getRedis().opsForValue().get(key);
                            Event event = JsonUtils.fromJson(json, Event.class);
                            getRedis().delete(key);
                            eventMap.get(key).setEvent(event);
                            eventMap.remove(key);
                            logDebug(method, "found event: %s", event.getId());
                        }
                    }
                }
                redisTimerRunning.set(false);
            }
        }
    }

    class EventWrapper extends Loggable {
        private Event event;
        private boolean ready;
        private final Instant timestamp;

        private EventWrapper() {
            this.timestamp = Instant.now();
            this.ready = false;
        }

        synchronized void setEvent(Event event) {
            this.event = event;
            this.ready = true;
            this.notifyAll();
        }

        Instant getTimestamp() {
            return timestamp;
        }

        synchronized Event waitForResult(long timeoutMs) {
            if (!ready) {
                try {
                    this.wait(timeoutMs);
                } catch (Exception e) {
                }
            }
            return event;
        }
    }
}
