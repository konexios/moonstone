package com.arrow.kronos.web.model;

import java.time.Instant;
import java.util.Map;

import com.arrow.kronos.data.DeviceStateTrans;
import com.arrow.kronos.data.DeviceStateTrans.Status;
import com.arrow.kronos.data.DeviceStateValue;
import com.arrow.pegasus.webapi.data.CoreDocumentModel;

public class DeviceStateTransModels {
	public static class DeviceStateTransModel extends CoreDocumentModel {
		private static final long serialVersionUID = 1985712862762613759L;

		private String type;
		private String status;
		private Instant createdDate;
		private Instant lastModifiedDate;
		private String error;
		
		public DeviceStateTransModel(DeviceStateTrans deviceStateTrans) {
			super(deviceStateTrans.getId(), deviceStateTrans.getHid());

			this.type = deviceStateTrans.getType().name();
			this.status = deviceStateTrans.getStatus().name();
			this.createdDate = deviceStateTrans.getCreatedDate();
			this.lastModifiedDate = deviceStateTrans.getLastModifiedDate();
			if(deviceStateTrans.getStatus() == Status.ERROR) {
				this.error = deviceStateTrans.getError();
			}
		}
		
		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}
		
		public Instant getCreatedDate() {
			return createdDate;
		}

		public void setCreatedDate(Instant createdDate) {
			this.createdDate = createdDate;
		}
		
		public Instant getLastModifiedDate() {
			return lastModifiedDate;
		}

		public void setLastModifiedDate(Instant lastModifiedDate) {
			this.lastModifiedDate = lastModifiedDate;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}
		
		public String getError() {
			return error;
		}

		public void setError(String error) {
			this.error = error;
		}
	}

	public static class DeviceStateTransDetailsModel extends DeviceStateTransModel {
		private static final long serialVersionUID = -2943665730093627687L;

		private Map<String, DeviceStateValue> states;

		public DeviceStateTransDetailsModel(DeviceStateTrans deviceStateTrans) {
			super(deviceStateTrans);
			this.states = deviceStateTrans.getStates();
		}

		public Map<String, DeviceStateValue> getStates() {
			return states;
		}
	}
}
