package com.arrow.selene.device.harting.rfid.config;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.SerializationUtils;

public class Trigger implements ConfigParameter<Trigger> {
	private static final long serialVersionUID = -8358224770065462710L;

	private static final int ID = 10;

	private static final String OPERATING_MODE_TRIGGER_ENABLE = "OperatingMode_Trigger_Enable";
	private static final String OPERATING_MODE_TRIGGER_CONDITION = "OperatingMode_Trigger_Condition";
	private static final String OPERATING_MODE_TRIGGER_ENABLE_UNLIMIT_TRANSPONDER_VALID_TIME =
			"OperatingMode_Trigger_Enable_UnlimitTransponderValidTime";
	private static final String OPERATING_MODE_TRIGGER_RF_OFF_AFTER_READ = "OperatingMode_Trigger_RFOffAfterRead";
	private static final String OPERATING_MODE_TRIGGER_NO_READ_SIGNALIZATION =
			"OperatingMode_Trigger_NoReaderSignalization";
	private static final String OPERATING_MODE_TRIGGER_SOURCE_INPUT_NO1_TRIGGER_USE =
			"OperatingMode_Trigger_Source_Input_No1_TriggerUse";
	private static final String OPERATING_MODE_TRIGGER_SOURCE_INPUT_NO1_HOLD_TIME =
			"OperatingMode_Trigger_Source_Input_No1_HoldTime";
	private static final String OPERATING_MODE_TRIGGER_SOURCE_INPUT_NO2_TRIGGER_USE =
			"OperatingMode_Trigger_Source_Input_No2_TriggerUse";
	private static final String OPERATING_MODE_TRIGGER_SOURCE_INPUT_NO2_HOLD_TIME =
			"OperatingMode_Trigger_Source_Input_No2_HoldTime";

	private TriggerMode triggerMode;
	private TriggerUse triggerUse;
	private int trigger1HoldTime;
	private int trigger2HoldTime;
	private Action action;

	private static final List<String> ALL = new ArrayList<>();

	static {
		ALL.add(OPERATING_MODE_TRIGGER_ENABLE);
		ALL.add(OPERATING_MODE_TRIGGER_CONDITION);
		ALL.add(OPERATING_MODE_TRIGGER_ENABLE_UNLIMIT_TRANSPONDER_VALID_TIME);
		ALL.add(OPERATING_MODE_TRIGGER_RF_OFF_AFTER_READ);
		ALL.add(OPERATING_MODE_TRIGGER_NO_READ_SIGNALIZATION);
		ALL.add(OPERATING_MODE_TRIGGER_SOURCE_INPUT_NO1_TRIGGER_USE);
		ALL.add(OPERATING_MODE_TRIGGER_SOURCE_INPUT_NO1_HOLD_TIME);
		ALL.add(OPERATING_MODE_TRIGGER_SOURCE_INPUT_NO2_TRIGGER_USE);
		ALL.add(OPERATING_MODE_TRIGGER_SOURCE_INPUT_NO2_HOLD_TIME);
	}

	public TriggerMode getTriggerMode() {
		return triggerMode;
	}

	public Trigger withTriggerMode(TriggerMode triggerMode) {
		this.triggerMode = triggerMode;
		return this;
	}

	public TriggerUse getTriggerUse() {
		return triggerUse;
	}

	public Trigger withTriggerUseA(TriggerUse triggerUseA) {
		this.triggerUse = triggerUseA;
		return this;
	}

	public int getTrigger1HoldTime() {
		return trigger1HoldTime;
	}

	public Trigger withTrigger1HoldTime(int trigger1HoldTime) {
		this.trigger1HoldTime = trigger1HoldTime;
		return this;
	}

	public int getTrigger2HoldTime() {
		return trigger2HoldTime;
	}

	public Trigger withTrigger2HoldTime(int trigger2HoldTime) {
		this.trigger2HoldTime = trigger2HoldTime;
		return this;
	}

	public Action getAction() {
		return action;
	}

	public Trigger withAction(Action action) {
		this.action = action;
		return this;
	}

	@Override
	public byte[] build() {
		ByteBuffer buffer = ByteBuffer.allocate(14);
		buffer.put(0, (byte) TriggerMode.build(triggerMode));
		buffer.put(1, (byte) TriggerUse.build(triggerUse));
		buffer.putShort(2, (short) trigger1HoldTime);
		buffer.putShort(4, (short) trigger2HoldTime);
		buffer.put(13, (byte) Action.build(action));
		return buffer.array();
	}

