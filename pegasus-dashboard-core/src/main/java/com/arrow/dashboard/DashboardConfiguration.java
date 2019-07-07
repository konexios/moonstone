package com.arrow.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.arrow.dashboard.runtime.BoardRuntimeManager;
import com.arrow.dashboard.runtime.DataProviderScanner;
import com.arrow.dashboard.runtime.RouterRuntimeManager;
import com.arrow.dashboard.runtime.WidgetRuntimeDefinitionManager;
import com.arrow.dashboard.runtime.WidgetRuntimeManager;
import com.arrow.dashboard.widget.configuration.ConfigurationPersistence;
import com.arrow.widget.ootb.UserProviderImplementation;

@org.springframework.context.annotation.Configuration
@EnableMongoRepositories(basePackages = { "com.arrow.pegasus.dashboard.repo" })
@EnableAutoConfiguration
@ComponentScan(basePackages = "")
public class DashboardConfiguration extends DashboardEntityAbstract {

	@Autowired
	private AutowireCapableBeanFactory beanFactory;

	@Bean
	public DataProviderScanner getDataProviderScanner() {
		DataProviderScanner dataProviderScanner = new DataProviderScanner();
		beanFactory.autowireBean(dataProviderScanner);
		dataProviderScanner.scan();
		return dataProviderScanner;
	}

	@Bean
	BoardRuntimeManager getBoardRuntimeManager() {
		BoardRuntimeManager boardRuntimeManager = new BoardRuntimeManager();
		beanFactory.autowireBean(boardRuntimeManager);
		return boardRuntimeManager;
	}

	@Bean
	public WidgetRuntimeManager getWidgetManager() {
		WidgetRuntimeManager widgetManager = new WidgetRuntimeManager();
		beanFactory.autowireBean(widgetManager);
		return widgetManager;
	}

	@Bean
	public WidgetRuntimeDefinitionManager getWidgetScanner() {
		WidgetRuntimeDefinitionManager widgetScanner = new WidgetRuntimeDefinitionManager();
		beanFactory.autowireBean(widgetScanner);
		widgetScanner.scan();
		return widgetScanner;
	}

	@Bean
	public RouterRuntimeManager getRoutingService() {
		RouterRuntimeManager routingService = new RouterRuntimeManager();
		return routingService;
	}

	@Bean
	public UserProviderImplementation getUserProviderImplementation() {
		UserProviderImplementation userProviderImplementation = new UserProviderImplementation();
		beanFactory.autowireBean(userProviderImplementation);
		return userProviderImplementation;
	}

	@Bean
	public ConfigurationPersistence getConfigurationPersistence() {
		ConfigurationPersistence c = new ConfigurationPersistence();
		beanFactory.autowireBean(c);
		return c;
	}
}
