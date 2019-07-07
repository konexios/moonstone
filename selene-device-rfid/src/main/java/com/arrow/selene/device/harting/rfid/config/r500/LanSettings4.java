package com.arrow.selene.device.harting.rfid.config.r500;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.arrow.selene.device.harting.rfid.config.ConfigParameter;

public class LanSettings4 implements ConfigParameter<LanSettings4> {
	private static final long serialVersionUID = -3143758252709550399L;

	private boolean dhcpEnabled;
	private boolean keepAliveEnabled;
	private int keepCount;
	private int keepIdle;
	private int keepInterval;

	private static final List<String> ALL = new ArrayList<>();

	public LanSettings4(boolean dhcpEnabled, boolean keepAliveEnabled, int keepCount, int keepIdle, int keepInterval) {
		this.dhcpEnabled = dhcpEnabled;
		this.keepAliveEnabled = keepAliveEnabled;
		this.keepCount = keepCount;
		this.keepIdle = keepIdle;
		this.keepInterval = keepInterval;
	}

	@Override
	public byte[] build() {
		ByteBuffer buffer = ByteBuffer.allocate(30);
		buffer.put(0, (byte) (dhcpEnabled ? 1 : 0));
		buffer.put(4, (byte) (keepAliveEnabled ? 1 : 0));
		buffer.put(5, (byte) keepCount);
		buffer.putShort(6, (short) keepIdle);
		buffer.putShort(8, (short) keepInterval);
		return buffer.array();
	}

	@Override
	public LanSettings4 parse(byte... payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		boolean dhcpEnabled = buffer.get(0) == 1;
		boolean keepAliveEnabled = buffer.get(4) == 1;
		int keepCount = Byte.toUnsignedInt(buffer.get(5));
		int keepIdle = Short.toUnsignedInt(buffer.getShort(6));
		int keepInterval = Short.toUnsignedInt(buffer.getShort(8));
		return new LanSettings4(dhcpEnabled, keepAliveEnabled, keepCount, keepIdle, keepInterval);
	}

	@Override
	public int getId() {
		return 0;
	}

	@Override
	public boolean updateState(String name, String value) {
		return false;
	}

	@Override
	public Map<String, String> getStates() {
		return Collections.emptyMap();
	}

	@Override
	public List<String> getParams() {
		return ALL;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		LanSettings4 that = (LanSettings4) o;
		return dhcpEnabled == that.dhcpEnabled && keepAliveEnabled == that.keepAliveEnabled &&
				keepCount == that.keepCount && keepIdle == that.keepIdle && keepInterval == that.keepInterval;
	}

	@Override
	public int hashCode() {

		return Objects.hash(dhcpEnabled, keepAliveEnabled, keepCount, keepIdle, keepInterval);
	}
}
