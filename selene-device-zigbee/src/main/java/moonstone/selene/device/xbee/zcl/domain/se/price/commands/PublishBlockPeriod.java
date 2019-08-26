package moonstone.selene.device.xbee.zcl.domain.se.price.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import com.digi.xbee.api.utils.ByteUtils;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;
import moonstone.selene.device.xbee.zcl.domain.se.price.data.BlockPeriodControl;
import moonstone.selene.device.xbee.zcl.domain.se.price.data.DurationTimebase;
import moonstone.selene.device.xbee.zcl.domain.se.price.data.TariffResolutionPeriod;
import moonstone.selene.device.xbee.zcl.domain.se.price.data.TariffType;

public class PublishBlockPeriod extends ClusterSpecificCommand<PublishBlockPeriod> {
	private long providerId;
	private long issuerEventId;
	private long blockPeriodStartTime;
	private int blockPeriodDuration;
	private BlockPeriodControl blockPeriodControl;
	private DurationTimebase durationTimebase;
	private TariffType tariffType;
	private TariffResolutionPeriod tariffResolutionPeriod;

	public long getProviderId() {
		return providerId;
	}

	public PublishBlockPeriod withProviderId(long providerId) {
		this.providerId = providerId;
		return this;
	}

	public long getIssuerEventId() {
		return issuerEventId;
	}

	public PublishBlockPeriod withIssuerEventId(long issuerEventId) {
		this.issuerEventId = issuerEventId;
		return this;
	}

	public long getBlockPeriodStartTime() {
		return blockPeriodStartTime;
	}

	public PublishBlockPeriod withBlockPeriodStartTime(long blockPeriodStartTime) {
		this.blockPeriodStartTime = blockPeriodStartTime;
		return this;
	}

	public int getBlockPeriodDuration() {
		return blockPeriodDuration;
	}

	public PublishBlockPeriod withBlockPeriodDuration(int blockPeriodDuration) {
		this.blockPeriodDuration = blockPeriodDuration;
		return this;
	}

	public BlockPeriodControl getBlockPeriodControl() {
		return blockPeriodControl;
	}

	public PublishBlockPeriod withBlockPeriodControl(BlockPeriodControl blockPeriodControl) {
		this.blockPeriodControl = blockPeriodControl;
		return this;
	}

	public DurationTimebase getDurationTimebase() {
		return durationTimebase;
	}

	public PublishBlockPeriod withDurationTimebase(DurationTimebase durationTimebase) {
		this.durationTimebase = durationTimebase;
		return this;
	}

	public TariffType getTariffType() {
		return tariffType;
	}

	public PublishBlockPeriod withTariffType(TariffType tariffType) {
		this.tariffType = tariffType;
		return this;
	}

	public TariffResolutionPeriod getTariffResolutionPeriod() {
		return tariffResolutionPeriod;
	}

	public PublishBlockPeriod withTariffResolutionPeriod(TariffResolutionPeriod tariffResolutionPeriod) {
		this.tariffResolutionPeriod = tariffResolutionPeriod;
		return this;
	}

	@Override
	protected int getId() {
		return PriceClusterCommands.PUBLISH_BLOCK_PERIOD_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(19);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt((int) providerId);
		buffer.putInt((int) issuerEventId);
		buffer.putInt((int) blockPeriodStartTime);
		byte[] bytes = ByteUtils.swapByteArray(ByteUtils.intToByteArray(blockPeriodDuration));
		buffer.put(bytes, 0, 3);
		buffer.put((byte) blockPeriodControl.ordinal());
		buffer.put((byte) durationTimebase.ordinal());
		buffer.put((byte) tariffType.ordinal());
		buffer.put((byte) tariffResolutionPeriod.ordinal());
		return buffer.array();
	}

	@Override
	public PublishBlockPeriod fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 19, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		providerId = Integer.toUnsignedLong(buffer.getInt());
		issuerEventId = Integer.toUnsignedLong(buffer.getInt());
		blockPeriodStartTime = Integer.toUnsignedLong(buffer.getInt());
		byte[] bytes = new byte[3];
		buffer.get(bytes);
		blockPeriodDuration = ByteUtils.byteArrayToInt(ByteUtils.swapByteArray(bytes));
		blockPeriodControl = BlockPeriodControl.values()[Byte.toUnsignedInt(buffer.get())];
		durationTimebase = DurationTimebase.values()[Byte.toUnsignedInt(buffer.get())];
		tariffType = TariffType.values()[Byte.toUnsignedInt(buffer.get())];
		tariffResolutionPeriod = TariffResolutionPeriod.values()[Byte.toUnsignedInt(buffer.get())];
		return this;
	}
}
