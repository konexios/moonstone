package com.arrow.dashboard.widget.configuration;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.util.Assert;

import com.arrow.dashboard.DashboardEntityAbstract;
import com.arrow.dashboard.property.Controller;
import com.arrow.dashboard.property.Property;
import com.arrow.dashboard.property.SimpleProperty;
import com.arrow.dashboard.property.impl.DefaultController;
import com.arrow.dashboard.property.impl.PropertyImpl;
import com.arrow.pegasus.dashboard.data.ConfigurationPage;
import com.arrow.pegasus.dashboard.data.PageProperty;
import com.arrow.pegasus.dashboard.data.PropertyValue;
import com.arrow.pegasus.dashboard.data.WidgetConfiguration;
import com.arrow.pegasus.dashboard.repo.ConfigurationPageSearchParams;
import com.arrow.pegasus.dashboard.repo.PagePropertySearchParams;
import com.arrow.pegasus.dashboard.repo.PropertyValueSearchParams;
import com.arrow.pegasus.dashboard.service.ConfigurationPageService;
import com.arrow.pegasus.dashboard.service.PagePropertyService;
import com.arrow.pegasus.dashboard.service.PropertyValueService;
import com.arrow.pegasus.dashboard.service.WidgetConfigurationService;

/**
 * Class to manage configuration persistence for {@link ConfigurationManager}
 * 
 * FIXME: exceptions
 * 
 * @author dantonov
 *
 */

public class ConfigurationPersistence extends DashboardEntityAbstract {

	@Autowired
	private WidgetConfigurationService widgetConfigurationService;
	@Autowired
	private ConfigurationPageService configurationPageService;
	@Autowired
	private PagePropertyService pagePropertyService;
	@Autowired
	private PropertyValueService propertyValueService;

	public ConfigurationPersistence() {
		super();
	}

	/**
	 * Method to persist {@link Configuration} for widget instance
	 * 
	 * @param widgetInstanceId
	 * @param configuration
	 */
	public void saveConfiguration(String widgetInstanceId, Configuration configuration, String who) {
		Assert.hasText(widgetInstanceId, "widgetInstanceId is empty");
		Assert.notNull(configuration, "configuration is null");

		String method = "saveConfiguration";
		logInfo(method, "...");
		logDebug(method, "ConfigurationPersistence saves configuration for widget " + widgetInstanceId);
		logTrace(method,
		        "ConfigurationPersistence saves configuration for widget " + widgetInstanceId + " : " + configuration);

		deleteExistingConfiguration(widgetInstanceId);

		logDebug(method, "ConfigurationPersistence is going to save new configuration for widget " + widgetInstanceId);

		WidgetConfiguration widgetConfiguration = new WidgetConfiguration();
		widgetConfiguration.setChangedPage(configuration.getChangedPage());
		widgetConfiguration.setCurrentPage(configuration.getCurrentPage());
		widgetConfiguration.setName(configuration.getName());
		widgetConfiguration.setLabel(configuration.getLabel());
		widgetConfiguration.setVersion(configuration.getVersion());
		widgetConfiguration.setWidgetId(widgetInstanceId);

		// create widget configuration
		widgetConfiguration = widgetConfigurationService.create(widgetConfiguration, who);

		for (int i = 0; i < configuration.getPages().size(); i++) {

			Page page = configuration.getPages().get(i);
			logDebug(method, "ConfigurationPersistence is going to save page " + page.getName()
			        + " for new configuration for widget " + widgetInstanceId);

			ConfigurationPage configurationPage = new ConfigurationPage();
			configurationPage.setWidgetConfigurationId(widgetConfiguration.getId());
			configurationPage.setName(page.getName());
			configurationPage.setLabel(page.getLabel());
			configurationPage.setPageNumber(i);

			// create configuration page
			configurationPage = configurationPageService.create(configurationPage, who);

			for (int p = 0; p < page.getProperties().size(); p++) {

				com.arrow.dashboard.widget.configuration.PageProperty configurationPageProperty = page.getProperties()
				        .get(p);

				logDebug(method,
				        "ConfigurationPersistence is " + (configurationPageProperty.isPersisted() ? "" : "NOT")
				                + " going to save page property " + configurationPageProperty.getName() + "for page "
				                + page.getName() + " for new configuration for widget " + widgetInstanceId);

				if (configurationPageProperty.isPersisted()) {
					// note: skip index p if not persisted
					PageProperty pageProperty = new PageProperty();
					pageProperty.setActive(configurationPageProperty.isActive());
					pageProperty.setConfigurationPageId(configurationPage.getId());
					pageProperty.setName(configurationPageProperty.getName());
					pageProperty.setLabel(configurationPageProperty.getLabel());
					pageProperty.setPropertyNumber(p);
					pageProperty.setRequired(configurationPageProperty.isRequired());
					pageProperty.setDescription(configurationPageProperty.getDescription());

					// create page property
					pageProperty = pagePropertyService.create(pageProperty, who);

					Property<?, ?, ?> property = configurationPageProperty.getProperty();
					Controller controller = property.getController();
					Object valueObject = controller.persist(property.getValue());

					PropertyValue propertyValue = new PropertyValue();
					propertyValue.setPagePropertyId(pageProperty.getId());
					propertyValue.setType(property.getType().getName());
					propertyValue.setValue(valueObject);
					propertyValue.setVersion(property.getVersion());

					// create property value
					propertyValueService.create(propertyValue, who);
				}
			}
		}
	}

