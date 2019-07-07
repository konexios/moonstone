package com.arrow.dashboard.messaging;

import java.lang.reflect.Method;
import java.util.Optional;

import org.springframework.util.Assert;

import com.arrow.dashboard.exception.MessagingException;
import com.arrow.dashboard.messaging.topic.DeleteWidgetTopicMessaging;
import com.arrow.dashboard.messaging.topic.TopicProviderMessaging;
import com.arrow.dashboard.messaging.topic.TopicProviderMessenger;
import com.arrow.dashboard.runtime.model.AbstractExceptionable;
import com.arrow.dashboard.runtime.model.MessageEndpoint;
import com.arrow.dashboard.runtime.model.WidgetDefinitionException;
import com.arrow.dashboard.runtime.model.WidgetRuntimeInstance;
import com.arrow.dashboard.widget.configuration.Configuration;
import com.arrow.dashboard.widget.configuration.ConfigurationManager;
import com.arrow.dashboard.widget.configuration.Page;
import com.arrow.dashboard.widget.configuration.messaging.ConfigurationTopic;
import com.arrow.dashboard.widget.configuration.messaging.ConfigurationTopicProvider;
import com.arrow.dashboard.widget.configuration.messaging.FastConfigurationTopic;
import com.arrow.dashboard.widget.configuration.messaging.FastConfigurationTopicProvider;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Router is responsible to manage message routing<br>
 * Basic example: user opened 10 web pages with the same dashboard<br>
 * There is one back end widget but 10 front end ones<br>
 * So, we need a way to route message from 10 front end endpoints to one back
 * end widget and vice versa: if back end widget publish a message - we route it
 * to all front end widgets<br>
 * <p>
 * A separate router also will allow to control messaging flows<br>
 * It is supposed to create one router for each back end widget instance to
 * manage only its messaging
 * 
 * @author dantonov
 *
 */
public class Router extends AbstractExceptionable {

	/**
	 * target widget instance to manage routes for
	 */
	private WidgetRuntimeInstance widgetRuntimeInstance;
	private TopicProviderMessaging topicProviderMessaging;
	private ConfigurationManager configurationManager;
	private ConfigurationTopicProvider configurationTopicProvider;
	private FastConfigurationTopicProvider fastConfigurationTopicProvider;
	private DeleteWidgetTopicMessaging deleteWidgetTopicMessaging;

	/**
	 * Router constructor<br>
	 * 
	 * @param widgetRuntimeInstance
	 *            widget instance to 'register' potential routes by analyzing
	 *            message endpoints and topic providers
	 * @param configurationManager
	 * @param messenger
	 */
	public Router(WidgetRuntimeInstance widgetRuntimeInstance, ConfigurationManager configurationManager,
	        TopicProviderMessenger messenger) {
		super();
		this.configurationManager = configurationManager;
		this.widgetRuntimeInstance = widgetRuntimeInstance;

		String method = "Router";

		configurationTopicProvider = new ConfigurationTopicProvider(widgetRuntimeInstance.getWidgetRuntimeId(),
		        messenger);
		fastConfigurationTopicProvider = new FastConfigurationTopicProvider(widgetRuntimeInstance.getWidgetRuntimeId(),
		        messenger);
		topicProviderMessaging = new TopicProviderMessaging(widgetRuntimeInstance.getWidgetRuntimeId(), messenger);
		deleteWidgetTopicMessaging = new DeleteWidgetTopicMessaging(widgetRuntimeInstance.getWidgetRuntimeId(),
		        messenger);

		logDebug(method, "Router has been created for for widget " + widgetRuntimeInstance.getWidgetRuntimeId());

		// assign topic providers
		widgetRuntimeInstance.getWidgetRuntimeDefinition().getFieldTopicProviders().stream().forEach(ftp -> {
			try {
				ftp.getField().set(widgetRuntimeInstance.getInstance(), new SimpleTopicProvider() {
			        @Override
			        public void send(Object object) throws MessagingException {
				        // we need to route message to each dashboard
				        routeOutgoingMessage(ftp.getTopic(), object);
			        }
		        });
			} catch (IllegalArgumentException | IllegalAccessException e) {
				exception = new WidgetDefinitionException("Failed to assign topic provider", e);
			}
		});

		if (configurationManager != null) {

			// assign configuration topic providers
			configurationManager.assignConfigurationTopicProviders(new ConfigurationTopic() {

				@Override
				public void send(Configuration configuraion) {
					routeOutgogingConfiguration(configuraion);
				}
			}, new FastConfigurationTopic() {

				@Override
				public void send(Page page) {
					routeOutgogingFastConfiguration(page);
				}
			});

			configurationManager.loadWidgetConfiguration();
		}
	}

