package com.arrow.dashboard.runtime.model;

import java.lang.reflect.Field;

import com.arrow.dashboard.widget.annotation.messaging.TopicProvider;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Represents field annotated by {@link TopicProvider}
 * 
 * @author dantonov
 *
 */
public class TopicProviderField {

	private String topic;
	private Field field;

	public String getTopic() {
		return topic;
	}

	@JsonIgnore
	public Field getField() {
		return field;
	}

	public TopicProviderField(Field field) {
		field.setAccessible(true);
		this.field = field;
		TopicProvider annotation = field.getAnnotation(TopicProvider.class);
		topic = annotation.value().replaceAll("^/", "");
	}
}
