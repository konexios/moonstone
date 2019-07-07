package com.arrow.selene.device.xbee.zcl.domain.se.price.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.arrow.selene.device.xbee.zcl.domain.se.price.data.DurationControl;
import com.arrow.selene.device.xbee.zcl.domain.se.price.data.DurationTimebase;
import com.arrow.selene.device.xbee.zcl.domain.se.price.data.TariffType;
import com.digi.xbee.api.utils.ByteUtils;

public class PublishConsolidatedBill extends ClusterSpecificCommand<PublishConsolidatedBill> {
	private long providerId;
	private long issuerEventId;
	private long billingPeriodStartTime;
	private int billingPeriodDuration;
	private DurationTimebase durationTimebase;
	private DurationControl durationControl;
	private TariffType tariffType;
	private long consolidatedBill;
	private int currency;
	private byte billTrailingDigit;

	public long getProviderId() {
		return providerId;
	}

	public PublishConsolidatedBill withProviderId(long providerId) {
		this.providerId = providerId;
		return this;
	}

	public long getIssuerEventId() {
		return issuerEventId;
	}

	public PublishConsolidatedBill withIssuerEventId(long issuerEventId) {
		this.issuerEventId = issuerEventId;
		return this;
	}

	public long getBillingPeriodStartTime() {
		return billingPeriodStartTime;
	}

	public PublishConsolidatedBill withBillingPeriodStartTime(long billingPeriodStartTime) {
		this.billingPeriodStartTime = billingPeriodStartTime;
		return this;
	}

	public int getBillingPeriodDuration() {
		return billingPeriodDuration;
	}

	public PublishConsolidatedBill withBillingPeriodDuration(int billingPeriodDuration) {
		this.billingPeriodDuration = billingPeriodDuration;
		return this;
	}

	public DurationTimebase getDurationTimebase() {
		return durationTimebase;
	}

	public PublishConsolidatedBill withDurationTimebase(DurationTimebase durationTimebase) {
		this.durationTimebase = durationTimebase;
		return this;
	}

	public DurationControl getDurationControl() {
		return durationControl;
	}

	public PublishConsolidatedBill withDurationControl(DurationControl durationControl) {
		this.durationControl = durationControl;
		return this;
	}

	public TariffType getTariffType() {
		return tariffType;
	}

	public PublishConsolidatedBill withTariffType(TariffType tariffType) {
		this.tariffType = tariffType;
		return this;
	}

	public long getConsolidatedBill() {
		return consolidatedBill;
	}

	public PublishConsolidatedBill withConsolidatedBill(long consolidatedBill) {
		this.consolidatedBill = consolidatedBill;
		return this;
	}

	public int getCurrency() {
		return currency;
	}

	public PublishConsolidatedBill withCurrency(int currency) {
		this.currency = currency;
		return this;
	}

	public byte getBillTrailingDigit() {
		return billTrailingDigit;
	}

	public PublishConsolidatedBill withBillTrailingDigit(byte billTrailingDigit) {
		this.billTrailingDigit = billTrailingDigit;
		return this;
	}

	@Override
	protected int getId() {
		return PriceClusterCommands.PUBLISH_CONSOLIDATED_BILL_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(24);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt((int) providerId);
		buffer.putInt((int) issuerEventId);
		buffer.putInt((int) billingPeriodStartTime);
		byte[] bytes = ByteUtils.swapByteArray(ByteUtils.intToByteArray(billingPeriodDuration));
		buffer.put(bytes, 0, 3);
		buffer.put((byte) (durationTimebase.ordinal() | durationControl.ordinal() << 4));
		buffer.put((byte) tariffType.ordinal());
		buffer.putInt((int) consolidatedBill);
		buffer.putShort((short) currency);
		buffer.put((byte) (billTrailingDigit << 4));
		return buffer.array();
	}

	@Override
	public PublishConsolidatedBill fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 24, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		providerId = Integer.toUnsignedLong(buffer.getInt());
		issuerEventId = Integer.toUnsignedLong(buffer.getInt());
		billingPeriodStartTime = Integer.toUnsignedLong(buffer.getInt());
		byte[] bytes = new byte[3];
		buffer.get(bytes);
		billingPeriodDuration = ByteUtils.byteArrayToInt(ByteUtils.swapByteArray(bytes));
		int value = Byte.toUnsignedInt(buffer.get());
		durationTimebase = DurationTimebase.values()[value & 0x0f];
		durationControl = DurationControl.values()[value >> 4];
		tariffType = TariffType.values()[Byte.toUnsignedInt(buffer.get())];
		consolidatedBill = Integer.toUnsignedLong(buffer.getInt());
		currency = Short.toUnsignedInt(buffer.getShort());
		billTrailingDigit = (byte) (buffer.get() >> 4);
		return this;
	}
}
