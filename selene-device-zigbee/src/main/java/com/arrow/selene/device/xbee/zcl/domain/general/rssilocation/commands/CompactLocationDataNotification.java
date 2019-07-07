package com.arrow.selene.device.xbee.zcl.domain.general.rssilocation.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class CompactLocationDataNotification extends ClusterSpecificCommand<CompactLocationDataNotification> {
	private byte locationType;
	private short coord1;
	private short coord2;
	private short coord3;
	private byte qualityMeasure;
	private short locationAge;

	public byte getLocationType() {
		return locationType;
	}

	public CompactLocationDataNotification withLocationType(byte locationType) {
		this.locationType = locationType;
		return this;
	}

	public short getCoord1() {
		return coord1;
	}

	public CompactLocationDataNotification withCoord1(short coord1) {
		this.coord1 = coord1;
		return this;
	}

	public short getCoord2() {
		return coord2;
	}

	public CompactLocationDataNotification withCoord2(short coord2) {
		this.coord2 = coord2;
		return this;
	}

	public short getCoord3() {
		return coord3;
	}

	public CompactLocationDataNotification withCoord3(short coord3) {
		this.coord3 = coord3;
		return this;
	}

	public byte getQualityMeasure() {
		return qualityMeasure;
	}

	public CompactLocationDataNotification withQualityMeasure(byte qualityMeasure) {
		this.qualityMeasure = qualityMeasure;
		return this;
	}

	public short getLocationAge() {
		return locationAge;
	}

	public CompactLocationDataNotification withLocationAge(short locationAge) {
		this.locationAge = locationAge;
		return this;
	}

	@Override
	protected int getId() {
		return RssiLocationClusterCommands.COMPACT_LOCATION_DATA_NOTIFICATION_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(
				9 + ((locationType & 0b000_00010) == 0 ? 2 : 0) + ((locationType & 0b000_00001) == 0 ? 3 : 0));
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put(locationType);
		buffer.putShort(coord1);
		buffer.putShort(coord2);
		if ((locationType & 0b000_00010) == 0) {
			buffer.putShort(coord3);
		}
		if ((locationType & 0b000_00001) == 0) {
			buffer.put(qualityMeasure);
			buffer.putShort(locationAge);
		}
		return buffer.array();
	}

	@Override
	public CompactLocationDataNotification fromPayload(byte[] payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		locationType = buffer.get();
		coord1 = buffer.getShort();
		coord2 = buffer.getShort();
		if ((locationType & 0b000_00010) == 0) {
			coord3 = buffer.getShort();
		}
		if ((locationType & 0b000_00001) == 0) {
			qualityMeasure = buffer.get();
			locationAge = buffer.getShort();
		}
		return this;
	}
}
