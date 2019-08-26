package moonstone.selene.device.xbee.zcl.domain.general.commissioning.commands;

import java.nio.ByteBuffer;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class SaveStartupParameters extends ClusterSpecificCommand<SaveStartupParameters> {
	private byte options;
	private byte index;

	public byte getOptions() {
		return options;
	}

	public SaveStartupParameters withOptions(byte options) {
		this.options = options;
		return this;
	}

	public byte getIndex() {
		return index;
	}

	public SaveStartupParameters withIndex(byte index) {
		this.index = index;
		return this;
	}

	@Override
	protected int getId() {
		return CommissioningClusterCommands.SAVE_STARTUP_PARAMETERS_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(2);
		buffer.put(options);
		buffer.put(index);
		return buffer.array();
	}

	@Override
	public SaveStartupParameters fromPayload(byte[] payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		options = buffer.get();
		index = buffer.get();
		return this;
	}
}
