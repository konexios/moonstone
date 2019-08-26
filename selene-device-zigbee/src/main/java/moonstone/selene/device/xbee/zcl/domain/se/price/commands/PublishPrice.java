package moonstone.selene.device.xbee.zcl.domain.se.price.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import org.apache.commons.lang3.Validate;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;
import moonstone.selene.device.xbee.zcl.domain.se.price.data.AlternateCostUnit;
import moonstone.selene.device.xbee.zcl.domain.se.price.data.ExtendedPriceTier;
import moonstone.selene.device.xbee.zcl.domain.se.price.data.ExtendedRegisterTier;
import moonstone.selene.device.xbee.zcl.domain.se.price.data.GenerationTier;
import moonstone.selene.device.xbee.zcl.domain.se.price.data.PriceControl;
import moonstone.selene.device.xbee.zcl.domain.se.price.data.PriceTier;
import moonstone.selene.device.xbee.zcl.domain.se.price.data.RegisterTier;
import moonstone.selene.device.xbee.zcl.domain.se.price.data.UnitOfMeasure;

public class PublishPrice extends ClusterSpecificCommand<PublishPrice> {
	private long providerId;
	private String rateLabel;
	private long issuerEventId;
	private long currentTime;
	private UnitOfMeasure unitOfMeasure;
	private int currency;
	private byte priceTrailingDigit;
	private PriceTier priceTier;
	private byte numberOfPriceTiers;
	private RegisterTier registerTier;
	private long startTime;
	private int durationInMinutes;
	private long price;
	private int priceRatio;
	private long generationPrice;
	private int generationPriceRatio;
	private long alternateCostDelivered;
	private AlternateCostUnit alternateCostUnit;
	private byte alternateCostTrailingDigit;
	private int numberOfBlockThresholds;
	private Set<PriceControl> priceControls;
	private int numberOfGenerationTiers;
	private GenerationTier generationTier;
	private int extendedNumberOfPriceTiers;
	private ExtendedPriceTier extendedPriceTier;
	private ExtendedRegisterTier extendedRegisterTier;

	public long getProviderId() {
		return providerId;
	}

	public PublishPrice withProviderId(long providerId) {
		this.providerId = providerId;
		return this;
	}

	public String getRateLabel() {
		return rateLabel;
	}

	public PublishPrice withRateLabel(String rateLabel) {
		this.rateLabel = rateLabel;
		return this;
	}

	public long getIssuerEventId() {
		return issuerEventId;
	}

	public PublishPrice withIssuerEventId(long issuerEventId) {
		this.issuerEventId = issuerEventId;
		return this;
	}

	public long getCurrentTime() {
		return currentTime;
	}

	public PublishPrice withCurrentTime(long currentTime) {
		this.currentTime = currentTime;
		return this;
	}

	public UnitOfMeasure getUnitOfMeasure() {
		return unitOfMeasure;
	}

