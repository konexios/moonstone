package com.arrow.dashboard.widget.configuration;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.arrow.dashboard.DashboardEntityAbstract;
import com.arrow.dashboard.runtime.model.WidgetRuntimeConfigurationPatch;
import com.arrow.dashboard.runtime.model.WidgetRuntimeInstance;
import com.arrow.dashboard.widget.annotation.configuration.ConfigurationChanged;
import com.arrow.dashboard.widget.annotation.configuration.ConfigurationPageRequest;
import com.arrow.dashboard.widget.annotation.configuration.ConfigurationPersistence;
import com.arrow.dashboard.widget.annotation.configuration.ConfigurationRequest;
import com.arrow.dashboard.widget.annotation.configuration.FastConfigurationPageRequest;
import com.arrow.dashboard.widget.annotation.configuration.FastConfigurationRequest;
import com.arrow.dashboard.widget.configuration.messaging.ConfigurationTopic;
import com.arrow.dashboard.widget.configuration.messaging.FastConfigurationTopic;

/**
 * Configuration manager is a special handler for widget configuring
 * <p>
 * Each runtime widget instance has own configuration manager.<br>
 * This manager cares on request processing (to/from widget) and configurations
 * persistence<br>
 * Basic sequence of management:<br>
 * 1. on initialization, configuration manager detects all configuration
 * annotations in widget definition<br>
 * Refer to {@link ConfigurationRequest}, {@link ConfigurationPageRequest},
 * {@link ConfigurationPersistence} etc.<br>
 * 2. When, front end configurator sends messages with configuration to widget,
 * this manager receives them.<br>
 * According to internal logic, it calls appropriate methods on widget instance.
 * 3. Widget instance 'answers' on method calls and this manager provide
 * returned values to the front end 4. Configuration manager also provides
 * persisted configuration to widget on start up 5. Configuration manager
 * persists configuration for the widget (when widget returns closed
 * configuration to front end)
 * 
 * @author dantonov
 *
 */
public class ConfigurationManager extends DashboardEntityAbstract {

	private final static String KEY_DELIMITER = "%s.#.%s";

	// method to answer on configuration request - first request to get
	// configuration
	private Method configurationMethod;
	// map for methods that has page and configuration name specified for
	// @ConfigurationPageRequest annotation
	private Map<String, Method> namedPagedMethods;
	// map for methods that has only configuration name specified for
	// @ConfigurationPageRequest annotation
	private Map<String, Method> namedMethods;
	// map for methods that has only page specified for
	// @ConfigurationPageRequest annotation
	private Map<String, Method> pagedMethods;
	// methods that has @ConfigurationPageRequest annotation without parameters
	private Method commonMethod;
	// method that has @ConfigurationChanged annotation
	private Method configurationChangedMethod;
	// method that has @FastConfigurationRequest annotation
	private Method fastConfigurationMethod;
	// method that has @FastConfigurationPageRequst annotation
	private Method fastConfigurationPageMethod;
	// method that has @ConfigurationPersistence
	private Method persistedConfigurationMethod;
	// widget id
	private String widgetId;
	// string to be used in logs to describe the widget this manager is working
	// with
	private String widgetDescription;
	// widget instance object to call methods
	private Object widgetInstanceObject;
	// topic provider to answer during configuration processing
	private ConfigurationTopic configurationTopic;
	// topic provider to answer during fast configuration processing
	private FastConfigurationTopic fastConfigurationTopic;
	// last processed configuration
	private Configuration configuration;
	// last processed fast configuration page
	private Page page;
	// properties to insert to configuration on load, in runtime
	private WidgetRuntimeConfigurationPatch configurationPatch;

	@Autowired
	private com.arrow.dashboard.widget.configuration.ConfigurationPersistence configurationPersistence;

	public void assignConfigurationTopicProviders(ConfigurationTopic configurationTopic,
			FastConfigurationTopic fastConfigurationTopic) {
		this.configurationTopic = configurationTopic;
		this.fastConfigurationTopic = fastConfigurationTopic;
	}

