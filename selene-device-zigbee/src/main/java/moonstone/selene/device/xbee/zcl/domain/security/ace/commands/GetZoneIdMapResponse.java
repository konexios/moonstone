package moonstone.selene.device.xbee.zcl.domain.security.ace.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class GetZoneIdMapResponse extends ClusterSpecificCommand<GetZoneIdMapResponse> {
	private short[] sections;

	public short[] getSections() {
		return sections;
	}

	public GetZoneIdMapResponse withSections(short[] sections) {
		this.sections = sections;
		return this;
	}

	@Override
	protected int getId() {
		return SecurityAceClusterCommands.GET_ZONE_ID_MAP_RESPONSE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(16 * 16);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		for (short section : sections) {
			buffer.putShort(section);
		}
		return buffer.array();
	}

	@Override
	public GetZoneIdMapResponse fromPayload(byte[] payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		sections = new short[16];
		for (int i = 0; i < 16; i++) {
			sections[i] = buffer.getShort();
		}
		return this;
	}
}
