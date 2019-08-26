package moonstone.selene.device.xbee.zcl.domain.se.metering.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Set;

import org.apache.commons.lang3.Validate;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;
import moonstone.selene.device.xbee.zcl.domain.se.metering.data.SnapshotCause;

public class TakeSnapshot extends ClusterSpecificCommand<TakeSnapshot> {
	private Set<SnapshotCause> snapshotCauses;

	public Set<SnapshotCause> getSnapshotCauses() {
		return snapshotCauses;
	}

	public TakeSnapshot withSnapshotCauses(Set<SnapshotCause> snapshotCauses) {
		this.snapshotCauses = snapshotCauses;
		return this;
	}

	@Override
	protected int getId() {
		return SimpleMeteringClusterCommands.TAKE_SNAPSHOT_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt(SnapshotCause.getByValue(snapshotCauses));
		return buffer.array();
	}

	@Override
	public TakeSnapshot fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 4, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		snapshotCauses = SnapshotCause.getByValue(buffer.getInt());
		return this;
	}
}
