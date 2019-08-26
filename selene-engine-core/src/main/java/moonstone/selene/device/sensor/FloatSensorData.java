package moonstone.selene.device.sensor;

import java.util.List;

import org.apache.commons.lang3.Validate;

import moonstone.acn.client.IotParameters;
import moonstone.acn.client.model.TelemetryItemType;
import moonstone.selene.data.Telemetry;

public class FloatSensorData extends SensorDataAbstract<Float> {

	private final String format;

	public FloatSensorData(String name, Float data, String format) {
		super(name, data);
		this.format = format;
		Validate.notBlank(format, "format is empty");
	}

	@Override
	public void writeIoTParameters(IotParameters parameters) {
		if (getData() != null) {
			parameters.setFloat(getName(), getData(), format);
		}
	}

	@Override
	public void writeTelemetry(List<Telemetry> telemetries, long timestamp) {
		if (getData() != null) {
			Telemetry result = new Telemetry(TelemetryItemType.Float, getName(), timestamp);
			result.setFloatValue(Double.valueOf(getData()));
			telemetries.add(result);
		}
	}
}
