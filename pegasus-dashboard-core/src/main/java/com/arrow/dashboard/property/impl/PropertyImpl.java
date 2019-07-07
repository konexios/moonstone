package com.arrow.dashboard.property.impl;

import com.arrow.dashboard.property.Controller;
import com.arrow.dashboard.property.Property;

// FIXME: visibility
public class PropertyImpl<M, V, C extends Controller<M>> implements Property<M, V, C> {

	private M value;
	private V view;
	private C controller;
	private Class<? extends Property<M, V, C>> propertyType;

	// FIXME: visibility
	public PropertyImpl(M value, V view, C controller, Class<? extends Property<M, V, C>> propertyType) {
		super();
		this.value = value;
		this.view = view;
		this.controller = controller;
		this.propertyType = propertyType;
	}

	@Override
	public Class<? extends Property<M, V, C>> getType() {
		return propertyType;
	}

	@Override
	public M getValue() {
		return value;
	}

	@Override
	public V getView() {
		return view;
	}

	@Override
	public C getController() {
		return controller;
	}

}
