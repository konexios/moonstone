package com.arrow.dashboard.properties.integer;

import com.arrow.dashboard.property.impl.SimplePropertyBuilder;

/**
 * Property builder definition for {@link IntegerProperty}
 * 
 * @author dantonov
 *
 */
public class IntegerPropertyBuilder extends SimplePropertyBuilder<IntegerProperty, Integer, IntegerPropertyView> {

	public IntegerPropertyBuilder() {
		super(IntegerProperty.class);
	}
}