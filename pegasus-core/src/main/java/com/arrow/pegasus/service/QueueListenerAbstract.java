package com.arrow.pegasus.service;

import org.springframework.util.Assert;

public abstract class QueueListenerAbstract extends BlockingProcessorAbstract {

	private String[] queues = {};
	private boolean started = false;

	public void setQueues(String[] queues) {
		Assert.notEmpty(queues, "empty queues");
		this.queues = queues;
	}

	public String[] getQueues() {
		Assert.notEmpty(queues, "empty queues");
		return queues;
	}

	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}

	@Override
	protected int getNumWorkerThreads() {
		return 1;
	}

	public abstract void receiveMessage(byte[] message, String queueName);
}
