package moonstone.selene.device.xbee.zcl.domain.general.rssilocation.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;
import moonstone.selene.device.xbee.zcl.ZclStatus;

public class LocationDataResponse extends ClusterSpecificCommand<LocationDataResponse> {
	private byte status;
	private byte locationType;
	private short coord1;
	private short coord2;
	private short coord3;
	private short power;
	private short pathLossExponent;
	private byte locationMethod;
	private byte qualityMeasure;
	private short locationAge;

	public byte getStatus() {
		return status;
	}

	public LocationDataResponse withStatus(byte status) {
		this.status = status;
		return this;
	}

	public byte getLocationType() {
		return locationType;
	}

	public LocationDataResponse withLocationType(byte locationType) {
		this.locationType = locationType;
		return this;
	}

	public short getCoord1() {
		return coord1;
	}

	public LocationDataResponse withCoord1(short coord1) {
		this.coord1 = coord1;
		return this;
	}

	public short getCoord2() {
		return coord2;
	}

	public LocationDataResponse withCoord2(short coord2) {
		this.coord2 = coord2;
		return this;
	}

	public short getCoord3() {
		return coord3;
	}

	public LocationDataResponse withCoord3(short coord3) {
		this.coord3 = coord3;
		return this;
	}

	public short getPower() {
		return power;
	}

	public LocationDataResponse withPower(short power) {
		this.power = power;
		return this;
	}

	public short getPathLossExponent() {
		return pathLossExponent;
	}

	public LocationDataResponse withPathLossExponent(short pathLossExponent) {
		this.pathLossExponent = pathLossExponent;
		return this;
	}

	public byte getLocationMethod() {
		return locationMethod;
	}

	public LocationDataResponse withLocationMethod(byte locationMethod) {
		this.locationMethod = locationMethod;
		return this;
	}

	public byte getQualityMeasure() {
		return qualityMeasure;
	}

	public LocationDataResponse withQualityMeasure(byte qualityMeasure) {
		this.qualityMeasure = qualityMeasure;
		return this;
	}

	public short getLocationAge() {
		return locationAge;
	}

	public LocationDataResponse withLocationAge(short locationAge) {
		this.locationAge = locationAge;
		return this;
	}

	@Override
	protected int getId() {
		return RssiLocationClusterCommands.LOCATION_DATA_RESPONSE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(status == ZclStatus.SUCCESS ? 16 : 1);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put(status);
		if (status == ZclStatus.SUCCESS) {
			buffer.put(locationType);
			buffer.putShort(coord1);
			buffer.putShort(coord2);
			buffer.putShort(coord3);
			buffer.putShort(power);
			buffer.putShort(pathLossExponent);
			buffer.put(locationMethod);
			buffer.put(qualityMeasure);
			buffer.putShort(locationAge);
		}
		return buffer.array();
	}

	@Override
	public LocationDataResponse fromPayload(byte[] payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		status = buffer.get();
		if (status == ZclStatus.SUCCESS) {
			locationType = buffer.get();
			coord1 = buffer.getShort();
			coord2 = buffer.getShort();
			coord3 = buffer.getShort();
			power = buffer.getShort();
			pathLossExponent = buffer.getShort();
			locationMethod = buffer.get();
			qualityMeasure = buffer.get();
			locationAge = buffer.getShort();
		}
		return this;
	}
}
