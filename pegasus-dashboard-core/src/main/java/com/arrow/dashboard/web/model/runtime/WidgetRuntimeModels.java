package com.arrow.dashboard.web.model.runtime;

import java.io.Serializable;
import java.util.Map;

import com.arrow.dashboard.web.model.WidgetModels;
import com.arrow.dashboard.widget.configuration.Configuration;

public class WidgetRuntimeModels {

	public static class WidgetRuntimeModel implements Serializable {
		private static final long serialVersionUID = -3949993289986658977L;

		// TODO rename id to widgetRuntimeId
		private String id;
		// TODO add widgetId (the persisted widget id, not the runtime id)
		private String widgetTypeId;
		private String parentId;
		private WidgetMetaData widgetMetaData;
		private String directive;
		private Object layout;
		private String socketEndpoint;
		private Map<String, String> topicProviders;
		private Map<String, String> messageEndpoints;

		// topics
		private String configurationTopic;
		private String fastConfigurationTopic;
		private String metaDataUpdateTopic;

		// urls
		private String configurationInit;
		private String configurationProcess;
		private String saveConfiguration;
		private String fastConfigurationInit;
		private String fastConfigurationProcess;
		private String editWidgetUrl;
		private String updateWidgetUrl;
		private String cancelEditWidgetUrl;
		// TODO rename to deleteWidgetUrl
		private String deleteUrl;
		// TODO rename to updateMetaDataUrl
		private String metaDataUpdate;

		private String error;

		private WidgetModels.ReadWidgetModel widget;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getWidgetTypeId() {
			return widgetTypeId;
		}

		public void setWidgetTypeId(String widgetTypeId) {
			this.widgetTypeId = widgetTypeId;
		}

		public String getParentId() {
			return parentId;
		}

		public void setParentId(String parentId) {
			this.parentId = parentId;
		}

		public WidgetMetaData getWidgetMetaData() {
			return widgetMetaData;
		}

		public void setWidgetMetaData(WidgetMetaData widgetMetaData) {
			this.widgetMetaData = widgetMetaData;
		}

		public String getDirective() {
			return directive;
		}

		public void setDirective(String directive) {
			this.directive = directive;
		}

		public Object getLayout() {
			return layout;
		}

		public void setLayout(Object layout) {
			this.layout = layout;
		}

		public String getSocketEndpoint() {
			return socketEndpoint;
		}

		public void setSocketEndpoint(String socketEndpoint) {
			this.socketEndpoint = socketEndpoint;
		}

		public Map<String, String> getTopicProviders() {
			return topicProviders;
		}

		public void setTopicProviders(Map<String, String> topicProviders) {
			this.topicProviders = topicProviders;
		}

		public Map<String, String> getMessageEndpoints() {
			return messageEndpoints;
		}

		public void setMessageEndpoints(Map<String, String> messageEndpoints) {
			this.messageEndpoints = messageEndpoints;
		}

		public String getConfigurationInit() {
			return configurationInit;
		}

		public void setConfigurationInit(String configurationInit) {
			this.configurationInit = configurationInit;
		}

		public String getConfigurationProcess() {
			return configurationProcess;
		}

		public void setConfigurationProcess(String configurationProcess) {
			this.configurationProcess = configurationProcess;
		}

		public String getSaveConfiguration() {
			return saveConfiguration;
		}

		public void setSaveConfiguration(String saveConfiguration) {
			this.saveConfiguration = saveConfiguration;
		}

		public String getConfigurationTopic() {
			return configurationTopic;
		}

		public void setConfigurationTopic(String configurationTopic) {
			this.configurationTopic = configurationTopic;
		}

		public String getFastConfigurationInit() {
			return fastConfigurationInit;
		}

		public void setFastConfigurationInit(String fastConfigurationInit) {
			this.fastConfigurationInit = fastConfigurationInit;
		}

		public String getFastConfigurationProcess() {
			return fastConfigurationProcess;
		}

		public void setFastConfigurationProcess(String fastConfigurationProcess) {
			this.fastConfigurationProcess = fastConfigurationProcess;
		}

		public String getFastConfigurationTopic() {
			return fastConfigurationTopic;
		}

		public void setFastConfigurationTopic(String fastConfigurationTopic) {
			this.fastConfigurationTopic = fastConfigurationTopic;
		}

		public String getEditWidgetUrl() {
			return editWidgetUrl;
		}

