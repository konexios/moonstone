package com.arrow.kronos.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.arrow.kronos.data.DeviceAction;
import com.arrow.pegasus.service.EventService;

import moonstone.acs.Loggable;

public abstract class ActionHandlerAbstract extends Loggable implements ActionHandler {

	@Autowired
	private EventService eventService;

	public EventService getEventService() {
		return eventService;
	}

	protected String getRequiredParameter(DeviceAction action, String name) {
		String value = action.getParameters().get(name);
		Assert.notNull(value, name + " is required");
		return value;
	}
}
