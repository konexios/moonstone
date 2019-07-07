package com.arrow.widget.socialevent;

import java.time.ZoneOffset;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import com.arrow.dashboard.messaging.SimpleTopicProvider;
import com.arrow.dashboard.widget.ScheduledWidgetAbstract;
import com.arrow.dashboard.widget.WidgetData;
import com.arrow.dashboard.widget.annotation.Widget;
import com.arrow.dashboard.widget.annotation.configuration.ConfigurationPageRequest;
import com.arrow.dashboard.widget.annotation.configuration.ConfigurationPersistence;
import com.arrow.dashboard.widget.annotation.configuration.ConfigurationRequest;
import com.arrow.dashboard.widget.annotation.data.DataProvider;
import com.arrow.dashboard.widget.annotation.messaging.TopicProvider;
import com.arrow.dashboard.widget.annotation.size.Large;
import com.arrow.dashboard.widget.annotation.size.Medium;
import com.arrow.dashboard.widget.annotation.size.Small;
import com.arrow.dashboard.widget.annotation.size.XtraLarge;
import com.arrow.dashboard.widget.configuration.Configuration;
import com.arrow.dashboard.widget.configuration.Page;
import com.arrow.pegasus.data.SocialEvent;
import com.arrow.widget.WidgetConstants;
import com.arrow.widget.configuration.WidgetConfigurationHelper;

@Small(width = 1, height = 1)
@Medium(width = 2, height = 1)
@Large(width = 2, height = 2)
@XtraLarge(width = 4, height = 2)
@Widget(directive = "event-registered-widget", name = "Social Event Registration Statistics Widget", description = "A widget that shows the statistics of social event registrations.")
public class SocialEventRegistrationStatWidget extends ScheduledWidgetAbstract {

	@DataProvider
	private SocialEventDataProvider socialEventDataProvider;
	@TopicProvider("/statistics")
	private SimpleTopicProvider statisticsTopicProvider;

	private String timeZoneId;
	private String socialEventId;
	private SocialEvent socialEvent;

	public SocialEventRegistrationStatWidget() {
		super(1);
	}

	@ConfigurationRequest
	public Configuration initialConfig() {
		String method = "initialConfig";
		logInfo(method, "...");

		// @formatter:off
		Configuration configuration = new Configuration()
				.withName(WidgetConstants.Configuration.Name.SOCIAL_EVENT_REGISTRATION_WIDGET)
				.withLabel(WidgetConstants.Configuration.Label.SOCIAL_EVENT_REGISTRATION_WIDGET).setChangedPage(0)
				.setCurrentPage(0);
		// @formatter:on

		List<SocialEvent> events = socialEventDataProvider.findAllSocialEvents();

		if (events != null && !events.isEmpty()) {

			// single device selection page
			Page singleSocialEventPage = new Page().withName(WidgetConstants.Page.Name.SINGLE_SOCIAL_EVENT_SELECTION)
					.withLabel(WidgetConstants.Page.Label.SINGLE_SOCIAL_EVENT_SELECTION);
			singleSocialEventPage.addProperty(
					WidgetConfigurationHelper.buildSingleSocialEventSelectionPageProperty(events, socialEventId));
			singleSocialEventPage.addProperty(WidgetConfigurationHelper.buildTimeZomeSelectionPageProperty(timeZoneId));
			configuration.addPage(singleSocialEventPage);
		} else {
			configuration.withError("No available social events");
		}

		return configuration;
	}

	@ConfigurationPageRequest(page = 0)
	public Configuration processDeviceSelection(Configuration configuration) {
		String method = "processDeviceSelection";
		logInfo(method, "...");

		// re-populate based on selections
		populateConfigurationData(configuration);

		// close configuration
		configuration.close();

		return configuration;

	}

	@ConfigurationPersistence
	public void loadConfiguration(Configuration configuration) {
		String method = "loadConfiguration";
		logInfo(method, "...");
		logDebug(method, "name: %s, version: %s", configuration.getName(), configuration.getVersion());

		// populate
		populateConfigurationData(configuration);

	}

	@Override
	public void run() {
		String method = "run";
		logInfo(method, "...");

		addScheduledWorker(new ScheduledWorker(), 0, 250, TimeUnit.MILLISECONDS);
	}

	@Override
	public void start() {
		String method = "start";
		logInfo(method, "...");

		if (isReady()) {
			// lookup social event
			if (!StringUtils.isEmpty(socialEventId))
				socialEvent = socialEventDataProvider.findSocialEventById(socialEventId);

			if (socialEvent == null) {
				onErrorState();

				return;
			}

			onRunningState();
		}
	}

	private void populateConfigurationData(Configuration configuration) {
		String method = "populateConfigurationData";
		logInfo(method, "...");

		if (configuration != null) {
			// uid
			socialEventId = Configuration.<String>getValue(configuration,
					WidgetConstants.Page.Name.SINGLE_SOCIAL_EVENT_SELECTION,
					WidgetConstants.Property.Name.SOCIAL_EVENT_ID);

			timeZoneId = Configuration.<String>getValue(configuration,
					WidgetConstants.Page.Name.SINGLE_SOCIAL_EVENT_SELECTION,
					WidgetConstants.Property.Name.TIME_ZONE_ID);
		}
	}

	private class ScheduledWorker implements Runnable {
		@Override
		public void run() {
			String method = "run";
			logInfo(method, "...");
			logDebug(method, "state: %s", getState());

			try {
				SocialEventStat statistics = new SocialEventStat();
				if (timeZoneId != null) {
					TimeZone timeZone = TimeZone.getTimeZone(timeZoneId);
					long hours = TimeUnit.MILLISECONDS.toHours(timeZone.getRawOffset());
					long minutes = TimeUnit.MILLISECONDS.toMinutes(timeZone.getRawOffset())
							- TimeUnit.HOURS.toMinutes(hours);
					minutes = Math.abs(minutes);
					String zoneOffsetValue = "";
					if (hours >= 0) {
						zoneOffsetValue = String.format("+%02d:%02d", hours, minutes);
					} else {
						zoneOffsetValue = String.format("%03d:%02d", hours, minutes);
					}
					ZoneOffset zoneOffset = ZoneOffset.of(zoneOffsetValue);
					statistics = socialEventDataProvider.requestStat(socialEvent.getId(), zoneOffset);
				}
				logDebug(method, "statistics for social event" + socialEvent.getId() + ": " + statistics);
				sendMessage(statisticsTopicProvider, new WidgetData().withState(getState()).withData(statistics));
			} catch (Throwable t) {
				logError(method, "error refreshing social event statistics", t);
			}
		}
	}
}