	private void routeOutgoingMessage(String topic, Object object) {
		Assert.hasText(topic, "topic is empty");
		Assert.notNull(object, "object is null");

		String method = "routeOutgoingMessage";
		logInfo(method, "...");
		logDebug(method, "topic: %s, widgetId: %s, widgetRuntimeId: %s", topic, widgetRuntimeInstance.getWidgetId(),
		        widgetRuntimeInstance.getWidgetRuntimeId());

		// TODO revisit, widget can't be found
		try {
			topicProviderMessaging.send(widgetRuntimeInstance.getParentRuntimeId(), topic, object);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	private void routeOutgogingConfiguration(Configuration configuration) {
		Assert.notNull(configuration, "configuration is null");

		String method = "routeOutgogingConfiguration";
		logInfo(method, "...");
		logDebug(method, "widgetId: %s, widgetRuntimeId: %s", widgetRuntimeInstance.getWidgetId(),
		        widgetRuntimeInstance.getWidgetRuntimeId());

		// send configuration to each parent runtime
		logDebug(method, "Sending configuration! parentRuntimeId: %s, widgetRuntimeId: %s",
		        widgetRuntimeInstance.getParentRuntimeId(), widgetRuntimeInstance.getWidgetRuntimeId());
		configurationTopicProvider.send(configuration, widgetRuntimeInstance.getParentRuntimeId());
	}

	private void routeOutgogingFastConfiguration(Page page) {
		Assert.notNull(page, "page is null");

		String method = "routeOutgogingFastConfiguration";
		logInfo(method, "...");
		logDebug(method, "widgetId: %s, widgetRuntimeId: %s", widgetRuntimeInstance.getWidgetId(),
		        widgetRuntimeInstance.getWidgetRuntimeId());

		logDebug(method, "Sending page! boardRuntimeId: %s, widgetId: %s", widgetRuntimeInstance.getParentRuntimeId(),
		        widgetRuntimeInstance.getWidgetRuntimeId());
		fastConfigurationTopicProvider.send(page, widgetRuntimeInstance.getParentRuntimeId());
	}

	/**
	 * Method to route incoming message
	 * 
	 * FIXME exception handling
	 * 
	 * @param endpoint
	 * @param message
	 */
	public void routeMessage(String endpoint, String message) {
		Assert.hasText(endpoint, "endpoint is empty");
		// MESSAGE MAY BE EMPTY! this is not a requirementAssert.hasText(message, "message is empty");

		String methodName = "routeMessage";
		logInfo(methodName, "...");
		logDebug(methodName, "widgetId: %s, endpoint: %s, message: %s", widgetRuntimeInstance.getWidgetRuntimeId(),
		        endpoint, message);

		try {
			// if method is answering - call routeOutgoingMessage()

			Optional<MessageEndpoint> messageEnpoint = widgetRuntimeInstance.getWidgetRuntimeDefinition()
			        .getMessageEndpoints().stream().filter(me -> me.getEndpoint().equals(endpoint)).findFirst();

			if (messageEnpoint.isPresent()) {
				MessageEndpoint endpointInstance = messageEnpoint.get();

				Object target = widgetRuntimeInstance.getInstance();

				Method method = endpointInstance.getMethod();
				Object result = null;
				if (message == null) {
					if (method.getReturnType().equals(Void.TYPE)) {
						method.invoke(target);
					} else {
						result = method.invoke(target);
					}
				} else {
					ObjectMapper mapper = new ObjectMapper();
					Class<?> cs = endpointInstance.getParameterObjectClass().get();
					logDebug(methodName, "parameter class: " + cs.getName());
					logDebug(methodName, "message class is " + message.getClass().getName());
					if (method.getReturnType().equals(Void.TYPE)) {
						method.invoke(target, mapper.readValue(message, cs));
					} else {
						result = method.invoke(target, mapper.readValue(message, cs));
					}
				}

				if (result != null) {
					routeOutgoingMessage(endpointInstance.getTopicProvider().getTopic(), result);
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}

	}

	/**
	 * Method to route configuration request for the widget
	 */
	public void routeConfigurationRequest() {
		String method = "routeConfigurationRequest";
		logInfo(method, "...");
		logDebug(method, "widgetId: %s", widgetRuntimeInstance.getWidgetRuntimeId());

		configurationManager.handleConfigurationRequest();
	}

	/**
	 * Method to route configuration page request for the widget
	 */
	public void routeConfigurationPageRequest(Configuration configuration) {
		Assert.notNull(configuration, "configuration is null");

		String method = "routeConfigurationPageRequest";
		logInfo(method, "...");
		logDebug(method, "widgetId: %s", widgetRuntimeInstance.getWidgetRuntimeId());

		configurationManager.handleConfigurationPageRequest(configuration);
	}

	public void routeConfigurationSaveRequest(Configuration configuration, String who) {
		Assert.notNull(configuration, "configuration is null");

		String method = "routeConfigurationSaveRequest";
		logInfo(method, "...");
		logDebug(method, "widgetId: %s", widgetRuntimeInstance.getWidgetRuntimeId());

		configurationManager.handleConfigurationSaveRequest(configuration, who);
	}

	/**
	 * Method to route fast configuration request for the widget
	 */
	public void routeFastConfigurationRequest() {
		String method = "routeFastConfigurationRequest";
		logInfo(method, "...");
		logDebug(method, "widgetId: %s", widgetRuntimeInstance.getWidgetRuntimeId());

		configurationManager.handleFastConfigurationRequest();
	}

	/**
	 * Method to route fast configuration page request for the widget
	 */
	public void routeFastConfigurationPageRequest(Page page) {
		String method = "routeFastConfigurationPageRequest";
		logInfo(method, "...");
		logDebug(method, "widgetId: %s", widgetRuntimeInstance.getWidgetRuntimeId());

		configurationManager.handleFastConfigurationPageRequest(page);
	}

	/**
	 * Method to route deletion request for the widget
	 */
	public void routeDeleteRequest() {
		String method = "routeDeleteRequest";
		logInfo(method, "...");
		logDebug(method, "widgetId: %s", widgetRuntimeInstance.getWidgetRuntimeId());

		logDebug(method, "Sending delete request! parentRuntimeId: %s, widgetRuntimeId: %s",
		        widgetRuntimeInstance.getParentRuntimeId(), widgetRuntimeInstance.getWidgetRuntimeId());
		deleteWidgetTopicMessaging.send(widgetRuntimeInstance.getParentRuntimeId());
	}

	/**
	 * Method to close route
	 */
	public void close() {
		// TODO: check if something is needed
	}
}