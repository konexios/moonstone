package moonstone.selene.device.xbee.zcl.domain.closures.shade.attributes;

import moonstone.selene.device.sensor.DoubleSensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;
import moonstone.selene.engine.EngineConstants;

public class MotorStepSize implements Attribute<DoubleSensorData> {
	private static final double TO_DEGREES_SCALE = 0.1;

	public static double calculate(byte... value) {
		return Byte.toUnsignedInt(value[0]) * TO_DEGREES_SCALE;
	}

	@Override
	public int getId() {
		return ShadeClusterAttributes.MOTOR_STEP_SIZE_ATTRIBUTE_ID;
	}

	@Override
	public DoubleSensorData toData(String name, byte... value) {
		return new DoubleSensorData(name, calculate(value), EngineConstants.FORMAT_DECIMAL_2);
	}
}
