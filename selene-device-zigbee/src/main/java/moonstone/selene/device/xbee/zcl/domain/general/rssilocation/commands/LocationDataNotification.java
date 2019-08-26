package moonstone.selene.device.xbee.zcl.domain.general.rssilocation.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class LocationDataNotification extends ClusterSpecificCommand<LocationDataNotification> {
	private byte locationType;
	private short coord1;
	private short coord2;
	private short coord3;
	private short power;
	private short pathLossExponent;
	private byte locationMethod;
	private byte qualityMeasure;
	private short locationAge;

	public byte getLocationType() {
		return locationType;
	}

	public LocationDataNotification withLocationType(byte locationType) {
		this.locationType = locationType;
		return this;
	}

	public short getCoord1() {
		return coord1;
	}

	public LocationDataNotification withCoord1(short coord1) {
		this.coord1 = coord1;
		return this;
	}

	public short getCoord2() {
		return coord2;
	}

	public LocationDataNotification withCoord2(short coord2) {
		this.coord2 = coord2;
		return this;
	}

	public short getCoord3() {
		return coord3;
	}

	public LocationDataNotification withCoord3(short coord3) {
		this.coord3 = coord3;
		return this;
	}

	public short getPower() {
		return power;
	}

	public LocationDataNotification withPower(short power) {
		this.power = power;
		return this;
	}

	public short getPathLossExponent() {
		return pathLossExponent;
	}

	public LocationDataNotification withPathLossExponent(short pathLossExponent) {
		this.pathLossExponent = pathLossExponent;
		return this;
	}

	public byte getLocationMethod() {
		return locationMethod;
	}

	public LocationDataNotification withLocationMethod(byte locationMethod) {
		this.locationMethod = locationMethod;
		return this;
	}

	public byte getQualityMeasure() {
		return qualityMeasure;
	}

	public LocationDataNotification withQualityMeasure(byte qualityMeasure) {
		this.qualityMeasure = qualityMeasure;
		return this;
	}

	public short getLocationAge() {
		return locationAge;
	}

	public LocationDataNotification withLocationAge(short locationAge) {
		this.locationAge = locationAge;
		return this;
	}

	@Override
	protected int getId() {
		return RssiLocationClusterCommands.LOCATION_DATA_NOTIFICATION_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(
				9 + ((locationType & 0b000_00010) == 0 ? 2 : 0) + ((locationType & 0b000_00001) == 0 ? 4 : 0));
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put(locationType);
		buffer.putShort(coord1);
		buffer.putShort(coord2);
		if ((locationType & 0b000_00010) == 0) {
			buffer.putShort(coord3);
		}
		buffer.putShort(power);
		buffer.putShort(pathLossExponent);
		if ((locationType & 0b000_00001) == 0) {
			buffer.put(locationMethod);
			buffer.put(qualityMeasure);
			buffer.putShort(locationAge);
		}
		return buffer.array();
	}

	@Override
	public LocationDataNotification fromPayload(byte[] payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		locationType = buffer.get();
		coord1 = buffer.getShort();
		coord2 = buffer.getShort();
		if ((locationType & 0b000_00010) == 0) {
			coord3 = buffer.getShort();
		}
		power = buffer.getShort();
		pathLossExponent = buffer.getShort();
		if ((locationType & 0b000_00001) == 0) {
			locationMethod = buffer.get();
			qualityMeasure = buffer.get();
			locationAge = buffer.getShort();
		}
		return this;
	}
}
