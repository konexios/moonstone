package moonstone.selene.device.xbee.zcl.domain.se.price.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;
import moonstone.selene.device.xbee.zcl.domain.se.price.data.Co2Unit;
import moonstone.selene.device.xbee.zcl.domain.se.price.data.TariffType;

public class PublishCo2Value extends ClusterSpecificCommand<PublishCo2Value> {
	private long providerId;
	private long issuerEventId;
	private long startTime;
	private TariffType tariffType;
	private long co2Value;
	private Co2Unit co2Unit;
	private byte co2ValueTrailingDigit;

	public long getProviderId() {
		return providerId;
	}

	public PublishCo2Value withProviderId(long providerId) {
		this.providerId = providerId;
		return this;
	}

	public long getIssuerEventId() {
		return issuerEventId;
	}

	public PublishCo2Value withIssuerEventId(long issuerEventId) {
		this.issuerEventId = issuerEventId;
		return this;
	}

	public long getStartTime() {
		return startTime;
	}

	public PublishCo2Value withStartTime(long startTime) {
		this.startTime = startTime;
		return this;
	}

	public TariffType getTariffType() {
		return tariffType;
	}

	public PublishCo2Value withTariffType(TariffType tariffType) {
		this.tariffType = tariffType;
		return this;
	}

	public long getCo2Value() {
		return co2Value;
	}

	public PublishCo2Value withCo2Value(long co2Value) {
		this.co2Value = co2Value;
		return this;
	}

	public Co2Unit getCo2Unit() {
		return co2Unit;
	}

	public PublishCo2Value withCo2Unit(Co2Unit co2Unit) {
		this.co2Unit = co2Unit;
		return this;
	}

	public byte getCo2ValueTrailingDigit() {
		return co2ValueTrailingDigit;
	}

	public PublishCo2Value withCo2ValueTrailingDigit(byte co2ValueTrailingDigit) {
		this.co2ValueTrailingDigit = co2ValueTrailingDigit;
		return this;
	}

	@Override
	protected int getId() {
		return PriceClusterCommands.PUBLISH_CO2_VALUE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(19);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt((int) providerId);
		buffer.putInt((int) issuerEventId);
		buffer.putInt((int) startTime);
		buffer.put((byte) tariffType.ordinal());
		buffer.putInt((int) co2Value);
		buffer.put((byte) co2Unit.ordinal());
		buffer.put((byte) (co2ValueTrailingDigit << 4));
		return buffer.array();
	}

	@Override
	public PublishCo2Value fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 19, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		providerId = Integer.toUnsignedLong(buffer.getInt());
		issuerEventId = Integer.toUnsignedLong(buffer.getInt());
		startTime = Integer.toUnsignedLong(buffer.getInt());
		tariffType = TariffType.values()[Byte.toUnsignedInt(buffer.get())];
		co2Value = Integer.toUnsignedLong(buffer.getInt());
		co2Unit = Co2Unit.values()[Byte.toUnsignedInt(buffer.get())];
		co2ValueTrailingDigit = (byte) (buffer.get() >> 4);
		return this;
	}
}
