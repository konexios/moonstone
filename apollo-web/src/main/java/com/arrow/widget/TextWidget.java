package com.arrow.widget;

import java.util.concurrent.TimeUnit;

import com.arrow.dashboard.messaging.SimpleTopicProvider;
import com.arrow.dashboard.properties.string.SimpleStringView;
import com.arrow.dashboard.properties.string.StringPropertyBuilder;
import com.arrow.dashboard.widget.ScheduledWidgetAbstract;
import com.arrow.dashboard.widget.WidgetData;
import com.arrow.dashboard.widget.annotation.Widget;
import com.arrow.dashboard.widget.annotation.configuration.ConfigurationPageRequest;
import com.arrow.dashboard.widget.annotation.configuration.ConfigurationPersistence;
import com.arrow.dashboard.widget.annotation.configuration.ConfigurationRequest;
import com.arrow.dashboard.widget.annotation.messaging.TopicProvider;
import com.arrow.dashboard.widget.annotation.size.Large;
import com.arrow.dashboard.widget.annotation.size.Medium;
import com.arrow.dashboard.widget.annotation.size.Small;
import com.arrow.dashboard.widget.annotation.size.XtraLarge;
import com.arrow.dashboard.widget.configuration.Configuration;
import com.arrow.dashboard.widget.configuration.Page;
import com.arrow.dashboard.widget.configuration.PageProperty;

@Small(width = 1, height = 1)
@Medium(width = 2, height = 1)
@Large(width = 3, height = 1)
@XtraLarge(width = 4, height = 1)
@Widget(directive = "text-widget", name = "Text Widget", description = "A widget that shows text.")
public class TextWidget extends ScheduledWidgetAbstract {

	@TopicProvider("/widget-data")
	private SimpleTopicProvider textTopicProvider;

	private String text;

	public TextWidget() {
		super(1); // corePoolSize
	}

	@ConfigurationRequest
	public Configuration initialConfig() {
		String method = "initialConfig";
		logInfo(method, "...");

		PageProperty textProperty = new PageProperty().withName(WidgetConstants.Property.Name.TEXT)
		        .withLabel(WidgetConstants.Property.Label.CONTENT).withDescription("Text to display")
		        .withProperty(new StringPropertyBuilder().withValue(text).withView(new SimpleStringView()).build());

		Page page = new Page().withName(WidgetConstants.Page.Name.TEXT).withLabel(WidgetConstants.Page.Label.TEXT)
		        .addProperty(textProperty);

		// @formatter:off
		Configuration configuration = new Configuration()
				.withName(WidgetConstants.Configuration.Name.TEXT_WIDGET)
				.withLabel(WidgetConstants.Configuration.Label.TEXT_WIDGET)
				.setChangedPage(0)
				.setCurrentPage(0)
		        .addPage(page);
		// @formatter:on 

		return configuration;
	}

	@ConfigurationPageRequest(configurationName = WidgetConstants.Configuration.Name.TEXT_WIDGET)
	public Configuration processText(Configuration configuration) {
		String method = "processText";
		logInfo(method, "...");

		// close configuration
		configuration.close();

		return configuration;
	}

	@ConfigurationPersistence
	public void loadConfiguration(Configuration configuration) {
		String method = "loadConfiguration";
		logInfo(method, "...");
		logDebug(method, "name: %s, version: %s", configuration.getName(), configuration.getVersion());

		text = Configuration.<String> getValue(configuration, WidgetConstants.Page.Name.TEXT,
		        WidgetConstants.Property.Name.TEXT);
		logDebug(method, "text: %s", text);
	}

	@Override
	public void start() {
		String method = "start";
		logInfo(method, "...");

		if (isReady()) {

			onRunningState();
		}
	}

	@Override
	public void run() {
		String method = "run";
		logInfo(method, "...");

		addScheduledWorker(new ScheduledWorker(), 0, 1, TimeUnit.SECONDS);
	}

	private class ScheduledWorker implements Runnable {
		@Override
		public void run() {
			String method = "run";
			logInfo(method, "...");
			logDebug(method, "state: %s", getState());

			try {
				sendMessage(textTopicProvider, new WidgetData().withState(getState()).withData(text));
			} catch (Throwable t) {
				logError(method, "error refreshing last location", t);
			}
		}
	}
}
