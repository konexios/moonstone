package com.arrow.dashboard.widget.configuration;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.arrow.acs.Loggable;
import com.arrow.dashboard.property.Property;
import com.arrow.dashboard.property.impl.PropertyDeserializer.DeserializedProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Utility class that helps {@link ConfigurationManager} to work with
 * {@link Configuration}
 * 
 * @author dantonov
 *
 */
public class ConfigurationUtils extends Loggable {

	private static Logger LOGGER = LoggerFactory.getLogger(ConfigurationUtils.class);

	/**
	 * Method to apply changes, made on front end, into configuration
	 * <p>
	 * Basically, idea is to always keep configuration that has been sent to
	 * front end to be 'filled' by the user<br>
	 * Front end will process configuration and provide changed one<br>
	 * Since {@link Configuration} supports any {@link Property} inside, and the
	 * configuration object passed via JSON transformation, we will 'just put'
	 * new values of properties to previously sent configuration
	 * <p>
	 * This allows us to:<br>
	 * - keep configuration consistent (we can always check that configuration
	 * structure is not broken)<br>
	 * - keep original property objects (since they are dynamically
	 * defined/created inside the widget)
	 * 
	 * @param processedOnFrontEndConfiguration
	 * @param originallySentConfiguration
	 */
	public static void processChangesInConfiguration(Configuration processedOnFrontEndConfiguration,
	        Configuration originallySentConfiguration) {

		LOGGER.debug("processing configuration changes");
		LOGGER.trace("processedOnFrontEndConfiguration: " + processedOnFrontEndConfiguration
		        + ", originallySentConfiguration: " + originallySentConfiguration);

		originallySentConfiguration.setChangedPage(processedOnFrontEndConfiguration.getChangedPage());
		originallySentConfiguration.setCurrentPage(processedOnFrontEndConfiguration.getCurrentPage());

		processedOnFrontEndConfiguration.getPages().stream().forEach(page -> {

			LOGGER.trace("process next page");

			String pageName = page.getName();

			LOGGER.trace("page name " + pageName);

			Optional<Page> managedPage = originallySentConfiguration.getPages().stream()
		            .filter(p -> p.getName().equals(pageName)).findFirst();

			LOGGER.trace("managed page with name " + pageName + " "
		            + (managedPage.isPresent() ? "exists" : "does not exist"));

			if (managedPage.isPresent()) {

				processChangesInPage(page, managedPage.get());
			}

		});
	}

	/**
	 * Method to apply changes in the {@link Page}
	 * <p>
	 * Similar to
	 * {@link ConfigurationUtils#processChangesInConfiguration(Configuration, Configuration)}
	 * 
	 * @param processedOnFrontEndPage
	 * @param originallySentPage
	 */
	public static void processChangesInPage(Page processedOnFrontEndPage, Page originallySentPage) {

		LOGGER.trace("process changes in page");
		if (processedOnFrontEndPage != null) {
			processedOnFrontEndPage.getProperties().stream().forEach(property -> {

				String propertyName = property.getName();
				LOGGER.trace("process property " + propertyName + ". property instance of: "
			            + property.getProperty().getClass().getName());

				if (property.getProperty() instanceof DeserializedProperty) {

					DeserializedProperty jsonProp = (DeserializedProperty) property.getProperty();

					String valueString = jsonProp.getValue();

					LOGGER.trace("value string is " + valueString);

					Optional<Page> managedPage = Optional.of(originallySentPage);

					LOGGER.trace("managed page " + (managedPage.isPresent() ? "exist" : "does not exist"));

					if (managedPage.isPresent()) {

						Optional<PageProperty> pageProp = managedPage.get().getProperties().stream()
			                    .filter(pageProperty -> pageProperty.getName().equals(propertyName)).findFirst();

						LOGGER.trace("managed page property " + (pageProp.isPresent() ? "exist" : "does not exist"));

						if (pageProp.isPresent()) {
							replacePropertyValue(pageProp.get(), valueString);
						}

					}
				}

			});
		}
	}
	
	public static void replaceValue(Configuration configuration, String propertyName, String value) {

		LOGGER.trace("replaceValue: propertyName: "+propertyName+", value: "+value);

		configuration.getPages().stream().flatMap(page -> page.getProperties().stream())
				.filter(prop -> prop.getName().equals(propertyName)).forEach(pageProperty -> {
					ConfigurationUtils.replacePropertyValue(pageProperty, value);
				});

	}
	
	private static void replacePropertyValue(PageProperty pageProperty, String valueString){
		Property<?, ?, ?> managedProperty = pageProperty.getProperty();
		Class<?> managedPropertyType = managedProperty.getType();

		LOGGER.trace("managed page property type is " + managedPropertyType.getName());

		LOGGER.trace("value to replace for property is " + valueString);
		
		Type valueType = ((ParameterizedType) managedPropertyType.getGenericInterfaces()[0])
                .getActualTypeArguments()[0];

		LOGGER.trace("managed page property value type is " + valueType);
		
		String valueClassName;
		if (valueType.toString().contains(" ")){
			valueClassName = valueType.toString().split(" ")[1];
		}else {
			valueClassName = valueType.toString();
		}
		
		if (valueClassName.contains("<")){
			// assume generic type. very carefully with this! right now working fine with List<String> only as property value
			valueClassName = valueClassName.replaceAll("<.*>", "");
		}

		LOGGER.trace("managed page property value class name is " + valueClassName);

		try {
			Class<?> valueClass = Class.forName(valueClassName);

			ObjectMapper mapper = new ObjectMapper();
			Object check = valueString == null ? null : mapper.readValue(valueString, valueClass);

			Class<?> managedPropertyClass = managedProperty.getClass();

			LOGGER.trace("managed page property class is " + managedPropertyClass.getName());

			Field valueField = managedProperty.getClass().getDeclaredField("value");
			valueField.setAccessible(true);
			valueField.set(managedProperty, check);
			valueField.setAccessible(false);

		} catch (Throwable t) {
			LOGGER.trace("failed to write value", t);
			LOGGER.error("Failed to write property value " + t.getMessage());
		}
	}
}
