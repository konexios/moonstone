package moonstone.selene.device.conduit.handler;

public class DownHandler extends HandlerAbstract {
	@Override
	public void handle(String euid, String type, byte[] data) {
		String method = "handle";
		logInfo(method, "message has been queued to send to: %s", euid);
	}
}
