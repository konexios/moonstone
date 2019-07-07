package com.arrow.selene.engine;

import java.util.List;

import com.arrow.acn.client.IotParameters;
import com.arrow.selene.data.Telemetry;

public interface DeviceData {
	long getTimestamp();

	void setTimestamp(long timestamp);

	boolean isParsedFully();

	void setParsedFully(boolean parsedFully);

	IotParameters writeIoTParameters();

	List<Telemetry> writeTelemetries();

	IotParameters getParsedIotParameters();

	void setParsedIotParameters(IotParameters parsedIotParameters);

	List<Telemetry> getParsedTelemetries();

	void setParsedTelemetries(List<Telemetry> parsedTelemetries);
}
