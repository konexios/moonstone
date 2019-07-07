package com.arrow.kronos.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.pegasus.data.event.Event;
import com.arrow.pegasus.service.EventService;

@RestController
@RequestMapping("/api/kronos/event")
public class EventController extends BaseControllerAbstract {

	@Autowired
	private EventService eventService;

	@RequestMapping(value = "/statuses", method = RequestMethod.POST)
	public Event[] getStatuses(@RequestBody List<String> eventIds) {
		Iterable<Event> eventResults = eventService.getEventRespository().findAllById(eventIds);

		Event[] events = new Event[eventIds.size()];
		for (Event event : eventResults) {
			int index = eventIds.indexOf(event.getId());
			if (index >= 0 && index < events.length) {
				// statuses[index] = event.getStatus().name();
				events[index] = event;
			}
		}

		return events;
	}
}
