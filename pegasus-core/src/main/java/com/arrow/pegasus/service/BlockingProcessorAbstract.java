package com.arrow.pegasus.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.springframework.util.Assert;

import com.arrow.acs.AcsRuntimeException;
import com.arrow.acs.AcsSystemException;
import com.arrow.pegasus.LifeCycleAbstract;

public abstract class BlockingProcessorAbstract extends LifeCycleAbstract implements Processor {

	private String name;
	private List<WorkerThread> workerThreads;
	private BlockingQueue<WorkerThread> queue;

	@Override
	public void start() {
		String method = "BlockingProcessorAbstract.start";
		logInfo(method, "starting ...");
		int numThreads = getNumWorkerThreads();
		if (numThreads <= 0 || numThreads > 200) {
			throw new AcsSystemException("invalid numWorkerThreads: " + numThreads);
		}
		workerThreads = new ArrayList<>(numThreads);
		queue = new ArrayBlockingQueue<>(numThreads);
		for (int i = 0; i < numThreads; i++) {
			WorkerThread worker = new WorkerThread("WorkerThread-" + i);
			worker.start();
			workerThreads.add(worker);
		}
		logInfo(method, "started");
	}

	@Override
	protected void preDestroy() {
		String method = "BlockingProcessorAbstract.stop";
		super.preDestroy();
		logInfo(method, "stopping ...");
		if (workerThreads != null) {
			for (WorkerThread worker : workerThreads) {
				if (worker.isAlive()) {
					logInfo(method, "stopping %s", worker.getName());
					worker.terminate();
					try {
						worker.join();
					} catch (InterruptedException e) {
					}
				}
			}
			workerThreads.clear();
			workerThreads = null;
			queue.clear();
			queue = null;
		}
		logInfo(method, "stopping complete");
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	protected void blockDispatch(Runnable task) {
		String method = "blockDispatch";
		Assert.notNull(task, "task is null");
		Assert.notNull(queue, "queue is not initialized or already terminated");
		try {
			queue.take().assignTask(task);
		} catch (Exception e) {
			logError(method, "Error waiting for worker thread", e);
			throw handleException(e);
		}
	}

	protected AcsRuntimeException handleException(Exception e) {
		String method = "handleException";
		logError(method, e);
		if (e.getClass().isAssignableFrom(AcsRuntimeException.class)) {
			return (AcsRuntimeException) e;
		} else {
			return new AcsSystemException("Exception encountered: " + e.getClass().getName(), e);
		}
	}

	protected abstract int getNumWorkerThreads();

	class WorkerThread extends Thread {
		boolean terminated = false;
		Runnable task = null;

		WorkerThread(String name) {
			super(name);
		}

		synchronized void terminate() {
			terminated = true;
			notify();
		}

		synchronized void assignTask(Runnable task) {
			this.task = task;
			notify();
		}

		synchronized void waitForTask() {
			queue.add(this);
			try {
				this.wait();
			} catch (InterruptedException e) {
			}
		}

		@Override
		public void run() {
			String method = "WorkerThread.run";
			logInfo(method, "%s started", this.getName());
			while (!terminated) {
				waitForTask();
				if (task != null) {
					try {
						task.run();
					} catch (Throwable t) {
						logError(method, "ERROR executing task", t);
					}
				} else {
					logError(method, "task is null!");
				}
			}
			logInfo(method, "%s terminated!", this.getName());
		}
	}
}
