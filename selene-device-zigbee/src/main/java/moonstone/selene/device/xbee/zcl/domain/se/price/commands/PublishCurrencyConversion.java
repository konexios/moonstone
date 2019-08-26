package moonstone.selene.device.xbee.zcl.domain.se.price.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Set;

import org.apache.commons.lang3.Validate;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;
import moonstone.selene.device.xbee.zcl.domain.se.price.data.CurrencyChangeControl;

public class PublishCurrencyConversion extends ClusterSpecificCommand<PublishCurrencyConversion> {
	private long providerId;
	private long issuerEventId;
	private long startTime;
	private int oldCurrency;
	private int newCurrency;
	private long conversionFactor;
	private byte conversionFactorTrailingDigit;
	private Set<CurrencyChangeControl> currencyChangeControls;

	public long getProviderId() {
		return providerId;
	}

	public PublishCurrencyConversion withProviderId(long providerId) {
		this.providerId = providerId;
		return this;
	}

	public long getIssuerEventId() {
		return issuerEventId;
	}

	public PublishCurrencyConversion withIssuerEventId(long issuerEventId) {
		this.issuerEventId = issuerEventId;
		return this;
	}

	public long getStartTime() {
		return startTime;
	}

	public PublishCurrencyConversion withStartTime(long startTime) {
		this.startTime = startTime;
		return this;
	}

	public int getOldCurrency() {
		return oldCurrency;
	}

	public PublishCurrencyConversion withOldCurrency(int oldCurrency) {
		this.oldCurrency = oldCurrency;
		return this;
	}

	public int getNewCurrency() {
		return newCurrency;
	}

	public PublishCurrencyConversion withNewCurrency(int newCurrency) {
		this.newCurrency = newCurrency;
		return this;
	}

	public long getConversionFactor() {
		return conversionFactor;
	}

	public PublishCurrencyConversion withConversionFactor(long conversionFactor) {
		this.conversionFactor = conversionFactor;
		return this;
	}

	public byte getConversionFactorTrailingDigit() {
		return conversionFactorTrailingDigit;
	}

	public PublishCurrencyConversion withConversionFactorTrailingDigit(byte conversionFactorTrailingDigit) {
		this.conversionFactorTrailingDigit = conversionFactorTrailingDigit;
		return this;
	}

	public Set<CurrencyChangeControl> getCurrencyChangeControls() {
		return currencyChangeControls;
	}

	public PublishCurrencyConversion withCurrencyChangeControls(Set<CurrencyChangeControl> currencyChangeControls) {
		this.currencyChangeControls = currencyChangeControls;
		return this;
	}

	@Override
	protected int getId() {
		return PriceClusterCommands.PUBLISH_CURRENCY_CONVERSION_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(25);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt((int) providerId);
		buffer.putInt((int) issuerEventId);
		buffer.putInt((int) startTime);
		buffer.putShort((short) oldCurrency);
		buffer.putShort((short) newCurrency);
		buffer.putInt((int) conversionFactor);
		buffer.put((byte) (conversionFactorTrailingDigit << 4));
		buffer.putInt(CurrencyChangeControl.getValue(currencyChangeControls));
		return buffer.array();
	}

	@Override
	public PublishCurrencyConversion fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 25, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		providerId = Integer.toUnsignedLong(buffer.getInt());
		issuerEventId = Integer.toUnsignedLong(buffer.getInt());
		startTime = Integer.toUnsignedLong(buffer.getInt());
		oldCurrency = Short.toUnsignedInt(buffer.getShort());
		newCurrency = Short.toUnsignedInt(buffer.getShort());
		conversionFactor = Integer.toUnsignedLong(buffer.getInt());
		conversionFactorTrailingDigit = (byte) (buffer.get() >> 4);
		currencyChangeControls = CurrencyChangeControl.getByValue((byte) buffer.getInt());
		return this;
	}
}
