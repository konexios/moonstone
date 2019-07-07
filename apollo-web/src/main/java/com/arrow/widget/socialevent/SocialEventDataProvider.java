package com.arrow.widget.socialevent;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

import com.arrow.pegasus.data.SocialEvent;

public interface SocialEventDataProvider {
	List<SocialEvent> findAllSocialEvents();

	SocialEvent findSocialEventById(String id);

	// TODO: check of we need to provide a date for request
	// now it will use server time
	SocialEventStat requestStat(String socialEventId, ZoneOffset zoneOffset);

	List<SocialEventLastRegistered> requestLastRegisteredBulk(String socialEventId, Instant lastUpdateTime,
	        ZoneOffset zoneOffset);

	List<SocialEventLastRegistered> requestLastRegistered(String socialEventId, int size);
}
