package com.arrow.selene.device.harting.rfid.command;

import java.nio.ByteBuffer;

public class SetSystemTimeAndDate extends AbstractExtendedCommand {
	private static final long serialVersionUID = 6094648720875490031L;
	private static final int LENGTH = 9;
	private static final int CONTROL = 0x87;
	private Date date;
	private Time time;

	public Date getDate() {
		return date;
	}

	public Time getTime() {
		return time;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public SetSystemTimeAndDate withDate(Date date) {
		this.date = date;
		return this;
	}

	public SetSystemTimeAndDate withTime(Time time) {
		this.time = time;
		return this;
	}

	@Override
	protected byte[] buildPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(LENGTH);
		buffer.put(Date.build(date));
		buffer.put(Time.build(time));
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

	public static class Date {
		private int century;
		private int year;
		private int month;
		private int day;
		private int timeZone;

		public int getCentury() {
			return century;
		}

		public int getYear() {
			return year;
		}

		public int getMonth() {
			return month;
		}

		public int getDay() {
			return day;
		}

		public int getTimeZone() {
			return timeZone;
		}

		public void setCentury(int century) {
			this.century = century;
		}

		public void setYear(int year) {
			this.year = year;
		}

		public void setMonth(int month) {
			this.month = month;
		}

		public void setDay(int day) {
			this.day = day;
		}

		public void setTimeZone(int timeZone) {
			this.timeZone = timeZone;
		}

		public Date withCentury(int century) {
			this.century = century;
			return this;
		}

		public Date withYear(int year) {
			this.year = year;
			return this;
		}

		public Date withMonth(int month) {
			this.month = month;
			return this;
		}

		public Date withDay(int day) {
			this.day = day;
			return this;
		}

		public Date withTimeZone(int timeZone) {
			this.timeZone = timeZone;
			return this;
		}

		public static byte[] build(Date value) {
			byte[] payload = new byte[5];
			payload[0] = (byte) value.century;
			payload[1] = (byte) value.year;
			payload[2] = (byte) value.month;
			payload[3] = (byte) value.day;
			payload[4] = (byte) value.timeZone;
			return payload;
		}
	}

	public static class Time {
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

		public Time withHour(int hour) {
			this.hour = hour;
			return this;
		}

		public Time withMinute(int minute) {
			this.minute = minute;
			return this;
		}

		public Time withMillisecond(int millisecond) {
			this.millisecond = millisecond;
			return this;
		}

		public static byte[] build(Time value) {
			ByteBuffer buffer = ByteBuffer.allocate(4);
			buffer.put(0, (byte) value.hour);
			buffer.put(1, (byte) value.minute);
			buffer.putShort(2, (short) value.millisecond);
			return buffer.array();
		}
	}
}
