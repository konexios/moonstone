package com.arrow.selene.device.libelium;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import com.arrow.selene.SeleneException;
import com.arrow.selene.device.libelium.data.Sensor;
import com.arrow.selene.engine.DeviceModuleAbstract;
import com.arrow.selene.engine.Utils;

public class MeshliumModuleImpl extends
        DeviceModuleAbstract<MeshliumInfo, MeshliumProperties, MeshliumStates, MeshliumData> implements MeshliumModule {
    private static final String MYSQL_JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    private DbConnectionInfo dbConnectionInfo;
    private Timer syncTimer;
    private Map<String, Sensor> sensorMap = new HashMap<>();

    @Override
    public void startDevice() {
        super.startDevice();
        String method = "MeshliumModuleImpl.sendCommand";
        try {
            logInfo(method, "loading JDBC driver ...");
            Class.forName(MYSQL_JDBC_DRIVER).getDeclaredConstructor().newInstance();

            logInfo(method, "loading DB configuration file ...");
            dbConnectionInfo = new DbConnectionInfo()
                    .populateFrom(Utils.readIniFile(getProperties().getDbConfigFile(), "DB", new HashMap<>()));

            logInfo(method, "loading plugin configuration file ...");
            getProperties()
                    .populateFrom(Utils.readIniFile(getProperties().getPluginConfigFile(), "ARROW", new HashMap<>()));

            startSyncTimer();
        } catch (SeleneException e) {
            throw e;
        } catch (Throwable t) {
            throw new SeleneException("Unable to start meshlium module", t);
        }
    }

    @Override
    public void stop() {
        super.stop();
        stopSyncTimer();
    }

    @Override
    public Sensor getSensorInfo(String idAscii) {
        String method = "getSensorInfo";
        Sensor sensor = sensorMap.get(idAscii);
        if (sensor == null) {
            Connection conn = null;
            PreparedStatement stmt = null;
            ResultSet rset = null;
            try {
                logInfo(method, "getting connection ...");
                conn = DriverManager
                        .getConnection(dbConnectionInfo.buildConnectionString(getProperties().getConnectionString()));
                String query = "select * from sensors where id_ascii = ?";
                stmt = conn.prepareStatement(query);
                stmt.setString(1, idAscii);
                rset = stmt.executeQuery();
                if (rset.next()) {
                    sensor = new Sensor();
                    sensor.setId(rset.getInt("id"));
                    sensor.setName(rset.getString("name"));
                    sensor.setDescription(rset.getString("description"));
                    sensor.setIdAscii(rset.getString("id_ascii"));
                    sensor.setUnits(rset.getString("units"));
                    sensor.setValue(rset.getInt("value"));
                    sensor.setVis(rset.getInt("vis"));
                    sensor.setFields(rset.getInt("fields"));
                    sensorMap.put(idAscii, sensor);
                }
                logInfo(method, "loaded sensor: %s", idAscii);
            } catch (Throwable t) {
                logError(method, "getSensorInfo ERROR", t);
            } finally {
                Utils.close(conn, stmt, rset);
            }
        }
        return sensor;
    }

    private void startSyncTimer() {
        String method = "startSyncTimer";
        stopSyncTimer();
        logInfo(method, "starting sync timer ...");
        syncTimer = new Timer();
        syncTimer.scheduleAtFixedRate(new SyncTimerTask(this), 0, getProperties().getSyncIntervalMs());
    }

    private void stopSyncTimer() {
        String method = "stopTimer";
        if (syncTimer != null) {
            logInfo(method, "stopping sync timer ...");
            syncTimer.cancel();
            syncTimer = null;
        }
    }

    @Override
    public DbConnectionInfo getDbConnectionInfo() {
        return dbConnectionInfo;
    }

    @Override
    protected MeshliumProperties createProperties() {
        return new MeshliumProperties();
    }

    @Override
    protected MeshliumInfo createInfo() {
        return new MeshliumInfo();
    }

    @Override
    protected MeshliumStates createStates() {
        return new MeshliumStates();
    }
}
