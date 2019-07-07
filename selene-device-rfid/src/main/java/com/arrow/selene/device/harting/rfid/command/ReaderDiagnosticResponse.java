package com.arrow.selene.device.harting.rfid.command;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.EnumSet;
import java.util.Set;

import com.arrow.selene.device.harting.rfid.command.ReaderDiagnostic.Mode;

public class ReaderDiagnosticResponse implements Response<ReaderDiagnosticResponse> {
	private static final long serialVersionUID = -1783349791214733760L;
	private int status;
	private Mode1 mode1;
	private Mode4 mode4;
	private Mode5 mode5;
	private String message;

	public int getStatus() {
		return status;
	}

	public Mode1 getMode1() {
		return mode1;
	}

	public Mode4 getMode4() {
		return mode4;
	}

	public Mode5 getMode5() {
		return mode5;
	}

	public String getMessage() {
		return message;
	}

	public ReaderDiagnosticResponse withStatus(int status) {
		this.status = status;
		return this;
	}

	public ReaderDiagnosticResponse withMode1(Mode1 mode1) {
		this.mode1 = mode1;
		return this;
	}

	public ReaderDiagnosticResponse withMode4(Mode4 mode4) {
		this.mode4 = mode4;
		return this;
	}

	public ReaderDiagnosticResponse withMode5(Mode5 mode5) {
		this.mode5 = mode5;
		return this;
	}

	public ReaderDiagnosticResponse withMessage(String message) {
		this.message = message;
		return this;
	}

	@Override
	public ReaderDiagnosticResponse parse(int mode, byte... payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		int status = Byte.toUnsignedInt(buffer.get(2));
		ReaderDiagnosticResponse result = new ReaderDiagnosticResponse().withStatus(status);
		boolean all = mode == GetReaderInfo.Mode.ALL.getValue();
		int shift = all ? 4 : 3;
		byte[] bytes;
		do {
			buffer.position(shift);
			Mode byValue = all ? Mode.getByValue(Byte.toUnsignedInt(buffer.get())) : Mode.getByValue(mode);
			switch (byValue) {
				case LISTING_FOR_STATUS_0X84_RF_COMMUNICATION_ERROR: {
					bytes = new byte[2];
					buffer.get(bytes);
					result.withMode1(Mode1.parse(bytes));
					break;
				}
				case LISTING_FOR_STATUS_0X10_EEPROM_FAILURE:
					bytes = new byte[2];
					buffer.get(bytes);
					result.withMode4(Mode4.parse(bytes));
					break;
				case LISTING_FOR_FLAG_A: {
					bytes = new byte[12];
					buffer.get(bytes);
					result.withMode5(Mode5.parse(bytes));
					break;
				}
				case LISTING_FOR_STATUS_0X18_WRONG_FIRMWARE: {
					bytes = new byte[buffer.remaining() - 2];
					buffer.get(bytes);
					result.withMessage(new String(bytes, StandardCharsets.UTF_8));
					break;
				}
				case ALL: {
					// ignore
					break;
				}
			}
			shift += 31;
		} while (shift < buffer.limit() - 2);
		return result;

		//			ByteBuffer buffer = ByteBuffer.wrap(payload);
		//			int status = Byte.toUnsignedInt(buffer.get(2));
		//			ReaderDiagnosticResponse result = new ReaderDiagnosticResponse().withStatus(status);
		//			buffer.position(3);
		//			if (mode == Mode.LISTING_FOR_STATUS_0X84_RF_COMMUNICATION_ERROR.getValue()) {
		//				byte[] bytes = new byte[2];
		//				buffer.get(bytes);
		//				return result.withMode1(Mode1.parse(bytes));
		//			}
		//			if (mode == Mode.LISTING_FOR_STATUS_0X10_EEPROM_FAILURE.getValue()) {
		//				byte[] bytes = new byte[2];
		//				buffer.get(bytes);
		//				return result.withMode4(Mode4.parse(bytes));
		//			}
		//			if (mode == Mode.LISTING_FOR_FLAG_A.getValue()) {
		//				byte[] bytes = new byte[12];
		//				buffer.get(bytes);
		//				return result.withMode5(Mode5.parse(bytes));
		//			}
		//			if (mode == Mode.LISTING_FOR_STATUS_0X18_WRONG_FIRMWARE.getValue()) {
		//				byte[] bytes = new byte[buffer.remaining() - 2];
		//				buffer.get(bytes);
		//				return result.withMessage(new String(bytes, StandardCharsets.UTF_8));
		//			}
		//			buffer.position(5);
		//			byte[] bytes = new byte[2];
		//			buffer.get(bytes);
		//			Mode1 mode1 = Mode1.parse(bytes);
		//			buffer.position(36);
		//			bytes = new byte[2];
		//			buffer.get(bytes);
		//			Mode4 mode4 = Mode4.parse(bytes);
		//			buffer.position(67);
		//			bytes = new byte[12];
		//			buffer.get(bytes);
		//			Mode5 mode5 = Mode5.parse(bytes);
		//			buffer.position(98);
		//			bytes = new byte[30];
		//			buffer.get(bytes);
		//			return result.withMode1(mode1).withMode4(mode4).withMode5(mode5).withMessage(
		//					new String(bytes, StandardCharsets.UTF_8));

	}

