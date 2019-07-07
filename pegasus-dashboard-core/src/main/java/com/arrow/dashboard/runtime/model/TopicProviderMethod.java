package com.arrow.dashboard.runtime.model;

import java.lang.reflect.Method;

import com.arrow.dashboard.widget.annotation.messaging.TopicProvider;

/**
 * Represents method annotated by {@link TopicProvider}
 * 
 * @author dantonov
 *
 */
public class TopicProviderMethod {

	private String topic;
	private Class<?> returnType;

	public String getTopic() {
		return topic;
	}

	public Class<?> getReturnType() {
		return returnType;
	}

	public TopicProviderMethod(Method method) {
		TopicProvider annotation = method.getAnnotation(TopicProvider.class);
		topic = annotation.value().replaceAll("^/", "");
		returnType = method.getReturnType();
	}
}
