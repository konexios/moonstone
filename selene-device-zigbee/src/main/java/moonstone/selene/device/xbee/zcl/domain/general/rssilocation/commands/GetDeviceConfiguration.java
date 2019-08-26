package moonstone.selene.device.xbee.zcl.domain.general.rssilocation.commands;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class GetDeviceConfiguration extends ClusterSpecificCommand<GetDeviceConfiguration> {
	private byte[] targetAddress;

	public byte[] getTargetAddress() {
		return targetAddress;
	}

	public GetDeviceConfiguration withTargetAddress(byte[] targetAddress) {
		this.targetAddress = targetAddress;
		return this;
	}

	@Override
	protected int getId() {
		return RssiLocationClusterCommands.GET_DEVICE_CONFIGURATION_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		return targetAddress;
	}

	@Override
	public GetDeviceConfiguration fromPayload(byte[] payload) {
		targetAddress = payload;
		return this;
	}
}
