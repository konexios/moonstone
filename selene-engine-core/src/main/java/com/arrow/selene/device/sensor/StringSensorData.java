package com.arrow.selene.device.sensor;

import java.util.List;

import com.arrow.acn.client.IotParameters;
import com.arrow.acn.client.model.TelemetryItemType;
import com.arrow.selene.data.Telemetry;

public class StringSensorData extends SensorDataAbstract<Object> {

	public StringSensorData(String name, Object data) {
		super(name, data);
	}

	@Override
	public void writeIoTParameters(IotParameters parameters) {
		if (getData() != null) {
			parameters.setString(getName(), getData().toString());
		}
	}

	@Override
	public void writeTelemetry(List<Telemetry> telemetries, long timestamp) {
		if (getData() != null) {
			Telemetry result = new Telemetry(TelemetryItemType.Integer, getName(), timestamp);
			result.setStrValue(getData().toString());
			telemetries.add(result);
		}
	}
}
