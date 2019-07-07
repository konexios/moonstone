package com.arrow.pegasus.webapi.data;

import java.io.Serializable;

public class DeviceTagModels {

	public static class DeviceTagOption implements Serializable {
		private static final long serialVersionUID = 8423879907707723443L;

		private String id;
		private String name;

		public DeviceTagOption(String deviceTag) {
			this.id = deviceTag;
			this.name = deviceTag;
		}

		public String getId() {
			return id;
		}

		public String getName() {
			return name;
		}
	}
}
