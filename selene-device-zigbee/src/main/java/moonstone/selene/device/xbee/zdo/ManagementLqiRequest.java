package moonstone.selene.device.xbee.zdo;

public class ManagementLqiRequest {
	public static byte[] toPayload(byte sequence, int startIndex) {
		byte[] payload = new byte[2];
		payload[0] = sequence;
		payload[1] = (byte) startIndex;
		return payload;
	}
}
