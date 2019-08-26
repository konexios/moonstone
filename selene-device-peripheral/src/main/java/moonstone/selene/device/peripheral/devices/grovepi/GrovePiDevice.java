package moonstone.selene.device.peripheral.devices.grovepi;

import java.nio.ByteBuffer;
import java.util.Map;

import moonstone.selene.device.peripheral.devices.I2cDevice;
import moonstone.selene.engine.state.DeviceStates;

public abstract class GrovePiDevice<State extends DeviceStates> extends I2cDevice<State> {
	public static final int GROVE_PI_DEVICE_ADDRESS = 4;
	protected byte pin;

	@Override
	public void init(Map<String, String> values) {
		values.put(ADDRESS_FIELD_NAME, Integer.toString(GROVE_PI_DEVICE_ADDRESS));
		super.init(values);
		pin = Byte.parseByte(values.get(PIN_FIELD_NAME));
	}

	protected void outputMode() {
		i2c.write(new byte[]{Commands.PIN_MODE, pin, 1, Commands.UNUSED});
	}

	protected void inputMode() {
		i2c.write(new byte[]{Commands.PIN_MODE, pin, 0, Commands.UNUSED});
	}

	protected void digitalWrite(int value) {
		i2c.write(new byte[]{Commands.DIGITAL_WRITE, pin, (byte) value, Commands.UNUSED});
	}

	protected synchronized int digitalRead() {
		i2c.write(new byte[]{Commands.DIGITAL_READ, pin, Commands.UNUSED, Commands.UNUSED});
		return i2c.readByte();
	}

	protected void analogWrite(int value) {
		i2c.write(new byte[]{Commands.ANALOG_WRITE, pin, (byte) value, Commands.UNUSED});
	}

	protected synchronized int analogRead() {
		i2c.write(new byte[]{Commands.ANALOG_READ, pin, Commands.UNUSED, Commands.UNUSED});
		byte[] bytes = new byte[32];
		i2c.read(bytes);
		return Short.toUnsignedInt(ByteBuffer.wrap(bytes).getShort(1));
	}
}
