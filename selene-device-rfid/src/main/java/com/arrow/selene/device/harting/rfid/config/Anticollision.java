package com.arrow.selene.device.harting.rfid.config;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.SerializationUtils;

public class Anticollision implements ConfigParameter<Anticollision> {
	private static final long serialVersionUID = -5685508301701108085L;

	private static final int ID = 5;

	private static final String TRANSPONDER_ANTICOLLISION_ENABLE = "Transponder_Anticollision_Enable";
	private boolean anticollision;
	private Session session;

	private static final List<String> ALL = new ArrayList<>();

	static {
		ALL.add(TRANSPONDER_ANTICOLLISION_ENABLE);
	}

	public Anticollision withAnticollision(boolean anticollision) {
		this.anticollision = anticollision;
		return this;
	}

	public Anticollision withSession(Session session) {
		this.session = session;
		return this;
	}

	@Override
	public byte[] build() {
		ByteBuffer buffer = ByteBuffer.allocate(14);
		buffer.put(10, (byte) Session.build(session));
		buffer.put(11, (byte) (anticollision ? 4 : 0));
		return buffer.array();
	}

	@Override
	public Anticollision parse(byte... payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		Session session = Session.extract(buffer.get(10));
		boolean anticollision = (buffer.get(11) & 0x04) == 4;
		return new Anticollision().withAnticollision(anticollision).withSession(session);
	}

	@Override
	public int getId() {
		return ID;
	}

	enum Session {
		SESSION0,
		SESSION1,
		SESSION2,
		SESSION3;

		public static Session extract(int value) {
			for (Session item : values()) {
				if (value == item.ordinal()) {
					return item;
				}
			}
			return null;
		}

		public static int build(Session value) {
			return value.ordinal();
		}
	}

	@Override
	public boolean updateState(String name, String value) {
		Anticollision stored = SerializationUtils.clone(this);
		switch (name) {
			case TRANSPONDER_ANTICOLLISION_ENABLE: {
				anticollision = Boolean.parseBoolean(value);
				break;
			}
		}
		return !equals(stored);
	}

	@Override
	public Map<String, String> getStates() {
		Map<String, String> result = new HashMap<>();
		result.put(TRANSPONDER_ANTICOLLISION_ENABLE, Boolean.toString(anticollision));
		return result;
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
		Anticollision that = (Anticollision) o;
		return anticollision == that.anticollision && session == that.session;
	}

	@Override
	public int hashCode() {
		return Objects.hash(anticollision, session);
	}
}
