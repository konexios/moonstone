package com.arrow.kronos.web.model;

import java.time.Instant;
import java.util.Map;

import com.arrow.kronos.data.DeviceActionType;
import com.arrow.pegasus.webapi.data.CoreDefinitionModelOption;

public class DeviceActionTypeModels {

    public static class DeviceActionTypeOption extends CoreDefinitionModelOption {
        private static final long serialVersionUID = 2945645394792502428L;

        private String systemName;
        private boolean enabled;
        private Map<String, String> parameters;

        public DeviceActionTypeOption() {
            super(null, null, null);
        }

        public DeviceActionTypeOption(DeviceActionType deviceActionType) {
            super(deviceActionType.getId(), deviceActionType.getHid(), deviceActionType.getName());
            this.systemName = deviceActionType.getSystemName();
            this.enabled = deviceActionType.isEnabled();
            this.parameters = deviceActionType.getParameters();
        }

        public String getSystemName() {
            return systemName;
        }

        public void setSystemName(String systemName) {
            this.systemName = systemName;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public Map<String, String> getParameters() {
            return parameters;
        }

        public void setParameters(Map<String, String> parameters) {
            this.parameters = parameters;
        }

    }

    public static class DeviceActionTypeDetailsModel extends DeviceActionTypeOption {
        private static final long serialVersionUID = 6007802512361650850L;

        private String description;
        private boolean editable;
        private String lastModifiedBy;
        private Instant lastModifiedDate;

        public DeviceActionTypeDetailsModel() {
            super();
        }

        public DeviceActionTypeDetailsModel(DeviceActionType deviceActionType) {
            super(deviceActionType);

            this.description = deviceActionType.getDescription();
            this.editable = deviceActionType.isEditable();
            this.lastModifiedBy = deviceActionType.getLastModifiedBy();
            this.lastModifiedDate = deviceActionType.getLastModifiedDate();
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public boolean isEditable() {
            return editable;
        }

        public void setEditable(boolean editable) {
            this.editable = editable;
        }

        public String getLastModifiedBy() {
            return lastModifiedBy;
        }

        public void setLastModifiedBy(String lastModifiedBy) {
            this.lastModifiedBy = lastModifiedBy;
        }

        public Instant getLastModifiedDate(){
            return lastModifiedDate;
        }
    }
}
