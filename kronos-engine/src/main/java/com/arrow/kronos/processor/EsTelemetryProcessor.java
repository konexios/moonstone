package com.arrow.kronos.processor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arrow.kronos.KronosEngineConstants;
import com.arrow.kronos.TelemetryWrapper;
import com.arrow.kronos.data.EsTelemetryItem;
import com.arrow.kronos.data.TelemetryItem;
import com.arrow.kronos.service.SpringDataEsTelemetryItemService;

@Component
public class EsTelemetryProcessor extends KafkaTelemetryProcessorAbstract {

    @Autowired
    private SpringDataEsTelemetryItemService itemService;

    private long flushQueueIntervalMs;
    private long maxQueueSize;

    private Timer indexTimer;
    private LinkedList<EsTelemetryItem> queue = new LinkedList<>();

    public EsTelemetryProcessor() {
        super(KronosEngineConstants.KafkaTelemetryProcessor.ES_TELEMETRY);
    }

    @Override
    protected void postConstruct() {
        String method = "postContruct";
        super.postConstruct();

        flushQueueIntervalMs = Long.valueOf(getEnvProperty("flushQueueIntervalMs", "1000"));
        maxQueueSize = Long.valueOf(getEnvProperty("maxQueueSize", "1000"));
        logInfo(method, "flushQueueIntervalMs: %d, maxQueueSize: %d", flushQueueIntervalMs, maxQueueSize);

        indexTimer = new Timer();
        indexTimer.scheduleAtFixedRate(new IndexTask(), 0, flushQueueIntervalMs);
    }

    @Override
    public void preDestroy() {
        super.preDestroy();
        if (indexTimer != null)
            indexTimer.cancel();
    }

    @Override
    protected void doProcessTelemetry(TelemetryWrapper wrapper) {
        String method = "doProcessTelemetry";
        try {
            List<EsTelemetryItem> list = new ArrayList<>();
            for (TelemetryItem item : wrapper.getItems()) {
                EsTelemetryItem esItem = new EsTelemetryItem();
                esItem.setDeviceId(item.getDeviceId());
                esItem.setApplicationId(item.getApplicationId());
                esItem.setName(item.getName());
                esItem.setType(item.getType());
                esItem.setTimestamp(item.getTimestamp());
                esItem.setStrValue(item.getStrValue());
                esItem.setIntValue(item.getIntValue());

                Double floatValue = item.getFloatValue();
                if (floatValue != null) {
                    if (Double.isInfinite(floatValue) || Double.isNaN(floatValue)) {
                        logWarn(method, "IGNORED NaN value for telemetry time: %s", item.getName());
                        esItem.setFloatValue(0.0);
                    } else {
                        esItem.setFloatValue(floatValue);
                    }
                }

                esItem.setBoolValue(item.getBoolValue());
                esItem.setDateValue(item.getDateValue());
                esItem.setDatetimeValue(item.getDatetimeValue());
                esItem.setIntSqrValue(item.getIntSqrValue());
                esItem.setFloatSqrValue(item.getFloatSqrValue());
                esItem.setIntCubeValue(item.getIntCubeValue());
                esItem.setFloatCubeValue(item.getFloatCubeValue());
                list.add(esItem);
            }
            enqueue(list);
        } catch (Exception e) {
            logError(method, "ERROR processing ElasticSearch", e);
        }
    }

    private synchronized void enqueue(List<EsTelemetryItem> items) {
        queue.addAll(items);
        if (queue.size() > maxQueueSize) {
            flushQueue();
        }
    }

    private synchronized void flushQueue() {
        String method = "flush";
        if (!queue.isEmpty()) {
            itemService.getItemRepository().bulkIndex(queue);
            logInfo(method, "indexed %d items", queue.size());
            queue.clear();
        }
    }

    private class IndexTask extends TimerTask {
        private AtomicBoolean running = new AtomicBoolean(false);

        @Override
        public void run() {
            String method = "IndexTask.run";
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
