package moonstone.selene.device.xbee.zcl.domain.general.commissioning.commands;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class RestoreStartupParametersResponse extends ClusterSpecificCommand<RestoreStartupParametersResponse> {
	private byte[] status = new byte[1];

	public byte[] getStatus() {
		return status;
	}

	public RestoreStartupParametersResponse withStatus(byte status) {
		this.status[0] = status;
		return this;
	}

	@Override
	protected int getId() {
		return CommissioningClusterCommands.RESTORE_STARTUP_PARAMETERS_RESPONSE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		return status;
	}

	@Override
	public RestoreStartupParametersResponse fromPayload(byte[] payload) {
		status = payload;
		return this;
	}
}
