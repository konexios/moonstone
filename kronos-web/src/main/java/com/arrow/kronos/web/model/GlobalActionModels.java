package com.arrow.kronos.web.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.arrow.kronos.data.action.GlobalAction;
import com.arrow.kronos.data.action.GlobalActionInput;
import com.arrow.kronos.data.action.GlobalActionProperty;
import com.arrow.kronos.data.action.GlobalActionType;
import com.arrow.kronos.web.model.GlobalActionTypeModels.GlobalActionTypeOption;
import com.arrow.pegasus.webapi.data.CoreDefinitionModelOption;

public class GlobalActionModels {

	public static class GlobalActionDetailsModel extends CoreDefinitionModelOption {

		private static final long serialVersionUID = -6221877176841062481L;

		private String systemName;
		private String description;
		private boolean enabled;
		private String lastModifiedBy;
		private Instant lastModifiedDate;
		private List<GlobalActionProperty> properties = new ArrayList<>();
		private List<GlobalActionInput> input = new ArrayList<>();
		private GlobalActionTypeOption globalActionType;

		public GlobalActionDetailsModel() {
			super(null, null, null);
		}

		public GlobalActionDetailsModel(String id, String hid, String name) {
			super(id, hid, name);
		}

		public GlobalActionDetailsModel(GlobalAction globalAction) {
			super(globalAction.getId(), globalAction.getHid(), globalAction.getName());
			this.systemName = globalAction.getSystemName();
			this.description = globalAction.getDescription();
			this.enabled = globalAction.isEnabled();
			this.lastModifiedBy = globalAction.getLastModifiedBy();
			this.lastModifiedDate = globalAction.getLastModifiedDate();
			this.properties = globalAction.getProperties();
			this.input = globalAction.getInput();
		}

		public GlobalActionDetailsModel(GlobalAction globalAction, GlobalActionType globalActionType) {
			this(globalAction);
			setGlobalActionType(new GlobalActionTypeOption(globalActionType));
		}

		public String getSystemName() {
			return systemName;
		}

		public void setSystemName(String systemName) {
			this.systemName = systemName;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
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

		public void setLastModifiedBy(String lastModifiedBy) {
			this.lastModifiedBy = lastModifiedBy;
		}

		public Instant getLastModifiedDate() {
			return lastModifiedDate;
		}

		public void setLastModifiedDate(Instant lastModifiedDate) {
			this.lastModifiedDate = lastModifiedDate;
		}

		public List<GlobalActionProperty> getProperties() {
			return properties;
		}

		public void setProperties(List<GlobalActionProperty> properties) {
			this.properties = properties;
		}

		public List<GlobalActionInput> getInput() {
			return input;
		}

		public void setInput(List<GlobalActionInput> input) {
			this.input = input;
		}

		public GlobalActionTypeOption getGlobalActionType() {
			return globalActionType;
		}

		public void setGlobalActionType(GlobalActionTypeOption globalActionType) {
			this.globalActionType = globalActionType;
		}
	}
}
