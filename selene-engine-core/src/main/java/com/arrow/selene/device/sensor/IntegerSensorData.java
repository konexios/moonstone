package com.arrow.selene.device.sensor;

import java.util.List;

import com.arrow.acn.client.IotParameters;
import com.arrow.acn.client.model.TelemetryItemType;
import com.arrow.selene.data.Telemetry;

public class IntegerSensorData extends SensorDataAbstract<Integer> {

	public IntegerSensorData(String name, Integer data) {
		super(name, data);
	}

	@Override
	public void writeIoTParameters(IotParameters parameters) {
		if (getData() != null) {
			parameters.setInteger(getName(), getData());
		}
	}

	@Override
	public void writeTelemetry(List<Telemetry> telemetries, long timestamp) {
		if (getData() != null) {
			Telemetry result = new Telemetry(TelemetryItemType.Integer, getName(), timestamp);
			result.setIntValue(Long.valueOf(getData()));
			telemetries.add(result);
		}
	}
}
