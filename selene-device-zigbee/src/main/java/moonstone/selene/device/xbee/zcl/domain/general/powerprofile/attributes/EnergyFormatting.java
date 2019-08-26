package moonstone.selene.device.xbee.zcl.domain.general.powerprofile.attributes;

import moonstone.selene.device.sensor.StringSensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public class EnergyFormatting implements Attribute<StringSensorData> {
	public static int rightDigits(byte... value) {
		return value[0] & 0b0_0000_111;
	}

	public static int leftDigits(byte... value) {
		return (value[0] & 0b0_1111_000) >> 3;
	}

	public static boolean suppressLeadingZeros(byte... value) {
		return (value[0] & 0b1_0000_000) != 0;
	}

	@Override
	public int getId() {
		return PowerProfileClusterAttributes.ENERGY_FORMATTING_ATTRIBUTE_ID;
	}

	@Override
	public StringSensorData toData(String name, byte... value) {
		return new StringSensorData(name,
				String.format("suppress leading zeros: %s, left digits: %d, right digits: " + "%d",
						suppressLeadingZeros(value), leftDigits(value), rightDigits()));
	}
}
