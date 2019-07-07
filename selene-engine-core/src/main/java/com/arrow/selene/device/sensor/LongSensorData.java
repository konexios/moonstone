package com.arrow.selene.device.sensor;

import java.util.List;

import com.arrow.acn.client.IotParameters;
import com.arrow.acn.client.model.TelemetryItemType;
import com.arrow.selene.data.Telemetry;

public class LongSensorData extends SensorDataAbstract<Long> {

	public LongSensorData(String name, Long data) {
		super(name, data);
	}

	@Override
	public void writeIoTParameters(IotParameters parameters) {
		if (getData() != null) {
			parameters.setLong(getName(), getData());
		}
	}

	@Override
	public void writeTelemetry(List<Telemetry> telemetries, long timestamp) {
		if (getData() != null) {
			Telemetry result = new Telemetry(TelemetryItemType.Integer, getName(), timestamp);
			result.setIntValue(getData());
			telemetries.add(result);
		}
	}
}