	public static class Mode1 {
		private FlagsA flagsA;
		private FlagsB flagsB;

		public FlagsA getFlagsA() {
			return flagsA;
		}

		public FlagsB getFlagsB() {
			return flagsB;
		}

		public Mode1 withFlagsA(FlagsA flagsA) {
			this.flagsA = flagsA;
			return this;
		}

		public Mode1 withFlagsB(FlagsB flagsB) {
			this.flagsB = flagsB;
			return this;
		}

		public static Mode1 parse(byte... data) {
			FlagsA flagsA = FlagsA.parse(data[0]);
			FlagsB flagsB = FlagsB.parse(data[0]);
			return new Mode1().withFlagsA(flagsA).withFlagsB(flagsB);
		}
	}

	public static class FlagsA {
		private boolean temperatureAlarm;
		private boolean temperatureWarning;
		private boolean rfPowerOutOfRange;
		private boolean noise;

		public boolean isTemperatureAlarm() {
			return temperatureAlarm;
		}

		public boolean isTemperatureWarning() {
			return temperatureWarning;
		}

		public boolean isRfPowerOutOfRange() {
			return rfPowerOutOfRange;
		}

		public boolean isNoise() {
			return noise;
		}

		public FlagsA withTemperatureAlarm(boolean temperatureAlarm) {
			this.temperatureAlarm = temperatureAlarm;
			return this;
		}

		public FlagsA withTemperatureWarning(boolean temperatureWarning) {
			this.temperatureWarning = temperatureWarning;
			return this;
		}

		public FlagsA withRfPowerOutOfRange(boolean rfPowerOutOfRange) {
			this.rfPowerOutOfRange = rfPowerOutOfRange;
			return this;
		}

		public FlagsA withNoise(boolean noise) {
			this.noise = noise;
			return this;
		}

		public static FlagsA parse(int value) {
			boolean temperatureAlarm = (value & 0x80) == 0x80;
			boolean temperatureWarning = (value & 0x20) == 0x20;
			boolean rfPowerOutOfRange = (value & 0x10) == 0x10;
			boolean noise = (value & 0x02) == 0x02;
			return new FlagsA().withTemperatureAlarm(temperatureAlarm).withTemperatureWarning(temperatureWarning)
					.withRfPowerOutOfRange(rfPowerOutOfRange).withNoise(noise);
		}
	}

	public static class FlagsB {
		private boolean checkAntenna4;
		private boolean checkAntenna3;
		private boolean checkAntenna2;
		private boolean checkAntenna1;

		public boolean isCheckAntenna4() {
			return checkAntenna4;
		}

		public boolean isCheckAntenna3() {
			return checkAntenna3;
		}

		public boolean isCheckAntenna2() {
			return checkAntenna2;
		}

		public boolean isCheckAntenna1() {
			return checkAntenna1;
		}

		public FlagsB withCheckAntenna4(boolean checkAntenna4) {
			this.checkAntenna4 = checkAntenna4;
			return this;
		}

		public FlagsB withCheckAntenna3(boolean checkAntenna3) {
			this.checkAntenna3 = checkAntenna3;
			return this;
		}

		public FlagsB withCheckAntenna2(boolean checkAntenna2) {
			this.checkAntenna2 = checkAntenna2;
			return this;
		}

		public FlagsB withCheckAntenna1(boolean checkAntenna1) {
			this.checkAntenna1 = checkAntenna1;
			return this;
		}

		public static FlagsB parse(int value) {
			boolean checkAntenna4 = (value & 0x08) == 0x08;
			boolean checkAntenna3 = (value & 0x04) == 0x04;
			boolean checkAntenna2 = (value & 0x02) == 0x02;
			boolean checkAntenna1 = (value & 0x01) == 0x01;
			return new FlagsB().withCheckAntenna1(checkAntenna1).withCheckAntenna2(checkAntenna2).withCheckAntenna3(
					checkAntenna3).withCheckAntenna4(checkAntenna4);
		}
	}

	public static class Mode4 {
		private boolean usbError;
		private boolean dcOutError;
		private boolean ioExpanderError;
		private boolean adcError;
		private boolean rtcError;
		private boolean rfDecodercError;
		private boolean eepromError;