	@Override
	public Trigger parse(byte... payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		TriggerMode triggerMode = TriggerMode.exract(Byte.toUnsignedInt(buffer.get(0)));
		TriggerUse triggerUseA = TriggerUse.extract(Byte.toUnsignedInt(buffer.get(1)));
		int trigger1HoldTime = Short.toUnsignedInt(buffer.getShort(2));
		int trigger2HoldTime = Short.toUnsignedInt(buffer.getShort(4));
		Action action = Action.extract(Byte.toUnsignedInt(buffer.get(13)));
		return new Trigger().withTriggerMode(triggerMode).withTriggerUseA(triggerUseA).withTrigger1HoldTime(
				trigger1HoldTime).withTrigger2HoldTime(trigger2HoldTime).withAction(action);
	}

	@Override
	public int getId() {
		return ID;
	}

	static class TriggerMode {
		private boolean trigger;
		private boolean condition;
		private boolean unlimitedValidTime;

		public TriggerMode(boolean trigger, boolean condition, boolean unlimitedValidTime) {
			this.trigger = trigger;
			this.condition = condition;
			this.unlimitedValidTime = unlimitedValidTime;
		}

		public boolean isTrigger() {
			return trigger;
		}

		public TriggerMode withTrigger(boolean trigger) {
			this.trigger = trigger;
			return this;
		}

		public boolean isCondition() {
			return condition;
		}

		public TriggerMode withCondition(boolean condition) {
			this.condition = condition;
			return this;
		}

		public boolean isUnlimitedValidTime() {
			return unlimitedValidTime;
		}

		public TriggerMode withUnlimitedValidTime(boolean unlimitedValidTime) {
			this.unlimitedValidTime = unlimitedValidTime;
			return this;
		}

		public static int build(TriggerMode value) {
			int result = 0;
			result |= value.trigger ? 0b10000000 : 0;
			result |= value.condition ? 0b00100000 : 0;
			result |= value.unlimitedValidTime ? 0b10000001 : 0;
			return result;
		}

		public static TriggerMode exract(int value) {
			boolean trigger = (value & 0b10000000) == 0b10000000;
			boolean condition = (value & 0b00100000) == 0b00100000;
			boolean unlimitedValidTime = (value & 0b00000001) == 0b00000001;
			return new TriggerMode(trigger, condition, unlimitedValidTime);
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}
			TriggerMode that = (TriggerMode) o;
			return trigger == that.trigger && condition == that.condition &&
					unlimitedValidTime == that.unlimitedValidTime;
		}

