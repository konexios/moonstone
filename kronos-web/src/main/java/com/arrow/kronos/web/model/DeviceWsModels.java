package com.arrow.kronos.web.model;

import java.io.Serializable;

public class DeviceWsModels {
	
	public static class DeviceTelemetryOptions implements Serializable {

		private static final long serialVersionUID = -7958192762312909851L;

		private int refreshInterval;

		public int getRefreshInterval() {
			return refreshInterval;
		}

		public void setRefreshInterval(int refreshInterval) {
			this.refreshInterval = refreshInterval;
		}

		@Override
		public String toString() {
			return "DeviceTelemetryOptions [refreshInterval=" + refreshInterval + "]";
		}
	}
}
