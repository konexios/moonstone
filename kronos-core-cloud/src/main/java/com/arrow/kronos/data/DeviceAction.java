package com.arrow.kronos.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Transient;

import com.arrow.kronos.KronosConstants;
import com.arrow.pegasus.CoreConstant;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class DeviceAction implements Serializable {
    private static final long serialVersionUID = 8378619416673255006L;

    @NotBlank
    private String deviceActionTypeId;
    private String description;
    private String criteria;
    private boolean noTelemetry;
    private long noTelemetryTime;
    private long expiration = KronosConstants.DeviceEvent.DEFAULT_EXPIRES_SECS;
    private boolean enabled = CoreConstant.DEFAULT_ENABLED;
    private Map<String, String> parameters = new HashMap<>();

    @Transient
    @JsonIgnore
    private DeviceActionType refDeviceActionType;

    /*----------------------------------------------------------------
     getters / setters
     ----------------------------------------------------------------*/

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public String getDeviceActionTypeId() {
        return deviceActionTypeId;
    }

    public void setDeviceActionTypeId(String deviceActionTypeId) {
        this.deviceActionTypeId = deviceActionTypeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public DeviceActionType getRefDeviceActionType() {
        return refDeviceActionType;
    }

    public void setRefDeviceActionType(DeviceActionType refDeviceActionType) {
        this.refDeviceActionType = refDeviceActionType;
    }

    public boolean isNoTelemetry() {
        return noTelemetry;
    }

    public void setNoTelemetry(boolean noTelemetry) {
        this.noTelemetry = noTelemetry;
    }

    public long getNoTelemetryTime() {
        return noTelemetryTime;
    }

    public void setNoTelemetryTime(long noTelemetryTime) {
        this.noTelemetryTime = noTelemetryTime;
    }
}
