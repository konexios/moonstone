package com.arrow.widget;

import com.arrow.dashboard.widget.ScheduledWidgetAbstract;
import com.arrow.dashboard.widget.annotation.Widget;
import com.arrow.dashboard.widget.annotation.configuration.ConfigurationPageRequest;
import com.arrow.dashboard.widget.annotation.configuration.ConfigurationPersistence;
import com.arrow.dashboard.widget.annotation.configuration.ConfigurationRequest;
import com.arrow.dashboard.widget.annotation.messaging.MessageEndpoint;
import com.arrow.dashboard.widget.annotation.messaging.TopicProvider;
import com.arrow.dashboard.widget.annotation.size.Large;
import com.arrow.dashboard.widget.annotation.size.Medium;
import com.arrow.dashboard.widget.annotation.size.Small;
import com.arrow.dashboard.widget.annotation.size.XtraLarge;
import com.arrow.dashboard.widget.configuration.Configuration;
import com.arrow.dashboard.widget.configuration.Page;
import com.arrow.widget.configuration.WidgetConfigurationHelper;

@Small(width = 1, height = 1)
@Medium(width = 2, height = 1)
@Large(width = 2, height = 2)
@XtraLarge(width = 3, height = 3)
@Widget(directive = "logo-widget", name = "Logo Widget", description = "A widget that shows a logo image.")
public class LogoWidget extends ScheduledWidgetAbstract {

	public LogoWidget() {
		// TODO implement abstract class for widgets with no start/stop/run
		// support (widget that does nothing on BE)
		super(1);
	}

	private String url;

	@ConfigurationRequest
	public Configuration initialConfig() {

		String method = "initialConfig";
		logInfo(method, "...");

		Configuration configuration = null;

		// @formatter:off
		configuration = new Configuration()
				.withName(WidgetConstants.Configuration.Name.LOGO_WIDGET)
				.withLabel(WidgetConstants.Configuration.Label.LOGO_WIDGET)
				.setChangedPage(0)
				.setCurrentPage(0);
		// @formatter:on

		Page logoUrlPage = new Page().withName(WidgetConstants.Page.Name.LOGO_URL)
		        .withLabel(WidgetConstants.Page.Label.LOGO_URL);
		logoUrlPage.addProperty(WidgetConfigurationHelper.buildPicUrlPageProperty(url));
		configuration.addPage(logoUrlPage);

		return configuration;
	}

	@ConfigurationPageRequest(page = 0)
	public Configuration processUrlSelection(Configuration configuration) {
		String method = "processUrlSelection";
		logInfo(method, "...");

		// re-populate based on selections
		populateConfigurationRuntimeModel(configuration);

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
		populateConfigurationRuntimeModel(configuration);
	}

	@MessageEndpoint("/url")
	@TopicProvider("/url")
	public String getUrl(String dummy) {
		return url;
	}

	private void populateConfigurationRuntimeModel(Configuration configuration) {
		String method = "populateUrl";
		logInfo(method, "...");

		if (configuration != null) {
			url = Configuration.<String> getValue(configuration, WidgetConstants.Page.Name.LOGO_URL,
			        WidgetConstants.Property.Name.LOGO_URL);
			logDebug(method, "url: %s", url);
		}
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		if (isReady()) {
			onRunningState();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		String method = "run";
		logInfo(method, "...");
	}
}
