package com.arrow.pegasus.data.event;

import org.springframework.util.Assert;

public class EventBuilder {
	private Event event;

	private EventBuilder() {
		event = new Event();
		event.setType(EventType.Normal);
		event.setStatus(EventStatus.Pending);
	}

	public static EventBuilder create() {
		return new EventBuilder();
	}

	public EventBuilder name(String name) {
		event.setName(name);
		return this;
	}

	public EventBuilder applicationId(String applicationId) {
		Assert.hasLength(applicationId, "applicationId is empty");
		event.setApplicationId(applicationId);
		return this;
	}

	public EventBuilder encrypted() {
		event.setType(EventType.Encrypted);
		return this;
	}

	public EventBuilder parameter(EventParameter parameter) {
		Assert.notNull(parameter, "parameter is null");
		event.getParameters().add(parameter);
		return this;
	}

	public Event build() {
		return event;
	}
}
