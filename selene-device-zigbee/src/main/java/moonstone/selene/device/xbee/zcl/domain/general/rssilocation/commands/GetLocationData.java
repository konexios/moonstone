package moonstone.selene.device.xbee.zcl.domain.general.rssilocation.commands;

import java.nio.ByteBuffer;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class GetLocationData extends ClusterSpecificCommand<GetLocationData> {
	private boolean compactResponse;
	private boolean broadcastResponse;
	private boolean broadcastIndicator;
	private boolean recalculate;
	private boolean absoluteOnly;
	private byte numberResponses;
	private byte[] targetAddress;

	public boolean isCompactResponse() {
		return compactResponse;
	}

	public GetLocationData withCompactResponse(boolean compactResponse) {
		this.compactResponse = compactResponse;
		return this;
	}

	public boolean isBroadcastResponse() {
		return broadcastResponse;
	}

	public GetLocationData withBroadcastResponse(boolean broadcastResponse) {
		this.broadcastResponse = broadcastResponse;
		return this;
	}

	public boolean isBroadcastIndicator() {
		return broadcastIndicator;
	}

	public GetLocationData withBroadcastIndicator(boolean broadcastIndicator) {
		this.broadcastIndicator = broadcastIndicator;
		return this;
	}

	public boolean isRecalculate() {
		return recalculate;
	}

	public GetLocationData withRecalculate(boolean recalculate) {
		this.recalculate = recalculate;
		return this;
	}

	public boolean isAbsoluteOnly() {
		return absoluteOnly;
	}

	public GetLocationData withAbsoluteOnly(boolean absoluteOnly) {
		this.absoluteOnly = absoluteOnly;
		return this;
	}

	public byte getNumberResponses() {
		return numberResponses;
	}

	public GetLocationData withNumberResponses(byte numberResponses) {
		this.numberResponses = numberResponses;
		return this;
	}

	public byte[] getTargetAddress() {
		return targetAddress;
	}

	public GetLocationData withTargetAddress(byte[] targetAddress) {
		this.targetAddress = targetAddress;
		return this;
	}

	@Override
	protected int getId() {
		return RssiLocationClusterCommands.GET_LOCATION_DATA_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		int bitmap = 0b000_00000;
		if (compactResponse) {
			bitmap |= 0b000_10000;
		}
		if (broadcastResponse) {
			bitmap |= 0b000_01000;
		}
		if (broadcastIndicator) {
			bitmap |= 0b000_00100;
		}
		if (recalculate) {
			bitmap |= 0b000_00010;
		}
		if (absoluteOnly) {
			bitmap |= 0b000_00001;
		}
		ByteBuffer buffer = ByteBuffer.allocate(broadcastIndicator ? 2 : 10);
		buffer.put((byte) bitmap);
		buffer.put(numberResponses);
		if (!broadcastIndicator) {
			buffer.put(targetAddress);
		}
		return targetAddress;
	}

	@Override
	public GetLocationData fromPayload(byte[] payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		byte bitmap = buffer.get();
		compactResponse = (bitmap & 0b000_10000) != 0;
		broadcastResponse = (bitmap & 0b000_01000) != 0;
		broadcastIndicator = (bitmap & 0b000_00100) != 0;
		recalculate = (bitmap & 0b000_00010) != 0;
		absoluteOnly = (bitmap & 0b000_00001) != 0;
		numberResponses = buffer.get();
		if (!broadcastIndicator) {
			targetAddress = new byte[8];
			buffer.get(targetAddress);
		}
		return this;
	}
}