	/**
	 * Method to init configuration manager
	 * <p>
	 * It detects configuration methods in widget and initialize handlers for
	 * configuration processing
	 * 
	 * @param widgetRuntimeInstance
	 */
	public void init(WidgetRuntimeInstance widgetRuntimeInstance, WidgetRuntimeConfigurationPatch configurationPatch) {
		Assert.notNull(widgetRuntimeInstance, "widgetRuntimeInstance is null");

		String method = "init";
		logInfo(method, "...");

		widgetId = widgetRuntimeInstance.getWidgetId();
		widgetInstanceObject = widgetRuntimeInstance.getInstance();
		widgetDescription = widgetRuntimeInstance.getWidgetRuntimeDefinition().getWidgetName() + " ["
				+ widgetRuntimeInstance.getWidgetRuntimeId() + "] for user "
				+ widgetRuntimeInstance.getUserRuntime().getLogin();

		logDebug(method, "Configuration manager start init for widget instance " + widgetDescription);

		this.configurationPatch = configurationPatch;

		if (this.configurationPatch != null && this.configurationPatch.getProperties() != null) {
			logDebug(method, "Configuration manager will use configuration patch: " + this.configurationPatch);
		}

		Class<?> cs = widgetRuntimeInstance.getWidgetRuntimeDefinition().getWidgetClass();

		Optional<Method> configurationRequest = Arrays.asList(cs.getDeclaredMethods()).stream()
				.filter(m -> m.isAnnotationPresent(ConfigurationRequest.class)).findFirst();
		logDebug(method, "widget instance " + widgetRuntimeInstance.getWidgetRuntimeId()
				+ (configurationRequest.isPresent() ? "has" : "has no") + " @ConfigurationRequest method");

		if (configurationRequest.isPresent()) {
			this.configurationMethod = configurationRequest.get();

			pagedMethods = new HashMap<>();
			namedMethods = new HashMap<>();
			namedPagedMethods = new HashMap<>();

			Arrays.asList(cs.getDeclaredMethods()).stream()
					.filter(m -> m.isAnnotationPresent(ConfigurationPageRequest.class)).forEach(m -> {
						ConfigurationPageRequest annotation = m.getAnnotation(ConfigurationPageRequest.class);
						int page = annotation.page();
						String name = annotation.configurationName();

						if (page == 0 && name.equals("")) {
							// common method
							commonMethod = m;
						} else if (page != 0 && name.equals("")) {
							pagedMethods.put(String.valueOf(page), m);
						} else if (page == 0 && !name.equals("")) {
							namedMethods.put(name, m);
						} else {
							namedPagedMethods.put(String.format(KEY_DELIMITER, page, name), m);
						}
					});

			Optional<Method> configurationChangedMethod = Arrays.asList(cs.getDeclaredMethods()).stream()
					.filter(m -> m.isAnnotationPresent(ConfigurationChanged.class)).findFirst();
			if (configurationChangedMethod.isPresent()) {
				this.configurationChangedMethod = configurationChangedMethod.get();
			}

			Optional<Method> fastConfiguration = Arrays.asList(cs.getDeclaredMethods()).stream()
					.filter(m -> m.isAnnotationPresent(FastConfigurationRequest.class)).findFirst();
			if (fastConfiguration.isPresent()) {
				fastConfigurationMethod = fastConfiguration.get();
			}

			Optional<Method> fastConfigurationRequest = Arrays.asList(cs.getDeclaredMethods()).stream()
					.filter(m -> m.isAnnotationPresent(FastConfigurationPageRequest.class)).findFirst();
			if (fastConfigurationRequest.isPresent()) {
				fastConfigurationPageMethod = fastConfigurationRequest.get();
			}

			Optional<Method> persistedConfiguration = Arrays.asList(cs.getDeclaredMethods()).stream()
					.filter(m -> m.isAnnotationPresent(ConfigurationPersistence.class)).findFirst();
			if (persistedConfiguration.isPresent()) {
				persistedConfigurationMethod = persistedConfiguration.get();
			}
		}
	}

	/**
	 * Method that will find persisted configuration and provide it to widget's
	 * {@link ConfigurationPersistence} annotated method
	 */
	public void loadWidgetConfiguration() {
		String method = "loadWidgetConfiguration";
		logInfo(method, "...");
		logDebug(method, "loading persisted configuration...");

		Configuration configuration = configurationPersistence.readConfiguration(widgetId);

		if (configuration != null && persistedConfigurationMethod != null) {
			logDebug(method, "Attempting to send configuration to widget method. method=%s",
					persistedConfigurationMethod.getName());
			try {
				configuration = patchConfiguration(configuration, configurationPatch);
				persistedConfigurationMethod.invoke(widgetInstanceObject, configuration);
				logDebug(method, "Configuration has been provided to widget.");
			} catch (Throwable t) {
				t.printStackTrace();
				logError(method, "Failed to provide configuration to widget! exception: %s ", t.getMessage());
				logTrace(method, "Failed to provide configuration to widget! exception: %s ", t);
			}
		}
	}