	/**
	 * Method to get persisted configuration
	 * 
	 * @param widgetInstance
	 * @return
	 */
	public Configuration readConfiguration(String widgetId) {
		Assert.hasText(widgetId, "widgetId is empty");

		String method = "readConfiguration";
		logInfo(method, "...");
		logDebug(method, "Reading configuration for widget. widgetId=%s", widgetId);

		WidgetConfiguration widgetConfiguration = findWidgetConfiguration(widgetId);

		if (widgetConfiguration != null) {

			Configuration configuration = new Configuration().withName(widgetConfiguration.getName())
			        .withLabel(widgetConfiguration.getLabel()).withVersion(widgetConfiguration.getVersion())
			        .setCurrentPage(widgetConfiguration.getCurrentPage())
			        .setChangedPage(widgetConfiguration.getChangedPage());

			logDebug(method, "Reading configuration pages for widget. widgetId=%s, widgetConfigurationId=%s", widgetId,
			        widgetConfiguration.getId());

			ConfigurationPageSearchParams configurationPageSearchParams = new ConfigurationPageSearchParams();
			configurationPageSearchParams.addWidgetConfigurationIds(widgetConfiguration.getId());

			// loop over each configuration page and load it's properties
			configurationPageService.getConfigurationPageRepository()
			        .findConfigurationPages(configurationPageSearchParams, new Sort(Direction.ASC, "pageNumber"))
			        .stream().forEach(configurationPage -> {
				        logDebug(method, "Reading configuration page: name=%s, configurationPageId=%s",
			                    configurationPage.getName(), configurationPage.getId());

				        // convert to page and add to configuration
				        Page page = new Page().withName(configurationPage.getName())
			                    .withLabel(configurationPage.getLabel());
				        configuration.addPage(page);

				        // loop over each page property and load it's value
				        PagePropertySearchParams pagePropertySearchParams = new PagePropertySearchParams();
				        pagePropertySearchParams.addConfigurationPageIds(configurationPage.getId());
				        pagePropertyService.getPagePropertyRepository()
			                    .findPageProperties(pagePropertySearchParams, new Sort(Direction.ASC, "propertyNumber"))
			                    .stream().forEach(pageProperty -> {
				                    logDebug(method, "Reading page property: name=%s, pagePropertyId=%s",
			                                pageProperty.getName(), pageProperty.getId());

				                    // convert to page property and add to page
				                    com.arrow.dashboard.widget.configuration.PageProperty configurationPageProperty = new com.arrow.dashboard.widget.configuration.PageProperty()
			                                .withName(pageProperty.getName()).withLabel(pageProperty.getLabel())
			                                .setActive(pageProperty.isActive()).setRequired(pageProperty.isRequired())
			                                .withDescription(pageProperty.getDescription());
				                    page.addProperty(configurationPageProperty);

				                    // populate the page properties value
				                    PropertyValueSearchParams propertyValueSearchParams = new PropertyValueSearchParams();
				                    propertyValueSearchParams.addPagePropertyIds(pageProperty.getId());
				                    propertyValueService.getPropertyValueRepository()
			                                .findPropertyValues(propertyValueSearchParams).forEach(propertyValue -> {
				                                logDebug(method, "Reading property value: type=%s, propertyValueId=%s",
			                                            propertyValue.getType(), propertyValue.getId());

				                                String propertyType = propertyValue.getType();
				                                Object value = propertyValue.getValue();

				                                try {
					                                Class<?> propertyClass = Class.forName(propertyType);

					                                logDebug(method,"propertyClass: "+propertyClass);
					                                
					                                ParameterizedType genericProperty = (ParameterizedType) propertyClass
			                                                .getGenericInterfaces()[0];
					                                
					                                logDebug(method,"genericProperty: "+genericProperty);
					                                
					                                String modelTypeRaw = String.valueOf(genericProperty.getActualTypeArguments()[0]);
					  
					                                logDebug(method,"modelTypeRaw: "+modelTypeRaw);
					                                
					                                Class<?> modelType;
					                                if (modelTypeRaw.contains("<")){
					                                	// generic in model type. apply hot fix...
					                                	modelTypeRaw = modelTypeRaw.replaceAll("<.*>", "");
					                                	modelType = Class.forName(modelTypeRaw);
					                                }else {
					                                	modelType = (Class<?>) genericProperty
				                                                .getActualTypeArguments()[0];
					                                }
					                                
					                                Class<?> viewType = (Class<?>) genericProperty
			                                                .getActualTypeArguments()[1];

					                                logDebug(method, "types for property: " + "genericProperty: "
			                                                + genericProperty.getRawType() + ", " + "modelType: "
			                                                + modelType + ", " + "viewType: " + viewType);

					                                Class<?> controllerType = null;
					                                if (genericProperty.getRawType().getTypeName()
			                                                .equals(SimpleProperty.class.getTypeName())) {
						                                controllerType = DefaultController.class;
					                                } else {
						                                controllerType = (Class<?>) genericProperty
			                                                    .getActualTypeArguments()[2];
					                                }

					                                // convert and add to
			                                        // configuration property
					                                Controller controllerInstance = (Controller) controllerType
			                                                .newInstance();
					                                Object restoredValue = controllerInstance.restore(value);

					                                Property<?, ?, ?> restoredProperty = new PropertyImpl(restoredValue,
			                                                null, controllerInstance, propertyClass);
					                                configurationPageProperty.withProperty(restoredProperty);
				                                } catch (Throwable t) {
					                                logError(method,
			                                                "ConfigurationPersistence failed to add value of type "
			                                                        + propertyType + " for page property "
			                                                        + pageProperty.getName() + " for the page "
			                                                        + configurationPage.getName()
			                                                        + " for new configuration for widget " + widgetId
			                                                        + " with exception " + t.getMessage());
					                                logTrace(method,
			                                                "ConfigurationPersistence failed to add value of type "
			                                                        + propertyType + " for page property "
			                                                        + pageProperty.getName() + " for the page "
			                                                        + configurationPage.getName()
			                                                        + " for new configuration for widget " + widgetId,
			                                                t);

				                                }

			                                });

			                    });
			        });

			return configuration;
		}

		return null;
	}

	private WidgetConfiguration findWidgetConfiguration(String widgetId) {
		Assert.hasText(widgetId, "widgetId is empty");

		String method = "findWidgetConfiguration";
		logInfo(method, "...");
		logDebug(method, "Looking for widet configuration. widgetId=%s", widgetId);

		WidgetConfiguration widgetConfiguration = widgetConfigurationService.getWidgetConfigurationRepository()
		        .findByWidgetId(widgetId);
		logDebug(method, "Widget configuration " + (widgetConfiguration == null ? "does not exist" : "exists")
		        + ". widgetId=" + widgetId);

		return widgetConfiguration;

	}

	private void deleteExistingConfiguration(String widgetInstanceId) {
		Assert.hasText(widgetInstanceId, "widgetInstanceId is empty");

		String method = "deleteExistingConfiguration";
		logInfo(method, "...");
		logDebug(method, "widgetInstanceId: %s", widgetInstanceId);

		WidgetConfiguration existingConfiguration = findWidgetConfiguration(widgetInstanceId);
		if (existingConfiguration != null) {
			widgetConfigurationService.delete(existingConfiguration.getId());
		}
	}
}