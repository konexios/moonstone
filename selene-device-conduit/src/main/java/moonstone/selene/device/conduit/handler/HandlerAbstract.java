package moonstone.selene.device.conduit.handler;

import java.nio.charset.StandardCharsets;

import moonstone.acs.Loggable;
import moonstone.selene.device.conduit.LoraServerModule;

public abstract class HandlerAbstract extends Loggable implements TopicTypeHandler {

	private LoraServerModule server;

	public HandlerAbstract withServer(LoraServerModule server) {
		this.server = server;
		return this;
	}

	protected LoraServerModule getServer() {
		return server;
	}

	@Override
	public void handle(String euid, String type, byte[] data) {
		String method = "handle";
		String payload = new String(data, StandardCharsets.UTF_8);
		logInfo(method, "I'M NOT IMPLEMENTED YET ---> euid: %s, type: %s, payload: %s", euid, type, payload);
	}
}
