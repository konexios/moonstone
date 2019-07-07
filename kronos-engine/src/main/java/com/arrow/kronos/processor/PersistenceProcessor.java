package com.arrow.kronos.processor;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.arrow.kronos.KronosEngineConstants;
import com.arrow.kronos.TelemetryWrapper;
import com.arrow.kronos.data.TelemetryItem;
import com.arrow.pegasus.CoreConstant;

@Component
public class PersistenceProcessor extends KafkaTelemetryProcessorAbstract {

    private Timer persistTimer;
    private LinkedList<TelemetryItem> queue = new LinkedList<>();

    private long flushQueueIntervalMs;
    private long maxQueueSize;

    public PersistenceProcessor() {
        super(KronosEngineConstants.KafkaTelemetryProcessor.PERSISTENCE);
    }

    @Override
    protected void postConstruct() {
        String method = "postConstruct";
        super.postConstruct();

        flushQueueIntervalMs = Long.valueOf(getEnvProperty("flushQueueIntervalMs", "1000"));
        maxQueueSize = Long.valueOf(getEnvProperty("maxQueueSize", "1000"));
        logInfo(method, "flushQueueIntervalMs: %d, maxQueueSize: %d", flushQueueIntervalMs, maxQueueSize);

        persistTimer = new Timer();
        persistTimer.scheduleAtFixedRate(new PersistenceTask(), 0, flushQueueIntervalMs);
    }

    @Override
    public void preDestroy() {
        super.preDestroy();
        if (persistTimer != null)
            persistTimer.cancel();
    }

    @Override
    protected void doProcessTelemetry(TelemetryWrapper wrapper) {
        String method = "doProcessTelemetry";
        if (StringUtils.isEmpty(wrapper.getId())) {
            logWarn(method, "wrapper was not persisted, must be old data!");
        } else {
            enqueue(wrapper.getItems());
        }
    }

    private synchronized void enqueue(List<TelemetryItem> items) {
        queue.addAll(items);
        if (queue.size() > maxQueueSize) {
            flushQueue();
        }
    }

    private synchronized void flushQueue() {
        String method = "flush";
        if (!queue.isEmpty()) {
            logInfo(method, "persisting %d items ...", queue.size());
            getContext().getTelemetryItemService().getTelemetryItemRepository().doInsert(queue,
                    CoreConstant.ADMIN_USER);
            logInfo(method, "persisted %d items", queue.size());
            queue.clear();
        }
    }

    private class PersistenceTask extends TimerTask {
        private AtomicBoolean running = new AtomicBoolean(false);

        @Override
        public void run() {
            String method = "PersistenceTask.run";
            if (running.compareAndSet(false, true)) {
                try {
                    flushQueue();
                } catch (Throwable e) {
                    logError(method, e);
                } finally {
                    running.set(false);
                }
            }
        }
    }
}
