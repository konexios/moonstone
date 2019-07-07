package com.arrow.dashboard.runtime.model;

import java.io.Serializable;
import java.util.List;

public class WidgetRuntimeInstance implements Serializable {
	private static final long serialVersionUID = 178210056893497586L;

	private String widgetRuntimeId;
	private String widgetId;
	private String parentRuntimeId;
	private String parentId;
	private String name;
	private String description;
	private Object layout;
	private String widgetClassName;
	private Object instance;
	private WidgetRuntimeDefinition widgetRuntimeDefinition;
	private List<DataProviderInstance> dataProviders;
	private UserRuntimeInstance userRuntime;

	public WidgetRuntimeInstance() {
	}

	public String getWidgetRuntimeId() {
		return widgetRuntimeId;
	}

	public void setWidgetRuntimeId(String widgetRuntimeId) {
		this.widgetRuntimeId = widgetRuntimeId;
	}

	public String getWidgetId() {
		return widgetId;
	}

	public void setWidgetId(String widgetId) {
		this.widgetId = widgetId;
	}

	public String getParentRuntimeId() {
		return parentRuntimeId;
	}

	public void setParentRuntimeId(String parentRuntimeId) {
		this.parentRuntimeId = parentRuntimeId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
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

	public Object getLayout() {
		return layout;
	}

	public void setLayout(Object layout) {
		this.layout = layout;
	}

	public String getWidgetClassName() {
		return widgetClassName;
	}

	public void setWidgetClassName(String widgetClassName) {
		this.widgetClassName = widgetClassName;
	}

	public Object getInstance() {
		return instance;
	}

	public void setInstance(Object instance) {
		this.instance = instance;
	}

	public WidgetRuntimeDefinition getWidgetRuntimeDefinition() {
		return widgetRuntimeDefinition;
	}

	public void setWidgetRuntimeDefinition(WidgetRuntimeDefinition widgetRuntimeDefinition) {
		this.widgetRuntimeDefinition = widgetRuntimeDefinition;
	}

	public List<DataProviderInstance> getDataProviders() {
		return dataProviders;
	}

	public void setDataProviders(List<DataProviderInstance> dataProviders) {
		this.dataProviders = dataProviders;
	}

	public UserRuntimeInstance getUserRuntime() {
		return userRuntime;
	}

	public void setUserRuntime(UserRuntimeInstance userRuntime) {
		this.userRuntime = userRuntime;
	}

	public WidgetRuntimeInstance withWidgetRuntimeId(String widgetRuntimeId) {
		setWidgetRuntimeId(widgetRuntimeId);

		return this;
	}

	public WidgetRuntimeInstance withWidgetId(String widgetId) {
		setWidgetId(widgetId);

		return this;
	}

	public WidgetRuntimeInstance withParentRuntimeId(String parentRuntimeId) {
		setParentRuntimeId(parentRuntimeId);

		return this;
	}

	public WidgetRuntimeInstance withParentId(String parentId) {
		setParentId(parentId);

		return this;
	}

	public WidgetRuntimeInstance withName(String name) {
		setName(name);

		return this;
	}

	public WidgetRuntimeInstance withDescription(String description) {
		setDescription(description);

		return this;
	}

	public WidgetRuntimeInstance withLayout(Object layout) {
		setLayout(layout);

		return this;
	}

	public WidgetRuntimeInstance withWidgetClassName(String widgetClassName) {
		setWidgetClassName(widgetClassName);

		return this;
	}

	public WidgetRuntimeInstance withInstance(Object instance) {
		setInstance(instance);

		return this;
	}

	public WidgetRuntimeInstance withWidgetRuntimeDefinition(WidgetRuntimeDefinition widgetRuntimeDefinition) {
		setWidgetRuntimeDefinition(widgetRuntimeDefinition);

		return this;
	}

	public WidgetRuntimeInstance withDataProviders(List<DataProviderInstance> dataProviders) {
		setDataProviders(dataProviders);

		return this;
	}

	public WidgetRuntimeInstance withUserRuntime(UserRuntimeInstance userRuntime) {
		setUserRuntime(userRuntime);

		return this;
	}
}
