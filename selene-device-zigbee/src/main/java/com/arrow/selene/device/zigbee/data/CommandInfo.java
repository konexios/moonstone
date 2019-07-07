package com.arrow.selene.device.zigbee.data;

import java.io.Serializable;

public class CommandInfo implements Serializable {
	private static final long serialVersionUID = 1461739317816453357L;

	public static final String UNKNOWN_COMMAND = "Unknown command";

	private byte commandId;
	private String name;

	public CommandInfo() {
	}

	public CommandInfo(byte commandId, String name) {
		this.commandId = commandId;
		if (name == null || name.isEmpty()) {
			name = UNKNOWN_COMMAND;
		}
		this.name = name;
	}

	public byte getCommandId() {
		return commandId;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return String.format("0x%02x (%s)", commandId, name);
	}
}
