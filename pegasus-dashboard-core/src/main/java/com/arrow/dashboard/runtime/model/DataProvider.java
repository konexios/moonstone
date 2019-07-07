package com.arrow.dashboard.runtime.model;

import java.lang.reflect.Field;

/**
 * Represents a field, inside the widget, annotated by
 * {@link com.arrow.dashboard.widget.annotation.data.DataProvider}<br>
 * 
 * @author dantonov
 *
 */
public class DataProvider {

	private Field field;

	public DataProvider(Field field) {
		field.setAccessible(true);
		this.field = field;
	}

	public Field getField() {
		return field;
	}

}
