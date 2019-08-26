package moonstone.selene.device.harting.rfid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;

public final class Utils {
	private static final int CRC_POLYNOMIAL = 0x8408;
	private static final int CRC_PRESET = 0xffff;

	private static TypeReference<Map<String, String>> mapTypeRef;

	public static int calculateCrc16(byte... data) {
		int crc = CRC_PRESET;
		for (byte aData : data) {
			crc ^= Byte.toUnsignedInt(aData);
			for (int j = 0; j < 8; j++) {
				if ((crc & 0x1) == 1) {
					crc = crc >> 1 ^ CRC_POLYNOMIAL;
				} else {
					crc >>= 1;
				}
			}
		}
		return crc;
	}

	public static TypeReference<Map<String, String>> getMapTypeRef() {
		return mapTypeRef != null ? mapTypeRef : (mapTypeRef = new TypeReference<Map<String, String>>() {
		});
	}

	public static String joinBytes(String delimiter, byte... bytes) {
		List<Byte> list = new ArrayList<>();
		for (byte b : bytes) {
			list.add(b);
		}
		return String.join(delimiter,
				list.stream().map(b -> Integer.toString(Byte.toUnsignedInt(b))).collect(Collectors.toList()));
	}

	public static String joinBytesHex(String delimiter, byte... bytes) {
		List<Byte> list = new ArrayList<>();
		for (byte b : bytes) {
			list.add(b);
		}
		return String.join(delimiter,
				list.stream().map(b -> String.format("%02x", Byte.toUnsignedInt(b))).collect(Collectors.toList()));
	}
}
