package moonstone.selene.device.sensor;

import java.util.List;

import moonstone.acn.client.IotParameters;
import moonstone.selene.data.Telemetry;

public interface SensorData<Type> {
	void writeIoTParameters(IotParameters parameters);

	void writeTelemetry(List<Telemetry> telemetries, long timestamp);

	String getName();
}
