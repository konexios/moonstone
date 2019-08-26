package moonstone.selene.device.xbee.zcl.domain.lighting.color.commands;

import java.nio.ByteBuffer;
import java.util.Set;

import org.apache.commons.lang3.Validate;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;
import moonstone.selene.device.xbee.zcl.domain.lighting.color.data.Action;
import moonstone.selene.device.xbee.zcl.domain.lighting.color.data.LoopDirection;
import moonstone.selene.device.xbee.zcl.domain.lighting.color.data.UpdateFlags;

public class ColorLoopSet extends ClusterSpecificCommand<ColorLoopSet> {
	private Set<UpdateFlags> updateFlags;
	private Action action;
	private LoopDirection direction;
	private int time;
	private int startHue;

	public Set<UpdateFlags> getUpdateFlags() {
		return updateFlags;
	}

	public ColorLoopSet withUpdateFlags(Set<UpdateFlags> updateFlags) {
		this.updateFlags = updateFlags;
		return this;
	}

	public Action getAction() {
		return action;
	}

	public ColorLoopSet withAction(Action action) {
		this.action = action;
		return this;
	}

	public LoopDirection getDirection() {
		return direction;
	}

	public ColorLoopSet withDirection(LoopDirection direction) {
		this.direction = direction;
		return this;
	}

	public int getTime() {
		return time;
	}

	public ColorLoopSet withTime(int time) {
		this.time = time;
		return this;
	}

	public int getStartHue() {
		return startHue;
	}

	public ColorLoopSet withStartHue(int startHue) {
		this.startHue = startHue;
		return this;
	}

	@Override
	protected int getId() {
		return LightingColorClusterCommands.COLOR_LOOP_SET_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(7);
		int update = 0;
		for (UpdateFlags item : updateFlags) {
			update |= 1 << item.ordinal();
		}
		buffer.put((byte) update);
		buffer.put((byte) action.ordinal());
		buffer.put((byte) direction.ordinal());
		buffer.putShort((short) time);
		buffer.putShort((short) startHue);
		return buffer.array();
	}

	@Override
	public ColorLoopSet fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 7, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		updateFlags = UpdateFlags.getByValue(buffer.get());
		action = Action.values()[Byte.toUnsignedInt(buffer.get())];
		direction = LoopDirection.values()[Byte.toUnsignedInt(buffer.get())];
		time = Short.toUnsignedInt(buffer.getShort());
		startHue = Short.toUnsignedInt(buffer.getShort());
		return this;
	}
}
