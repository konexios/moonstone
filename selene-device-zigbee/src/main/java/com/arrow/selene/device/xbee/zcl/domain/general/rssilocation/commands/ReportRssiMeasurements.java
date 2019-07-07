package com.arrow.selene.device.xbee.zcl.domain.general.rssilocation.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.arrow.selene.device.xbee.zcl.domain.general.rssilocation.data.Neighbor;

public class ReportRssiMeasurements extends ClusterSpecificCommand<ReportRssiMeasurements> {
	private byte[] reportingAddress;
	private byte numberOfNeighbors;
	private List<Neighbor> neighbors;

	public byte[] getReportingAddress() {
		return reportingAddress;
	}

	public ReportRssiMeasurements withReportingAddress(byte[] reportingAddress) {
		this.reportingAddress = reportingAddress;
		return this;
	}

	public byte getNumberOfNeighbors() {
		return numberOfNeighbors;
	}

	public ReportRssiMeasurements withNumberOfNeighbors(byte numberOfNeighbors) {
		this.numberOfNeighbors = numberOfNeighbors;
		return this;
	}

	public List<Neighbor> getNeighbors() {
		return neighbors;
	}

	public ReportRssiMeasurements withNeighbors(List<Neighbor> neighbors) {
		this.neighbors = neighbors;
		return this;
	}

	@Override
	protected int getId() {
		return RssiLocationClusterCommands.REPORT_RSSI_MEASUREMENTS_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(9 + neighbors.size() * 16);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put(reportingAddress);
		buffer.put(numberOfNeighbors);
		for (Neighbor neighbor : neighbors) {
			buffer.put(neighbor.getNeighborAddress());
			buffer.putShort(neighbor.getCoord1());
			buffer.putShort(neighbor.getCoord2());
			buffer.putShort(neighbor.getCoord3());
			buffer.put(neighbor.getRssi());
			buffer.put(neighbor.getNumberRssiMeasurements());
		}
		return buffer.array();
	}

	@Override
	public ReportRssiMeasurements fromPayload(byte[] payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		reportingAddress = new byte[8];
		buffer.get(reportingAddress);
		numberOfNeighbors = buffer.get();
		neighbors.clear();
		for (int i = 0; i < numberOfNeighbors; i++) {
			byte[] neibghorAddress = new byte[8];
			buffer.get(neibghorAddress);
			short coord1 = buffer.getShort();
			short coord2 = buffer.getShort();
			short coord3 = buffer.getShort();
			byte rssi = buffer.get();
			byte numberRssiMeasurements = buffer.get();
			neighbors.add(new Neighbor().withNeighborAddress(neibghorAddress).withCoord1(coord1).withCoord2(coord2)
					.withCoord3(coord3).withRssi(rssi).withNumberRssiMeasurements(numberRssiMeasurements));
		}
		return this;
	}
}
