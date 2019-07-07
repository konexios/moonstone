package com.arrow.widget;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.util.Assert;

import com.arrow.acn.client.model.TelemetryItemType;
import com.arrow.dashboard.messaging.SimpleTopicProvider;
import com.arrow.dashboard.properties.integer.IntegerKeyValue;
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
import com.arrow.kronos.data.EsTelemetryItem;
import com.arrow.widget.configuration.WidgetConfigurationHelper;
import com.arrow.widget.configuration.WidgetHelper;
import com.arrow.widget.model.CubeTelemetryItemLineChartModel;
import com.arrow.widget.model.LineChartModel;
import com.arrow.widget.model.SquareTelemetryItemLineChartModel;
import com.arrow.widget.model.TelemetryItemLineChartModel;
import com.arrow.widget.provider.DeviceDataProvider;
import com.arrow.widget.provider.DeviceTypeDataProvider;
import com.arrow.widget.provider.EsTelemetryItemDataProvider;

@Small(width = 1, height = 1)
@Medium(width = 2, height = 1)
@Large(width = 3, height = 1)
@XtraLarge(width = 4, height = 1)
@Widget(directive = "device-telemetry-line-chart-widget", name = "Device Telemetry Line Chart Widget", description = "A widget that shows the telemetry values over time as a line chart for a device.")
public class DeviceTelemetryLineChartWidget extends ScheduledWidgetAbstract {

	@DataProvider
	private DeviceTypeDataProvider deviceTypeDataProvider;
	@DataProvider
	private DeviceDataProvider deviceDataProvider;
	@DataProvider
	private EsTelemetryItemDataProvider esTelemetryItemDataProvider;
	@TopicProvider("/widget-data")
	private SimpleTopicProvider lastTelemetryTopicProvider;

	// runtime
	private ConfigurationRuntimeModel configurationRuntimeModel;
	private WidgetRuntimeModel widgetRuntimeModel;

	public DeviceTelemetryLineChartWidget() {
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

			// @formatter:off
			configuration = new Configuration()
					.withName(WidgetConstants.Configuration.Name.DEVICE_TELEMETRY_LINE_CHART_WIDGET)
					.withLabel(WidgetConstants.Configuration.Label.DEVICE_TELEMETRY_LINE_CHART_WIDGET).setChangedPage(0)
					.setCurrentPage(0);
			// @formatter:on

			// single device selection page
			Page singleDevicePage = new Page().withName(WidgetConstants.Page.Name.SINGLE_DEVICE_SELECTION)
					.withLabel(WidgetConstants.Page.Label.SINGLE_DEVICE_SELECTION);
			singleDevicePage.addProperty(WidgetConfigurationHelper.buildSingleDeviceSelectionPageProperty(devices,
					configurationRuntimeModel.getDeviceUid()));
			configuration.addPage(singleDevicePage);

			// telemetry selection page (stub)
			configuration.addPage(new Page().withName(WidgetConstants.Page.Name.SINGLE_TELEMETRY_SELECTION)
					.withLabel(WidgetConstants.Page.Label.SINGLE_TELEMETRY_SELECTION));

			// settings (stub)
			configuration.addPage(new Page().withName(WidgetConstants.Page.Name.SETTINGS)
					.withLabel(WidgetConstants.Page.Label.SETTINGS));
		} else {
			configuration = WidgetConfigurationHelper.buildErrorPageConfiguration(
					WidgetConstants.Configuration.Name.DEVICE_TELEMETRY_LINE_CHART_WIDGET,
					WidgetConstants.Configuration.Label.DEVICE_TELEMETRY_LINE_CHART_WIDGET,
					WidgetConstants.Page.Label.SINGLE_DEVICE_SELECTION, "No available devices");
		}