	/**
	 * Method to process incoming request of initial configuration from the
	 * widget
	 * <p>
	 * Configuration manager will call appropriate method annotated by
	 * {@link ConfigurationRequest}
	 * 
	 * FIXME: case when several pages opened and user press configuration button
	 * on different pages
	 * 
	 * @param configurationTopicProvider
	 */
	public void handleConfigurationRequest() {
		String method = "handleConfigurationRequest";
		logInfo(method, "...");
		logDebug(method, "Configuration manager for widget " + widgetDescription + " handling configuration request");

		Configuration configuration = null;
		if (configurationMethod == null) {
			logDebug(method, "Widget " + widgetDescription + " has no configuration method implementation");
			configuration = buildEmptyConfiguration();
		} else {
			try {
				configuration = (Configuration) configurationMethod.invoke(widgetInstanceObject);
			} catch (Throwable e) {
				// do not allow any exception be thrown from configuration
				// manager calls
				logError(method, "Failed to handle configuration request ", e);
				configuration = buildFailedConfiguration(
						"Failed to process request due to exception " + e.getMessage());
			}
		}

		this.configurationTopic.send(configuration);
		this.configuration = configuration;
	}

	/**
	 * Method to process incoming request with modified configuration
	 * <p>
	 * Configuration manager will call appropriate method annotated by
	 * {@link ConfigurationPageRequest}
	 * 
	 * @param configuration
	 */
	public void handleConfigurationPageRequest(Configuration changedConfiguration) {
		Assert.notNull(changedConfiguration, "changedConfiguration is null");

		String method = "handleConfigurationPageRequest";
		logInfo(method, "...");
		logDebug(method,
				"Configuration manager for widget " + widgetDescription + " handling configuration page request");

		ConfigurationUtils.processChangesInConfiguration(changedConfiguration, this.configuration);

		int page = configuration.getCurrentPage();
		int changedPage = configuration.getChangedPage();
		String name = configuration.getName();

		if (changedPage != page) {
			callConfigurationMethod(configurationChangedMethod, configuration);
		} else {
			String namedPagedKey = String.format(KEY_DELIMITER, page, name);

			if (namedPagedMethods.containsKey(namedPagedKey)) {
				logDebug(method, "ConfigurationManager for widget " + widgetId + " call namedPagedMethod");
				callConfigurationMethod(namedPagedMethods.get(namedPagedKey), configuration);
			} else if (namedMethods.containsKey(name)) {
				logDebug(method, "ConfigurationManager for widget " + widgetId + " call namedMethods");
				callConfigurationMethod(namedMethods.get(name), configuration);
			} else if (pagedMethods.containsKey(String.valueOf(page))) {
				logDebug(method, "ConfigurationManager for widget " + widgetId + " call pagedMethods");
				callConfigurationMethod(pagedMethods.get(String.valueOf(page)), configuration);
			} else if (commonMethod != null) {
				logDebug(method, "ConfigurationManager for widget " + widgetId + " call commonMethod");
				callConfigurationMethod(commonMethod, configuration);
			} else {
				logError(method, "ConfigurationManager for widget " + widgetId
						+ " did not find method to handle configuration page request");
				configurationTopic.send(buildFailedConfiguration("failed to find handler"));
			}
		}
	}

	public void handleConfigurationSaveRequest(Configuration changedConfiguration, String who) {
		Assert.notNull(changedConfiguration, "changedConfiguration is null");

		String method = "";
		logInfo(method, "...");

		if (this.configuration.isClosed() && configurationMethod != null) {
			ConfigurationUtils.processChangesInConfiguration(changedConfiguration, this.configuration);
			persistClosedConfiguration(this.configuration, who);
		}
	}

	/**
	 * Method to process incoming request of fast configuration page
	 * <p>
	 * Configuration manager will call appropriate method annotated by
	 * {@link FastConfigurationRequest}
	 * 
	 * @param fastConfigurationTopicProvider
	 */
	public void handleFastConfigurationRequest() {
		String method = "handleFastConfigurationRequest";
		logInfo(method, "...");
		logDebug(method,
				"Configuration manager for widget " + widgetDescription + " handling fast configuration request");

		Page page = null;
		try {
			page = (Page) fastConfigurationMethod.invoke(widgetInstanceObject);
			logDebug(method,
					"Configuration manager for widget " + widgetDescription + " handled fast configuration request");
		} catch (Throwable e) {
			// do not allow any exception be thrown from configuration manager
			// calls
			page = buildFailedPage("Failed to process request due to exception " + e.getMessage());
		}

		this.fastConfigurationTopic.send(page);
		this.page = page;
	}

