package com.arrow.selene.device.harting.rfid.command;

import java.nio.ByteBuffer;

public class GetSystemTimeAndDateResponse implements Response<GetSystemTimeAndDateResponse> {
	private static final long serialVersionUID = -4723341371662628091L;
	private int status;
	private Date date;
	private Time time;

	public GetSystemTimeAndDateResponse withStatus(int status) {
		this.status = status;
		return this;
	}

	public GetSystemTimeAndDateResponse withDate(Date date) {
		this.date = date;
		return this;
	}

	public GetSystemTimeAndDateResponse withTime(Time time) {
		this.time = time;
		return this;
	}

	public int getStatus() {
		return status;
	}

	public Date getDate() {
		return date;
	}

	public Time getTime() {
		return time;
	}

	public GetSystemTimeAndDateResponse parse(int mode, byte... payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload, 2, payload.length - 2);
		int status = Byte.toUnsignedInt(buffer.get());
		byte[] bytes = new byte[5];
		buffer.get(bytes);
		Date date = Date.extract(bytes);
		bytes = new byte[4];
		buffer.get(bytes);
		Time time = Time.extract(bytes);
		return new GetSystemTimeAndDateResponse().withStatus(status).withTime(time).withDate(date);
	}

	static class Date {
		private int century;
		private int year;
		private int month;
		private int day;
		private int timeZone;

		public Date(int century, int year, int month, int day, int timeZone) {
			this.century = century;
			this.year = year;
			this.month = month;
			this.day = day;
			this.timeZone = timeZone;
		}

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

		public static Date extract(byte... value) {
			return new Date(value[0], value[1], value[2], value[3], value[4]);
		}
	}

	static class Time {
		private int hour;
		private int minute;
		private int millisecond;

		public Time(int hour, int minute, int millisecond) {
			this.hour = hour;
			this.minute = minute;
			this.millisecond = millisecond;
		}

		public int getHour() {
			return hour;
		}

		public int getMinute() {
			return minute;
		}

		public int getMillisecond() {
			return millisecond;
		}

		public static Time extract(byte... value) {
			ByteBuffer buffer = ByteBuffer.wrap(value);
			int hour = Byte.toUnsignedInt(buffer.get(0));
			int minute = Byte.toUnsignedInt(buffer.get(1));
			int millisecond = Short.toUnsignedInt(buffer.getShort(2));
			return new Time(hour, minute, millisecond);
		}
	}
}
