package com.arrow.selene.device.xbee.zcl.domain.general.scenes.data;

import java.util.Arrays;

public class ExtensionField {
	private int clusterId;
	private byte[] value;

	public int getClusterId() {
		return clusterId;
	}

	public ExtensionField withClusterId(int clusterId) {
		this.clusterId = clusterId;
		return this;
	}

	public byte[] getValue() {
		return value;
	}

	public ExtensionField withValue(byte[] value) {
		this.value = value;
		return this;
	}

	public int calcSize() {
		return 1 + 1 + value.length;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		ExtensionField that = (ExtensionField) o;

		return clusterId == that.clusterId && Arrays.equals(value, that.value);
	}

	@Override
	public int hashCode() {
		int result = clusterId;
		result = 31 * result + Arrays.hashCode(value);
		return result;
	}
}
