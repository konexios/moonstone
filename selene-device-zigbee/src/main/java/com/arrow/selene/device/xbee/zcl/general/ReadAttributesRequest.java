package com.arrow.selene.device.xbee.zcl.general;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ReadAttributesRequest extends GeneralRequest {
	int[] ids;

	public ReadAttributesRequest(int manufacturerCode, int... ids) {
		super(manufacturerCode);
		this.ids = ids;
	}

	@Override
	protected byte[] toPayload() {
		ByteBuffer stream = ByteBuffer.allocate(ids.length * 2);
		stream.order(ByteOrder.LITTLE_ENDIAN);
		for (int id : ids) {
			stream.putShort((short) id);
		}
		return stream.array();
	}

	@Override
	protected int getId() {
		return HaProfileCommands.READ_ATTRIBUTES;
	}
}
