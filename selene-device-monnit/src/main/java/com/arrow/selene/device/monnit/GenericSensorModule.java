package com.arrow.selene.device.monnit;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;

import com.arrow.acn.client.utils.Utils;
import com.arrow.selene.engine.DeviceModuleAbstract;
import com.arrow.selene.engine.ModuleState;
import com.arrow.selene.engine.state.State;
import com.arrow.selene.engine.state.StateChangeHandler;
import com.arrow.selene.engine.state.StateUpdate;
import com.monnit.mine.BaseApplication.applicationclasses.TemperatureBase;
import com.monnit.mine.MonnitMineAPI.Sensor;
import com.monnit.mine.MonnitMineAPI.SensorMessage;
import com.monnit.mine.MonnitMineAPI.enums.eSensorApplication;

public class GenericSensorModule
        extends DeviceModuleAbstract<GenericSensorInfo, GenericSensorProperties, GenericSensorStates, GenericSensorData>
        implements StateChangeHandler<GenericSensorStates> {

    private long sensorId;
    private MonnitModule monnitModule;

    public GenericSensorModule(MonnitModule monnitModule, long sensorId, eSensorApplication application) {
        this.monnitModule = monnitModule;
        this.sensorId = sensorId;
        String name = String.format("generic-sensor-%s-%06d", application, sensorId);
        getInfo().setName(name);
        getInfo().setUid(name.toLowerCase(Locale.getDefault()));
        getInfo().setSensorId(sensorId);
        getInfo().setProfileId(application.Value());
        getInfo().setProfileName(application.name());
        handlers = Collections.singletonList(this);
    }

    @Override
    protected GenericSensorProperties createProperties() {
        return new GenericSensorProperties();
    }

    @Override
    protected GenericSensorInfo createInfo() {
        return new GenericSensorInfo();
    }

    @Override
    protected GenericSensorStates createStates() {
        return new GenericSensorStates();
    }

    public void processSensorMessage(Sensor sensor, SensorMessage sensorMessage) {
        String method = "processSensorMessage";
        while (getState() != ModuleState.STARTED) {
            logInfo(method, "waiting for module to be ready ...");
            Utils.sleep(1000L);
        }
        
        GenericSensorData genericSensorData = new GenericSensorData(sensorMessage);
        getService().submit(() -> queueDataForSending(genericSensorData));
        logInfo(method, "Data queued for sending (%s) ", genericSensorData.toString());
        
        if (getStates().populateFrom(sensor)) {
            logInfo(method, "publishing updated states for sensor: %s ...", sensor.getSensorID());
            StateUpdate stateUpdate = new StateUpdate().withStates(getStates().exportStates());
            queueStatesForSending(stateUpdate);
            logInfo(method, "States queued for sending (%s)", stateUpdate.toString());
        } else {
        	logInfo(method, "There are no updated states for sensor %s", sensor.toString());
        }
    }

    public void publishTelemetry(SensorMessage sensorMessage) {
        while (getState() != ModuleState.STARTED) {
            Utils.sleep(1000L);
        }
    }

    @Override
    public void handle(GenericSensorStates states, Map<String, State> statesMap) {
        String method = "handle";
        State linkInterval = statesMap.get("linkInterval");
        if (linkInterval != null) {
            try {
                logInfo(method, "UpdateLinkInterval: %s", linkInterval.getValue());
                Sensor sensor = monnitModule.getServer().FindSensor(sensorId);
                if (sensor != null) {
                    sensor.UpdateLinkInterval(new Integer(linkInterval.getValue()));
                } else {
                    logError(method, "sensor not found in mine server: %d", sensorId);
                }
            } catch (Exception e) {
                logError(method, e);
            }
        }
        State recoveryCount = statesMap.get("recoveryCount");
        if (recoveryCount != null) {
            try {
                logInfo(method, "UpdateRecoveryCount: %s", recoveryCount.getValue());
                Sensor sensor = monnitModule.getServer().FindSensor(sensorId);
                if (sensor != null) {
                    sensor.UpdateRecoveryCount(new Integer(recoveryCount.getValue()));
                } else {
                    logError(method, "sensor not found in mine server: %d", sensorId);
                }
            } catch (Exception e) {
                logError(method, e);
            }

        }
        State reportInterval = statesMap.get("reportInterval");
        if (reportInterval != null) {
            try {
                logInfo(method, "UpdateReportInterval: %s", reportInterval.getValue());
                Sensor sensor = monnitModule.getServer().FindSensor(sensorId);
                if (sensor != null) {
                    sensor.UpdateReportInterval(new Double(reportInterval.getValue()));

                    if (sensor.getMonnitApplication().equals(eSensorApplication.Temperature)) {
                        logInfo(method, "-----> special handling for temperature sensor ...");
                        TemperatureBase.UpdateSensorHeartBeat(sensor, Double.parseDouble(reportInterval.getValue()),
                                true);
                    }

                } else {
                    logError(method, "sensor not found in mine server: %d", sensorId);
                }
            } catch (Exception e) {
                logError(method, e);
            }

        }
        State retryCount = statesMap.get("retryCount");
        if (retryCount != null) {
            try {
                logInfo(method, "UpdateRetryCount: %s", retryCount.getValue());
                Sensor sensor = monnitModule.getServer().FindSensor(sensorId);
                if (sensor != null) {
                    sensor.UpdateRetryCount(new Integer(retryCount.getValue()));
                } else {
                    logError(method, "sensor not found in mine server: %d", sensorId);
                }
            } catch (Exception e) {
                logError(method, e);
            }
        }
    }
}
