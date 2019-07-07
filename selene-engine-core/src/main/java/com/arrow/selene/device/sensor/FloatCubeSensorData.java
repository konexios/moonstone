package com.arrow.selene.device.sensor;

import java.util.List;

import org.apache.commons.lang3.Validate;

import com.arrow.acn.client.IotParameters;
import com.arrow.acn.client.model.TelemetryItemType;
import com.arrow.selene.data.Telemetry;

public class FloatCubeSensorData extends SensorDataAbstract<float[]> {

	private final String format;

	public FloatCubeSensorData(String name, float[] data, String format) {
		super(name, data);
		this.format = format;
		Validate.notBlank(format, "format is empty");
		Validate.isTrue(data != null && data.length == 3, "invalid data");
	}

	@Override
	public void writeIoTParameters(IotParameters parameters) {
		if (getData() != null) {
			parameters.setFloatCube(getName(), getData()[0], getData()[1], getData()[2], format);
		}
	}

	@Override
	public void writeTelemetry(List<Telemetry> telemetries, long timestamp) {
		if (getData() != null) {
			Telemetry result = new Telemetry(TelemetryItemType.FloatCube, getName(), timestamp);
			result.setStrValue(String.format("%s|%s|%s", String.format(format, getData()[0]),
					String.format(format, getData()[1]), String.format(format, getData()[2])));
			telemetries.add(result);
		}
	}
}