		@Override
		public int hashCode() {

			return Objects.hash(trigger, condition, unlimitedValidTime);
		}
	}

	static class TriggerUse {
		private boolean tu1;
		private boolean tu2;

		public TriggerUse(boolean tu1, boolean tu2) {
			this.tu1 = tu1;
			this.tu2 = tu2;
		}

		public boolean getTu1() {
			return tu1;
		}

		public TriggerUse withTu1(boolean tu1) {
			this.tu1 = tu1;
			return this;
		}

		public boolean getTu2() {
			return tu2;
		}

		public TriggerUse withTu2(boolean tu2) {
			this.tu2 = tu2;
			return this;
		}

		public static int build(TriggerUse value) {
			int result = 0;
			result |= value.tu2 ? 4 : 0;
			result |= value.tu1 ? 1 : 0;
			return result;
		}

		public static TriggerUse extract(int value) {
			boolean tu2 = (value & 0x04) == 0x04;
			boolean tu1 = (value & 0x01) == 0x01;
			return new TriggerUse(tu1, tu2);
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}
			TriggerUse that = (TriggerUse) o;
			return tu1 == that.tu1 && tu2 == that.tu2;
		}

		@Override
		public int hashCode() {
			return Objects.hash(tu1, tu2);
		}
	}

	static class Action {
		private boolean enableRFOffAfterRead;
		private NoReadSignalization noReadSignalization;

		public Action(boolean enableRFOffAfterRead, NoReadSignalization noReadSignalization) {
			this.enableRFOffAfterRead = enableRFOffAfterRead;
			this.noReadSignalization = noReadSignalization;
		}

		public boolean isEnableRFOffAfterRead() {
			return enableRFOffAfterRead;
		}

		public Action withEnableRFOffAfterRead(boolean enableRFOffAfterRead) {
			this.enableRFOffAfterRead = enableRFOffAfterRead;
			return this;
		}

		public NoReadSignalization getNoReadSignalization() {
			return noReadSignalization;
		}

		public Action withNoReadSignalization(NoReadSignalization noReadSignalization) {
			this.noReadSignalization = noReadSignalization;
			return this;
		}

		public static int build(Action value) {
			int result = 0;
			result |= value.enableRFOffAfterRead ? 4 : 0;
			result |= value.noReadSignalization.ordinal() << 1;
			return result;
		}

		public static Action extract(int value) {
			boolean enableRFOffAfterRead = (value & 0x01) == 0x01;
			NoReadSignalization noReadSignalization = NoReadSignalization.values()[value >> 1];
			return new Action(enableRFOffAfterRead, noReadSignalization);
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}
			Action action = (Action) o;
			return enableRFOffAfterRead == action.enableRFOffAfterRead &&
					noReadSignalization == action.noReadSignalization;
		}

		@Override
		public int hashCode() {
			return Objects.hash(enableRFOffAfterRead, noReadSignalization);
		}
	}

	enum NoReadSignalization {
		NO_SIGNAL_EMITTER_WILL_BE_ACTIVATED,
		OUT1_WILL_BE_ACTIVATED,
		OUT2_WILL_BE_ACTIVATED,
		REL1_WILL_BE_ACTIVATED,
		REL2_WILL_BE_ACTIVATED
	}

	@Override
	public boolean updateState(String name, String value) {
		Trigger stored = SerializationUtils.clone(this);
		switch (name) {
			case OPERATING_MODE_TRIGGER_CONDITION: {
				triggerMode.withCondition(Boolean.parseBoolean(value));
				break;
			}
			case OPERATING_MODE_TRIGGER_ENABLE: {
				triggerMode.withTrigger(Boolean.parseBoolean(value));
				break;
			}
			case OPERATING_MODE_TRIGGER_ENABLE_UNLIMIT_TRANSPONDER_VALID_TIME: {
				triggerMode.withUnlimitedValidTime(Boolean.parseBoolean(value));
				break;
			}
			case OPERATING_MODE_TRIGGER_NO_READ_SIGNALIZATION: {
				action.withNoReadSignalization(NoReadSignalization.values()[Integer.parseInt(value)]);
				break;
			}
			case OPERATING_MODE_TRIGGER_RF_OFF_AFTER_READ: {
				action.withEnableRFOffAfterRead(Boolean.parseBoolean(value));
				break;
			}
			case OPERATING_MODE_TRIGGER_SOURCE_INPUT_NO1_HOLD_TIME: {
				trigger1HoldTime = Integer.parseInt(value);
				break;
			}
			case OPERATING_MODE_TRIGGER_SOURCE_INPUT_NO1_TRIGGER_USE: {
				triggerUse.withTu1(Boolean.parseBoolean(value));
				break;
			}
			case OPERATING_MODE_TRIGGER_SOURCE_INPUT_NO2_HOLD_TIME: {
				trigger2HoldTime = Integer.parseInt(value);
				break;
			}
			case OPERATING_MODE_TRIGGER_SOURCE_INPUT_NO2_TRIGGER_USE: {
				triggerUse.withTu2(Boolean.parseBoolean(value));
				break;
			}
		}
		return !equals(stored);
	}

	@Override
	public Map<String, String> getStates() {
		Map<String, String> result = new HashMap<>();
		result.put(OPERATING_MODE_TRIGGER_ENABLE, Boolean.toString(triggerMode.trigger));
		result.put(OPERATING_MODE_TRIGGER_CONDITION, Boolean.toString(triggerMode.condition));
		result.put(OPERATING_MODE_TRIGGER_ENABLE_UNLIMIT_TRANSPONDER_VALID_TIME,
				Boolean.toString(triggerMode.unlimitedValidTime));
		result.put(OPERATING_MODE_TRIGGER_RF_OFF_AFTER_READ, Boolean.toString(action.enableRFOffAfterRead));
		result.put(OPERATING_MODE_TRIGGER_NO_READ_SIGNALIZATION, action.noReadSignalization.name());
		result.put(OPERATING_MODE_TRIGGER_SOURCE_INPUT_NO1_TRIGGER_USE, Boolean.toString(triggerUse.tu1));
		result.put(OPERATING_MODE_TRIGGER_SOURCE_INPUT_NO1_HOLD_TIME, Integer.toString(trigger1HoldTime));
		result.put(OPERATING_MODE_TRIGGER_SOURCE_INPUT_NO2_TRIGGER_USE, Boolean.toString(triggerUse.tu2));
		result.put(OPERATING_MODE_TRIGGER_SOURCE_INPUT_NO2_HOLD_TIME, Integer.toString(trigger2HoldTime));
		return result;
	}

	@Override
	public List<String> getParams() {
		return ALL;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Trigger trigger = (Trigger) o;
		return trigger1HoldTime == trigger.trigger1HoldTime && trigger2HoldTime == trigger.trigger2HoldTime &&
				Objects.equals(triggerMode, trigger.triggerMode) && Objects.equals(triggerUse, trigger.triggerUse);
	}

	@Override
	public int hashCode() {
		return Objects.hash(triggerMode, triggerUse, trigger1HoldTime, trigger2HoldTime);
	}
}
