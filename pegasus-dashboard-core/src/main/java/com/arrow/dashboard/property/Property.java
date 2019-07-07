package com.arrow.dashboard.property;

import com.arrow.dashboard.property.impl.PropertyDeserializer;
import com.arrow.dashboard.widget.annotation.property.PropertyView;
import com.arrow.dashboard.widget.annotation.property.Versioned;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Property interface
 * <p>
 * Represents a property to be managed widget in configuration and to be
 * configured by the user as part of widget configuration
 * 
 * @author dantonov
 *
 * @param <M>
 *            model object type that keeps a value
 * @param <V>
 *            view type
 * @param <C>
 *            {@link Controller} type for this property
 */
@JsonDeserialize(using = PropertyDeserializer.class)
public interface Property<M, V, C extends Controller<M>> {

	default int getVersion() {
		Versioned versioned = getType().getAnnotation(Versioned.class);
		return versioned == null ? 0 : versioned.value();
	}

	@JsonIgnore
	Class<? extends Property<M, V, C>> getType();

	M getValue();

	V getView();

	default String getViewId() {
		PropertyView view = getView().getClass().getAnnotation(PropertyView.class);
		return view.value();
	}

	@JsonIgnore
	C getController();
}
