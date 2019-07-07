package com.arrow.dashboard.property.impl;

import java.io.IOException;

import com.arrow.dashboard.property.Controller;
import com.arrow.dashboard.property.Property;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.IntNode;

/**
 * Special deserializer for property object to convert JSON representation to
 * {@link Property} instance
 * 
 * @author dantonov
 *
 */
public class PropertyDeserializer extends StdDeserializer<Property<?, ?, ?>> {

	public PropertyDeserializer() {
		this(null);
	}

	public PropertyDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public Property<String, String, Controller<String>> deserialize(JsonParser jp, DeserializationContext ctxt)
	        throws IOException, JsonProcessingException {

		JsonNode node = jp.getCodec().readTree(jp);

		String viewId = node.get("viewId").asText();
		int version = (Integer) ((IntNode) node.get("version")).numberValue();

		String valueAsText = node.get("value") == null ? null : node.get("value").toString();
		String viewAsText = node.get("view") == null ? null : node.get("view").toString();

		return new DeserializedProperty(version, viewId, valueAsText, viewAsText);
	}

	public static class DeserializedProperty implements Property<String, String, Controller<String>> {

		int version;
		String viewId;
		String valueAsText;
		String viewAsText;

		public DeserializedProperty(int version, String viewId, String valueAsText, String viewAsText) {
			super();
			this.version = version;
			this.viewId = viewId;
			this.valueAsText = valueAsText;
			this.viewAsText = viewAsText;
		}

		@Override
		public int getVersion() {
			// TODO Auto-generated method stub
			return version;
		}

		@Override
		public Class<? extends Property<String, String, Controller<String>>> getType() {
			// TODO Auto-generated method stub
			return DeserializedProperty.class;
		}

		@Override
		public String getValue() {
			// TODO Auto-generated method stub
			return valueAsText;
		}

		@Override
		public String getView() {
			// TODO Auto-generated method stub
			return viewAsText;
		}

		@Override
		public Controller<String> getController() {
			// TODO Auto-generated method stub
			return null;
		}
	}

}