	public PublishPrice withUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
		return this;
	}

	public int getCurrency() {
		return currency;
	}

	public PublishPrice withCurrency(int currency) {
		this.currency = currency;
		return this;
	}

	public byte getPriceTrailingDigit() {
		return priceTrailingDigit;
	}

	public PublishPrice withPriceTrailingDigit(byte priceTrailingDigit) {
		this.priceTrailingDigit = priceTrailingDigit;
		return this;
	}

	public PriceTier getPriceTier() {
		return priceTier;
	}

	public PublishPrice withPriceTier(PriceTier priceTier) {
		this.priceTier = priceTier;
		return this;
	}

	public byte getNumberOfPriceTiers() {
		return numberOfPriceTiers;
	}

	public PublishPrice withNumberOfPriceTiers(byte numberOfPriceTiers) {
		this.numberOfPriceTiers = numberOfPriceTiers;
		return this;
	}

	public RegisterTier getRegisterTier() {
		return registerTier;
	}

	public PublishPrice withRegisterTier(RegisterTier registerTier) {
		this.registerTier = registerTier;
		return this;
	}

	public long getStartTime() {
		return startTime;
	}

	public PublishPrice withStartTime(long startTime) {
		this.startTime = startTime;
		return this;
	}

	public int getDurationInMinutes() {
		return durationInMinutes;
	}

	public PublishPrice withDurationInMinutes(int durationInMinutes) {
		this.durationInMinutes = durationInMinutes;
		return this;
	}

	public long getPrice() {
		return price;
	}

	public PublishPrice withPrice(long price) {
		this.price = price;
		return this;
	}

	public int getPriceRatio() {
		return priceRatio;
	}

	public PublishPrice withPriceRatio(int priceRatio) {
		this.priceRatio = priceRatio;
		return this;
	}

	public long getGenerationPrice() {
		return generationPrice;
	}

	public PublishPrice withGenerationPrice(long generationPrice) {
		this.generationPrice = generationPrice;
		return this;
	}

	public int getGenerationPriceRatio() {
		return generationPriceRatio;
	}

	public PublishPrice withGenerationPriceRatio(int generationPriceRatio) {
		this.generationPriceRatio = generationPriceRatio;
		return this;
	}

	public long getAlternateCostDelivered() {
		return alternateCostDelivered;
	}

	public PublishPrice withAlternateCostDelivered(long alternateCostDelivered) {
		this.alternateCostDelivered = alternateCostDelivered;
		return this;
	}

	public AlternateCostUnit getAlternateCostUnit() {
		return alternateCostUnit;
	}

	public PublishPrice withAlternateCostUnit(AlternateCostUnit alternateCostUnit) {
		this.alternateCostUnit = alternateCostUnit;
		return this;
	}

	public byte getAlternateCostTrailingDigit() {
		return alternateCostTrailingDigit;
	}

	public PublishPrice withAlternateCostTrailingDigit(byte alternateCostTrailingDigit) {
		this.alternateCostTrailingDigit = alternateCostTrailingDigit;
		return this;
	}

	public int getNumberOfBlockThresholds() {
		return numberOfBlockThresholds;
	}

	public PublishPrice withNumberOfBlockThresholds(int numberOfBlockThresholds) {
		this.numberOfBlockThresholds = numberOfBlockThresholds;
		return this;
	}

	public Set<PriceControl> getPriceControls() {
		return priceControls;
	}

	public PublishPrice withPriceControls(Set<PriceControl> priceControls) {
		this.priceControls = priceControls;
		return this;
	}

	public int getNumberOfGenerationTiers() {
		return numberOfGenerationTiers;
	}

	public PublishPrice withNumberOfGenerationTiers(int numberOfGenerationTiers) {
		this.numberOfGenerationTiers = numberOfGenerationTiers;
		return this;
	}

	public GenerationTier getGenerationTier() {
		return generationTier;
	}

	public PublishPrice withGenerationTier(GenerationTier generationTier) {
		this.generationTier = generationTier;
		return this;
	}

	public int getExtendedNumberOfPriceTiers() {
		return extendedNumberOfPriceTiers;
	}

	public PublishPrice withExtendedNumberOfPriceTiers(int extendedNumberOfPriceTiers) {
		this.extendedNumberOfPriceTiers = extendedNumberOfPriceTiers;
		return this;
	}

	public ExtendedPriceTier getExtendedPriceTier() {
		return extendedPriceTier;
	}

	public PublishPrice withExtendedPriceTier(ExtendedPriceTier extendedPriceTier) {
		this.extendedPriceTier = extendedPriceTier;
		return this;
	}

	public ExtendedRegisterTier getExtendedRegisterTier() {
		return extendedRegisterTier;
	}

	public PublishPrice withExtendedRegisterTier(ExtendedRegisterTier extendedRegisterTier) {
		this.extendedRegisterTier = extendedRegisterTier;
		return this;
	}

	@Override
	protected int getId() {
		return PriceClusterCommands.PUBLISH_PRICE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(47 + rateLabel.length());
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt((int) providerId);
		buffer.put((byte) rateLabel.length());
		buffer.put(rateLabel.getBytes(StandardCharsets.UTF_8));
		buffer.putInt((int) issuerEventId);
		buffer.putInt((int) currentTime);
		buffer.put((byte) unitOfMeasure.getValue());
		buffer.putShort((short) currency);
		buffer.put((byte) (priceTrailingDigit << 4 | priceTier.ordinal()));
		buffer.put((byte) (numberOfPriceTiers << 4 | registerTier.ordinal()));
		buffer.putInt((int) startTime);
		buffer.putShort((short) durationInMinutes);
		buffer.putInt((int) price);
		buffer.put((byte) priceRatio);
		buffer.putInt((int) generationPrice);
		buffer.put((byte) generationPriceRatio);
		buffer.putInt((int) alternateCostDelivered);
		buffer.put((byte) alternateCostUnit.ordinal());
		buffer.put((byte) (alternateCostTrailingDigit << 4));
		buffer.put((byte) numberOfBlockThresholds);
		buffer.put((byte) PriceControl.getValue(priceControls));
		buffer.put((byte) numberOfGenerationTiers);
		buffer.put((byte) generationTier.ordinal());
		buffer.put((byte) extendedNumberOfPriceTiers);
		buffer.put((byte) extendedPriceTier.ordinal());
		buffer.put((byte) extendedRegisterTier.ordinal());
		return buffer.array();
	}

	@Override
	public PublishPrice fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length >= 47 && payload.length <= 59, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		providerId = Integer.toUnsignedLong(buffer.getInt());
		byte[] label = new byte[buffer.get()];
		buffer.get(label);
		rateLabel = new String(label, StandardCharsets.UTF_8);
		issuerEventId = Integer.toUnsignedLong(buffer.getInt());
		currentTime = Integer.toUnsignedLong(buffer.getInt());
		unitOfMeasure = UnitOfMeasure.getByValue(Byte.toUnsignedInt(buffer.get()));
		currency = Short.toUnsignedInt(buffer.getShort());
		int value = Byte.toUnsignedInt(buffer.get());
		priceTrailingDigit = (byte) (value >> 4);
		priceTier = PriceTier.values()[value & 0x0f];
		numberOfPriceTiers = (byte) (value >> 4);
		registerTier = RegisterTier.values()[value & 0x0f];
		startTime = Integer.toUnsignedLong(buffer.getInt());
		durationInMinutes = Short.toUnsignedInt(buffer.getShort());
		price = Integer.toUnsignedLong(buffer.getInt());
		priceRatio = Byte.toUnsignedInt(buffer.get());
		generationPrice = Integer.toUnsignedLong(buffer.getInt());
		generationPriceRatio = Byte.toUnsignedInt(buffer.get());
		alternateCostDelivered = Integer.toUnsignedLong(buffer.getInt());
		alternateCostUnit = AlternateCostUnit.values()[Byte.toUnsignedInt(buffer.get())];
		numberOfBlockThresholds = Byte.toUnsignedInt(buffer.get());
		priceControls = PriceControl.getByValue(buffer.get());
		numberOfGenerationTiers = Byte.toUnsignedInt(buffer.get());
		generationTier = GenerationTier.values()[Byte.toUnsignedInt(buffer.get())];
		extendedNumberOfPriceTiers = Byte.toUnsignedInt(buffer.get());
		extendedPriceTier = ExtendedPriceTier.values()[Byte.toUnsignedInt(buffer.get())];
		extendedRegisterTier = ExtendedRegisterTier.values()[Byte.toUnsignedInt(buffer.get())];
		return this;
	}
}
