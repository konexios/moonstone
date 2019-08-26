package moonstone.selene.device.harting.rfid.command;

import java.nio.ByteBuffer;

public class GetSystemTimerResponse implements Response<GetSystemTimerResponse> {
	private static final long serialVersionUID = -1980598664034240134L;

	private int status;
	private Timer timer;

	public GetSystemTimerResponse withStatus(int status) {
		this.status = status;
		return this;
	}

	public GetSystemTimerResponse withTimer(Timer timer) {
		this.timer = timer;
		return this;
	}

	public int getStatus() {
		return status;
	}

	public Timer getTimer() {
		return timer;
	}

	public GetSystemTimerResponse parse(int mode, byte... payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload, 2, payload.length - 2);
		int status = Byte.toUnsignedInt(buffer.get());
		byte[] bytes = new byte[4];
		buffer.get(bytes);
		Timer timer = Timer.extract(bytes);
		return new GetSystemTimerResponse().withStatus(status).withTimer(timer);
	}

	static class Timer {
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

		public static Timer extract(byte... value) {
			ByteBuffer buffer = ByteBuffer.wrap(value);
			int hour = Byte.toUnsignedInt(buffer.get(0));
			int minute = Byte.toUnsignedInt(buffer.get(1));
			int millisecond = Short.toUnsignedInt(buffer.getShort(2));
			return new Timer().withHour(hour).withMinute(minute).withMillisecond(millisecond);
		}
	}
}
