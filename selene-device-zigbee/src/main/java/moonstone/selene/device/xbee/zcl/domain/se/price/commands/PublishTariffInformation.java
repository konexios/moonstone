package moonstone.selene.device.xbee.zcl.domain.se.price.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.Validate;

import com.digi.xbee.api.utils.ByteUtils;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;
import moonstone.selene.device.xbee.zcl.domain.se.price.data.TariffChargingScheme;
import moonstone.selene.device.xbee.zcl.domain.se.price.data.TariffType;
import moonstone.selene.device.xbee.zcl.domain.se.price.data.TierBlockMode;
import moonstone.selene.device.xbee.zcl.domain.se.price.data.UnitOfMeasure;

public class PublishTariffInformation extends ClusterSpecificCommand<PublishTariffInformation> {
	private long providerId;
	private long issuerEventId;
	private long issuerTariffId;
	private long startTime;
	private TariffType tariffType;
	private TariffChargingScheme tariffChargingScheme;
	private String tariffLabel;
	private int numberOfPriceTiersInUse;
	private int numberOfBlockThresholdsInUse;
	private UnitOfMeasure unitOfMeasure;
	private int currency;
	private byte priceTrailingDigit;
	private long standingCharge;
	private TierBlockMode tierBlockMode;
	private int blockThresholdMultiplier;
	private int blockThresholdDivisor;

	public long getProviderId() {
		return providerId;
	}

	public PublishTariffInformation withProviderId(long providerId) {
		this.providerId = providerId;
		return this;
	}

	public long getIssuerEventId() {
		return issuerEventId;
	}

	public PublishTariffInformation withIssuerEventId(long issuerEventId) {
		this.issuerEventId = issuerEventId;
		return this;
	}

	public long getIssuerTariffId() {
		return issuerTariffId;
	}

	public PublishTariffInformation withIssuerTariffId(long issuerTariffId) {
		this.issuerTariffId = issuerTariffId;
		return this;
	}

	public long getStartTime() {
		return startTime;
	}

	public PublishTariffInformation withStartTime(long startTime) {
		this.startTime = startTime;
		return this;
	}

	public TariffType getTariffType() {
		return tariffType;
	}

	public PublishTariffInformation withTariffType(TariffType tariffType) {
		this.tariffType = tariffType;
		return this;
	}

	public TariffChargingScheme getTariffChargingScheme() {
		return tariffChargingScheme;
	}

	public PublishTariffInformation withTariffChargingScheme(TariffChargingScheme tariffChargingScheme) {
		this.tariffChargingScheme = tariffChargingScheme;
		return this;
	}

	public String getTariffLabel() {
		return tariffLabel;
	}

	public PublishTariffInformation withTariffLabel(String tariffLabel) {
		this.tariffLabel = tariffLabel;
		return this;
	}

	public int getNumberOfPriceTiersInUse() {
		return numberOfPriceTiersInUse;
	}

	public PublishTariffInformation withNumberOfPriceTiersInUse(int numberOfPriceTiersInUse) {
		this.numberOfPriceTiersInUse = numberOfPriceTiersInUse;
		return this;
	}

	public int getNumberOfBlockThresholdsInUse() {
		return numberOfBlockThresholdsInUse;
	}

	public PublishTariffInformation withNumberOfBlockThresholdsInUse(int numberOfBlockThresholdsInUse) {
		this.numberOfBlockThresholdsInUse = numberOfBlockThresholdsInUse;
		return this;
	}

	public UnitOfMeasure getUnitOfMeasure() {
		return unitOfMeasure;
	}

