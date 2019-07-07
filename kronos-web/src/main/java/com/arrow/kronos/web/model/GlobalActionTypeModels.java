package com.arrow.kronos.web.model;

import java.time.Instant;
import java.util.List;

import com.arrow.kronos.data.action.GlobalActionType;
import com.arrow.kronos.data.action.GlobalActionTypeParameter;
import com.arrow.pegasus.webapi.data.CoreDefinitionModelOption;

public class GlobalActionTypeModels {

	public static class GlobalActionTypeOption extends CoreDefinitionModelOption {

		private static final long serialVersionUID = -1606866399109811350L;

		private String systemName;
		private boolean enabled;

		public GlobalActionTypeOption() {
			super(null, null, null);
		}

		public GlobalActionTypeOption(GlobalActionType globalActionType) {
			super(globalActionType.getId(), globalActionType.getHid(), globalActionType.getName());
			this.systemName = globalActionType.getSystemName();
			this.enabled = globalActionType.isEnabled();
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
	}

	public static class GlobalActionTypeDetailsModel extends GlobalActionTypeOption {

		private static final long serialVersionUID = 3527924674413883181L;

		private String description;
		private boolean editable;
		private String lastModifiedBy;
		private Instant lastModifiedDate;
		private List<GlobalActionTypeParameter> parameters;

		public GlobalActionTypeDetailsModel() {
			super();
		}

		public GlobalActionTypeDetailsModel(GlobalActionType globalActionType) {
			super(globalActionType);
			this.description = globalActionType.getDescription();
			this.editable = globalActionType.isEditable();
			this.lastModifiedBy = globalActionType.getLastModifiedBy();
			this.lastModifiedDate = globalActionType.getLastModifiedDate();
			this.parameters = globalActionType.getParameters();
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
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

		public boolean isEditable() {
			return editable;
		}

		public void setEditable(boolean editable) {
			this.editable = editable;
		}

		public List<GlobalActionTypeParameter> getParameters() {
			return parameters;
		}

		public void setParameters(List<GlobalActionTypeParameter> parameters) {
			this.parameters = parameters;
		}
	}
}
