package moonstone.selene.device.xbee.zcl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ZclHeader {
	protected boolean clusterSpecific;
	protected boolean manufacturerSpecific;
	protected boolean fromServer;
	private boolean disableDefaultResponse;
	private int manufacturerCode;
	private int sequence;
	private int commandId;

	public boolean isClusterSpecific() {
		return clusterSpecific;
	}

	public void setClusterSpecific(boolean clusterSpecific) {
		this.clusterSpecific = clusterSpecific;
	}

	public boolean isFromServer() {
		return fromServer;
	}

	public void setFromServer(boolean fromServer) {
		this.fromServer = fromServer;
	}

	public boolean isDisableDefaultResponse() {
		return disableDefaultResponse;
	}

	public void setDisableDefaultResponse(boolean disableDefaultResponse) {
		this.disableDefaultResponse = disableDefaultResponse;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public int getCommandId() {
		return commandId;
	}

	public void setCommandId(int commandId) {
		this.commandId = commandId;
	}

	public int getManufacturerCode() {
		return manufacturerCode;
	}

	public void setManufacturerCode(int manufacturerCode) {
		this.manufacturerCode = manufacturerCode;
	}

	public boolean isManufacturerSpecific() {
		return manufacturerSpecific;
	}

	public void setManufacturerSpecific(boolean manufacturerSpecific) {
		this.manufacturerSpecific = manufacturerSpecific;
	}

	public byte[] buildHeader(int sequence, boolean manufacturerSpecific) {
		ByteBuffer os = ByteBuffer.allocate(manufacturerSpecific ? 5 : 3);
		os.order(ByteOrder.LITTLE_ENDIAN);
		int frameControl = 0;
		if (clusterSpecific) {
			frameControl |= 0x01;
		}
		if (manufacturerSpecific) {
			frameControl |= 0x04;
		}
		if (fromServer) {
			frameControl |= 0x08;
		}
		if (disableDefaultResponse) {
			frameControl |= 0x10;
		}
		os.put((byte) frameControl);
		if (manufacturerSpecific) {
			os.putShort((short) manufacturerCode);
		}
		os.put((byte) sequence);
		os.put((byte) commandId);
		return os.array();
	}

	public ZclHeader extractHeader(byte[] payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		setFrameControl(buffer.get());
		if (manufacturerSpecific) {
			manufacturerCode = Short.toUnsignedInt(buffer.getShort());
		}
		sequence = Byte.toUnsignedInt(buffer.get());
		commandId = Byte.toUnsignedInt(buffer.get());
		return this;
	}

	public void setFrameControl(byte frameControl) {
		clusterSpecific = (frameControl & 0x01) != 0;
		manufacturerSpecific = (frameControl & 0x04) != 0;
		fromServer = (frameControl & 0x08) != 0;
		disableDefaultResponse = (frameControl & 0x10) != 0;
	}
}
