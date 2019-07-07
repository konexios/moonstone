package com.arrow.selene.device.xbee.zcl.general;

import java.util.List;

import com.arrow.selene.device.xbee.zcl.data.AttributeRecord;

public class WriteAttributesNoResponse extends WriteAttributes {
	public WriteAttributesNoResponse(int manufacturerCode, List<AttributeRecord> attributes) {
		super(manufacturerCode, attributes);
	}

	@Override
	protected int getId() {
		return HaProfileCommands.WRITE_ATTRIBUTES_NO_RESPONSE;
	}
}
