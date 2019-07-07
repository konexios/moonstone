package com.arrow.kronos.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arrow.kronos.KronosEngineConstants;
import com.arrow.kronos.TelemetryWrapper;
import com.arrow.kronos.data.TelemetryItem;
import com.arrow.kronos.service.LastTelemetryItemService;

@Component
public class LastTelemetryItemProcessor extends KafkaTelemetryProcessorAbstract {

    @Autowired
    private LastTelemetryItemService lastTelemetryItemService;

    public LastTelemetryItemProcessor() {
        super(KronosEngineConstants.KafkaTelemetryProcessor.LAST_TELEMETRY_ITEM);
    }

    @Override
    protected void doProcessTelemetry(TelemetryWrapper wrapper) {
        String method = "doProcessTelemetry";
        for (TelemetryItem item : wrapper.getItems()) {
            try {
                lastTelemetryItemService.update2(item);
            } catch (Throwable t) {
                logError(method, t);
            }
        }
        logInfo(method, "completed %s items", wrapper.getItems().size());
    }
}
