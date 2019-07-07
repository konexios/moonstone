package com.arrow.pegasus.web.model;

import java.io.Serializable;
import java.util.List;

public class CacheModels {

	public static class CacheGroup implements Serializable {
		private static final long serialVersionUID = 5746965654984007901L;

		private String groupName;
		private List<String> keys;

		public CacheGroup(String groupName, List<String> keys) {
			this.groupName = groupName;
			this.keys = keys;
		}

		public String getGroupName() {
			return groupName;
		}

		public List<String> getKeys() {
			return keys;
		}
	}
}