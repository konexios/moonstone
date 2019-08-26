package moonstone.selene.device.conduit.handler;

import java.nio.charset.StandardCharsets;

import moonstone.selene.device.conduit.LoraDeviceModule;
import moonstone.selene.engine.DeviceModuleAbstract;

public class UpHandler extends HandlerAbstract {
	@Override
	public void handle(String euid, String type, byte[] data) {
		String method = "handle";
		String payload = new String(data, StandardCharsets.UTF_8);

		// manually tweak the payload
		payload = payload.replace("\"timestamp\"", "\"ts\"");

		logInfo(method, "euid: %s, type: %s", euid, type);
		DeviceModuleAbstract<?, ?, ?, ?> device = getServer().findOrCreateModule(euid);
		logInfo(method, "found deviceHid: %s", device.getDevice().getHid());
		((LoraDeviceModule) device).processPayload(payload);
	}
}