		public void setEditWidgetUrl(String editWidgetUrl) {
			this.editWidgetUrl = editWidgetUrl;
		}

		public String getUpdateWidgetUrl() {
			return updateWidgetUrl;
		}

		public void setUpdateWidgetUrl(String updateWidgetUrl) {
			this.updateWidgetUrl = updateWidgetUrl;
		}

		public String getCancelEditWidgetUrl() {
			return cancelEditWidgetUrl;
		}

		public void setCancelEditWidgetUrl(String cancelEditWidgetUrl) {
			this.cancelEditWidgetUrl = cancelEditWidgetUrl;
		}

		public String getDeleteUrl() {
			return deleteUrl;
		}

		public void setDeleteUrl(String deleteUrl) {
			this.deleteUrl = deleteUrl;
		}

		public String getMetaDataUpdate() {
			return metaDataUpdate;
		}

		public void setMetaDataUpdate(String metaDataUpdate) {
			this.metaDataUpdate = metaDataUpdate;
		}

		public String getMetaDataUpdateTopic() {
			return metaDataUpdateTopic;
		}

		public void setMetaDataUpdateTopic(String metaDataUpdateTopic) {
			this.metaDataUpdateTopic = metaDataUpdateTopic;
		}

		public String getError() {
			return error;
		}

		public void setError(String error) {
			this.error = error;
		}

		public WidgetModels.ReadWidgetModel getWidget() {
			return widget;
		}

		public void setWidget(WidgetModels.ReadWidgetModel widget) {
			this.widget = widget;
		}

		public WidgetRuntimeModel withId(String id) {
			setId(id);

			return this;
		}

		public WidgetRuntimeModel withWidgetTypeId(String widgetTypeId) {
			setWidgetTypeId(widgetTypeId);

			return this;
		}

		public WidgetRuntimeModel withParentId(String parentId) {
			setParentId(parentId);

			return this;
		}

		public WidgetRuntimeModel withDirective(String direction) {
			setDirective(direction);

			return this;
		}

		public WidgetRuntimeModel withLayout(Object layout) {
			setLayout(layout);

			return this;
		}

		public WidgetRuntimeModel withSocketEndpoint(String socketEndpoint) {
			setSocketEndpoint(socketEndpoint);

			return this;
		}

		public WidgetRuntimeModel withTopicProviders(Map<String, String> topicProviders) {
			setTopicProviders(topicProviders);

			return this;
		}

		public WidgetRuntimeModel withMessageEndpoints(Map<String, String> messageEndpoints) {
			setMessageEndpoints(messageEndpoints);

			return this;
		}

		public WidgetRuntimeModel withConfigurationInit(String configurationInit) {
			setConfigurationInit(configurationInit);

			return this;
		}

		public WidgetRuntimeModel withConfigurationProcess(String configurationProcess) {
			setConfigurationProcess(configurationProcess);

			return this;
		}

		public WidgetRuntimeModel withSaveConfiguration(String saveConfiguration) {
			setSaveConfiguration(saveConfiguration);

			return this;
		}

		public WidgetRuntimeModel withConfigurationTopic(String configurationTopic) {
			setConfigurationTopic(configurationTopic);

			return this;
		}

		public WidgetRuntimeModel withFastConfigurationInit(String fastConfigurationInit) {
			setFastConfigurationInit(fastConfigurationInit);

			return this;
		}

		public WidgetRuntimeModel withFastConfigurationProcess(String fastConfigurationProcess) {
			setFastConfigurationProcess(fastConfigurationProcess);

			return this;
		}

		public WidgetRuntimeModel withFastConfigurationTopic(String fastConfigurationTopic) {
			setFastConfigurationTopic(fastConfigurationTopic);

			return this;
		}

		public WidgetRuntimeModel withEditWidgetUrl(String editWidgetUrl) {
			setEditWidgetUrl(editWidgetUrl);

			return this;
		}

		public WidgetRuntimeModel withUpdateWidgetUrl(String updateWidgetUrl) {
			setUpdateWidgetUrl(updateWidgetUrl);

			return this;
		}

		public WidgetRuntimeModel withCancelEditWidgetUrl(String cancelEditWidgetUrl) {
			setCancelEditWidgetUrl(cancelEditWidgetUrl);

			return this;
		}

		public WidgetRuntimeModel withDeleteUrl(String deleteUrl) {
			setDeleteUrl(deleteUrl);

			return this;
		}

