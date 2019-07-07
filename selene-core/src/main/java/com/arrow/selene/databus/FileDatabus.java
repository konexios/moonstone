package com.arrow.selene.databus;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.Validate;

import com.arrow.acn.client.utils.Utils;
import com.arrow.selene.SeleneException;
import com.arrow.selene.service.ConfigService;
import com.squareup.tape.FileObjectQueue;
import com.squareup.tape.SerializedConverter;

public class FileDatabus extends DatabusAbstract {
	private Map<String, FileObjectQueue<byte[]>> queues = new ConcurrentHashMap<>();
	private Thread databusThread;
	private File databusDirectory = new File(
			ConfigService.getInstance().getSeleneProperties().getFileDatabusDirectory());
	private long peekWaitingMs = ConfigService.getInstance().getSeleneProperties().getDatabusPeekInterval();

	public FileDatabus() {
		logInfo(getClass().getSimpleName(), "...");

		if (databusDirectory.exists()) {
			if (databusDirectory.isFile()) {
				throw new SeleneException("path to databus directory points to file");
			}
		} else {
			if (!databusDirectory.mkdirs()) {
				throw new SeleneException("cannot create databus directory");
			}
		}
	}

	@Override
	public void start() {
		String method = "start";
		Validate.isTrue(!isStopped(), "databus is already stopped");

		if (databusThread == null) {
			databusThread = new DatabusThread();
			databusThread.setName("FileDatabus");
			databusThread.start();
			logInfo(method, "started!");
		}
	}

	@Override
	public void stop() {
		String method = "stop";
		setStopped(true);
		if (queues != null) {
			queues.values().forEach(FileObjectQueue::close);
			queues = null;
		}
		Utils.shutdownThread(databusThread);
		logInfo(method, "databus stopped");
		databusThread = null;
	}

	@Override
	public void send(String queue, byte[] message) {
		String method = "send";
		if (queues == null || isStopped()) {
			return;
		}
		if (!queues.containsKey(queue)) {
			checkAndCreateQueue(queue);
		}
		logDebug(method, "publishing to queue: %s, message size: %d", queue, message == null ? 0 : message.length);
		FileObjectQueue<byte[]> fileObjectQueue = queues.get(queue);
		if (fileObjectQueue != null) {
			synchronized (fileObjectQueue) {
				fileObjectQueue.add(message);
			}
		} else {
			logError(method, "unknown queue: %s", queue);
		}
	}

	private class DatabusThread extends Thread {
		@Override
		public void run() {
			String method = "DatabusThread.run";
			if (queues != null) {
				logInfo(method, "peekWaitingMs: %d", peekWaitingMs);
				byte[] message;
				while (!isStopped()) {
					try {
						for (String queue : queueToListeners.keySet()) {
							FileObjectQueue<byte[]> fileObjectQueue = queues.get(queue);
							if (fileObjectQueue == null) {
								checkAndCreateQueue(queue);
								fileObjectQueue = queues.get(queue);
							}
							while ((message = fileObjectQueue.peek()) != null) {
								notifyListener(queue, message);
								fileObjectQueue.remove();
							}
						}
					} catch (Exception e) {
						logError(method, e);
					} finally {
						Utils.sleep(peekWaitingMs);
					}
				}
			} else {
				logError(method, "queues is null");
			}
			logInfo(method, "exiting ...");
		}
	}

	@Override
	protected synchronized void checkAndCreateQueue(String queue) {
		String method = "checkAndCreateQueue";
		if (!queues.containsKey(queue)) {
			try {
				File file = new File(databusDirectory, queue + ".db");
				queues.put(queue, new FileObjectQueue<>(file, new SerializedConverter<>()));
				logInfo(method, "created new queue: %s", queue);
			} catch (IOException e) {
				logError(method, "cannot create queue %s", queue);
				throw new SeleneException("unable to create queue " + queue, e);
			}
		}
	}
}
