package com.arrow.kronos.web.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.arrow.kronos.data.DeviceAction;
import com.arrow.kronos.data.DeviceActionType;

public class DeviceActionModels {

    public static class DeviceActionList implements Serializable {
        private static final long serialVersionUID = -6546366724681119281L;

        private int index;
        private String deviceActionTypeName;
        private String description;
        private boolean enabled;

        public DeviceActionList(int index, DeviceAction deviceAction, DeviceActionType deviceActionType) {
            this.index = index;
            this.deviceActionTypeName = deviceActionType.getName();
            this.description = deviceAction.getDescription();
            this.enabled = deviceAction.isEnabled();
        }

        public int getIndex() {
            return index;
        }

        public String getDeviceActionTypeName() {
            return deviceActionTypeName;
        }

        public String getDescription() {
            return description;
        }

        public boolean isEnabled() {
            return enabled;
        }
    }

    public static class DeviceActionModel implements Serializable {
        private static final long serialVersionUID = 4051297356196557594L;

        private String deviceActionTypeId;
        private String description;
        private String criteria;
        // if false - expect criteria is not empty, if true - special case for No Telemetry Enhancement
        private boolean noTelemetry;
        private long noTelemetryTime;
        private long expiration;
        private boolean enabled;
        private Map<String, String> parameters = new HashMap<>();

        public DeviceActionModel() {

        }

        public DeviceActionModel(DeviceAction deviceAction) {
            this.deviceActionTypeId = deviceAction.getDeviceActionTypeId();
            this.description = deviceAction.getDescription();
            this.criteria = deviceAction.getCriteria();
            this.noTelemetry = deviceAction.isNoTelemetry();
            this.noTelemetryTime = deviceAction.getNoTelemetryTime();
            this.expiration = deviceAction.getExpiration();
            this.enabled = deviceAction.isEnabled();
            this.parameters = deviceAction.getParameters();
        }

        public String getDeviceActionTypeId() {
            return deviceActionTypeId;
        }

        public String getDescription() {
            return description;
        }

        public String getCriteria() {
            return criteria;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public Map<String, String> getParameters() {
            return parameters;
        }

        public boolean isNoTelemetry() {
			return noTelemetry;
		}

		public long getNoTelemetryTime() {
			return noTelemetryTime;
		}

		public void setDeviceActionTypeId(String deviceActionTypeId) {
            this.deviceActionTypeId = deviceActionTypeId;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setCriteria(String criteria) {
            this.criteria = criteria;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public void setParameters(Map<String, String> parameters) {
            this.parameters = parameters;
        }

        public long getExpiration() {
            return expiration;
        }

        public void setExpiration(long expiration) {
            this.expiration = expiration;
        }

        public void setNoTelemetry(boolean noTelemetry) {
			this.noTelemetry = noTelemetry;
		}

		public void setNoTelemetryTime(long noTelemetryTime) {
			this.noTelemetryTime = noTelemetryTime;
		}

		public DeviceAction populate() {
            DeviceAction deviceAction = new DeviceAction();
            deviceAction.setDeviceActionTypeId(this.getDeviceActionTypeId());
            return this.populate(deviceAction);
        }

        public DeviceAction populate(DeviceAction deviceAction) {
            deviceAction.setDescription(this.getDescription());
            deviceAction.setCriteria(this.getCriteria());
            deviceAction.setNoTelemetry(this.isNoTelemetry());
            deviceAction.setNoTelemetryTime(this.getNoTelemetryTime());
            deviceAction.setExpiration(this.getExpiration());
            deviceAction.setParameters(this.getParameters());
            deviceAction.setEnabled(this.isEnabled());

            return deviceAction;
        }
    }

    public static class DeviceActionUpsert implements Serializable {
        private static final long serialVersionUID = -2575221673302886072L;

        private DeviceActionModel deviceAction;
        private List<DeviceActionTypeModels.DeviceActionTypeOption> deviceActionTypeOptions;

        public DeviceActionUpsert(DeviceActionModel deviceAction,
                List<DeviceActionTypeModels.DeviceActionTypeOption> deviceActionTypeOptions) {
            this.deviceAction = deviceAction;
            this.deviceActionTypeOptions = deviceActionTypeOptions;
        }

        public DeviceActionModel getDeviceAction() {
            return deviceAction;
        }

        public List<DeviceActionTypeModels.DeviceActionTypeOption> getDeviceActionTypeOptions() {
            return deviceActionTypeOptions;
        }
    }
}
