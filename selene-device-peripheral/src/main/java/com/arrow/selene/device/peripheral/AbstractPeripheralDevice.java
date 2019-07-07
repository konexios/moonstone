package com.arrow.selene.device.peripheral;

import java.util.Map;

import com.arrow.selene.Loggable;
import com.arrow.selene.engine.state.DeviceStates;

public abstract class AbstractPeripheralDevice<State extends DeviceStates> extends Loggable
        implements PeripheralDevice {
    protected static final String PIN_FIELD_NAME = "pin";
    private boolean shuttingDown;
    public State states = createStates();

    public abstract State createStates();

    public State getStates() {
        return states;
    }

    protected State populate(Map<String, com.arrow.selene.engine.state.State> states) {
        State deviceStates = createStates();
        deviceStates.importStates(states);
        return deviceStates;
    }

    @Override
    public void start() {
        shuttingDown = false;
    }

    @Override
    public void stop() {
        shuttingDown = true;
    }

    @Override
    public boolean isShuttingDown() {
        return shuttingDown;
    }
}