		public boolean isUsbError() {
			return usbError;
		}

		public boolean isDcOutError() {
			return dcOutError;
		}

		public boolean isIoExpanderError() {
			return ioExpanderError;
		}

		public boolean isAdcError() {
			return adcError;
		}

		public boolean isRtcError() {
			return rtcError;
		}

		public boolean isRfDecodercError() {
			return rfDecodercError;
		}

		public boolean isEepromError() {
			return eepromError;
		}

		public Mode4 withUsbError(boolean usbError) {
			this.usbError = usbError;
			return this;
		}

		public Mode4 withDcOutError(boolean dcOutError) {
			this.dcOutError = dcOutError;
			return this;
		}

		public Mode4 withIoExpanderError(boolean ioExpanderError) {
			this.ioExpanderError = ioExpanderError;
			return this;
		}

		public Mode4 withAdcError(boolean adcError) {
			this.adcError = adcError;
			return this;
		}

		public Mode4 withRtcError(boolean rtcError) {
			this.rtcError = rtcError;
			return this;
		}

		public Mode4 withRfDecodercError(boolean rfDecodercError) {
			this.rfDecodercError = rfDecodercError;
			return this;
		}

		public Mode4 withEepromError(boolean eepromError) {
			this.eepromError = eepromError;
			return this;
		}

		public static Mode4 parse(byte... data) {
			boolean usbError = (data[0] & 0x04) == 0x04;
			boolean dcOutError = (data[0] & 0x02) == 0x02;
			boolean ioExpanderError = (data[0] & 0x01) == 0x01;
			boolean adcError = (data[1] & 0x80) == 0x80;
			boolean rtcError = (data[1] & 0x40) == 0x40;
			boolean rfDecodercError = (data[1] & 0x08) == 0x08;
			boolean eepromError = (data[1] & 0x01) == 0x01;
			return new Mode4().withUsbError(usbError).withDcOutError(dcOutError).withIoExpanderError(ioExpanderError)
					.withAdcError(adcError).withRtcError(rtcError).withRfDecodercError(rfDecodercError)
					.withEepromError(
							eepromError);
		}
	}

	public static class Mode5 {
		private Set<ControlUmux> controlUmux1;
		private Set<ControlUmux> controlUmux2;
		private Set<ControlUmux> controlUmux3;
		private Set<ControlUmux> controlUmux4;

		public Set<ControlUmux> getControlUmux1() {
			return controlUmux1;
		}

		public Set<ControlUmux> getControlUmux2() {
			return controlUmux2;
		}

		public Set<ControlUmux> getControlUmux3() {
			return controlUmux3;
		}

		public Set<ControlUmux> getControlUmux4() {
			return controlUmux4;
		}

		public Mode5 withControlUmux1(Set<ControlUmux> controlUmux1) {
			this.controlUmux1 = controlUmux1;
			return this;
		}

		public Mode5 withControlUmux2(Set<ControlUmux> controlUmux2) {
			this.controlUmux2 = controlUmux2;
			return this;
		}

		public Mode5 withControlUmux3(Set<ControlUmux> controlUmux3) {
			this.controlUmux3 = controlUmux3;
			return this;
		}

		public Mode5 withControlUmux4(Set<ControlUmux> controlUmux4) {
			this.controlUmux4 = controlUmux4;
			return this;
		}

		public static Mode5 parse(byte... data) {
			Set<ControlUmux> controlUmux1 = ControlUmux.extract(data[0]);
			Set<ControlUmux> controlUmux2 = ControlUmux.extract(data[3]);
			Set<ControlUmux> controlUmux3 = ControlUmux.extract(data[6]);
			Set<ControlUmux> controlUmux4 = ControlUmux.extract(data[9]);
			return new Mode5().withControlUmux1(controlUmux1).withControlUmux2(controlUmux2).withControlUmux3(
					controlUmux3).withControlUmux4(controlUmux4);
		}
	}

	public enum ControlUmux {
		CHANNEL1,
		CHANNEL2,
		CHANNEL3,
		CHANNEL4,
		CHANNEL5,
		CHANNEL6,
		CHANNEL7,
		CHANNEL8;

		public static Set<ControlUmux> extract(int value) {
			Set<ControlUmux> result = EnumSet.noneOf(ControlUmux.class);
			for (ControlUmux item : values()) {
				if ((value >> item.ordinal() & 0x01) == 1) {
					result.add(item);
				}
			}
			return result;
		}

		public static int build(Iterable<ControlUmux> value) {
			int result = 0;
			for (ControlUmux item : value) {
				result |= 1 << item.ordinal();
			}
			return result;
		}
	}
}
