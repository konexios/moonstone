package com.arrow.kronos;

import java.util.List;

import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.Telemetry;
import com.arrow.kronos.data.TelemetryItem;
import com.arrow.pegasus.data.profile.Application;

import moonstone.acs.AcsLogicalException;

public class TelemetryWrapper extends Telemetry {
    private static final long serialVersionUID = 6503673152689913809L;

    private final static AcsLogicalException FATAL_EXCEPTION = new AcsLogicalException(
            "***** YOU SHOULD NOT SEE THIS EXCEPTION! *****");

    private List<TelemetryItem> items;

    public List<TelemetryItem> getItems() {
        return items;
    }

    public void setItems(List<TelemetryItem> items) {
        this.items = items;
    }

    @Override
    public Application getRefApplication() {
        throw FATAL_EXCEPTION;
    }

    @Override
    public void setRefApplication(Application refApplication) {
        throw FATAL_EXCEPTION;
    }

    @Override
    public Device getRefDevice() {
        throw FATAL_EXCEPTION;
    }

    @Override
    public void setRefDevice(Device refDevice) {
        throw FATAL_EXCEPTION;
    }

    @Override
    public List<TelemetryItem> getRefItems() {
        throw FATAL_EXCEPTION;
    }

    @Override
    public void setRefItems(List<TelemetryItem> refItems) {
        throw FATAL_EXCEPTION;
    }
}
