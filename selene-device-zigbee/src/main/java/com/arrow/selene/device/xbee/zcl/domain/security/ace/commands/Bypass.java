package com.arrow.selene.device.xbee.zcl.domain.security.ace.commands;

import java.nio.ByteBuffer;
import java.util.List;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class Bypass extends ClusterSpecificCommand<Bypass> {
	private byte numberOfZones;
	private List<Byte> zones;

	public byte getNumberOfZones() {
		return numberOfZones;
	}

	public Bypass withNumberOfZones(byte numberOfZones) {
		this.numberOfZones = numberOfZones;
		return this;
	}

	public List<Byte> getZones() {
		return zones;
	}

	public Bypass withZones(List<Byte> zones) {
		this.zones = zones;
		return this;
	}

	@Override
	protected int getId() {
		return SecurityAceClusterCommands.BYPASS_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(1 + zones.size());
		buffer.put(numberOfZones);
		for (Byte zone : zones) {
			buffer.put(zone);
		}
		return buffer.array();
	}

	@Override
	public Bypass fromPayload(byte[] payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		numberOfZones = buffer.get();
		zones.clear();
		for (int i = 0; i < numberOfZones; i++) {
			zones.add(buffer.get());
		}
		return this;
	}
}
