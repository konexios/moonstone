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
import com.arrow.dashboard.properties.string.ChoiceKeyValueStringPropertyView;
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
import com.arrow.kronos.data.DeviceTelemetry;
import com.arrow.kronos.data.DeviceType;
import com.arrow.kronos.data.LastTelemetryItem;
import com.arrow.kronos.repo.LastTelemetryItemSearchParams;
import com.arrow.widget.configuration.WidgetConfigurationHelper;
import com.arrow.widget.configuration.WidgetHelper;
import com.arrow.widget.model.DeviceTelemetryGaugeModel;
import com.arrow.widget.provider.DeviceDataProvider;
import com.arrow.widget.provider.DeviceTypeDataProvider;
import com.arrow.widget.provider.LastTelemetryItemDataProvider;

@Small(width = 1, height = 1)
@Medium(width = 2, height = 1)
@Large(width = 3, height = 1)
@XtraLarge(width = 4, height = 1)
@Widget(directive = "device-telemetry-gauge-widget", name = "Device Telemetry Gauge Widget", description = "A widget that shows the telemetry value as a gauge for a device.")
public class DeviceTelemetryGaugeWidget extends ScheduledWidgetAbstract {

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

	public DeviceTelemetryGaugeWidget() {
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
			        WidgetConstants.Configuration.Name.DEVICE_TELEMETRY_GAUGE_WIDGET,
			        WidgetConstants.Configuration.Label.DEVICE_TELEMETRY_GAUGE_WIDGET, devices,
			        configurationRuntimeModel.getDeviceUid());

			// telemetry selection page (stub)
			configuration.addPage(new Page().withName(WidgetConstants.Page.Name.SINGLE_TELEMETRY_SELECTION)
			        .withLabel(WidgetConstants.Page.Label.SINGLE_TELEMETRY_SELECTION));

			// settings page (stub)
			configuration.addPage(new Page().withName(WidgetConstants.Page.Name.SETTINGS)
			        .withLabel(WidgetConstants.Page.Label.SETTINGS));

