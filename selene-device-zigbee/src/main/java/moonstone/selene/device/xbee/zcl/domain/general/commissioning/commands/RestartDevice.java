package moonstone.selene.device.xbee.zcl.domain.general.commissioning.commands;

import java.nio.ByteBuffer;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class RestartDevice extends ClusterSpecificCommand<RestartDevice> {
	private boolean startupMode;
	private boolean immediately;
	private byte delay;
	private byte jitter;

	public boolean isStartupMode() {
		return startupMode;
	}

	public RestartDevice withStartupMode(boolean startupMode) {
		this.startupMode = startupMode;
		return this;
	}

	public boolean isImmediately() {
		return immediately;
	}

	public RestartDevice withImmediately(boolean immediately) {
		this.immediately = immediately;
		return this;
	}

	public byte getDelay() {
		return delay;
	}

	public RestartDevice withDelay(byte delay) {
		this.delay = delay;
		return this;
	}

	public byte getJitter() {
		return jitter;
	}

	public RestartDevice withJitter(byte jitter) {
		this.jitter = jitter;
		return this;
	}

	@Override
	protected int getId() {
		return CommissioningClusterCommands.RESTART_DEVICE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(3);
		int options = 0x00;
		if (startupMode) {
			options |= 0b0000_0001;
		}
		if (immediately) {
			options |= 0b0000_1000;
		}
		buffer.put((byte) options);
		buffer.put(delay);
		buffer.put(jitter);
		return buffer.array();
	}

	@Override
	public RestartDevice fromPayload(byte[] payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		byte options = buffer.get();
		startupMode = (options & 0b0000_0001) != 0;
		immediately = (options & 0b0000_1000) != 0;
		delay = buffer.get();
		jitter = buffer.get();
		return this;
	}
}
