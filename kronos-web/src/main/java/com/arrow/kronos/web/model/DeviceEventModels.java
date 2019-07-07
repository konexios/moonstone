package com.arrow.kronos.web.model;

import com.arrow.kronos.data.DeviceActionType;
import com.arrow.kronos.data.DeviceEvent;
import com.arrow.kronos.data.DeviceEventStatus;
import com.arrow.pegasus.webapi.data.CoreDocumentModel;

public class DeviceEventModels {

    public static class DeviceEventOption extends CoreDocumentModel {
        private static final long serialVersionUID = 166389846318503796L;

        private String deviceActionTypeName;

        public DeviceEventOption(DeviceEvent deviceEvent, DeviceActionType deviceActionType) {
            super(deviceEvent.getId(), deviceEvent.getHid());
            this.deviceActionTypeName = deviceActionType.getName();
        }

        public String getDeviceActionTypeName() {
            return deviceActionTypeName;
        }
    }

    public static class DeviceEventList extends CoreDocumentModel {
        private static final long serialVersionUID = 9075279349261779809L;

        private String deviceActionTypeName;
        private String criteria;
        private DeviceEventStatus status;
        private long createdDate;
        private int counter;

        public DeviceEventList(DeviceEvent deviceEvent, DeviceActionType deviceActionType) {
            super(deviceEvent.getId(), deviceEvent.getHid());
            this.deviceActionTypeName = deviceActionType != null ? deviceActionType.getName() : "unknown";
            this.criteria = deviceEvent.getCriteria();
            this.status = deviceEvent.getStatus();
            this.createdDate = deviceEvent.getCreatedDate().getEpochSecond();
            this.counter = deviceEvent.getCounter();
        }

        public String getDeviceActionTypeName() {
            return deviceActionTypeName;
        }

        public String getCriteria() {
            return criteria;
        }

        public DeviceEventStatus getStatus() {
            return status;
        }

        public long getCreatedDate() {
            return createdDate;
        }

        public int getCounter() {
            return counter;
        }

    }
}
