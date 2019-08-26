package moonstone.selene.device.xbee.zcl.domain.security.ace.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;
import moonstone.selene.device.xbee.zcl.domain.security.ace.data.PanelStatus;

public class PanelStatusChanged extends ClusterSpecificCommand<PanelStatusChanged> {
	private PanelStatus panelStatus;
	private int secondsRemaining;

	public PanelStatus getPanelStatus() {
		return panelStatus;
	}

	public PanelStatusChanged withPanelStatus(PanelStatus panelStatus) {
		this.panelStatus = panelStatus;
		return this;
	}

	public int getSecondsRemaining() {
		return secondsRemaining;
	}

	public PanelStatusChanged withSecondsRemaining(int secondsRemaining) {
		this.secondsRemaining = secondsRemaining;
		return this;
	}

	@Override
	protected int getId() {
		return SecurityAceClusterCommands.PANEL_STATUS_CHANGED_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(2);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put((byte) secondsRemaining);
		buffer.put((byte) panelStatus.ordinal());
		return buffer.array();
	}

	@Override
	public PanelStatusChanged fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 2, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		panelStatus = PanelStatus.values()[Byte.toUnsignedInt(buffer.get())];
		secondsRemaining = Byte.toUnsignedInt(buffer.get());
		return this;
	}
}
