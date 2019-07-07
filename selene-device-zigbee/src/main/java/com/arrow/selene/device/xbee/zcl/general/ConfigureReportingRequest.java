package com.arrow.selene.device.xbee.zcl.general;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;

import com.arrow.selene.device.xbee.zcl.data.AttributeReportingConfigRecord;

public class ConfigureReportingRequest extends GeneralRequest {
	List<AttributeReportingConfigRecord> configs = Collections.emptyList();

	public ConfigureReportingRequest(int manufacturerCode, List<AttributeReportingConfigRecord> configs) {
		super(manufacturerCode);
		this.configs = configs;
	}

	@Override
	protected byte[] toPayload() {
		int size = 0;
		for (AttributeReportingConfigRecord config : configs) {
			size += config.calcSize();
		}
		ByteBuffer buffer = ByteBuffer.allocate(size);
		for (AttributeReportingConfigRecord config : configs) {
			buffer.put(config.buildPayload());
		}
		return buffer.array();
	}

	@Override
	protected int getId() {
		return HaProfileCommands.CONFIGURE_REPORTING;
	}
}
