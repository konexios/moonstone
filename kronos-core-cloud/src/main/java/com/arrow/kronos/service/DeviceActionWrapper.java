package com.arrow.kronos.service;

import com.arrow.kronos.data.DeviceAction;

public class DeviceActionWrapper extends DeviceAction {

    private static final long serialVersionUID = 1048113083943236071L;

    private int index;
    private String ownerId; // deviceId | deviceTypeId | nodeId

    public DeviceActionWrapper() {
    }

    public DeviceActionWrapper(int index, String ownerId, DeviceAction deviceAction) {
        withIndex(index);
        withOwnerId(ownerId);
        withDeviceAction(deviceAction);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public DeviceActionWrapper withIndex(int index) {
        setIndex(index);
        return this;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public DeviceActionWrapper withOwnerId(String ownerId) {
        setOwnerId(ownerId);
        return this;
    }

    public DeviceActionWrapper withDeviceAction(DeviceAction deviceAction) {
        setCriteria(deviceAction.getCriteria());
        setDescription(deviceAction.getDescription());
        setDeviceActionTypeId(deviceAction.getDeviceActionTypeId());
        setEnabled(deviceAction.isEnabled());
        setExpiration(deviceAction.getExpiration());
        setNoTelemetry(deviceAction.isNoTelemetry());
        setNoTelemetryTime(deviceAction.getNoTelemetryTime());
        setParameters(deviceAction.getParameters());
        setRefDeviceActionType(deviceAction.getRefDeviceActionType());
        return this;
    }

}
