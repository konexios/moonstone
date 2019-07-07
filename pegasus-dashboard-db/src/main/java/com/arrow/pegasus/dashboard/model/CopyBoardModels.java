package com.arrow.pegasus.dashboard.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.arrow.pegasus.dashboard.data.Board;
import com.arrow.pegasus.dashboard.data.ConfigurationPage;
import com.arrow.pegasus.dashboard.data.Container;
import com.arrow.pegasus.dashboard.data.PageProperty;
import com.arrow.pegasus.dashboard.data.PropertyValue;
import com.arrow.pegasus.dashboard.data.Widget;
import com.arrow.pegasus.dashboard.data.WidgetConfiguration;

public class CopyBoardModels {

	public static class CopyOfBoard implements Serializable {
		private static final long serialVersionUID = -5526009062624959861L;

		private Board board;
		private List<CopyOfContainer> containers = new ArrayList<>();
		private List<CopyOfWidget> widgets = new ArrayList<>();

		public Board getBoard() {
			return board;
		}

		public void setBoard(Board board) {
			this.board = board;
		}

		public CopyOfBoard withBoard(Board board) {
			setBoard(board);

			return this;
		}

		public List<CopyOfContainer> getContainers() {
			return containers;
		}

		public void setContainers(List<CopyOfContainer> containers) {
			if (containers != null)
				this.containers = containers;
		}

		public CopyOfBoard withContainers(List<CopyOfContainer> containers) {
			setContainers(containers);

			return this;
		}

		public List<CopyOfWidget> getWidgets() {
			return widgets;
		}

		public void setWidgets(List<CopyOfWidget> widgets) {
			if (widgets != null)
				this.widgets = widgets;
		}

		public CopyOfBoard withWidgets(List<CopyOfWidget> widgets) {
			setWidgets(widgets);

			return this;
		}
	}

	public static class CopyOfContainer implements Serializable {
		private static final long serialVersionUID = -5680225013372647304L;

		private Container container;
		private List<CopyOfWidget> widgets;

		public Container getContainer() {
			return container;
		}

		public void setContainer(Container container) {
			this.container = container;
		}

		public CopyOfContainer withContainer(Container container) {
			setContainer(container);

			return this;
		}

		public List<CopyOfWidget> getWidgets() {
			return widgets;
		}

		public void setWidgets(List<CopyOfWidget> widgets) {
			if (widgets != null)
				this.widgets = widgets;
		}

		public CopyOfContainer withWidgets(List<CopyOfWidget> widgets) {
			setWidgets(widgets);

			return this;
		}
	}

	public static class CopyOfWidget implements Serializable {
		private static final long serialVersionUID = 7693228303505357372L;

		private Widget widget;
		private List<CopyOfWidgetConfiguration> widgetConfigurations = new ArrayList<>();

		public Widget getWidget() {
			return widget;
		}

		public void setWidget(Widget widget) {
			this.widget = widget;
		}

		public CopyOfWidget withWidget(Widget widget) {
			setWidget(widget);

			return this;
		}

		public List<CopyOfWidgetConfiguration> getWidgetConfigurations() {
			return widgetConfigurations;
		}

		public void setWidgetConfigurations(List<CopyOfWidgetConfiguration> widgetConfigurations) {
			if (widgetConfigurations != null)
				this.widgetConfigurations = widgetConfigurations;
		}

		public CopyOfWidget withWidgetConfigurations(List<CopyOfWidgetConfiguration> widgetConfigurations) {
			setWidgetConfigurations(widgetConfigurations);

			return this;
		}
	}

	public static class CopyOfWidgetConfiguration implements Serializable {
		private static final long serialVersionUID = -4797182822864526312L;

		private WidgetConfiguration widgetConfiguration;
		private List<CopyOfConfigurationPage> configurationPages = new ArrayList<>();

		public WidgetConfiguration getWidgetConfiguration() {
			return widgetConfiguration;
		}

		public void setWidgetConfiguration(WidgetConfiguration widgetConfiguration) {
			this.widgetConfiguration = widgetConfiguration;
		}

		public CopyOfWidgetConfiguration withWidgetConfiguration(WidgetConfiguration widgetConfiguration) {
			setWidgetConfiguration(widgetConfiguration);

			return this;
		}

		public List<CopyOfConfigurationPage> getConfigurationPages() {
			return configurationPages;
		}

		public void setConfigurationPages(List<CopyOfConfigurationPage> configurationPages) {
			if (configurationPages != null)
				this.configurationPages = configurationPages;
		}

		public CopyOfWidgetConfiguration withConfigurationPages(List<CopyOfConfigurationPage> configurationPages) {
			setConfigurationPages(configurationPages);

			return this;
		}
	}

	public static class CopyOfConfigurationPage implements Serializable {
		private static final long serialVersionUID = 6501502569644879630L;

		private ConfigurationPage configurationPage;
		private List<CopyOfPageProperty> pageProperties;

		public ConfigurationPage getConfigurationPage() {
			return configurationPage;
		}

		public void setConfigurationPage(ConfigurationPage configurationPage) {
			this.configurationPage = configurationPage;
		}

		public CopyOfConfigurationPage withConfigurationPage(ConfigurationPage configurationPage) {
			setConfigurationPage(configurationPage);

			return this;
		}

		public List<CopyOfPageProperty> getPageProperties() {
			return pageProperties;
		}

		public void setPageProperties(List<CopyOfPageProperty> pageProperties) {
			if (pageProperties != null)
				this.pageProperties = pageProperties;
		}

		public CopyOfConfigurationPage withPageProperties(List<CopyOfPageProperty> pageProperties) {
			setPageProperties(pageProperties);

			return this;
		}
	}

	public static class CopyOfPageProperty implements Serializable {
		private static final long serialVersionUID = 5493188315489226374L;

		private PageProperty pageProperty;
		private List<CopyOfPropertyValue> propertyValues = new ArrayList<>();

		public PageProperty getPageProperty() {
			return pageProperty;
		}

		public void setPageProperty(PageProperty pageProperty) {
			this.pageProperty = pageProperty;
		}

		public CopyOfPageProperty withPageProperty(PageProperty pageProperty) {
			setPageProperty(pageProperty);

			return this;
		}

		public List<CopyOfPropertyValue> getPropertyValues() {
			return propertyValues;
		}

		public void setPropertyValues(List<CopyOfPropertyValue> propertyValues) {
			if (propertyValues != null)
				this.propertyValues = propertyValues;
		}

		public CopyOfPageProperty withPropertyValues(List<CopyOfPropertyValue> propertyValues) {
			setPropertyValues(propertyValues);

			return this;
		}
	}

	public static class CopyOfPropertyValue implements Serializable {
		private static final long serialVersionUID = -3429195944909835004L;

		private PropertyValue propertyValue;

		public PropertyValue getPropertyValue() {
			return propertyValue;
		}

		public void setPropertyValue(PropertyValue propertyValue) {
			this.propertyValue = propertyValue;
		}

		public CopyOfPropertyValue withPropertyValue(PropertyValue propertyValue) {
			setPropertyValue(propertyValue);

			return this;
		}
	}
}
