package moonstone.selene.device.conduit.handler;

import moonstone.selene.engine.DeviceModuleAbstract;

public class JoinedHandler extends HandlerAbstract {
	@Override
	public void handle(String euid, String type, byte[] data) {
		String method = "handle";
		logInfo(method, "euid: %s, type: %s", euid, type);
		DeviceModuleAbstract<?, ?, ?, ?> device = getServer().findOrCreateModule(euid);
		logInfo(method, "found deviceHid: %s", device.getDevice().getHid());
	}
}
