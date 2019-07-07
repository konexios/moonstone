package com.arrow.dashboard.property.impl;

import com.arrow.dashboard.property.SimpleProperty;

public abstract class SimplePropertyBuilder<P extends SimpleProperty<M, V>, M, V>
        extends PropertyBuilder<P, M, V, DefaultController<M>> {

	public SimplePropertyBuilder(Class<P> propertyType) {
		super(propertyType, new DefaultController<>());
	}

}
