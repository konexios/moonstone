package moonstone.selene.device.xbee.zdo;

public class PermitJoinRequest {
	public static byte[] toPayload(byte sequence, int duration) {
		byte[] payload = new byte[3];
		payload[0] = sequence;
		payload[1] = (byte) duration;
		payload[2] = 1;
		return payload;
	}
}
