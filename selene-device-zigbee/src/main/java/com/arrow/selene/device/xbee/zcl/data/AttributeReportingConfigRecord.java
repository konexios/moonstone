package com.arrow.selene.device.xbee.zcl.data;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import com.arrow.selene.device.xbee.zcl.ZclDataType;
import com.arrow.selene.device.xbee.zcl.ZclStatus;

public class AttributeReportingConfigRecord {
	private static final int DIRECTION_LENGTH = 1;
	private static final int ATTRIBUTE_ID_LENGTH = 2;
	private static final int DATA_TYPE_LENGTH = 1;
	private static final int MINIMUM_REPORTING_INTERVAL_LENGTH = 2;
	private static final int MAXIMUM_REPORTING_INTERVAL_LENGTH = 2;
	private static final int TIMEOUT_PERIOD_LENGTH = 2;
	private static final int SEND_DIRECTION = 0x00;
	private static final int EXPECT_DIRECTION = 0x01;

	private int status;
	private int direction;
	private int attributeId;
	private ZclDataType attributeDataType;
	private int minimumReportingInterval;
	private int maximumReportingInterval;
	private byte[] reportableChange;
	private int timeoutPeriod;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public int getAttributeId() {
		return attributeId;
	}

	public void setAttributeId(int attributeId) {
		this.attributeId = attributeId;
	}

	public ZclDataType getAttributeDataType() {
		return attributeDataType;
	}

	public void setAttributeDataType(ZclDataType attributeDataType) {
		this.attributeDataType = attributeDataType;
	}

	public int getMinimumReportingInterval() {
		return minimumReportingInterval;
	}

	public void setMinimumReportingInterval(int minimumReportingInterval) {
		this.minimumReportingInterval = minimumReportingInterval;
	}

	public int getMaximumReportingInterval() {
		return maximumReportingInterval;
	}

	public void setMaximumReportingInterval(int maximumReportingInterval) {
		this.maximumReportingInterval = maximumReportingInterval;
	}

	public byte[] getReportableChange() {
		return reportableChange;
	}

	public void setReportableChange(byte[] reportableChange) {
		this.reportableChange = reportableChange;
	}

	public int getTimeoutPeriod() {
		return timeoutPeriod;
	}

	public void setTimeoutPeriod(int timeoutPeriod) {
		this.timeoutPeriod = timeoutPeriod;
	}

	public static List<AttributeReportingConfigRecord> parse(byte... payload) {
		List<AttributeReportingConfigRecord> records = new ArrayList<>();
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		while (buffer.hasRemaining()) {
			AttributeReportingConfigRecord record = new AttributeReportingConfigRecord();
			int status = Byte.toUnsignedInt(buffer.get());
			record.setStatus(status);
			int direction = Byte.toUnsignedInt(buffer.get());
			record.setDirection(direction);
			record.setAttributeId(Short.toUnsignedInt(buffer.getShort()));
			if (status == ZclStatus.SUCCESS) {
				if (direction == SEND_DIRECTION) {
					ZclDataType dataType = ZclDataType.findById(Byte.toUnsignedInt(buffer.get()));
					record.setAttributeDataType(dataType);
					record.setMinimumReportingInterval(Short.toUnsignedInt(buffer.getShort()));
					record.setMaximumReportingInterval(Short.toUnsignedInt(buffer.getShort()));
					if (dataType.isAnalog()) {
						byte[] change = new byte[dataType.getLength()];
						buffer.get(change);
						record.setReportableChange(change);
					}
				} else {
					if (direction == EXPECT_DIRECTION) {
						record.setTimeoutPeriod(Short.toUnsignedInt(buffer.getShort()));
					}
				}
			}
			records.add(record);
		}
		return records;
	}

	public int calcSize() {
		int size = 0;
		if (direction == SEND_DIRECTION) {
			size = DIRECTION_LENGTH + ATTRIBUTE_ID_LENGTH + DATA_TYPE_LENGTH + MINIMUM_REPORTING_INTERVAL_LENGTH +
					MAXIMUM_REPORTING_INTERVAL_LENGTH + (attributeDataType.isAnalog() ? reportableChange.length : 0);
		} else if (direction == EXPECT_DIRECTION) {
			size = DIRECTION_LENGTH + ATTRIBUTE_ID_LENGTH + TIMEOUT_PERIOD_LENGTH;
		}
		return size;
	}

	public byte[] buildPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(calcSize());
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		if (direction == SEND_DIRECTION) {
			buffer.put((byte) direction);
			buffer.putShort((short) attributeId);
			buffer.put((byte) attributeDataType.getId());
			buffer.putShort((short) minimumReportingInterval);
			buffer.putShort((short) maximumReportingInterval);
			if (attributeDataType.isAnalog()) {
				buffer.put(reportableChange);
			}
		} else if (direction == EXPECT_DIRECTION) {
			buffer.put((byte) direction);
			buffer.putShort((short) attributeId);
			buffer.putShort((short) timeoutPeriod);
		}
		return buffer.array();
	}
}
