package com.arrow.selene.device.conduit.handler;

public interface TopicTypeHandler {
	void handle(String euid, String type, byte[] data);
}
