package com.arrow.selene.device.sensor;

import java.util.List;

import com.arrow.acn.client.IotParameters;
import com.arrow.acn.client.model.TelemetryItemType;
import com.arrow.selene.data.Telemetry;

public class BooleanSensorData extends SensorDataAbstract<Boolean> {

	public BooleanSensorData(String name, Boolean data) {
		super(name, data);
	}

	@Override
	public void writeIoTParameters(IotParameters parameters) {
		if (getData() != null) {
			parameters.setBoolean(getName(), getData());
		}
	}

	@Override
	public void writeTelemetry(List<Telemetry> telemetries, long timestamp) {
		if (getData() != null) {
			Telemetry result = new Telemetry(TelemetryItemType.Boolean, getName(), timestamp);
			result.setBoolValue(getData());
			telemetries.add(result);
		}
	}
}
