package com.arrow.selene.device.harting.rfid.config.r500;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.arrow.selene.device.harting.rfid.config.ConfigParameter;

public class WlanSecuritySettings1 implements ConfigParameter<WlanSecuritySettings1> {
	private static final long serialVersionUID = -224562821901965656L;

	private SecurityMode securityMode;
	private byte[] ssid;

	private static final List<String> ALL = new ArrayList<>();

	public WlanSecuritySettings1(SecurityMode securityMode, byte[] ssid) {
		this.securityMode = securityMode;
		this.ssid = ssid;
	}

	@Override
	public byte[] build() {
		ByteBuffer buffer = ByteBuffer.allocate(30);
		buffer.put(0, (byte) SecurityMode.build(securityMode));
		buffer.put(1, (byte) ssid.length);
		buffer.put(ssid);
		return buffer.array();
	}

	@Override
	public WlanSecuritySettings1 parse(byte... payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		SecurityMode securityMode = SecurityMode.extract(Byte.toUnsignedInt(buffer.get(0)));
		byte[] ssid = new byte[buffer.get(1)];
		buffer.get(ssid);
		return new WlanSecuritySettings1(securityMode, ssid);
	}

	@Override
	public int getId() {
		return 0;
	}

	static class SecurityMode {
		private EncryptionType encryptionType;
		private AuthenticationType authenticationType;
		private boolean ssidEnabled;

		public SecurityMode(EncryptionType encryptionType, AuthenticationType authenticationType, boolean
				ssidEnabled) {
			this.encryptionType = encryptionType;
			this.authenticationType = authenticationType;
			this.ssidEnabled = ssidEnabled;
		}

		public static int build(SecurityMode value) {
			int result = 0;
			result |= value.ssidEnabled ? 0b10000000 : 0;
			result |= AuthenticationType.build(value.authenticationType) << 2;
			result |= EncryptionType.build(value.encryptionType);
			return result;
		}

		public static SecurityMode extract(int value) {
			boolean ssidEnabled = (value & 0b10000000) == 0b10000000;
			AuthenticationType authenticationType = AuthenticationType.extract(value >> 2 & 0x01);
			EncryptionType encryptionType = EncryptionType.extract(value & 0x03);
			return new SecurityMode(encryptionType, authenticationType, ssidEnabled);
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}
			SecurityMode that = (SecurityMode) o;
			return ssidEnabled == that.ssidEnabled && encryptionType == that.encryptionType &&
					authenticationType == that.authenticationType;
		}

		@Override
		public int hashCode() {

			return Objects.hash(encryptionType, authenticationType, ssidEnabled);
		}
	}

	enum EncryptionType {
		DISABLED,
		WEP,
		WPA,
		WPA2;

		public static int build(EncryptionType value) {
			return value.ordinal();
		}

		public static EncryptionType extract(int value) {
			for (EncryptionType item : values()) {
				if (item.ordinal() == value) {
					return item;
				}
			}
			return null;
		}
	}

	enum AuthenticationType {
		OPEN,
		RESTRICTED;

		public static int build(AuthenticationType value) {
			return value.ordinal();
		}

		public static AuthenticationType extract(int value) {
			for (AuthenticationType item : values()) {
				if (item.ordinal() == value) {
					return item;
				}
			}
			return null;
		}
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
		WlanSecuritySettings1 that = (WlanSecuritySettings1) o;
		return Objects.equals(securityMode, that.securityMode) && Arrays.equals(ssid, that.ssid);
	}

	@Override
	public int hashCode() {

		int result = Objects.hash(securityMode);
		result = 31 * result + Arrays.hashCode(ssid);
		return result;
	}
}
