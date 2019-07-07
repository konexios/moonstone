package com.arrow.selene.device.zigbee.handlers.commands;

import com.arrow.selene.device.zigbee.ZigBeeCoordinatorModule;

public class StartDiscoveryCommandHandler implements CommandHandler {
	public static final String COMMAND = "startDiscovery";

	private ZigBeeCoordinatorModule module;

	public StartDiscoveryCommandHandler(ZigBeeCoordinatorModule module) {
		this.module = module;
	}

	@Override
	public void handle(String command, String payload) {
		String method = "StartDiscoveryCommandHandler.handle";
		module.getProperties().setDiscovering(true);
		module.logInfo(method, "starting discovery");
	}

	@Override
	public String getName() {
		return COMMAND;
	}

	@Override
	public String getPayload() {
		return "";
	}
}
