package com.arrow.widget;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import com.arrow.dashboard.messaging.SimpleTopicProvider;
import com.arrow.dashboard.widget.ScheduledWidgetAbstract;
import com.arrow.dashboard.widget.WidgetData;
import com.arrow.dashboard.widget.annotation.Widget;
import com.arrow.dashboard.widget.annotation.configuration.ConfigurationPageRequest;
import com.arrow.dashboard.widget.annotation.configuration.ConfigurationPersistence;
import com.arrow.dashboard.widget.annotation.configuration.ConfigurationRequest;
import com.arrow.dashboard.widget.annotation.data.DataProvider;
import com.arrow.dashboard.widget.annotation.messaging.TopicProvider;
import com.arrow.dashboard.widget.annotation.size.Large;
import com.arrow.dashboard.widget.annotation.size.Medium;
import com.arrow.dashboard.widget.annotation.size.Small;
import com.arrow.dashboard.widget.annotation.size.XtraLarge;
import com.arrow.dashboard.widget.configuration.Configuration;
import com.arrow.dashboard.widget.configuration.Page;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.DeviceType;
import com.arrow.kronos.data.LastTelemetryItem;
import com.arrow.widget.configuration.WidgetConfigurationHelper;
import com.arrow.widget.configuration.WidgetHelper;
import com.arrow.widget.model.DeviceLastTelemetryItemModel;
import com.arrow.widget.provider.DeviceDataProvider;
import com.arrow.widget.provider.DeviceTypeDataProvider;
import com.arrow.widget.provider.LastTelemetryItemDataProvider;

@Small(width = 1, height = 1)
@Medium(width = 2, height = 1)
@Large(width = 3, height = 1)
@XtraLarge(width = 4, height = 1)
@Widget(directive = "device-last-telemetry-widget", name = "Device Last Telemetry Widget", description = "A widget that shows the latest telemetry for a device.")
public class DeviceLastTelemetryWidget extends ScheduledWidgetAbstract {

	@DataProvider
	private DeviceTypeDataProvider deviceTypeDataProvider;
	@DataProvider
	private DeviceDataProvider deviceDataProvider;
	@DataProvider
	private LastTelemetryItemDataProvider lastTelemetryItemDataProvider;
	@TopicProvider("/widget-data")
	private SimpleTopicProvider lastTelemetryTopicProvider;

	// runtime
	private ConfigurationRuntimeModel configurationRuntimeModel;
	private WidgetRuntimeModel widgetRuntimeModel;

	public DeviceLastTelemetryWidget() {
		super(1); // corePoolSize

		configurationRuntimeModel = new ConfigurationRuntimeModel();
	}

	@ConfigurationRequest
	public Configuration initialConfig() {
		String method = "initialConfig";
		logInfo(method, "...");

		Configuration configuration = null;

		List<Device> devices = deviceDataProvider.findDevices();
		if (devices != null && !devices.isEmpty()) {
			// sort devices
			devices.sort(Comparator.comparing(Device::getName, String.CASE_INSENSITIVE_ORDER));

			configuration = WidgetConfigurationHelper.buildSingleDeviceConfiguration(
			        WidgetConstants.Configuration.Name.DEVICE_LAST_TELEMETRY_WIDGET,
			        WidgetConstants.Configuration.Label.DEVICE_LAST_TELEMETRY_WIDGET, devices,
			        configurationRuntimeModel.getDeviceUid());

			// settings page
			configuration.addPage(new Page().withName(WidgetConstants.Page.Name.SETTINGS)
			        .withLabel(WidgetConstants.Page.Label.SETTINGS));

			// populate
			populateConfigurationRuntimeModel(configuration);
		} else {
			configuration = WidgetConfigurationHelper.buildErrorPageConfiguration(
					WidgetConstants.Configuration.Name.DEVICE_LAST_TELEMETRY_WIDGET,
					WidgetConstants.Configuration.Label.DEVICE_LAST_TELEMETRY_WIDGET,
					WidgetConstants.Page.Label.SINGLE_DEVICE_SELECTION, "No available devices");
		}

		return configuration;
	}

	@ConfigurationPageRequest(page = 0)
	public Configuration processDeviceSelection(Configuration configuration) {
		String method = "processDeviceSelection";
		logInfo(method, "...");

		Page settingsPage = configuration.getPage(WidgetConstants.Page.Name.SETTINGS);

		// telemetry name
		settingsPage.addProperty(WidgetConfigurationHelper
		        .buildUseTelemetryNamePageProperty(configurationRuntimeModel.isUseTelemetryName()));

		// refresh rate
		settingsPage.addProperty(
		        WidgetConfigurationHelper.buildRefreshRatePageProperty(configurationRuntimeModel.getRefreshRate()));

		// re-populate based on selections
		populateConfigurationRuntimeModel(configuration);

		// move to next page
		configuration.setCurrentPage(1).setChangedPage(1);

		return configuration;
	}

	@ConfigurationPageRequest(page = 1)
	public Configuration processSettings(Configuration configuration) {
		String method = "processSettings";
		logInfo(method, "...");

		// re-populate based on selections
		populateConfigurationRuntimeModel(configuration);

		// close configuration
		configuration.close();

		return configuration;
	}

	@ConfigurationPersistence
	public void loadConfiguration(Configuration configuration) {
		String method = "loadConfiguration";
		logInfo(method, "...");
		logDebug(method, "name: %s, version: %s", configuration.getName(), configuration.getVersion());

		populateConfigurationRuntimeModel(configuration);
	}

