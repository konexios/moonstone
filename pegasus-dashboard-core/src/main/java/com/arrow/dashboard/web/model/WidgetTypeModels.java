package com.arrow.dashboard.web.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class WidgetTypeModels {

	public static class GetWidgetTypesResponse implements Serializable {
		private static final long serialVersionUID = 6197037045852290231L;

		private String requestId;
		private List<WidgetTypeModels.ReadWidgetTypeModel> widgetTypes = new ArrayList<>();

		public GetWidgetTypesResponse() {
		}

		public String getRequestId() {
			return requestId;
		}

		public void setRequestId(String requestId) {
			this.requestId = requestId;
		}

		public List<WidgetTypeModels.ReadWidgetTypeModel> getWidgetTypes() {
			return widgetTypes;
		}

		public void setWidgetTypes(List<WidgetTypeModels.ReadWidgetTypeModel> widgets) {
			this.widgetTypes = widgets;
		}

		public GetWidgetTypesResponse withRequestId(String requestId) {
			setRequestId(requestId);

			return this;
		}

		public GetWidgetTypesResponse withWidgets(List<WidgetTypeModels.ReadWidgetTypeModel> widgetTypes) {
			setWidgetTypes(widgetTypes);

			return this;
		}
	}

	public static class ReadWidgetTypeModel implements Serializable {
		private static final long serialVersionUID = -2522956798593592883L;

		private String id;
		private Instant createdDate;
		private String createdBy;
		private Instant lastModifiedDate;
		private String lastModifiedBy;
		private String name;
		private String description;
		private boolean enabled;
		private String className;
		private String directive;

		public ReadWidgetTypeModel() {
			super();
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public Instant getCreatedDate() {
			return createdDate;
		}

		public void setCreatedDate(Instant createdDate) {
			this.createdDate = createdDate;
		}

		public String getCreatedBy() {
			return createdBy;
		}

		public void setCreatedBy(String createdBy) {
			this.createdBy = createdBy;
		}

		public Instant getLastModifiedDate() {
			return lastModifiedDate;
		}

		public void setLastModifiedDate(Instant lastModifiedDate) {
			this.lastModifiedDate = lastModifiedDate;
		}

		public String getLastModifiedBy() {
			return lastModifiedBy;
		}

		public void setLastModifiedBy(String lastModifiedBy) {
			this.lastModifiedBy = lastModifiedBy;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
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

		public String getClassName() {
			return className;
		}

		public void setClassName(String className) {
			this.className = className;
		}

		public String getDirective() {
			return directive;
		}

		public void setDirective(String directive) {
			this.directive = directive;
		}
	}
}
