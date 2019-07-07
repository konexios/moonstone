package com.arrow.selene.device.ble.sensor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.Loggable;
import com.arrow.selene.SeleneException;
import com.arrow.selene.device.ble.gatt.Gatt;
import com.arrow.selene.device.sensor.SensorData;
import com.arrow.selene.engine.Utils;

public abstract class BleSensorAbstract<Prop extends SensorProperties, Data extends SensorData<?>> extends Loggable
        implements BleSensor<Prop, Data> {
    private Gatt bluetoothGatt;
    private boolean enabled = true;
    private Prop properties;
    private final String name;
    private List<String> controlledTelemetry;
    protected int payloadSize = 2;

    public BleSensorAbstract(String... controlledTelemetry) {
        name = getClass().getSimpleName();
        this.controlledTelemetry = Arrays.asList(controlledTelemetry);
    }

    public void setBluetoothGatt(Gatt bluetoothGatt) {
        this.bluetoothGatt = bluetoothGatt;
    }

    protected Gatt getBluetoothGatt() {
        return bluetoothGatt;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void enable() {
        enabled = true;
    }

    @Override
    public void disable() {
        enabled = false;
    }

    @Override
    public Prop getProperties() {
        return properties;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<Data> parseData(byte[] data, int offset) {
        // not implemented
        return new ArrayList<Data>();
    }

    public void init(Properties props) {
        String method = "init";
        Validate.notNull(props, "properties is null");
        try {
            Map<String, String> map = new HashMap<>();
            for (String s : props.stringPropertyNames()) {
                if (!s.contains("/")) {
                    map.putIfAbsent(s, props.getProperty(s));
                } else if (s.substring(0, s.indexOf('/')).equals(getClass().getSimpleName())) {
                    map.put(s.substring(s.indexOf('/') + 1), props.getProperty(s));
                }
            }

            // load properties
            properties = createProperties();
            properties.importProperties(map);
            logDebug(method, "loaded properties from file");
        } catch (Throwable t) {
            throw new SeleneException("unable to load sensor properties", t);
        }
    }

    protected abstract Prop createProperties();

    @Override
    public void setProperties(Map<String, String> properties) {
        try {
            this.properties.importProperties(properties);
        } catch (Throwable t) {
            throw new SeleneException("unable to change properties", t);
        }
    }

    @Override
    public byte[] readValue() {
        return Utils.EMPTY_BYTE_ARRAY;
    }

    @Override
    public void configure(Gatt bluetoothGatt) {
        setBluetoothGatt(bluetoothGatt);
        if (getProperties().isEnabled()) {
            enable();
            setPeriod(getProperties().getPeriod());
        } else {
            disable();
        }
    }

    @Override
    public List<String> getControlledTelemetry() {
        return Collections.unmodifiableList(controlledTelemetry);
    }

    @Override
    public void setTelemetry(String value) {
    }

    public int getPayloadSize() {
        return payloadSize;
    }
}
