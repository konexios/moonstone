package com.arrow.selene.device.sensor;

import org.apache.commons.lang3.Validate;

public abstract class SensorDataAbstract<Type> implements SensorData<Type> {
	private final String name;
	private final Type data;

	public SensorDataAbstract(String name, Type data) {
		Validate.notBlank(name, "name is empty");
		Validate.notNull(data, "data is null");
		this.name = name;
		this.data = data;
	}

	@Override
	public String getName() {
		return name;
	}

	public Type getData() {
		return data;
	}
}
