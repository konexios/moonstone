package com.arrow.selene.engine;

import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.arrow.acn.client.ClientConstants;
import com.arrow.selene.Loggable;
import com.arrow.selene.SeleneException;
import com.arrow.selene.service.CryptoService;

public abstract class ModuleAbstract extends Loggable implements Module {

	private String name;
	private ModuleState state;
	private ExecutorService service;
	private boolean shuttingDown;
	private CryptoService cryptoService;
	private String errorStateMessage;

	protected ModuleAbstract() {
		name = getClass().getSimpleName();
		cryptoService = CryptoService.getInstance();
		state = ModuleState.CREATED;
	}

	@Override
	public void start() {
		String method = "ProcessorAbstract.start";
		logInfo(method, "starting ...");
		shuttingDown = false;
		service = Executors.newCachedThreadPool();
		logInfo(method, "started");
	}

	@Override
	public void stop() {
		String method = "ProcessorAbstract.stop";
		logInfo(method, "stopping ...");
		shuttingDown = true;
		if (service != null) {
			logInfo(method, "stopping service ...");
			service.shutdown();
			try {
				service.awaitTermination(ClientConstants.DEFAULT_SHUTDOWN_WAITING_MS, TimeUnit.MILLISECONDS);
			} catch (Throwable t) {
				service.shutdownNow();
			}
			service = null;
		}
		logInfo(method, "shutdown complete");
	}

	public boolean waitForModuleReady(long timeoutInSeconds) {
		String method = "waitForModuleReady";
		if (state == ModuleState.CREATED) {
			return true;
		} else if (state != ModuleState.STARTING) {
			throw new SeleneException("module is in the wrong state: %s" + state.toString());
		} else {
			long start = Instant.now().getEpochSecond();
			while (state == ModuleState.STARTING && (Instant.now().getEpochSecond() - start) < timeoutInSeconds) {
				try {
					logInfo(method, "waiting for module ready ...");
					Thread.sleep(1000);
				} catch (Exception e) {
				}
			}
			return state == ModuleState.STARTED;
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setErrorState(String errorStateMessage) {
		setState(ModuleState.ERROR);
		this.errorStateMessage = errorStateMessage;
	}

	@Override
	public String getErrorStateMessage() {
		return errorStateMessage;
	}

	protected void setName(String name) {
		this.name = name;
	}

	@Override
	public ModuleState getState() {
		return state;
	}

	@Override
	public void setState(ModuleState state) {
		this.state = state;
	}

	protected boolean isShuttingDown() {
		return shuttingDown;
	}

	protected ExecutorService getService() {
		return service;
	}

	protected CryptoService getCryptoService() {
		return cryptoService;
	}
}
