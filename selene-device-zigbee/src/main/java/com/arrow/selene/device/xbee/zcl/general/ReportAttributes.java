package com.arrow.selene.device.xbee.zcl.general;

import java.util.Collections;
import java.util.List;

import com.arrow.selene.device.xbee.zcl.data.AttributeRecord;

public class ReportAttributes extends GeneralResponse<ReportAttributes> {
	private List<AttributeRecord> attributes = Collections.emptyList();

	public List<AttributeRecord> getAttributes() {
		return attributes;
	}

	@Override
	protected ReportAttributes fromPayload(byte[] payload) {
		attributes = AttributeRecord.parse(payload);
		return this;
	}

	@Override
	protected int getId() {
		return HaProfileCommands.REPORT_ATTRIBUTES;
	}
}
