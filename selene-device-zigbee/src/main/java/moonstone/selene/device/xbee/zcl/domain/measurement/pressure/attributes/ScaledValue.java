package moonstone.selene.device.xbee.zcl.domain.measurement.pressure.attributes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import moonstone.selene.device.sensor.DoubleSensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;
import moonstone.selene.engine.EngineConstants;

public class ScaledValue implements Attribute<DoubleSensorData> {
	public static double calculate(short value, int scale) {
		return value * Math.pow(10, scale - 1);
	}

	public static double calculate(byte[] value, int scale) {
		return calculate(ByteBuffer.wrap(value).order(ByteOrder.LITTLE_ENDIAN).getShort(), scale);
	}

	@Override
	public int getId() {
		return PressureMeasurementClusterAttributes.SCALED_VALUE_ATTRIBUTE_ID;
	}

	@Override
	public DoubleSensorData toData(String name, byte... value) {
		return new DoubleSensorData(name, calculate(value, 1), EngineConstants.FORMAT_DECIMAL_2);
	}
}
