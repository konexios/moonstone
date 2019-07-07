package com.arrow.widget;

import java.io.Serializable;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import com.arrow.dashboard.messaging.SimpleTopicProvider;
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
import com.arrow.pegasus.data.location.LastLocation;
import com.arrow.pegasus.data.location.LocationObjectType;
import com.arrow.pegasus.repo.LastLocationSearchParams;
import com.arrow.widget.configuration.WidgetConfigurationHelper;
import com.arrow.widget.model.DeviceLastLocationModel;
import com.arrow.widget.provider.DeviceDataProvider;
import com.arrow.widget.provider.LastLocationDataProvider;

@Small(width = 1, height = 1)
@Medium(width = 2, height = 1)
@Large(width = 3, height = 1)
@XtraLarge(width = 4, height = 1)
@Widget(directive = "device-last-location-widget", name = "Device Last Location Widget", description = "A widget that shows the latest location for a device.")
public class DeviceLastLocationWidget extends LastLocationWidgetAbstract {

	@DataProvider
	private DeviceDataProvider deviceDataProvider;
	@DataProvider
	private LastLocationDataProvider lastLocationDataProvider;
	@TopicProvider("/widget-data")
	private SimpleTopicProvider lastLocationTopicProvider;

	// runtime
	private ConfigurationRuntimeModel configurationRuntimeModel;
	private Device device;

	public DeviceLastLocationWidget() {
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
			        WidgetConstants.Configuration.Name.DEVICE_LAST_LOCATION_WIDGET,
			        WidgetConstants.Configuration.Label.DEVICE_LAST_LOCATION_WIDGET, devices,
			        configurationRuntimeModel.getDeviceUid());

			// settings (stub)
			configuration.addPage(new Page().withName(WidgetConstants.Page.Name.SETTINGS)
			        .withLabel(WidgetConstants.Page.Label.SETTINGS));

			// populate
			populateConfigurationRuntimeModel(configuration);
		} else {
			configuration = WidgetConfigurationHelper.buildErrorPageConfiguration(
					WidgetConstants.Configuration.Name.DEVICE_LAST_LOCATION_WIDGET,
					WidgetConstants.Configuration.Label.DEVICE_LAST_LOCATION_WIDGET,
					WidgetConstants.Page.Label.SINGLE_DEVICE_SELECTION, "No available devices");
		}

		return configuration;
	}

	@ConfigurationPageRequest(page = 0)
	public Configuration processDeviceSelection(Configuration configuration) {
		String method = "processDeviceSelection";
		logInfo(method, "...");

		Page settingsPage = configuration.getPage(WidgetConstants.Page.Name.SETTINGS);

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

		addScheduledWorker(new ScheduledWorker(), 0, configurationRuntimeModel.getRefreshRate(), TimeUnit.SECONDS);
	}

	private DeviceLastLocationModel loadWidgetData() {
		String method = "loadWidgetData";
		logInfo(method, "...");

		LastLocation lastLocation = null;
		try {
			LastLocationSearchParams params = new LastLocationSearchParams();
			params.addObjectIds(device.getId());
			params.setObjectTypes(EnumSet.of(LocationObjectType.DEVICE));

			List<LastLocation> lastLocations = lastLocationDataProvider.findLastLocations(params);

			lastLocation = null;
			if (lastLocations != null && !lastLocations.isEmpty())
				lastLocation = lastLocations.get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (lastLocation == null)
			return null;

		// @formatter:off
		return new DeviceLastLocationModel()
				.withName(device.getName())
		        .withId(lastLocation.getObjectId())
		        .withLatitude(lastLocation.getLatitude().toString())
		        .withLongitude(lastLocation.getLongitude().toString())
		        .withTimestamp(lastLocation.getTimestamp());
		// @formatter:on
	}

	private class ScheduledWorker implements Runnable {
		@Override
		public void run() {
			String method = "run";
			logInfo(method, "...");
			logDebug(method, "state: %s", getState());

			try {
				DeviceLastLocationModel lastLocation = loadWidgetData();

				WidgetData widgetData = new WidgetData().withState(getState());
				if (lastLocation != null)
					widgetData.withData(lastLocation);

				sendMessage(lastLocationTopicProvider, widgetData);
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
			String deviceUid = Configuration.<String> getValue(configuration,
			        WidgetConstants.Page.Name.SINGLE_DEVICE_SELECTION, WidgetConstants.Property.Name.DEVICE_UID);

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

			logDebug(method, "deviceUID: %s, refreshRate: %s", deviceUid, refreshRate);

			// populate configuration runtime model
			configurationRuntimeModel.withDeviceUid(deviceUid).withRefreshRate(refreshRate);
		}
	}

	private class ConfigurationRuntimeModel implements Serializable {
		private static final long serialVersionUID = -7244703954967660784L;

		private String deviceUid;
		private Integer refreshRate = WidgetConstants.DEFAULT_REFRESH_RATE;

		public ConfigurationRuntimeModel() {
		}

		public String getDeviceUid() {
			return deviceUid;
		}

		public void setDeviceUid(String deviceUid) {
			this.deviceUid = deviceUid;
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

		public ConfigurationRuntimeModel withRefreshRate(Integer refreshRate) {
			setRefreshRate(refreshRate);

			return this;
		}
	}
}