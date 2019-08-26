package moonstone.selene.device.sensor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import moonstone.acn.client.IotParameters;
import moonstone.acn.client.model.TelemetryItemType;
import moonstone.selene.data.Telemetry;

public class SetSensorData extends SensorDataAbstract<Set<?>> {

	public SetSensorData(String name, Set<?> data) {
		super(name, data);
	}

	@Override
	public void writeIoTParameters(IotParameters parameters) {
		if (getData() != null) {
			parameters.setString(getName(),
					getData().stream().map(o -> o.toString()).collect(Collectors.joining(", ")));
		}
	}

	@Override
	public void writeTelemetry(List<Telemetry> telemetries, long timestamp) {
		if (getData() != null) {
			Telemetry result = new Telemetry(TelemetryItemType.Integer, getName(), timestamp);
			result.setStrValue(getData().stream().map(o -> o.toString()).collect(Collectors.joining(", ")));
			telemetries.add(result);
		}
	}
}
