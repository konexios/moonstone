package moonstone.selene.device.peripheral.devices.jhd1313m1;

import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import moonstone.selene.device.peripheral.AbstractPeripheralDevice;
import moonstone.selene.device.peripheral.ControlledPeripheralDevice;
import moonstone.selene.engine.state.State;

public class Jhd1313m1 extends AbstractPeripheralDevice<Jhd1313m1States> implements ControlledPeripheralDevice {
	public static final String DEFAULT_LCD_ADDRESS = "3e";
	public static final String DEFAULT_RGB_ADDRESS = "62";
	public static final String ON_VALUE = "on";
	public static final String BUS_FIELD_NAME = "bus";
	public static final String LCD_ADDRESS_FIELD_NAME = "lcdAddress";
	public static final String RGB_ADDRESS_FIELD_NAME = "rgbAddress";
	private upm_jhd1313m1.Jhd1313m1 device;

	@Override
	public void init(Map<String, String> values) {
		String bus = values.get(BUS_FIELD_NAME);
		Validate.notBlank(bus, "bus is not defined");
		String lcdAddress = values.get(LCD_ADDRESS_FIELD_NAME);
		if (StringUtils.isEmpty(lcdAddress)) {
			lcdAddress = DEFAULT_LCD_ADDRESS;
		}
		String rgbAddress = values.get(RGB_ADDRESS_FIELD_NAME);
		if (StringUtils.isEmpty(rgbAddress)) {
			rgbAddress = DEFAULT_RGB_ADDRESS;
		}
		device = new upm_jhd1313m1.Jhd1313m1(Integer.parseInt(bus), Integer.parseInt(lcdAddress, 16), Integer.parseInt
				(rgbAddress, 16));
	}

	@Override
	public boolean changeState(Map<String, State> states) {
		Jhd1313m1States jhd1313m1States = populate(states);
		String backlight = jhd1313m1States.getBacklight().getValue();
		if (backlight != null) {
			if (Objects.equals(backlight, ON_VALUE)) {
				device.backlightOn();
			} else {
				device.backlightOff();
			}
		}
		String color = jhd1313m1States.getColor().getValue();
		if (color != null) {
			Integer rgb = Integer.parseInt(color, 16);
			device.setColor((short) (rgb >> 16), (short) (rgb >> 8 & 0xFF), (short) (rgb & 0xFF));
		}
		String display = jhd1313m1States.getDisplay().getValue();
		if (display != null) {
			if (Objects.equals(display, ON_VALUE)) {
				device.displayOn();
			} else {
				device.displayOff();
			}
		}
		String row1 = jhd1313m1States.getRow1().getValue();
		if (row1 != null) {
			device.setCursor(0, 0);
			device.write(row1);
		}
		String row2 = jhd1313m1States.getRow2().getValue();
		if (row2 != null) {
			device.setCursor(1, 0);
			device.write(row2);
		}
		return true;
	}

	@Override
	public Jhd1313m1States createStates() {
		return new Jhd1313m1States();
	}
}
