package com.arrow.selene.device.harting.rfid.command;

import java.nio.ByteBuffer;

import com.arrow.selene.device.harting.rfid.command.ReadConfiguration.CfgAddress;
import com.arrow.selene.device.harting.rfid.config.ConfigParameter;

public class WriteConfiguration extends AbstractExtendedCommand {
	private static final long serialVersionUID = 7718661181790094110L;
	private static final int LENGTH = 15;
	private static final int CONTROL = 0x81;
	private CfgAddress cfgAddress;
	private ConfigParameter<?> parameter;

	public void setCfgAddress(CfgAddress cfgAddress) {
		this.cfgAddress = cfgAddress;
	}

	public void setParameter(ConfigParameter<?> parameter) {
		this.parameter = parameter;
	}

	public WriteConfiguration withCfgAddress(CfgAddress cfgAddress) {
		this.cfgAddress = cfgAddress;
		return this;
	}

	public WriteConfiguration withParameter(ConfigParameter<?> parameter) {
		this.parameter = parameter;
		return this;
	}

	@Override
	protected byte[] buildPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(LENGTH);
		buffer.put((byte) CfgAddress.build(cfgAddress));
		buffer.put(parameter.build());
		return buffer.array();
	}

	@Override
	protected int getControl() {
		return CONTROL;
	}

	@Override
	protected int getLength() {
		return LENGTH;
	}
}