	/**
	 * Method to process incoming request with modified fast configuration page
	 * <p>
	 * Configuration manager will call appropriate method annotated by
	 * {@link FastConfigurationPageRequest}
	 * 
	 * @param page
	 */
	public void handleFastConfigurationPageRequest(Page page) {
		Assert.notNull(page, "page is null");

		String method = "handleFastConfigurationPageRequest";
		logInfo(method, "...");
		logDebug(method,
				"Configuration manager for widget " + widgetDescription + " handling fast configuration page request");

		ConfigurationUtils.processChangesInPage(page, this.page);

		Page newPage = null;
		try {
			newPage = (Page) fastConfigurationPageMethod.invoke(widgetInstanceObject);
			logDebug(method, "Configuration manager for widget " + widgetDescription
					+ " handled fast configuration page request");
		} catch (Throwable e) {
			// do not allow any exception be thrown from configuration manager
			// calls
			newPage = buildFailedPage("Failed to process request due to exception " + e.getMessage());
		}

		this.fastConfigurationTopic.send(newPage);
		this.page = newPage;

	}

	/**
	 * Method to apply runtime configuration patch - replace some properties
	 * values
	 * 
	 * @param configuration
	 * @param patch
	 * @return
	 */
	private Configuration patchConfiguration(Configuration configuration, WidgetRuntimeConfigurationPatch patch) {
		String method = "patchConfiguration";
		logInfo(method, "patch: "+patch);
		
		if (patch == null || patch.getProperties() == null) {
			logDebug(method, "will not apply a patch");
			return configuration;
		}
		
		// for now, replace string values only
		for(Entry<String, String> patchProperty : patch.getProperties().entrySet()) {
			// FIXME: now, adding " is a workaround for object mapper. to correct
			ConfigurationUtils.replaceValue(configuration, patchProperty.getKey(), "\""+patchProperty.getValue()+"\"");
		}

		return configuration;
	}

	/**
	 * Method to provide closed empty configuration on request when widget has
	 * no configuration implementation
	 * 
	 * @return
	 */
	private Configuration buildEmptyConfiguration() {
		String method = "buildEmptyConfiguration";
		logInfo(method, "...");

		Configuration c = new Configuration().withName("EMPTY_CONFIGURATION").withLabel("Empty Configuration");
		c.close();
		return c;
	}

	/**
	 * this method builds empty configuration with error. front end will show
	 * this error for the user. this means configuration failed
	 * 
	 * @param error
	 * @return
	 */
	private Configuration buildFailedConfiguration(String error) {
		Assert.hasText(error, "error is empty");

		String method = "buildFailedConfiguration";
		logInfo(method, "...");
		logError(method,
				"Configuration manager for widget " + widgetDescription + " builds configuration with error" + error);

		return new Configuration().withError(error);
	}

	private Page buildFailedPage(String error) {
		Assert.hasText(error, "error is empty");

		String method = "buildFailedPage";
		logInfo(method, "...");
		logError(method, "Configuration manager for widget " + widgetDescription + " builds page with error" + error);

		return new Page().withError(error);
	}

	private void callConfigurationMethod(Method method, Configuration configuration) {
		Assert.notNull(method, "method is null");
		Assert.notNull(configuration, "configuration is null");

		String methodName = "callConfigurationMethod";
		logInfo(methodName, "...");
		logDebug(methodName, "Configuration manager for widget " + widgetDescription + " pass configuration to widget");
		logTrace(methodName, "Configuration manager for widget " + widgetDescription + " pass configuration to widget: "
				+ configuration);

		Configuration nextConfiguration = null;
		try {
			nextConfiguration = (Configuration) method.invoke(widgetInstanceObject, configuration);
		} catch (Throwable t) {
			logError(methodName, "failed to send configuration to widget ", t);
			nextConfiguration = buildFailedConfiguration(
					"Failed to pass configuration to widget with exception " + t.getMessage());
		}
		this.configuration = nextConfiguration;
		logDebug(methodName, "Configuration manager for widget " + widgetDescription
				+ " got configuration response from the widget");
		logDebug(methodName, "Configuration manager for widget " + widgetDescription
				+ " got configuration response from the widget: " + configuration);

		configurationTopic.send(nextConfiguration);
	}

	private void persistClosedConfiguration(Configuration configuration, String who) {
		Assert.notNull(configuration, "configuration is null");

		String method = "persistClosedConfiguration";
		logInfo(method, "...");
		logDebug(method, "Configuration manager is going to save closed configuration");

		configurationPersistence.saveConfiguration(widgetId, configuration, who);

		// provide configuration to widget
		if (configuration != null && persistedConfigurationMethod != null) {
			try {
				logDebug(method, "Configuration manager for widget " + widgetDescription
						+ " provides configuration to widget instance");

				persistedConfigurationMethod.invoke(widgetInstanceObject, configuration);
			} catch (Throwable t) {
				logError(method, "Configuration manager for widget " + widgetDescription
						+ " failed to provide configuration to widget instance with exception " + t.getMessage(), t);
			}
		}
	}
}
