package moonstone.selene.device.xbee.zcl.domain.se.metering.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import com.digi.xbee.api.utils.ByteUtils;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;
import moonstone.selene.device.xbee.zcl.domain.ha.measurement.data.ProfileIntervalPeriod;
import moonstone.selene.device.xbee.zcl.domain.se.metering.data.Status;

public class GetProfileResponse extends ClusterSpecificCommand<GetProfileResponse> {
	private long endTime;
	private Status status;
	private ProfileIntervalPeriod profileIntervalPeriod;
	private int[] intervals;

	public long getEndTime() {
		return endTime;
	}

	public GetProfileResponse withEndTime(long endTime) {
		this.endTime = endTime;
		return this;
	}

	public Status getStatus() {
		return status;
	}

	public GetProfileResponse withStatus(Status status) {
		this.status = status;
		return this;
	}

	public ProfileIntervalPeriod getProfileIntervalPeriod() {
		return profileIntervalPeriod;
	}

	public GetProfileResponse withProfileIntervalPeriod(ProfileIntervalPeriod profileIntervalPeriod) {
		this.profileIntervalPeriod = profileIntervalPeriod;
		return this;
	}

	public int[] getIntervals() {
		return intervals;
	}

	public GetProfileResponse withIntervals(int[] intervals) {
		this.intervals = intervals;
		return this;
	}

	@Override
	protected int getId() {
		return SimpleMeteringClusterCommands.GET_PROFILE_RESPONSE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(7 + intervals.length * 3);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt((int) endTime);
		buffer.put((byte) status.ordinal());
		buffer.put((byte) profileIntervalPeriod.ordinal());
		buffer.put((byte) intervals.length);
		for (int item : intervals) {
			buffer.put(ByteUtils.swapByteArray(ByteUtils.intToByteArray(item)), 0, 3);
		}
		return buffer.array();
	}

	@Override
	public GetProfileResponse fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length >= 7, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		endTime = Integer.toUnsignedLong(buffer.getInt());
		status = Status.values()[Byte.toUnsignedInt(buffer.get())];
		profileIntervalPeriod = ProfileIntervalPeriod.values()[Byte.toUnsignedInt(buffer.get())];
		int length = Byte.toUnsignedInt(buffer.get());
		intervals = new int[length];
		for (int i = 0; i < intervals.length; i++) {
			byte[] bytes = new byte[3];
			buffer.get(bytes);
			intervals[i] = ByteUtils.byteArrayToInt(ByteUtils.swapByteArray(bytes));
		}
		return this;
	}
}
