package com.arrow.selene.device.harting.rfid.command;

public abstract class EpcHostCommand extends AbstractExtendedCommand {
	private static final long serialVersionUID = 8735277334525968506L;
	private static final int CONTROL = 0xb3;

	@Override
	protected int getControl() {
		return CONTROL;
	}
}