		public WidgetRuntimeModel withWidgetMetaData(WidgetMetaData widgetMetaData) {
			setWidgetMetaData(widgetMetaData);

			return this;
		}

		public WidgetRuntimeModel withMetaDataUpdate(String metaDataUpdate) {
			setMetaDataUpdate(metaDataUpdate);

			return this;
		}

		public WidgetRuntimeModel withMetaDataUpdateTopic(String metaDataUpdateTopic) {
			setMetaDataUpdateTopic(metaDataUpdateTopic);

			return this;
		}

		public WidgetRuntimeModel withError(String error) {
			setError(error);

			return this;
		}

		public WidgetRuntimeModel withWidget(WidgetModels.ReadWidgetModel widget) {
			setWidget(widget);

			return this;
		}
	}

	public static class WidgetMetaData implements Serializable {
		private static final long serialVersionUID = -8734273281764607863L;

		private String name;
		private String description;
		private int positionX;
		private int positionY;
		private int width;
		private int height;

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

		public int getPositionX() {
			return positionX;
		}

		public void setPositionX(int positionX) {
			this.positionX = positionX;
		}

		public int getPositionY() {
			return positionY;
		}

		public void setPositionY(int positionY) {
			this.positionY = positionY;
		}

		public int getWidth() {
			return width;
		}

		public void setWidth(int width) {
			this.width = width;
		}

		public int getHeight() {
			return height;
		}

		public void setHeight(int height) {
			this.height = height;
		}

		public WidgetMetaData withName(String name) {
			setName(name);

			return this;
		}

		public WidgetMetaData withDescription(String description) {
			setDescription(description);

			return this;
		}

		public WidgetMetaData withPositionX(int positionX) {
			setPositionX(positionX);

			return this;
		}

		public WidgetMetaData withPositionY(int positionY) {
			setPositionY(positionY);

			return this;
		}

		public WidgetMetaData withWidth(int width) {
			setWidth(width);

			return this;
		}

		public WidgetMetaData withHeight(int height) {
			setHeight(height);

			return this;
		}
	}

	public static class NewWidgetRequest implements Serializable {
		private static final long serialVersionUID = 5304834224046166269L;

		private String generatedParentRuntimeId;
		private String widgetTypeId;

		public NewWidgetRequest() {
		}

		public String getWidgetTypeId() {
			return widgetTypeId;
		}

		public void setWidgetTypeId(String widgetTypeId) {
			this.widgetTypeId = widgetTypeId;
		}

		public String getGeneratedParentRuntimeId() {
			return generatedParentRuntimeId;
		}

		public void setGeneratedParentRuntimeId(String generatedParentRuntimeId) {
			this.generatedParentRuntimeId = generatedParentRuntimeId;
		}
	}

	public static class CancelNewWidgetRequest implements Serializable {
		private static final long serialVersionUID = 3616461187853735646L;

		private String widgetRuntimeId;

		public String getWidgetRuntimeId() {
			return widgetRuntimeId;
		}

		public void setWidgetRuntimeId(String widgetRuntimeId) {
			this.widgetRuntimeId = widgetRuntimeId;
		}
	}

	public static class RegisterWidgetToParentRequest extends UpdateWidgetRequest implements Serializable {
		private static final long serialVersionUID = -4776360956810139261L;

		private String parentRuntimeId;
		private String parentId;
		private String widgetRuntimeId;

		public String getParentRuntimeId() {
			return parentRuntimeId;
		}

		public void setParentRuntimeId(String currentParentId) {
			this.parentRuntimeId = currentParentId;
		}

		public String getParentId() {
			return parentId;
		}

		public void setParentId(String parentId) {
			this.parentId = parentId;
		}

		public String getWidgetRuntimeId() {
			return widgetRuntimeId;
		}

		public void setWidgetRuntimeId(String widgetRuntimeId) {
			this.widgetRuntimeId = widgetRuntimeId;
		}
	}

	public static class UpdateWidgetRequest implements Serializable {
		private static final long serialVersionUID = 1868757009052442445L;

		private WidgetMetaData widgetMetaData;
		private Configuration configuration;

		public WidgetMetaData getWidgetMetaData() {
			return widgetMetaData;
		}

		public void setWidgetMetaData(WidgetMetaData widgetMetaData) {
			this.widgetMetaData = widgetMetaData;
		}

		public Configuration getConfiguration() {
			return configuration;
		}

		public void setConfiguration(Configuration configuration) {
			this.configuration = configuration;
		}
	}
}