	public PublishTariffInformation withUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
		return this;
	}

	public int getCurrency() {
		return currency;
	}

	public PublishTariffInformation withCurrency(int currency) {
		this.currency = currency;
		return this;
	}

	public byte getPriceTrailingDigit() {
		return priceTrailingDigit;
	}

	public PublishTariffInformation withPriceTrailingDigit(byte priceTrailingDigit) {
		this.priceTrailingDigit = priceTrailingDigit;
		return this;
	}

	public long getStandingCharge() {
		return standingCharge;
	}

	public PublishTariffInformation withStandingCharge(long standingCharge) {
		this.standingCharge = standingCharge;
		return this;
	}

	public TierBlockMode getTierBlockMode() {
		return tierBlockMode;
	}

	public PublishTariffInformation withTierBlockMode(TierBlockMode tierBlockMode) {
		this.tierBlockMode = tierBlockMode;
		return this;
	}

	public int getBlockThresholdMultiplier() {
		return blockThresholdMultiplier;
	}

	public PublishTariffInformation withBlockThresholdMultiplier(int blockThresholdMultiplier) {
		this.blockThresholdMultiplier = blockThresholdMultiplier;
		return this;
	}

	public int getBlockThresholdDivisor() {
		return blockThresholdDivisor;
	}

	public PublishTariffInformation withBlockThresholdDivisor(int blockThresholdDivisor) {
		this.blockThresholdDivisor = blockThresholdDivisor;
		return this;
	}

	@Override
	protected int getId() {
		return PriceClusterCommands.PUBLISH_TARIFF_INFORMATION_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(35 + tariffLabel.length());
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt((int) providerId);
		buffer.putInt((int) issuerEventId);
		buffer.putInt((int) issuerTariffId);
		buffer.putInt((int) startTime);
		buffer.put((byte) (tariffType.ordinal() | tariffChargingScheme.ordinal() << 4));
		buffer.put((byte) tariffLabel.length());
		buffer.put(tariffLabel.getBytes(StandardCharsets.UTF_8));
		buffer.put((byte) numberOfPriceTiersInUse);
		buffer.put((byte) numberOfBlockThresholdsInUse);
		buffer.put((byte) unitOfMeasure.getValue());
		buffer.putShort((short) currency);
		buffer.put((byte) (priceTrailingDigit << 4));
		buffer.putInt((int) standingCharge);
		buffer.put((byte) tierBlockMode.ordinal());
		byte[] bytes = ByteUtils.swapByteArray(ByteUtils.intToByteArray(blockThresholdMultiplier));
		buffer.put(bytes, 0, 3);
		bytes = ByteUtils.swapByteArray(ByteUtils.intToByteArray(blockThresholdDivisor));
		buffer.put(bytes, 0, 3);
		return buffer.array();
	}

	@Override
	public PublishTariffInformation fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length >= 35 && payload.length <= 60, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		providerId = Integer.toUnsignedLong(buffer.getInt());
		issuerEventId = Integer.toUnsignedLong(buffer.getInt());
		issuerTariffId = Integer.toUnsignedLong(buffer.getInt());
		startTime = Integer.toUnsignedLong(buffer.getInt());
		int value = Byte.toUnsignedInt(buffer.get());
		tariffType = TariffType.values()[value & 0x0f];
		tariffChargingScheme = TariffChargingScheme.values()[value >> 4];
		byte[] label = new byte[buffer.get()];
		buffer.get(label);
		tariffLabel = new String(label, StandardCharsets.UTF_8);
		numberOfPriceTiersInUse = Byte.toUnsignedInt(buffer.get());
		numberOfBlockThresholdsInUse = Byte.toUnsignedInt(buffer.get());
		unitOfMeasure = UnitOfMeasure.getByValue(Byte.toUnsignedInt(buffer.get()));
		currency = Short.toUnsignedInt(buffer.getShort());
		priceTrailingDigit = (byte) (Byte.toUnsignedInt(buffer.get()) >> 4);
		standingCharge = Integer.toUnsignedLong(buffer.getInt());
		tierBlockMode = TierBlockMode.values()[Byte.toUnsignedInt(buffer.get())];
		byte[] bytes = new byte[3];
		buffer.get(bytes);
		blockThresholdMultiplier = ByteUtils.byteArrayToInt(ByteUtils.swapByteArray(bytes));
		buffer.get(bytes);
		blockThresholdDivisor = ByteUtils.byteArrayToInt(ByteUtils.swapByteArray(bytes));
		return this;
	}
}
