package com.arrow.kronos.processor;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.arrow.kronos.KronosConstants;
import com.arrow.kronos.KronosEngineConstants;
import com.arrow.kronos.TelemetryWrapper;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.TelemetryItem;
import com.arrow.pegasus.data.location.LastLocationType;
import com.arrow.pegasus.data.location.LocationObjectType;
import com.arrow.pegasus.service.LastLocationService;

@Component
public class LocationProcessor extends KafkaTelemetryProcessorAbstract {

    @Autowired
    private LastLocationService lastLocationService;

    public LocationProcessor() {
        super(KronosEngineConstants.KafkaTelemetryProcessor.LOCATION);
    }

    protected void doProcessTelemetry(TelemetryWrapper wrapper) {
        String method = "doProcessTelemetry";
        Double latitude = null;
        Double longitude = null;
        try {
            for (TelemetryItem item : wrapper.getItems()) {
                if (item.getName().equals(KronosConstants.Telemetry.LATITUDE)) {
                    latitude = item.getFloatValue();
                } else if (item.getName().equals(KronosConstants.Telemetry.LONGITUDE)) {
                    longitude = item.getFloatValue();
                }
            }
            if (latitude != null && longitude != null) {
                TelemetryItem firstItem = wrapper.getItems().get(0);
                lastLocationService.update(LocationObjectType.DEVICE, firstItem.getDeviceId(), latitude, longitude,
                        LastLocationType.DYNAMIC, Instant.ofEpochMilli(firstItem.getTimestamp()));
                Device device = getContext().getKronosCache().findDeviceById(wrapper.getDeviceId());
                Assert.notNull(device, "device not found: " + wrapper.getDeviceId());
                logInfo(method, "updated last location for device: %s", device.getName());
            }
        } catch (Exception e) {
            logError(method, "Erorr processing location information", e);
        }
    }
}
