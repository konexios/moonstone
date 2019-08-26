package moonstone.selene.device.xbee.zcl.domain.general.appliance.commands;

import org.apache.commons.lang3.Validate;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class ExecutionOfACommand extends ClusterSpecificCommand<ExecutionOfACommand> {
	private byte[] commandId = new byte[1];

	public byte[] getCommandId() {
		return commandId;
	}

	public ExecutionOfACommand withCommandId(byte commandId) {
		this.commandId[0] = commandId;
		return this;
	}

	@Override
	protected int getId() {
		return ApplianceControlClusterCommands.EXECUTION_OF_A_COMMAND_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		return commandId;
	}

	@Override
	public ExecutionOfACommand fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 1, "payload length is incorrect");
		commandId = payload;
		return this;
	}
}
