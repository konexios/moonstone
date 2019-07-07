package com.arrow.pegasus.service;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.arrow.acs.Loggable;

public abstract class RemoteCacheMonitorAbstract extends Loggable {

	private final static long DEFAULT_INTERVAL_MS = 10000;

	private Timer timer;

	@PostConstruct
	private void init() {
		String method = "init";
		timer = new Timer(true);
		timer.scheduleAtFixedRate(new Monitor(), 0, DEFAULT_INTERVAL_MS);
		logInfo(method, "timer started");
	}

	@PreDestroy
	private void destroy() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	protected abstract Map<String, Long> getRemoteCache();

	protected abstract Map<String, Long> getLocalCache();

	protected abstract void handlePrefix(String prefix, String id);

	private class Monitor extends TimerTask {
		private AtomicBoolean running = new AtomicBoolean(false);

		@Override
		public void run() {
			String method = "monitor";
			if (running.compareAndSet(false, true)) {
				try {
					Map<String, Long> remote = getRemoteCache();
					logInfo(method, "remote size: %d", remote.size());
					Map<String, Long> local = getLocalCache();
					logInfo(method, "local size: %d", local.size());
					for (String key : local.keySet()) {
						Long lts = local.get(key);
						Long rts = remote.get(key);
						if (rts == null || lts < rts) {
							logInfo(method, "removing out-of-date key: %s, lts: %s, rts: %s", key, lts.toString(),
							        rts == null ? "" : rts.toString());
							String[] tokens = key.split("/");
							if (tokens.length == 2) {
								String prefix = tokens[0].trim();
								String id = tokens[1].trim();
								handlePrefix(prefix, id);
							} else {
								logError(method, "Invalid key found: %s", key);
							}
						}
					}
				} catch (Exception e) {
					logError(method, e);
				} finally {
					running.set(false);
				}
			}
		}
	}
}
