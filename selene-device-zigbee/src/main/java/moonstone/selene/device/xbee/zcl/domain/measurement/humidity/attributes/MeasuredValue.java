package moonstone.selene.device.xbee.zcl.domain.measurement.humidity.attributes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import moonstone.selene.device.sensor.DoubleSensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;
import moonstone.selene.engine.EngineConstants;

public class MeasuredValue implements Attribute<DoubleSensorData> {
	private static final double TO_PERCENT_SCALE = 0.01;

	public static double calculate(short value) {
		return Short.toUnsignedInt(value) * TO_PERCENT_SCALE;
	}

	public static double calculate(byte[] value) {
		return calculate(ByteBuffer.wrap(value).order(ByteOrder.LITTLE_ENDIAN).getShort());
	}

	@Override
	public int getId() {
		return HumidityMeasurementClusterAttributes.MEASURED_VALUE_ATTRIBUTE_ID;
	}

	@Override
	public DoubleSensorData toData(String name, byte... value) {
		return new DoubleSensorData(name, calculate(value), EngineConstants.FORMAT_DECIMAL_2);
	}
}
