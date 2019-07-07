package com.arrow.selene.device.conduit.handler;

public class PacketSentHandler extends HandlerAbstract {
	@Override
	public void handle(String euid, String type, byte[] data) {
		String method = "handle";
		logInfo(method, "packet sent for device: %s", euid);
	}
}