			// populate
			populateConfigurationRuntimeModel(configuration);
		} else {
			configuration = WidgetConfigurationHelper.buildErrorPageConfiguration(
					WidgetConstants.Configuration.Name.DEVICE_TELEMETRY_GAUGE_WIDGET,
					WidgetConstants.Configuration.Label.DEVICE_TELEMETRY_GAUGE_WIDGET,
					WidgetConstants.Page.Label.SINGLE_DEVICE_SELECTION, "No available devices");
		}

		return configuration;
	}

	@ConfigurationPageRequest(page = 0)
	public Configuration processDeviceSelection(Configuration configuration) {
		String method = "processDeviceSelection";
		logInfo(method, "...");

		// get selected device
		String deviceUid = Configuration.<String> getValue(configuration,
		        WidgetConstants.Page.Name.SINGLE_DEVICE_SELECTION, WidgetConstants.Property.Name.DEVICE_UID);

		// lookup device
		Device device = deviceDataProvider.findDeviceByUid(deviceUid);

		// lookup device type
		DeviceType deviceType = deviceTypeDataProvider.findDeviceType(device.getDeviceTypeId());

		List<DeviceTelemetry> deviceTelemetries = new ArrayList<>();
		for (DeviceTelemetry deviceTelemetry : deviceType.getTelemetries())
			if (deviceTelemetry.getType().isChartable())
				deviceTelemetries.add(deviceTelemetry);

		if (deviceTelemetries.isEmpty()) {
			configuration.getPage(WidgetConstants.Page.Name.SINGLE_DEVICE_SELECTION)
					.setError("There are no telemetries to show on chart avalable for selected device");
		} else {
			// remove possible values
			ChoiceKeyValueStringPropertyView view = (ChoiceKeyValueStringPropertyView) configuration
			        .getPage(WidgetConstants.Page.Name.SINGLE_DEVICE_SELECTION)
			        .getPageProperty(WidgetConstants.Property.Name.DEVICE_UID).getProperty().getView();
			view.getPossibleValues().clear();

			configuration.getPage(WidgetConstants.Page.Name.SINGLE_TELEMETRY_SELECTION)
					.addProperty(WidgetConfigurationHelper.buildSingleTelemetrySelectionPageProperty(deviceTelemetries,
							configurationRuntimeModel.getTelemetryName()));

			// re-populate based on selections
			populateConfigurationRuntimeModel(configuration);

			// move to next page
			configuration.setCurrentPage(1).setChangedPage(1);
		}
		return configuration;
	}

	@ConfigurationPageRequest(page = 1)
	public Configuration processTelemetrySelection(Configuration configuration) {
		String method = "processTelemetrySelection";
		logInfo(method, "...");

		Page settingsPage = configuration.getPage(WidgetConstants.Page.Name.SETTINGS);

		// minimum value
		settingsPage.addProperty(
		        WidgetConfigurationHelper.buildMinValuePageProperty(configurationRuntimeModel.getMinimumValue()));

		// maximum value
		settingsPage.addProperty(
		        WidgetConfigurationHelper.buildMaxValuePageProperty(configurationRuntimeModel.getMaximumValue()));

		// refresh rate
		settingsPage.addProperty(
		        WidgetConfigurationHelper.buildRefreshRatePageProperty(configurationRuntimeModel.getRefreshRate()));

		// re-populate based on selections
		populateConfigurationRuntimeModel(configuration);

		// move to next page
		configuration.setCurrentPage(2).setChangedPage(2);

		return configuration;
	}

	@ConfigurationPageRequest(page = 2)
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

	private DeviceTelemetryGaugeModel loadWidgetData(String deviceId, String telemetryName) {
		Assert.hasText(deviceId, "deviceId is empty");

		LastTelemetryItemSearchParams params = new LastTelemetryItemSearchParams();
		params.addDeviceIds(deviceId);
		params.addNames(telemetryName);
		LastTelemetryItem lastTelemetryItem = lastTelemetryItemDataProvider.findLastTelemetryItem(params);

		DeviceTelemetryGaugeModel lastTelemetryItemModel = null;
		try {
			lastTelemetryItemModel = new DeviceTelemetryGaugeModel()
			        .withName(configurationRuntimeModel.getTelemetryName()).withValue("0")
			        .withMinValue(configurationRuntimeModel.getMinimumValue().toString())
			        .withMaxValue(configurationRuntimeModel.getMaximumValue().toString())
			        .withTimestamp(System.currentTimeMillis());

			if (lastTelemetryItem != null)
				lastTelemetryItemModel.withName(lastTelemetryItem.getName())
				        .withValue(lastTelemetryItem.value().toString())
				        .withTimestamp(lastTelemetryItem.getTimestamp());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return lastTelemetryItemModel;
	}

	private class ScheduledWorker implements Runnable {
		@Override
		public void run() {
			String method = "run";
			logInfo(method, "...");
			logDebug(method, "state: %s", getState());

			DeviceTelemetryGaugeModel lastTelemetry = loadWidgetData(widgetRuntimeModel.getDevice().getId(),
			        configurationRuntimeModel.getTelemetryName());
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

			// telemetry name
			String telemetryName = Configuration.<String> getValue(configuration,
			        WidgetConstants.Page.Name.SINGLE_TELEMETRY_SELECTION,
			        WidgetConstants.Property.Name.SINGLE_TELEMETRY_SELECTION);

			// min value
			Integer minimumValue = Configuration.<Integer> getValue(configuration, WidgetConstants.Page.Name.SETTINGS,
			        WidgetConstants.Property.Name.MIN_VALUE);

			// max value
			Integer maximumValue = Configuration.<Integer> getValue(configuration, WidgetConstants.Page.Name.SETTINGS,
			        WidgetConstants.Property.Name.MAX_VALUE);

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

			logDebug(method, "deviceUID: %s, telemetryName: %s, refreshRate: %s", deviceUid, telemetryName,
			        refreshRate);

			// populate configuration runtime model
			configurationRuntimeModel.withDeviceUid(deviceUid).withTelemetryName(telemetryName)
			        .withMinimumValue(minimumValue).withMaximumValue(maximumValue).withRefreshRate(refreshRate);
		}
	}

	private class ConfigurationRuntimeModel implements Serializable {
		private static final long serialVersionUID = -7244703954967660784L;

		private String deviceUid;
		private String telemetryName;
		private Integer minimumValue = WidgetConstants.DEFAULT_MINIMUM_VALUE;
		private Integer maximumValue = WidgetConstants.DEFAULT_MAXIMUM_VALUE;
		private Integer refreshRate = WidgetConstants.DEFAULT_REFRESH_RATE;

		public ConfigurationRuntimeModel() {
		}

		public String getDeviceUid() {
			return deviceUid;
		}

		public void setDeviceUid(String deviceUid) {
			this.deviceUid = deviceUid;
		}

		public String getTelemetryName() {
			return telemetryName;
		}

		public void setTelemetryName(String telemetryName) {
			this.telemetryName = telemetryName;
		}

		public Integer getMinimumValue() {
			return minimumValue;
		}

		public void setMinimumValue(Integer minimumValue) {
			if (minimumValue != null)
				this.minimumValue = minimumValue;
		}

		public Integer getMaximumValue() {
			return maximumValue;
		}

		public void setMaximumValue(Integer maximumValue) {
			if (maximumValue != null)
				this.maximumValue = maximumValue;
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

		public ConfigurationRuntimeModel withTelemetryName(String telemetryName) {
			setTelemetryName(telemetryName);

			return this;
		}

		public ConfigurationRuntimeModel withMinimumValue(Integer minimumValue) {
			setMinimumValue(minimumValue);

			return this;
		}

		public ConfigurationRuntimeModel withMaximumValue(Integer maximumValue) {
			setMaximumValue(maximumValue);

			return this;
		}

		public ConfigurationRuntimeModel withRefreshRate(Integer refreshRate) {
			setRefreshRate(refreshRate);

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