package com.arrow.kronos.web.model;

import java.time.Instant;

import com.arrow.kronos.data.TelemetryUnit;
import com.arrow.pegasus.webapi.data.CoreDefinitionModelOption;

public class TelemetryUnitModels {
    public static class TelemetryUnitOption extends CoreDefinitionModelOption {
        private static final long serialVersionUID = 8410411647336577005L;

        public TelemetryUnitOption() {
            super(null, null, null);
        }

        public TelemetryUnitOption(TelemetryUnit telemetryUnit) {
            super(telemetryUnit.getId(), telemetryUnit.getHid(), telemetryUnit.getName());
        }
    }

    public static class TelemetryUnitModel extends TelemetryUnitOption {
        private static final long serialVersionUID = -5790203554705274831L;

        private String description;
        private String systemName;
        private boolean enabled;
        private String lastModifiedBy;
        private Instant lastModifiedDate;

        public TelemetryUnitModel() {
            super();
        }

        public TelemetryUnitModel(TelemetryUnit telemetryUnit) {
            super(telemetryUnit);

            this.description = telemetryUnit.getDescription();
            this.systemName = telemetryUnit.getSystemName();
            this.enabled = telemetryUnit.isEnabled();
            this.lastModifiedBy = telemetryUnit.getLastModifiedBy();
            this.lastModifiedDate = telemetryUnit.getLastModifiedDate();
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
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

        public String getLastModifiedBy() {
            return lastModifiedBy;
        }

        public Instant getLastModifiedDate(){
            return lastModifiedDate;
        }

        public void setLastModifiedBy(String lastModifiedBy) {
            this.lastModifiedBy = lastModifiedBy;
        }
    }
}
