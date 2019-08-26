package moonstone.selene.device.self;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang3.Validate;

import moonstone.selene.Loggable;
import moonstone.selene.data.Gateway;
import moonstone.selene.service.MessageService;

class PurgeMessagesTimerTask extends TimerTask {
	private static final Loggable LOGGER = new Loggable();
	private Gateway gateway;
	private AtomicBoolean running = new AtomicBoolean(false);

	public PurgeMessagesTimerTask(Gateway gateway) {
		this.gateway = gateway;
	}

	@Override
	public void run() {
		String method = "PurgeMessagesTimerTask.run";
		if (running.compareAndSet(false, true)) {
			Validate.notNull(gateway, "gateway is null");
			LOGGER.logInfo(method, "purging old messages ...");
			try {
				MessageService.getInstance().deleteMessageBefore(
						Instant.now().minus(gateway.getPurgeMessagesIntervalDays(), ChronoUnit.DAYS).toEpochMilli());
				LOGGER.logInfo(method, "messages purged successfully");
			} catch (Throwable throwable) {
				LOGGER.logError(method, "failed to purge old messages", throwable);
			}
			running.set(false);
		}
	}
}
