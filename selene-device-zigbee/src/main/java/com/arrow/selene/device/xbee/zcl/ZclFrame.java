package com.arrow.selene.device.xbee.zcl;

import com.arrow.selene.Loggable;

public abstract class ZclFrame extends Loggable {
	protected ZclHeader header = new ZclHeader();

	protected abstract int getId();

	public ZclHeader getHeader() {
		return header;
	}
}
