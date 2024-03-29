package moonstone.selene.device.xbee.zcl.domain.general.appliance.attributes;

import com.digi.xbee.api.utils.ByteUtils;

import moonstone.selene.device.sensor.StringSensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public class RemainingTime implements Attribute<StringSensorData> {
	private static final int MINUTES_MASK = 0b00000000_00_111111;
	private static final int TIME_ENCODING_MASK = 0b00000000_11_000000;
	private static final int HOURS_MASK = 0b11111111_00_000000;

	public static int getMinutes(int value) {
		return value & MINUTES_MASK;
	}

	public static boolean isRelative(int value) {
		return (value & TIME_ENCODING_MASK) >> 6 == 0;
	}

	public static boolean isAbsolute(int value) {
		return (value & TIME_ENCODING_MASK) >> 6 == 1;
	}

	public static int getHours(int value) {
		return (value & HOURS_MASK) >> 8;
	}

	public static int getMinutes(byte... value) {
		return getMinutes(ByteUtils.byteArrayToInt(ByteUtils.swapByteArray(value)));
	}

	public static boolean isRelative(byte... value) {
		return isRelative(ByteUtils.byteArrayToInt(ByteUtils.swapByteArray(value)));
	}

	public static boolean isAbsolute(byte... value) {
		return isAbsolute(ByteUtils.byteArrayToInt(ByteUtils.swapByteArray(value)));
	}

	public static int getHours(byte... value) {
		return getHours(ByteUtils.byteArrayToInt(ByteUtils.swapByteArray(value)));
	}

	public static boolean isValid(int value) {
		return value != 0;
	}

	public static boolean isValid(byte... value) {
		return value[0] != 0 || value[1] != 0;
	}

	@Override
	public int getId() {
		return ApplianceControlClusterAttributes.REMAINING_TIME_ATTRIBUTE_ID;
	}

	@Override
	public StringSensorData toData(String name, byte... value) {
		return new StringSensorData(name,
				String.format("%s %02d:%02d", isRelative(value) ? "rel" : "abs", getHours(value), getMinutes(value)));
	}
}
