package com.arrow.selene.databus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.Loggable;

public abstract class DatabusAbstract extends Loggable implements Databus {
	protected Map<String, List<DatabusListener>> queueToListeners = new ConcurrentHashMap<>();
	private boolean stopped;
	private long maxBuffer = Long.MAX_VALUE;

	@Override
	public synchronized void registerListener(DatabusListener listener, String... queues) {
		Validate.isTrue(!stopped, "databus already stopped");
		Validate.notNull(listener, "listener is null");
		Validate.notEmpty(queues, "queues is empty");

		String method = "registerListener";
		for (String queue : queues) {
			queueToListeners.computeIfAbsent(queue, listeners -> new ArrayList<>()).add(listener);
			checkAndCreateQueue(queue);
			logInfo(method, "added listener: %s for queue: %s", listener.getName(), queue);
		}
	}

	protected void notifyListener(String queue, byte[] message) {
		String method = "notifyListener";
		Validate.notEmpty(queue, "queue is missing");
		Validate.isTrue(message != null && message.length > 0, "empty message");

		if (isStopped()) {
			logWarn(method, "databus not started or already stopped");
		} else {
			List<DatabusListener> list = queueToListeners.get(queue);
			if (list == null) {
				logError(method, "listeners not found for queue: %s", queue);
			} else {
				list.forEach(listener -> listener.receive(queue, message));
			}
		}
	}

	protected void checkAndCreateQueue(String queue) {
	}

	@Override
	public long getMaxBuffer() {
		return maxBuffer;
	}

	@Override
	public void setMaxBuffer(long maxBuffer) {
		this.maxBuffer = maxBuffer;
	}

	protected boolean isStopped() {
		return stopped;
	}

	protected void setStopped(boolean stopped) {
		this.stopped = stopped;
	}
}
