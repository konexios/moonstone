package com.arrow.selene.device.xbee.zcl.domain.se.metering.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.arrow.selene.device.xbee.zcl.domain.se.price.data.UnitOfMeasure;

public class SetUncontrolledFlowThreshold extends ClusterSpecificCommand<SetUncontrolledFlowThreshold> {
	private long providerId;
	private long issuerEventId;
	private int uncontrolledFlowThreshold;
	private UnitOfMeasure unitOfMeasure;
	private int multiplier;
	private int divisor;
	private int stabilizationPeriod;
	private int measurementPeriod;

	public long getProviderId() {
		return providerId;
	}

	public SetUncontrolledFlowThreshold withProviderId(long providerId) {
		this.providerId = providerId;
		return this;
	}

	public long getIssuerEventId() {
		return issuerEventId;
	}

	public SetUncontrolledFlowThreshold withIssuerEventId(long issuerEventId) {
		this.issuerEventId = issuerEventId;
		return this;
	}

	public int getUncontrolledFlowThreshold() {
		return uncontrolledFlowThreshold;
	}

	public SetUncontrolledFlowThreshold withUncontrolledFlowThreshold(int uncontrolledFlowThreshold) {
		this.uncontrolledFlowThreshold = uncontrolledFlowThreshold;
		return this;
	}

	public UnitOfMeasure getUnitOfMeasure() {
		return unitOfMeasure;
	}

	public SetUncontrolledFlowThreshold withUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
		return this;
	}

	public int getMultiplier() {
		return multiplier;
	}

	public SetUncontrolledFlowThreshold withMultiplier(int multiplier) {
		this.multiplier = multiplier;
		return this;
	}

	public int getDivisor() {
		return divisor;
	}

	public SetUncontrolledFlowThreshold withDivisor(int divisor) {
		this.divisor = divisor;
		return this;
	}

	public int getStabilizationPeriod() {
		return stabilizationPeriod;
	}

	public SetUncontrolledFlowThreshold withStabilizationPeriod(int stabilizationPeriod) {
		this.stabilizationPeriod = stabilizationPeriod;
		return this;
	}

	public int getMeasurementPeriod() {
		return measurementPeriod;
	}

	public SetUncontrolledFlowThreshold withMeasurementPeriod(int measurementPeriod) {
		this.measurementPeriod = measurementPeriod;
		return this;
	}

	@Override
	protected int getId() {
		return SimpleMeteringClusterCommands.SET_UNCONTROLLED_FLOW_THRESHOLD_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(18);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt((int) providerId);
		buffer.putInt((int) issuerEventId);
		buffer.putShort((short) uncontrolledFlowThreshold);
		buffer.put((byte) unitOfMeasure.getValue());
		buffer.putShort((short) multiplier);
		buffer.putShort((short) divisor);
		buffer.put((byte) stabilizationPeriod);
		buffer.putShort((short) measurementPeriod);
		return buffer.array();
	}

	@Override
	public SetUncontrolledFlowThreshold fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 18, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		providerId = Integer.toUnsignedLong(buffer.getInt());
		issuerEventId = Integer.toUnsignedLong(buffer.getInt());
		uncontrolledFlowThreshold = Short.toUnsignedInt(buffer.getShort());
		unitOfMeasure = UnitOfMeasure.getByValue(Byte.toUnsignedInt(buffer.get()));
		multiplier = Short.toUnsignedInt(buffer.getShort());
		divisor = Short.toUnsignedInt(buffer.getShort());
		stabilizationPeriod = Byte.toUnsignedInt(buffer.get());
		measurementPeriod = Short.toUnsignedInt(buffer.getShort());
		return this;
	}
}
