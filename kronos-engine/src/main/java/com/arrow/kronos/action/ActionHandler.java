package com.arrow.kronos.action;

import com.arrow.kronos.TelemetryWrapper;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.DeviceAction;
import com.arrow.kronos.data.DeviceEvent;

public interface ActionHandler {
    public void handle(DeviceEvent deviceEvent, TelemetryWrapper wrapper, Device device, DeviceAction action);
}