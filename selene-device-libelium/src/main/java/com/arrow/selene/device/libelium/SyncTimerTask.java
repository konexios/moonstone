package com.arrow.selene.device.libelium;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.Loggable;
import com.arrow.selene.dao.DeviceDao;
import com.arrow.selene.data.Device;
import com.arrow.selene.device.libelium.data.SensorParser;
import com.arrow.selene.device.self.SelfModule;
import com.arrow.selene.engine.Utils;
import com.arrow.selene.engine.service.DeviceService;
import com.arrow.selene.engine.service.ModuleService;

public class SyncTimerTask extends TimerTask {
	private final Loggable logger = new Loggable();
	private final MeshliumModule meshlium;
	private final MeshliumProperties properties;
	private AtomicBoolean running = new AtomicBoolean(false);
	private Map<String, WaspmoteModule> moduleMap = new HashMap<>();

	// shared objects
	private String pendingRecordsQuery = null;
	private SensorParser record = null;

	public SyncTimerTask(MeshliumModule module) {
		this.meshlium = module;
		this.properties = meshlium.getProperties();

		String format = "select id, id_wasp, sensor, value, timestamp, sync from %s where sync & %d = false order by timestamp desc limit %d for update";
		this.pendingRecordsQuery = String.format(format, meshlium.getDbConnectionInfo().getParser_table(),
		        properties.getMask(), properties.getLimit());
		this.record = new SensorParser();
	}

	@Override
	public void run() {
		String method = "SyncTimerTask";
		if (running.compareAndSet(false, true)) {
			Connection connection = null;
			try {
				logger.logInfo(method, "connecting to database ...");
				connection = DriverManager.getConnection(
				        meshlium.getDbConnectionInfo().buildConnectionString(properties.getConnectionString()));
				int counter = processPendingRecords(connection);
				logger.logInfo(method, "processing complete: records = %d", counter);
			} catch (Throwable t) {
				logger.logError(method, "syncTimer ERROR", t);
			} finally {
				Utils.close(connection, null, null);
			}
			running.set(false);
		}
	}

	private int processPendingRecords(Connection connection) throws Exception {
		String method = "queryPendingRecords";
		Statement stmt = null;
		ResultSet rset = null;
		int counter = 0;
		try {
			stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rset = stmt.executeQuery(pendingRecordsQuery);
			while (rset.next()) {
				record.setId(rset.getInt("id"));
				record.setIdWasp(rset.getString("id_wasp"));
				record.setSensor(rset.getString("sensor"));
				record.setValue(rset.getString("value"));
				record.setTimestamp(rset.getTimestamp("timestamp"));
				record.setSync(rset.getLong("sync"));

				// process
				processRecord(record);

				// mark complete
				rset.updateLong("sync", record.getSync() | properties.getMask());
				rset.updateRow();
				logger.logDebug(method, "marked record complete: %d", record.getId());

				counter++;
			}
		} finally {
			Utils.close(null, stmt, rset);
		}
		return counter;
	}

	private void processRecord(SensorParser data) {
		Validate.notNull(data, "data is null");
		String method = "checkAndCreateWaspmote";
		String uid = buildWaspmoteUid(data.getIdWasp());
		WaspmoteModule waspmote = moduleMap.get(uid);
		boolean started = false;
		if (waspmote == null) {
			Properties props = new Properties();
			Device existing = DeviceDao.getInstance().findByTypeAndUid(WaspmoteInfo.DEFAULT_DEVICE_TYPE, uid);
			if (existing == null) {
				logger.logInfo(method, "creating new waspmote: %s", uid);
				waspmote = new WaspmoteModuleImpl(meshlium);
				props.setProperty("name", data.getIdWasp());
				props.setProperty("uid", uid);
			} else {
				waspmote = (WaspmoteModule) ModuleService.getInstance().findDevice(existing.getHid());
				if (waspmote == null) {
					logger.logWarn(method, "waspmote not found for uid: %s, loading from DB ...", uid);
					waspmote = new WaspmoteModuleImpl(meshlium);
					DeviceService.getInstance().loadDeviceProperties(waspmote, existing);
				} else {
					started = true;
				}
			}
			if (!started) {
				logger.logInfo(method, "initializing waspmote: %s", uid);
				waspmote.init(props);

				logger.logInfo(method, "registering waspmote: %s", uid);
				ModuleService.getInstance().registerModule(waspmote);

				logger.logInfo(method, "starting waspmote: %s", uid);
				ModuleService.getInstance().startModule(waspmote);
			}
			moduleMap.put(uid, waspmote);
		}
		waspmote.processSensorData(data);
	}

	private String buildWaspmoteUid(String id) {
		id = id.toLowerCase().replace(" ", "-");
		return String.format("%s-%s", SelfModule.getInstance().getGateway().getUid(), id);
	}
}
