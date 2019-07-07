package com.arrow.kronos.web.model;

import java.io.Serializable;
import java.util.List;

public class DeviceStateModels {
	public static class DeviceStateDeleteModel implements Serializable {
		private static final long serialVersionUID = -7836515806245513625L;

		private List<String> states = null;
		private boolean removeStateDefinition;

		public List<String> getStates() {
			return states;
		}

		public void setStates(List<String> statess) {
			this.states = statess;
		}

		public boolean isRemoveStateDefinition() {
			return removeStateDefinition;
		}

		public void setRemoveStateDefinition(boolean removeStateDefinition) {
			this.removeStateDefinition = removeStateDefinition;
		}
	}
}
