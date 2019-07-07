package com.arrow.widget;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import com.arrow.acn.client.model.TelemetryItemType;
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
import com.arrow.widget.model.DeviceCubeModel;
import com.arrow.widget.provider.DeviceDataProvider;
import com.arrow.widget.provider.DeviceTypeDataProvider;
import com.arrow.widget.provider.LastTelemetryItemDataProvider;

@Small(width = 1, height = 1)
@Medium(width = 2, height = 1)
@Large(width = 3, height = 1)
@XtraLarge(width = 4, height = 1)
@Widget(directive = "device-acceleration-cube-widget", name = "Device Acceleration Cube Widget", description = "A widget that shows the acceleration of the device via a 3D cube.")
public class DeviceAccelerationCubeWidget extends ScheduledWidgetAbstract {

	@DataProvider
	private DeviceTypeDataProvider deviceTypeDataProvider;
	@DataProvider
	private DeviceDataProvider deviceDataProvider;
	@DataProvider
	private LastTelemetryItemDataProvider lastTelemetryItemDataProvider;
	@TopicProvider("/widget-data")
	private SimpleTopicProvider cubeTopicProvider;

	// runtime
	private ConfigurationRuntimeModel configurationRuntimeModel;
	private Device device;

	public DeviceAccelerationCubeWidget() {
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
					.withName(WidgetConstants.Configuration.Name.DEVICE_ACCELERATION_CUBE_WIDGET)
					.withLabel(WidgetConstants.Configuration.Label.DEVICE_ACCELERATION_CUBE_WIDGET).setChangedPage(0)
					.setCurrentPage(0);
			// @formatter:on

			Page singleDevicePage = new Page().withName(WidgetConstants.Page.Name.SINGLE_DEVICE_SELECTION)
					.withLabel(WidgetConstants.Page.Label.SINGLE_DEVICE_SELECTION);

			singleDevicePage.addProperty(WidgetConfigurationHelper.buildSingleDeviceSelectionPageProperty(devices,
					configurationRuntimeModel.getDeviceUid()));
			configuration.addPage(singleDevicePage);

			// telemetry selection page (stub)
			configuration.addPage(new Page().withName(WidgetConstants.Page.Name.SINGLE_TELEMETRY_SELECTION)
					.withLabel(WidgetConstants.Page.Label.SINGLE_TELEMETRY_SELECTION));
		} else {
			configuration = WidgetConfigurationHelper.buildErrorPageConfiguration(
					WidgetConstants.Configuration.Name.DEVICE_ACCELERATION_CUBE_WIDGET,
					WidgetConstants.Configuration.Label.DEVICE_ACCELERATION_CUBE_WIDGET,
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
			if (deviceTelemetry.getType().equals(TelemetryItemType.FloatCube))
				deviceTelemetries.add(deviceTelemetry);

		if (deviceTelemetries.isEmpty()) {
			configuration.getPage(WidgetConstants.Page.Name.SINGLE_DEVICE_SELECTION)
					.setError("No Cube telemetries defined for selected device");
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
			// lookup device
			if (!StringUtils.isEmpty(configurationRuntimeModel.getDeviceUid()))
				device = deviceDataProvider.findDeviceByUid(configurationRuntimeModel.getDeviceUid());

			if (device == null) {
				onErrorState();

				return;
			}

			onRunningState();
		}
	}

	@Override
	public void run() {
		String method = "run";
		logInfo(method, "...");

		addScheduledWorker(new ScheduledWorker(), 0, 250, TimeUnit.MILLISECONDS);
	}

	private DeviceCubeModel loadWidgetData() {

		String method = "loadWidgetData";
		logInfo(method, "...");

		long threshold = 30 * 1000;
		DeviceCubeModel model = new DeviceCubeModel();

		try {
			LastTelemetryItemSearchParams params = new LastTelemetryItemSearchParams();
			params.addDeviceIds(device.getId());
			params.addNames(configurationRuntimeModel.getTelemetryName());

			LastTelemetryItem lastTelemetryItem = lastTelemetryItemDataProvider.findLastTelemetryItem(params);
			if (lastTelemetryItem != null
					&& (System.currentTimeMillis() - lastTelemetryItem.getTimestamp() < threshold))
				model.setCubeValues(lastTelemetryItem.value().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (model.getCubeValues() == null)
			model.setCubeValues("0.0|0.0|0.0");

		return model;
	}

	private class ScheduledWorker implements Runnable {
		@Override
		public void run() {
			String method = "run";
			logInfo(method, "...");
			logDebug(method, "state: %s", getState());

			try {
				DeviceCubeModel deviceCubeModel = loadWidgetData();
				if (deviceCubeModel != null)
					sendMessage(cubeTopicProvider, new WidgetData().withState(getState()).withData(deviceCubeModel));
			} catch (Throwable t) {
				logError(method, "error refreshing last location", t);
			}
		}
	}

	private void populateConfigurationRuntimeModel(Configuration configuration) {
		String method = "populateConfigurationRuntimeModel";
		logInfo(method, "...");

		if (configuration != null) {
			// uid
			String deviceUID = Configuration.<String>getValue(configuration,
					WidgetConstants.Page.Name.SINGLE_DEVICE_SELECTION, WidgetConstants.Property.Name.DEVICE_UID);

			// telemetry name
			String telemetryName = Configuration.<String>getValue(configuration,
					WidgetConstants.Page.Name.SINGLE_TELEMETRY_SELECTION,
					WidgetConstants.Property.Name.SINGLE_TELEMETRY_SELECTION);

			logDebug(method, "deviceUID: %s, telemetryName: %s", deviceUID, telemetryName);

			// populate configuration runtime model
			configurationRuntimeModel.withDeviceUid(deviceUID).withTelemetryName(telemetryName);
		}
	}

	private class ConfigurationRuntimeModel implements Serializable {
		private static final long serialVersionUID = 3086737384109005111L;

		private String deviceUid;
		private String telemetryName;

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

		public ConfigurationRuntimeModel withDeviceUid(String deviceUid) {
			setDeviceUid(deviceUid);

			return this;
		}

		public ConfigurationRuntimeModel withTelemetryName(String telemetryName) {
			setTelemetryName(telemetryName);

			return this;
		}
	}
}