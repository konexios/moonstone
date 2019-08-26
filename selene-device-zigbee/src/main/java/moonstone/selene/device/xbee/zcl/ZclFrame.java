package moonstone.selene.device.xbee.zcl;

import moonstone.selene.Loggable;

public abstract class ZclFrame extends Loggable {
	protected ZclHeader header = new ZclHeader();

	protected abstract int getId();

	public ZclHeader getHeader() {
		return header;
	}
}
