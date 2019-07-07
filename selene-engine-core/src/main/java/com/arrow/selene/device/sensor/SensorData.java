package com.arrow.selene.device.sensor;

import java.util.List;

import com.arrow.acn.client.IotParameters;
import com.arrow.selene.data.Telemetry;

public interface SensorData<Type> {
	void writeIoTParameters(IotParameters parameters);

	void writeTelemetry(List<Telemetry> telemetries, long timestamp);

	String getName();
}
