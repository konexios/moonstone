package moonstone.selene.device.wifi;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import org.freedesktop.dbus.DBusSigHandler;
import org.freedesktop.dbus.Path;
import org.freedesktop.dbus.exceptions.DBusException;

import moonstone.selene.device.dbus.Wireless.AccessPointAdded;
import moonstone.selene.device.dbus.Wireless.AccessPointRemoved;
import moonstone.selene.engine.DeviceModuleAbstract;
import moonstone.selene.engine.service.DbusService;

public class WifiModule extends DeviceModuleAbstract<WifiInfo, WifiProperties, WifiStates, WifiData> {
	private DbusService dbusService = DbusService.getInstance();
	private Path activeAccessPoint;
	private Timer accessPointsPollingTimer;
	private final AccessPointAddedHandler accessPointAddedHandler = new AccessPointAddedHandler();
	private final AccessPointRemovedHandler accessPointRemovedHandler = new AccessPointRemovedHandler();

	@Override
	protected void startDevice() {
		String method = "sendCommand";
		super.startDevice();
		try {
			if (dbusService.connect()) {
				dbusService.addSignalHandler(AccessPointAdded.class, accessPointAddedHandler);
				dbusService.addSignalHandler(AccessPointRemoved.class, accessPointRemovedHandler);
				dbusService.createOrUpdateConnection(getProperties().getSsid(), getProperties().getAuthAlg(),
						getProperties().getKeyMgmt(), getProperties().getPassword());
				if (WifiMode.valueOf(getProperties().getMode()) == WifiMode.ACTIVE) {
					activeAccessPoint = dbusService.activateWifiConnection(getProperties().getDeviceName(),
							getProperties().getSsid());
				}
				startAccessPointsPollingTimer(dbusService.findWifiDevice(getProperties().getDeviceName()));
			} else {
				logError(method, "cannot start device, cannot connect to DBus");
			}
		} catch (DBusException e) {
			logError(method, "cannot start device", e);
		}
	}

	@Override
	public void stop() {
		String method = "stop";
		super.stop();
		try {
			stopAccessPointsPollingTimer();
			dbusService.removeSignalHandler(AccessPointAdded.class, accessPointAddedHandler);
			dbusService.removeSignalHandler(AccessPointRemoved.class, accessPointRemovedHandler);
			if (WifiMode.valueOf(getProperties().getMode()) == WifiMode.ACTIVE) {
				dbusService.deactivateConnection(getProperties().getDeviceName());
			}
		} catch (DBusException e) {
			logError(method, "cannot stop device", e);
		}
	}

	@Override
	protected WifiProperties createProperties() {
		return new WifiProperties();
	}

	@Override
	protected WifiInfo createInfo() {
		return new WifiInfo();
	}

	@Override
	protected WifiStates createStates() {
		return new WifiStates();
	}

	@Override
	public void notifyPropertiesChanged(Map<String, String> properties) {
		String method = "notifyPropertiesChanged";
		super.notifyPropertiesChanged(properties);
		try {
			dbusService.createOrUpdateConnection(getProperties().getSsid(), getProperties().getAuthAlg(),
					getProperties().getKeyMgmt(), getProperties().getPassword());
		} catch (DBusException e) {
			logError(method, e);
		}
	}

	private void startAccessPointsPollingTimer(Path devicePath) {
		String method = "startAccessPointsPollingTimer";
		long accessPointsPollingMs = getProperties().getAccessPointsPollingMs();
		if (accessPointsPollingMs > 0L) {
			if (accessPointsPollingTimer == null) {
				logInfo(method, "starting accessPointsPollingTimer ...");
				accessPointsPollingTimer = new Timer("AccessPointsPollingTimer", true);
				accessPointsPollingTimer.schedule(new AccessPointsPollingTimerTask(devicePath), 0L,
						accessPointsPollingMs);
			} else {
				logWarn(method, "accessPointsPollingTimer already started");
			}
		} else {
			logWarn(method, "accessPointsPollingTimer is disabled");
		}
	}

	private void stopAccessPointsPollingTimer() {
		String method = "stopAccessPointsPollingTimer";
		if (accessPointsPollingTimer != null) {
			logInfo(method, "stopping accessPointsPollingTimer ...");
			accessPointsPollingTimer.cancel();
			accessPointsPollingTimer = null;
		}
	}

	private class AccessPointAddedHandler implements DBusSigHandler<AccessPointAdded> {
		@Override
		public void handle(AccessPointAdded accessPointAdded) {
			String method = "AccessPointAddedHandler.handle";
			try {
				String accessPointSsid = dbusService.getAccessPointSsid(accessPointAdded.getAccessPoint().getPath());
				logInfo(method, "access point '%s' appeared", accessPointSsid);
				if (WifiMode.valueOf(getProperties().getMode()) == WifiMode.ACTIVE
						&& Objects.equals(accessPointSsid, getProperties().getSsid())) {
					activeAccessPoint = dbusService.activateWifiConnection(getProperties().getDeviceName(),
							getProperties().getSsid());
				}
			} catch (DBusException e) {
				logWarn(method, "signal handling error");
			}
		}
	}

	private class AccessPointRemovedHandler implements DBusSigHandler<AccessPointRemoved> {
		@Override
		public void handle(AccessPointRemoved accessPointRemoved) {
			String method = "AccessPointRemovedHandler.handle";
			Path path = accessPointRemoved.getAccessPoint();
			logInfo(method, "access point '%s' disappeared", path.getPath());
			if (Objects.equals(path, activeAccessPoint)) {
				activeAccessPoint = null;
				logInfo(method, "disconnected from WiFi network");
			}
		}
	}

	private class AccessPointsPollingTimerTask extends TimerTask {
		private Path devicePath;
		private AtomicBoolean running = new AtomicBoolean(false);

		public AccessPointsPollingTimerTask(Path devicePath) {
			this.devicePath = devicePath;
		}

		@Override
		public void run() {
			String method = "GpsPollingTimerTask";
			if (running.compareAndSet(false, true)) {
				AccessPointData data = new AccessPointData();
				List<Path> accessPoints = Collections.emptyList();
				try {
					accessPoints = dbusService.getAccessPoints(devicePath);
				} catch (DBusException e) {
					logError(method, e);
					data.setError(e.getMessage());
					queueDataForSending(data);
				}
				for (Path path : accessPoints) {
					try {
						data.parseData(dbusService.getAccessPointProperties(path.getPath()));
					} catch (DBusException e) {
						logError(method, e);
						data.setError(e.getMessage());
					}
					queueDataForSending(data, true);
				}
				running.set(false);
			}
		}
	}
}
