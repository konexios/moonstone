package com.arrow.selene.device.xbee.zcl.general;

import java.util.Collections;
import java.util.List;

import com.arrow.selene.device.xbee.zcl.data.WriteAttributeStatusRecord;

@SuppressWarnings("rawtypes")
public class WriteAttributesResponse extends GeneralResponse {
	private List<WriteAttributeStatusRecord> statuses = Collections.emptyList();

	public List<WriteAttributeStatusRecord> getStatuses() {
		return statuses;
	}

	@Override
	protected WriteAttributesResponse fromPayload(byte[] payload) {
		statuses = WriteAttributeStatusRecord.parse(payload);
		return this;
	}

	@Override
	protected int getId() {
		return HaProfileCommands.WRITE_ATTRIBUTES_RSP;
	}
}
