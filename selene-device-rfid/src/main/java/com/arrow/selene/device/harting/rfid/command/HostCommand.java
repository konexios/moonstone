package com.arrow.selene.device.harting.rfid.command;

public abstract class HostCommand extends AbstractExtendedCommand {
	private static final long serialVersionUID = -990929053381009938L;
	private static final int CONTROL = 0xb0;

	@Override
	protected int getControl() {
		return CONTROL;
	}

	@Override
	protected int getLength() {
		return 1;
	}
}
