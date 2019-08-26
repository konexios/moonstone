package moonstone.selene.device.xbee.zcl.domain.closures.window.commands;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class GoToLiftPercentage extends ClusterSpecificCommand<GoToLiftPercentage> {
	private int liftPercentage;

	public int getLiftPercentage() {
		return liftPercentage;
	}

	public GoToLiftPercentage withLiftPercentage(int liftPercentage) {
		this.liftPercentage = liftPercentage;
		return this;
	}

	@Override
	protected int getId() {
		return WindowCoveringClusterCommands.WINDOW_COVERING_GO_TO_LIFT_PERCENTAGE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		return new byte[]{(byte) liftPercentage};
	}

	@Override
	public GoToLiftPercentage fromPayload(byte[] payload) {
		liftPercentage = Byte.toUnsignedInt(payload[0]);
		return this;
	}
}
