package moonstone.selene.device.harting.rfid.config.r500;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import moonstone.selene.device.harting.rfid.config.ConfigParameter;

public class WlanSecuritySettings2 implements ConfigParameter<WlanSecuritySettings2> {
	private static final long serialVersionUID = -2308438506778263775L;

	private byte[] wepKey;

	private static final List<String> ALL = new ArrayList<>();

	public WlanSecuritySettings2(byte[] wepKey) {
		this.wepKey = wepKey;
	}

	@Override
	public byte[] build() {
		ByteBuffer buffer = ByteBuffer.allocate(30);
		buffer.put(0, (byte) wepKey.length);
		buffer.put(wepKey);
		return buffer.array();
	}

	@Override
	public WlanSecuritySettings2 parse(byte... payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		byte[] wepKey = new byte[buffer.get(0)];
		buffer.get(wepKey);
		return new WlanSecuritySettings2(wepKey);
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
		WlanSecuritySettings2 that = (WlanSecuritySettings2) o;
		return Arrays.equals(wepKey, that.wepKey);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(wepKey);
	}
}
