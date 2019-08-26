package moonstone.selene.device.harting.rfid.config.r500;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import moonstone.selene.device.harting.rfid.config.ConfigParameter;

public class WlanSecuritySettings3Cont implements ConfigParameter<WlanSecuritySettings3Cont> {
	private static final long serialVersionUID = -502885388415206515L;

	private byte[] wpaKey;

	private static final List<String> ALL = new ArrayList<>();

	public WlanSecuritySettings3Cont(byte[] wpaKey) {
		this.wpaKey = wpaKey;
	}

	@Override
	public byte[] build() {
		return wpaKey;
	}

	@Override
	public WlanSecuritySettings3Cont parse(byte... payload) {
		return new WlanSecuritySettings3Cont(payload);
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
		WlanSecuritySettings3Cont that = (WlanSecuritySettings3Cont) o;
		return Arrays.equals(wpaKey, that.wpaKey);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(wpaKey);
	}
}
