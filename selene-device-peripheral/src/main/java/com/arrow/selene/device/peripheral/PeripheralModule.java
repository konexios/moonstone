package com.arrow.selene.device.peripheral;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Properties;

import org.apache.commons.lang3.Validate;

import com.arrow.acn.client.utils.Utils;
import com.arrow.acs.JsonUtils;
import com.arrow.selene.SeleneException;
import com.arrow.selene.device.sensor.SensorData;
import com.arrow.selene.device.sensor.SensorDataImpl;
import com.arrow.selene.engine.DeviceModuleAbstract;
import com.arrow.selene.engine.EngineConstants;
import com.arrow.selene.engine.state.State;
import com.arrow.selene.engine.state.StateChangeHandler;
import com.arrow.selene.engine.state.StateUpdate;
import com.arrow.selene.model.StatusModel;
import com.fasterxml.jackson.core.type.TypeReference;

public class PeripheralModule
        extends DeviceModuleAbstract<PeripheralInfo, PeripheralProperties, PeripheralStates, SensorDataImpl<?>>
        implements StateChangeHandler<PeripheralStates> {
    private static final String DEVICE_NAME_PARAM_NAME = "name";
    private static final String ADD_DEVICE_COMMAND = "addDevice";
    private static final String REMOVE_DEVICE_COMMAND = "removeDevice";
    private Map<String, ControlledPeripheralDevice> controlledDevices = new HashMap<>();
    private Map<String, ReportingPeripheralDevice<? extends SensorData<?>>> reportingDevices = new HashMap<>();
    private Thread pollingThread;
    private static TypeReference<Map<String, String>> mapTypeRef;

    public PeripheralModule() {
        handlers = Collections.singletonList(this);
    }

    @Override
    protected void startDevice() {
        String method = "startDevice";
        super.startDevice();
        logInfo(method, "starting health check thread ...");
        pollingThread = new Thread(new PollingTask(), "pollingThread");
        pollingThread.start();
        logInfo(method, "started");
    }

    @Override
    public void init(Properties props) {
        super.init(props);
        loadPeripheralDevices();
    }

    private void loadPeripheralDevices() {
        String method = "loadPeripheralDevices";
        for (Entry<String, Map<String, String>> entry : getInfo().getPeriphery().getDevices().entrySet()) {
            Map<String, String> properties = entry.getValue();
            try {
                startDevice(createDevice(entry.getKey(), properties), properties);
            } catch (SeleneException e) {
                logError(method, e);
            }
        }
    }

    private PeripheralDevice createDevice(String name, Map<String, String> properties) {
        String method = "createDevice";
        String deviceClass = properties.get(EngineConstants.DEVICE_CLASS);
        if (deviceClass != null) {
            try {
                logInfo(method, "instantiating device %s of class: %s", name, deviceClass);
                Object instance = Class.forName(deviceClass).getConstructor().newInstance();
                boolean valid = false;
                if (instance instanceof ControlledPeripheralDevice) {
                    controlledDevices.put(name, (ControlledPeripheralDevice) instance);
                    valid = true;
                }
                if (instance instanceof ReportingPeripheralDevice) {
                    reportingDevices.put(name, (ReportingPeripheralDevice<?>) instance);
                    valid = true;
                }
                if (valid) {
                    return (PeripheralDevice) instance;
                } else {
                    throw new SeleneException("Error: device class is not of type PeripheralDevice");
                }
            } catch (ReflectiveOperationException e) {
                throw new SeleneException("error instantiating class", e);
            }
        } else {
            throw new SeleneException(String.format("skipping device %s: required '%s' property not found", name,
                    EngineConstants.DEVICE_CLASS));
        }
    }

    @Override
    public void stop() {
        super.stop();
        Utils.shutdownThread(pollingThread);
        for (ControlledPeripheralDevice device : controlledDevices.values()) {
            device.stop();
        }
        for (ReportingPeripheralDevice<? extends SensorData<?>> device : reportingDevices.values()) {
            device.stop();
        }
    }

    @Override
    protected PeripheralProperties createProperties() {
        return new PeripheralProperties();
    }

    @Override
    protected PeripheralInfo createInfo() {
        return new PeripheralInfo();
    }

    @Override
    protected PeripheralStates createStates() {
        return new PeripheralStates();
    }

    @Override
    public StatusModel performCommand(byte... bytes) {
        String method = "performCommand";
        super.performCommand(bytes);
        Map<String, String> params = JsonUtils.fromJsonBytes(bytes, getMapTypeRef());
        String command = params.get("command");
        StatusModel result = StatusModel.OK;
        Map<String, String> payload = JsonUtils.fromJson(params.get("payload"), getMapTypeRef());
        String deviceName = payload.get(DEVICE_NAME_PARAM_NAME);
        if (deviceName == null) {
            logWarn(method, "required 'name' field not fount");
            result.withStatus("WARNING").withMessage("required 'name' field not fount");
        } else {
            if (Objects.equals(command, ADD_DEVICE_COMMAND)) {
                startDevice(createDevice(deviceName, payload), payload);
                getInfo().getPeriphery().getDevices().put(deviceName, payload);
                persistUpdatedDeviceInfo();
            } else if (Objects.equals(command, REMOVE_DEVICE_COMMAND)) {
                stopDevice(controlledDevices.remove(deviceName));
                stopDevice(reportingDevices.remove(deviceName));
                if (getInfo().getPeriphery().getDevices().remove(deviceName) != null) {
                    persistUpdatedDeviceInfo();
                }
            }
        }
        return result;
    }

    private static void startDevice(PeripheralDevice device, Map<String, String> properties) {
        Validate.notNull(device, "device is null");
        device.init(properties);
        device.start();
    }

    private static void stopDevice(PeripheralDevice device) {
        if (device != null) {
            device.stop();
        }
    }

    private class PollingTask implements Runnable {
        @Override
        public void run() {
            List<SensorData<?>> data = new ArrayList<>();
            Map<String, String> states = new HashMap<>();
            while (!isShuttingDown()) {
                data.clear();
                for (ReportingPeripheralDevice<? extends SensorData<?>> value : reportingDevices.values()) {
                    data.addAll(value.getData(states));
                }
                if (!data.isEmpty()) {
                    queueDataForSending(new SensorDataImpl<>(data));
                }
                Utils.sleep(getProperties().getMaxPollingIntervalMs());
                queueStatesForSending(new StateUpdate().withStates(states));
            }
        }
    }

    @Override
    public void handle(PeripheralStates states, Map<String, State> statesMap) {
        controlledDevices.values().forEach(device -> device.changeState(statesMap));
    }

    private static TypeReference<Map<String, String>> getMapTypeRef() {
        return mapTypeRef != null ? mapTypeRef : (mapTypeRef = new TypeReference<Map<String, String>>() {
        });
    }
}
