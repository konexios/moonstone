package moonstone.selene.device.peripheral.devices;

import java.util.Map;

import moonstone.selene.device.peripheral.AbstractPeripheralDevice;
import moonstone.selene.engine.state.DeviceStates;
import mraa.I2c;

public abstract class I2cDevice<State extends DeviceStates> extends AbstractPeripheralDevice<State> {
	public static final String BUS_FIELD_NAME = "bus";
	public static final String ADDRESS_FIELD_NAME = "address";

	protected I2c i2c;

	@Override
	public void init(Map<String, String> values) {
		i2c = new I2c(Integer.parseInt(values.get(BUS_FIELD_NAME)), true);
		i2c.address(Short.parseShort(values.get(ADDRESS_FIELD_NAME)));
	}
}
