package com.arrow.selene.device.xbee.zcl.domain.se.price.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.arrow.selene.device.xbee.zcl.domain.se.price.data.CreditPaymentStatus;

public class PublishCreditPayment extends ClusterSpecificCommand<PublishCreditPayment> {
	private long providerId;
	private long issuerEventId;
	private long creditPaymentDueDate;
	private long creditPaymentOverdueAmount;
	private CreditPaymentStatus creditPaymentStatus;
	private long creditPayment;
	private long creditPaymentDate;
	private String creditPaymentRef;

	public long getProviderId() {
		return providerId;
	}

	public PublishCreditPayment withProviderId(long providerId) {
		this.providerId = providerId;
		return this;
	}

	public long getIssuerEventId() {
		return issuerEventId;
	}

	public PublishCreditPayment withIssuerEventId(long issuerEventId) {
		this.issuerEventId = issuerEventId;
		return this;
	}

	public long getCreditPaymentDueDate() {
		return creditPaymentDueDate;
	}

	public PublishCreditPayment withCreditPaymentDueDate(long creditPaymentDueDate) {
		this.creditPaymentDueDate = creditPaymentDueDate;
		return this;
	}

	public long getCreditPaymentOverdueAmount() {
		return creditPaymentOverdueAmount;
	}

	public PublishCreditPayment withCreditPaymentOverdueAmount(long creditPaymentOverdueAmount) {
		this.creditPaymentOverdueAmount = creditPaymentOverdueAmount;
		return this;
	}

	public CreditPaymentStatus getCreditPaymentStatus() {
		return creditPaymentStatus;
	}

	public PublishCreditPayment withCreditPaymentStatus(CreditPaymentStatus creditPaymentStatus) {
		this.creditPaymentStatus = creditPaymentStatus;
		return this;
	}

	public long getCreditPayment() {
		return creditPayment;
	}

	public PublishCreditPayment withCreditPayment(long creditPayment) {
		this.creditPayment = creditPayment;
		return this;
	}

	public long getCreditPaymentDate() {
		return creditPaymentDate;
	}

	public PublishCreditPayment withCreditPaymentDate(long creditPaymentDate) {
		this.creditPaymentDate = creditPaymentDate;
		return this;
	}

	public String getCreditPaymentRef() {
		return creditPaymentRef;
	}

	public PublishCreditPayment withCreditPaymentRef(String creditPaymentRef) {
		this.creditPaymentRef = creditPaymentRef;
		return this;
	}

	@Override
	protected int getId() {
		return PriceClusterCommands.PUBLISH_CREDIT_PAYMENT_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(26 + creditPaymentRef.length());
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt((int) providerId);
		buffer.putInt((int) issuerEventId);
		buffer.putInt((int) creditPaymentDueDate);
		buffer.putInt((int) creditPaymentOverdueAmount);
		buffer.put((byte) creditPaymentStatus.ordinal());
		buffer.putInt((int) creditPayment);
		buffer.putInt((int) creditPaymentDate);
		buffer.put((byte) creditPaymentRef.length());
		buffer.put(creditPaymentRef.getBytes(StandardCharsets.UTF_8));
		return buffer.array();
	}

	@Override
	public PublishCreditPayment fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length >= 26 && payload.length <= 46, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		providerId = Integer.toUnsignedLong(buffer.getInt());
		issuerEventId = Integer.toUnsignedLong(buffer.getInt());
		creditPaymentDueDate = Integer.toUnsignedLong(buffer.getInt());
		creditPaymentOverdueAmount = Integer.toUnsignedLong(buffer.getInt());
		creditPaymentStatus = CreditPaymentStatus.values()[Byte.toUnsignedInt(buffer.get())];
		creditPayment = Integer.toUnsignedLong(buffer.getInt());
		creditPaymentDate = Integer.toUnsignedLong(buffer.getInt());
		byte[] bytes = new byte[Byte.toUnsignedInt(buffer.get())];
		buffer.get(bytes);
		creditPaymentRef = new String(bytes, StandardCharsets.UTF_8);
		return this;
	}
}
