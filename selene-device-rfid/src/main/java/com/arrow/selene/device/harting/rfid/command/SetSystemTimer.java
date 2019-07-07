package com.arrow.selene.device.harting.rfid.command;

import java.nio.ByteBuffer;

public class SetSystemTimer extends AbstractExtendedCommand {
	private static final long serialVersionUID = 5475649492241629292L;
	private static final int LENGTH = 4;
	private static final int CONTROL = 0x85;
	private Timer timer;

	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	public SetSystemTimer withTimer(Timer timer) {
		this.timer = timer;
		return this;
	}

	@Override
	protected byte[] buildPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(LENGTH);
		buffer.put(Timer.build(timer));
		return buffer.array();
	}

	@Override
	protected int getControl() {
		return CONTROL;
	}

	@Override
	protected int getLength() {
		return LENGTH;
	}

	public static class Timer {
		private int hour;
		private int minute;
		private int millisecond;

		public int getHour() {
			return hour;
		}

		public int getMinute() {
			return minute;
		}

		public int getMillisecond() {
			return millisecond;
		}

		public void setHour(int hour) {
			this.hour = hour;
		}

		public void setMinute(int minute) {
			this.minute = minute;
		}

		public void setMillisecond(int millisecond) {
			this.millisecond = millisecond;
		}

		public Timer withHour(int hour) {
			this.hour = hour;
			return this;
		}

		public Timer withMinute(int minute) {
			this.minute = minute;
			return this;
		}

		public Timer withMillisecond(int millisecond) {
			this.millisecond = millisecond;
			return this;
		}

		public static byte[] build(Timer value) {
			ByteBuffer buffer = ByteBuffer.allocate(4);
			buffer.put(0, (byte) value.hour);
			buffer.put(1, (byte) value.minute);
			buffer.putShort(2, (short) value.millisecond);
			return buffer.array();
		}
	}
}
