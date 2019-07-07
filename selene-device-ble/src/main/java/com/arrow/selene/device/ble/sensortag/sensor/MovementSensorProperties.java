package com.arrow.selene.device.ble.sensortag.sensor;

import com.arrow.selene.device.ble.sensor.SensorProperties;

public class MovementSensorProperties extends SensorProperties {
    private static final long serialVersionUID = 4817747887158031681L;
    private boolean accelerometerEnabled;
    private boolean magnetometerEnabled;
    private boolean gyroscopeEnabled;

    private int accelerometerRange;

    public boolean isAccelerometerEnabled() {
        return accelerometerEnabled;
    }

    public void setAccelerometerEnabled(boolean accelerometerEnabled) {
        this.accelerometerEnabled = accelerometerEnabled;
    }

    public boolean isMagnetometerEnabled() {
        return magnetometerEnabled;
    }

    public void setMagnetometerEnabled(boolean magnetometerEnabled) {
        this.magnetometerEnabled = magnetometerEnabled;
    }

    public boolean isGyroscopeEnabled() {
        return gyroscopeEnabled;
    }

    public void setGyroscopeEnabled(boolean gyroscopeEnabled) {
        this.gyroscopeEnabled = gyroscopeEnabled;
    }

    public int getAccelerometerRange() {
        return accelerometerRange;
    }

    public void setAccelerometerRange(int accelerometerRange) {
        this.accelerometerRange = accelerometerRange;
    }
}
