package moonstone.selene.device.xbee.zcl.domain.general.powerprofile.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class GetPowerProfilePriceResponse extends ClusterSpecificCommand<GetPowerProfilePriceResponse> {
	private int powerProfileId;
	private int currency;
	private long price;
	private int priceTrailingDigit;

	public int getPowerProfileId() {
		return powerProfileId;
	}

	public GetPowerProfilePriceResponse withPowerProfileId(int powerProfileId) {
		this.powerProfileId = powerProfileId;
		return this;
	}

	public int getCurrency() {
		return currency;
	}

	public GetPowerProfilePriceResponse withCurrency(int currency) {
		this.currency = currency;
		return this;
	}

	public long getPrice() {
		return price;
	}

	public GetPowerProfilePriceResponse withPrice(long price) {
		this.price = price;
		return this;
	}

	public int getPriceTrailingDigit() {
		return priceTrailingDigit;
	}

	public GetPowerProfilePriceResponse withPriceTrailingDigit(int priceTrailingDigit) {
		this.priceTrailingDigit = priceTrailingDigit;
		return this;
	}

	@Override
	protected int getId() {
		return PowerProfileClusterCommands.GET_POWER_PROFILE_PRICE_RESPONSE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(8);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put((byte) powerProfileId);
		buffer.putShort((short) currency);
		buffer.putInt((int) price);
		buffer.put((byte) priceTrailingDigit);
		return buffer.array();
	}

	@Override
	public GetPowerProfilePriceResponse fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 8, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		powerProfileId = Byte.toUnsignedInt(buffer.get());
		currency = Short.toUnsignedInt(buffer.getShort());
		price =Integer.toUnsignedLong(buffer.getInt());
		priceTrailingDigit = Byte.toUnsignedInt(buffer.get());
		return this;
	}
}