		return configuration;
	}

	@ConfigurationPageRequest(page = 0)
	public Configuration processDeviceSelection(Configuration configuration) {
		String method = "processDeviceSelection";
		logInfo(method, "...");

		// get selected device
		String deviceUID = Configuration.<String>getValue(configuration,
				WidgetConstants.Page.Name.SINGLE_DEVICE_SELECTION, WidgetConstants.Property.Name.DEVICE_UID);

		// lookup device
		Device device = deviceDataProvider.findDeviceByUid(deviceUID);

		// lookup device type
		DeviceType deviceType = deviceTypeDataProvider.findDeviceType(device.getDeviceTypeId());

		List<DeviceTelemetry> deviceTelemetries = new ArrayList<>();
		for (DeviceTelemetry deviceTelemetry : deviceType.getTelemetries())
			if (deviceTelemetry.getType().isChartable()
					|| deviceTelemetry.getType().equals(TelemetryItemType.FloatSquare)
					|| deviceTelemetry.getType().equals(TelemetryItemType.FloatCube)
					|| deviceTelemetry.getType().equals(TelemetryItemType.IntegerSquare)
					|| deviceTelemetry.getType().equals(TelemetryItemType.IntegerCube))
				deviceTelemetries.add(deviceTelemetry);

		if (deviceTelemetries.isEmpty()) {
			configuration.getPage(WidgetConstants.Page.Name.SINGLE_DEVICE_SELECTION)
					.setError("There are no telemetries to show on line chart avalable for selected device");
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

		// display interval options
		List<IntegerKeyValue> displayIntervalOptions = new ArrayList<>();
		displayIntervalOptions.add(new IntegerKeyValue(60000, "1 min"));
		displayIntervalOptions.add(new IntegerKeyValue(300000, "5 min"));
		displayIntervalOptions.add(new IntegerKeyValue(900000, "15 min"));
		displayIntervalOptions.add(new IntegerKeyValue(1800000, "30 min"));
		displayIntervalOptions.add(new IntegerKeyValue(3600000, "1 hour"));

		// display interval value
		settingsPage.addProperty(WidgetConfigurationHelper.buildChoiceIntegerKeyValuePageProperty(
				WidgetConstants.Property.Name.DISPLAY_INTERVAL, WidgetConstants.Property.Label.DISPLAY_INTERVAL, null,
				true, displayIntervalOptions, configurationRuntimeModel.getDisplayInterval()));

		// include average value
		// TODO revisit, may include

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

		// populate
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
			widgetRuntimeModel.setDeviceType(deviceType);
			for (DeviceTelemetry dt : deviceType.getTelemetries()) {
				if (dt.getName().equals(configurationRuntimeModel.getTelemetryName())) {
					widgetRuntimeModel.setTelemetryItemType(dt.getType());
					break;
				}
			}

			onRunningState();
		}
	}

	@Override
	public void run() {
		String method = "run";
		logInfo(method, "...");

		addScheduledWorker(new ScheduledWorker(), 0, configurationRuntimeModel.getRefreshRate(), TimeUnit.SECONDS);
	}

	private List<EsTelemetryItem> loadWidgetData(String deviceId, String telemetryName, long from, long to) {
		Assert.hasText(deviceId, "deviceId is empty");

		String method = "loadWidgetData";
		logInfo(method, "...");

		try {
			return esTelemetryItemDataProvider.findAllTelemetryItems(deviceId, new String[] { telemetryName }, from, to,
					SortOrder.ASC);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private class ScheduledWorker implements Runnable {
		@Override
		public void run() {
			String method = "run";
			logInfo(method, "...");
			logDebug(method, "state: %s", getState());

			long marginBuffer = 5 * 1000;
			long now = Instant.now().toEpochMilli();
			long from = now - configurationRuntimeModel.displayInterval - marginBuffer;
			long to = now + marginBuffer;

			LineChartModel model = new LineChartModel()
					.withDisplayInterval(configurationRuntimeModel.getDisplayInterval())
					.withTelemetryName(WidgetHelper.telemetryNameToDescription(widgetRuntimeModel.getMap(),
							configurationRuntimeModel.getTelemetryName()))
					.withType(widgetRuntimeModel.getTelemetryItemType());

			List<EsTelemetryItem> esTelemetryData = loadWidgetData(widgetRuntimeModel.getDevice().getId(),
					configurationRuntimeModel.getTelemetryName(), from, to);

			if (esTelemetryData != null) {
				Object[] telemetryData = TelemetryItemLineChartModel.getTelemetryItemChartModels(esTelemetryData);
				if (widgetRuntimeModel.getTelemetryItemType().equals(TelemetryItemType.IntegerSquare)
						|| widgetRuntimeModel.getTelemetryItemType().equals(TelemetryItemType.FloatSquare)) {
					telemetryData = SquareTelemetryItemLineChartModel.getTelemetryItemChartModels(esTelemetryData);
				} else if (widgetRuntimeModel.getTelemetryItemType().equals(TelemetryItemType.IntegerCube)
						|| widgetRuntimeModel.getTelemetryItemType().equals(TelemetryItemType.FloatCube)) {
					telemetryData = CubeTelemetryItemLineChartModel.getTelemetryItemChartModels(esTelemetryData);
				}

				model.withTelemetryItems(telemetryData);
			}

			sendMessage(lastTelemetryTopicProvider, new WidgetData().withState(getState()).withData(model));
		}
	}

	private void populateConfigurationRuntimeModel(Configuration configuration) {
		String method = "populateConfigurationRuntimeModel";
		logInfo(method, "...");

		if (configuration != null) {
			// uid
			String deviceUid = Configuration.<String>getValue(configuration,
					WidgetConstants.Page.Name.SINGLE_DEVICE_SELECTION, WidgetConstants.Property.Name.DEVICE_UID);

			// telemetry name
			String telemetryName = Configuration.<String>getValue(configuration,
					WidgetConstants.Page.Name.SINGLE_TELEMETRY_SELECTION,
					WidgetConstants.Property.Name.SINGLE_TELEMETRY_SELECTION);

			// display interval
			Integer displayInterval = Configuration.<Integer>getValue(configuration, WidgetConstants.Page.Name.SETTINGS,
					WidgetConstants.Property.Name.DISPLAY_INTERVAL);

			// refresh rate
			Integer refreshRate = Configuration.<Integer>getValue(configuration, WidgetConstants.Page.Name.SETTINGS,
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
					.withDisplayInterval(displayInterval).withRefreshRate(refreshRate);
		}
	}

	private class ConfigurationRuntimeModel implements Serializable {
		private static final long serialVersionUID = -7244703954967660784L;

		private String deviceUid;
		private String telemetryName;
		private Integer displayInterval = WidgetConstants.DEFAULT_DISPLAY_INTERVAL;
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

		public void setDisplayInterval(Integer displayInterval) {
			if (displayInterval != null)
				this.displayInterval = displayInterval;
		}

		public Integer getDisplayInterval() {
			return displayInterval;
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

		public ConfigurationRuntimeModel withDisplayInterval(Integer displayInterval) {
			setDisplayInterval(displayInterval);

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
		private DeviceType deviceType;
		private TelemetryItemType telemetryItemType;
		private Map<String, String> map;

		public WidgetRuntimeModel(Device device, Map<String, String> map) {
			this.device = device;
			this.map = map;
		}

		public Device getDevice() {
			return device;
		}

		public DeviceType getDeviceType() {
			return deviceType;
		}

		public void setDeviceType(DeviceType deviceType) {
			this.deviceType = deviceType;
		}

		public TelemetryItemType getTelemetryItemType() {
			return telemetryItemType;
		}

		public void setTelemetryItemType(TelemetryItemType telemetryItemType) {
			this.telemetryItemType = telemetryItemType;
		}

		public Map<String, String> getMap() {
			return map;
		}
	}
}