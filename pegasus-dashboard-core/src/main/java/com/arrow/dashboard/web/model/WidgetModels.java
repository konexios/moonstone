package com.arrow.dashboard.web.model;

import java.io.Serializable;
import java.time.Instant;

public class WidgetModels {

	public abstract static class BaseWidgetModelAbstract<T extends BaseWidgetModelAbstract<T>> implements Serializable {
		private static final long serialVersionUID = -7847465307051631179L;

		private String name;
		private String description;
		private String parentId;
		private String widgetTypeId;
		private String layout;

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

		public String getParentId() {
			return parentId;
		}

		public void setParentId(String parentId) {
			this.parentId = parentId;
		}

		public String getWidgetTypeId() {
			return widgetTypeId;
		}

		public void setWidgetTypeId(String widgetTypeId) {
			this.widgetTypeId = widgetTypeId;
		}

		public String getLayout() {
			return layout;
		}

		public void setLayout(String layout) {
			this.layout = layout;
		}

		protected abstract T self();

		public T withName(String name) {
			setName(name);

			return self();
		}

		public T withDescription(String description) {
			setDescription(description);

			return self();
		}

		public T withParentId(String parentId) {
			setParentId(parentId);

			return self();
		}

		public T withWidgetTypeId(String widgetTypeId) {
			setWidgetTypeId(widgetTypeId);

			return self();
		}

		public T withLayoutId(String layout) {
			setLayout(layout);

			return self();
		}
	}

	public static class CreateWidgetModel extends BaseWidgetModelAbstract<CreateWidgetModel> {
		private static final long serialVersionUID = -2349540491150383L;

		@Override
		protected CreateWidgetModel self() {
			return this;
		};
	}

	public static class UpdateWidgetModel extends BaseWidgetModelAbstract<UpdateWidgetModel> {
		private static final long serialVersionUID = 2624619327670215767L;

		private String id;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		@Override
		protected UpdateWidgetModel self() {
			return this;
		};

		public UpdateWidgetModel withId(String id) {
			setId(id);

			return self();
		}
	}

	public static class ReadWidgetModel extends BaseWidgetModelAbstract<ReadWidgetModel> {
		private static final long serialVersionUID = -7562171899264613335L;

		private String id;
		private Instant createdDate;
		private String createdBy;
		private Instant lastModifiedDate;
		private String lastModifiedBy;
		private String parentName;
		private String widgetTypeName;

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

		public String getParentName() {
			return parentName;
		}

		public void setParentName(String parentName) {
			this.parentName = parentName;
		}

		public String getWidgetTypeName() {
			return widgetTypeName;
		}

		public void setWidgetTypeName(String widgetTypeName) {
			this.widgetTypeName = widgetTypeName;
		}

		@Override
		protected ReadWidgetModel self() {
			return this;
		};

		public ReadWidgetModel withId(String id) {
			setId(id);

			return self();
		}

		public ReadWidgetModel withCreatedDate(Instant createdDate) {
			setCreatedDate(createdDate);

			return self();
		}

		public ReadWidgetModel withCreatedBy(String createdBy) {
			setCreatedBy(createdBy);

			return self();
		}

		public ReadWidgetModel withLastModifiedDate(Instant lastModifiedDate) {
			setLastModifiedDate(lastModifiedDate);

			return self();
		}

		public ReadWidgetModel withLastModifiedBy(String lastModifiedBy) {
			setLastModifiedBy(lastModifiedBy);

			return self();
		}

		public ReadWidgetModel withParentName(String parentName) {
			setParentName(parentName);

			return self();
		}

		public ReadWidgetModel withWidgetTypeName(String widgetTypeName) {
			setWidgetTypeName(widgetTypeName);

			return self();
		}
	}

	public static class CreateWidgetRequest implements Serializable {
		private static final long serialVersionUID = -391863072168934596L;

		private CreateWidgetModel widget;

		public CreateWidgetModel getWidget() {
			return widget;
		}

		public void setWidget(CreateWidgetModel widget) {
			this.widget = widget;
		}
	}

	public static class CreateWidgetResponse implements Serializable {
		private static final long serialVersionUID = -5908432822049124513L;

		private ReadWidgetModel widget;

		public ReadWidgetModel getWidget() {
			return widget;
		}

		public void setWidget(ReadWidgetModel widget) {
			this.widget = widget;
		}

		public CreateWidgetResponse withWidget(ReadWidgetModel widget) {
			setWidget(widget);

			return this;
		}
	}

	public static class UpdateWidgetRequest implements Serializable {
		private static final long serialVersionUID = 1866610621573363672L;

		private UpdateWidgetModel widget;

		public UpdateWidgetModel getWidget() {
			return widget;
		}

		public void setWidget(UpdateWidgetModel widget) {
			this.widget = widget;
		}
	}

	public static class UpdateWidgetResponse implements Serializable {
		private static final long serialVersionUID = -7395883988673290685L;

		private ReadWidgetModel widget;

		public ReadWidgetModel getWidget() {
			return widget;
		}

		public void setWidget(ReadWidgetModel widget) {
			this.widget = widget;
		}

		public UpdateWidgetResponse withWidget(ReadWidgetModel widget) {
			setWidget(widget);

			return this;
		}
	}

	public static class DeleteWidgetResponse implements Serializable {
		private static final long serialVersionUID = 8362193864324962503L;

	}
}