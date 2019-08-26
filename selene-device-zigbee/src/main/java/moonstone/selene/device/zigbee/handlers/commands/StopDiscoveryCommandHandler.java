package moonstone.selene.device.zigbee.handlers.commands;

import moonstone.selene.device.zigbee.ZigBeeCoordinatorModule;

public class StopDiscoveryCommandHandler implements CommandHandler {
	public static final String COMMAND = "stopDiscovery";

	private ZigBeeCoordinatorModule module;

	public StopDiscoveryCommandHandler(ZigBeeCoordinatorModule module) {
		this.module = module;
	}

	@Override
	public void handle(String command, String payload) {
		String method = "StopDiscoveryCommandHandler.handle";
		module.getProperties().setDiscovering(false);
		module.logInfo(method, "stopping discovery");
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
