package com.arrow.selene.device.xbee;

import java.util.ArrayList;
import java.util.List;

import com.arrow.acn.client.IotParameters;
import com.arrow.selene.data.Telemetry;
import com.arrow.selene.engine.DeviceDataAbstract;
import com.arrow.selene.engine.EngineConstants;
import com.arrow.selene.engine.Utils;

public class XbeeSensorDataImpl extends DeviceDataAbstract implements XbeeSensorData {

    private final double temperature;
    private final double humidity;
    private final double light;

    public XbeeSensorDataImpl(double temperature, double humidity, double light) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.light = light;
    }

    @Override
    public IotParameters writeIoTParameters() {
        IotParameters result = new IotParameters();
        result.setDouble("temperature", temperature, EngineConstants.FORMAT_DECIMAL_2);
        result.setDouble("humidity", humidity, EngineConstants.FORMAT_DECIMAL_2);
        result.setDouble("light", light, EngineConstants.FORMAT_DECIMAL_2);
        return result;
    }

    @Override
    public List<Telemetry> writeTelemetries() {
        List<Telemetry> result = new ArrayList<>();
        result.add(writeFloatTelemetry("temperature", Utils.trim2Decimals(temperature)));
        result.add(writeFloatTelemetry("humidity", Utils.trim2Decimals(humidity)));
        result.add(writeFloatTelemetry("light", Utils.trim2Decimals(light)));
        return result;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getLight() {
        return light;
    }
}
