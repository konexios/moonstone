package com.arrow.widget.socialevent;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.arrow.dashboard.runtime.model.UserWrapper;
import com.arrow.dashboard.widget.WidgetDataProviderAbstract;
import com.arrow.dashboard.widget.annotation.data.DataProviderImpl;
import com.arrow.dashboard.widget.annotation.data.DataProviderImpl.Retention;
import com.arrow.kronos.data.SocialEventRegistration;
import com.arrow.kronos.data.SocialEventRegistrationStatuses;
import com.arrow.kronos.repo.SocialEventRegistrationSearchParams;
import com.arrow.kronos.service.SocialEventRegistrationService;
import com.arrow.pegasus.client.api.ClientSocialEventApi;
import com.arrow.pegasus.data.SocialEvent;

@Component
@DataProviderImpl(dataProviders = SocialEventDataProvider.class, retention = Retention.AUTOWIRED)
public class SocialEventDataProviderImpl extends WidgetDataProviderAbstract {

	@Autowired
	private ClientSocialEventApi clientSocialEventApi;

	@Autowired
	private SocialEventRegistrationService socialEventRegistrationService;

	public List<SocialEvent> findAllSocialEvents(UserWrapper userWrapper) {
		Assert.notNull(userWrapper, "userWrapper is null");

		// get all. no filters
		return clientSocialEventApi.findBy(null, null, null, null);
	}

	public SocialEvent findSocialEventById(UserWrapper userWrapper, String id) {
		Assert.notNull(userWrapper, "userWrapper is null");
		Assert.hasText(id, "uid is empty");

		return clientSocialEventApi.findById(id);
	}

	// TODO: check of we need to provide a date for request
	// now it will use server time
	public SocialEventStat requestStat(UserWrapper userWrapper, String socialEventId, ZoneOffset zoneOffset) {

		String method = "requestStat";
		logDebug(method, "...");

		SocialEvent event = checkEvent(userWrapper, socialEventId);

		SocialEventStat stat = new SocialEventStat();

		stat.setTodayRegisteredUsersCount(findTodaysSocialEventRegistrations(socialEventId, zoneOffset).size());

		SocialEventRegistrationSearchParams paramsForAllRegistered = new SocialEventRegistrationSearchParams();
		paramsForAllRegistered.addSocialEventIds(socialEventId);
		paramsForAllRegistered.setStatuses(EnumSet.of(SocialEventRegistrationStatuses.VERIFIED));

		paramsForAllRegistered.setUpdatedAfter(event.getStartDate());
		paramsForAllRegistered.setUpdatedBefore(event.getEndDate());

		stat.setAllRegisteredUsersCount(
		        socialEventRegistrationService.findSocialEventRegistrations(paramsForAllRegistered).size());

		logDebug(method, "observed statictics for social event " + event.getName() + ": " + stat);

		return stat;
	}

	public List<SocialEventLastRegistered> requestLastRegisteredBulk(UserWrapper userWrapper, String socialEventId,
	        Instant lastUpdateTime, ZoneOffset zoneOffset) {

		String method = "requestLastRegisteredBulk";
		logDebug(method, "...");

		checkEvent(userWrapper, socialEventId);

		List<SocialEventLastRegistered> regustrations = findTodaysSocialEventRegistrations(socialEventId, zoneOffset)
		        .stream().filter(se -> se.getLastModifiedDate().compareTo(lastUpdateTime) > 0)
		        .map(se -> new SocialEventLastRegistered(se.getLastModifiedDate().toEpochMilli(), se.getName()))
		        .collect(Collectors.toList());
		return regustrations;
	}

	public List<SocialEventLastRegistered> requestLastRegistered(UserWrapper userWrapper, String socialEventId,
	        int size) {

		String method = "requestLastRegistered";
		logDebug(method, "...");

		checkEvent(userWrapper, socialEventId);

		SocialEventRegistrationSearchParams params = new SocialEventRegistrationSearchParams();
		params.addSocialEventIds(socialEventId);
		params.setStatuses(EnumSet.of(SocialEventRegistrationStatuses.VERIFIED));

		Sort sort = new Sort(Direction.DESC, "lastModifiedDate");
		PageRequest pageRequest = new PageRequest(0, size, sort);

		Page<SocialEventRegistration> registrationPage = socialEventRegistrationService
		        .findSocialEventRegistrations(pageRequest, params);

		List<SocialEventLastRegistered> registrations = new ArrayList<>();
		for (SocialEventRegistration ser : registrationPage.getContent())
			registrations.add(new SocialEventLastRegistered(ser.getLastModifiedDate().toEpochMilli(), ser.getName()));

		return registrations;
	}

	private SocialEvent checkEvent(UserWrapper userWrapper, String socialEventId) {
		String method = "checkEvent";
		logDebug(method, "...");

		Assert.notNull(userWrapper, "userWrapper is null");
		Assert.hasText(socialEventId, "socialEventId is empty");

		SocialEvent event = clientSocialEventApi.findById(socialEventId);
		Assert.notNull(event, "no event found with id " + socialEventId);

		return event;
	}

	private List<SocialEventRegistration> findTodaysSocialEventRegistrations(String socialEventId,
	        ZoneOffset zoneOffset) {
		SocialEventRegistrationSearchParams paramsForRegisteredToday = new SocialEventRegistrationSearchParams();
		paramsForRegisteredToday.addSocialEventIds(socialEventId);
		paramsForRegisteredToday.setStatuses(EnumSet.of(SocialEventRegistrationStatuses.VERIFIED));

		paramsForRegisteredToday.setUpdatedAfter(LocalDate.now().atStartOfDay().toInstant(zoneOffset));
		paramsForRegisteredToday
		        .setUpdatedBefore(LocalDate.now().atStartOfDay().plusDays(1).minusSeconds(1).toInstant(zoneOffset));

		return socialEventRegistrationService.findSocialEventRegistrations(paramsForRegisteredToday);
	}
}
