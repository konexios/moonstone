package com.arrow.apollo.web.model;

import java.io.Serializable;

public class DeviceModels {

	public static class DeviceOptionModel implements Serializable {
		private static final long serialVersionUID = -4223473061818434848L;

		private String uid;
		private String name;

		public String getUid() {
			return uid;
		}

		public void setUid(String uid) {
			this.uid = uid;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public DeviceOptionModel withUid(String uid) {
			setUid(uid);

			return this;
		}

		public DeviceOptionModel withName(String name) {
			setName(name);

			return this;
		}
	}

}
