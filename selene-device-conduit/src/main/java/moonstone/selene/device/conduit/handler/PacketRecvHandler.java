package moonstone.selene.device.conduit.handler;

public class PacketRecvHandler extends HandlerAbstract {
	@Override
	public void handle(String euid, String type, byte[] data) {
		String method = "handle";
		logInfo(method, "packet received for device: %s", euid);
	}
}
