package com.arrow.dashboard.widget;

import com.arrow.dashboard.properties.string.SimpleStringView;
import com.arrow.dashboard.properties.string.StringPropertyBuilder;
import com.arrow.dashboard.widget.configuration.Page;
import com.arrow.dashboard.widget.configuration.PageProperty;

public abstract class WidgetConfigurationAbstract extends WidgetEntityAbstract {
	protected static final String WIDGET_INFORMATION_PAGE_NAME = "widget-information-page";
	protected static final String WIDGET_ASPECT_PAGE_NAME = "widget-aspect-page";

	protected Page addWidgetInformationPage() {
		// @formatter:off
		Page page = new Page().withName(WIDGET_INFORMATION_PAGE_NAME)
		        .addProperty(new PageProperty()
		        		.withName("name")
		        		.withDescription("name property description")
		                .setPersisted(true)
		                .setRequired(true)
		                .withProperty(new StringPropertyBuilder()
		                		.withValue("Widget Name")
		                		.withView(new SimpleStringView()).build()))
		        .addProperty(new PageProperty()
		        		.withName("description")
		                .withDescription("description property description")
		                .setPersisted(true)
		                .setRequired(true)
		                .withProperty(new StringPropertyBuilder()
		                		.withValue("Widget Description")
		                		.withView(new SimpleStringView()).build()));
		// @formatter:on 

		return page;
	}

	protected Page addWidgetAspectPage() {
		// @formatter:off
		Page page = new Page().withName(WIDGET_ASPECT_PAGE_NAME)
		        .addProperty(new PageProperty()
		        		.withName("size")
		        		.withDescription("size property description")
		                .setPersisted(true)
		                .setRequired(true)
		                .withProperty(new StringPropertyBuilder()
		                		.withValue("S")
		                		.withView(new SimpleStringView()).build()));
		// @formatter:on 

		return page;
	}
}
