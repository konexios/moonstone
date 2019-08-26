package moonstone.selene.device.harting.rfid.command;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import moonstone.selene.device.harting.rfid.Utils;

public abstract class AbstractExtendedCommand implements Command {
	private static final long serialVersionUID = -415564160762923540L;
	protected ExtendedHeader header = new ExtendedHeader();

	@Override
	public byte[] build(int comAddress) {
		byte[] bytes = header.withLength(getLength()).withControl(getControl()).withComAddress(comAddress).build();
		ByteBuffer buffer = ByteBuffer.allocate(bytes.length + getLength() + 2).order(ByteOrder.LITTLE_ENDIAN);
		buffer.put(bytes);
		buffer.put(buildPayload());
		buffer.putShort((short) Utils.calculateCrc16(Arrays.copyOf(buffer.array(), buffer.capacity() - 2)));
		return buffer.array();
	}

	protected abstract byte[] buildPayload();

	protected abstract int getControl();

	protected abstract int getLength();

	@Override
	public int getCommandMode() {
		return IGNORED_MODE;
	}
}