	@Override
	public void start() {
		String method = "start";
		logInfo(method, "...");

		if (isReady()) {

			Device device = null;

			// lookup device
			if (!StringUtils.isEmpty(configurationRuntimeModel.getDeviceUid()))
				device = deviceDataProvider.findDeviceByUid(configurationRuntimeModel.getDeviceUid());

			if (device == null) {
				onErrorState();

				return;
			}

			DeviceType deviceType = deviceTypeDataProvider.findDeviceType(device.getDeviceTypeId());
			if (deviceType == null) {
				onErrorState();

				return;
			}

			widgetRuntimeModel = new WidgetRuntimeModel(device,
			        WidgetHelper.populateDeviceTelemetryNameDescriptionMap(new HashMap<>(), deviceType));

			onRunningState();
		}
	}

	@Override
	public void run() {
		String method = "run";
		logInfo(method, "...");

		addScheduledWorker(new ScheduledWorker(), 0, configurationRuntimeModel.getRefreshRate(), TimeUnit.SECONDS);
	}

	private List<DeviceLastTelemetryItemModel> loadWidgetData(String deviceId) {
		Assert.hasText(deviceId, "deviceId is empty");

		String method = "loadWidgetData";
		logInfo(method, "...");

		List<DeviceLastTelemetryItemModel> models = new ArrayList<>();
		try {
			List<LastTelemetryItem> lastTelemetryItems = lastTelemetryItemDataProvider.findByDeviceId(deviceId);
			for (LastTelemetryItem item : lastTelemetryItems) {
				String name = item.getName();

				if (!configurationRuntimeModel.isUseTelemetryName())
					name = WidgetHelper.telemetryNameToDescription(widgetRuntimeModel.getMap(), name);

				models.add(new DeviceLastTelemetryItemModel().withName(name).withValue(item.value().toString())
				        .withTimestamp(item.getTimestamp()));
			}

			// sort
			models.sort(Comparator.comparing(DeviceLastTelemetryItemModel::getName, String.CASE_INSENSITIVE_ORDER));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return models;
	}

	private class ScheduledWorker implements Runnable {
		@Override
		public void run() {
			String method = "run";
			logInfo(method, "...");
			logDebug(method, "state: %s", getState());

			List<DeviceLastTelemetryItemModel> lastTelemetry = loadWidgetData(widgetRuntimeModel.getDevice().getId());
			if (lastTelemetry != null)
				sendMessage(lastTelemetryTopicProvider, new WidgetData().withState(getState()).withData(lastTelemetry));
		}
	}

	private void populateConfigurationRuntimeModel(Configuration configuration) {
		String method = "populateConfigurationRuntimeModel";
		logInfo(method, "...");

		if (configuration != null) {
			// uid
			String deviceUid = Configuration.<String> getValue(configuration,
			        WidgetConstants.Page.Name.SINGLE_DEVICE_SELECTION, WidgetConstants.Property.Name.DEVICE_UID);

			// use telemetry name
			Boolean useTelemetryName = Configuration.<Boolean> getValue(configuration,
			        WidgetConstants.Page.Name.SETTINGS, WidgetConstants.Property.Name.USE_TELEMETRY_RAW_NAME);

			// refresh rate
			Integer refreshRate = Configuration.<Integer> getValue(configuration, WidgetConstants.Page.Name.SETTINGS,
			        WidgetConstants.Property.Name.REFRESH_RATE);
			
			if (refreshRate != null) {
				// ensure refresh rate is a positive value
				if (refreshRate < 0)
					refreshRate = Math.abs(refreshRate);

				// ensure refresh rate is not zero
				if (refreshRate == 0)
					refreshRate = WidgetConstants.DEFAULT_REFRESH_RATE;
			}

			logDebug(method, "deviceUID: %s, useTelemetryName: %s, refreshRate: %s", deviceUid, useTelemetryName,
			        refreshRate);

			// populate configuration runtime model
			configurationRuntimeModel.withDeviceUid(deviceUid).withUseTelemetryName(useTelemetryName)
			        .withRefreshRate(refreshRate);
		}
	}

	private class ConfigurationRuntimeModel implements Serializable {
		private static final long serialVersionUID = -7244703954967660784L;

		private String deviceUid;
		private Boolean useTelemetryName = WidgetConstants.DEFAULT_USE_TELEMETRY_NAME;
		private Integer refreshRate = WidgetConstants.DEFAULT_REFRESH_RATE;

		public ConfigurationRuntimeModel() {
		}

		public String getDeviceUid() {
			return deviceUid;
		}

		public void setDeviceUid(String deviceUid) {
			this.deviceUid = deviceUid;
		}

		public Boolean isUseTelemetryName() {
			return useTelemetryName;
		}

		public void setUseTelemetryName(Boolean useTelemetryName) {
			if (useTelemetryName != null)
				this.useTelemetryName = useTelemetryName;
		}

		public Integer getRefreshRate() {
			return refreshRate;
		}

		public void setRefreshRate(Integer refreshRate) {
			if (refreshRate != null)
				this.refreshRate = refreshRate;
		}

		public ConfigurationRuntimeModel withDeviceUid(String deviceUid) {
			setDeviceUid(deviceUid);

			return this;
		}

		public ConfigurationRuntimeModel withUseTelemetryName(Boolean useTelemetryName) {
			setUseTelemetryName(useTelemetryName);

			return this;
		}

		public ConfigurationRuntimeModel withRefreshRate(Integer refreshRateSeconds) {
			setRefreshRate(refreshRateSeconds);

			return this;
		}
	}

	private class WidgetRuntimeModel implements Serializable {
		private static final long serialVersionUID = -752599151378564453L;

		private Device device;
		private Map<String, String> map;

		public WidgetRuntimeModel(Device device, Map<String, String> map) {
			this.device = device;
			this.map = map;
		}

		public Device getDevice() {
			return device;
		}

		public Map<String, String> getMap() {
			return map;
		}
	}
}