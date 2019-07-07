package com.arrow.kronos.data.action;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ParameterValidation implements Serializable {

	private static final long serialVersionUID = -1077243995732231718L;

	@JsonFormat(shape = JsonFormat.Shape.STRING)
	public enum ValidationType {
		STRING("String"), MULTILINE_STRING("Multiline string"), SELECT("Select"), JSON("JSON"), XML("XML"), HTML(
		        "HTML"), KEY_VALUE_PAIRS("Key-value pairs"), EMAIL("E-mail");

		private String value;

		private ValidationType(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public String getName() {
			return name();
		}

		public static ValidationType fromValue(String value) {
			if (value != null) {
				for (ValidationType item : ValidationType.values()) {
					if (item.getValue().equals(value)) {
						return item;
					}
				}
			}
			throw new IllegalArgumentException("Unsupported AttributeType");
		}
	}

	@NotNull
	private ValidationType type = ValidationType.STRING;
	private String defaultValue;
	private String data;

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public ValidationType getType() {
		return type;
	}

	public void setType(ValidationType type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((defaultValue == null) ? 0 : defaultValue.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ParameterValidation other = (ParameterValidation) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (defaultValue == null) {
			if (other.defaultValue != null)
				return false;
		} else if (!defaultValue.equals(other.defaultValue))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
}
