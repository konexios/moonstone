package com.arrow.kronos.web.model;

import java.io.Serializable;
import java.time.Instant;

import com.arrow.kronos.data.NodeType;
import com.arrow.pegasus.webapi.data.CoreDefinitionModelOption;
import com.arrow.rhea.data.DeviceCategory;

public class NodeTypeModels {
	public static class NodeTypeOption extends CoreDefinitionModelOption {
		private static final long serialVersionUID = -2763916024297274366L;

		public NodeTypeOption() {
			super(null, null, null);
		}

		public NodeTypeOption(NodeType nodeType) {
			super(nodeType.getId(), nodeType.getHid(), nodeType.getName());
		}
	}

	public static class NodeTypeModel extends NodeTypeOption {
		private static final long serialVersionUID = 7845405794901893047L;

		private String description;
		// private DeviceCategoryModel category;
		private boolean enabled;
		private String lastModifiedBy;
		private Instant lastModifiedDate;

		public NodeTypeModel() {
			super();
			this.enabled = true;
		}

		public NodeTypeModel(NodeType nodeType) {
			super(nodeType);

			this.description = nodeType.getDescription();
			this.enabled = nodeType.isEnabled();
			this.lastModifiedBy = nodeType.getLastModifiedBy();
			this.lastModifiedDate = nodeType.getLastModifiedDate();
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		// public DeviceCategoryModel getCategory() {
		// return category;
		// }
		//
		// public void setCategory(DeviceCategoryModel category) {
		// this.category = category;
		// }
		//
		// public NodeTypeModel withCategory(DeviceCategoryModel category) {
		// setCategory(category);
		// return this;
		// }

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

		public Instant getLastModifiedDate(){
			return lastModifiedDate;
		}
	}

	public static class DeviceCategoryModel implements Serializable {
		private static final long serialVersionUID = 706985335900110614L;

		private String name;
		private String id;

		public DeviceCategoryModel() {
		}

		public DeviceCategoryModel(DeviceCategory category) {
			this.name = category.getName();
			this.id = category.getId();
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}
	}
}
