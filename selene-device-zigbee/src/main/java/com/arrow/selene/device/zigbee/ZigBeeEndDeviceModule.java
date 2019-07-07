package com.arrow.selene.device.zigbee;

import java.util.Locale;
import java.util.Map;

import com.arrow.selene.device.zigbee.handlers.commands.BindCommandHandler;
import com.arrow.selene.device.zigbee.handlers.commands.CommandHandler;
import com.arrow.selene.device.zigbee.handlers.commands.ConfigureReportingCommandHandler;
import com.arrow.selene.device.zigbee.handlers.commands.RequestInfoCommandHandler;
import com.arrow.selene.device.zigbee.handlers.commands.SendCommandCommandHandler;
import com.arrow.selene.device.zigbee.handlers.commands.UnbindCommandHandler;

public class ZigBeeEndDeviceModule extends
        ZigBeeModuleAbstract<ZigBeeEndDeviceInfo, ZigBeeEndDeviceProperties, ZigBeeEndDeviceStates, ZigBeeHealthData> {

	private String localAddress;

	public ZigBeeEndDeviceModule(ZigBeeCoordinatorModule hub, String address) {
		this.hub = hub;

		getInfo().setAddress(address);
		commandHandlers.put(SendCommandCommandHandler.COMMAND, new SendCommandCommandHandler(this));
		commandHandlers.put(BindCommandHandler.COMMAND, new BindCommandHandler(this));
		commandHandlers.put(UnbindCommandHandler.COMMAND, new UnbindCommandHandler(this));
		commandHandlers.put(ConfigureReportingCommandHandler.COMMAND, new ConfigureReportingCommandHandler(this));
		commandHandlers.put(RequestInfoCommandHandler.COMMAND, new RequestInfoCommandHandler(this));

		getInfo().setName(String.format("ZigBee-%s", getInfo().getAddress()));
		getInfo().setUid(getInfo().getName().toLowerCase(Locale.getDefault()));
		Map<String, String> info = getInfo().getInfo();
		commandHandlers.values()
		        .forEach(handler -> info.put(
		                String.format("%s:%s", CommandHandler.COMMAND_PROPERTY_NAME, handler.getName()),
		                handler.getPayload()));
	}

	@Override
	protected ZigBeeEndDeviceProperties createProperties() {
		return new ZigBeeEndDeviceProperties();
	}

	@Override
	protected ZigBeeEndDeviceInfo createInfo() {
		return new ZigBeeEndDeviceInfo();
	}

	@Override
	protected ZigBeeEndDeviceStates createStates() {
		return new ZigBeeEndDeviceStates();
	}

	public String getLocalAddress() {
		return localAddress;
	}

	public void setLocalAddress(String localAddress) {
		this.localAddress = localAddress;
	}

	// public void sendNextMessage() {
	// String method = "sendNextMessage";
	// logInfo(method, "******* buffer size: %d", messages.size());
	// if (!messages.isEmpty()) {
	// resendMessage(messages.getFirst());
	// } else {
	// hub.persistUpdatedDeviceInfo();
	// }
	// }

	public void sendNodeDescriptorRequest() {
		hub.sendNodeDescriptorRequest(this);
	}
}
