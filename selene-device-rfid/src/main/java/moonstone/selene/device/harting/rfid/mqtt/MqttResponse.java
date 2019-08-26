package moonstone.selene.device.harting.rfid.mqtt;

import moonstone.selene.device.harting.rfid.command.Response;

public class MqttResponse {
	private String message;
	private Response<?> response;

	public String getMessage() {
		return message;
	}

	public MqttResponse withMessage(String message) {
		this.message = message;
		return this;
	}

	public Response<?> getResponse() {
		return response;
	}

	public MqttResponse withResponse(Response<?> response) {
		this.response = response;
		return this;
	}
}
