package com.arrow.selene.device.xbee.zcl.domain.ha.measurement.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class GetProfileInfoResponse extends ClusterSpecificCommand<GetProfileInfoResponse> {
	private int profileCount;
	private byte profileIntervalPeriod;
	private int maxNumberOfIntervals;
	private int[] attributes;

	public int getProfileCount() {
		return profileCount;
	}

	public GetProfileInfoResponse withProfileCount(int profileCount) {
		this.profileCount = profileCount;
		return this;
	}

	public byte getProfileIntervalPeriod() {
		return profileIntervalPeriod;
	}

	public GetProfileInfoResponse withProfileIntervalPeriod(byte profileIntervalPeriod) {
		this.profileIntervalPeriod = profileIntervalPeriod;
		return this;
	}

	public int getMaxNumberOfIntervals() {
		return maxNumberOfIntervals;
	}

	public GetProfileInfoResponse withMaxNumberOfIntervals(int maxNumberOfIntervals) {
		this.maxNumberOfIntervals = maxNumberOfIntervals;
		return this;
	}

	public int[] getAttributes() {
		return attributes;
	}

	public GetProfileInfoResponse withAttributes(int[] attributes) {
		this.attributes = attributes;
		return this;
	}

	@Override
	protected int getId() {
		return ElectricalMeasurementClusterCommands.GET_PROFILE_INFO_RESPONSE_COMMAND_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(3 + attributes.length * 2);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put((byte) profileCount);
		buffer.put(profileIntervalPeriod);
		buffer.put((byte) maxNumberOfIntervals);
		for (int attribute : attributes) {
			buffer.putShort((short) attribute);
		}
		return buffer.array();
	}

	@Override
	public GetProfileInfoResponse fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length >= 3, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		profileCount = Byte.toUnsignedInt(buffer.get());
		profileIntervalPeriod = buffer.get();
		maxNumberOfIntervals = Byte.toUnsignedInt(buffer.get());
		attributes = new int[buffer.remaining() / 2];
		for (int i = 0; i < attributes.length; i++) {
			attributes[i] = Short.toUnsignedInt(buffer.getShort());
		}
		return this;
	}
}
