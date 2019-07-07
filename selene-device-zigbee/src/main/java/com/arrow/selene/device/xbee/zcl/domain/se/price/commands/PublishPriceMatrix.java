package com.arrow.selene.device.xbee.zcl.domain.se.price.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Set;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.arrow.selene.device.xbee.zcl.domain.se.price.data.MatrixControl;
import com.arrow.selene.device.xbee.zcl.domain.se.price.data.PriceMatrix;

public class PublishPriceMatrix extends ClusterSpecificCommand<PublishPriceMatrix> {
	private long providerId;
	private long issuerEventId;
	private long startTime;
	private long issuerTariffId;
	private int commandIndex;
	private int totalNumberOfCommands;
	private Set<MatrixControl> matrixControls;
	private PriceMatrix[] priceMatrices;

	public long getProviderId() {
		return providerId;
	}

	public PublishPriceMatrix withProviderId(long providerId) {
		this.providerId = providerId;
		return this;
	}

	public long getIssuerEventId() {
		return issuerEventId;
	}

	public PublishPriceMatrix withIssuerEventId(long issuerEventId) {
		this.issuerEventId = issuerEventId;
		return this;
	}

	public long getStartTime() {
		return startTime;
	}

	public PublishPriceMatrix withStartTime(long startTime) {
		this.startTime = startTime;
		return this;
	}

	public long getIssuerTariffId() {
		return issuerTariffId;
	}

	public PublishPriceMatrix withIssuerTariffId(long issuerTariffId) {
		this.issuerTariffId = issuerTariffId;
		return this;
	}

	public int getCommandIndex() {
		return commandIndex;
	}

	public PublishPriceMatrix withCommandIndex(int commandIndex) {
		this.commandIndex = commandIndex;
		return this;
	}

	public int getTotalNumberOfCommands() {
		return totalNumberOfCommands;
	}

	public PublishPriceMatrix withTotalNumberOfCommands(int totalNumberOfCommands) {
		this.totalNumberOfCommands = totalNumberOfCommands;
		return this;
	}

	public Set<MatrixControl> getMatrixControls() {
		return matrixControls;
	}

	public PublishPriceMatrix withMatrixControls(Set<MatrixControl> matrixControls) {
		this.matrixControls = matrixControls;
		return this;
	}

	public PriceMatrix[] getPriceMatrices() {
		return priceMatrices;
	}

	public PublishPriceMatrix withPriceMatrices(PriceMatrix[] priceMatrices) {
		this.priceMatrices = priceMatrices;
		return this;
	}

	@Override
	protected int getId() {
		return PriceClusterCommands.PUBLISH_PRICE_MATRIX_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(19 + priceMatrices.length * 5);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt((int) providerId);
		buffer.putInt((int) issuerEventId);
		buffer.putInt((int) startTime);
		buffer.putInt((int) issuerTariffId);
		buffer.put((byte) commandIndex);
		buffer.put((byte) totalNumberOfCommands);
		buffer.put((byte) MatrixControl.getValue(matrixControls));
		for (PriceMatrix item : priceMatrices) {
			buffer.put((byte) item.getTierBlockId());
			buffer.putInt((int) item.getPrice());
		}
		return buffer.array();
	}

	@Override
	public PublishPriceMatrix fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length >= 19, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		providerId = Integer.toUnsignedLong(buffer.getInt());
		issuerEventId = Integer.toUnsignedLong(buffer.getInt());
		startTime = Integer.toUnsignedLong(buffer.getInt());
		issuerTariffId = Integer.toUnsignedLong(buffer.getInt());
		commandIndex = Byte.toUnsignedInt(buffer.get());
		totalNumberOfCommands = Byte.toUnsignedInt(buffer.get());
		matrixControls = MatrixControl.getByValue(buffer.get());
		priceMatrices = new PriceMatrix[buffer.remaining() / 5];
		for (int i = 0; i < priceMatrices.length; i++) {
			priceMatrices[i] = new PriceMatrix().withTierBlockId(Byte.toUnsignedInt(buffer.get())).withPrice(
					Integer.toUnsignedLong(buffer.getInt()));
		}
		return this;
	}
}
