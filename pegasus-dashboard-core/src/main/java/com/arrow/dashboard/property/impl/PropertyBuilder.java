package com.arrow.dashboard.property.impl;

import com.arrow.dashboard.property.Controller;
import com.arrow.dashboard.property.Property;

public abstract class PropertyBuilder<P extends Property<M, V, C>, M, V, C extends Controller<M>> {
	private M value;
	private V view;
	private C controller;
	private Class<P> propertyType;

	public PropertyBuilder(Class<P> propertyType, C controller) {
		this.propertyType = propertyType;
		this.controller = controller;
	}

	public PropertyBuilder<P, M, V, C> withValue(M value) {
		this.value = value;
		return this;
	}

	public PropertyBuilder<P, M, V, C> withView(V view) {
		this.view = view;
		return this;
	}

	public Property<M, V, C> build() {
		PropertyImpl<M, V, C> propertyImpl = new PropertyImpl<>(value, view, controller, propertyType);
		return propertyImpl;
	}
}
