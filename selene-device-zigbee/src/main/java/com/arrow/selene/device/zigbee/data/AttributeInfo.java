package com.arrow.selene.device.zigbee.data;

import java.io.Serializable;
import java.util.Locale;

import com.arrow.selene.device.xbee.zcl.ZclDataType;

public class AttributeInfo implements Serializable {
	private static final long serialVersionUID = -5440531645961104428L;

	private int attributeId;
	private ZclDataType attributeDataType;
	private String name;
	private AccessType accessType;
	private String value;

	public AttributeInfo() {
	}

	public AttributeInfo(int attributeId, ZclDataType attributeDataType, String name, AccessType accessType) {
		this.attributeId = attributeId;
		this.attributeDataType = attributeDataType;
		if (name == null || name.isEmpty()) {
			name = "Unknown attribute";
		}
		this.name = name;
		this.accessType = accessType;
	}

	public int getAttributeId() {
		return attributeId;
	}

	public void setAttributeId(short attributeId) {
		this.attributeId = attributeId;
	}

	public ZclDataType getAttributeDataType() {
		return attributeDataType;
	}

	public void setAttributeDataType(ZclDataType attributeDataType) {
		this.attributeDataType = attributeDataType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AccessType getAccessType() {
		return accessType;
	}

	public void setAccessType(AccessType accessType) {
		this.accessType = accessType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return String.format(Locale.getDefault(), "0x%04x (%s) %s %s", attributeId, name, attributeDataType,
				accessType);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		AttributeInfo attribute = (AttributeInfo) o;

		return attributeId == attribute.attributeId;
	}

	@Override
	public int hashCode() {
		return attributeId;
	}
}
