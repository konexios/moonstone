package com.arrow.kronos.data;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.arrow.acn.client.model.DeviceStateValueType;

public class DeviceStateValueMetadata implements Serializable {
	private static final long serialVersionUID = 2160270507833662715L;

	@NotBlank
	private String name;
	private String description;
	@NotNull
	private DeviceStateValueType type = DeviceStateValueType.String;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public DeviceStateValueType getType() {
		return type;
	}

	public void setType(DeviceStateValueType type) {
		this.type = type;
	}
}
