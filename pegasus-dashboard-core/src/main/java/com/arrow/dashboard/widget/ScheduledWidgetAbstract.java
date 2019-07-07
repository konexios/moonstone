package com.arrow.dashboard.widget;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.util.Assert;

import com.arrow.dashboard.widget.annotation.OnDestroy;
import com.arrow.dashboard.widget.annotation.OnError;
import com.arrow.dashboard.widget.annotation.OnReady;
import com.arrow.dashboard.widget.annotation.OnRunning;
import com.arrow.dashboard.widget.annotation.OnStopped;

public abstract class ScheduledWidgetAbstract extends WidgetAbstract implements IScheduledWidget {

	private ScheduledExecutorService scheduledExecutorService;
	private List<ScheduledFuture<?>> workers = new ArrayList<>();

	protected ScheduledWidgetAbstract(int corePoolSize) {
		scheduledExecutorService = Executors.newScheduledThreadPool(corePoolSize);
	}

	@OnReady
	public void onReadyState() {
		super.onReadyState();

		start();
	}

	@OnRunning
	public void onRunningState() {
		super.onRunningState();

		run();
	}

	@OnStopped
	public void onStoppedState() {
		super.onStoppedState();

		stop();
	}

	@OnError
	public void onErrorState() {
		super.onErrorState();

		stop();
	}

	@OnDestroy
	public void onDestroyState() {
		super.onDestroyState();

		destroy();
	}

	@Override
	public abstract void start();

	@Override
	public abstract void run();

	@Override
	public void stop() {
		String method = "stop";
		logInfo(method, "...");
		logDebug(method, "state: %s", getState());

		// cancel and interrupt the the scheduled workers
		for (ScheduledFuture<?> worker : workers)
			worker.cancel(true);

		workers.clear();
	}

	@Override
	public void destroy() {
		String method = "destroy";
		logInfo(method, "...");
		logDebug(method, "state: %s", getState());

		stop();

		// shutdown the executor service
		scheduledExecutorService.shutdownNow();
	}

	protected void addScheduledWorker(Runnable command, long initialDelay, long delay, TimeUnit unit) {
		Assert.notNull(command, "command is null");
		Assert.notNull(unit, "unit is null");

		workers.add(scheduledExecutorService.scheduleWithFixedDelay(command, initialDelay, delay, unit));
	}
}
