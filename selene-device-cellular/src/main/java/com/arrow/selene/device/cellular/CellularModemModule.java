package com.arrow.selene.device.cellular;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import org.freedesktop.dbus.Path;
import org.freedesktop.dbus.UInt32;
import org.freedesktop.dbus.Variant;

import com.arrow.selene.device.dbus.ObjectManager;
import com.arrow.selene.device.dbus.Properties;
import com.arrow.selene.engine.DbusUtils;
import com.arrow.selene.engine.DeviceModuleAbstract;
import com.arrow.selene.engine.service.DbusService;

public class CellularModemModule extends
		DeviceModuleAbstract<CellularModemInfo, CellularModemProperties, CellularModemStates, CellularModemData> {

	private DbusService dbusService = DbusService.getInstance();
	private Timer healthCheckTimer;
	private Timer gpsPollingTimer;

	private boolean gpsInitialized = false;

	@Override
	protected void startDevice() {
		String method = "sendCommand";
		super.startDevice();

		try {
			if (dbusService.connect()) {
				logInfo(method, "dbusService connected!");

				Path modemPath = findFirstModem();
				if (modemPath != null) {
					Properties properties = dbusService.getConnection().getRemoteObject(
							DbusModemManager1.MODEM_MANAGER1_IFACE, modemPath.getPath(), Properties.class);
					Map<String, Variant<?>> all = properties.getAll(DbusModem.MODEM_IFACE);
					logInfo(method, "modem properties size: %d", all.size());
					CellularModemInfo info = getInfo();
					info.setManufacturer(DbusUtils.getString(all.get("Manufacturer")));
					info.setModel(DbusUtils.getString(all.get("Model")));
					info.setRevision(DbusUtils.getString(all.get("Revision")));
					info.setImei(DbusUtils.getString(all.get("EquipmentIdentifier")));
					String[] numbers = DbusUtils.getStringArray(all.get("OwnNumbers"));
					if (numbers != null && numbers.length > 0) {
						info.setPhone(numbers[0].trim());
					}

					String simPath = DbusUtils.getString(all.get("Sim"));
					logInfo(method, "found simPath: %s", simPath);
					properties = dbusService.getConnection().getRemoteObject(DbusModemManager1.MODEM_MANAGER1_IFACE,
							simPath, Properties.class);
					all = properties.getAll(DbusSim.SIM_IFACE);
					logInfo(method, "sim properties size: %d", all.size());
					info.setImsi(DbusUtils.getString(all.get("Imsi")));
					info.setIccid(DbusUtils.getString(all.get("SimIdentifier")));

					persistUpdatedDeviceInfo();
				} else {
					logError(method, "modem not found!");
				}
			} else {
				logError(method, "cannot start device, cannot connect to DBus");
			}
		} catch (Exception e) {
			logError(method, "cannot start device", e);
		}
		startHealthCheckTimer();
		startGpsPollingTimer();
	}

	@Override
	public void stop() {
		stopHealthCheckTimer();
		stopGpsPollingTimer();
		super.stop();
	}

	private void startHealthCheckTimer() {
		String method = "startHealthCheckTimer";
		long healthCheckMs = getProperties().getHealthCheckMs();
		if (healthCheckMs > 0) {
			if (healthCheckTimer == null) {
				logInfo(method, "starting healthCheckTimer ...");
				healthCheckTimer = new Timer(true);
				healthCheckTimer.schedule(new HealthCheckTimerTask(), 0, healthCheckMs);
			} else {
				logWarn(method, "healthCheckTimer already started");
			}
		} else {
			logWarn(method, "healthCheckTimer is disabled");
		}
	}

	private void startGpsPollingTimer() {
		String method = "startGpsPollingTimer";
		long gpsPollingMs = getProperties().getGpsPollingMs();
		if (gpsPollingMs > 0) {
			if (gpsPollingTimer == null) {
				logInfo(method, "starting gpsPollingTimer ...");
				gpsPollingTimer = new Timer(true);
				gpsPollingTimer.schedule(new GpsPollingTimerTask(), 0, gpsPollingMs);
			} else {
				logWarn(method, "gpsPollingTimer already started");
			}
		} else {
			logWarn(method, "gpsPollingTimer is disabled");
		}
	}

	private void stopHealthCheckTimer() {
		String method = "stopHealthCheckTimer";
		if (healthCheckTimer != null) {
			logInfo(method, "stopping healthCheckTimer ...");
			healthCheckTimer.cancel();
			healthCheckTimer = null;
		}
	}

	private void stopGpsPollingTimer() {
		String method = "stopGpsPollingTimer";
		if (gpsPollingTimer != null) {
			logInfo(method, "stopping gpsPollingTimer ...");
			gpsPollingTimer.cancel();
			gpsPollingTimer = null;
		}
	}

	private Path findFirstModem() {
		String method = "findFirstModem";
		Path path = null;
		try {
			logInfo(method, "querying modems ...");
			ObjectManager manager = dbusService.getConnection().getRemoteObject(DbusModemManager1.MODEM_MANAGER1_IFACE,
					DbusModemManager1.MODEM_MANAGER1_PATH, ObjectManager.class);
			Map<Path, Map<String, Map<String, Variant<?>>>> objects = manager.getManagedObjects();
			if (objects.size() > 0) {
				path = objects.keySet().iterator().next();
				logInfo(method, "found %d modems, first: %s", objects.size(), path);
			} else {
				logError(method, "no modems found!");
			}
		} catch (Exception e) {
			logError(method, e);
		}
		return path;
	}

	@Override
	protected CellularModemProperties createProperties() {
		return new CellularModemProperties();
	}

	@Override
	protected CellularModemInfo createInfo() {
		return new CellularModemInfo();
	}

	@Override
	protected CellularModemStates createStates() {
		return new CellularModemStates();
	}

	class HealthCheckTimerTask extends TimerTask {
		private AtomicBoolean running = new AtomicBoolean(false);

		@Override
		public void run() {
			String method = "HealthCheckTimerTask";
			if (running.compareAndSet(false, true)) {
				CellularModemDataImpl data = new CellularModemDataImpl();
				try {
					Path modemPath = findFirstModem();
					if (modemPath != null) {
						DbusModemSimple simple = dbusService.getConnection().getRemoteObject(
								DbusModemManager1.MODEM_MANAGER1_IFACE, modemPath.getPath(), DbusModemSimple.class);
						Map<String, Variant<?>> status = simple.getStatus();
						data.setModemPath(modemPath.getPath());
						data.setOperatorCode(DbusUtils.getString(status.get("m3gpp-operator-code")));
						data.setOperatorName(DbusUtils.getString(status.get("m3gpp-operator-name")));
						data.setRegistrationState(DbusUtils.getLong(status.get("m3gpp-registration-state")));
						data.setState(DbusUtils.getLong(status.get("state")));
						Object[] quality = DbusUtils.getStruct(status.get("signal-quality"));
						if (quality != null && quality.length == 2) {
							data.setSignalQuality(Long.parseLong(quality[0].toString()));
							data.setSignalValid(Boolean.parseBoolean(quality[1].toString()));
						}
						logInfo(method, "quality: %d", data.getSignalQuality());

						DbusModemLocation modemLocation = dbusService.getConnection().getRemoteObject(
								DbusModemManager1.MODEM_MANAGER1_IFACE, modemPath.getPath(), DbusModemLocation.class);
						Map<UInt32, Variant<?>> location = modemLocation.getLocation();
						Variant<?> lacCi = location.get(new UInt32(1));
						if (lacCi != null) {
							String lacCiValue = lacCi.getValue().toString();
							logInfo(method, "lacCiValue: %s", lacCiValue);
							String[] tokens = lacCiValue.split(",");
							if (tokens != null && tokens.length == 4) {
								data.setLocationMcc(tokens[0]);
								data.setLocationMnc(tokens[1]);
								data.setLocationLac(tokens[2]);
								data.setLocationCi(tokens[3]);
							} else {
								logError(method, "invalid lacCi information: %s", lacCi.getValue().toString());
							}
						}
					} else {
						data.setError("no modem found");
						logError(method, "modem is possibly disconnected!");
					}
				} catch (Throwable t) {
					logError(method, t);
					data.setError(t.getMessage());
				}
				queueDataForSending(data);
				running.set(false);
			}
		}
	}

	class GpsPollingTimerTask extends TimerTask {
		private AtomicBoolean running = new AtomicBoolean(false);

		@Override
		public void run() {
			String method = "GpsPollingTimerTask";
			if (running.compareAndSet(false, true)) {
				GpsAcpData data = new GpsAcpData();
				try {
					Path modemPath = findFirstModem();
					if (modemPath != null) {
						DbusModem modem = dbusService.getConnection().getRemoteObject(
								DbusModemManager1.MODEM_MANAGER1_IFACE, modemPath.getPath(), DbusModem.class);
						if (!gpsInitialized) {
							try {
								String command = "AT$GPSSLSR=2,3,,,,,1";
								logInfo(method, "sending command: %s", command);
								String response = modem.Command(command, new UInt32(10000L));
								logInfo(method, "---> response: %s", response);
							} catch (Throwable t) {
								logError(method, t);
							}
							gpsInitialized = true;
						}
						String command = "AT$GPSACP";
						logInfo(method, "sending command: %s", command);
						String response = modem.Command(command, new UInt32(10000L));
						logInfo(method, "---> response: %s", response);
						if (response.startsWith("$GPSACP")) {
							data.parseRawData(response.getBytes(StandardCharsets.UTF_8));
							if (data.containGpsData()) {
								logInfo(method, "---> FOUND: lat: %f, lng: %f", data.getLatitude(),
										data.getLongitude());
							} else {
								logInfo(method, "---> GPS data not available");
							}
						}
					} else {
						data.setError("no modem found");
						logError(method, "modem is possibly disconnected!");
					}
				} catch (Throwable t) {
					logError(method, t);
					data.setError(t.getMessage());
				}
				queueDataForSending(data);
				running.set(false);
			}
		}
	}
}
