package moonstone.selene.device.zigbee.handlers.commands;

import moonstone.selene.device.zigbee.ZigBeeEndDeviceModule;

public class RequestInfoCommandHandler implements CommandHandler {
	public static final String COMMAND = "requestInfo";

	private ZigBeeEndDeviceModule module;

	public RequestInfoCommandHandler(ZigBeeEndDeviceModule module) {
		this.module = module;
	}

	@Override
	public void handle(String command, String payload) {
		module.sendNodeDescriptorRequest();
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
