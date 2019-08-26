package moonstone.selene.device.xbee.zcl.domain.se.price.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;
import moonstone.selene.device.xbee.zcl.domain.se.price.data.CppAuth;
import moonstone.selene.device.xbee.zcl.domain.se.price.data.CppPriceTier;
import moonstone.selene.device.xbee.zcl.domain.se.price.data.TariffType;

public class PublishCppEvent extends ClusterSpecificCommand<PublishCppEvent> {
	private long providerId;
	private long issuerEventId;
	private long startTime;
	private int durationInMinutes;
	private TariffType tariffType;
	private CppPriceTier cppPriceTier;
	private CppAuth cppAuth;

	public long getProviderId() {
		return providerId;
	}

	public PublishCppEvent withProviderId(long providerId) {
		this.providerId = providerId;
		return this;
	}

	public long getIssuerEventId() {
		return issuerEventId;
	}

	public PublishCppEvent withIssuerEventId(long issuerEventId) {
		this.issuerEventId = issuerEventId;
		return this;
	}

	public long getStartTime() {
		return startTime;
	}

	public PublishCppEvent withStartTime(long startTime) {
		this.startTime = startTime;
		return this;
	}

	public int getDurationInMinutes() {
		return durationInMinutes;
	}

	public PublishCppEvent withDurationInMinutes(int durationInMinutes) {
		this.durationInMinutes = durationInMinutes;
		return this;
	}

	public TariffType getTariffType() {
		return tariffType;
	}

	public PublishCppEvent withTariffType(TariffType tariffType) {
		this.tariffType = tariffType;
		return this;
	}

	public CppPriceTier getCppPriceTier() {
		return cppPriceTier;
	}

	public PublishCppEvent withCppPriceTier(CppPriceTier cppPriceTier) {
		this.cppPriceTier = cppPriceTier;
		return this;
	}

	public CppAuth getCppAuth() {
		return cppAuth;
	}

	public PublishCppEvent withCppAuth(CppAuth cppAuth) {
		this.cppAuth = cppAuth;
		return this;
	}

	@Override
	protected int getId() {
		return PriceClusterCommands.PUBLISH_CPP_EVENT_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(17);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt((int) providerId);
		buffer.putInt((int) issuerEventId);
		buffer.putInt((int) startTime);
		buffer.putShort((short) durationInMinutes);
		buffer.put((byte) tariffType.ordinal());
		buffer.put((byte) cppPriceTier.ordinal());
		buffer.put((byte) cppAuth.ordinal());
		return buffer.array();
	}

	@Override
	public PublishCppEvent fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 17, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		providerId = Integer.toUnsignedLong(buffer.getInt());
		issuerEventId = Integer.toUnsignedLong(buffer.getInt());
		startTime = Integer.toUnsignedLong(buffer.getInt());
		durationInMinutes = Short.toUnsignedInt(buffer.getShort());
		tariffType = TariffType.values()[Byte.toUnsignedInt(buffer.get())];
		cppPriceTier = CppPriceTier.values()[Byte.toUnsignedInt(buffer.get())];
		cppAuth = CppAuth.values()[Byte.toUnsignedInt(buffer.get())];
		return this;
	}
}
