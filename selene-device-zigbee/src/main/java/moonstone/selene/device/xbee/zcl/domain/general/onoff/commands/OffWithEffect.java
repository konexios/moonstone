package moonstone.selene.device.xbee.zcl.domain.general.onoff.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class OffWithEffect extends ClusterSpecificCommand<OffWithEffect> {
	private EffectId effectId;
	private EffectVariant effectVariant;

	public OffWithEffect() {
	}

	public OffWithEffect(EffectId effectId, EffectVariant effectVariant) {
		this.effectId = effectId;
		this.effectVariant = effectVariant;
	}

	public EffectId getEffectId() {
		return effectId;
	}

	public EffectVariant getEffectVariant() {
		return effectVariant;
	}

	@Override
	protected int getId() {
		return OnOffClusterCommands.OFF_WITH_EFFECT_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(2);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put((byte) effectId.ordinal());
		buffer.put((byte) effectVariant.getValue());
		return buffer.array();
	}

	@Override
	public OffWithEffect fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 2, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		int id = Byte.toUnsignedInt(buffer.get());
		effectId = EffectId.values()[id];
		effectVariant = EffectVariant.getByValue(effectId, Byte.toUnsignedInt(buffer.get()));
		return this;
	}

	public enum EffectId {
		FADE,
		RAISE_AND_FAIL
	}

	public enum EffectVariant {
		FADE_TO_OFF_IN_0_8_SECONDS(EffectId.FADE, 0x00),
		NO_FADE(EffectId.FADE, 0x01),
		REDUCE_BRIGHTNESS_BY_50_IN_0_8_SECONDS_THEN_FADE_TO_OFF_IN_4_SECONDS(EffectId.FADE, 0x02),
		INCREASE_BRIGHTNESS_BY_20_IN_0_5_SECONDS_THEN_FADE_TO_OFF_IN_1_SECOND(EffectId.RAISE_AND_FAIL, 0x00);

		private final EffectId effectId;
		private final int value;

		EffectVariant(EffectId effectId, int value) {
			this.effectId = effectId;
			this.value = value;
		}

		public EffectId getEffectId() {
			return effectId;
		}

		public int getValue() {
			return value;
		}

		public static EffectVariant getByValue(EffectId id, int value) {
			for (EffectVariant item : values()) {
				if (item.effectId == id && item.value == value) {
					return item;
				}
			}
			return null;
		}
	}
}
