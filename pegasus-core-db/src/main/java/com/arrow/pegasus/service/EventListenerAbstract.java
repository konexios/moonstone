package com.arrow.pegasus.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.arrow.pegasus.data.event.Event;

import moonstone.acs.JsonUtils;

public abstract class EventListenerAbstract extends RabbitListenerAbstract {

	@Autowired
	private EventService eventService;

	@Override
	public void receiveMessage(byte[] message, String queue) {
		String method = "EventListenerAbstract.receiveMessage";
		try {
			logDebug(method, "queue: %s, message: %s", queue, message);
			processEvent(queue, JsonUtils.fromJsonBytes(message, Event.class));
		} catch (Throwable t) {
			logError(method, "system error while processing event", t);
		}
	}

	protected EventService getEventService() {
		return eventService;
	}

	protected abstract void processEvent(String queue, Event event);
}
