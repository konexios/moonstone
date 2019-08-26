package moonstone.selene.device.zigbee.handlers.commands;

public interface CommandHandler {
	String COMMAND_PROPERTY_NAME = "command";
	String PAYLOAD_PROPERTY_NAME = "payload";

	void handle(String command, String payload);

	String getName();

	String getPayload();
}
