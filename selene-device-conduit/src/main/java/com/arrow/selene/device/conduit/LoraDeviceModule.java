package com.arrow.selene.device.conduit;

public interface LoraDeviceModule {
	LoraServerModule getServer();

	void setServer(LoraServerModule server);

	void processPayload(String payload);
}
