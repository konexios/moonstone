package com.arrow.dashboard.runtime.model;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Represents {@link com.arrow.dashboard.widget.annotation.messaging.MessageEndpoint} in
 * widget
 * 
 * @author dantonov
 *
 */
public class MessageEndpoint extends AbstractExceptionable {

	private Method method;
	private String endpoint;
	private Optional<Class<?>> parameterObjectClass;
	private Optional<TopicProviderMethod> topicProvider;

	public String getEndpoint() {
		return endpoint;
	}

	@JsonIgnore
	public Method getMethod() {
		return method;
	}

	public boolean isAnswering() {
		return topicProvider.isPresent();
	}

	public TopicProviderMethod getTopicProvider() {
		return topicProvider.orElse(null);
	}

	public boolean isParametrizied() {
		return parameterObjectClass.isPresent();
	}

	@JsonIgnore
	public Optional<Class<?>> getParameterObjectClass() {
		return parameterObjectClass;
	}

	public MessageEndpoint(Method method) {
		try {
			this.method = method;
			com.arrow.dashboard.widget.annotation.messaging.MessageEndpoint annotation = method
			        .getAnnotation(com.arrow.dashboard.widget.annotation.messaging.MessageEndpoint.class);
			endpoint = annotation.value().replaceAll("^/", "");
			parameterObjectClass = Arrays.asList(method.getParameterTypes()).stream().findFirst();
			topicProvider = Optional
			        .ofNullable(method.isAnnotationPresent(com.arrow.dashboard.widget.annotation.messaging.TopicProvider.class)
			                ? new TopicProviderMethod(method) : null);
		} catch (Throwable t) {
			// TODO: improve exception catching to provide more details
			exception = new WidgetDefinitionException(
			        "Failed to load message endpoint [" + method.getName() + "] due to exception " + t.getMessage(), t);
		}
	}

}
