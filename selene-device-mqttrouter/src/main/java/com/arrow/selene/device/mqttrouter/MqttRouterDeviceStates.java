package com.arrow.selene.device.mqttrouter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.arrow.selene.SeleneException;
import com.arrow.selene.engine.state.DeviceStates;
import com.arrow.selene.engine.state.State;

public class MqttRouterDeviceStates extends DeviceStates {
    private static final long serialVersionUID = 2007776204901287073L;

    private Map<String, State> deviceStates = new HashMap<>();

    @Override
    public Map<String, String> importStates(Map<String, State> states) {
        Map<String, String> result = new HashMap<>();
        for (String key : states.keySet()) {
            State state = states.get(key);
            State current = deviceStates.get(key);
            if (current == null || !StringUtils.equals(state.getValue(), current.getValue())) {
                deviceStates.put(key, state);
                result.put(key, state.getValue());
            }
        }
        return result;
    }

    @Override
    public Map<String, String> exportStates() {
        try {
            return deviceStates.entrySet().stream()
                    .collect(Collectors.toMap((entry -> entry.getKey()), (entry -> entry.getValue().toString())));
        } catch (Exception e) {
            throw new SeleneException("Error exporting states", e);
        }
    }

    public Map<String, State> extractStates(Map<String, String> stateChangeProperties) {

        List<String> availableProperties = stateChangeProperties.keySet().stream()
                .filter(propertyName -> deviceStates.containsKey(propertyName)).collect(Collectors.toList());

        return availableProperties.stream()
                .filter(propertyName -> !Objects.equals(stateChangeProperties.get(propertyName),
                        deviceStates.get(propertyName).getValue()))
                .collect(Collectors.toMap(s -> s, s -> new State().withValue(stateChangeProperties.get(s))));
    }

    public Map<String, State> getDeviceStates() {
        return deviceStates;
    }

    public void setDeviceStates(Map<String, State> deviceStates) {
        this.deviceStates = deviceStates;
    }

    public void addDeviceStates(String propertyName) {
        deviceStates.put(propertyName, new State());
    }
}
