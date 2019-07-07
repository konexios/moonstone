package com.arrow.kronos.web.model;

import java.io.Serializable;

import com.arrow.pegasus.data.heartbeat.HeartbeatAbstract;

public class HeartbeatModels {
    public static class HeartbeatModel implements Serializable {
        private static final long serialVersionUID = -7003553782722182663L;

        private Long timestamp;

        public HeartbeatModel(HeartbeatAbstract heartbeat) {
            this.timestamp = heartbeat != null ? heartbeat.getTimestamp() : null;
        }

        public Long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Long timestamp) {
            this.timestamp = timestamp;
        }

    }
}
