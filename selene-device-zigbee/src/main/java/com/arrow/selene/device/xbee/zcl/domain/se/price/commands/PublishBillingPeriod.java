package com.arrow.selene.device.xbee.zcl.domain.se.price.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.arrow.selene.device.xbee.zcl.domain.se.price.data.DurationControl;
import com.arrow.selene.device.xbee.zcl.domain.se.price.data.DurationTimebase;
import com.arrow.selene.device.xbee.zcl.domain.se.price.data.TariffType;
import com.digi.xbee.api.utils.ByteUtils;

public class PublishBillingPeriod extends ClusterSpecificCommand<PublishBillingPeriod> {
	private long providerId;
	private long issuerEventId;
	private long billingPeriodStartTime;
	private int billingPeriodDuration;
	private DurationTimebase durationTimebase;
	private DurationControl durationControl;
	private TariffType tariffType;

	public long getProviderId() {
		return providerId;
	}

	public PublishBillingPeriod withProviderId(long providerId) {
		this.providerId = providerId;
		return this;
	}

	public long getIssuerEventId() {
		return issuerEventId;
	}

	public PublishBillingPeriod withIssuerEventId(long issuerEventId) {
		this.issuerEventId = issuerEventId;
		return this;
	}

	public long getBillingPeriodStartTime() {
		return billingPeriodStartTime;
	}

	public PublishBillingPeriod withBillingPeriodStartTime(long billingPeriodStartTime) {
		this.billingPeriodStartTime = billingPeriodStartTime;
		return this;
	}

	public int getBillingPeriodDuration() {
		return billingPeriodDuration;
	}

	public PublishBillingPeriod withBillingPeriodDuration(int billingPeriodDuration) {
		this.billingPeriodDuration = billingPeriodDuration;
		return this;
	}

	public DurationTimebase getDurationTimebase() {
		return durationTimebase;
	}

	public PublishBillingPeriod withDurationTimebase(DurationTimebase durationTimebase) {
		this.durationTimebase = durationTimebase;
		return this;
	}

	public DurationControl getDurationControl() {
		return durationControl;
	}

	public PublishBillingPeriod withDurationControl(DurationControl durationControl) {
		this.durationControl = durationControl;
		return this;
	}

	public TariffType getTariffType() {
		return tariffType;
	}

	public PublishBillingPeriod withTariffType(TariffType tariffType) {
		this.tariffType = tariffType;
		return this;
	}

	@Override
	protected int getId() {
		return PriceClusterCommands.PUBLISH_BILLING_PERIOD_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(17);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt((int) providerId);
		buffer.putInt((int) issuerEventId);
		buffer.putInt((int) billingPeriodStartTime);
		byte[] bytes = ByteUtils.swapByteArray(ByteUtils.intToByteArray(billingPeriodDuration));
		buffer.put(bytes, 0, 3);
		buffer.put((byte) (durationTimebase.ordinal() | durationControl.ordinal() << 4));
		buffer.put((byte) tariffType.ordinal());
		return buffer.array();
	}

	@Override
	public PublishBillingPeriod fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 17, "payload length is incorrect");
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
		return this;
	}
}
