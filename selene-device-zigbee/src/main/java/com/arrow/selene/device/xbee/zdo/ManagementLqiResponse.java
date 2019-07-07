package com.arrow.selene.device.xbee.zdo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.arrow.selene.device.xbee.zcl.ZclStatus;
import com.arrow.selene.device.xbee.zdo.data.Neighbor;

public class ManagementLqiResponse {
	private int status;
	private int totalNumberOfEntries;
	private int startIndex;
	private Neighbor[] neighbors;

	public int getStatus() {
		return status;
	}

	public ManagementLqiResponse withStatus(int status) {
		this.status = status;
		return this;
	}

	public int getTotalNumberOfEntries() {
		return totalNumberOfEntries;
	}

	public ManagementLqiResponse withTotalNumberOfEntries(int totalNumberOfEntries) {
		this.totalNumberOfEntries = totalNumberOfEntries;
		return this;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public ManagementLqiResponse withStartIndex(int startIndex) {
		this.startIndex = startIndex;
		return this;
	}

	public Neighbor[] getNeighbors() {
		return neighbors;
	}

	public ManagementLqiResponse withNeighbors(Neighbor[] neighbors) {
		this.neighbors = neighbors;
		return this;
	}

	public static ManagementLqiResponse fromPayload(byte[] payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.get(); // sequence
		byte status = buffer.get();
		ManagementLqiResponse result = new ManagementLqiResponse().withStatus(status);
		if (status == ZclStatus.SUCCESS) {
			result.withTotalNumberOfEntries(Byte.toUnsignedInt(buffer.get()));
			result.withStartIndex(Byte.toUnsignedInt(buffer.get()));
			buffer.get(); // skip length
			byte[] bytes = new byte[buffer.remaining()];
			buffer.get(bytes);
			result.withNeighbors(Neighbor.fromPayload(bytes));
		}
		return result;
	}
}
